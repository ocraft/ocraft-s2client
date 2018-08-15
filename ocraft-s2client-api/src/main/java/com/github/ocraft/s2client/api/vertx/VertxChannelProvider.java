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

import com.github.ocraft.s2client.api.Channel;
import com.github.ocraft.s2client.api.ChannelProvider;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertxChannelProvider implements ChannelProvider {

    private Logger log = LoggerFactory.getLogger(VertxChannelProvider.class);

    private final Vertx vertx = VertxFactory.create();
    private final VertxChannel channel = VertxChannel.from(vertx.eventBus());

    @Override
    public void start(String ip, int port, int connectTimeoutInMillis) {
        vertx.getDelegate().deployVerticle(
                new S2ClientVerticle(channel),
                getDeploymentOptions(ip, port, connectTimeoutInMillis),
                result -> {
                    if (result.failed()) {
                        Throwable cause = result.cause();
                        log.error("VertxChannelProvider: deploy failure", cause);
                        channel.error(cause);
                    }
                });
    }

    private DeploymentOptions getDeploymentOptions(String ip, int port, long connectTimeoutInMillis) {
        return new DeploymentOptions().setConfig(
                new JsonObject()
                        .put(S2ClientVerticle.CFG_IP, ip)
                        .put(S2ClientVerticle.CFG_PORT, port)
                        .put(S2ClientVerticle.CFG_CONNECT_TIMEOUT, connectTimeoutInMillis));
    }

    @Override
    public void stop() {
        vertx.close();
    }

    @Override
    public Channel getChannel() {
        return channel;
    }
}
