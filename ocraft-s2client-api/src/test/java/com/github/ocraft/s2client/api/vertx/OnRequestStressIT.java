package com.github.ocraft.s2client.api.vertx;

import com.github.ocraft.s2client.api.OcraftConfig;
import com.github.ocraft.test.MultiThreadedStressTester;
import io.vertx.reactivex.core.http.WebSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.github.ocraft.s2client.api.Configs.refreshConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OnRequestStressIT {

    private static final int THREAD_COUNT = 10;
    private static final int ITERATION_COUNT = 10000;

    @BeforeEach
    void setUp() {
        System.setProperty(OcraftConfig.CLIENT_BUFFER_SIZE_REQUEST_EVENT_BUS, String.valueOf(ITERATION_COUNT));
        System.setProperty(OcraftConfig.CLIENT_BUFFER_SIZE_REQUEST_QUEUE, String.valueOf(ITERATION_COUNT));
        refreshConfig();
    }

    @AfterEach
    void tearDown() {
        System.clearProperty(OcraftConfig.CLIENT_BUFFER_SIZE_REQUEST_EVENT_BUS);
        System.clearProperty(OcraftConfig.CLIENT_BUFFER_SIZE_REQUEST_QUEUE);
        refreshConfig();
    }

    @Test
    void worksCorrectlyUnderStress() throws Exception {
        AtomicInteger counter = new AtomicInteger();
        OnRequest onRequest = new OnRequest(mock(VertxChannel.class));

        MultiThreadedStressTester stress = new MultiThreadedStressTester(THREAD_COUNT, ITERATION_COUNT).stress(() -> {
            onRequest.onNext(msg());
            if (shouldDisconnect(counter)) onRequest.onConnectionLost();
            if (shouldConnect(counter)) onRequest.onConnected(webSocket());
        }, true);
        stress.shutdown();

        assertThat(stress.errorOccurred()).as("Error occurred under stress test").isFalse();
    }

    private byte[] msg() {
        return new byte[]{0xF};
    }

    private boolean shouldDisconnect(AtomicInteger counter) {
        return counter.incrementAndGet() % 100 == 0;
    }

    private boolean shouldConnect(AtomicInteger counter) {
        return counter.get() % 150 == 0;
    }

    private WebSocket webSocket() {
        WebSocket mock = mock(WebSocket.class);
        io.vertx.core.http.WebSocket webSocket = mock(io.vertx.core.http.WebSocket.class);
        when(webSocket.writeBinaryMessage(any())).thenReturn(webSocket);

        when(mock.getDelegate()).thenReturn(webSocket);
        return mock;
    }

}