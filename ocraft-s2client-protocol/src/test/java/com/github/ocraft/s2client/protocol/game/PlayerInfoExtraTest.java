/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PlayerInfoExtraTest {

    @Test
    void throwsExceptionWhenSc2ApiPlayerInfoExtraIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerInfoExtra.from(nothing()))
                .withMessage("sc2api player info extra is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiPlayerInfoExtra() {
        assertThatAllFieldsAreConverted(PlayerInfoExtra.from(sc2ApiPlayerInfoExtra()));
    }

    private void assertThatAllFieldsAreConverted(PlayerInfoExtra playerInfoExtra) {
        assertThat(playerInfoExtra.getPlayerInfo()).as("player info").isNotNull();
        assertThat(playerInfoExtra.getPlayerResult()).as("player result").isNotNull();
        assertThat(playerInfoExtra.getPlayerMatchMakingRating()).as("player mmr").hasValue(PLAYER_MMR);
        assertThat(playerInfoExtra.getPlayerActionPerMinute()).as("player apm").isEqualTo(PLAYER_APM);
    }

    @Test
    void throwsExceptionWhenSc2ApiPlayerInfoDoesNotExist() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerInfoExtra.from(sc2ApiPlayerInfoExtraWithoutPlayerInfo()))
                .withMessage("player info is required");
    }

    private Sc2Api.PlayerInfoExtra sc2ApiPlayerInfoExtraWithoutPlayerInfo() {
        return without(
                () -> sc2ApiPlayerInfoExtra().toBuilder(),
                Sc2Api.PlayerInfoExtra.Builder::clearPlayerInfo).build();
    }

    @Test
    void throwsExceptionWhenSc2ApiPlayerApmDoesNotExist() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerInfoExtra.from(without(
                        () -> sc2ApiPlayerInfoExtra().toBuilder(),
                        Sc2Api.PlayerInfoExtra.Builder::clearPlayerApm).build()))
                .withMessage("player apm is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(PlayerInfoExtra.class)
                .withNonnullFields("playerInfo", "playerResult")
                .verify();
    }

}