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

import SC2APIProtocol.Common;
import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Fixtures;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.game.ComputerPlayerSetup.computer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ComputerPlayerSetupTest {

    @Test
    void throwsExceptionWhenRaceIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> computer(nothing(), Difficulty.MEDIUM))
                .withMessage("race is required");
    }

    @Test
    void throwsExceptionWhenDifficultyIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> computer(Race.PROTOSS, nothing()))
                .withMessage("difficulty level is required");
    }

    @Test
    void serializesToSc2ApiPlayerSetup() {
        assertThat(ComputerPlayerSetup.computer(Race.PROTOSS, Difficulty.MEDIUM, Fixtures.PLAYER_NAME).toSc2Api())
                .as("sc2api computer player setup")
                .isEqualTo(expectedComputerPlayerSetup());
    }

    private Sc2Api.PlayerSetup expectedComputerPlayerSetup() {
        return Sc2Api.PlayerSetup.newBuilder()
                .setType(Sc2Api.PlayerType.Computer)
                .setRace(Common.Race.Protoss)
                .setDifficulty(Sc2Api.Difficulty.Medium)
                .setPlayerName(Fixtures.PLAYER_NAME)
                .build();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ComputerPlayerSetup.class)
                .withRedefinedSuperclass()
                .withNonnullFields("difficulty", "race", "playerType")
                .verify();
    }

}
