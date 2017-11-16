package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiActionUiSelectWarpGates;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectWarpGates.selectWarpGates;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionUiSelectWarpGatesTest {

    @Test
    void throwsExceptionWhenSc2ApiActionUiSelectWarpGatesIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiSelectWarpGates.from(nothing()))
                .withMessage("sc2api action ui select warp gates is required");
    }

    @Test
    void convertsSc2ApiActionUiSelectWarpGatesToActionUiSelectWarpGates() {
        assertThatAllFieldsAreConverted(ActionUiSelectWarpGates.from(sc2ApiActionUiSelectWarpGates()));
    }

    private void assertThatAllFieldsAreConverted(ActionUiSelectWarpGates selectWarpGates) {
        assertThat(selectWarpGates.isSelectionAdd()).as("action ui select warp gates: selection add").isTrue();
    }

    @Test
    void hasDefaultValueForSelectionAddFieldIfNotProvided() {
        ActionUiSelectWarpGates unitSelectionRect = ActionUiSelectWarpGates.from(without(
                () -> sc2ApiActionUiSelectWarpGates().toBuilder(),
                Ui.ActionSelectWarpGates.Builder::clearSelectionAdd).build());

        assertThat(unitSelectionRect.isSelectionAdd())
                .as("action ui select warp gates: default selection add value")
                .isFalse();
    }

    @Test
    void serializesToSc2ApiActionUiSelectWarpGates() {
        assertThatAllFieldsInRequestAreSerialized(selectWarpGates().add().build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Ui.ActionSelectWarpGates sc2ApiUiSelectWarpGates) {
        assertThat(sc2ApiUiSelectWarpGates.getSelectionAdd()).as("sc2api ui select warp gates: selection add").isTrue();
    }

    @Test
    void serializesDefaultValueForSelectionAddFieldsIfNotProvided() {
        assertThat(selectWarpGates().build().toSc2Api().getSelectionAdd())
                .as("sc2api action ui select WarpGates: default selection add value").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUiSelectWarpGates.class).verify();
    }
}