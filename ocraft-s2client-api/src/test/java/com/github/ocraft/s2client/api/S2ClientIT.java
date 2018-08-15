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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.api.test.GameServer;
import com.github.ocraft.s2client.protocol.request.Requests;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.api.S2Client.starcraft2Client;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Tag("integration")
class S2ClientIT {

    @Test
    void throwsExceptionWhenPingFailsAfterSuccessfulConnection() {
        int port = 6000;
        GameServer gameServer = GameServer.create(port);
        gameServer.start()
                .onRequest(Sc2Api.Request::hasPing, () -> Sc2Api.Response.newBuilder().addError("ping error").build());

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> starcraft2Client().connectTo("127.0.0.1", port).start().untilReady())
                .withMessage("Client is already closed.");

        gameServer.stop();
    }

    @Test
    void doesNotStopAfterResponseParseException() {
        int port = 6000;
        GameServer gameServer = GameServer.create(port);
        gameServer.start().onRequest(
                Sc2Api.Request::hasObservation,
                () -> Sc2Api.Response.newBuilder()
                        .setObservation(Sc2Api.ResponseObservation.newBuilder().build()).build());
        S2Client s2Client = starcraft2Client().connectTo("127.0.0.1", port).start();

        TestS2ClientSubscriber testS2ClientSubscriber = new TestS2ClientSubscriber();
        s2Client.responseStream().subscribe(testS2ClientSubscriber);
        s2Client.request(Requests.observation());

        testS2ClientSubscriber.awaitTerminalEvent();
        testS2ClientSubscriber.assertError(ResponseParseException.class);
        assertThat(s2Client.isDone()).as("client status after response parse error").isFalse();
        gameServer.stop();
    }

}
