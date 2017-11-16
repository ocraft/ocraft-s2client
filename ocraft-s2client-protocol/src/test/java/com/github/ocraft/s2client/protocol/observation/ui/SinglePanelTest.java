package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.unit.UnitInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class SinglePanelTest {
    @Test
    void throwsExceptionWhenSc2ApiSinglePanelIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> SinglePanel.from(nothing()))
                .withMessage("sc2api single panel is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiSinglePanel() {
        assertThatAllFieldsAreConverted(SinglePanel.from(sc2ApiSinglePanel()));
    }

    private void assertThatAllFieldsAreConverted(SinglePanel singlePanel) {
        assertThat(singlePanel.getUnit()).as("single panel: unit").isNotNull();
    }

    @Test
    void throwsExceptionWhenTagIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> SinglePanel.from(
                        without(() -> sc2ApiSinglePanel().toBuilder(), Ui.SinglePanel.Builder::clearUnit).build()))
                .withMessage("unit is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(SinglePanel.class)
                .withNonnullFields("unit")
                .withPrefabValues(UnitInfo.class, UnitInfo.from(sc2ApiUnitInfoAddOn()), UnitInfo.from(sc2ApiUnitInfo()))
                .verify();
    }
}