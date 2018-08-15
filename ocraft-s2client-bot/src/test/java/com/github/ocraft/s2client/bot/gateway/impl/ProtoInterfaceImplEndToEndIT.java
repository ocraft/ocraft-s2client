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

import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.gateway.ProtoInterface;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.request.Requests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("endtoend")
class ProtoInterfaceImplEndToEndIT {

    private static final int TEST_TIMEOUT = 30000;

    private S2Controller theGame;

    @AfterEach
    void tearDown() {
        if (isSet(theGame)) {
            theGame.stopAndWait();
            theGame = null;
        }
    }

    @Test
    void connectsToTheGame() {
        theGame = S2Controller.starcraft2Game().launch().untilReady();
        ProtoInterfaceImpl protoInterface = new ProtoInterfaceImpl();

        assertThat(protoInterface.connectToGame(theGame, TEST_TIMEOUT, TEST_TIMEOUT, true)).isTrue();
        assertThat(protoInterface.getBaseBuild()).isNotNull();
        assertThat(protoInterface.getDataVersion()).isNotNull();
        protoInterface.quit();
    }

    @Test
    void publishesConnectionClosedError() {
        List<ClientError> clientErrors = new ArrayList<>();

        theGame = S2Controller.starcraft2Game().launch().untilReady();
        ProtoInterfaceImpl protoInterface = new ProtoInterfaceImpl();
        protoInterface.setOnError((cErr, pErr) -> clientErrors.add(cErr));
        assertThat(protoInterface.connectToGame(theGame, TEST_TIMEOUT, TEST_TIMEOUT, true)).isTrue();
        theGame.stop();

        await().atMost(TEST_TIMEOUT, TimeUnit.SECONDS)
                .until(() -> clientErrors.contains(ClientError.CONNECTION_CLOSED));
    }

    @Test
    void returnsFalseAfterConnectionTimeout() {
        assertThat(new ProtoInterfaceImpl().connectToGame("127.0.0.1", 5000, 1500, 1500, true)).isFalse();
    }

    @Test
    void storesLatestGameStatus() {
        theGame = S2Controller.starcraft2Game().launch().untilReady();
        ProtoInterface protoInterface = new ProtoInterfaceImpl();
        assertThat(protoInterface.connectToGame(theGame, TEST_TIMEOUT, TEST_TIMEOUT, true)).isTrue();

        protoInterface.waitForResponse(protoInterface.sendRequest(Requests.ping()));

        assertThat(protoInterface.lastStatus()).isEqualTo(GameStatus.LAUNCHED);
        protoInterface.quit();
    }
}
