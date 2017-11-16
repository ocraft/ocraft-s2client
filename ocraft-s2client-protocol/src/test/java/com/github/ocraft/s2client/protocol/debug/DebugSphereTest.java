package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Defaults;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.debug.DebugSphere.sphere;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DebugSphereTest {
    @Test
    void serializesToSc2ApiDebugSphere() {
        assertThatAllFieldsAreSerialized(debugSphere().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugSphere sc2ApiSphere) {
        assertThat(sc2ApiSphere.hasColor()).as("sc2api sphere: has color").isTrue();
        assertThat(sc2ApiSphere.hasP()).as("sc2api sphere: has p").isTrue();
        assertThat(sc2ApiSphere.getR()).as("sc2api sphere: r").isEqualTo(RADIUS);
    }

    @Test
    void throwsExceptionWhenColorIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(sphere().on(P0).withRadius(RADIUS)).withColor(nothing()).build())
                .withMessage("color is required");
    }

    private DebugSphere.Builder fullAccessTo(Object obj) {
        return (DebugSphere.Builder) obj;
    }

    @Test
    void throwsExceptionWhenCenterIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(sphere()).withRadius(RADIUS).withColor(SAMPLE_COLOR).build())
                .withMessage("center is required");
    }

    @Test
    void throwsExceptionWhenRadiusIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(sphere().on(P0)).withColor(SAMPLE_COLOR).build())
                .withMessage("radius is required");
    }

    @Test
    void hasDefaultColorIfNotSet() {
        assertThat(sphere().on(P0).withRadius(RADIUS).build().getColor()).as("default color").isEqualTo(Defaults.COLOR);
    }

    @Test
    void throwsExceptionWhenRadiusIsNotGreaterThanZero() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sphere().on(P0).withRadius(0).build())
                .withMessage("radius has value 0.0 and is not greater than 0.0");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugSphere.class).withNonnullFields("color", "center").verify();
    }

}