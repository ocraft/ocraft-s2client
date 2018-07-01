package com.github.ocraft.s2client.protocol.game;

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

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PlayerInfoTest {

    @Test
    void throwsExceptionWhenSc2ApiPlayerInfoIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerInfo.from(nothing()))
                .withMessage("sc2api player info is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiPlayerInfo() {
        assertThatAllFieldsAreConverted(PlayerInfo.from(sc2ApiPlayerInfo()));
    }

    private void assertThatAllFieldsAreConverted(PlayerInfo playerInfo) {
        assertThat(playerInfo.getPlayerId()).as("player id").isEqualTo(PLAYER_ID);
        assertThat(playerInfo.getPlayerType()).as("player type").hasValue(PlayerType.PARTICIPANT);
        assertThat(playerInfo.getRequestedRace()).as("requested race").isEqualTo(Race.ZERG);
        assertThat(playerInfo.getActualRace()).as("actual race").hasValue(Race.PROTOSS);
        assertThat(playerInfo.getDifficulty()).as("difficulty").hasValue(Difficulty.CHEAT_VISION);
    }

    @Test
    void throwsExceptionWhenSc2ApiPlayerIdDoesNotExist() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerInfo.from(sc2ApiPlayerInfoWithoutPlayerId()))
                .withMessage("player id is required");
    }

    private Sc2Api.PlayerInfo sc2ApiPlayerInfoWithoutPlayerId() {
        return without(
                () -> sc2ApiPlayerInfoExtra().toBuilder().getPlayerInfoBuilder(),
                Sc2Api.PlayerInfo.Builder::clearPlayerId).build();
    }

    @Test
    void throwsExceptionWhenSc2ApiRequestedRaceDoesNotExist() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerInfo.from(sc2ApiPlayerInfoWithoutRaceRequested()))
                .withMessage("requested race is required");
    }

    private Sc2Api.PlayerInfo sc2ApiPlayerInfoWithoutRaceRequested() {
        return without(
                () -> sc2ApiPlayerInfoExtra().toBuilder().getPlayerInfoBuilder(),
                Sc2Api.PlayerInfo.Builder::clearRaceRequested).build();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(PlayerInfo.class).withNonnullFields("requestedRace").verify();
    }
}
