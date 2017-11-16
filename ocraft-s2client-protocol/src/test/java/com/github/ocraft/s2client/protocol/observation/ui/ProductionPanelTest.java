package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.unit.UnitInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ProductionPanelTest {
    @Test
    void throwsExceptionWhenSc2ApiProductionPanelIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ProductionPanel.from(nothing()))
                .withMessage("sc2api production panel is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiProductionPanel() {
        assertThatAllFieldsAreConverted(ProductionPanel.from(sc2ApiProductionPanel()));
    }

    private void assertThatAllFieldsAreConverted(ProductionPanel productionPanel) {
        assertThat(productionPanel.getUnit()).as("production panel: unit").isNotNull();
        assertThat(productionPanel.getBuildQueue()).as("production panel: build queue").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenUnitIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ProductionPanel.from(without(
                        () -> sc2ApiProductionPanel().toBuilder(),
                        Ui.ProductionPanel.Builder::clearUnit).build()))
                .withMessage("unit is required");
    }

    @Test
    void hasEmptyListOfBuildQueueWhenNotProvided() {
        assertThat(ProductionPanel.from(
                without(() -> sc2ApiProductionPanel().toBuilder(), Ui.ProductionPanel.Builder::clearBuildQueue).build()
        ).getBuildQueue()).as("production panel: empty build queue list").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ProductionPanel.class)
                .withNonnullFields("unit", "buildQueue")
                .withPrefabValues(UnitInfo.class, UnitInfo.from(sc2ApiUnitInfoAddOn()), UnitInfo.from(sc2ApiUnitInfo()))
                .verify();
    }


}