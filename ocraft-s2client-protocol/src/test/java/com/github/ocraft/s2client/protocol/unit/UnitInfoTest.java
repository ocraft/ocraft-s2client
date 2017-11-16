package com.github.ocraft.s2client.protocol.unit;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.data.Units;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UnitInfoTest {
    @Test
    void throwsExceptionWhenSc2ApiUnitInfoIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> UnitInfo.from(nothing()))
                .withMessage("sc2api unit info is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiUnitInfo() {
        assertThatAllFieldsAreConverted(UnitInfo.from(sc2ApiUnitInfo()));
    }

    private void assertThatAllFieldsAreConverted(UnitInfo unitInfo) {
        assertThat(unitInfo.getUnitType()).as("unit info: unit type").isEqualTo(Units.PROTOSS_NEXUS);
        assertThat(unitInfo.getPlayerRelative()).as("unit info: player relative").hasValue(Alliance.SELF);
        assertThat(unitInfo.getHealth()).as("unit info: health").hasValue((int) UNIT_HEALTH);
        assertThat(unitInfo.getShields()).as("unit info: shields").hasValue((int) UNIT_SHIELD);
        assertThat(unitInfo.getEnergy()).as("unit info: energy").hasValue((int) UNIT_ENERGY);
        assertThat(unitInfo.getTransportSlotsTaken()).as("unit info: transport slots taken").hasValue(CARGO_SIZE);
        assertThat(unitInfo.getBuildProgress()).as("unit info: build progress").hasValue(UNIT_BUILD_PROGRESS);
        assertThat(unitInfo.getAddOn()).as("unit info: addon").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenUnitTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> UnitInfo.from(
                        without(() -> sc2ApiUnitInfo().toBuilder(), Ui.UnitInfo.Builder::clearUnitType).build()))
                .withMessage("unit type is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(UnitInfo.class)
                .withNonnullFields("unitType")
                .withPrefabValues(UnitInfo.class, UnitInfo.from(sc2ApiUnitInfoAddOn()), UnitInfo.from(sc2ApiUnitInfo()))
                .verify();
    }
}