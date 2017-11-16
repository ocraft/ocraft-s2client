package com.github.ocraft.s2client.protocol.observation.raw;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PowerSourceTest {

    @Test
    void throwsExceptionWhenSc2ApiPowerSourceIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PowerSource.from(nothing()))
                .withMessage("sc2api power source is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiPowerSource() {
        assertThatAllFieldsAreConverted(PowerSource.from(sc2ApiPowerSource()));
    }

    private void assertThatAllFieldsAreConverted(PowerSource powerSource) {
        assertThat(powerSource.getPosition()).as("power source: position").isNotNull();
        assertThat(powerSource.getRadius()).as("power source: radius").isEqualTo(POWER_SOURCE_RADIUS);
        assertThat(powerSource.getTag()).as("power source: tags").isEqualTo(Tag.from(UNIT_TAG));
    }


    @Test
    void throwsExceptionWhenPositionIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PowerSource.from(without(
                        () -> sc2ApiPowerSource().toBuilder(),
                        Raw.PowerSource.Builder::clearPos).build()))
                .withMessage("position is required");
    }

    @Test
    void throwsExceptionWhenRadiusIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PowerSource.from(without(
                        () -> sc2ApiPowerSource().toBuilder(),
                        Raw.PowerSource.Builder::clearRadius).build()))
                .withMessage("radius is required");
    }

    @Test
    void throwsExceptionWhenTagIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PowerSource.from(without(
                        () -> sc2ApiPowerSource().toBuilder(),
                        Raw.PowerSource.Builder::clearTag).build()))
                .withMessage("tag is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(PowerSource.class).withNonnullFields("position", "tag").verify();
    }
}