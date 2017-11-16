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
package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PlayerResultTest {

    @Test
    void throwsExceptionWhenSc2ApiPlayerResultIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerResult.from(nothing()))
                .withMessage("sc2api player result is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiPlayerResult() {
        assertThatAllFieldsAreConverted(PlayerResult.from(sc2ApiPlayerResult()));
    }

    private void assertThatAllFieldsAreConverted(PlayerResult playerInfo) {
        assertThat(playerInfo.getPlayerId()).as("player id").isEqualTo(PLAYER_ID);
        assertThat(playerInfo.getResult()).as("result").isEqualTo(Result.VICTORY);
    }

    @Test
    void throwsExceptionWhenSc2ApiPlayerInfoDoesNotExist() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerResult.from(sc2ApiPlayerResultWithoutPlayerId()))
                .withMessage("player id is required");
    }

    private Sc2Api.PlayerResult sc2ApiPlayerResultWithoutPlayerId() {
        return without(
                () -> sc2ApiPlayerResult().toBuilder(),
                Sc2Api.PlayerResult.Builder::clearPlayerId).build();
    }

    @Test
    void throwsExceptionWhenSc2ApiResultDoesNotExist() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerResult.from(sc2PlayerResultWithoutResult()))
                .withMessage("result is required");
    }

    private Sc2Api.PlayerResult sc2PlayerResultWithoutResult() {
        return without(
                () -> sc2ApiPlayerResult().toBuilder(),
                Sc2Api.PlayerResult.Builder::clearResult).build();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(PlayerResult.class).withNonnullFields("result").verify();
    }

}