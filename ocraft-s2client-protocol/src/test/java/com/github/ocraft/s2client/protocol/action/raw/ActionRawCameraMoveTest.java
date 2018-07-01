package com.github.ocraft.s2client.protocol.action.raw;

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

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.unit.Unit;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.raw.ActionRawCameraMove.cameraMove;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionRawCameraMoveTest {

    @Test
    void throwsExceptionWhenSc2ApiActionRawCameraMoveIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRawCameraMove.from(nothing()))
                .withMessage("sc2api action raw camera move is required");
    }

    @Test
    void convertsSc2ApiActionRawCameraMoveToActionRawCameraMove() {
        assertThatAllFieldsAreConverted(ActionRawCameraMove.from(sc2ApiActionRawCameraMove()));
    }

    private void assertThatAllFieldsAreConverted(ActionRawCameraMove cameraMove) {
        assertThat(cameraMove.getCenterInWorldSpace()).as("action raw camera move: center in world space").isNotNull();
    }

    @Test
    void throwsExceptionWhenCenterInWorldSpaceIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRawCameraMove.from(without(
                        () -> sc2ApiActionRawCameraMove().toBuilder(),
                        Raw.ActionRawCameraMove.Builder::clearCenterWorldSpace).build()))
                .withMessage("center in world space is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionRawCameraMove.Builder) cameraMove()).build())
                .withMessage("center in world space is required");
    }

    @Test
    void serializesToSc2ApiActionRawCameraMove() {
        assertThatAllFieldsInRequestAreSerialized(cameraMove().to(Point.of(10, 20, 30)).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Raw.ActionRawCameraMove sc2ApiActionRawCameraMove) {
        assertThat(sc2ApiActionRawCameraMove.getCenterWorldSpace()).as("sc2api raw camera move: center in world space")
                .isNotNull();
    }

    @Test
    void serializesToSc2ApiActionRawCameraMoveUsingUnitPosition() {
        assertThatAllFieldsInRequestAreSerialized(cameraMove().to(Unit.from(sc2ApiUnit())).build().toSc2Api());
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionRawCameraMove.class).withNonnullFields("centerInWorldSpace").verify();
    }
}
