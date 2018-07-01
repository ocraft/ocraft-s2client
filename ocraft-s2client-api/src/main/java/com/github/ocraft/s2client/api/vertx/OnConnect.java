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

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

class OnConnect extends DefaultObserver<WebSocket> implements ConnectionHandler {

    private final Logger log = LoggerFactory.getLogger(OnConnect.class);

    private final Set<ConnectionHandler> connectionHandlers = new HashSet<>();
    private final Runnable connect;
    private final VertxChannel channel;
    private boolean done;

    OnConnect(VertxChannel channel, Runnable connect, ConnectionHandler... onConnect) {
        this.connect = connect;
        this.channel = channel;
        connectionHandlers.add(new OnResponse(channel, this::onConnectionLost));
        connectionHandlers.addAll(asList(onConnect));
    }

    @Override
    public void onComplete() {
        // Called after websocket is passed to onNext. Nothing to do here.
        log.debug("OnConnect.onComplete");
    }

    @Override
    public void onError(Throwable e) {
        log.debug("OnConnect.onError", e);
        channel.error(e);
    }

    @Override
    public void onNext(WebSocket webSocket) {
        log.info("Connected.");
        onConnected(webSocket);
    }

    @Override
    public void onConnected(WebSocket webSocket) {
        webSocket.toObservable().subscribe(new Ping(channel, webSocket, () -> this.onConnectionVerified(webSocket)));
    }

    private void onConnectionVerified(WebSocket webSocket) {
        connectionHandlers.forEach(handler -> handler.onConnected(webSocket));
    }

    @Override
    public void onConnectionLost() {
        if (!done) {
            log.info("Connection lost.");
            connectionHandlers.forEach(ConnectionHandler::onConnectionLost);
            cancel();
            connect.run();
        }
    }

    void close() {
        this.done = true;
    }
}
