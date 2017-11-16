package com.github.ocraft.s2client.protocol.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class UnitsTest {
    @ParameterizedTest
    @EnumSource(Units.class)
    void isMappedBySc2ApiUnitTypeId(UnitType unitType) {
        assertThat(Units.from(unitType.getUnitTypeId())).as("unit type mapped from unit type id")
                .isEqualTo(unitType);
    }

    @Test
    void returnsOtherTypeIfIdIsNotMapped() {
        assertThat(Units.from(-1000)).as("unit type: not mapped id").isEqualTo(Units.Other.of(-1000));
    }
}