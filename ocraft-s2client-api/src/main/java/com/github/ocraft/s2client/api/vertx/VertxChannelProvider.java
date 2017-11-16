package com.github.ocraft.s2client.api.vertx;

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
    public void start(String ip, int port) {
        vertx.getDelegate().deployVerticle(new S2ClientVerticle(channel), getDeploymentOptions(ip, port), result -> {
            if (result.failed()) {
                Throwable cause = result.cause();
                log.debug("VertxChannelProvider: deploy failure", cause);
                channel.error(cause);
            }
        });
    }

    private DeploymentOptions getDeploymentOptions(String ip, int port) {
        return new DeploymentOptions().setConfig(
                new JsonObject()
                        .put(S2ClientVerticle.CFG_IP, ip)
                        .put(S2ClientVerticle.CFG_PORT, port));
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
