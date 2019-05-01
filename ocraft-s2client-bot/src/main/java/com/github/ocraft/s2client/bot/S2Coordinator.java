package com.github.ocraft.s2client.bot;

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
import com.github.ocraft.s2client.api.controller.ExecutableParser;
import com.github.ocraft.s2client.bot.gateway.ActionInterface;
import com.github.ocraft.s2client.bot.gateway.AppState;
import com.github.ocraft.s2client.bot.gateway.ControlInterface;
import com.github.ocraft.s2client.bot.setting.*;
import com.github.ocraft.s2client.bot.syntax.SettingsSyntax;
import com.github.ocraft.s2client.bot.syntax.StartGameSyntax;
import com.github.ocraft.s2client.protocol.game.*;
import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;
import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.*;
import static com.github.ocraft.s2client.protocol.game.MultiplayerOptions.multiplayerSetupFor;
import static java.util.Arrays.asList;

/**
 * Coordinator of one or more clients. Used to start, step and stop games and replays.
 */
public class S2Coordinator {

    private static Logger log = LoggerFactory.getLogger(S2Coordinator.class);

    private final List<S2Agent> agents;
    private final List<S2ReplayObserver> replayObservers;

    // TODO p.picheta refactor settings to immutables
    private final InterfaceSettings interfaceSettings;
    private final ProcessSettings processSettings;
    private final ReplaySettings replaySettings;
    private final GameSettings gameSettings;

    private final boolean useGeneralizedAbilityId;


    private S2Coordinator(Builder builder) {
        oneOfIsNotEmpty("agents or replay observers", builder.agents, builder.replayObservers);

        agents = builder.agents;
        replayObservers = builder.replayObservers;
        processSettings = builder.processSettings;
        gameSettings = builder.gameSettings;

        agents.forEach(agent -> {
            if (!agent.control().connect(processSettings)) {
                log.error("Failed to attach to starcraft.");
                throw new IllegalStateException("Failed to attach to starcraft.");
            }
        });

        replayObservers.forEach(replayObserver -> {
            if (!replayObserver.control().connect(processSettings)) {
                log.error("Failed to attach to starcraft.");
                throw new IllegalStateException("Failed to attach to starcraft.");
            }
        });

        interfaceSettings = new InterfaceSettings(
                builder.featureLayerSettings,
                builder.renderSettings,
                builder.showCloaked,
                builder.rawAffectsSelection,
                builder.rawCropToPlayableArea);
        replaySettings = builder.replaySettings;
        useGeneralizedAbilityId = builder.useGeneralizedAbilityId;

        if (processSettings.isLadderGame() && !builder.ladderSettings.getComputerOpponent()) {
            setupPorts(agents.size() + 1, () -> processSettings.getPortSetup().fetchPort(), false);
        } else {
            setupPorts(agents.size(), () -> processSettings.getPortSetup().fetchPort(), true);
        }

        gameSettings.resolveMap(processSettings);
    }

    /**
     * Sets up the sc2 game ports to use
     *
     * @param numberOfAgents Number of agents in the game
     * @param portStart      Starting port number
     * @param checkSingle    Checks if the game is a single player or multiplayer game
     */
    public void setupPorts(int numberOfAgents, Supplier<Integer> portStart, boolean checkSingle) {
        int bots;
        if (checkSingle) {
            bots = (int) gameSettings.getPlayerSettings().stream()
                    .filter(player -> player.getPlayerSetup().getPlayerType().equals(PlayerType.PARTICIPANT))
                    .count();
        } else {
            bots = numberOfAgents;
        }
        if (bots > 1) {
            gameSettings.setMultiplayerOptions(multiplayerSetupFor(portStart.get(), bots));
            log.info("Setting multiplayer options: {}", gameSettings.getMultiplayerOptions());
        }
    }

    public static SettingsSyntax setup() {
        return new Builder();
    }

    // Initialization and setup.
    private static class Builder implements SettingsSyntax, StartGameSyntax {

        private List<S2Agent> agents = new ArrayList<>();
        private List<S2ReplayObserver> replayObservers = new ArrayList<>();

        private ProcessSettings processSettings = new ProcessSettings();
        private ReplaySettings replaySettings = new ReplaySettings();
        private GameSettings gameSettings = new GameSettings();
        private CliSettings cliSettings = new CliSettings();
        private LadderSettings ladderSettings = new LadderSettings();

        private SpatialCameraSetup featureLayerSettings;
        private SpatialCameraSetup renderSettings;

        private boolean useGeneralizedAbilityId = true; // TODO p.picheta move to settings class?
        private Boolean showCloaked;
        private Boolean rawAffectsSelection;
        private Boolean rawCropToPlayableArea;

        private Builder() {
            if (OcraftBotConfig.cfg().hasPath(OcraftBotConfig.BOT_MAP)) {
                gameSettings.resolveMap(OcraftBotConfig.cfg().getString(OcraftBotConfig.BOT_MAP));
            }
        }

        @Override
        public SettingsSyntax loadSettings(String[] args) {
            try {
                CommandLine.populateCommand(cliSettings, args);

                if (cliSettings.isUsageHelpRequested()) {
                    printHelp();
                    System.exit(0);
                }

                Map<String, Object> exeSettings = tryDiscoverDefaultSettings(args);

                setProcessPath(cliSettings.getProcessPath().orElse(
                        Optional.ofNullable(exeSettings.get(OcraftApiConfig.GAME_EXE_PATH))
                                .map(p -> Paths.get(String.valueOf(p)))
                                .orElse(nothing())));
                setStepSize(cliSettings.getStepSize().orElse(nothing()));
                setPortStart(cliSettings.getPort().orElse(nothing()));
                setRealtime(cliSettings.getRealtime().orElse(nothing()));
                gameSettings.resolveMap(cliSettings.getMapName().orElse(nothing()));
                setTimeoutMS(cliSettings.getTimeout().orElse(nothing()));

            } catch (Exception e) {
                printHelp();
                throw e;
            }

            return this;
        }

        private Map<String, Object> tryDiscoverDefaultSettings(String[] args) {
            Map<String, Object> exeSettings = new HashMap<>();
            try {
                exeSettings = ExecutableParser.loadSettings(null, null, null);
            } catch (Exception e) {
                log.debug("Auto-discovering of Starcraft II configuration failed", e);
                if (args.length == 0) {
                    System.err.println("Please run StarCraft II before running this API");
                    printHelp();
                    System.exit(0);
                }
            }
            return exeSettings;
        }

        private void printHelp() {
            CommandLine.usage(cliSettings, System.out);
        }

        @Override
        public SettingsSyntax loadLadderSettings(String[] args) {
            try {
                CommandLine cmd = new CommandLine(ladderSettings);
                cmd.registerConverter(Race.class, Race::forName);
                cmd.registerConverter(Difficulty.class, Difficulty::forName);
                cmd.parse(args);

                if (ladderSettings.isUsageHelpRequested()) {
                    printLadderHelp();
                    System.exit(0);
                }

                setPortStart(ladderSettings.getStartPort());
                processSettings.setConnection(ladderSettings.getLadderServer(), ladderSettings.getGamePort());
                if (ladderSettings.getComputerOpponent()) {
                    setParticipants(createComputer(
                            ladderSettings.getComputerRace().orElseThrow(required("computer race")),
                            ladderSettings.getComputerDifficulty().orElseThrow(required("computer difficulty"))));
                }

            } catch (Exception e) {
                printLadderHelp();
                throw e;
            }

            return this;
        }

        private void printLadderHelp() {
            CommandLine.usage(ladderSettings, System.out);
        }


        @Override
        public SettingsSyntax setMultithreaded(Boolean value) {
            if (isSet(value)) processSettings.setMultithreaded(value);
            return this;
        }

        @Override
        public SettingsSyntax setRealtime(Boolean value) {
            if (isSet(value)) processSettings.setRealtime(value);
            return this;
        }

        @Override
        public SettingsSyntax setStepSize(Integer stepSize) {
            if (isSet(stepSize)) processSettings.setStepSize(stepSize);
            return this;
        }

        @Override
        public SettingsSyntax setProcessPath(Path path) {
            if (isSet(path)) processSettings.setProcessPath(path);
            return this;
        }

        @Override
        public SettingsSyntax setDataVersion(String version) {
            if (isSet(version)) processSettings.setDataVersion(version);
            return this;
        }

        @Override
        public SettingsSyntax setTimeoutMS(Integer timeoutInMillis) {
            if (isSet(timeoutInMillis)) {
                processSettings.setRequestTimeoutMS(timeoutInMillis).setConnectionTimeoutMS(timeoutInMillis);
            }
            return this;
        }

        @Override
        public SettingsSyntax setPortStart(Integer portStart) {
            if (isSet(portStart)) processSettings.setPortStart(portStart);
            return this;
        }

        @Override
        public SettingsSyntax setFeatureLayers(SpatialCameraSetup settings) {
            if (isSet(settings)) this.featureLayerSettings = settings;
            return this;
        }

        @Override
        public SettingsSyntax setRender(SpatialCameraSetup settings) {
            if (isSet(settings)) this.renderSettings = settings;
            return this;
        }

        @Override
        public SettingsSyntax setWindowSize(Integer width, Integer height) {
            if (isSet(width) && isSet(height)) processSettings.setWindowSize(width, height);
            return this;
        }

        @Override
        public SettingsSyntax setWindowLocation(Integer x, Integer y) {
            if (isSet(x) && isSet(y)) processSettings.setWindowLocation(x, y);
            return this;
        }

        @Override
        public SettingsSyntax setUseGeneralizedAbilityId(Boolean value) {
            if (isSet(value)) this.useGeneralizedAbilityId = value;
            return this;
        }

        @Override
        public SettingsSyntax setVerbose(Boolean value) {
            if (isSet(value)) processSettings.setVerbose(value);
            return this;
        }

        @Override
        public SettingsSyntax setNeedsSupportDir(Boolean value) {
            if (isSet(value)) processSettings.setNeedsSupportDir(value);
            return this;
        }

        @Override
        public SettingsSyntax setTraced(Boolean value) {
            if (isSet(value)) processSettings.setTraced(value);
            return this;
        }

        @Override
        public SettingsSyntax setTmpDir(Path tmpDirPath) {
            if (isSet(tmpDirPath)) processSettings.setTmpDir(tmpDirPath);
            return this;
        }

        @Override
        public SettingsSyntax setDataDir(Path dataDirPath) {
            if (isSet(dataDirPath)) processSettings.setDataDir(dataDirPath);
            return this;
        }

        @Override
        public SettingsSyntax setOsMesaPath(Path osMesaPath) {
            if (isSet(osMesaPath)) processSettings.setOsMesaPath(osMesaPath);
            return this;
        }

        @Override
        public SettingsSyntax setEglPath(Path eglPath) {
            if (isSet(eglPath)) processSettings.setEglPath(eglPath);
            return this;
        }

        @Override
        public SettingsSyntax setShowCloaked(Boolean showCloaked) {
            if (isSet(showCloaked)) this.showCloaked = showCloaked;
            return this;
        }

        @Override
        public SettingsSyntax setRawAffectsSelection(Boolean rawAffectsSelection) {
            if (isSet(rawAffectsSelection)) this.rawAffectsSelection = rawAffectsSelection;
            return this;
        }

        @Override
        public SettingsSyntax setRawCropToPlayableArea(Boolean rawCropToPlayableArea) {
            if (isSet(rawCropToPlayableArea)) this.rawCropToPlayableArea = rawCropToPlayableArea;
            return this;
        }

        @Override
        public StartGameSyntax setParticipants(PlayerSettings... participants) {
            if (isEmpty(participants)) return this;
            List<PlayerSettings> playerSettings = new ArrayList<>();
            asList(participants).forEach(participant -> {
                if (participant.getAgent() != null) agents.add(participant.getAgent());
                playerSettings.add(participant);
            });
            gameSettings.setPlayerSettings(playerSettings);
            return this;
        }

        @Override
        public SettingsSyntax setReplayRecovery(Boolean value) {
            if (isSet(value)) replaySettings.setReplayRecovery(value);
            return this;
        }

        @Override
        public SettingsSyntax addReplayObserver(S2ReplayObserver replayObserver) {
            if (isSet(replayObserver)) this.replayObservers.add(replayObserver);
            return this;
        }

        @Override
        public SettingsSyntax setReplayPath(Path path) throws IOException {
            if (isSet(path)) replaySettings.setReplayPath(path);
            return this;
        }

        @Override
        public SettingsSyntax loadReplayList(Path path) throws IOException {
            if (isSet(path)) replaySettings.loadReplayList(path);
            return this;
        }

        @Override
        public S2Coordinator launchStarcraft() {
            try {
                processSettings.setWithGameController(true);
                return new S2Coordinator(this);
            } catch (Exception e) {
                printHelp();
                throw e;
            }
        }

        @Override
        public S2Coordinator connect(String ip, Integer port) {
            try {
                processSettings.setConnection(ip, port);
                processSettings.setWithGameController(false);
                return new S2Coordinator(this);
            } catch (Exception e) {
                printHelp();
                throw e;
            }
        }

        @Override
        public S2Coordinator connectToLadder() {
            try {
                processSettings.setWithGameController(false);
                processSettings.setLadderGame(true);
                return new S2Coordinator(this);
            } catch (Exception e) {
                printLadderHelp();
                throw e;
            }
        }
    }

    // Start-up

    /**
     * Starts a game on a certain map. There are multiple ways to specify a map:
     * Absolute path: Any .SC2Map file.
     * Relative path: Any .SC2Map file relative to either the library or installation maps folder.
     * Map name: Any BattleNet published map that has been locally cached.
     *
     * @see #startGame(BattlenetMap)
     * @see #startGame(LocalMap)
     */
    public S2Coordinator startGame() {
        createGame();
        return joinGame();
    }

    /**
     * Starts a game on a certain map. There are multiple ways to specify a map:
     * Absolute path: Any .SC2Map file.
     * Relative path: Any .SC2Map file relative to either the library or installation maps folder.
     * Map name: Any BattleNet published map that has been locally cached.
     *
     * @param localMap Path to the map to run.
     * @see #startGame(BattlenetMap)
     * @see #startGame()
     */
    public S2Coordinator startGame(LocalMap localMap) {
        require("local map", localMap);
        gameSettings.setLocalMap(localMap);
        gameSettings.resolveMap(processSettings);
        return startGame();
    }

    /**
     * Starts a game on a certain map. There are multiple ways to specify a map:
     * Absolute path: Any .SC2Map file.
     * Relative path: Any .SC2Map file relative to either the library or installation maps folder.
     * Map name: Any BattleNet published map that has been locally cached.
     *
     * @param battlenetMap Name of the battlenet map to run.
     * @see #startGame(LocalMap)
     * @see #startGame()
     */
    public S2Coordinator startGame(BattlenetMap battlenetMap) {
        require("battlenet map", battlenetMap);
        gameSettings.setBattlenetMap(battlenetMap);
        return startGame();
    }

    /**
     * Creates a game but does not join the bots to the game
     *
     * @see #createGame(BattlenetMap)
     * @see #createGame(LocalMap)
     */
    public S2Coordinator createGame() {
//        // Create the game with the first client.
        S2Agent firstClient = agents.get(0);

        Optional<BattlenetMap> battlenetMap = gameSettings.getBattlenetMap();
        if (battlenetMap.isPresent()) {
            firstClient.control().createGame(
                    battlenetMap.get(),
                    gameSettings.getPlayerSettings(),
                    processSettings.getRealtime());
        } else {
            Optional<LocalMap> localMap = gameSettings.getLocalMap();
            if (localMap.isPresent()) {
                firstClient.control().createGame(
                        localMap.get(),
                        gameSettings.getPlayerSettings(),
                        processSettings.getRealtime());
            } else {
                throw new IllegalArgumentException("map is required");
            }
        }
        return this;
    }


    /**
     * Creates a game but does not join the bots to the game
     *
     * @param localMap Path to the map to run.
     * @see #createGame(BattlenetMap)
     * @see #createGame()
     */
    public S2Coordinator createGame(LocalMap localMap) {
        require("local map", localMap);
        gameSettings.setLocalMap(localMap);
        return createGame();
    }

    /**
     * Creates a game but does not join the bots to the game
     *
     * @param battlenetMap Name of the battlenet map to run.
     * @see #createGame(LocalMap)
     * @see #createGame()
     */
    public S2Coordinator createGame(BattlenetMap battlenetMap) {
        require("battlenet map", battlenetMap);
        gameSettings.setBattlenetMap(battlenetMap);
        return createGame();
    }

    /**
     * Joins agents to the game
     */
    public S2Coordinator joinGame() {
        Map<S2Agent, Maybe<Response>> waitForJoin = new HashMap<>();
        agents.forEach(agent -> waitForJoin.put(agent, agent.control().requestJoinGame(
                gameSettings.playerSettingsFor(agent).orElseThrow(required("player settings")),
                interfaceSettings,
                gameSettings.getMultiplayerOptions())));

        agents.forEach(agent -> agent.control().waitJoinGame(waitForJoin.get(agent)));

        // TODO p.picheta to test
        // Check if any errors occurred during game start.
        boolean errorOccurred = false;
        for (S2Agent agent : agents) {
            if (!agent.control().getClientErrors().isEmpty()) {
                agent.onError(agent.control().getClientErrors(), agent.control().getProtocolErrors());
                errorOccurred = true;
            }
        }

        if (errorOccurred) {
            throw new IllegalStateException("Game failed to start");
        }

        agents.forEach(agent -> agent.control().useGeneralizedAbility(useGeneralizedAbilityId));
        agents.forEach(agent -> agent.control().getObservation());
        agents.forEach(ClientEvents::onGameFullStart);
        agents.forEach(agent -> {
            agent.control().onGameStart();
            agent.onGameStart();
        });
        agents.forEach(agent -> agent.control().issueEvents(agent.actions().commands()));

        return this;
    }

    // Run.

    /**
     * Helper function used to actually run a bot. This function will behave differently in real-time compared to
     * non real-time. In real-time there is no step sent over the wire but instead will request and read observations
     * as the game runs.
     * <p>
     * For non-real time Update will perform the following:
     * <ol>
     * <li>Step the simulation forward by a certain amount of game steps, this essentially moves the game loops
     * forward.</li>
     * <li>Wait for the step to complete, the step is completed when a response is received and read from
     * the StarCraft II binary. When the step is completed an Observation has been received. It is parsed and various
     * client events are dispatched.</li>
     * <li>Call the user's onStep function.</li>
     * </ol>
     * <p>
     * Real time applications will perform the following:
     * <ol>
     * <li>The Observation is directly requested. The process will block while waiting for it.</li>
     * <li>The Observation is parsed and client events are dispatched.</li>
     * <li>Unit actions batched from the ActionInterface are dispatched.</li>
     * </ol>
     *
     * @return False if the game has ended, true otherwise.
     */
    public boolean update() {
        if (!agents.isEmpty()) {
            if (processSettings.getRealtime()) {
                stepAgentsRealtime();
            } else {
                stepAgents();
            }
        }

        if (!replayObservers.isEmpty()) {
            if (processSettings.getRealtime()) {
                stepReplayObserversRealtime();
            } else {
                stepReplayObservers();
            }

            if (anyObserverAvailable()) {
                startReplay();
            }
        }
        // Check for errors in all agents/replay observers at the end of an update.
        boolean errorOccurred = false;
        for (S2Agent agent : agents) {
            ControlInterface control = agent.control();
            if (!control.getClientErrors().isEmpty()) {
                agent.onError(control.getClientErrors(), control.getProtocolErrors());
                errorOccurred = true;
            }
        }

        boolean relaunched = false;
        for (S2ReplayObserver replayObserver : replayObservers) {
            ControlInterface control = replayObserver.control();
            if (!control.getClientErrors().isEmpty()) {
                replayObserver.onError(control.getClientErrors(), control.getProtocolErrors());
                errorOccurred = true;
                if (replaySettings.isReplayRecovery()) {
                    // An error did occur but if we successfully recovered ignore it. The client will still gets
                    // its event
                    boolean connected = relaunch(replayObserver);
                    if (connected) {
                        errorOccurred = false;
                        relaunched = true;
                    }
                }
            }
        }

        // End the coordinator update on the idea that an error in the API should mean it's time to stop.
        if (errorOccurred) {
            return false;
        }

        return !allGamesEnded() || relaunched;
    }

    private void stepAgentsRealtime() {
        if (processSettings.getMultithreaded()) {
            runParallel(stepAgentRealtime());
        } else {
            agents.forEach(stepAgentRealtime());
        }
    }

    private Consumer<S2Agent> stepAgentRealtime() {
        return agent -> {
            ControlInterface control = agent.control();
            if (control.getAppState() != AppState.NORMAL) return;
            if (control.pollLeaveGame()) return;
            if (control.isFinishedGame()) return;

            ActionInterface actions = agent.actions();
            // This agent shouldn't call step since it's real time.
            control.getObservation();
            control.issueEvents(actions.commands());
            actions.sendActions();

            if (!control.isInGame()) {
                agent.onGameEnd();
                if (control.isMultiplayer()) control.requestLeaveGame();  // Only for multiplayer.
            }
        };
    }

    private void stepAgents() {
        if (agents.size() == 1) {
            stepAgent().accept(agents.get(0));
        } else {
            runParallel(stepAgent());
        }
        if (!processSettings.getMultithreaded()) {
            agents.stream()
                    .filter(agent -> agent.control().getAppState() == AppState.NORMAL)
                    .filter(agent -> !agent.control().pollLeaveGame())
                    .forEach(this::callOnStep);
        }
    }

    private Consumer<S2Agent> stepAgent() {
        return agent -> {
            ControlInterface control = agent.control();
            if (control.getAppState() != AppState.NORMAL) return;
            if (control.pollLeaveGame()) return;
            if (control.isFinishedGame()) return;

            control.waitStep(control.step(processSettings.getStepSize()));
            if (processSettings.getMultithreaded()) {
                callOnStep(agent);
            }
        };
    }

    private void callOnStep(S2Agent agent) {
        ControlInterface control = agent.control();
        if (!control.isInGame()) {
            agent.onGameEnd();
            if (control.isMultiplayer()) control.requestLeaveGame();
            return;
        }
        ActionInterface actions = agent.actions();
        control.issueEvents(actions.commands());
        actions.sendActions();
        agent.actionsFeatureLayer().sendActions();
    }

    private void runParallel(Consumer<S2Agent> step) {
        agents.parallelStream().forEach(step);
    }

    private boolean anyObserverAvailable() {
        return replayObservers.stream().anyMatch(replayObserver -> !replayObserver.control().isInGame());
    }

    private boolean startReplay() {
        // If no replays given in the settings don't try.
        if (replaySettings.getReplayFiles().isEmpty()) return false;
        if (replayObservers.isEmpty()) return false;

        // Run a replay with each available replay observer.
        replayObservers.forEach(replayObserver -> {
            // If the replay observer is idle or out of game use it for a new replay.
            if (!replayObserver.control().isReadyForCreateGame()) return;
            replayObserver.replayControl().useGeneralizedAbility(useGeneralizedAbilityId);

            List<Path> replays = replaySettings.getReplayFiles();
            while (!replays.isEmpty()) {
                Path replay = replays.get(replays.size() - 1);
                if (shouldIgnore(replayObserver, replay)) {
                    replays.remove(replays.size() - 1);
                    continue;
                }
                if (shouldRelaunch(replayObserver)) break;
                boolean launched = !replayObserver.replayControl()
                        .loadReplay(replay, interfaceSettings, 1, processSettings.getRealtime())
                        .isEmpty()
                        .blockingGet();
                replays.remove(replays.size() - 1);
                if (launched) break;
            }

        });

        return true;
    }

    private boolean shouldIgnore(S2ReplayObserver replayObserver, Path replay) {
        if (replay.toString().isEmpty()) return true;

        // Gather replay information with the available observer.
        replayObserver.replayControl().gatherReplayInfo(replay, true);

        // If the replay isn't being pruned based on replay info start it.
        return replayObserver.ignoreReplay(replayObserver.replayControl().getReplayInfo(), 1);
    }

    private boolean shouldRelaunch(S2ReplayObserver replayObserver) {
        ReplayInfo replayInfo = replayObserver.replayControl().getReplayInfo();

        boolean versionMatch = replayInfo.getBaseBuild() == replayObserver.control().proto().getBaseBuild() &&
                replayInfo.getDataVersion().equals(replayObserver.control().proto().getDataVersion());

        if (versionMatch) return false;

        // Version failed to download. Just continue with trying to load in current version.
        // It will likely fail, and then just skip past this replay.
        if (!ExecutableParser.findBaseExe(
                processSettings.getRootPath(),
                replayInfo.getBaseBuild(),
                processSettings.getActualProcessPath().getFileName().toString())) {
            return false;
        }

        log.warn("Replay is from a different version. Relaunching client into the correct version...");
        processSettings.setDataVersion(replayInfo.getDataVersion());
        processSettings.setBaseBuild(replayInfo.getBaseBuild());
        processSettings.setRootPath(null);
        processSettings.setActualProcessPath(null);
        replayObserver.control().error(ClientError.WRONG_GAME_VERSION, Collections.emptyList());
        return true;
    }

    private void stepReplayObservers() {
        // Run all replay observers.
        if (replayObservers.size() == 1) {
            runReplay().accept(replayObservers.get(0));
        } else {
            // Run all steps in parallel.
            replayObservers.parallelStream().forEach(runReplay());
        }
        // Do everyone's OnStep, if not multi threaded, in single threaded mode.
        if (!processSettings.getMultithreaded()) {
            replayObservers.stream()
                    .filter(replayObserver -> replayObserver.control().getAppState().equals(AppState.NORMAL))
                    .forEach(replayObserver -> {
                        replayObserver.control().issueEvents(Collections.emptyList());
                        replayObserver.observerAction().sendActions();
                    });
        }
    }

    private Consumer<S2ReplayObserver> runReplay() {
        return replayObserver -> {
            if (!replayObserver.control().getAppState().equals(AppState.NORMAL)) return;

            // If the replay is loading wait for it to finish loading before performing a step.
            if (replayObserver.control().hasResponsePending(ResponseType.START_REPLAY)) {
                // Don't consume a response if there isn't one in the queue.
                if (replayObservers.size() > 1 && !replayObserver.control().pollResponse(ResponseType.START_REPLAY)) {
                    return;
                }
                replayObserver.replayControl()
                        .waitForReplay(replayObserver.control().getResponsePending(ResponseType.START_REPLAY));
            }

            if (replayObserver.control().isInGame()) {
                replayObserver.control().waitStep(replayObserver.control().step(processSettings.getStepSize()));

                // If multithreaded run everyone's OnStep in parallel.
                if (processSettings.getMultithreaded()) {
                    replayObserver.control().issueEvents(Collections.emptyList());
                    replayObserver.observerAction().sendActions();
                }

                if (!replayObserver.control().isInGame()) {
                    replayObserver.onGameEnd();
                }

            }
        };
    }

    private void stepReplayObserversRealtime() {
        // Run all replay observers.
        if (replayObservers.size() == 1) {
            runReplayRealtime().accept(replayObservers.get(0));
        } else {
            // Run all steps in parallel.
            replayObservers.parallelStream().forEach(runReplayRealtime());
        }
        // Do everyone's OnStep, if not multi threaded, in single threaded mode.
        if (!processSettings.getMultithreaded()) {
            replayObservers.stream()
                    .filter(replayObserver -> replayObserver.control().getAppState().equals(AppState.NORMAL))
                    .forEach(replayObserver -> replayObserver.control().issueEvents(Collections.emptyList()));
        }
    }

    private Consumer<S2ReplayObserver> runReplayRealtime() {
        return replayObserver -> {
            if (!replayObserver.control().getAppState().equals(AppState.NORMAL)) return;

            // If the replay is loading wait for it to finish loading before performing a step.
            if (replayObserver.control().hasResponsePending(ResponseType.START_REPLAY)) {
                // Don't consume a response if there isn't one in the queue.
                if (replayObservers.size() > 1 && !replayObserver.control().pollResponse(ResponseType.START_REPLAY)) {
                    return;
                }
                replayObserver.replayControl()
                        .waitForReplay(replayObserver.control().getResponsePending(ResponseType.START_REPLAY));
            }

            if (replayObserver.control().isInGame()) {
                replayObserver.control().getObservation();

                // If multithreaded run everyone's OnStep in parallel.
                if (processSettings.getMultithreaded()) {
                    replayObserver.control().issueEvents(Collections.emptyList());
                }

                if (!replayObserver.control().isInGame()) {
                    replayObserver.onGameEnd();
                }

            }
        };
    }

    private boolean relaunch(S2ReplayObserver replayObserver) {
        // TODO p.picheta to test
        replayObserver.reset();

        return replayObserver.control()
                .connect(processSettings.setPortStart(processSettings.getPortSetup().fetchPort()));
    }

    // TODO p.picheta WaitForAllResponses

    /**
     * Requests for the currently running game to end.
     */
    public void leaveGame() {
        // TODO p.picheta to test
        agents.forEach(agent -> agent.control().requestLeaveGame());
    }

    // Status.

    /**
     * Returns true if all running games have ended.
     */
    public boolean allGamesEnded() {
        // TODO p.picheta to test
        return gamesEndedForAgents() && gamesEndedForReplayObservers();
    }

    private boolean gamesEndedForAgents() {
        return agents.stream()
                .noneMatch(agent -> agent.control().isInGame() || agent.control().hasResponsePending());
    }

    private boolean gamesEndedForReplayObservers() {
        return replayObservers.stream()
                .noneMatch(replayObserver ->
                        replayObserver.control().isInGame() || replayObserver.control().hasResponsePending());
    }

    // Replay specific.

    /**
     * Saves replays to a file.
     *
     * @param path The file path.
     */
    public S2Coordinator saveReplayList(Path path) throws IOException {
        // TODO p.picheta to test

        Files.write(
                path,
                (Iterable<String>) replaySettings.getReplayFiles().stream()
                        .map(p -> p.toString() + System.lineSeparator())::iterator,
                Charset.forName("UTF-8"),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);

        return this;
    }

    /**
     * Determines if there are unprocessed replays.
     *
     * @return Is true if there are replays left.
     */
    public boolean hasReplays() {
        return !replaySettings.getReplayFiles().isEmpty();
    }

    // Misc.

    /**
     * Saves a binary blob as a map to a remote location.
     *
     * @param data The map data.
     * @param path The file path to save the data to.
     */
    public boolean remoteSaveMap(byte[] data, Path path) {
        return agents.stream().allMatch(agent -> agent.control().remoteSaveMap(LocalMap.of(path, data))) &&
                replayObservers.stream()
                        .allMatch(replayObserver -> replayObserver.control().remoteSaveMap(LocalMap.of(path, data)));
    }

    /**
     * Gets the game executable path.
     *
     * @return The game executable path.
     */
    public Path getExePath() {
        return processSettings.getActualProcessPath();
    }

    public void quit() {
        agents.forEach(agent -> agent.control().quit());
        replayObservers.forEach(replayObserver -> replayObserver.control().quit());
    }

    public static PlayerSettings createParticipant(Race race, S2Agent bot) {
        return PlayerSettings.participant(race, bot);
    }

    public static PlayerSettings createComputer(Race race, Difficulty difficulty) {
        return PlayerSettings.computer(race, difficulty);
    }

    public static PlayerSettings createParticipant(Race race, S2Agent bot, String playerName) {
        return PlayerSettings.participant(race, bot, playerName);
    }

    public static PlayerSettings createComputer(Race race, Difficulty difficulty, String playerName) {
        return PlayerSettings.computer(race, difficulty, playerName);
    }

    public static PlayerSettings createComputer(Race race, Difficulty difficulty, AiBuild aiBuild) {
        return PlayerSettings.computer(race, difficulty, aiBuild);
    }

    public static PlayerSettings createComputer(Race race, Difficulty difficulty, String playerName, AiBuild aiBuild) {
        return PlayerSettings.computer(race, difficulty, playerName, aiBuild);
    }

    List<S2Agent> getAgents() {
        return new ArrayList<>(agents);
    }

    List<S2ReplayObserver> getReplayObservers() {
        return new ArrayList<>(replayObservers);
    }

    InterfaceSettings getInterfaceSettings() {
        return interfaceSettings;
    }

    ProcessSettings getProcessSettings() {
        return processSettings;
    }

    ReplaySettings getReplaySettings() {
        return replaySettings;
    }

    GameSettings getGameSettings() {
        return gameSettings;
    }

    boolean isUseGeneralizedAbilityId() {
        return useGeneralizedAbilityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        S2Coordinator that = (S2Coordinator) o;

        if (useGeneralizedAbilityId != that.useGeneralizedAbilityId) return false;
        if (!Objects.equals(agents, that.agents)) return false;
        if (!Objects.equals(replayObservers, that.replayObservers))
            return false;
        if (!Objects.equals(interfaceSettings, that.interfaceSettings))
            return false;
        if (!Objects.equals(processSettings, that.processSettings))
            return false;
        if (!Objects.equals(replaySettings, that.replaySettings))
            return false;
        return Objects.equals(gameSettings, that.gameSettings);
    }

    @Override
    public int hashCode() {
        int result = agents != null ? agents.hashCode() : 0;
        result = 31 * result + (replayObservers != null ? replayObservers.hashCode() : 0);
        result = 31 * result + (interfaceSettings != null ? interfaceSettings.hashCode() : 0);
        result = 31 * result + (processSettings != null ? processSettings.hashCode() : 0);
        result = 31 * result + (replaySettings != null ? replaySettings.hashCode() : 0);
        result = 31 * result + (gameSettings != null ? gameSettings.hashCode() : 0);
        result = 31 * result + (useGeneralizedAbilityId ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "S2Coordinator{" +
                "agents=" + agents +
                ", replayObservers=" + replayObservers +
                ", interfaceSettings=" + interfaceSettings +
                ", processSettings=" + processSettings +
                ", replaySettings=" + replaySettings +
                ", gameSettings=" + gameSettings +
                ", useGeneralizedAbilityId=" + useGeneralizedAbilityId +
                '}';
    }
}
