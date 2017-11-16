package com.github.ocraft.s2client.protocol.unit;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.data.Units;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PassengerUnitTest {

    @Test
    void throwsExceptionWhenSc2ApiPassengerUnitIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PassengerUnit.from(nothing()))
                .withMessage("sc2api passenger unit is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiPassengerUnit() {
        assertThatAllFieldsAreConverted(PassengerUnit.from(sc2ApiPassengerUnit()));
    }

    private void assertThatAllFieldsAreConverted(PassengerUnit passengerUnit) {
        assertThat(passengerUnit.getTag()).as("passenger unit: tag").isEqualTo(Tag.from(UNIT_TAG));
        assertThat(passengerUnit.getHealth()).as("passenger unit: health").isEqualTo(UNIT_HEALTH);
        assertThat(passengerUnit.getHealthMax()).as("passenger unit: health").isEqualTo(UNIT_HEALTH_MAX);
        assertThat(passengerUnit.getShield()).as("passenger unit: shield").hasValue(UNIT_SHIELD);
        assertThat(passengerUnit.getShieldMax()).as("passenger unit: shield max").hasValue(UNIT_SHIELD_MAX);
        assertThat(passengerUnit.getEnergy()).as("passenger unit: energy").hasValue(UNIT_ENERGY);
        assertThat(passengerUnit.getEnergyMax()).as("passenger unit: energy max").hasValue(UNIT_ENERGY_MAX);
        assertThat(passengerUnit.getType()).as("passenger unit: type").isEqualTo(Units.PROTOSS_NEXUS);
    }

    @Test
    void throwsExceptionWhenTagIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PassengerUnit.from(
                        without(() -> sc2ApiPassengerUnit().toBuilder(), Raw.PassengerUnit.Builder::clearTag).build()))
                .withMessage("tag is required");
    }

    @Test
    void throwsExceptionWhenHealthIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PassengerUnit.from(
                        without(() -> sc2ApiPassengerUnit().toBuilder(), Raw.PassengerUnit.Builder::clearHealth)
                                .build()))
                .withMessage("health is required");
    }

    @Test
    void throwsExceptionWhenHealthMaxIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PassengerUnit.from(
                        without(() -> sc2ApiPassengerUnit().toBuilder(), Raw.PassengerUnit.Builder::clearHealthMax)
                                .build()))
                .withMessage("health max is required");
    }

    @Test
    void throwsExceptionWhenTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PassengerUnit.from(
                        without(() -> sc2ApiPassengerUnit().toBuilder(), Raw.PassengerUnit.Builder::clearUnitType)
                                .build()))
                .withMessage("unit type is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(PassengerUnit.class).withNonnullFields("tag", "type").verify();
    }

}