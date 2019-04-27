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

import com.github.ocraft.s2client.api.OcraftApiConfig;
import com.github.ocraft.s2client.api.ResponseParseException;
import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.ClientEvents;
import com.github.ocraft.s2client.bot.S2ReplayObserver;
import com.github.ocraft.s2client.bot.gateway.*;
import com.github.ocraft.s2client.bot.setting.InterfaceSettings;
import com.github.ocraft.s2client.bot.setting.PlayerSettings;
import com.github.ocraft.s2client.bot.setting.ProcessSettings;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.data.Upgrade;
import com.github.ocraft.s2client.protocol.game.*;
import com.github.ocraft.s2client.protocol.observation.raw.ObservationRaw;
import com.github.ocraft.s2client.protocol.request.Requests;
import com.github.ocraft.s2client.protocol.response.*;
import com.github.ocraft.s2client.protocol.syntax.request.FeatureLayerSyntax;
import com.github.ocraft.s2client.protocol.unit.Alliance;
import com.github.ocraft.s2client.protocol.unit.DisplayType;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;
import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.lang.String.format;

class ControlInterfaceImpl implements ControlInterface {

    private Logger log = LoggerFactory.getLogger(ControlInterfaceImpl.class);

    private final ProtoInterfaceImpl protoInterface;
    private final ObservationInterfaceImpl observationInterface;
    private final AgentControlInterface agentControlInterface;
    private final QueryInterface queryInterface;
    private final DebugInterface debugInterface;
    private final ObserverActionInterface observerActionInterface;

    private final List<ClientError> clientErrors = new ArrayList<>();
    private final List<String> protocolErrors = new ArrayList<>();
    private final ClientEvents clientEvents;

    private S2Controller theGame;
    private AppState appState = AppState.NORMAL;
    private boolean multiplayer;
    private ProcessInfo processInfo;
    private boolean useGeneralizedAbilityId;

    ControlInterfaceImpl(ClientEvents clientEvents) {
        require("client events callback", clientEvents);

        this.protoInterface = new ProtoInterfaceImpl();
        this.observationInterface = new ObservationInterfaceImpl(this);

        this.protoInterface.setOnError(this::onError);
        this.agentControlInterface = new AgentControlInterfaceImpl(this);
        this.queryInterface = new QueryInterfaceImpl(this);
        this.debugInterface = new DebugInterfaceImpl(this);
        this.observerActionInterface = new ObserverActionInterfaceImpl(this);
        this.clientEvents = clientEvents;
    }

    ControlInterfaceImpl(ClientEvents clientEvents, ProtoInterfaceImpl protoInterface) {
        require("client events callback", clientEvents);
        require("proto interface", protoInterface);

        this.protoInterface = protoInterface;
        this.observationInterface = new ObservationInterfaceImpl(this);

        this.protoInterface.setOnError(this::onError);
        this.agentControlInterface = new AgentControlInterfaceImpl(this);
        this.queryInterface = new QueryInterfaceImpl(this);
        this.debugInterface = new DebugInterfaceImpl(this);
        this.observerActionInterface = new ObserverActionInterfaceImpl(this);
        this.clientEvents = clientEvents;
    }

    ControlInterfaceImpl(
            ClientEvents clientEvents,
            ProtoInterfaceImpl protoInterface,
            ObservationInterfaceImpl observationInterface) {

        require("client events callback", clientEvents);
        require("proto interface", protoInterface);
        require("observation interface", observationInterface);

        this.protoInterface = protoInterface;
        this.observationInterface = observationInterface;

        this.protoInterface.setOnError(this::onError);
        this.agentControlInterface = new AgentControlInterfaceImpl(this);
        this.queryInterface = new QueryInterfaceImpl(this);
        this.debugInterface = new DebugInterfaceImpl(this);
        this.observerActionInterface = new ObserverActionInterfaceImpl(this);
        this.clientEvents = clientEvents;
    }

    private void onError(ClientError clientError, List<String> protocolErrors) {
        clientErrors.add(clientError);
        this.protocolErrors.addAll(protocolErrors);
    }

    @Override
    public ProtoInterface proto() {
        return protoInterface;
    }

    @Override
    public ObservationInterface observation() {
        return observationInterface;
    }

    @Override
    public boolean connect(ProcessSettings processSettings) {
        require("process settings", processSettings);

        log.info("Waiting for connection...");
        boolean connected;
        if (processSettings.withGameController()) {
            theGame = tryLaunchProcess(processSettings);
            connected = proto().connectToGame(
                    theGame,
                    processSettings.getConnectionTimeoutMS(),
                    processSettings.getRequestTimeoutMS(),
                    processSettings.getTraced());
        } else {
            connected = proto().connectToGame(
                    processSettings.getIp(),
                    processSettings.getPort(),
                    processSettings.getConnectionTimeoutMS(),
                    processSettings.getRequestTimeoutMS(),
                    processSettings.getTraced());
        }

        if (connected) {
            log.info("Connected to {}:{}", proto().getConnectToIp(), proto().getConnectToPort());
            if (!isSet(processSettings.getPortSetup())) {
                processSettings.setPortStart(proto().getConnectToPort());
                processSettings.getPortSetup().fetchPort(); // this port is already used
            }
            if (isSet(theGame) && !isSet(processSettings.getRootPath())) {
                processSettings.setRootPath(Paths.get(theGame.getConfig().getString(OcraftApiConfig.GAME_EXE_ROOT)));
            }
            if (isSet(theGame) && !isSet(processSettings.getActualProcessPath())) {
                processSettings.setActualProcessPath(
                        Paths.get(theGame.getConfig().getString(OcraftApiConfig.GAME_EXE_PATH)));
            }
            updateProcessInfo(processSettings);
            return true;
        } else {
            log.error("Unable to connect to game");
            return false;
        }
    }

    private S2Controller tryLaunchProcess(ProcessSettings processSettings) {
        if (isSet(theGame)) return theGame;
        return S2Controller.starcraft2Game()
                .withExecutablePath(processSettings.getProcessPath())
                .withDataVersion(processSettings.getDataVersion())
                .withBaseBuild(processSettings.getBaseBuild())
                .withListenIp(processSettings.getIp())
                .withPort(isSet(processSettings.getPortSetup()) ? processSettings.getPortSetup().fetchPort() : null)
                .withWindowSize(processSettings.getWindowWidth(), processSettings.getWindowHeight())
                .withWindowPosition(processSettings.getWindowX(), processSettings.getWindowY())
                .withDataDir(processSettings.getDataDir())
                .withTmpDir(processSettings.getTmpDir())
                .withEglPath(processSettings.getEglPath())
                .withOsMesaPath(processSettings.getOsMesaPath())
                .verbose(processSettings.getVerbose())
                .needsSupportDir(processSettings.getNeedsSupportDir())
                .launch().untilReady();
    }

    // test purposes only
    void setS2Controller(S2Controller s2Controller) {
        theGame = s2Controller;
    }

    private void updateProcessInfo(ProcessSettings processSettings) {
        setProcessInfo(ProcessInfo.from(
                isSet(theGame)
                        ? Paths.get(theGame.getConfig().getString(OcraftApiConfig.GAME_EXE_PATH))
                        : processSettings.getProcessPath(),
                isSet(theGame)
                        ? theGame.getS2Process().pid()
                        : null,
                isSet(theGame)
                        ? theGame.getConfig().getInt(OcraftApiConfig.GAME_NET_PORT)
                        : processSettings.getPort()));
    }

    @Override
    public boolean remoteSaveMap(LocalMap localMap) {
        Optional<ResponseSaveMap> responseSaveMap =
                waitForResponse(proto().sendRequest(Requests.saveMap().to(localMap)))
                        .flatMap(response -> response.as(ResponseSaveMap.class));
        boolean isSuccess = responseSaveMap.isPresent() && !responseSaveMap.get().getError().isPresent();
        if (!isSuccess) {
            responseSaveMap.ifPresent(response -> response.getError().ifPresent(
                    errorCode -> log.error("SaveMap request returned an error code: {}", errorCode)));
        }
        return isSuccess;
    }

    @Override
    public boolean createGame(BattlenetMap battlenetMap, List<PlayerSettings> playerSettings, Boolean realtime) {
        Optional<ResponseCreateGame> responseCreateGame = waitForResponse(
                proto().sendRequest(
                        Requests.createGame()
                                .onBattlenetMap(battlenetMap)
                                .withPlayerSetup(playerSetupFrom(playerSettings))
                                .realTime(realtime)))
                .flatMap(response -> response.as(ResponseCreateGame.class));
        boolean isSuccess = responseCreateGame.isPresent() && !responseCreateGame.get().getError().isPresent();
        if (!isSuccess) {
            responseCreateGame.ifPresent(createGameErrorHandler());
        }
        return isSuccess;
    }

    private PlayerSetup[] playerSetupFrom(List<PlayerSettings> playerSettings) {
        return playerSettings
                .stream()
                .map(PlayerSettings::getPlayerSetup)
                .collect(Collectors.toList())
                .toArray(new PlayerSetup[playerSettings.size()]);
    }

    private Consumer<ResponseCreateGame> createGameErrorHandler() {
        return response -> {
            response.getError().ifPresent(
                    errorCode -> log.error("CreateGame request returned an error code: {}", errorCode));
            response.getErrorDetails().ifPresent(
                    errorDetails -> log.error("CreateGame request returned error details: {}", errorDetails));
        };
    }

    @Override
    public boolean createGame(LocalMap localMap, List<PlayerSettings> playerSettings, Boolean realtime) {
        Optional<ResponseCreateGame> responseCreateGame = waitForResponse(
                proto().sendRequest(
                        Requests.createGame()
                                .onLocalMap(localMap)
                                .withPlayerSetup(playerSetupFrom(playerSettings))
                                .realTime(realtime)))
                .flatMap(response -> response.as(ResponseCreateGame.class));
        boolean isSuccess = responseCreateGame.isPresent() && !responseCreateGame.get().getError().isPresent();
        if (!isSuccess) {
            responseCreateGame.ifPresent(createGameErrorHandler());
        }
        return isSuccess;
    }

    @Override
    public Maybe<Response> requestJoinGame(
            PlayerSettings playerSettings, InterfaceSettings interfaceSettings, MultiplayerOptions multiplayerOptions) {
        try {
            observationInternal().clearFlags();

            InterfaceOptions interfaceOptions = interfaceOptionsFrom(interfaceSettings);

            return proto().sendRequest(
                    Requests.joinGame()
                            .as(playerSettings.getRace(), playerSettings.getPlayerName())
                            .use(interfaceOptions)
                            .with(multiplayerOptions));
        } finally {
            setMultiplayer(isSet(multiplayerOptions));
        }
    }

    InterfaceOptions interfaceOptionsFrom(InterfaceSettings interfaceSettings) {
        FeatureLayerSyntax interfaces = InterfaceOptions.interfaces()
                .showCloaked(interfaceSettings.getShowCloaked())
                .raw()
                .rawCropToPlayableArea(interfaceSettings.getRawCropToPlayableArea())
                .rawAffectsSelection(interfaceSettings.getRawAffectsSelection())
                .score();
        interfaceSettings.getFeatureLayerSettings().ifPresent(interfaces::featureLayer);
        interfaceSettings.getRenderSettings().ifPresent(interfaces::render);
        return interfaces.build();
    }

    void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    @Override
    public boolean isMultiplayer() {
        return multiplayer;
    }

    @Override
    public boolean waitJoinGame(Maybe<Response> waitFor) {
        log.info("Waiting for the JoinGame response.");
        Optional<ResponseJoinGame> responseJoinGame = waitForResponse(waitFor)
                .flatMap(response -> response.as(ResponseJoinGame.class));

        if (responseJoinGame.isPresent()) {
            ResponseJoinGame joinGame = responseJoinGame.get();
            if (joinGame.getError().isPresent()) {
                log.error("Error in joining the game.");

                joinGame.getError().ifPresent(
                        errorCode -> log.error("JoinGame request returned an error code: {}", errorCode));
                joinGame.getErrorDetails().ifPresent(
                        errorDetails -> log.error("JoinGame request returned error details: {}", errorDetails));

                return false;
            }

            observationInternal().setPlayerId(joinGame.getPlayerId());

            log.info("WaitJoinGame finished successfully.");
            return true;
        } else {
            log.error("Response received is not JoinGame response.");
            return false;
        }
    }

    @Override
    public Maybe<Response> requestLeaveGame() {
        if (!multiplayer) throw new IllegalStateException("LeaveGame request is only available for multiplayer game.");
        return proto().sendRequest(Requests.leaveGame());
    }

    @Override
    public boolean pollLeaveGame(Maybe<Response> waitFor) {
        if (!multiplayer) throw new IllegalStateException("LeaveGame response is only available for multiplayer game.");
        return waitForResponse(waitFor).flatMap(response -> response.as(ResponseLeaveGame.class)).isPresent();
    }

    // TODO p.picheta to test
    @Override
    public boolean pollLeaveGame() {
        if (!multiplayer) return false;

        if (!proto().hasResponsePending(ResponseType.LEAVE_GAME)) {
            // If not in a game, then it is in the end state trying to leave the game.
            errorIf(proto().hasResponsePending(), ClientError.RESPONSE_NOT_CONSUMED, Collections.emptyList());
            return !isInGame();
        }

        return waitForResponse(getResponsePending(ResponseType.LEAVE_GAME))
                .flatMap(response -> response.as(ResponseLeaveGame.class))
                .isPresent();
    }

    @Override
    public Maybe<Response> step(int count) {
        checkApplicationState();
        return proto().sendRequest(Requests.nextStep().withCount(count));
    }

    private void checkApplicationState() {
        if (invalidState()) {
            throw new IllegalStateException(format("Invalid application state: %s", getAppState()));
        }
    }

    private boolean invalidState() {
        return !getAppState().equals(AppState.NORMAL);
    }

    @Override
    public boolean waitStep(Maybe<Response> waitFor) {
        return waitForResponse(waitFor).flatMap(response -> response.as(ResponseStep.class)).isPresent() &&
                getObservation();
    }

    @Override
    public boolean saveReplay(Path path) throws IOException {

        Optional<ResponseSaveReplay> responseSaveReplay =
                waitForResponse(proto().sendRequest(Requests.saveReplay()))
                        .flatMap(response -> response.as(ResponseSaveReplay.class));

        boolean success = responseSaveReplay.isPresent() && responseSaveReplay.get().getData().length > 0;

        if (success) {
            Files.write(path, responseSaveReplay.get().getData());
        }

        return success;
    }

    @Override
    public boolean ping() {
        return waitForResponse(proto().sendRequest(Requests.ping()))
                .flatMap(response -> response.as(ResponsePing.class))
                .isPresent();
    }

    @Override
    public void quit() {
        proto().quit();
        if (isSet(theGame)) {
            theGame.stop();
            theGame = null;
        }
    }

    @Override
    public Optional<Response> waitForResponse(Maybe<Response> responseMaybe) {
        checkApplicationState();
        try {
            Optional<Response> response = proto().waitForResponse(responseMaybe);
            if (!response.isPresent()) {
                onUnresponsiveServer();
            }

            Optional<ResponseError> error = response.flatMap(r -> r.as(ResponseError.class));
            if (error.isPresent()) {
                onError(ClientError.SC2_PROTOCOL_ERROR, error.get().getErrors());
                return response;
            }

            return response;
        } catch (ResponseParseException e) {
            onError(ClientError.INVALID_RESPONSE, Collections.emptyList());
            return Optional.empty();
        } catch (Exception e) {
            log.error("waitForResponse error", e);
            if (e.getCause() instanceof TimeoutException) {
                onUnresponsiveServer();
            }
            return Optional.empty();
        }
    }

    private void onUnresponsiveServer() {
        // The game application did not responded, the previous request was either not sent or the app is non-responsive.

        if (isSet(theGame)) {
            handleProcessError();
        } else {
            protocolTimeout();
        }
    }

    private void handleProcessError() {
        // Step 1: distinguish between a hang and a crash. Lots of time has elapsed, so if there was a crash
        // it should have finished by now.
        if (!theGame.getS2Process().isAlive()) {
            log.error("Game process is not alive.");
            onProcessUnexpectedlyClosed();
            return;
        }
        // Step 2: distinguish between a non-responsive app and a failure to deliver a valid request.
        if (!protoInternal().isConnected()) {
            log.error("Connection unexpectedly closed.");
            // Mark the game app as unresponsive.
            onConnectionUnexpectedlyClosed();
        } else {
            // Wait for a ping response. If this fails, the game is unresponsive.
            if (verifyGameResponsiveness()) return;
        }

        // The game application has hanged. Try and terminate it.
        tryTerminateHangedProcess();
    }

    private void onProcessUnexpectedlyClosed() {
        setAppState(AppState.CRASHED);
        onError(ClientError.SC2_APP_FAILURE, Collections.emptyList());
        log.error("Game application has terminated unexpectedly.");
    }

    private void onConnectionUnexpectedlyClosed() {
        protocolTimeout();
    }

    private void protocolTimeout() {
        setAppState(AppState.TIMEOUT);
        onError(ClientError.SC2_PROTOCOL_TIMEOUT, Collections.emptyList());
    }

    private boolean verifyGameResponsiveness() {
        log.info("Verifying game responsiveness...");
        try {
            proto().waitForResponse(proto().sendRequest(Requests.ping()));
            log.info("Game is responsive.");
            if (proto().lastStatus().equals(GameStatus.UNKNOWN)) {
                log.error("Game has unknown status.");
                onError(ClientError.SC2_UNKNOWN_STATUS, Collections.emptyList());
            } else {
                // The game is responsive, but there was another problem. This isn't the right
                // place to handle another type of problem. Just return the nullptr.
                onError(ClientError.SC2_UNKNOWN_STATUS, Collections.emptyList());
                return true;
            }
        } catch (Exception e) {
            log.error("Game is unresponsive.", e);
            protocolTimeout();
        }
        return false;
    }

    private void tryTerminateHangedProcess() {
        setAppState(AppState.TIMEOUT);
        if (!theGame.stopAndWait() || theGame.getS2Process().isAlive()) {
            log.error("Termination of hanged process failed.");
            // Failed to kill the running process.
            setAppState(AppState.TIMEOUT_ZOMBIE);
        }
        log.error("Game application has been terminated due to unresponsiveness.");
        onError(ClientError.SC2_APP_FAILURE, Collections.emptyList());
    }

    @Override
    public void setProcessInfo(ProcessInfo pi) {
        this.processInfo = pi;
    }

    @Override
    public ProcessInfo getProcessInfo() {
        return this.processInfo;
    }

    void setAppState(AppState appState) {
        require("application state", appState);
        this.appState = appState;
    }

    @Override
    public AppState getAppState() {
        return appState;
    }

    @Override
    public GameStatus getLastStatus() {
        return proto().lastStatus();
    }

    @Override
    public boolean isInGame() {
        return !invalidState() &&
                (getLastStatus().equals(GameStatus.IN_GAME) || getLastStatus().equals(GameStatus.IN_REPLAY));
    }

    @Override
    public boolean isFinishedGame() {
        if (invalidState()) return true;
        if (isInGame()) return false;
        return !hasResponsePending();
    }

    @Override
    public boolean isReadyForCreateGame() {
        if (invalidState()) return false;
        // Make sure the pipes are clear first.
        if (hasResponsePending()) return false;

        return (getLastStatus().equals(GameStatus.LAUNCHED) || getLastStatus().equals(GameStatus.ENDED));
    }

    @Override
    public boolean hasResponsePending(ResponseType responseType) {
        return proto().hasResponsePending(responseType);
    }

    @Override
    public boolean hasResponsePending() {
        return proto().hasResponsePending();
    }

    @Override
    public Maybe<Response> getResponsePending(ResponseType responseType) {
        return proto().getResponsePending(responseType);
    }

    @Override
    public boolean pollResponse(ResponseType responseType) {
        return proto().pollResponse(responseType);
    }

    @Override
    public boolean getObservation() {
        checkApplicationState();

        Optional<ResponseObservation> responseObservation = waitForResponse(proto().sendRequest(Requests.observation()))
                .flatMap(response -> response.as(ResponseObservation.class));

        return responseObservation.isPresent() && observationInternal().updateObservation(responseObservation.get());
    }

    @Override
    public boolean issueEvents(List<Tag> commands) {
        if (!observationInternal().gameLoopChanged()) return false;

        issueUnitDestroyedEvents();
        issueUnitAddedEvents();

        observation().getUnits(Alliance.SELF).forEach(unit -> {
            issueIdleEvent(unit, commands);
            issueBuildingCompletedEvent(unit);
        });

        issueUpgradeEvents();
        issueAlertEvents();

        // Run the users onStep function after events have been issued.
        clientEvents.onStep();

        return true;
    }

    private void issueUnitDestroyedEvents() {
        observationInternal().getRawObservation().getRaw()
                .flatMap(ObservationRaw::getEvent)
                .ifPresent(event -> event.getDeadUnits().forEach(tag ->
                        observationInternal().unitPool().getUnit(tag).ifPresent(unit -> {
                            observationInternal().unitPool().markDead(tag);
                            clientEvents.onUnitDestroyed(unit);
                        })));
    }

    private void issueUnitAddedEvents() {
        observationInternal().unitPool().forEachExistingUnit(unitInPool -> unitInPool.getUnit().ifPresent(unit -> {
            if (!hasPreviousState(unit)) {
                if (unit.getAlliance().equals(Alliance.ENEMY) && unit.getDisplayType().equals(DisplayType.VISIBLE)) {
                    clientEvents.onUnitEnterVision(unitInPool);
                } else if (unit.getAlliance().equals(Alliance.SELF)) {
                    clientEvents.onUnitCreated(unitInPool);
                }
            }
        }));

    }

    private boolean hasPreviousState(Unit unit) {
        return observation().getGameLoop() > 1 && observationInternal().unitPool().previous().containsKey(unit.getTag());
    }

    private void issueIdleEvent(UnitInPool unitInPool, List<Tag> commands) {
        unitInPool.getUnit()
                .filter(idleUnit())
                .ifPresent(unit -> {
                    // Lookup unit from previous map.
                    if (!hasPreviousState(unit)) {
                        // If it's not in the previous map it's a new unit with new orders so trigger the OnIdle event.
                        clientEvents.onUnitIdle(unitInPool);
                        return;
                    } else {
                        // Otherwise get that unit from the previous list and verify it's state changed to idle.
                        if (!idleUnit().test(getPreviousState(unit))) {
                            clientEvents.onUnitIdle(unitInPool);
                            return;
                        }
                    }

                    // Iterate the issued commands, if a unit exists in that list but does not currently have orders
                    // the order must have failed. Reissue the OnUnitIdle event in that case.
                    commands.stream()
                            .filter(tag -> tag.equals(unit.getTag()))
                            .findFirst()
                            .ifPresent(tag -> clientEvents.onUnitIdle(unitInPool));
                });
    }

    private Unit getPreviousState(Unit unit) {
        return observationInternal().unitPool().previous().get(unit.getTag());
    }

    private Predicate<Unit> idleUnit() {
        return unit -> unit.getOrders().isEmpty() && isBuild(unit);
    }

    private boolean isBuild(Unit unit) {
        return unit.getBuildProgress() == 1.0f;
    }

    private void issueBuildingCompletedEvent(UnitInPool unitInPool) {
        // If the units build progress is complete but it previously wasn't call construction complete
        unitInPool.getUnit()
                .filter(this::isBuild)
                .ifPresent(unit -> {
                    if (hasPreviousState(unit)) {
                        if (!isBuild(getPreviousState(unit))) {
                            clientEvents.onBuildingConstructionComplete(unitInPool);
                        }
                    }
                });
    }

    private void issueUpgradeEvents() {
        Set<Upgrade> upgradesPrevious = observationInternal().getUpgradesPrevious();
        observation().getUpgrades().forEach(upgrade -> {
            if (!upgradesPrevious.contains(upgrade)) {
                clientEvents.onUpgradeCompleted(upgrade);
            }
        });
    }

    private void issueAlertEvents() {
        observationInternal().getRawObservation().getAlerts().forEach(alert -> {
            switch (alert) {
                case NUCLEAR_LAUNCH_DETECTED:
                    clientEvents.onNuclearLaunchDetected();
                    break;
                case NYDUS_WORM_DETECTED:
                    clientEvents.onNydusDetected();
                    break;
                default:
                    clientEvents.onAlert(alert);
            }
        });
    }

    @Override
    public void onGameStart() {
        observation()
                .getUnits(Alliance.SELF, isMainBase())
                .stream()
                .findFirst()
                .ifPresent(unitInPool -> {
                    if (unitInPool.getUnit().isPresent()) {
                        observationInternal().setStartLocation(unitInPool.getUnit().get().getPosition());
                    }
                });
    }

    private Predicate<UnitInPool> isMainBase() {
        return unitInPool -> {
            Optional<Unit> unit = unitInPool.getUnit();
            if (unit.isPresent()) {
                UnitType type = unit.get().getType();
                return Set
                        .of((UnitType) Units.TERRAN_COMMAND_CENTER, Units.PROTOSS_NEXUS, Units.ZERG_HATCHERY)
                        .contains(type);
            } else {
                return false;
            }
        };
    }

    @Override
    public void dumpProtoUsage() {
        StringBuilder protoUsage = new StringBuilder();

        protoUsage.append("\n******************************************************\n");
        protoUsage.append("Protocol use by message type:\n");
        proto().getCountUses().forEach((responseType, count) -> {
            if (count != null) {
                protoUsage.append(responseType).append(": ").append(count).append("\n");
            }
        });

        protoUsage.append("******************************************************\n");

        log.info("Dump proto usage" + protoUsage.toString());
    }

    @Override
    public void error(ClientError clientError, List<String> errors) {
        onError(clientError, errors);
    }

    @Override
    public void errorIf(boolean condition, ClientError clientError, List<String> errors) {
        if (condition) onError(clientError, errors);
    }

    @Override
    public List<ClientError> getClientErrors() {
        return new ArrayList<>(clientErrors);
    }

    @Override
    public List<String> getProtocolErrors() {
        return new ArrayList<>(protocolErrors);
    }

    @Override
    public void clearClientErrors() {
        clientErrors.clear();
    }

    @Override
    public void clearProtocolErrors() {
        protocolErrors.clear();
    }

    @Override
    public void useGeneralizedAbility(boolean useGeneralizedAbilityId) {
        this.useGeneralizedAbilityId = useGeneralizedAbilityId;
    }

    @Override
    public boolean save() {
        return waitForResponse(proto().sendRequest(Requests.quickSave()))
                .flatMap(response -> response.as(ResponseQuickSave.class))
                .isPresent();
    }

    @Override
    public boolean load() {
        return waitForResponse(proto().sendRequest(Requests.quickLoad()))
                .flatMap(response -> response.as(ResponseQuickLoad.class))
                .isPresent();
    }

    @Override
    public AgentControlInterface agentControl() {
        return agentControlInterface;
    }

    @Override
    public QueryInterface query() {
        return queryInterface;
    }

    @Override
    public DebugInterface debug() {
        return debugInterface;
    }

    @Override
    public ObserverActionInterface observerAction() {
        return observerActionInterface;
    }

    @Override
    public ReplayControlInterface replayControl(S2ReplayObserver replayObserver) {
        return new ReplayControlInterfaceImpl(this, replayObserver);
    }

    boolean isUseGeneralizedAbilityId() {
        return useGeneralizedAbilityId;
    }

    // test purposes only
    S2Controller getTheGame() {
        return theGame;
    }

    // test purposes only
    void disconnect() {
        protoInternal().disconnect();
    }

    // test purposes only
    void forceStop() {
        if (isSet(theGame)) {
            theGame.stopAndWait();
            theGame = null;
        }
    }

    ClientEvents clientEvents() {
        return clientEvents;
    }

    ObservationInterfaceImpl observationInternal() {
        return observationInterface;
    }

    private ProtoInterfaceImpl protoInternal() {
        return protoInterface;
    }

    @Override
    public String toString() {
        return "ControlInterfaceImpl{" +
                "clientErrors=" + clientErrors +
                ", protocolErrors=" + protocolErrors +
                ", appState=" + appState +
                ", multiplayer=" + multiplayer +
                '}';
    }

}
