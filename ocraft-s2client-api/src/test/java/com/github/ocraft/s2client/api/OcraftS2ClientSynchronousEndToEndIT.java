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

import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.response.ResponseJoinGame;
import com.github.ocraft.s2client.protocol.response.ResponsePing;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.github.ocraft.s2client.api.Configs.refreshConfig;
import static com.github.ocraft.s2client.api.S2Client.starcraft2Client;
import static com.github.ocraft.s2client.api.controller.S2Controller.starcraft2Game;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.request.Requests.joinGame;
import static com.github.ocraft.s2client.protocol.request.Requests.ping;
import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Tag("endtoend")
class OcraftS2ClientSynchronousEndToEndIT {

    private static final long CONNECTION_TIMEOUT_IN_SECONDS = 60;

    private S2Client client;
    private S2Controller game;

    @AfterEach
    void reset() {
        if (isSet(client)) {
            client.stop();
            client = null;
        }
        if (isSet(game)) {
            game.stopAndWait();
            game = null;
        }
    }

    @Test
    void sendsSynchronousRequests() {
        launchTheGame();

        ResponsePing responsePing = client.requestSync(ping(), ResponsePing.class);

        assertThat(responsePing).isNotNull();
    }

    private void launchTheGame() {
        game = starcraft2Game().launch().untilReady();
        client = starcraft2Client().connectTo(game).traced(true).start();
    }

    @Test
    void throwsTimeoutExceptionWhenResponseDoesNotAppearForTooLong() {
        System.setProperty(OcraftApiConfig.CLIENT_NET_SYNCH_REQUEST_TIMEOUT, String.valueOf(100L));
        refreshConfig();

        startClientWithoutGameServer();

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> client.requestSync(ping(), ResponsePing.class))
                .withCauseInstanceOf(TimeoutException.class);

        System.clearProperty(OcraftApiConfig.CLIENT_NET_SYNCH_REQUEST_TIMEOUT);
        refreshConfig();
    }

    private void startClientWithoutGameServer() {
        client = starcraft2Client().connectTo("127.0.0.1", 5000).traced(true).start();
    }

    @Test
    void worksWithAsynchronousMode() {
        launchTheGame();

        TestS2ClientSubscriber subscriber = new TestS2ClientSubscriber();
        client.responseStream().subscribe(subscriber);
        ResponsePing responsePing = client.requestSync(ping(), ResponsePing.class);

        assertThat(responsePing).isNotNull();
        subscriber.eventuallyReceivedExactly(1);
    }

    @Test
    void throwsResponseErrorExceptionForErrorsReceivedFromGameServer() {
        launchTheGame();

        assertThatExceptionOfType(ResponseErrorException.class)
                .isThrownBy(() -> client.requestSync(joinGame().as(Race.PROTOSS), ResponseJoinGame.class));
    }

    @Test
    void waitsForSuccessfulConnection() {
        launchTheGame();
        await().atMost(CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS).until(() -> {
            try {
                client.untilReady();
            } catch (TimeoutException e) {
                throw new AssertionError(e);
            }
        });
    }

    @Test
    void throwsExceptionOnConnectionTimeout() {
        assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> starcraft2Client()
                        .connectTo("127.0.0.1", 5000)
                        .connectTimeout(2000)
                        .start()
                        .untilReady());
    }

}
