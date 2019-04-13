package com.github.ocraft.s2client.protocol.observation.spatial;

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
import com.google.protobuf.ByteString;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiFeatureLayersMinimap;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FeatureLayersMinimapTest {
    @Test
    void throwsExceptionWhenSc2ApiFeatureLayersMinimapIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> FeatureLayersMinimap.from(nothing()))
                .withMessage("sc2api feature layers minimap is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiFeatureLayersMinimap() {
        assertThatAllFieldsAreConverted(FeatureLayersMinimap.from(sc2ApiFeatureLayersMinimap()));
    }

    private void assertThatAllFieldsAreConverted(FeatureLayersMinimap featureLayersMinimap) {
        assertThat(featureLayersMinimap.getHeightMap()).as("feature layers minimap: height map").isNotNull();
        assertThat(featureLayersMinimap.getVisibilityMap()).as("feature layers minimap: visibility map").isNotNull();
        assertThat(featureLayersMinimap.getCreep()).as("feature layers minimap: creep").isNotNull();
        assertThat(featureLayersMinimap.getCamera()).as("feature layers minimap: camera").isNotNull();
        assertThat(featureLayersMinimap.getPlayerId()).as("feature layers minimap: player id").isNotNull();
        assertThat(featureLayersMinimap.getUnitType()).as("feature layers minimap: unit type").isNotEmpty();
        assertThat(featureLayersMinimap.getSelected()).as("feature layers minimap: selected").isNotNull();
        assertThat(featureLayersMinimap.getPlayerRelative()).as("feature layers minimap: player relative").isNotNull();

        assertThat(featureLayersMinimap.getAlerts()).as("feature layers minimap: alerts").isNotEmpty();
        assertThat(featureLayersMinimap.getBuildable()).as("feature layers minimap: buildable").isNotEmpty();
        assertThat(featureLayersMinimap.getPathable()).as("feature layers minimap: pathable").isNotEmpty();

    }

    @Test
    void throwsExceptionWhenHeightMapIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> FeatureLayersMinimap.from(without(
                        () -> sc2ApiFeatureLayersMinimap().toBuilder(),
                        Spatial.FeatureLayersMinimap.Builder::clearHeightMap).build()))
                .withMessage("height map is required");
    }

    @Test
    void throwsExceptionWhenVisibilityMapIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> FeatureLayersMinimap.from(without(
                        () -> sc2ApiFeatureLayersMinimap().toBuilder(),
                        Spatial.FeatureLayersMinimap.Builder::clearVisibilityMap).build()))
                .withMessage("visibility map is required");
    }

    @Test
    void throwsExceptionWhenCreepIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> FeatureLayersMinimap.from(without(
                        () -> sc2ApiFeatureLayersMinimap().toBuilder(),
                        Spatial.FeatureLayersMinimap.Builder::clearCreep).build()))
                .withMessage("creep is required");
    }

    @Test
    void throwsExceptionWhenCameraIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> FeatureLayersMinimap.from(without(
                        () -> sc2ApiFeatureLayersMinimap().toBuilder(),
                        Spatial.FeatureLayersMinimap.Builder::clearCamera).build()))
                .withMessage("camera is required");
    }

    @Test
    void throwsExceptionWhenPlayerIdIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> FeatureLayersMinimap.from(without(
                        () -> sc2ApiFeatureLayersMinimap().toBuilder(),
                        Spatial.FeatureLayersMinimap.Builder::clearPlayerId).build()))
                .withMessage("player id is required");
    }

    @Test
    void throwsExceptionWhenSelectedIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> FeatureLayersMinimap.from(without(
                        () -> sc2ApiFeatureLayersMinimap().toBuilder(),
                        Spatial.FeatureLayersMinimap.Builder::clearSelected).build()))
                .withMessage("selected is required");
    }

    @Test
    void throwsExceptionWhenPlayerRelativeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> FeatureLayersMinimap.from(without(
                        () -> sc2ApiFeatureLayersMinimap().toBuilder(),
                        Spatial.FeatureLayersMinimap.Builder::clearPlayerRelative).build()))
                .withMessage("player relative is required");
    }

    @Test
    void fulfillsEqualsContract() throws UnsupportedEncodingException {
        EqualsVerifier
                .forClass(FeatureLayersMinimap.class)
                .withNonnullFields("heightMap", "visibilityMap", "creep", "camera", "playerId", "unitType", "selected",
                        "playerRelative")
                .withPrefabValues(
                        ByteString.class,
                        ByteString.copyFrom("test", "UTF-8"),
                        ByteString.copyFrom("test2", "UTF-8"))
                .verify();
    }
}
