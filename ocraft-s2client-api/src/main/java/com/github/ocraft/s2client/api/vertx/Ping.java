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

import com.github.ocraft.s2client.protocol.RequestSerializer;
import com.github.ocraft.s2client.protocol.ResponseParser;
import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponsePing;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import io.reactivex.observers.DefaultObserver;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.ocraft.s2client.protocol.Versions.API_VERSION;
import static com.github.ocraft.s2client.protocol.request.Requests.ping;

class Ping extends DefaultObserver<Buffer> {

    private final Logger log = LoggerFactory.getLogger(Ping.class);

    private final WebSocket webSocket;
    private final Runnable onConnectionVerified;
    private final VertxChannel channel;

    Ping(VertxChannel channel, WebSocket webSocket, Runnable onConnectionVerified) {
        this.channel = channel;
        this.webSocket = webSocket;
        this.onConnectionVerified = onConnectionVerified;
    }

    @Override
    public void onStart() {
        webSocket.getDelegate().writeBinaryMessage(
                io.vertx.core.buffer.Buffer.buffer(new RequestSerializer().apply(ping())));
    }

    @Override
    public void onComplete() {
        // Called when websocket is closed. Nothing to do here.
    }

    @Override
    public void onError(Throwable e) {
        log.debug("Ping.onError", e);
        channel.error(e);
    }

    @Override
    public void onNext(Buffer buffer) {
        Response response = new ResponseParser().apply(buffer.getDelegate().getBytes());
        if (ResponseType.PING.equals(response.getType())) {
            String gameVersion = ((ResponsePing) response).getGameVersion();
            if (!API_VERSION.equals(gameVersion)) {
                log.warn("Ocraft uses sc2api in version {}. The game server uses {}.", API_VERSION, gameVersion);
            }
            cancel();
            onConnectionVerified.run();
        }
    }
}
