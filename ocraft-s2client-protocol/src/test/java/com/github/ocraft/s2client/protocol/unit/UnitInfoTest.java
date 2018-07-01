package com.github.ocraft.s2client.protocol.unit;

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
