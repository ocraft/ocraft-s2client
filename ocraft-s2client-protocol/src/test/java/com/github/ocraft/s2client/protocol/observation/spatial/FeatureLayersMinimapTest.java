package com.github.ocraft.s2client.protocol.observation.spatial;

import SC2APIProtocol.Spatial;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

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
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(FeatureLayersMinimap.class)
                .withNonnullFields("heightMap", "visibilityMap", "creep", "camera", "playerId", "unitType", "selected",
                        "playerRelative")
                .verify();
    }
}