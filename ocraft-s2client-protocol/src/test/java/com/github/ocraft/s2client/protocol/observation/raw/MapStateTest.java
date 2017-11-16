package com.github.ocraft.s2client.protocol.observation.raw;

import SC2APIProtocol.Raw;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiMapState;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class MapStateTest {
    @Test
    void throwsExceptionWhenSc2ApiMapStateIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MapState.from(nothing()))
                .withMessage("sc2api map state is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiMapState() {
        assertThatAllFieldsAreConverted(MapState.from(sc2ApiMapState()));
    }

    private void assertThatAllFieldsAreConverted(MapState mapState) {
        assertThat(mapState.getVisibility()).as("map state: visibility").isNotNull();
        assertThat(mapState.getCreep()).as("map state: creep").isNotNull();
    }

    @Test
    void throwsExceptionWhenVisibilityIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MapState.from(without(
                        () -> sc2ApiMapState().toBuilder(),
                        Raw.MapState.Builder::clearVisibility).build()))
                .withMessage("visibility is required");
    }

    @Test
    void throwsExceptionWhenCreepIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MapState.from(without(
                        () -> sc2ApiMapState().toBuilder(),
                        Raw.MapState.Builder::clearCreep).build()))
                .withMessage("creep is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(MapState.class).withNonnullFields("visibility", "creep").verify();
    }
}