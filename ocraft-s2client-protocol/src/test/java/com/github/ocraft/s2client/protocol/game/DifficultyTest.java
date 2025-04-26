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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DifficultyTest {

    @ParameterizedTest(name = "\"{0}\" serializes to {1}")
    @MethodSource(value = "difficultyMappings")
    void serializesToSc2Api(Difficulty difficulty, Sc2Api.Difficulty expectedSc2ApiDifficulty) {
        assertThat(difficulty.toSc2Api()).isEqualTo(expectedSc2ApiDifficulty);

    }

    private static Stream<Arguments> difficultyMappings() {
        return Stream.of(
                Arguments.of(Difficulty.VERY_EASY, Sc2Api.Difficulty.VeryEasy),
                Arguments.of(Difficulty.EASY, Sc2Api.Difficulty.Easy),
                Arguments.of(Difficulty.MEDIUM, Sc2Api.Difficulty.Medium),
                Arguments.of(Difficulty.HARD, Sc2Api.Difficulty.Hard),
                Arguments.of(Difficulty.HARDER, Sc2Api.Difficulty.Harder),
                Arguments.of(Difficulty.VERY_HARD, Sc2Api.Difficulty.VeryHard),
                Arguments.of(Difficulty.ELITE, Sc2Api.Difficulty.Elite),
                Arguments.of(Difficulty.CHEAT_VISION, Sc2Api.Difficulty.CheatVision),
                Arguments.of(Difficulty.CHEAT_MONEY, Sc2Api.Difficulty.CheatMoney),
                Arguments.of(Difficulty.CHEAT_INSANE, Sc2Api.Difficulty.CheatInsane)
        );
    }

    @ParameterizedTest(name = "\"{0}\" maps {1}")
    @MethodSource(value = "difficultyMappings")
    void mapsSc2ApiDifficulty(Difficulty expectedDifficulty, Sc2Api.Difficulty sc2ApiDifficulty) {
        assertThat(Difficulty.from(sc2ApiDifficulty)).isEqualTo(expectedDifficulty);
    }

    @Test
    void throwsExceptionWhenSc2ApiDifficultyIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Difficulty.from(nothing()))
                .withMessage("sc2api difficulty is required");
    }

    @Test
    void createsDifficultyFromName() {
        assertThat(Difficulty.forName("veryHard")).isEqualTo(Difficulty.VERY_HARD);
    }
}
