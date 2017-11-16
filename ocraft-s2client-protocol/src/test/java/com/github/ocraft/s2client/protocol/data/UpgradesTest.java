package com.github.ocraft.s2client.protocol.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class UpgradesTest {
    @ParameterizedTest
    @EnumSource(Upgrades.class)
    void isMappedBySc2ApiUpgradeId(Upgrade upgrade) {
        assertThat(Upgrades.from(upgrade.getUpgradeId())).as("upgrade mapped from upgrade id").isEqualTo(upgrade);
    }

    @Test
    void returnsOtherTypeIfIdIsNotMapped() {
        assertThat(Upgrades.from(-1000)).as("effect: not mapped id").isEqualTo(Upgrades.Other.of(-1000));
    }
}