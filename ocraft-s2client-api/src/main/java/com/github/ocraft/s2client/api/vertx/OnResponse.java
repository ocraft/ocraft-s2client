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

import io.reactivex.observers.DefaultObserver;
import io.vertx.reactivex.core.http.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.BufferOverflowException;

class OnResponse extends DefaultObserver<byte[]> implements ConnectionHandler {

    private Logger log = LoggerFactory.getLogger(OnResponse.class);

    private final VertxChannel channel;
    private final Runnable onConnectionLost;

    OnResponse(VertxChannel channel, Runnable onConnectionLost) {
        this.channel = channel;
        this.onConnectionLost = onConnectionLost;
    }

    @Override
    public void onComplete() {
        log.debug("OnResponse.onComplete: connection lost.");
        onConnectionLost.run();
    }

    @Override
    public void onError(Throwable e) {
        log.debug("OnResponse.onError", e);
        channel.error(e);
    }

    @Override
    public void onNext(byte[] response) {
        log.debug("OnResponse.onNext: received message");
        try {
            channel.output(response);
        } catch (BufferOverflowException e) {
            cancel();
            onError(e);
        }
    }

    @Override
    public void onConnectionLost() {
        log.debug("Connection lost");
        cancel();
    }

    @Override
    public void onConnected(WebSocket webSocket) {
        log.debug("Connected");
        webSocket.toObservable().map(b -> b.getDelegate().getBytes()).subscribe(this);
    }
}
