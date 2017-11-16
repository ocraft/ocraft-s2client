package com.github.ocraft.s2client.protocol.observation.spatial;

import SC2APIProtocol.Spatial;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiObservationRender;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ObservationRenderTest {
    @Test
    void throwsExceptionWhenSc2ApiObservationRenderIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationRender.from(nothing()))
                .withMessage("sc2api observation render is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiObservationRender() {
        assertThatAllFieldsAreConverted(ObservationRender.from(sc2ApiObservationRender()));
    }

    private void assertThatAllFieldsAreConverted(ObservationRender observationRender) {
        assertThat(observationRender.getMap()).as("observation render: map").isNotNull();
        assertThat(observationRender.getMinimap()).as("observation render: minimap").isNotNull();
    }

    @Test
    void throwsExceptionWhenMapIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationRender.from(without(
                        () -> sc2ApiObservationRender().toBuilder(),
                        Spatial.ObservationRender.Builder::clearMap).build()))
                .withMessage("map is required");
    }

    @Test
    void throwsExceptionWhenMinimapIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationRender.from(without(
                        () -> sc2ApiObservationRender().toBuilder(),
                        Spatial.ObservationRender.Builder::clearMinimap).build()))
                .withMessage("minimap is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ObservationRender.class).withNonnullFields("map", "minimap").verify();
    }
}