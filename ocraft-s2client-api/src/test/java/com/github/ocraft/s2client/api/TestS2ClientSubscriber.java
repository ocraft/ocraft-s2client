/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.api;

import com.github.ocraft.s2client.protocol.response.Response;
import io.reactivex.subscribers.TestSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.test.Threads.delay;
import static org.assertj.core.api.Assertions.assertThat;

class TestS2ClientSubscriber extends TestSubscriber<Response> {

    private Logger log = LoggerFactory.getLogger(TestS2ClientSubscriber.class);

    private static final int WAITING_TIMEOUT_IN_MILLIS = 30000;
    private static final int ONLY_ONE = 1;
    private final boolean backpressure;

    TestS2ClientSubscriber() {
        backpressure = false;
    }

    private TestS2ClientSubscriber(boolean backpressure) {
        this.backpressure = backpressure;
    }

    static TestS2ClientSubscriber withBackPressure() {
        return new TestS2ClientSubscriber(true);
    }

    void hasReceivedResponse() {
        waitFor(ONLY_ONE);
        assertNoErrors();
        assertThat(valueCount()).as("response list is not empty").isGreaterThan(0);
    }

    private void waitFor(int count) {
        awaitCount(count, () -> {
        }, WAITING_TIMEOUT_IN_MILLIS);
    }

    <T extends Response> T hasReceivedResponseOfType(Class<T> type) {
        waitFor(ONLY_ONE);
        assertValueCount(ONLY_ONE);
        assertNoErrors();
        Response response = popNextEvent();
        assertThat(response).as("type of next event").isInstanceOf(type);
        return type.cast(response);
    }

    private Response popNextEvent() {
        Response response = values().stream().findFirst().orElse(nothing());
        reset();
        return response;
    }

    private void reset() {
        values().clear();
    }

    void isCompleted() {
        awaitTerminalEvent(WAITING_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS);
        assertComplete();
    }

    @Override
    protected void onStart() {
        if (backpressure) {
            request(ONLY_ONE);
        }
        super.onStart();
    }

    @Override
    public void onNext(Response response) {
        if (backpressure) {
            delay(1000);
            request(ONLY_ONE);
        }
        super.onNext(response);
    }

    @Override
    public void onError(Throwable e) {
        log.error("onError", e);
        super.onError(e);
    }

    void eventuallyReceivedExactly(int valueCount) {
        waitFor(valueCount);
    }

    void cancelAfter(int timeoutInMillis) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                TestS2ClientSubscriber.this.cancel();
            }
        }, timeoutInMillis);
    }
}
