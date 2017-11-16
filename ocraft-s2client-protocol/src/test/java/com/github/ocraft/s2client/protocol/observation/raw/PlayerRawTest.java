package com.github.ocraft.s2client.protocol.observation.raw;

import SC2APIProtocol.Raw;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PlayerRawTest {

    @Test
    void throwsExceptionWhenSc2ApiPlayerRawIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerRaw.from(nothing()))
                .withMessage("sc2api player raw is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiPlayerRaw() {
        assertThatAllFieldsAreConverted(PlayerRaw.from(sc2ApiPlayerRaw()));
    }

    private void assertThatAllFieldsAreConverted(PlayerRaw playerRaw) {
        assertThat(playerRaw.getPowerSources()).as("player raw: power sources").isNotEmpty();
        assertThat(playerRaw.getCamera()).as("player raw: camera").isNotNull();
        assertThat(playerRaw.getUpgrades()).as("player raw: upgrades").containsOnlyElementsOf(UPGRADES);
    }

    @Test
    void hasEmptySetOfPowerSourcesWhenNotProvided() {
        assertThat(PlayerRaw.from(
                without(() -> sc2ApiPlayerRaw().toBuilder(), Raw.PlayerRaw.Builder::clearPowerSources).build()
        ).getPowerSources()).as("player raw: empty power source set").isEmpty();
    }

    @Test
    void throwsExceptionWhenCameraIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerRaw.from(without(
                        () -> sc2ApiPlayerRaw().toBuilder(),
                        Raw.PlayerRaw.Builder::clearCamera).build()))
                .withMessage("camera is required");
    }

    @Test
    void hasEmptySetOfUpgradesWhenNotProvided() {
        assertThat(PlayerRaw.from(
                without(() -> sc2ApiPlayerRaw().toBuilder(), Raw.PlayerRaw.Builder::clearUpgradeIds).build()
        ).getUpgrades()).as("player raw: empty upgrade set").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(PlayerRaw.class).withNonnullFields("powerSources", "camera", "upgrades").verify();
    }

}