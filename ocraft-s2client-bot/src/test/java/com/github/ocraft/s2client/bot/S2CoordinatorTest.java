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

import com.github.ocraft.s2client.bot.gateway.ControlInterface;
import com.github.ocraft.s2client.bot.setting.GameSettings;
import com.github.ocraft.s2client.bot.setting.InterfaceSettings;
import com.github.ocraft.s2client.bot.setting.PlayerSettings;
import com.github.ocraft.s2client.bot.setting.ProcessSettings;
import com.github.ocraft.s2client.protocol.Defaults;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.github.ocraft.s2client.bot.S2Coordinator.createComputer;
import static com.github.ocraft.s2client.bot.S2Coordinator.createParticipant;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class S2CoordinatorTest {
    private static final String CFG_IP = "127.0.0.1";
    private static final int CFG_PORT = 5000;
    private static final boolean CFG_MULTITHREADED = true;
    private static final boolean CFG_REALTIME = true;
    private static final int CFG_STEP_SIZE = 5;
    private static final Path CFG_PROCESS_PATH = Paths.get("/tmp/sc2/Versions/Base58400/SC2_x64.exe");
    private static final String CFG_DATA_VERSION = "5BD7C31B44525DAB46E64C4602A81DC2";
    private static final int CFG_TIMEOUT_IN_MILLIS = 6000;
    private static final int CFG_PORT_START = 4000;
    private static final int CFG_WINDOWS_WIDTH = 1024;
    private static final int CFG_WINDOW_HEIGHT = 768;
    private static final int CFG_WINDOW_X = 10;
    private static final int CFG_WINDOW_Y = 15;
    private static final boolean CFG_WITH_GAME_CONTROLLER = true;
    private static final boolean CFG_VERBOSE = true;
    private static final Path CFG_TMP_DIR = Paths.get("/tmp");
    private static final Path CFG_DATA_DIR = Paths.get("/tmp/data");
    private static final Path CFG_OS_MESA_PATH = Paths.get("/tmp/osmesa");
    private static final Path CFG_EGL_PATH = Paths.get("/tmp/egl");
    private static final String CFG_MAP_NAME = "Lava Flow";

    @Test
    void providesSetupForTheGameServer() {
        S2Agent agent01 = makeAgent();
        S2Agent agent02 = makeAgent();

        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .setProcessPath(CFG_PROCESS_PATH)
                .setDataVersion(CFG_DATA_VERSION)
                .setTimeoutMS(CFG_TIMEOUT_IN_MILLIS)
                .setPortStart(CFG_PORT_START)
                .setWindowSize(CFG_WINDOWS_WIDTH, CFG_WINDOW_HEIGHT)
                .setWindowLocation(CFG_WINDOW_X, CFG_WINDOW_Y)
                .setVerbose(CFG_VERBOSE)
                .setTmpDir(CFG_TMP_DIR)
                .setDataDir(CFG_DATA_DIR)
                .setOsMesaPath(CFG_OS_MESA_PATH)
                .setEglPath(CFG_EGL_PATH)
                .setParticipants(
                        createParticipant(Race.PROTOSS, agent01),
                        createParticipant(Race.ZERG, agent02),
                        createComputer(Race.RANDOM, Difficulty.VERY_EASY))
                .launchStarcraft();

        ProcessSettings expectedProcessSettings = expectedProcessSettingsForLaunch();

        assertThat(s2Coordinator.getProcessSettings()).isEqualTo(fetchPortsFor(expectedProcessSettings, 2));
        assertThat(s2Coordinator.getAgents()).containsExactly(agent01, agent02);

        verify(agent01.control()).connect(expectedProcessSettings);
        verify(agent02.control()).connect(expectedProcessSettings);
    }

    private S2Agent makeAgent() {
        S2Agent agent = mock(S2Agent.class);
        ControlInterface controlInterface = mock(ControlInterface.class);
        doAnswer(invocationOnMock -> {
            ProcessSettings settings = invocationOnMock.getArgument(0);
            if (isSet(settings.getPortSetup()) && settings.withGameController()) settings.getPortSetup().fetchPort();
            return true;
        }).when(controlInterface).connect(any(ProcessSettings.class));
        when(agent.control()).thenReturn(controlInterface);
        return agent;
    }

    private ProcessSettings expectedProcessSettingsForLaunch() {
        return new ProcessSettings()
                .setProcessPath(CFG_PROCESS_PATH)
                .setDataVersion(CFG_DATA_VERSION)
                .setRequestTimeoutMS(CFG_TIMEOUT_IN_MILLIS)
                .setConnectionTimeoutMS(CFG_TIMEOUT_IN_MILLIS)
                .setPortStart(CFG_PORT_START)
                .setWindowSize(CFG_WINDOWS_WIDTH, CFG_WINDOW_HEIGHT)
                .setWindowLocation(CFG_WINDOW_X, CFG_WINDOW_Y)
                .setWithGameController(CFG_WITH_GAME_CONTROLLER)
                .setVerbose(CFG_VERBOSE)
                .setTmpDir(CFG_TMP_DIR)
                .setDataDir(CFG_DATA_DIR)
                .setOsMesaPath(CFG_OS_MESA_PATH)
                .setEglPath(CFG_EGL_PATH);
    }

    private ProcessSettings fetchPortsFor(ProcessSettings processSettings, int agentCount) {
        if (processSettings.withGameController()) {
            processSettings.getPortSetup().fetchPort();
        }
        Stream.iterate(0, i -> i < agentCount, i -> i + 1).forEach(i -> processSettings.getPortSetup().fetchPort());
        return processSettings;
    }

    @Test
    void connectsToExistingGameServer() {
        S2Agent agent01 = makeAgent();

        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .setMultithreaded(CFG_MULTITHREADED)
                .setRealtime(CFG_REALTIME)
                .setStepSize(CFG_STEP_SIZE)
                .setParticipants(
                        createParticipant(Race.PROTOSS, agent01),
                        createComputer(Race.RANDOM, Difficulty.VERY_EASY))
                .connect(CFG_IP, CFG_PORT);

        ProcessSettings expectedProcessSettings = expectedProcessSettingsForConnect();

        assertThat(s2Coordinator.getProcessSettings()).isEqualTo(fetchPortsFor(expectedProcessSettings, 0));
        assertThat(s2Coordinator.getAgents()).containsExactly(agent01);

        verify(agent01.control()).connect(expectedProcessSettings);
    }

    private ProcessSettings expectedProcessSettingsForConnect() {
        return new ProcessSettings()
                .setMultithreaded(CFG_MULTITHREADED)
                .setRealtime(CFG_REALTIME)
                .setStepSize(CFG_STEP_SIZE)
                .setPortStart(CFG_PORT)
                .setConnection(CFG_IP, CFG_PORT)
                .setWithGameController(false);
    }

    @Test
    void providesSetupForInterfaceOptions() {
        SpatialCameraSetup spatialCameraSetup = Defaults.defaultSpatialSetup();

        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .setRender(spatialCameraSetup)
                .setFeatureLayers(spatialCameraSetup)
                .setShowCloaked(true)
                .setRawAffectsSelection(true)
                .setRawCropToPlayableArea(true)
                .setParticipants(createParticipant(Race.PROTOSS, makeAgent()))
                .launchStarcraft();

        assertThat(s2Coordinator.getInterfaceSettings())
                .isEqualTo(new InterfaceSettings(spatialCameraSetup, spatialCameraSetup, true, true, true));
    }

    @Test
    void loadsSettingsFromCli() {
        S2Agent agent = makeAgent();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(new String[]{
                        "-e", CFG_PROCESS_PATH.toString(),
                        "-s", "5",
                        "-p", "4000",
                        "-r", "true",
                        "-m", "Lava Flow",
                        "-t", "6000"
                })
                .setParticipants(createParticipant(Race.PROTOSS, agent))
                .launchStarcraft();

        ProcessSettings expectedProcessSettings = new ProcessSettings()
                .setWithGameController(true)
                .setProcessPath(CFG_PROCESS_PATH)
                .setStepSize(CFG_STEP_SIZE)
                .setPortStart(CFG_PORT_START)
                .setRealtime(CFG_REALTIME)
                .setRequestTimeoutMS(CFG_TIMEOUT_IN_MILLIS)
                .setConnectionTimeoutMS(CFG_TIMEOUT_IN_MILLIS);

        assertThat(s2Coordinator.getProcessSettings()).isEqualTo(fetchPortsFor(expectedProcessSettings, 0));

        assertThat(s2Coordinator.getGameSettings())
                .isEqualTo(new GameSettings()
                        .setBattlenetMap(BattlenetMap.of(CFG_MAP_NAME))
                        .setPlayerSettings(singletonList(PlayerSettings.participant(Race.PROTOSS, agent))));
    }

    @Test
    void detectsLocalMapPath() {
        S2Agent agent = makeAgent();
        String localMap = "test.SC2Map";
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(new String[]{"-m", localMap})
                .setParticipants(createParticipant(Race.PROTOSS, agent))
                .launchStarcraft();

        assertThat(s2Coordinator.getGameSettings())
                .isEqualTo(new GameSettings()
                        .setLocalMap(LocalMap.of(Paths.get(localMap)))
                        .setPlayerSettings(singletonList(PlayerSettings.participant(Race.PROTOSS, agent))));
    }

    @Test
    void throwsExceptionWhenAgentListIsEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> S2Coordinator.setup().setParticipants().launchStarcraft())
                .withMessage("one of agents or replay observers is required");
    }

    @Test
    void connectsToTheLadderServerWithComputerOpponent() {
        S2Agent agent = makeAgent();
        int portStart = 7890;

        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadLadderSettings(new String[]{
                        "-g", String.valueOf(CFG_PORT),
                        "-o", String.valueOf(portStart),
                        "-l", CFG_IP,
                        "-c", "true",
                        "-a", "Zerg",
                        "-d", "VeryHard",
                })
                .setParticipants(createParticipant(Race.PROTOSS, agent))
                .connectToLadder();

        ProcessSettings expectedProcessSettings = expectedProcessSettingsForLadder(portStart);

        assertThat(s2Coordinator.getProcessSettings()).isEqualTo(fetchPortsFor(expectedProcessSettings, 0));
        assertThat(s2Coordinator.getAgents()).containsExactly(agent);
        assertThat(s2Coordinator.getGameSettings().getMultiplayerOptions()).isNull();
        assertThat(s2Coordinator.getGameSettings().getPlayerSettings()).hasSize(2);


        verify(agent.control()).connect(expectedProcessSettings);
    }

    private ProcessSettings expectedProcessSettingsForLadder(Integer portStart) {
        return new ProcessSettings()
                .setPortStart(portStart)
                .setConnection(CFG_IP, CFG_PORT)
                .setLadderGame(true)
                .setWithGameController(false);
    }

    @Test
    void connectsToTheLadderServer() {
        S2Agent agent = makeAgent();
        int portStart = 7890;

        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadLadderSettings(new String[]{
                        "-g", String.valueOf(CFG_PORT),
                        "-o", String.valueOf(portStart),
                        "-l", CFG_IP
                })
                .setParticipants(createParticipant(Race.PROTOSS, agent))
                .connectToLadder();

        ProcessSettings expectedProcessSettings = expectedProcessSettingsForLadder(portStart);

        assertThat(s2Coordinator.getProcessSettings()).isEqualTo(fetchPortsFor(expectedProcessSettings, 1));
        assertThat(s2Coordinator.getAgents()).containsExactly(agent);
        assertThat(s2Coordinator.getGameSettings().getMultiplayerOptions()).isNotNull();
        assertThat(s2Coordinator.getGameSettings().getPlayerSettings()).hasSize(1);


        verify(agent.control()).connect(expectedProcessSettings);
    }


    // TODO p.picheta setReplayPath/loadreplayList/setReplayRecovery/addReplayObserver
}
