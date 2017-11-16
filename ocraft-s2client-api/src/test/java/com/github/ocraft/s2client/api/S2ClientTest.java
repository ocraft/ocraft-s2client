package com.github.ocraft.s2client.api;

import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.api.log.DataFlowTracer;
import com.github.ocraft.s2client.api.syntax.TracedSyntax;
import com.github.ocraft.s2client.protocol.request.Request;
import com.github.ocraft.s2client.protocol.response.ResponsePing;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.api.S2Client.starcraft2Client;
import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.request.Requests.ping;
import static com.github.ocraft.test.Threads.delay;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class S2ClientTest {

    private static final byte[] sc2ApiResponse = new byte[]{
            -102, 1, 56, 10, 12, 51, 46, 49, 55, 46, 49, 46, 53, 55, 50, 49, 56, 18, 32, 51, 70, 50, 70, 67, 69, 68, 48,
            56, 55, 57, 56, 68, 56, 51, 66, 56, 55, 51, 66, 53, 53, 52, 51, 66, 69, 70, 65, 54, 67, 52, 66, 24, -126,
            -65, 3, 32, -45, -69, 3};

    @Test
    void throwsExceptionWhenIpIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> starcraft2Client().connectTo(nothing(), 5000).start())
                .withMessage("ip is required");
    }


    @Test
    void throwsExceptionIfTracerIsNotSetButRequired() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> aSampleS2Client().traced(true).withTracer(nothing()).start())
                .withMessage("data flow tracer is required");
    }

    private TracedSyntax aSampleS2Client() {
        return starcraft2Client().connectTo("127.0.0.1", 5000);
    }

    @Test
    void throwsExceptionForNullRequest() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> aSampleS2Client().start().request((Request) nothing()))
                .withMessage("request is required");
    }

    @Test
    void providesDataFlowMonitoringIfNeeded() {
        DataFlowTracer tracer = mock(DataFlowTracer.class);
        S2Client client = aSampleS2Client().traced(true).withTracer(tracer).start();
        client.responseStream().subscribe(r -> {
        });

        client.request(ping());
        client.channel().output(sc2ApiResponse);
        delay(100);

        verify(tracer).fire(ping());
        verify(tracer).fire(any(ResponsePing.class));
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
        when(s2Controller.getConfig()).thenReturn(OcraftConfig.cfg());
        return s2Controller;
    }

}