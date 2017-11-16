package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class WeaponTest {
    @Test
    void throwsExceptionWhenSc2ApiWeaponIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Weapon.from(nothing()))
                .withMessage("sc2api weapon is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiWeapon() {
        assertThatAllFieldsAreConverted(Weapon.from(sc2ApiWeapon()));
    }

    private void assertThatAllFieldsAreConverted(Weapon weapon) {
        assertThat(weapon.getTargetType()).as("weapon: target type").isNotNull();
        assertThat(weapon.getDamage()).as("weapon: damage").isEqualTo(WEAPON_DAMAGE);
        assertThat(weapon.getDamageBonuses()).as("weapon: damage bonuses").isNotEmpty();
        assertThat(weapon.getAttacks()).as("weapon: attacks").isEqualTo(WEAPON_ATTACKS);
        assertThat(weapon.getRange()).as("weapon: range").isEqualTo(WEAPON_RANGE);
        assertThat(weapon.getSpeed()).as("weapon: speed").isEqualTo(WEAPON_SPEED);
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "targetTypeMappings")
    void mapsSc2ApiTargetType(Data.Weapon.TargetType sc2ApiTargetType, Weapon.TargetType expectedTargetType) {
        assertThat(Weapon.TargetType.from(sc2ApiTargetType)).isEqualTo(expectedTargetType);
    }

    private static Stream<Arguments> targetTypeMappings() {
        return Stream.of(
                Arguments.of(Data.Weapon.TargetType.Ground, Weapon.TargetType.GROUND),
                Arguments.of(Data.Weapon.TargetType.Air, Weapon.TargetType.AIR),
                Arguments.of(Data.Weapon.TargetType.Any, Weapon.TargetType.ANY));
    }

    @Test
    void throwsExceptionWhenWeaponTargetTypeIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Weapon.TargetType.from(nothing()))
                .withMessage("sc2api target type is required");
    }

    @Test
    void throwsExceptionWhenTargetTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Weapon.from(
                        without(() -> sc2ApiWeapon().toBuilder(), Data.Weapon.Builder::clearType).build()))
                .withMessage("target type is required");
    }

    @Test
    void throwsExceptionWhenDamageIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Weapon.from(
                        without(() -> sc2ApiWeapon().toBuilder(), Data.Weapon.Builder::clearDamage).build()))
                .withMessage("damage is required");
    }

    @Test
    void hasEmptySetWhenDamageBonusesAreNotProvided() {
        assertThat(Weapon.from(without(
                () -> sc2ApiWeapon().toBuilder(), Data.Weapon.Builder::clearDamageBonus).build()
        ).getDamageBonuses()).as("weapon: default damage bonuses").isEmpty();
    }

    @Test
    void throwsExceptionWhenAttacksIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Weapon.from(
                        without(() -> sc2ApiWeapon().toBuilder(), Data.Weapon.Builder::clearAttacks).build()))
                .withMessage("attacks is required");
    }

    @Test
    void throwsExceptionWhenRangeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Weapon.from(
                        without(() -> sc2ApiWeapon().toBuilder(), Data.Weapon.Builder::clearRange).build()))
                .withMessage("range is required");
    }

    @Test
    void throwsExceptionWhenSpeedIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Weapon.from(
                        without(() -> sc2ApiWeapon().toBuilder(), Data.Weapon.Builder::clearSpeed).build()))
                .withMessage("speed is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Weapon.class).withNonnullFields("targetType", "damageBonuses").verify();
    }
}