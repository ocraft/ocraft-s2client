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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.api.test.GameServer;
import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.ClientEvents;
import com.github.ocraft.s2client.bot.GameServerResponses;
import com.github.ocraft.s2client.bot.gateway.AppState;
import com.github.ocraft.s2client.bot.setting.ProcessSettings;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

// TODO p.picheta game server should be closed after each test, because one fail also fails next tests
class GameSetup {

    static final int TEST_TIMEOUT_MS = 30000;
    static final int GAME_SERVER_PORT = 4000;

    private final GameServer gameServer = GameServer.create(GAME_SERVER_PORT);
    private ControlInterfaceImpl controlInterface;
    private ObservationInterfaceImpl observationInterface = mock(ObservationInterfaceImpl.class);

    private int connectTimeoutMs = TEST_TIMEOUT_MS;
    private int requestTimeoutMs = TEST_TIMEOUT_MS;
    private boolean stopAfterConnection;
    private AppState appState = AppState.NORMAL;
    private S2Controller s2Controller;
    private boolean multiplayer;
    private boolean mockObservation = true;
    private ClientEvents clientEvents = mock(ClientEvents.class);

    GameSetup withLimitedCorrectPingResponses(int limit) {
        gameServer.onRequest(Sc2Api.Request::hasPing, GameServerResponses.limitedCorrectPingResponses(limit));
        return this;
    }

    GameSetup unresponsivePing(int times) {
        return unresponsivePingThen(times, GameServerResponses::ping);
    }

    GameSetup unresponsivePingThen(int times, Supplier<Sc2Api.Response> responseAfterFail) {
        final AtomicInteger counter = new AtomicInteger(0);
        gameServer.onRequest(Sc2Api.Request::hasPing, () -> {
            if (counter.incrementAndGet() <= times) {
                return null;
            } else {
                return responseAfterFail.get();
            }
        });
        return this;
    }

    // TODO p.picheta make full Builder pattern with validation
    GameSetup start() {
        gameServer.start();

        if (mockObservation) {
            controlInterface = new ControlInterfaceImpl(clientEvents, new ProtoInterfaceImpl(), observationInterface);
        } else {
            controlInterface = new ControlInterfaceImpl(clientEvents, new ProtoInterfaceImpl());
            observationInterface = controlInterface.observationInternal();
        }
        controlInterface.setAppState(appState);
        if (isSet(s2Controller)) controlInterface.setS2Controller(s2Controller);
        controlInterface.setMultiplayer(multiplayer);

        connect();
        if (stopAfterConnection) {
            gameServer.stop();
        }
        return this;
    }

    private GameSetup connect() {
        assertThat(controlInterface.connect(new ProcessSettings()
                .setPortStart(GAME_SERVER_PORT)
                .setRequestTimeoutMS(requestTimeoutMs)
                .setConnectionTimeoutMS(connectTimeoutMs)
                .setTraced(true))
        ).as("connection status").isTrue();
        return this;
    }

    GameSetup observation(ObservationInterfaceImpl observation) {
        this.observationInterface = observation;
        return this;
    }

    GameSetup stop() {
        if (isSet(controlInterface)) controlInterface.quit();
        gameServer.stop();
        control().dumpProtoUsage();
        return this;
    }

    ControlInterfaceImpl control() {
        return controlInterface;
    }

    GameServer server() {
        return gameServer;
    }

    GameSetup inState(AppState state) {
        this.appState = state;
        return this;
    }

    GameSetup stopAfterConnection() {
        this.stopAfterConnection = true;
        return this;
    }

    GameSetup requestTimeout(int requestTimeoutMs) {
        this.requestTimeoutMs = requestTimeoutMs;
        return this;
    }

    GameSetup withS2Controller(S2Controller s2Controller) {
        this.s2Controller = s2Controller;
        return this;
    }

    GameSetup multiplayer(boolean value) {
        this.multiplayer = value;
        return this;
    }

    GameSetup assertThatErrorsWasEmitted(ClientError[] clientErrors, String[] protocolErrors) {
        assertThat(controlInterface.getClientErrors())
                .as("contains client error")
                .containsExactly(clientErrors);
        assertThat(controlInterface.getProtocolErrors())
                .as("contains protocol error")
                .containsExactly(protocolErrors);
        return this;
    }

    GameSetup assertThatAppStateIs(AppState state, String description) {
        assertThat(controlInterface.getAppState()).as(description).isEqualTo(state);
        return this;
    }

    GameSetup assertThatGameIsMultiplayer() {
        assertThat(controlInterface.isMultiplayer()).as("game is multiplayer").isTrue();
        return this;
    }

    ObservationInterfaceImpl observation() {
        return observationInterface;
    }

    public ClientEvents clientEvents() {
        return clientEvents;
    }

    GameSetup mockObservation(boolean value) {
        this.mockObservation = value;
        return this;
    }
}
