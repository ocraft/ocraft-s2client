package com.github.ocraft.s2client.protocol.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class AbilitiesTest {

    @ParameterizedTest
    @EnumSource(Abilities.class)
    void isMappedBySc2ApiAbilityId(Ability ability) {
        assertThat(Abilities.from(ability.toSc2Api())).as("ability mapped from ability id").isEqualTo(ability);
    }

    @Test
    void returnsOtherTypeIfIdIsNotMapped() {
        assertThat(Abilities.from(-1000)).as("ability: not mapped id").isEqualTo(Abilities.Other.of(-1000));
    }

}