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
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.spatial.RectangleI;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiActionSpatialUnitSelectionRect;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionRect.select;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionSpatialUnitSelectionRectTest {
    @Test
    void throwsExceptionWhenSc2ApiActionSpatialUnitSelectionRectIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatialUnitSelectionRect.from(nothing()))
                .withMessage("sc2api action spatial unit selection rect is required");
    }

    @Test
    void convertsSc2ApiActionSpatialUnitSelectionRectToActionSpatialUnitSelectionRect() {
        assertThatAllFieldsAreConverted(ActionSpatialUnitSelectionRect.from(sc2ApiActionSpatialUnitSelectionRect()));
    }

    private void assertThatAllFieldsAreConverted(ActionSpatialUnitSelectionRect unitSelectionRect) {
        assertThat(unitSelectionRect.getSelectionsInScreenCoord())
                .as("action spatial unit selection rect: selections in screen coord").isNotEmpty();
        assertThat(unitSelectionRect.isSelectionAdd()).as("action spatial unit selection rect: selection add").isTrue();
    }

    @Test
    void throwsExceptionWhenSelectionsAreNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatialUnitSelectionRect.from(without(
                        () -> sc2ApiActionSpatialUnitSelectionRect().toBuilder(),
                        Spatial.ActionSpatialUnitSelectionRect.Builder::clearSelectionScreenCoord).build()))
                .withMessage("selection list is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionSpatialUnitSelectionRect.Builder) select()).build())
                .withMessage("selection list is required");
    }

    @Test
    void hasDefaultValueForSelectionAddFieldIfNotProvided() {
        ActionSpatialUnitSelectionRect unitSelectionRect = ActionSpatialUnitSelectionRect.from(without(
                () -> sc2ApiActionSpatialUnitSelectionRect().toBuilder(),
                Spatial.ActionSpatialUnitSelectionRect.Builder::clearSelectionAdd).build());

        assertThat(unitSelectionRect.isSelectionAdd())
                .as("action spatial unit selection rect: default selection add value")
                .isFalse();
    }

    @Test
    void serializesDefaultValueForSelectionAddFieldIfNotProvided() {
        Spatial.ActionSpatialUnitSelectionRect sc2ApiSelectionRect = select()
                .of(RectangleI.of(PointI.of(1, 1), PointI.of(5, 5))).build().toSc2Api();

        assertThat(sc2ApiSelectionRect.getSelectionAdd())
                .as("action spatial unit selection rect: default selection add value").isFalse();
    }

    @Test
    void serializesToSc2ApiActionSpatialUnitSelectionRect() {
        assertThatAllFieldsInRequestAreSerialized(
                select().of(RectangleI.of(PointI.of(1, 1), PointI.of(5, 5))).add().build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Spatial.ActionSpatialUnitSelectionRect sc2ApiSpatialUnitSelectionRect) {
        assertThat(sc2ApiSpatialUnitSelectionRect.getSelectionScreenCoordList())
                .as("sc2api spatial unit selection rect: selection screen coord list").isNotEmpty();
        assertThat(sc2ApiSpatialUnitSelectionRect.getSelectionAdd())
                .as("sc2api spatial unit selection rect: selection add").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ActionSpatialUnitSelectionRect.class)
                .withNonnullFields("selectionsInScreenCoord")
                .verify();
    }

}