package com.github.ocraft.s2client.api.test;

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

import SC2APIProtocol.Sc2Api;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

class GameServerVerticle extends AbstractVerticle {

    private Logger log = LoggerFactory.getLogger(GameServerVerticle.class);

    static final String CFG_PORT = "port";
    private final RequestHandler requestHandler;
    private HttpServer httpServer;

    GameServerVerticle(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public void start() {
        httpServer = vertx.createHttpServer()
                .websocketHandler(onMessage())
                .listen(config().getInteger(CFG_PORT));
    }

    private Handler<ServerWebSocket> onMessage() {
        return serverWebSocket -> serverWebSocket.handler(handleConnection(serverWebSocket));
    }

    private Handler<Buffer> handleConnection(ServerWebSocket serverWebSocket) {
        return data -> {
            try {
                Sc2Api.Request request = Sc2Api.Request.parseFrom(data.getDelegate().getBytes());
                log.debug("Received request {}", request);
                Sc2Api.Response response = requestHandler.handle(
                        request);
                sendResponse(serverWebSocket, response);
            } catch (Exception e) {
                sendResponse(serverWebSocket, Sc2Api.Response.newBuilder().addError(e.getMessage()).build());
            }
        };
    }

    private void sendResponse(ServerWebSocket serverWebSocket, Sc2Api.Response response) {
        log.debug("Sending response {}", response);
        Buffer buffer = Buffer.buffer();
        buffer.getDelegate().appendBytes(response.toByteArray());
        serverWebSocket.write(buffer);
    }

    @Override
    public void stop() {
        if (isSet(httpServer)) {
            httpServer.close();
            httpServer = null;
        }
    }
}
