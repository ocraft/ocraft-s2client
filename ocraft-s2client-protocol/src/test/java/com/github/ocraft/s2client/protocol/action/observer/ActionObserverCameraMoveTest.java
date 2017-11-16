/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.action.observer;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.CAMERA_DISTANCE;
import static com.github.ocraft.s2client.protocol.Fixtures.observerCameraMove;
import static com.github.ocraft.s2client.protocol.action.Actions.Observer.cameraMove;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionObserverCameraMoveTest {
    @Test
    void serializesToSc2ApiActionObserverCameraMove() {
        assertThatAllFieldsInRequestAreSerialized(observerCameraMove().build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Sc2Api.ActionObserverCameraMove sc2ApiObserverCameraMove) {
        assertThat(sc2ApiObserverCameraMove.hasWorldPos()).as("sc2api observer camera move: has world pos")
                .isTrue();
        assertThat(sc2ApiObserverCameraMove.getDistance()).as("sc2api observer camera move: distance")
                .isEqualTo(CAMERA_DISTANCE);
    }

    @Test
    void throwsExceptionWhenPositionIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionObserverCameraMove.Builder) cameraMove()).build())
                .withMessage("position is required");
    }

    @Test
    void serializesDefaultDistanceIfNotSet() {
        assertThat(cameraMove().to(Point2d.of(1, 1)).build().toSc2Api().getDistance())
                .as("sc2api observer camera move: default distance").isEqualTo(0);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionObserverCameraMove.class).withNonnullFields("position").verify();
    }
}