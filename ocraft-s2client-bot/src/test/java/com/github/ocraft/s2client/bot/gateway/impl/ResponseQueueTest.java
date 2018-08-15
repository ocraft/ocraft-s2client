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

import com.github.ocraft.s2client.bot.GameServerResponses;
import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponsePing;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.MaybeSubject;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseQueueTest {

    @Test
    void allowsToStoreOnePendingResponseTypeAtTime() {
        ResponseQueue responseQueue = new ResponseQueue();
        assertThat(responseQueue.offer(ResponseType.ACTION, MaybeSubject.create())).isTrue();
        assertThat(responseQueue.offer(ResponseType.ACTION, MaybeSubject.create())).isFalse();
    }

    @Test
    void checksIfResponseOfGivenTypeIsPending() {
        ResponseQueue responseQueue = new ResponseQueue();

        responseQueue.offer(ResponseType.ACTION, MaybeSubject.create());

        assertThat(responseQueue.peek(ResponseType.ACTION)).isTrue();
        assertThat(responseQueue.poll(ResponseType.ACTION)).isNotNull();
        assertThat(responseQueue.peek(ResponseType.ACTION)).isFalse();
    }

    @Test
    void pollsResponse() {
        ResponseQueue responseQueue = new ResponseQueue();

        TestScheduler scheduler = new TestScheduler();
        Observable<Response> observable = responseStream(scheduler);
        MaybeSubject<Response> maybeSubject = MaybeSubject.create();
        observable.firstElement().subscribe(maybeSubject);

        responseQueue.offer(ResponseType.PING, maybeSubject);

        assertThat(responseQueue.peekResponse(ResponseType.PING)).isFalse();
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        assertThat(responseQueue.peekResponse(ResponseType.PING)).isTrue();
    }

    private Observable<Response> responseStream(TestScheduler scheduler) {
        List<Response> responses = Collections.singletonList(ResponsePing.from(GameServerResponses.ping()));
        Observable<Long> tick = Observable.interval(1, TimeUnit.SECONDS, scheduler);
        return Observable.fromIterable(responses).zipWith(tick, (r, index) -> r);
    }

    @Test
    void checksIfAnyResponseIsPending() {
        ResponseQueue responseQueue = new ResponseQueue();

        responseQueue.offer(ResponseType.ACTION, MaybeSubject.create());
        responseQueue.offer(ResponseType.LEAVE_GAME, MaybeSubject.create());

        assertThat(responseQueue.peek()).isTrue();

        responseQueue.poll(ResponseType.ACTION);
        responseQueue.poll(ResponseType.LEAVE_GAME);

        assertThat(responseQueue.peek()).isFalse();
    }

}
