package com.github.ocraft.s2client.api.vertx;

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