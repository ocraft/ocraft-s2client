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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@Tag("integration")
class AgentControlInterfaceImplIT {
    @Test
    void handlesRestartGameCommunication() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasRestartGame, GameServerResponses::restartGame);
        gameSetup.server().onRequest(Sc2Api.Request::hasObservation, GameServerResponses::observation);

        assertThat(gameSetup.control().agentControl().restart()).as("restart game status").isTrue();
        verify(gameSetup.clientEvents()).onGameStart();

        gameSetup.stop();
    }

    @Test
    void handlesErrorOfRestartGame() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasRestartGame, GameServerResponses::restartGameWithError);

        assertThat(gameSetup.control().agentControl().restart()).as("restart game status").isFalse();
        verifyNoMoreInteractions(gameSetup.clientEvents());

        gameSetup.stop();
    }
}
