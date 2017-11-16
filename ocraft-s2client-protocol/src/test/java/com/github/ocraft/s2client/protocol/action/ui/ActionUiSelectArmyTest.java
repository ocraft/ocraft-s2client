package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiActionUiSelectArmy;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectArmy.selectArmy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionUiSelectArmyTest {

    @Test
    void throwsExceptionWhenSc2ApiActionUiSelectArmyIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiSelectArmy.from(nothing()))
                .withMessage("sc2api action ui select army is required");
    }

    @Test
    void convertsSc2ApiActionUiSelectArmyToActionUiSelectArmy() {
        assertThatAllFieldsAreConverted(ActionUiSelectArmy.from(sc2ApiActionUiSelectArmy()));
    }

    private void assertThatAllFieldsAreConverted(ActionUiSelectArmy selectArmy) {
        assertThat(selectArmy.isSelectionAdd()).as("action ui select army: selection add").isTrue();
    }

    @Test
    void hasDefaultValueForSelectionAddFieldIfNotProvided() {
        ActionUiSelectArmy unitSelectionRect = ActionUiSelectArmy.from(without(
                () -> sc2ApiActionUiSelectArmy().toBuilder(),
                Ui.ActionSelectArmy.Builder::clearSelectionAdd).build());

        assertThat(unitSelectionRect.isSelectionAdd()).as("action ui select army: default selection add value")
                .isFalse();
    }

    @Test
    void serializesToSc2ApiActionUiSelectArmy() {
        assertThatAllFieldsInRequestAreSerialized(selectArmy().add().build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Ui.ActionSelectArmy sc2ApiUiSelectArmy) {
        assertThat(sc2ApiUiSelectArmy.getSelectionAdd()).as("sc2api ui select army: selection add").isTrue();
    }

    @Test
    void serializesDefaultValueForSelectionAddFieldsIfNotProvided() {
        assertThat(selectArmy().build().toSc2Api().getSelectionAdd())
                .as("sc2api action ui select army: default selection add value").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUiSelectArmy.class).verify();
    }
}