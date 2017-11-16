package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.unit.UnitInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class MultiPanelTest {
    @Test
    void throwsExceptionWhenSc2ApiMultiPanelIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MultiPanel.from(nothing()))
                .withMessage("sc2api multi panel is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiMultiPanel() {
        assertThatAllFieldsAreConverted(MultiPanel.from(sc2ApiMultiPanel()));
    }

    private void assertThatAllFieldsAreConverted(MultiPanel multiPanel) {
        assertThat(multiPanel.getUnits()).as("multi panel: units").isNotEmpty();
    }

    @Test
    void hasEmptySetOfUnitsWhenNotProvided() {
        assertThat(MultiPanel.from(
                without(() -> sc2ApiMultiPanel().toBuilder(), Ui.MultiPanel.Builder::clearUnits).build()
        ).getUnits()).as("multi panel: empty units set").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(MultiPanel.class)
                .withNonnullFields("units")
                .withPrefabValues(UnitInfo.class, UnitInfo.from(sc2ApiUnitInfoAddOn()), UnitInfo.from(sc2ApiUnitInfo()))
                .verify();
    }
}