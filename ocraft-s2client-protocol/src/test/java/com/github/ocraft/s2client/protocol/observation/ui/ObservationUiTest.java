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