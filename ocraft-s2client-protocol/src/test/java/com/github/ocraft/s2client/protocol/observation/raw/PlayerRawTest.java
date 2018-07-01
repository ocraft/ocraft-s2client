package com.github.ocraft.s2client.protocol.observation.raw;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
