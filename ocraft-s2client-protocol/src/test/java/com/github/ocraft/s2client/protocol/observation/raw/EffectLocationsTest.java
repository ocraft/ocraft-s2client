package com.github.ocraft.s2client.protocol.observation.raw;

import SC2APIProtocol.Raw;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiEffect;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class EffectLocationsTest {
    @Test
    void throwsExceptionWhenSc2ApiEffectIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> EffectLocations.from(nothing()))
                .withMessage("sc2api effect is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiEffect() {
        assertThatAllFieldsAreConverted(EffectLocations.from(sc2ApiEffect()));
    }

    private void assertThatAllFieldsAreConverted(EffectLocations effect) {
        assertThat(effect.getEffect()).as("effect: effect").isNotNull();
        assertThat(effect.getPositions()).as("effect: positions").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenEffectIdIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> EffectLocations.from(without(
                        () -> sc2ApiEffect().toBuilder(),
                        Raw.Effect.Builder::clearEffectId).build()))
                .withMessage("effect is required");
    }

    @Test
    void hasEmptySetOfPositionsWhenNotProvided() {
        assertThat(EffectLocations.from(
                without(() -> sc2ApiEffect().toBuilder(), Raw.Effect.Builder::clearPos).build()
        ).getPositions()).as("effect: empty positions set").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(EffectLocations.class).withNonnullFields("effect", "positions").verify();
    }
}