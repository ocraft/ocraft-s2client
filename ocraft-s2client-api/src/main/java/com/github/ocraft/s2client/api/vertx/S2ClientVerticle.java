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
import io.reactivex.Observable;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.ocraft.s2client.api.OcraftConfig.cfg;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

class S2ClientVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(S2ClientVerticle.class);

    static final String CFG_IP = "ip";
    static final String CFG_PORT = "port";

    private static final int RETRY = cfg().getInt(OcraftConfig.CLIENT_NET_RETRY);
    private static final int MAX_WEBSOCKET_FRAME_SIZE_IN_BYTES = cfg().getInt(OcraftConfig.CLIENT_NET_FRAME_SIZE);
    private static final int CONNECT_TIMEOUT = cfg().getInt(OcraftConfig.CLIENT_NET_CONNECT_TIMEOUT);
    private static final String SC2API_URI = "/sc2api";

    private final VertxChannel channel;
    private final OnRequest onRequest;

    private HttpClient httpClient;
    private OnConnect onConnect;

    S2ClientVerticle(VertxChannel channel) {
        this.channel = channel;
        this.onRequest = new OnRequest(channel);
        channel.inputStream().subscribe(onRequest);
    }

    @Override
    public void start() throws Exception {
        log.debug("S2ClientVerticle: START");

        initHttpClient();
        connect();
    }

    private void initHttpClient() {
        HttpClientOptions httpClientOptions = new HttpClientOptions();
        httpClientOptions.setKeepAlive(true);
        httpClientOptions.setPipelining(true);
        httpClientOptions.setConnectTimeout(CONNECT_TIMEOUT);
        httpClientOptions.setMaxWebsocketFrameSize(MAX_WEBSOCKET_FRAME_SIZE_IN_BYTES);
        httpClient = vertx.createHttpClient(httpClientOptions);
    }

    private void connect() {
        connection().retry(RETRY).subscribe(onConnectObserver());
    }

    private Observable<WebSocket> connection() {
        return Observable.defer(() -> httpClient
                .websocketStream(config().getInteger(CFG_PORT), config().getString(CFG_IP), SC2API_URI)
                .toObservable());
    }

    private OnConnect onConnectObserver() {
        if (isSet(onConnect)) onConnect.close();
        this.onConnect = new OnConnect(channel, this::connect, onRequest);
        return onConnect;
    }

    @Override
    public void stop() throws Exception {
        log.debug("S2ClientVerticle: STOP");
        if (isSet(onConnect)) {
            onConnect.close();
        }
        if (isSet(httpClient)) {
            httpClient.close();
        }
    }

}
