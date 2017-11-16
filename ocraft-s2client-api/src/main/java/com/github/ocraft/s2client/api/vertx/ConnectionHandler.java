package com.github.ocraft.s2client.api.vertx;


import io.vertx.reactivex.core.http.WebSocket;

interface ConnectionHandler {
    void onConnectionLost();

    void onConnected(WebSocket webSocket);
}
