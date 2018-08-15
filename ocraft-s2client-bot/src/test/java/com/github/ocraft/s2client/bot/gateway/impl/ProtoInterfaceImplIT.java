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
import com.github.ocraft.s2client.api.test.GameServer;
import com.github.ocraft.s2client.bot.GameServerResponses;
import com.github.ocraft.s2client.bot.gateway.ProtoInterface;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
class ProtoInterfaceImplIT {

    private static final int GAME_SERVER_PORT = 4000;
    private static final int TEST_TIMEOUT_MS = 3000;

    @Test
    void throwsExceptionIfPingAfterConnectFails() {
        GameServer gameServer = GameServer.create(GAME_SERVER_PORT).start();
        gameServer.onRequest(Sc2Api.Request::hasPing, GameServerResponses.limitedCorrectPingResponses(1));

        ProtoInterface protoInterface = new ProtoInterfaceImpl();
        assertThat(protoInterface.connectToGame("127.0.0.1", GAME_SERVER_PORT, TEST_TIMEOUT_MS, TEST_TIMEOUT_MS, true))
                .as("state of game connection attempt").isFalse();
        gameServer.stop();
    }

}
