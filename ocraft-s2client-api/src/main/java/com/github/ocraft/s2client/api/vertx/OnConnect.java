package com.github.ocraft.s2client.api.vertx;


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
