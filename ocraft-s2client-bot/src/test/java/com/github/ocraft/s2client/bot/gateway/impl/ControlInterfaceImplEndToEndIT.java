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

import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.ClientEvents;
import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.gateway.AppState;
import com.github.ocraft.s2client.bot.gateway.ProcessInfo;
import com.github.ocraft.s2client.bot.setting.InterfaceSettings;
import com.github.ocraft.s2client.bot.setting.PlayerSettings;
import com.github.ocraft.s2client.bot.setting.ProcessSettings;
import com.github.ocraft.s2client.protocol.debug.Commands;
import com.github.ocraft.s2client.protocol.debug.DebugCommand;
import com.github.ocraft.s2client.protocol.debug.DebugTestProcess;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.request.Requests;
import com.github.ocraft.s2client.protocol.response.Response;
import io.reactivex.Maybe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

@Tag("endtoend")
class ControlInterfaceImplEndToEndIT {

    private static final int TEST_TIMEOUT_MS = 30000;

    private ObservationInterfaceImpl observationInterfaceMock = mock(ObservationInterfaceImpl.class);

    @BeforeEach
    void setUp() {
        reset(observationInterfaceMock);
    }

    @Test
    void launchesGameProcessIfNeeded() {
        ControlInterfaceImpl controlInterface = control();

        controlInterface.connect(new ProcessSettings()
                .setWithGameController(true)
                .setRequestTimeoutMS(TEST_TIMEOUT_MS)
                .setConnectionTimeoutMS(TEST_TIMEOUT_MS));

        ProcessInfo processInfo = controlInterface.getProcessInfo();
        assertThat(processInfo.getProcessPath()).as("launched process path").isNotEmpty();
        assertThat(processInfo.getProcessId()).as("launched process pid").isNotEmpty();
        assertThat(processInfo.getPort()).as("launched process port").isNotNull();

        controlInterface.quit();
        controlInterface.forceStop();
    }

    private ControlInterfaceImpl control() {
        return new ControlInterfaceImpl(
                mock(ClientEvents.class), new ProtoInterfaceImpl(), observationInterfaceMock);
    }

    @Test
    void changesApplicationStateToCrashedWhenGameServerDoesNotRespondAndProcessIsNotAlive() {
        ControlInterfaceImpl controlInterface = control();
        setupGame(controlInterface);

        controlInterface.getTheGame().stop();

        assertThat(controlInterface.ping()).as("ping request status").isFalse();
        assertThat(controlInterface.getAppState()).as("application status after process crash")
                .isEqualTo(AppState.CRASHED);
        assertThat(controlInterface.getClientErrors())
                .as("contains client errors")
                .containsExactly(ClientError.CONNECTION_CLOSED, ClientError.SC2_APP_FAILURE);
    }

    private void setupGame(ControlInterfaceImpl controlInterface) {
        controlInterface.connect(new ProcessSettings()
                .setWithGameController(true)
                .setRequestTimeoutMS(10000)
                .setConnectionTimeoutMS(TEST_TIMEOUT_MS));
        assertThat(controlInterface.createGame(
                BattlenetMap.of("Lava Flow"),
                List.of(PlayerSettings.participant(Race.PROTOSS, mock(S2Agent.class))),
                true)
        ).as("create game status").isTrue();
        Maybe<Response> responseMaybe = controlInterface.requestJoinGame(
                PlayerSettings.participant(Race.PROTOSS, mock(S2Agent.class)),
                new InterfaceSettings(null, null),
                null);
        assertThat(controlInterface.waitJoinGame(responseMaybe)).as("join game status").isTrue();
    }

    @Test
    void changesApplicationStateToTimeoutWhenGameServerDoesNotRespondAndConnectionIsActive() {
        ControlInterfaceImpl controlInterface = control();
        setupGame(controlInterface);

        controlInterface.proto().sendRequest(Requests.debug().with(DebugCommand.command().of(
                Commands.testProcess().with(DebugTestProcess.Test.HANG).delayInMillis(0))));

        assertThat(controlInterface.ping()).as("ping request status").isFalse();
        assertThat(controlInterface.getAppState()).as("application status when game is unresponsive")
                .isEqualTo(AppState.TIMEOUT);
        assertThat(controlInterface.getClientErrors())
                .as("contains client errors")
                .contains(ClientError.SC2_PROTOCOL_TIMEOUT, ClientError.SC2_APP_FAILURE);

        controlInterface.forceStop();
    }

    @Test
    void changesApplicationStateToTimeoutWhenGameServerDoesNotRespondAndProcessIsAliveAndConnectionIsClosed() {
        ControlInterfaceImpl controlInterface = control();
        setupGame(controlInterface);

        controlInterface.proto().sendRequest(Requests.debug().with(DebugCommand.command().of(
                Commands.testProcess().with(DebugTestProcess.Test.HANG).delayInMillis(0))));
        Maybe<Response> responseMaybe = controlInterface.proto().sendRequest(Requests.ping());
        controlInterface.disconnect();

        assertThat(controlInterface.waitForResponse(responseMaybe)).as("ping response").isEmpty();
        assertThat(controlInterface.getAppState()).as("application status when game is unresponsive")
                .isEqualTo(AppState.TIMEOUT);
        assertThat(controlInterface.getClientErrors())
                .as("contains client errors")
                .containsExactly(
                        ClientError.CONNECTION_CLOSED, ClientError.SC2_PROTOCOL_TIMEOUT, ClientError.SC2_APP_FAILURE);

        controlInterface.forceStop();
    }
}
