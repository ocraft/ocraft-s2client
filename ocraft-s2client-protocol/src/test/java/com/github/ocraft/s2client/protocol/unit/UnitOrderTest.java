package com.github.ocraft.s2client.protocol.unit;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.data.Abilities;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UnitOrderTest {

    @Test
    void throwsExceptionWhenSc2ApiUnitOrderIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> UnitOrder.from(nothing()))
                .withMessage("sc2api unit order is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiUnitOrder() {
        assertThatAllFieldsAreConverted(UnitOrder.from(sc2ApiUnitOrder()));
        assertThatWorldSpacePositionInConvertedIfProvided(
                UnitOrder.from(sc2ApiUnitOrderWithTargetedWorldSpacePosition()));
    }

    private void assertThatAllFieldsAreConverted(UnitOrder unitOrder) {
        assertThat(unitOrder.getAbility()).as("unit order: ability").isEqualTo(Abilities.EFFECT_PSI_STORM);
        assertThat(unitOrder.getTargetedUnitTag()).as("unit order: targeted unit tag").hasValue(Tag.from(UNIT_TAG));
        assertThat(unitOrder.getTargetedWorldSpacePosition()).as("unit order: targeted world space position").isEmpty();
        assertThat(unitOrder.getProgress()).as("unit order: progress").hasValue(TRAIN_PROGRESS);
    }

    private void assertThatWorldSpacePositionInConvertedIfProvided(UnitOrder unitOrder) {
        assertThat(unitOrder.getTargetedUnitTag()).as("unit order: targeted unit tag").isEmpty();
        assertThat(unitOrder.getTargetedWorldSpacePosition()).as("unit order: targeted world space position")
                .isNotEmpty();
    }

    @Test
    void throwsExceptionWhenAbilityIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> UnitOrder.from(
                        without(() -> sc2ApiUnitOrder().toBuilder(), Raw.UnitOrder.Builder::clearAbilityId).build()))
                .withMessage("ability is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(UnitOrder.class).withNonnullFields("ability").verify();
    }

}