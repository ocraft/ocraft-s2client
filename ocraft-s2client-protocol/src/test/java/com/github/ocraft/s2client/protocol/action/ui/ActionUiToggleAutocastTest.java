package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Raw;
import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawToggleAutocast;
import com.github.ocraft.s2client.protocol.data.Abilities;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiToggleAutocast.toggleAutocast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionUiToggleAutocastTest {
    @Test
    void throwsExceptionWhenSc2ApiActionUiToggleAutocastIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiToggleAutocast.from(nothing()))
                .withMessage("sc2api action ui toggle autocast is required");
    }

    @Test
    void convertsSc2ApiActionUiToggleAutocastToActionUiToggleAutocast() {
        assertThatAllFieldsAreConverted(ActionUiToggleAutocast.from(sc2ApiActionUiToggleAutocast()));
    }

    private void assertThatAllFieldsAreConverted(ActionUiToggleAutocast toggleAutocast) {
        assertThat(toggleAutocast.getAbility()).as("action ui toggle autocast: ability")
                .isEqualTo(Abilities.EFFECT_PSI_STORM);
    }

    @Test
    void throwsExceptionIfAbilityIdIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRawToggleAutocast.from(without(
                        () -> sc2ApiActionRawToggleAutocast().toBuilder(),
                        Raw.ActionRawToggleAutocast.Builder::clearAbilityId).build()))
                .withMessage("ability id is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionUiToggleAutocast.Builder) toggleAutocast()).build())
                .withMessage("ability is required");
    }

    @Test
    void serializesToSc2ApiActionUiToggleAutocast() {
        assertThatAllFieldsInRequestAreSerialized(
                toggleAutocast().ofAbility(Abilities.EFFECT_PSI_STORM).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Ui.ActionToggleAutocast sc2ApiUiToggleAutocast) {
        assertThat(sc2ApiUiToggleAutocast.getAbilityId()).as("sc2api ui toggle autocast: ability")
                .isEqualTo(PSI_STORM_ABILITY_ID);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUiToggleAutocast.class).withNonnullFields("ability").verify();
    }
}