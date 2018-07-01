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
import com.github.ocraft.test.MultiThreadedStressTester;
import io.reactivex.exceptions.MissingBackpressureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.nio.BufferOverflowException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.github.ocraft.s2client.api.Configs.refreshConfig;
import static com.github.ocraft.s2client.api.S2Client.starcraft2Client;
import static com.github.ocraft.s2client.api.controller.S2Controller.starcraft2Game;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.request.Requests.ping;
import static com.github.ocraft.test.Threads.delay;
import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.core.IsEqual.equalTo;

class OcraftS2ClientStressIT {

    private static final int OBSERVER_COUNT = 4;
    private static final int THREAD_COUNT = 10;
    private static final int ITERATION_COUNT = 10000;
    private static final int REQUEST_DELAY_IN_MILLIS = 1;
    private static final int TIMEOUT_IN_SECONDS = 5;
    private static final int CONNECTION_TIMEOUT_IN_SECONDS = 30;

    private S2Controller game;
    private S2Client client;

    @BeforeEach
    void setUp() {
        System.setProperty(OcraftConfig.CLIENT_BUFFER_SIZE_REQUEST_EVENT_BUS, String.valueOf(ITERATION_COUNT));
        System.setProperty(OcraftConfig.CLIENT_BUFFER_SIZE_REQUEST_QUEUE, String.valueOf(ITERATION_COUNT));
        refreshConfig();
    }

    @AfterEach
    void tearDown() {
        if (isSet(client)) {
            client.stop();
            client = null;
        }
        if (isSet(game)) {
            game.stopAndWait();
            game = null;
        }

        System.clearProperty(OcraftConfig.CLIENT_BUFFER_SIZE_RESPONSE_STREAM);
        System.clearProperty(OcraftConfig.CLIENT_BUFFER_SIZE_RESPONSE_EVENT_BUS);
        System.clearProperty(OcraftConfig.CLIENT_BUFFER_SIZE_RESPONSE_BACKPRESSURE);
        System.clearProperty(OcraftConfig.CLIENT_BUFFER_SIZE_REQUEST_EVENT_BUS);
        System.clearProperty(OcraftConfig.CLIENT_BUFFER_SIZE_REQUEST_QUEUE);
        refreshConfig();
    }

    @Test
    void worksCorrectlyUnderMultiThreadedStress() throws Exception {
        System.setProperty(
                OcraftConfig.CLIENT_BUFFER_SIZE_RESPONSE_EVENT_BUS, String.valueOf(ITERATION_COUNT * THREAD_COUNT));
        refreshConfig();

        launchTheGame();

        AtomicInteger responseCounter = new AtomicInteger(0);

        Stream.iterate(0, i -> i < OBSERVER_COUNT, i -> i + 1)
                .forEach(i -> client.responseStream().subscribe(r -> responseCounter.incrementAndGet()));

        new MultiThreadedStressTester(THREAD_COUNT, ITERATION_COUNT).stress(() -> {
            client.request(ping());
            delay(REQUEST_DELAY_IN_MILLIS);
        }).shutdown();

        await().atMost(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS).untilAtomic(responseCounter, equalTo(expectedResponses()));
    }

    private void launchTheGame() {
        game = starcraft2Game().launch().untilReady();
        client = starcraft2Client().connectTo(game).start();
    }

    private int expectedResponses() {
        return OBSERVER_COUNT * THREAD_COUNT * ITERATION_COUNT;
    }

    @Test
    void isResilientToConnectionLost() throws InterruptedException {
        launchTheGame();

        TestS2ClientSubscriber subscriber = subscribeToResponseStream();

        new Thread(() -> game.restart()).start();
        Stream.iterate(0, i -> i < ITERATION_COUNT, i -> i + 1).forEach(i -> client.request(ping()));

        subscriber.eventuallyReceivedExactly(ITERATION_COUNT);
    }

    private TestS2ClientSubscriber subscribeToResponseStream() {
        TestS2ClientSubscriber subscriber = new TestS2ClientSubscriber();
        client.responseStream().subscribe(subscriber);
        return subscriber;
    }

    @Test
    void afterTooManyConnectionAttemptsReturnsError() {
        client = starcraft2Client().connectTo("127.0.0.1", 5000).start();

        assertThatSubscriberReceivedError(subscribeToResponseStream(), ConnectException.class);
    }

    private <T extends Throwable> void assertThatSubscriberReceivedError(
            TestS2ClientSubscriber subscriber, Class<T> error) {
        subscriber.awaitTerminalEvent(CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        subscriber.assertError(error);
        assertThat(client.isDone()).as("client stopped after error").isTrue();
    }

    @Test
    void throwsExceptionOnRequestEventBusBufferOverflow() throws InterruptedException {
        System.setProperty(OcraftConfig.CLIENT_BUFFER_SIZE_REQUEST_EVENT_BUS, "1");
        refreshConfig();

        client = starcraft2Client().connectTo("127.0.0.1", 5000).start();

        assertThatExceptionOfType(BufferOverflowException.class)
                .isThrownBy(this::startRequestStream);
    }

    private void startRequestStream() {
        Stream.iterate(0, i -> i < ITERATION_COUNT, i -> i + 1).forEach(i -> {
            if (!client.isDone()) {
                client.request(ping());
            }
        });
    }

    @Test
    void emitsErrorOnRequestQueueBufferOverflow() throws InterruptedException {
        System.setProperty(OcraftConfig.CLIENT_BUFFER_SIZE_REQUEST_QUEUE, "1");
        refreshConfig();

        launchTheGame();
        TestS2ClientSubscriber subscriber = subscribeToResponseStream();
        startRequestStream();

        assertThatSubscriberReceivedError(subscriber, BufferOverflowException.class);
    }

    @Test
    void emitsErrorOnResponseEventBusBufferOverflow() throws InterruptedException {
        System.setProperty(OcraftConfig.CLIENT_BUFFER_SIZE_RESPONSE_EVENT_BUS, "1");
        refreshConfig();

        launchTheGame();
        TestS2ClientSubscriber subscriber = subscribeToResponseStream();
        startRequestStream();

        assertThatSubscriberReceivedError(subscriber, BufferOverflowException.class);
    }

    @Test
    void emitsErrorOnResponseStreamBufferOverflow() throws InterruptedException {
        System.setProperty(OcraftConfig.CLIENT_BUFFER_SIZE_RESPONSE_STREAM, "1");
        System.setProperty(OcraftConfig.CLIENT_BUFFER_SIZE_RESPONSE_EVENT_BUS, String.valueOf(ITERATION_COUNT));
        System.setProperty(OcraftConfig.CLIENT_BUFFER_SIZE_RESPONSE_BACKPRESSURE, "1");

        refreshConfig();

        launchTheGame();
        TestS2ClientSubscriber subscriber = TestS2ClientSubscriber.withBackPressure();
        client.responseStream().subscribe(subscriber);
        startRequestStream();

        assertThatSubscriberReceivedError(subscriber, MissingBackpressureException.class);
    }

}
