package com.github.ocraft.s2client.protocol.observation.spatial;

import SC2APIProtocol.Spatial;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiObservationFeatureLayer;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ObservationFeatureLayerTest {
    @Test
    void throwsExceptionWhenSc2ApiObservationFeatureLayerIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationFeatureLayer.from(nothing()))
                .withMessage("sc2api observation feature layer is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiObservationFeatureLayer() {
        assertThatAllFieldsAreConverted(ObservationFeatureLayer.from(sc2ApiObservationFeatureLayer()));
    }

    private void assertThatAllFieldsAreConverted(ObservationFeatureLayer observationFeatureLayer) {
        assertThat(observationFeatureLayer.getRenders()).as("observation feature layer: renders").isNotNull();
        assertThat(observationFeatureLayer.getMinimapRenders()).as("observation feature layer: minimap renders")
                .isNotNull();
    }

    @Test
    void throwsExceptionWhenRendersIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationFeatureLayer.from(without(
                        () -> sc2ApiObservationFeatureLayer().toBuilder(),
                        Spatial.ObservationFeatureLayer.Builder::clearRenders).build()))
                .withMessage("renders is required");
    }

    @Test
    void throwsExceptionWhenMinimapRendersIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationFeatureLayer.from(without(
                        () -> sc2ApiObservationFeatureLayer().toBuilder(),
                        Spatial.ObservationFeatureLayer.Builder::clearMinimapRenders).build()))
                .withMessage("minimap renders is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ObservationFeatureLayer.class).withNonnullFields("renders", "minimapRenders").verify();
    }
}