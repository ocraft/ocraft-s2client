package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class EffectDataTest {
    @Test
    void throwsExceptionWhenSc2ApiEffectDataIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> EffectData.from(nothing()))
                .withMessage("sc2api effect data is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiUnit() {
        assertThatAllFieldsAreConverted(EffectData.from(sc2ApiEffectData()));
    }

    private void assertThatAllFieldsAreConverted(EffectData effect) {
        assertThat(effect.getEffect()).as("effect: id").isNotNull();
        assertThat(effect.getName()).as("effect: name").isEqualTo(EFFECT_NAME);
        assertThat(effect.getFriendlyName()).as("effect: friendly name").isEqualTo(EFFECT_FRIENDLY_NAME);
        assertThat(effect.getRadius()).as("effect: radius").isEqualTo(EFFECT_RADIUS);
    }

    @Test
    void throwsExceptionWhenEffectIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> EffectData.from(without(
                        () -> sc2ApiEffectData().toBuilder(),
                        Data.EffectData.Builder::clearEffectId).build()))
                .withMessage("effect is required");
    }

    @Test
    void throwsExceptionWhenNameIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> EffectData.from(without(
                        () -> sc2ApiEffectData().toBuilder(),
                        Data.EffectData.Builder::clearName).build()))
                .withMessage("name is required");
    }

    @Test
    void throwsExceptionWhenFriendlyNameIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> EffectData.from(without(
                        () -> sc2ApiEffectData().toBuilder(),
                        Data.EffectData.Builder::clearFriendlyName).build()))
                .withMessage("friendly name is required");
    }

    @Test
    void throwsExceptionWhenRadiusIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> EffectData.from(without(
                        () -> sc2ApiEffectData().toBuilder(),
                        Data.EffectData.Builder::clearRadius).build()))
                .withMessage("radius is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(EffectData.class).withNonnullFields("effect", "name", "friendlyName").verify();
    }

}