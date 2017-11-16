package com.github.ocraft.s2client.protocol.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class BuffsTest {
    @ParameterizedTest
    @EnumSource(Buffs.class)
    void isMappedBySc2ApiBuffId(Buff buff) {
        assertThat(Buffs.from(buff.getBuffId())).as("buff mapped from buff id").isEqualTo(buff);
    }

    @Test
    void returnsOtherTypeIfIdIsNotMapped() {
        assertThat(Buffs.from(-1000)).as("buff: not mapped id").isEqualTo(Buffs.Other.of(-1000));
    }
}