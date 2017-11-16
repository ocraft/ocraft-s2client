package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Common;
import com.github.ocraft.s2client.protocol.data.Abilities;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiAvailableAbility;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AvailableAbilityTest {

    @Test
    void throwsExceptionWhenSc2ApiAvailableAbilityIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AvailableAbility.from(nothing()))
                .withMessage("sc2api available ability is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiAvailableAbility() {
        assertThatAllFieldsAreConverted(AvailableAbility.from(sc2ApiAvailableAbility()));
    }

    private void assertThatAllFieldsAreConverted(AvailableAbility availableAbility) {
        assertThat(availableAbility.getAbility()).as("available ability: ability").isEqualTo(Abilities.EFFECT_PSI_STORM);
        assertThat(availableAbility.isRequiresPoint()).as("available ability: required point").isTrue();
    }

    @Test
    void throwsExceptionWhenAbilityIdIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AvailableAbility.from(without(
                        () -> sc2ApiAvailableAbility().toBuilder(),
                        Common.AvailableAbility.Builder::clearAbilityId).build()))
                .withMessage("ability is required");
    }

    @Test
    void hasDefaultValueForRequiresPointFieldIfNotProvided() {
        AvailableAbility availableAbility = AvailableAbility.from(without(
                () -> sc2ApiAvailableAbility().toBuilder(),
                Common.AvailableAbility.Builder::clearRequiresPoint).build());
        assertThat(availableAbility.isRequiresPoint()).as("available ability: requires point").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(AvailableAbility.class).withNonnullFields("ability").verify();
    }
}