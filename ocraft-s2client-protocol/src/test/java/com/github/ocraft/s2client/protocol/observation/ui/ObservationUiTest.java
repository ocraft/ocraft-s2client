/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.unit.UnitInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ObservationUiTest {
    @Test
    void throwsExceptionWhenSc2ApiObservationUiIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ObservationUi.from(nothing()))
                .withMessage("sc2api observation ui is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiObservationUi() {
        assertThatAllFieldsAreConvertedForSinglePanel(ObservationUi.from(sc2ApiObservationUiSingle()));
        assertThatAllFieldsAreConvertedForMultiPanel(ObservationUi.from(sc2ApiObservationUiMulti()));
        assertThatAllFieldsAreConvertedForCargoPanel(ObservationUi.from(sc2ApiObservationUiCargo()));
        assertThatAllFieldsAreConvertedForProductionPanel(ObservationUi.from(sc2ApiObservationUiProduction()));
    }

    private void assertThatAllFieldsAreConvertedForSinglePanel(ObservationUi observation) {
        assertThat(observation.getControlGroups()).as("observation ui: control groups").isNotEmpty();
        assertThat(observation.getSinglePanel()).as("observation ui: single panel").isNotEmpty();
        assertThat(observation.getMultiPanel()).as("observation ui: multi panel").isEmpty();
        assertThat(observation.getCargoPanel()).as("observation ui: cargo panel").isEmpty();
        assertThat(observation.getProductionPanel()).as("observation ui: production panel").isEmpty();
    }

    private void assertThatAllFieldsAreConvertedForMultiPanel(ObservationUi observation) {
        assertThat(observation.getControlGroups()).as("observation ui: control groups").isNotEmpty();
        assertThat(observation.getSinglePanel()).as("observation ui: single panel").isEmpty();
        assertThat(observation.getMultiPanel()).as("observation ui: multi panel").isNotEmpty();
        assertThat(observation.getCargoPanel()).as("observation ui: cargo panel").isEmpty();
        assertThat(observation.getProductionPanel()).as("observation ui: production panel").isEmpty();
    }

    private void assertThatAllFieldsAreConvertedForCargoPanel(ObservationUi observation) {
        assertThat(observation.getControlGroups()).as("observation ui: control groups").isNotEmpty();
        assertThat(observation.getSinglePanel()).as("observation ui: single panel").isEmpty();
        assertThat(observation.getMultiPanel()).as("observation ui: multi panel").isEmpty();
        assertThat(observation.getCargoPanel()).as("observation ui: cargo panel").isNotEmpty();
        assertThat(observation.getProductionPanel()).as("observation ui: production panel").isEmpty();
    }

    private void assertThatAllFieldsAreConvertedForProductionPanel(ObservationUi observation) {
        assertThat(observation.getControlGroups()).as("observation ui: control groups").isNotEmpty();
        assertThat(observation.getSinglePanel()).as("observation ui: single panel").isEmpty();
        assertThat(observation.getMultiPanel()).as("observation ui: multi panel").isEmpty();
        assertThat(observation.getCargoPanel()).as("observation ui: cargo panel").isEmpty();
        assertThat(observation.getProductionPanel()).as("observation ui: production panel").isNotEmpty();
    }

    @Test
    void hasEmptySetOfControlGroupsWhenNotProvided() {
        assertThat(ObservationUi.from(
                without(() -> sc2ApiObservationUiSingle().toBuilder(), Ui.ObservationUI.Builder::clearGroups).build()
        ).getControlGroups()).as("observation ui: empty control group set").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ObservationUi.class)
                .withNonnullFields("controlGroups")
                .withPrefabValues(UnitInfo.class, UnitInfo.from(sc2ApiUnitInfoAddOn()), UnitInfo.from(sc2ApiUnitInfo()))
                .verify();
    }
}