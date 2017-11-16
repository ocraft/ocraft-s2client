package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DamageBonusTest {
    @Test
    void throwsExceptionWhenSc2ApiDamageBonusIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DamageBonus.from(nothing()))
                .withMessage("sc2api damage bonus is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiDamageBonus() {
        assertThatAllFieldsAreConverted(DamageBonus.from(sc2ApiDamageBonus()));
    }

    private void assertThatAllFieldsAreConverted(DamageBonus damageBonus) {
        assertThat(damageBonus.getAttribute()).as("damage bonus: attribute").isNotNull();
        assertThat(damageBonus.getBonus()).as("damage bonus: bonus").isEqualTo(WEAPON_DAMAGE_BONUS);
    }

    @Test
    void throwsExceptionWhenAttributeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DamageBonus.from(without(
                        () -> sc2ApiDamageBonus().toBuilder(),
                        Data.DamageBonus.Builder::clearAttribute).build()))
                .withMessage("attribute is required");
    }

    @Test
    void throwsExceptionWhenBonusIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DamageBonus.from(without(
                        () -> sc2ApiDamageBonus().toBuilder(),
                        Data.DamageBonus.Builder::clearBonus).build()))
                .withMessage("bonus is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DamageBonus.class).withNonnullFields("attribute").verify();
    }
}