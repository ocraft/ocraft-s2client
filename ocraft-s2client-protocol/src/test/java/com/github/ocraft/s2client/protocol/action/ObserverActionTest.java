package com.github.ocraft.s2client.protocol.action;

/*-
 * #%L
 * ocraft-s2client-protocol
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
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraFollowPlayer;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraFollowUnits;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraMove;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverPlayerPerspective;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.ObserverAction.observerAction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ObserverActionTest {
    private enum Command {
        PLAYER_PERSPECTIVE,
        CAMERA_MOVE,
        CAMERA_FOLLOW_PLAYER,
        CAMERA_FOLLOW_UNITS
    }

    @Test
    void serializesToSc2ApiObserverActionWithPlayerPerspective() {
        assertThatCorrectActionIsSerialized(
                Command.PLAYER_PERSPECTIVE, observerAction().of(observerPlayerPerspective()).toSc2Api());
        assertThatCorrectActionIsSerialized(
                Command.PLAYER_PERSPECTIVE, observerAction().of(observerPlayerPerspective().build()).toSc2Api());
    }

    private void assertThatCorrectActionIsSerialized(Command command, Sc2Api.ObserverAction sc2ApiObserverAction) {
        if (Command.PLAYER_PERSPECTIVE.equals(command)) {
            assertThat(sc2ApiObserverAction.hasPlayerPerspective())
                    .as("sc2api observer action has player perspective").isTrue();
        } else {
            assertThat(sc2ApiObserverAction.hasPlayerPerspective())
                    .as("sc2api observer action has player perspective").isFalse();
        }
        if (Command.CAMERA_MOVE.equals(command)) {
            assertThat(sc2ApiObserverAction.hasCameraMove())
                    .as("sc2api observer action has camera move").isTrue();
        } else {
            assertThat(sc2ApiObserverAction.hasCameraMove())
                    .as("sc2api observer action has camera move").isFalse();
        }
        if (Command.CAMERA_FOLLOW_PLAYER.equals(command)) {
            assertThat(sc2ApiObserverAction.hasCameraFollowPlayer())
                    .as("sc2api observer action has camera follow player").isTrue();
        } else {
            assertThat(sc2ApiObserverAction.hasCameraFollowPlayer())
                    .as("sc2api observer action has camera follow player").isFalse();
        }
        if (Command.CAMERA_FOLLOW_UNITS.equals(command)) {
            assertThat(sc2ApiObserverAction.hasCameraFollowUnits())
                    .as("sc2api observer action has camera follow units").isTrue();
        } else {
            assertThat(sc2ApiObserverAction.hasCameraFollowUnits())
                    .as("sc2api observer action has camera follow units").isFalse();
        }
    }

    @Test
    void serializesToSc2ApiObserverActionWithCameraMove() {
        assertThatCorrectActionIsSerialized(
                Command.CAMERA_MOVE, observerAction().of(observerCameraMove()).toSc2Api());
        assertThatCorrectActionIsSerialized(
                Command.CAMERA_MOVE, observerAction().of(observerCameraMove().build()).toSc2Api());
    }

    @Test
    void serializesToSc2ApiObserverActionWithCameraFollowPlayer() {
        assertThatCorrectActionIsSerialized(
                Command.CAMERA_FOLLOW_PLAYER, observerAction().of(observerCameraFollowPlayer()).toSc2Api());
        assertThatCorrectActionIsSerialized(
                Command.CAMERA_FOLLOW_PLAYER, observerAction().of(observerCameraFollowPlayer().build()).toSc2Api());
    }

    @Test
    void serializesToSc2ApiObserverActionWithCameraFollowUnits() {
        assertThatCorrectActionIsSerialized(
                Command.CAMERA_FOLLOW_UNITS, observerAction().of(observerCameraFollowUnits()).toSc2Api());
        assertThatCorrectActionIsSerialized(
                Command.CAMERA_FOLLOW_UNITS, observerAction().of(observerCameraFollowUnits().build()).toSc2Api());
    }

    @Test
    void throwsExceptionWhenThereIsNoActionCase() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> observerAction().of((ActionObserverPlayerPerspective) nothing()))
                .withMessage("player perspective is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> observerAction().of((ActionObserverCameraMove) nothing()))
                .withMessage("camera move is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> observerAction().of((ActionObserverCameraFollowPlayer) nothing()))
                .withMessage("camera follow player is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> observerAction().of((ActionObserverCameraFollowUnits) nothing()))
                .withMessage("camera follow units is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ObserverAction.class).verify();
    }
}
