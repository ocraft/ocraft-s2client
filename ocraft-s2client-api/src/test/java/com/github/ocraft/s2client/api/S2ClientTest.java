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
import com.github.ocraft.s2client.api.log.DataFlowTracer;
import com.github.ocraft.s2client.api.syntax.TracedSyntax;
import com.github.ocraft.s2client.protocol.request.Request;
import com.github.ocraft.s2client.protocol.request.RequestPing;
import com.github.ocraft.s2client.protocol.response.ResponsePing;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.api.Configs.refreshConfig;
import static com.github.ocraft.s2client.api.S2Client.starcraft2Client;
import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.request.Requests.ping;
import static com.github.ocraft.s2client.test.Threads.delay;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class S2ClientTest {

    private static final byte[] sc2ApiResponse = new byte[]{
            -102, 1, 56, 10, 12, 51, 46, 49, 55, 46, 49, 46, 53, 55, 50, 49, 56, 18, 32, 51, 70, 50, 70, 67, 69, 68, 48,
            56, 55, 57, 56, 68, 56, 51, 66, 56, 55, 51, 66, 53, 53, 52, 51, 66, 69, 70, 65, 54, 67, 52, 66, 24, -126,
            -65, 3, 32, -45, -69, 3};

    @Test
    void throwsExceptionForNullRequest() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> aSampleS2Client().start().request((Request) nothing()))
                .withMessage("request is required");
    }

    private TracedSyntax aSampleS2Client() {
        return starcraft2Client().connectTo("127.0.0.1", 5000);
    }

    @Test
    void providesDataFlowMonitoringIfNeeded() {
        DataFlowTracer tracer = mock(DataFlowTracer.class);
        S2Client client = aSampleS2Client().traced(true).withTracer(tracer).start();
        client.responseStream().subscribe(r -> {
        });

        RequestPing ping = ping();
        client.request(ping);
        client.channel().output(sc2ApiResponse);
        delay(200);

        verify(tracer).fire(ping);
        verify(tracer).fire(any(ResponsePing.class));

        client.stop();
    }

    @Test
    void doesNotUseTracingIfDisabled() {
        DataFlowTracer tracer = mock(DataFlowTracer.class);
        S2Client client = aSampleS2Client().traced(false).withTracer(tracer).start();
        client.request(ping());
        verifyNoMoreInteractions(tracer);
    }

    @Test
    void runsUntilObserved() {
        TestS2ClientSubscriber subscriber01 = new TestS2ClientSubscriber();
        TestS2ClientSubscriber subscriber02 = new TestS2ClientSubscriber();
        S2Controller theGame = theGame();
        S2Client client = starcraft2Client().connectTo(theGame).start();

        client.responseStream().subscribe(subscriber01);
        client.responseStream().subscribe(subscriber02);

        subscriber01.cancelAfter(100);
        subscriber02.cancelAfter(300);

        client.await();

        assertThat(subscriber01.isCancelled()).as("subscriber 01 is cancelled").isTrue();
        assertThat(subscriber02.isCancelled()).as("subscriber 02 is cancelled").isTrue();
        assertThat(client.isDone()).as("client is done").isTrue();
        verify(theGame).stop();
    }

    private S2Controller theGame() {
        S2Controller s2Controller = mock(S2Controller.class);
        when(s2Controller.getConfig()).thenReturn(OcraftApiConfig.cfg());
        return s2Controller;
    }

    @Test
    void usesDefaultNetConfigurationIfNotProvided() {
        System.setProperty(OcraftApiConfig.CLIENT_NET_IP, "192.168.1.1");
        System.setProperty(OcraftApiConfig.CLIENT_NET_PORT, "1000");
        System.setProperty(OcraftApiConfig.CLIENT_NET_SYNCH_REQUEST_TIMEOUT, "100");
        System.setProperty(OcraftApiConfig.CLIENT_NET_CONNECT_TIMEOUT, "101");
        refreshConfig();

        S2Client s2Client = starcraft2Client().start();

        assertThat(s2Client.getConnectToIp()).as("default game ip").isEqualTo("192.168.1.1");
        assertThat(s2Client.getConnectToPort()).as("default game port").isEqualTo(1000);
        assertThat(s2Client.getRequestTimeoutInMillis()).as("default synchronous request timeout").isEqualTo(100);
        assertThat(s2Client.getConnectTimeoutInMillis()).as("default connect timeout").isEqualTo(101);

        System.clearProperty(OcraftApiConfig.CLIENT_NET_IP);
        System.clearProperty(OcraftApiConfig.CLIENT_NET_PORT);
        System.clearProperty(OcraftApiConfig.CLIENT_NET_SYNCH_REQUEST_TIMEOUT);
        System.clearProperty(OcraftApiConfig.CLIENT_NET_CONNECT_TIMEOUT);
        refreshConfig();

        s2Client.stop();
    }

}
