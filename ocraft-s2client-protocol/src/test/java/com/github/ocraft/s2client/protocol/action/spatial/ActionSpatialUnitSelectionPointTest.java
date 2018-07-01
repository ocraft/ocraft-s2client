package com.github.ocraft.s2client.protocol.action.spatial;

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

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiActionSpatialUnitSelectionPoint;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint.Type.*;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint.click;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class ActionSpatialUnitSelectionPointTest {
    @Test
    void throwsExceptionWhenSc2ApiActionSpatialUnitSelectionPointIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatialUnitSelectionPoint.from(nothing()))
                .withMessage("sc2api action spatial unit selection point is required");
    }

    @Test
    void convertsSc2ApiActionSpatialUnitSelectionPointToActionSpatialUnitSelectionPoint() {
        assertThatAllFieldsAreConverted(ActionSpatialUnitSelectionPoint.from(sc2ApiActionSpatialUnitSelectionPoint()));
    }

    private void assertThatAllFieldsAreConverted(ActionSpatialUnitSelectionPoint unitSelectionPoint) {
        assertThat(unitSelectionPoint.getSelectionInScreenCoord())
                .as("action spatial unit selection point: selection in screen coord").isNotNull();
        assertThat(unitSelectionPoint.getType()).as("action spatial unit selection point: type").isEqualTo(ALL_TYPE);
    }

    @Test
    void throwsExceptionWhenSelectionInScreenCoordIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatialUnitSelectionPoint.from(without(
                        () -> sc2ApiActionSpatialUnitSelectionPoint().toBuilder(),
                        Spatial.ActionSpatialUnitSelectionPoint.Builder::clearSelectionScreenCoord).build()))
                .withMessage("selection in screen coord is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(click()).withMode(SELECT).build())
                .withMessage("selection in screen coord is required");
    }

    private ActionSpatialUnitSelectionPoint.Builder fullAccessTo(Object obj) {
        return (ActionSpatialUnitSelectionPoint.Builder) obj;
    }

    @Test
    void throwsExceptionWhenTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatialUnitSelectionPoint.from(without(
                        () -> sc2ApiActionSpatialUnitSelectionPoint().toBuilder(),
                        Spatial.ActionSpatialUnitSelectionPoint.Builder::clearType).build()))
                .withMessage("type is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(click().on(PointI.of(1, 1))).build())
                .withMessage("type is required");
    }


    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "unitSelectionPointTypeMappings")
    void mapsSc2ApiUnitSelectionPointType(
            Spatial.ActionSpatialUnitSelectionPoint.Type sc2ApiUnitSelectionPointType,
            ActionSpatialUnitSelectionPoint.Type expectedUnitSelectionPointType) {
        assertThat(ActionSpatialUnitSelectionPoint.Type.from(sc2ApiUnitSelectionPointType))
                .isEqualTo(expectedUnitSelectionPointType);
    }

    private static Stream<Arguments> unitSelectionPointTypeMappings() {
        return Stream.of(
                of(Spatial.ActionSpatialUnitSelectionPoint.Type.Select, SELECT),
                of(Spatial.ActionSpatialUnitSelectionPoint.Type.Toggle, TOGGLE),
                of(Spatial.ActionSpatialUnitSelectionPoint.Type.AllType, ALL_TYPE),
                of(Spatial.ActionSpatialUnitSelectionPoint.Type.AddAllType, ADD_ALL_TYPE));
    }

    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "unitSelectionPointTypeMappings")
    void serializesToSc2ApiUnitSelectionPointType(
            Spatial.ActionSpatialUnitSelectionPoint.Type expectedSc2ApiSelectionPointType,
            ActionSpatialUnitSelectionPoint.Type selectionPointType) {
        assertThat(selectionPointType.toSc2Api()).isEqualTo(expectedSc2ApiSelectionPointType);
    }

    @Test
    void throwsExceptionWhenUnitSelectionPointTypeIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatialUnitSelectionPoint.Type.from(nothing()))
                .withMessage("sc2api unit selection point type is required");
    }

    @Test
    void serializesToSc2ApiActionSpatialUnitSelectionPoint() {
        assertThatAllFieldsInRequestAreSerialized(
                click().on(PointI.of(10, 20)).withMode(ADD_ALL_TYPE).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Spatial.ActionSpatialUnitSelectionPoint sc2ApiSpatialUnitSelectionPoint) {
        assertThat(sc2ApiSpatialUnitSelectionPoint.hasSelectionScreenCoord())
                .as("sc2api spatial unit selection point: has selection screen coord").isTrue();
        assertThat(sc2ApiSpatialUnitSelectionPoint.getType()).as("sc2api spatial unit selection point: type")
                .isEqualTo(Spatial.ActionSpatialUnitSelectionPoint.Type.AddAllType);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ActionSpatialUnitSelectionPoint.class)
                .withNonnullFields("selectionInScreenCoord", "type")
                .verify();
    }
}
