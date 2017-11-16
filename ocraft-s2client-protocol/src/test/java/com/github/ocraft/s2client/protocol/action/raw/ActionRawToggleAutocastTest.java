package com.github.ocraft.s2client.protocol.action.raw;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.raw.ActionRawToggleAutocast.toggleAutocast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionRawToggleAutocastTest {

    @Test
    void throwsExceptionWhenSc2ApiActionRawToggleAutocastIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRawToggleAutocast.from(nothing()))
                .withMessage("sc2api action raw toggle autocast is required");
    }

    @Test
    void convertsSc2ApiActionRawToggleAutocastToActionRawToggleAutocast() {
        assertThatAllFieldsAreConverted(ActionRawToggleAutocast.from(sc2ApiActionRawToggleAutocast()));
    }

    private void assertThatAllFieldsAreConverted(ActionRawToggleAutocast toggleAutocast) {
        assertThat(toggleAutocast.getAbility()).as("action raw toggle autocast: ability").isEqualTo(Abilities.ATTACK);
        assertThat(toggleAutocast.getUnitTags()).as("action raw toggle autocast: unit tags")
                .containsOnlyElementsOf(UNIT_TAGS);
    }

    @Test
    void throwsExceptionIfAbilityIdIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRawToggleAutocast.from(without(
                        () -> sc2ApiActionRawToggleAutocast().toBuilder(),
                        Raw.ActionRawToggleAutocast.Builder::clearAbilityId).build()))
                .withMessage("ability id is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(toggleAutocast()).forUnits(Tag.from(UNIT_TAG)).build())
                .withMessage("ability id is required");
    }

    private ActionRawToggleAutocast.Builder fullAccessTo(Object obj) {
        return (ActionRawToggleAutocast.Builder) obj;
    }

    @Test
    void throwsExceptionIfUnitTagsAreEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRawToggleAutocast.from(without(
                        () -> sc2ApiActionRawToggleAutocast().toBuilder(),
                        Raw.ActionRawToggleAutocast.Builder::clearUnitTags).build()))
                .withMessage("unit tag list is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(toggleAutocast().ofAbility(Abilities.EFFECT_PSI_STORM)).build())
                .withMessage("unit tag list is required");
    }

    @Test
    void serializesToSc2ApiActionRawToggleAutocast() {
        assertThatAllFieldsInRequestAreSerialized(
                toggleAutocast()
                        .ofAbility(Abilities.EFFECT_PSI_STORM)
                        .forUnits(Tag.from(UNIT_TAG))
                        .build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Raw.ActionRawToggleAutocast sc2ApiRawToggleAutocast) {
        assertThat(sc2ApiRawToggleAutocast.getAbilityId()).as("sc2api raw toggle autocast: ability id")
                .isEqualTo(PSI_STORM_ABILITY_ID);
        assertThat(sc2ApiRawToggleAutocast.getUnitTagsList()).as("sc2api raw toggle autocast: ability id")
                .containsExactly(UNIT_TAG);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionRawToggleAutocast.class).withNonnullFields("ability", "unitTags").verify();
    }

}