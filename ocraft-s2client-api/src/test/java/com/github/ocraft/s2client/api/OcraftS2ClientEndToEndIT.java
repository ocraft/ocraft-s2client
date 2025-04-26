package com.github.ocraft.s2client.api;

/*-
 * #%L
 * ocraft-s2client-api
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

import com.github.ocraft.s2client.api.controller.PortSetup;
import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.protocol.action.Actions;
import com.github.ocraft.s2client.protocol.debug.DebugGameState;
import com.github.ocraft.s2client.protocol.game.*;
import com.github.ocraft.s2client.protocol.game.Observer;
import com.github.ocraft.s2client.protocol.request.Requests;
import com.github.ocraft.s2client.protocol.response.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.api.Fixtures.*;
import static com.github.ocraft.s2client.api.S2Client.starcraft2Client;
import static com.github.ocraft.s2client.api.controller.S2Controller.starcraft2Game;
import static com.github.ocraft.s2client.protocol.Defaults.defaultInterfaces;
import static com.github.ocraft.s2client.protocol.action.Action.action;
import static com.github.ocraft.s2client.protocol.action.Actions.Observer.*;
import static com.github.ocraft.s2client.protocol.action.Actions.Raw.cameraMove;
import static com.github.ocraft.s2client.protocol.action.Actions.Raw.toggleAutocast;
import static com.github.ocraft.s2client.protocol.action.Actions.Raw.*;
import static com.github.ocraft.s2client.protocol.action.Actions.*;
import static com.github.ocraft.s2client.protocol.action.Actions.Spatial.click;
import static com.github.ocraft.s2client.protocol.action.Actions.Spatial.select;
import static com.github.ocraft.s2client.protocol.action.Actions.Ui.*;
import static com.github.ocraft.s2client.protocol.action.ObserverAction.observerAction;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint.Type.TOGGLE;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiControlGroup.Action.SET;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiMultiPanel.Type.SINGLE_SELECT;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectIdleWorker.Type.ADD_ALL;
import static com.github.ocraft.s2client.protocol.data.Abilities.*;
import static com.github.ocraft.s2client.protocol.data.Units.TERRAN_THOR;
import static com.github.ocraft.s2client.protocol.debug.Commands.Draw.*;
import static com.github.ocraft.s2client.protocol.debug.Commands.*;
import static com.github.ocraft.s2client.protocol.debug.DebugCommand.command;
import static com.github.ocraft.s2client.protocol.debug.DebugEndGame.EndResult.DECLARE_VICTORY;
import static com.github.ocraft.s2client.protocol.debug.DebugSetUnitValue.UnitValue.LIFE;
import static com.github.ocraft.s2client.protocol.game.ComputerPlayerSetup.computer;
import static com.github.ocraft.s2client.protocol.game.InterfaceOptions.interfaces;
import static com.github.ocraft.s2client.protocol.game.MultiplayerOptions.multiplayerSetupFor;
import static com.github.ocraft.s2client.protocol.game.PlayerSetup.observer;
import static com.github.ocraft.s2client.protocol.game.PlayerSetup.participant;
import static com.github.ocraft.s2client.protocol.game.Race.*;
import static com.github.ocraft.s2client.protocol.query.Queries.*;
import static com.github.ocraft.s2client.protocol.request.RequestData.Type.*;
import static com.github.ocraft.s2client.protocol.request.Requests.*;

@Tag("endtoend")
class OcraftS2ClientEndToEndIT {

    private S2Client client;
    private S2Controller game;
    private TestS2ClientSubscriber subscriber;
    private PortSetup portSetup;

    @BeforeEach
    void prepareTest() {
        portSetup = PortSetup.init(5000);
        game = starcraft2Game().withPort(portSetup.fetchPort()).launch();
        client = starcraft2Client().connectTo(game).traced(true).start();
        subscriber = new TestS2ClientSubscriber();
        client.responseStream().subscribe(subscriber);
    }

    @AfterEach
    void reset() {
        client.stop();
        game.stop();
        subscriber.isCompleted();
    }

    @Test
    void allowsCommunicationWithTheGameServer() {
        client.request(ping());
        subscriber.hasReceivedResponse();
    }

    @Test
    void providesInformationAboutAvailableMaps() {
        client.request(availableMaps());
        subscriber.hasReceivedResponseOfType(ResponseAvailableMaps.class);
    }

    @Test
    void managesLifecycleOfTheGame() {
        startTheGame();

        client.request(restartGame());
        subscriber.hasReceivedResponseOfType(ResponseRestartGame.class);
        game.isInState(GameStatus.IN_GAME);

        endTheGame();
    }

    private void startTheGame() {
        client.request(createGame()
                .onLocalMap(LocalMap.of(MAP_PATH))
                .withPlayerSetup(participant(), computer(PROTOSS, Difficulty.MEDIUM))
                .disableFog());
        subscriber.hasReceivedResponseOfType(ResponseCreateGame.class);
        game.isInState(GameStatus.INIT_GAME);

        client.request(joinGame().as(TERRAN));
        subscriber.hasReceivedResponseOfType(ResponseJoinGame.class);
        game.isInState(GameStatus.IN_GAME);
    }

    private void endTheGame() {
        client.request(debug().with(command().of(endGame().withResult(DECLARE_VICTORY))));
        subscriber.hasReceivedResponseOfType(ResponseDebug.class);

        client.request(saveReplay());
        subscriber.hasReceivedResponseOfType(ResponseSaveReplay.class);

        quitTheGame();
    }

    private void quitTheGame() {
        client.request(quitGame());
        subscriber.hasReceivedResponseOfType(ResponseQuitGame.class);
        game.isInState(GameStatus.QUIT);
    }

    @Test
    void processesReplay() {
        client.request(replayInfo().of(REPLAY_PATH).download());
        subscriber.hasReceivedResponseOfType(ResponseReplayInfo.class)
                .getReplayInfo()
                .ifPresent(info -> game.relaunchIfNeeded(info.getBaseBuild(), info.getDataVersion()));
        game.inState(GameStatus.LAUNCHED);

        client.request(startReplay().from(REPLAY_PATH).use(defaultInterfaces()).toObserve(PLAYER_ID).disableFog());
        subscriber.hasReceivedResponseOfType(ResponseStartReplay.class);
        game.isInState(GameStatus.IN_REPLAY);

        do {
            if (!observation().getStatus().equals(GameStatus.ENDED)) {
                client.request(nextStep().withCount(GAME_LOOP_COUNT));
                subscriber.hasReceivedResponseOfType(ResponseStep.class);
            }
        } while (!game.inState(GameStatus.ENDED));

        quitTheGame();
    }

    private ResponseObservation observation() {
        client.request(Requests.observation().disableFog());
        return subscriber.hasReceivedResponseOfType(ResponseObservation.class);
    }

    @Test
    void playsTheGame() {
        startTheGame();

        GameContext ctx = GameContext.from(observation());

        client.request(actions().of(
                action().raw(unitCommand().forUnits(ctx.commandCenter).useAbility(TRAIN_SCV).queued()),
                action().raw(cameraMove().to(ctx.scv)),
                action().raw(toggleAutocast().ofAbility(EFFECT_REPAIR_SCV).forUnits(ctx.scv)),
                action().featureLayer(Spatial.cameraMove().to(ctx.pointOnScreen)),
                action().featureLayer(click().on(ctx.pointOnScreen).withMode(TOGGLE)),
                action().featureLayer(select().of(ctx.area).add()),
                action().featureLayer(Spatial.unitCommand().useAbility(MOVE).onScreen(ctx.pointOnScreen).queued()),
                action().render(Spatial.cameraMove().to(ctx.pointOnScreen)),
                action().render(click().on(ctx.pointOnScreen).withMode(TOGGLE)),
                action().render(select().of(ctx.area).add()),
                action().render(Spatial.unitCommand().useAbility(MOVE).onScreen(ctx.pointOnScreen).queued()),
                action().ui(controlGroup().on(ctx.key).withMode(SET)),
                action().ui(multiPanel().select(ctx.commandCenterIndex).withMode(SINGLE_SELECT)),
                action().ui(cargoPanelUnload().of(ctx.unitIndex)),
                action().ui(removeFromQueue().of(ctx.unitIndex)),
                action().ui(selectArmy().add()),
                action().ui(selectWarpGates().add()),
                action().ui(selectLarva()),
                action().ui(selectIdleWorker().withMode(ADD_ALL)),
                action().ui(Ui.toggleAutocast().ofAbility(EFFECT_REPAIR_SCV)),
                action().chat(message().of(ctx.welcome).toAll())
        ));
        subscriber.hasReceivedResponseOfType(ResponseAction.class);
        game.isInState(GameStatus.IN_GAME);

        endTheGame();
    }

    @Test
    void providesInformationAboutTheGame() {
        startTheGame();

        client.request(gameInfo());
        subscriber.hasReceivedResponseOfType(ResponseGameInfo.class);

        GameContext ctx = GameContext.from(observation());

        client.request(query()
                .ofAbilities(availableAbilities().of(ctx.commandCenter))
                .ofPathings(path().from(ctx.start).to(ctx.end))
                .ofPlacements(placeBuilding().withUnit(ctx.scv).useAbility(BUILD_SUPPLY_DEPOT).on(ctx.end))
                .ignoreResourceRequirements());
        subscriber.hasReceivedResponseOfType(ResponseQuery.class);

        endTheGame();
    }

    @Test
    void playsMultiplayerGame() {
        client.request(saveMap().to(LocalMap.of(TMP_PATH)));
        subscriber.hasReceivedResponseOfType(ResponseSaveMap.class);

        S2Controller game02 = starcraft2Game().withPort(portSetup.fetchPort()).launch();
        S2Client client02 = starcraft2Client().connectTo(game02).traced(true).start();

        client.request(createGame()
                .onLocalMap(LocalMap.of(MAP_PATH))
                .withPlayerSetup(participant(), participant()).realTime());
        subscriber.hasReceivedResponseOfType(ResponseCreateGame.class);

        MultiplayerOptions multiplayerOptions = multiplayerSetupFor(portSetup.lastPort(), PLAYER_COUNT);

        client.request(joinGame().as(PROTOSS).use(interfaces().raw()).with(multiplayerOptions));
        client02.request(joinGame().as(ZERG).use(interfaces().raw()).with(multiplayerOptions));

        subscriber.hasReceivedResponseOfType(ResponseJoinGame.class);

        client.request(leaveGame());
        subscriber.hasReceivedResponseOfType(ResponseLeaveGame.class);
        game.isInState(GameStatus.LAUNCHED);

        quitTheGame();

        game02.stop();
        client02.stop();
    }

    @Test
    void providesInformationAboutGameplayElements() {
        startTheGame();

        client.request(data().of(ABILITIES, BUFFS, UPGRADES, EFFECTS, UNITS));
        subscriber.hasReceivedResponseOfType(ResponseData.class);

        endTheGame();
    }

    @Test
    void allowsToSaveGameState() {
        startTheGame();

        client.request(quickSave());
        subscriber.hasReceivedResponseOfType(ResponseQuickSave.class);
        game.isInState(GameStatus.IN_GAME);

        client.request(quickLoad());
        subscriber.hasReceivedResponseOfType(ResponseQuickLoad.class);
        game.isInState(GameStatus.IN_GAME);

        endTheGame();
    }

    @Test
    void debugsTheGame() {
        startTheGame();

        GameContext ctx = GameContext.from(observation());

        client.request(debug().with(
                command().of(draw().texts(
                        text().of(ctx.debugTxt).withColor(ctx.white).onMap(ctx.p0),
                        text().of(ctx.debugTxt).withColor(ctx.white).withSize(ctx.textSize).onMap(ctx.p1))),
                command().of(draw().lines(
                        line().of(ctx.p0, ctx.p1).withColor(ctx.white),
                        line().of(ctx.p1, ctx.p2).withColor(ctx.white))),
                command().of(draw().boxes(
                        box().of(ctx.p0, ctx.p1).withColor(ctx.white),
                        box().of(ctx.p1, ctx.p2).withColor(ctx.white))),
                command().of(draw().spheres(
                        sphere().on(ctx.p2).withRadius(ctx.radius).withColor(ctx.white),
                        sphere().on(ctx.p0).withRadius(ctx.radius).withColor(ctx.white))),
                command().of(DebugGameState.GOD),
                command().of(createUnit().ofType(TERRAN_THOR).forPlayer(ctx.terran).on(ctx.end).withQuantity(ctx.two)),
                command().of(killUnit().of(ctx.scv)),
                command().of(setScore().to(ctx.score)),
                command().of(setUnitValue().forUnit(ctx.commandCenter).set(LIFE).to(ctx.lifeValue)),
                command().of(endGame().withResult(DECLARE_VICTORY))
        ));
        subscriber.hasReceivedResponseOfType(ResponseDebug.class);

        endTheGame();
    }

    @Test
    void observesTheGame() {
        startTheGameAsObserver();

        GameContext ctx = GameContext.from(observation());

        client.request(observerActions().with(
                observerAction().of(playerPerspective().ofAll()),
                observerAction().of(Actions.Observer.cameraMove().to(ctx.start).withDistance(ctx.distance)),
                observerAction().of(cameraFollowPlayer().withId(PLAYER_ID)),
                observerAction().of(cameraFollowUnits().of(ctx.scv))
        ));
        subscriber.hasReceivedResponseOfType(ResponseObserverAction.class);
        game.isInState(GameStatus.IN_GAME);

        endTheGame();
    }

    private void startTheGameAsObserver() {
        client.request(createGame()
                .onLocalMap(LocalMap.of(MAP_PATH))
                .withPlayerSetup(observer(), computer(TERRAN, Difficulty.HARDER))
                .disableFog());
        subscriber.hasReceivedResponseOfType(ResponseCreateGame.class);
        game.isInState(GameStatus.INIT_GAME);

        client.request(joinGame().as(Observer.of(PLAYER_ID)));
        subscriber.hasReceivedResponseOfType(ResponseJoinGame.class);
        game.isInState(GameStatus.IN_GAME);
    }

    @Test
    void returnsResponseErrorOnInvalidRequest() {
        client.request(joinGame().as(PROTOSS));
        subscriber.hasReceivedResponseOfType(ResponseError.class);
    }

}
