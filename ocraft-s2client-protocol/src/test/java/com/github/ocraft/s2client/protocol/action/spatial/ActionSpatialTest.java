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
package com.github.ocraft.s2client.protocol.action.spatial;

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.spatial.RectangleI;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialCameraMove.cameraMove;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitCommand.unitCommand;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint.click;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionRect.select;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionSpatialTest {

    @Test
    void throwsExceptionWhenSc2ApiActionSpatialIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatial.from(nothing()))
                .withMessage("sc2api action spatial is required");
    }

    @Test
    void convertsSc2ApiActionSpatialUnitCommand() {
        ActionSpatial actionSpatial = ActionSpatial.from(sc2ApiActionSpatialWithUnitCommand());
        assertThat(actionSpatial.getUnitCommand()).as("action spatial: unit command").isNotEmpty();
        assertThat(actionSpatial.getCameraMove()).as("action spatial: camera move").isEmpty();
        assertThat(actionSpatial.getUnitSelectionPoint()).as("action spatial: unit selection point").isEmpty();
        assertThat(actionSpatial.getUnitSelectionRect()).as("action spatial: unit selection rect").isEmpty();
    }

    @Test
    void convertsSc2ApiActionSpatialCameraMove() {
        ActionSpatial actionSpatial = ActionSpatial.from(sc2ApiActionSpatialWithCameraMove());
        assertThat(actionSpatial.getUnitCommand()).as("action spatial: unit command").isEmpty();
        assertThat(actionSpatial.getCameraMove()).as("action spatial: camera move").isNotEmpty();
        assertThat(actionSpatial.getUnitSelectionPoint()).as("action spatial: unit selection point").isEmpty();
        assertThat(actionSpatial.getUnitSelectionRect()).as("action spatial: unit selection rect").isEmpty();
    }

    @Test
    void convertsSc2ApiActionSpatialUnitSelectionPoint() {
        ActionSpatial actionSpatial = ActionSpatial.from(sc2ApiActionSpatialWithUnitSelectionPoint());
        assertThat(actionSpatial.getUnitCommand()).as("action spatial: unit command").isEmpty();
        assertThat(actionSpatial.getCameraMove()).as("action spatial: camera move").isEmpty();
        assertThat(actionSpatial.getUnitSelectionPoint()).as("action spatial: unit selection point").isNotEmpty();
        assertThat(actionSpatial.getUnitSelectionRect()).as("action spatial: unit selection rect").isEmpty();
    }

    @Test
    void convertsSc2ApiActionSpatialUnitSelectionRect() {
        ActionSpatial actionSpatial = ActionSpatial.from(sc2ApiActionSpatialWithUnitSelectionRect());
        assertThat(actionSpatial.getUnitCommand()).as("action spatial: unit command").isEmpty();
        assertThat(actionSpatial.getCameraMove()).as("action spatial: camera move").isEmpty();
        assertThat(actionSpatial.getUnitSelectionPoint()).as("action spatial: unit selection point").isEmpty();
        assertThat(actionSpatial.getUnitSelectionRect()).as("action spatial: unit selection rect").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenThereIsNoActionCase() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatial.from(aSc2ApiActionSpatial().build()))
                .withMessage("one of action case is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatial.of((ActionSpatialUnitCommand) nothing()))
                .withMessage("unit command is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatial.of((ActionSpatialCameraMove) nothing()))
                .withMessage("camera move is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatial.of((ActionSpatialUnitSelectionPoint) nothing()))
                .withMessage("unit selection point is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatial.of((ActionSpatialUnitSelectionRect) nothing()))
                .withMessage("unit selection rect is required");
    }

    @Test
    void serializesToSc2ApiActionSpatialWithUnitCommand() {
        Spatial.ActionSpatial sc2ApiActionSpatial = ActionSpatial.of(
                unitCommand().useAbility(Abilities.ATTACK).build()
        ).toSc2Api();

        assertThat(sc2ApiActionSpatial.hasUnitCommand()).as("sc2api action spatial: case of action is unit command")
                .isTrue();
        assertThat(sc2ApiActionSpatial.hasCameraMove()).as("sc2api action spatial: case of action is camera move")
                .isFalse();
        assertThat(sc2ApiActionSpatial.hasUnitSelectionPoint())
                .as("sc2api action spatial: case of action is unit selection point").isFalse();
        assertThat(sc2ApiActionSpatial.hasUnitSelectionRect())
                .as("sc2api action spatial: case of action is unit selection rect").isFalse();
    }

    @Test
    void serializesToSc2ApiActionSpatialWithCameraMove() {
        Spatial.ActionSpatial sc2ApiActionSpatial = ActionSpatial.of(
                cameraMove().to(PointI.of(1, 1)).build()
        ).toSc2Api();

        assertThat(sc2ApiActionSpatial.hasUnitCommand()).as("sc2api action spatial: case of action is unit command")
                .isFalse();
        assertThat(sc2ApiActionSpatial.hasCameraMove()).as("sc2api action spatial: case of action is camera move")
                .isTrue();
        assertThat(sc2ApiActionSpatial.hasUnitSelectionPoint())
                .as("sc2api action spatial: case of action is unit selection point").isFalse();
        assertThat(sc2ApiActionSpatial.hasUnitSelectionRect())
                .as("sc2api action spatial: case of action is unit selection rect").isFalse();
    }

    @Test
    void serializesToSc2ApiActionSpatialWithUnitSelectionPoint() {
        Spatial.ActionSpatial sc2ApiActionSpatial = ActionSpatial.of(
                click().on(PointI.of(1, 2)).withMode(ActionSpatialUnitSelectionPoint.Type.SELECT).build()
        ).toSc2Api();

        assertThat(sc2ApiActionSpatial.hasUnitCommand()).as("sc2api action spatial: case of action is unit command")
                .isFalse();
        assertThat(sc2ApiActionSpatial.hasCameraMove()).as("sc2api action spatial: case of action is camera move")
                .isFalse();
        assertThat(sc2ApiActionSpatial.hasUnitSelectionPoint())
                .as("sc2api action spatial: case of action is unit selection point").isTrue();
        assertThat(sc2ApiActionSpatial.hasUnitSelectionRect())
                .as("sc2api action spatial: case of action is unit selection rect").isFalse();
    }

    @Test
    void serializesToSc2ApiActionSpatialWithUnitSelectionRect() {
        Spatial.ActionSpatial sc2ApiActionSpatial = ActionSpatial.of(
                select().of(RectangleI.of(PointI.of(0, 0), PointI.of(1, 1))).build()
        ).toSc2Api();

        assertThat(sc2ApiActionSpatial.hasUnitCommand()).as("sc2api action spatial: case of action is unit command")
                .isFalse();
        assertThat(sc2ApiActionSpatial.hasCameraMove()).as("sc2api action spatial: case of action is camera move")
                .isFalse();
        assertThat(sc2ApiActionSpatial.hasUnitSelectionPoint())
                .as("sc2api action spatial: case of action is unit selection point").isFalse();
        assertThat(sc2ApiActionSpatial.hasUnitSelectionRect())
                .as("sc2api action spatial: case of action is unit selection rect").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionSpatial.class).verify();
    }
}