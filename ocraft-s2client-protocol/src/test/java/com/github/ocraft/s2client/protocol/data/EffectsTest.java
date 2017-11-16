package com.github.ocraft.s2client.protocol.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class EffectsTest {
    @ParameterizedTest
    @EnumSource(Effects.class)
    void isMappedBySc2ApiEffectId(Effect effect) {
        assertThat(Effects.from(effect.getEffectId())).as("effect mapped from effect id").isEqualTo(effect);
    }

    @Test
    void returnsOtherTypeIfIdIsNotMapped() {
        assertThat(Effects.from(-1000)).as("effect: not mapped id").isEqualTo(Effects.Other.of(-1000));
    }
}