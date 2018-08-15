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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.bot.GameServerResponses;
import com.github.ocraft.s2client.bot.S2ReplayObserver;
import com.github.ocraft.s2client.bot.gateway.ControlInterface;
import com.github.ocraft.s2client.bot.gateway.ReplayControlInterface;
import com.github.ocraft.s2client.bot.setting.InterfaceSettings;
import com.github.ocraft.s2client.protocol.Defaults;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Tag("integration")
class ReplayControlInterfaceImplIT {

    private final Path REPLAY_PATH = Paths.get("/tst");

    @Test
    void handlesReplays() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasReplayInfo, GameServerResponses::replayInfo);
        gameSetup.server().onRequest(Sc2Api.Request::hasStartReplay, GameServerResponses::startReplay);

        S2ReplayObserver replayObserver = mockReplayObserver();

        ReplayControlInterface replayControl = gameSetup.control().replayControl(replayObserver);
        replayControl.useGeneralizedAbility(true);

        assertThat(replayControl.gatherReplayInfo(REPLAY_PATH, true)).as("status of gather replay").isTrue();
        assertThat(replayControl.getReplayInfo()).isNotNull();
        assertThat(replayControl.waitForReplay(
                replayControl.loadReplay(
                        REPLAY_PATH,
                        new InterfaceSettings(Defaults.defaultSpatialSetup(), Defaults.defaultSpatialSetup()),
                        1,
                        true))
        ).as("status of replay loading").isTrue();
        verify(replayObserver.control()).onGameStart();
        verify(replayObserver).onGameStart();

        gameSetup.stop();
    }

    private S2ReplayObserver mockReplayObserver() {
        S2ReplayObserver replayObserver = mock(S2ReplayObserver.class);
        ControlInterface replayObserverControl = mock(ControlInterface.class);
        when(replayObserver.control()).thenReturn(replayObserverControl);
        return replayObserver;
    }

    @Test
    void handlesErrorOfReplayInfo() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasReplayInfo, GameServerResponses::replayInfoWithError);

        S2ReplayObserver replayObserver = mockReplayObserver();

        ReplayControlInterface replayControl = gameSetup.control().replayControl(replayObserver);

        assertThat(replayControl.gatherReplayInfo(REPLAY_PATH, true)).as("status of gather replay").isFalse();

        gameSetup.stop();
    }

    @Test
    void handlesErrorOfStartReplay() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasStartReplay, GameServerResponses::startReplayWithError);

        assertLoadReplayError(gameSetup);

        gameSetup.stop();
    }

    private void assertLoadReplayError(GameSetup gameSetup) {
        S2ReplayObserver replayObserver = mockReplayObserver();

        ReplayControlInterface replayControl = gameSetup.control().replayControl(replayObserver);

        assertThat(replayControl.waitForReplay(
                replayControl.loadReplay(
                        REPLAY_PATH,
                        new InterfaceSettings(Defaults.defaultSpatialSetup(), Defaults.defaultSpatialSetup()),
                        1,
                        true))
        ).as("status of replay loading").isFalse();
    }

    @Test
    void handlesErrorOfInvalidGameState() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasStartReplay, GameServerResponses::startReplayWithInvalidState);

        assertLoadReplayError(gameSetup);

        gameSetup.stop();
    }
}
