package com.github.ocraft.s2client.bot.gateway.impl;

/*-
 * #%L
 * ocraft-s2client-bot
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import SC2APIProtocol.Raw;
import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.api.OcraftApiConfig;
import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.ClientEvents;
import com.github.ocraft.s2client.bot.GameServerResponses;
import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.gateway.AppState;
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.bot.setting.InterfaceSettings;
import com.github.ocraft.s2client.bot.setting.PlayerSettings;
import com.github.ocraft.s2client.protocol.action.Action;
import com.github.ocraft.s2client.protocol.action.raw.ActionRaw;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatial;
import com.github.ocraft.s2client.protocol.data.Upgrades;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.MultiplayerOptions;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.observation.*;
import com.github.ocraft.s2client.protocol.observation.raw.*;
import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponseObservation;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import com.github.ocraft.s2client.protocol.score.Score;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.unit.Unit;
import com.github.ocraft.s2client.test.TemporaryFolder;
import com.github.ocraft.s2client.test.TemporaryFolderExtension;
import com.typesafe.config.ConfigFactory;
import io.reactivex.Maybe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.github.ocraft.s2client.bot.Fixtures.*;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.Tag("integration")
@ExtendWith(TemporaryFolderExtension.class)
class ControlInterfaceImplIT {

    private static final String TEST_MAP = "test.SC2Map";

    private TemporaryFolder replayDir;

    @Test
    void providesErrorHandlingForProtocolCommunication() {
        GameSetup gameSetup = new GameSetup().withLimitedCorrectPingResponses(2).start();

        assertThat(gameSetup.control().ping()).as("ping request status").isFalse();

        gameSetup.assertThatErrorsWasEmitted(
                new ClientError[]{ClientError.SC2_PROTOCOL_ERROR},
                new String[]{GameServerResponses.PING_ERROR});

        gameSetup.stop();
    }

    @Test
    void providesErrorHandlingForInvalidResponse() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(
                Sc2Api.Request::hasObservation,
                () -> Sc2Api.Response.newBuilder()
                        .setObservation(Sc2Api.ResponseObservation.newBuilder().build())
                        .build());

        assertThat(gameSetup.control().getObservation()).as("observation request status").isFalse();

        gameSetup.assertThatErrorsWasEmitted(
                new ClientError[]{ClientError.INVALID_RESPONSE},
                new String[]{});

        gameSetup.stop();
    }

    @Test
    void throwsExceptionWhenWaitingForResponseAndApplicationStateIsNotNormal() {
        GameSetup gameSetup = new GameSetup().inState(AppState.TIMEOUT).start();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(gameSetup.control()::ping)
                .withMessage("Invalid application state: TIMEOUT");

        gameSetup.stop();
    }

    @Test
    void changesApplicationStateToTimeoutWhenGameServerDoesNotRespond() {
        GameSetup gameSetup = new GameSetup().stopAfterConnection().requestTimeout(1000).start();

        assertThat(gameSetup.control().ping()).as("ping request status").isFalse();

        gameSetup.assertThatAppStateIs(AppState.TIMEOUT, "application status after timeout");
        gameSetup.assertThatErrorsWasEmitted(
                new ClientError[]{ClientError.CONNECTION_CLOSED, ClientError.SC2_PROTOCOL_TIMEOUT},
                new String[]{});

        gameSetup.stop();
    }

    @Test
    void changesApplicationStateToTimeoutZombieWhenGameServerDoesNotRespondAndProcessTerminationFailed() {
        GameSetup gameSetup = new GameSetup()
                .withS2Controller(mockZombieGameProcess())
                .requestTimeout(100)
                .start();

        gameSetup.unresponsivePing(2);

        assertThat(gameSetup.control().ping()).as("ping request status").isFalse();

        gameSetup.assertThatAppStateIs(
                AppState.TIMEOUT_ZOMBIE, "application status when game and process is unresponsive");
        gameSetup.assertThatErrorsWasEmitted(
                new ClientError[]{ClientError.SC2_PROTOCOL_TIMEOUT, ClientError.SC2_APP_FAILURE},
                new String[]{});

        gameSetup.stop();
    }

    private S2Controller mockZombieGameProcess() {
        S2Controller s2ControllerMock = mock(S2Controller.class);
        when(s2ControllerMock.getConfig()).thenReturn(ConfigFactory.parseMap(Map.of(
                OcraftApiConfig.GAME_NET_IP, "127.0.0.1",
                OcraftApiConfig.GAME_NET_PORT, GameSetup.GAME_SERVER_PORT,
                OcraftApiConfig.GAME_EXE_PATH, "/test/sc2/Versions/Base65895/SC2.exe",
                OcraftApiConfig.GAME_EXE_ROOT, "/test/sc2"
        )));
        Process processMock = mock(Process.class);
        when(processMock.isAlive()).thenReturn(true);
        when(s2ControllerMock.getS2Process()).thenReturn(processMock);
        return s2ControllerMock;
    }

    @Test
    void emitsUnknownStatusErrorWhenGameIsResponsiveAfterSecondTry() {
        GameSetup gameSetup = new GameSetup()
                .withS2Controller(mockZombieGameProcess())
                .requestTimeout(100)
                .start();

        gameSetup.unresponsivePing(1);

        assertThat(gameSetup.control().ping()).as("ping request status").isFalse();

        gameSetup.assertThatAppStateIs(AppState.NORMAL, "application status when game is responsive after second try");
        gameSetup.assertThatErrorsWasEmitted(
                new ClientError[]{ClientError.SC2_UNKNOWN_STATUS},
                new String[]{});

        gameSetup.stop();
    }

    @Test
    void changesApplicationStateAndEmitsUnknownStatusErrorWhenGameIsResponsiveAfterSecondTryWithUnknownStatus() {
        GameSetup gameSetup = new GameSetup()
                .withS2Controller(mockZombieGameProcess())
                .requestTimeout(100)
                .start();

        gameSetup.unresponsivePingThen(
                1, () -> GameServerResponses.ping().toBuilder().setStatus(Sc2Api.Status.unknown).build());

        assertThat(gameSetup.control().ping()).as("ping request status").isFalse();

        gameSetup.assertThatAppStateIs(
                AppState.TIMEOUT_ZOMBIE, "application status when game and process is unresponsive");
        gameSetup.assertThatErrorsWasEmitted(
                new ClientError[]{ClientError.SC2_UNKNOWN_STATUS, ClientError.SC2_APP_FAILURE},
                new String[]{});

        gameSetup.stop();
    }

    @Test
    void handlesCreateGameOnLocalMapCommunication() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasCreateGame, GameServerResponses::createGame);

        assertThat(gameSetup.control().createGame(
                LocalMap.of(Paths.get(TEST_MAP)),
                List.of(PlayerSettings.participant(Race.RANDOM, mock(S2Agent.class))), true)
        ).as("create game status").isTrue();

        gameSetup.stop();
    }

    @Test
    void handlesCreateGameOnBattlenetMapCommunication() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasCreateGame, GameServerResponses::createGame);

        assertThat(gameSetup.control().createGame(
                BattlenetMap.of("Lava Flow"),
                List.of(PlayerSettings.participant(Race.PROTOSS, mock(S2Agent.class))),
                true)
        ).as("create game status").isTrue();

        gameSetup.stop();
    }

    @Test
    void handlesErrorOfCreateGameOnLocalMap() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasCreateGame, GameServerResponses::createGameWithError);

        assertThat(gameSetup.control().createGame(
                LocalMap.of(Paths.get("test.SC2Map")),
                List.of(PlayerSettings.participant(Race.ZERG, mock(S2Agent.class))),
                true)
        ).as("create game status").isFalse();

        gameSetup.stop();
    }

    @Test
    void handlesErrorOfCreateGameOnBattlenetMap() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasCreateGame, GameServerResponses::createGameWithError);

        assertThat(gameSetup.control().createGame(
                BattlenetMap.of("Lava Flow"),
                List.of(PlayerSettings.participant(Race.TERRAN, mock(S2Agent.class))),
                true)
        ).as("create game status").isFalse();

        gameSetup.stop();
    }

    @Test
    void handlesJoinGameCommunication() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasJoinGame, GameServerResponses::joinGame);

        Maybe<Response> responseMaybe = gameSetup.control().requestJoinGame(
                PlayerSettings.participant(Race.PROTOSS, mock(S2Agent.class)),
                new InterfaceSettings(null, null),
                MultiplayerOptions.multiplayerSetupFor(1, 2));

        assertThat(gameSetup.control().hasResponsePending(ResponseType.JOIN_GAME)).isTrue();
        assertThat(gameSetup.control().waitJoinGame(responseMaybe)).as("join game status").isTrue();
        assertThat(gameSetup.control().hasResponsePending(ResponseType.JOIN_GAME)).isFalse();

        gameSetup.assertThatGameIsMultiplayer();

        verify(gameSetup.observation()).clearFlags();
        verify(gameSetup.observation()).setPlayerId(GameServerResponses.PLAYER_ID);

        gameSetup.stop();
    }

    @Test
    void handlesErrorOfJoinGame() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasJoinGame, GameServerResponses::joinGameWithError);

        Maybe<Response> responseMaybe = gameSetup.control().requestJoinGame(
                PlayerSettings.participant(Race.PROTOSS, mock(S2Agent.class)),
                new InterfaceSettings(null, null),
                null);
        assertThat(gameSetup.control().waitJoinGame(responseMaybe)).as("join game status").isFalse();

        gameSetup.stop();
    }

    @Test
    void handlesSaveMapCommunication() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasSaveMap, GameServerResponses::saveMap);

        assertThat(gameSetup.control().remoteSaveMap(LocalMap.of(Paths.get(TEST_MAP))))
                .as("save map status").isTrue();

        gameSetup.stop();
    }

    @Test
    void handlesErrorOfSaveMap() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasSaveMap, GameServerResponses::saveMapWithError);

        assertThat(gameSetup.control().remoteSaveMap(LocalMap.of(Paths.get(TEST_MAP))))
                .as("save map status").isFalse();

        gameSetup.stop();
    }

    @Test
    void handlesLeaveGameCommunication() {
        GameSetup gameSetup = new GameSetup().multiplayer(true).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasLeaveGame, GameServerResponses::leaveGame);

        Maybe<Response> responseMaybe = gameSetup.control().requestLeaveGame();
        assertThat(gameSetup.control().pollLeaveGame(responseMaybe)).as("leave game status").isTrue();

        gameSetup.stop();
    }

    @Test
    void throwsExceptionWhenLeaveGameNotFromMultiplayerGame() {
        GameSetup gameSetup = new GameSetup().start();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(gameSetup.control()::requestLeaveGame)
                .withMessage("LeaveGame request is only available for multiplayer game.");

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> gameSetup.control().pollLeaveGame(Maybe.empty()))
                .withMessage("LeaveGame response is only available for multiplayer game.");

        gameSetup.stop();
    }

    @Test
    void handlesStepCommunication() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasStep, GameServerResponses::step);
        gameSetup.server().onRequest(Sc2Api.Request::hasObservation, GameServerResponses::observation);
        when(gameSetup.observation().updateObservation(any(ResponseObservation.class))).thenReturn(true);

        assertThat(gameSetup.control().waitStep(gameSetup.control().step(1))).as("step status").isTrue();

        gameSetup.stop();
    }

    @Test
    void handlesErrorOfStep() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasStep, GameServerResponses::error);
        gameSetup.server().onRequest(Sc2Api.Request::hasObservation, GameServerResponses::observation);
        when(gameSetup.observation().updateObservation(any(ResponseObservation.class))).thenReturn(true);

        assertThat(gameSetup.control().waitStep(gameSetup.control().step(1))).as("step status").isFalse();

        gameSetup.stop();
    }

    @Test
    void throwsExceptionWhenRequestingNextStepAndApplicationStateIsNotNormal() {
        GameSetup gameSetup = new GameSetup().inState(AppState.TIMEOUT).start();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> gameSetup.control().step(1))
                .withMessage("Invalid application state: TIMEOUT");

        gameSetup.stop();
    }

    @Test
    void handlesSaveReplayCommunication() throws IOException {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasSaveReplay, GameServerResponses::saveReplay);

        Path replay = replayDir.getRootFolder().resolve("test.SC2Replay");
        assertThat(gameSetup.control().saveReplay(replay)).as("save replay status").isTrue();
        assertThat(Files.exists(replay)).as("saved replay file exists").isTrue();

        gameSetup.stop();
    }

    @Test
    void handlesErrorOfSaveReplay() throws IOException {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasSaveReplay, GameServerResponses::saveReplayWithEmptyData);

        Path replay = replayDir.getRootFolder().resolve("test.SC2Replay");
        assertThat(gameSetup.control().saveReplay(replay)).as("save replay status").isFalse();
        assertThat(Files.exists(replay)).as("saved replay file exists").isFalse();

        gameSetup.stop();
    }

    @Test
    void handlesObservationCommunication() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasObservation, GameServerResponses::observation);
        when(gameSetup.observation().updateObservation(any(ResponseObservation.class))).thenReturn(true);

        assertThat(gameSetup.control().getObservation()).as("observation status").isTrue();

        verify(gameSetup.observation()).updateObservation(any(ResponseObservation.class));

        gameSetup.stop();
    }

    @Test
    void handlesErrorOfObservation() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasObservation, GameServerResponses::observation);
        when(gameSetup.observation().updateObservation(any(ResponseObservation.class))).thenReturn(false);

        assertThat(gameSetup.control().getObservation()).as("observation status").isFalse();

        verify(gameSetup.observation()).updateObservation(any(ResponseObservation.class));

        gameSetup.stop();
    }

    @Test
    void handlesNewObservation() {
        GameSetup gameSetup = new GameSetup().mockObservation(false).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasData, GameServerResponses::abilityData);
        gameSetup.control().useGeneralizedAbility(true);

        gameSetup.observation().unitPool()
                .createUnit(OLD_01_UNIT_TAG)
                .update(mockUnit(OLD_01_UNIT_TAG, Raw.Alliance.Self, true, 0.8f));
        gameSetup.observation().unitPool()
                .createUnit(OLD_02_UNIT_TAG)
                .update(mockUnit(OLD_02_UNIT_TAG, Raw.Alliance.Self, false, 1.0f));

        assertThat(gameSetup.observation().updateObservation(prepareObservation()))
                .as("status of update observation").isTrue();

        assertThatObservationInterfaceIsUpdated(gameSetup.observation());
        assertThat(gameSetup.control().issueEvents(List.of(OLD_02_UNIT_TAG))).as("status of issue event").isTrue();
        assertThatClientEventsAreIssued(gameSetup.clientEvents(), gameSetup.control());

        gameSetup.stop();
    }

    private ResponseObservation prepareObservation() {
        Unit deadUnit = mockUnit(DEAD_UNIT_TAG, Raw.Alliance.Self, false, 1.0f);
        Unit newUnit = mockUnit(NEW_UNIT_TAG, Raw.Alliance.Self, false, 1.0f);
        Unit enemyUnit = mockUnit(ENEMY_UNIT_TAG, Raw.Alliance.Enemy, true, 1.0f);
        Unit old01Unit = mockUnit(OLD_01_UNIT_TAG, Raw.Alliance.Self, false, 1.0f);
        Unit old02Unit = mockUnit(OLD_02_UNIT_TAG, Raw.Alliance.Self, false, 1.0f);

        Event event = mock(Event.class);
        when(event.getDeadUnits()).thenReturn(Set.of(DEAD_UNIT_TAG));

        PlayerRaw playerRaw = mock(PlayerRaw.class);
        when(playerRaw.getPowerSources()).thenReturn(Set.of(mock(PowerSource.class)));
        when(playerRaw.getUpgrades()).thenReturn(Set.of(Upgrades.HYDRALISK_SPEED));
        when(playerRaw.getCamera()).thenReturn(mock(Point.class));

        ObservationRaw observationRaw = mock(ObservationRaw.class);
        when(observationRaw.getEvent()).thenReturn(Optional.of(event));
        when(observationRaw.getUnits()).thenReturn(Set.of(deadUnit, newUnit, enemyUnit, old01Unit, old02Unit));
        when(observationRaw.getPlayer()).thenReturn(playerRaw);
        when(observationRaw.getEffects()).thenReturn(Set.of(mock(EffectLocations.class)));

        PlayerCommon playerCommon = mock(PlayerCommon.class);
        when(playerCommon.getPlayerId()).thenReturn(1);
        when(playerCommon.getMinerals()).thenReturn(1);
        when(playerCommon.getVespene()).thenReturn(1);
        when(playerCommon.getFoodCap()).thenReturn(1);
        when(playerCommon.getFoodUsed()).thenReturn(1);
        when(playerCommon.getFoodArmy()).thenReturn(1);
        when(playerCommon.getFoodWorkers()).thenReturn(1);
        when(playerCommon.getIdleWorkerCount()).thenReturn(1);
        when(playerCommon.getArmyCount()).thenReturn(1);
        when(playerCommon.getWarpGateCount()).thenReturn(Optional.of(1));

        Observation observation = mock(Observation.class);
        when(observation.getGameLoop()).thenReturn(1);
        when(observation.getRaw()).thenReturn(Optional.of(observationRaw));
        when(observation.getPlayerCommon()).thenReturn(playerCommon);
        when(observation.getScore()).thenReturn(Optional.of(mock(Score.class)));
        when(observation.getAlerts()).thenReturn(Set.of(Alert.NUCLEAR_LAUNCH_DETECTED, Alert.NYDUS_WORM_DETECTED));

        Action action = mock(Action.class);
        when(action.getRaw()).thenReturn(Optional.of(mock(ActionRaw.class)));
        when(action.getFeatureLayer()).thenReturn(Optional.of(mock(ActionSpatial.class)));
        when(action.getRender()).thenReturn(Optional.of(mock(ActionSpatial.class)));

        ResponseObservation responseObservation = mock(ResponseObservation.class);
        when(responseObservation.getChat()).thenReturn(List.of(mock(ChatReceived.class)));
        when(responseObservation.getActions()).thenReturn(List.of(action));
        when(responseObservation.getObservation()).thenReturn(observation);
        when(responseObservation.getPlayerResults()).thenReturn(List.of(mock(PlayerResult.class)));

        return responseObservation;
    }

    private void assertThatObservationInterfaceIsUpdated(ObservationInterfaceImpl observation) {
        assertThat(observation.getRawObservation()).isNotNull();
        assertThat(observation.getGameLoop()).isGreaterThan(0);
        assertThat(observation.gameLoopChanged()).isTrue();
        assertThat(observation.getPlayerId()).isGreaterThan(0);
        assertThat(observation.getRawActions()).isNotEmpty();
        assertThat(observation.getFeatureLayerActions()).isNotEmpty();
        assertThat(observation.getRenderedActions()).isNotEmpty();
        assertThat(observation.getChatMessages()).isNotEmpty();
        assertThat(observation.getPowerSources()).isNotEmpty();
        assertThat(observation.getEffects()).isNotEmpty();
        assertThat(observation.getUpgrades()).isNotEmpty();
        assertThat(observation.getResults()).isNotEmpty();
        assertThat(observation.getUnits()).isNotEmpty();
        assertThat(observation.getUnit(ENEMY_UNIT_TAG)).isNotNull();
        assertThat(observation.getUnit(NEW_UNIT_TAG)).isNotNull();
        assertThat(observation.getScore()).isNotNull();
        assertThat(observation.getMinerals()).isGreaterThan(0);
        assertThat(observation.getVespene()).isGreaterThan(0);
        assertThat(observation.getFoodCap()).isGreaterThan(0);
        assertThat(observation.getFoodUsed()).isGreaterThan(0);
        assertThat(observation.getFoodArmy()).isGreaterThan(0);
        assertThat(observation.getFoodWorkers()).isGreaterThan(0);
        assertThat(observation.getIdleWorkerCount()).isGreaterThan(0);
        assertThat(observation.getArmyCount()).isGreaterThan(0);
        assertThat(observation.getWarpGateCount()).isGreaterThan(0);
        assertThat(observation.getCameraPos()).isNotNull();
    }

    private void assertThatClientEventsAreIssued(ClientEvents clientEvents, ControlInterfaceImpl control) {
        UnitPool unitPool = control.observationInternal().unitPool();
        UnitInPool deadUnitInPool = unitPool.getUnit(DEAD_UNIT_TAG).orElseThrow(required("dead unit in pool"));
        UnitInPool enemyUnitInPool = unitPool.getUnit(ENEMY_UNIT_TAG).orElseThrow(required("enemy unit in pool"));
        UnitInPool newUnitInPool = unitPool.getUnit(NEW_UNIT_TAG).orElseThrow(required("new unit in pool"));
        UnitInPool oldUnit01InPool = unitPool.getUnit(OLD_01_UNIT_TAG).orElseThrow(required("old unit 01 in pool"));
        UnitInPool oldUnit02InPool = unitPool.getUnit(OLD_02_UNIT_TAG).orElseThrow(required("old unit 02 in pool"));

        verify(clientEvents).onUnitDestroyed(deadUnitInPool);
        verify(clientEvents).onUnitEnterVision(enemyUnitInPool);
        verify(clientEvents).onUnitCreated(newUnitInPool);
        verify(clientEvents).onUnitIdle(newUnitInPool);
        verify(clientEvents).onUnitIdle(oldUnit01InPool);
        verify(clientEvents).onUnitIdle(oldUnit02InPool);
        verify(clientEvents).onBuildingConstructionComplete(oldUnit01InPool);
        verify(clientEvents).onUpgradeCompleted(Upgrades.HYDRALISK_SPEED);
        verify(clientEvents).onNuclearLaunchDetected();
        verify(clientEvents).onNydusDetected();
        verify(clientEvents).onStep();

        verifyNoMoreInteractions(clientEvents);
    }

    @Test
    void throwsExceptionWhenRequestingObservationAndApplicationStateIsNotNormal() {
        GameSetup gameSetup = new GameSetup().inState(AppState.CRASHED).start();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(gameSetup.control()::getObservation)
                .withMessage("Invalid application state: CRASHED");

        gameSetup.stop();
    }

    @Test
    void handlesQuickSaveCommunication() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasQuickSave, GameServerResponses::quickSave);

        assertThat(gameSetup.control().save()).as("quick save status").isTrue();

        gameSetup.stop();
    }

    @Test
    void handlesQuickLoadCommunication() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasQuickLoad, GameServerResponses::quickLoad);

        assertThat(gameSetup.control().load()).as("quick load status").isTrue();

        gameSetup.stop();
    }

}
