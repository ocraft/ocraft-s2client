package com.github.ocraft.s2client.protocol.observation.ui;

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