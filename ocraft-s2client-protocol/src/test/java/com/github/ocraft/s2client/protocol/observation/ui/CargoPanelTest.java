package com.github.ocraft.s2client.protocol.observation.ui;

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
import com.github.ocraft.s2client.protocol.unit.UnitInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CargoPanelTest {

    @Test
    void throwsExceptionWhenSc2ApiCargoPanelIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CargoPanel.from(nothing()))
                .withMessage("sc2api cargo panel is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiCargoPanel() {
        assertThatAllFieldsAreConverted(CargoPanel.from(sc2ApiCargoPanel()));
    }

    private void assertThatAllFieldsAreConverted(CargoPanel cargoPanel) {
        assertThat(cargoPanel.getUnit()).as("cargo panel: unit").isNotNull();
        assertThat(cargoPanel.getPassengers()).as("cargo panel: passengers").isNotEmpty();
        assertThat(cargoPanel.getCargoSize()).as("cargo panel: cargo size").isEqualTo(CARGO_SIZE);
    }

    @Test
    void throwsExceptionWhenUnitIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CargoPanel.from(
                        without(() -> sc2ApiCargoPanel().toBuilder(), Ui.CargoPanel.Builder::clearUnit).build()))
                .withMessage("unit is required");
    }

    @Test
    void hasEmptyListOfPassengersWhenNotProvided() {
        assertThat(CargoPanel.from(
                without(() -> sc2ApiCargoPanel().toBuilder(), Ui.CargoPanel.Builder::clearPassengers).build()
        ).getPassengers()).as("cargo panel: empty passengers list").isEmpty();
    }

    @Test
    void throwsExceptionWhenCargoSizeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CargoPanel.from(without(
                        () -> sc2ApiCargoPanel().toBuilder(),
                        Ui.CargoPanel.Builder::clearSlotsAvailable).build()))
                .withMessage("cargo size is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(CargoPanel.class)
                .withNonnullFields("unit", "passengers")
                .withPrefabValues(UnitInfo.class, UnitInfo.from(sc2ApiUnitInfoAddOn()), UnitInfo.from(sc2ApiUnitInfo()))
                .verify();
    }

}
