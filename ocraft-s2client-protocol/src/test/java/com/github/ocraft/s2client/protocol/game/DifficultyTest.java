package com.github.ocraft.s2client.protocol.game;

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
                Arguments.of(Difficulty.MEDIUM_HARD, Sc2Api.Difficulty.MediumHard),
                Arguments.of(Difficulty.HARD, Sc2Api.Difficulty.Hard),
                Arguments.of(Difficulty.HARDER, Sc2Api.Difficulty.Harder),
                Arguments.of(Difficulty.VERY_HARD, Sc2Api.Difficulty.VeryHard),
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

}