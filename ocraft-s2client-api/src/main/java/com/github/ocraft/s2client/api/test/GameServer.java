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
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public class GameServer {

    private static final String GAME_VERSION = "3.17.1.57218";
    private static final String DATA_VERSION = "3F2FCED08798D83B873B5543BEFA6C4B";
    private static final int DATA_BUILD = 57218;
    private static final int BASE_BUILD = 56787;
    private static final int TIMEOUT_MS = 3000;

    private Logger log = LoggerFactory.getLogger(GameServer.class);

    private Vertx vertx;
    private RequestHandler requestHandler = new RequestHandler();
    private Integer port;

    private GameServer(Integer port) {
        this.port = port;
        setupDefaultResponses();
    }

    public static GameServer create(int port) {
        return new GameServer(port);
    }

    private void setupDefaultResponses() {
        onRequest(Sc2Api.Request::hasPing, defaultPingResponse());
        onRequest(Sc2Api.Request::hasQuit, defaultQuitResponse());
    }

    private Supplier<Sc2Api.Response> defaultPingResponse() {
        return () -> Sc2Api.Response.newBuilder().setPing(
                Sc2Api.ResponsePing.newBuilder()
                        .setBaseBuild(BASE_BUILD)
                        .setDataBuild(DATA_BUILD)
                        .setDataVersion(DATA_VERSION)
                        .setGameVersion(GAME_VERSION)
                        .build())
                .build();
    }

    private Supplier<Sc2Api.Response> defaultQuitResponse() {
        return () -> Sc2Api.Response.newBuilder().setQuit(Sc2Api.ResponseQuit.newBuilder().build()).build();
    }

    public GameServer start() {
        if (!isSet(vertx)) vertx = VertxFactory.create();
        vertx.getDelegate().deployVerticle(
                new GameServerVerticle(requestHandler),
                getDeploymentOptions(port),
                result -> {
                    if (result.failed()) {
                        Throwable cause = result.cause();
                        log.error("GameServer: deploy failure", cause);
                    }
                });
        return this;
    }

    private DeploymentOptions getDeploymentOptions(int port) {
        return new DeploymentOptions().setConfig(new JsonObject().put(GameServerVerticle.CFG_PORT, port));
    }

    public GameServer onRequest(Predicate<Sc2Api.Request> condition, Supplier<Sc2Api.Response> responseSupplier) {
        requestHandler.addHandler(condition, responseSupplier);
        return this;
    }

    public boolean stop() {
        if (isSet(vertx)) {
            try {
                final CountDownLatch wait = new CountDownLatch(1);
                vertx.close(voidAsyncResult -> wait.countDown());
                vertx = null;
                return wait.await(TIMEOUT_MS, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.debug("Thread was interrupted.", e);
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "GameServer{" +
                "port=" + port +
                '}';
    }
}
