package com.github.ocraft.s2client.protocol.action;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionErrorTest {
    @Test
    void throwsExceptionWhenSc2ApiActionErrorIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionError.from(nothing()))
                .withMessage("sc2api action error is required");
    }

    @Test
    void convertsSc2ApiActionErrorToActionError() {
        assertThatAllFieldsAreConverted(ActionError.from(sc2ApiActionError()));
    }

    private void assertThatAllFieldsAreConverted(ActionError actionError) {
        assertThat(actionError.getUnitTag()).as("action error: unit tag").hasValue(Tag.from(UNIT_TAG));
        assertThat(actionError.getAbility()).as("action error: ability").hasValue(Abilities.EFFECT_PSI_STORM);
        assertThat(actionError.getActionResult()).as("action error: action result")
                .isEqualTo(ActionResult.COULDNT_REACH_TARGET);
    }

    @Test
    void throwsExceptionWhenActionResultIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionError.from(without(
                        () -> sc2ApiActionError().toBuilder(),
                        Sc2Api.ActionError.Builder::clearResult).build()))
                .withMessage("action result is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionError.class).withNonnullFields("actionResult").verify();
    }
}