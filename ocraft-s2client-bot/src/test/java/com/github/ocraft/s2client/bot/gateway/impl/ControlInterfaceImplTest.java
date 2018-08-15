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

import com.github.ocraft.s2client.api.OcraftApiConfig;
import com.github.ocraft.s2client.api.controller.S2Controller;
import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.ClientEvents;
import com.github.ocraft.s2client.bot.gateway.AppState;
import com.github.ocraft.s2client.bot.setting.ProcessSettings;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.unit.Alliance;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControlInterfaceImplTest {

    public static final boolean PENDING_RESPONSE = true;

    @Test
    void providesInformationAboutGameState() {
        ControlInterfaceImpl control = new ControlInterfaceImpl(
                mock(ClientEvents.class), mock(ProtoInterfaceImpl.class), mock(ObservationInterfaceImpl.class));

        state(control, AppState.NORMAL, GameStatus.IN_GAME, PENDING_RESPONSE);
        assertThat(control.isInGame()).isTrue();
        assertThat(control.isFinishedGame()).isFalse();
        assertThat(control.isReadyForCreateGame()).isFalse();

        state(control, AppState.NORMAL, GameStatus.IN_REPLAY, PENDING_RESPONSE);
        assertThat(control.isInGame()).isTrue();
        assertThat(control.isFinishedGame()).isFalse();
        assertThat(control.isReadyForCreateGame()).isFalse();

        state(control, AppState.NORMAL, GameStatus.ENDED, !PENDING_RESPONSE);
        assertThat(control.isInGame()).isFalse();
        assertThat(control.isFinishedGame()).isTrue();
        assertThat(control.isReadyForCreateGame()).isTrue();

        state(control, AppState.CRASHED, GameStatus.IN_GAME, !PENDING_RESPONSE);
        assertThat(control.isInGame()).isFalse();
        assertThat(control.isFinishedGame()).isTrue();
        assertThat(control.isReadyForCreateGame()).isFalse();
    }

    private void state(
            ControlInterfaceImpl controlInterface,
            AppState appState,
            GameStatus gameStatus,
            boolean pendingResponse) {
        controlInterface.setAppState(appState);
        when(controlInterface.proto().lastStatus()).thenReturn(gameStatus);
        when(controlInterface.proto().hasResponsePending()).thenReturn(pendingResponse);
    }

    @Test
    void findsInformationAboutStartLocation() {
        ControlInterfaceImpl control = controlWithMainBaseInUnitPool();

        control.onGameStart();

        assertThat(control.observation().getStartLocation()).as("start location").isNotNull();
    }

    private ControlInterfaceImpl controlWithMainBaseInUnitPool() {
        ControlInterfaceImpl control = new ControlInterfaceImpl(
                mock(ClientEvents.class), mock(ProtoInterfaceImpl.class));
        Unit unit = mock(Unit.class);
        when(unit.getAlliance()).thenReturn(Alliance.SELF);
        when(unit.getType()).thenReturn(Units.TERRAN_COMMAND_CENTER);
        when(unit.getPosition()).thenReturn(Point.of(1.0f, 1.0f, 1.0f));
        control.observationInternal().unitPool().createUnit(Tag.of(1L)).update(unit, 1, true);
        return control;
    }

    @Test
    void collectsErrors() {
        ControlInterfaceImpl control = new ControlInterfaceImpl(
                mock(ClientEvents.class), mock(ProtoInterfaceImpl.class));

        control.error(ClientError.SC2_PROTOCOL_ERROR, List.of("error 01"));
        control.errorIf(true, ClientError.SC2_APP_FAILURE, List.of("error 02"));
        control.errorIf(false, ClientError.INVALID_ABILITY_REMAP, List.of("error 03"));

        assertThat(control.getClientErrors())
                .containsExactly(ClientError.SC2_PROTOCOL_ERROR, ClientError.SC2_APP_FAILURE);
        assertThat(control.getProtocolErrors()).containsExactly("error 01", "error 02");

        control.clearClientErrors();
        control.clearProtocolErrors();

        assertThat(control.getClientErrors()).isEmpty();
        assertThat(control.getProtocolErrors()).isEmpty();
    }

    @Test
    void updatesProcessPathsInProcessSettings() {
        ControlInterfaceImpl control = new ControlInterfaceImpl(
                mock(ClientEvents.class), mockProto(), mock(ObservationInterfaceImpl.class));
        control.setS2Controller(mockS2Controller());

        ProcessSettings processSettings = new ProcessSettings().setWithGameController(true);
        control.connect(processSettings);

        assertThat(processSettings.getRootPath()).as("updated root path in process settings").isNotNull();
        assertThat(processSettings.getActualProcessPath()).as("actual exe path in process settings").isNotNull();
    }

    private ProtoInterfaceImpl mockProto() {
        ProtoInterfaceImpl protoInterface = mock(ProtoInterfaceImpl.class);
        when(protoInterface.connectToGame(any(), any(), any(), any())).thenReturn(true);
        return protoInterface;
    }

    private S2Controller mockS2Controller() {
        S2Controller s2Controller = mock(S2Controller.class);
        when(s2Controller.getConfig()).thenReturn(ConfigFactory.parseMap(Map.of(
                OcraftApiConfig.GAME_EXE_ROOT, "/test/sc2",
                OcraftApiConfig.GAME_EXE_PATH, "/test/sc2/Versions/Base/SC2.exe",
                OcraftApiConfig.GAME_NET_PORT, 5000
        )));
        when(s2Controller.getS2Process()).thenReturn(mock(Process.class));
        return s2Controller;
    }

}
