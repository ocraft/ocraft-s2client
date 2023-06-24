package com.github.ocraft.s2client.bot.gateway.impl;

/*-
 * #%L
 * ocraft-s2client-bot
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

import com.github.ocraft.s2client.api.S2Client;
import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.gateway.ProtoInterface;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.request.Request;
import com.github.ocraft.s2client.protocol.request.Requests;
import com.github.ocraft.s2client.protocol.response.Response;
import com.github.ocraft.s2client.protocol.response.ResponsePing;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

class ProtoInterfaceImpl implements ProtoInterface {

    private Logger log = LoggerFactory.getLogger(ProtoInterfaceImpl.class);

    private BiConsumer<ClientError, List<String>> onError = (clientError, strings) -> {
    };

    private S2Client s2Client;
    private GameStatus latestStatus = GameStatus.UNKNOWN;
    private String dataVersion;
    private Integer baseBuild;
    private Map<ResponseType, Integer> countUses = new EnumMap<>(ResponseType.class);
    private ResponseQueue responseQueue = new ResponseQueue();

    void setOnError(BiConsumer<ClientError, List<String>> onError) {
        require("onError callback", onError);
        this.onError = onError;
    }

    @Override
    public boolean connectToGame(
            S2Controller theGame,
            Integer connectionTimeoutInMillis,
            Integer requestTimeoutInMillis,
            Boolean traced) {
        S2Client.Builder aClient = new S2Client.Builder();
        aClient.connectTo(theGame);
        return connect(aClient, connectionTimeoutInMillis, requestTimeoutInMillis, traced);
    }

    @Override
    public boolean connectToGame(
            String address,
            Integer port,
            Integer connectionTimeoutInMillis,
            Integer requestTimeoutInMillis,
            Boolean traced) {
        S2Client.Builder aClient = new S2Client.Builder();
        aClient.connectTo(address, port);
        return connect(aClient, connectionTimeoutInMillis, requestTimeoutInMillis, traced);
    }

    private boolean connect(
            S2Client.Builder aClient,
            Integer connectionTimeoutInMillis,
            Integer requestTimeoutInMillis,
            Boolean traced) {
        try {
            s2Client = aClient
                    .requestTimeout(requestTimeoutInMillis)
                    .connectTimeout(connectionTimeoutInMillis)
                    .onConnectionLost(() -> onError.accept(ClientError.CONNECTION_CLOSED, Collections.emptyList()))
                    .traced(traced)
                    .start()
                    .untilReady();

            waitForResponse(sendRequest(Requests.ping()))
                    .flatMap(response -> response.as(ResponsePing.class))
                    .ifPresentOrElse(ping -> {
                        this.dataVersion = ping.getDataVersion();
                        this.baseBuild = ping.getBaseBuild();
                        Units.remapForBuild(this.baseBuild);
                        Abilities.remapForBuild(this.baseBuild);
                    }, () -> {
                        throw new IllegalStateException("ping failed");
                    });
            return true;
        } catch (TimeoutException e) {
            log.error("Connection timeout.", e);
            return false;
        } catch (IllegalStateException e) {
            log.error("Ping failed.", e);
            return false;
        }
    }

    @Override
    public <T extends Request> Maybe<Response> sendRequest(T requestData) {
        require("request", requestData);
        Maybe<Response> responseMaybe;
        if (!requestData.responseType().equals(ResponseType.PING) && responseQueue.peek(requestData.responseType())) {
            // We don't need to sync when using a ping response type
            synchronized(requestData.responseType()) {
                responseMaybe = s2Client.waitForResponse(requestData.responseType());
            }
        } else {
            responseMaybe = s2Client.waitForResponse(requestData.responseType());
        }
        s2Client.request(requestData);
        countUses.compute(requestData.responseType(), (responseType, count) -> count != null ? ++count : 1);
        responseQueue.offer(requestData.responseType(), responseMaybe);
        return responseMaybe;
    }

    @Override
    public <T extends Request> Maybe<Response> sendRequest(BuilderSyntax<T> requestDataBuilder) {
        require("request", requestDataBuilder);
        return sendRequest(requestDataBuilder.build());
    }

    @Override
    public Optional<Response> waitForResponse(Maybe<Response> waitFor) {
        return Optional.ofNullable(waitFor.blockingGet())
                .map(response -> {
                    latestStatus = response.getStatus();
                    responseQueue.poll(response.getType());
                    return response;
                });
    }

    @Override
    public void quit() {
        if (isSet(s2Client)) {
            sendRequest(Requests.quitGame());
            s2Client.stop();
        }
    }

    @Override
    public boolean pollResponse(ResponseType type) {
        return responseQueue.peekResponse(type);
    }

    @Override
    public boolean hasResponsePending(ResponseType type) {
        return responseQueue.peek(type);
    }

    @Override
    public boolean hasResponsePending() {
        return responseQueue.peek();
    }

    @Override
    public Maybe<Response> getResponsePending(ResponseType type) {
        return responseQueue.poll(type);
    }

    @Override
    public GameStatus lastStatus() {
        return latestStatus;
    }

    @Override
    public String getConnectToIp() {
        return s2Client.getConnectToIp();
    }

    @Override
    public Integer getConnectToPort() {
        return s2Client.getConnectToPort();
    }

    @Override
    public String getDataVersion() {
        return dataVersion;
    }

    @Override
    public Integer getBaseBuild() {
        return baseBuild;
    }

    @Override
    public Map<ResponseType, Integer> getCountUses() {
        return countUses;
    }

    boolean isConnected() {
        return isSet(s2Client) && !s2Client.isDone();
    }

    // test purposes only
    void disconnect() {
        s2Client.stop();
    }

    @Override
    public String toString() {
        return "ProtoInterface{" +
                "latestStatus=" + latestStatus +
                ", dataVersion='" + dataVersion + '\'' +
                ", baseBuild=" + baseBuild +
                '}';
    }
}
