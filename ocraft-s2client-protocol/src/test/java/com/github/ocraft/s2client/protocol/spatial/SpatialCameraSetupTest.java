package com.github.ocraft.s2client.protocol.spatial;

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

import SC2APIProtocol.Common;
import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup.spatialSetup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class SpatialCameraSetupTest {

    private static final int CAMERA_WIDTH = 100;
    private static final int MAP_X = 64;
    private static final int MAP_Y = 64;
    private static final int MINIMAP_X = 32;
    private static final int MINIMAP_Y = 32;

    @Test
    void serializesToSc2ApiSpatialCameraSetup() {
        assertThatAllFieldsAreSerialized(
                spatialSetup()
                        .width(CAMERA_WIDTH)
                        .resolution(MAP_X, MAP_Y)
                        .minimap(MINIMAP_X, MINIMAP_Y)
                        .build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Sc2Api.SpatialCameraSetup sc2ApiSpatialCameraSetup) {
        assertThat(sc2ApiSpatialCameraSetup.getWidth()).as("width of camera").isEqualTo(CAMERA_WIDTH);
        assertThat(sc2ApiSpatialCameraSetup.getResolution()).as("resolution of map")
                .isEqualTo(Common.Size2DI.newBuilder().setX(MAP_X).setY(MAP_Y).build());
        assertThat(sc2ApiSpatialCameraSetup.getMinimapResolution()).as("resolution of minimap")
                .isEqualTo(Common.Size2DI.newBuilder().setX(MINIMAP_X).setY(MINIMAP_Y).build());
    }

    @Test
    void throwsExceptionWhenResolutionIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(spatialSetup())
                        .resolution(nothing()).minimap(MINIMAP_X, MINIMAP_Y).build())
                .withMessage("resolution is required");
    }

    private SpatialCameraSetup.Builder fullAccessTo(Object obj) {
        return (SpatialCameraSetup.Builder) obj;
    }

    @Test
    void throwsExceptionWhenMinimapResolutionIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(spatialSetup()).resolution(MAP_X, MAP_Y).minimap(nothing()).build())
                .withMessage("minimap is required");
    }

    @Test
    void throwsExceptionWhenWidthIsNotGreaterThanZero() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> spatialSetup()
                        .width(0).resolution(MAP_X, MAP_Y).minimap(MINIMAP_X, MINIMAP_Y).build())
                .withMessage("camera width has value 0.0 and is not greater than 0.0");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(SpatialCameraSetup.class).withNonnullFields("map", "minimap").verify();
    }

}
