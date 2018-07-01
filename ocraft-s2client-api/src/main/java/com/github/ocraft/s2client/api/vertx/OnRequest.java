package com.github.ocraft.s2client.api.vertx;

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

import com.github.ocraft.s2client.api.OcraftConfig;
import io.reactivex.observers.DefaultObserver;
import io.vertx.reactivex.core.http.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.BufferOverflowException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.ocraft.s2client.api.OcraftConfig.cfg;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

class OnRequest extends DefaultObserver<byte[]> implements ConnectionHandler {

    private Logger log = LoggerFactory.getLogger(OnRequest.class);

    private WebSocket webSocket;
    private final VertxChannel channel;
    private final Queue<byte[]> queue = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicInteger counter = new AtomicInteger();

    OnRequest(VertxChannel channel) {
        this.channel = channel;
    }

    @Override
    public void onComplete() {
        log.debug("OnRequest.onComplete");
        if (!queue.isEmpty()) {
            log.warn("There was still pending messages to send.");
            queue.clear();
        }
    }

    @Override
    public void onError(Throwable e) {
        log.debug("OnRequest.onError", e);
        channel.error(e);
    }

    @Override
    public void onNext(byte[] msg) {
        log.debug("input stream: received message");
        if (counter.incrementAndGet() > cfg().getInt(OcraftConfig.CLIENT_BUFFER_SIZE_REQUEST_QUEUE)) {
            cancel();
            onError(new BufferOverflowException());
            return;
        }
        queue.offer(msg);
        drain();
    }

    private void drain() {
        while (connected.get() && !queue.isEmpty()) {
            byte[] msg = queue.poll();
            counter.decrementAndGet();
            if (msg != null) {
                send(msg);
            }
        }
    }

    private void send(byte[] msg) {
        WebSocket localWebSocket = this.webSocket;
        if (isSet(localWebSocket) && connected.get()) {
            localWebSocket.getDelegate().writeBinaryMessage(io.vertx.core.buffer.Buffer.buffer(msg));
        }
    }

    @Override
    public void onConnectionLost() {
        log.debug("Connection lost.");
        connected.compareAndSet(true, false);
        this.webSocket = null;
    }

    @Override
    public void onConnected(WebSocket webSocket) {
        log.debug("Connected.");
        this.webSocket = webSocket;
        connected.compareAndSet(false, true);
        drain();
    }
}
