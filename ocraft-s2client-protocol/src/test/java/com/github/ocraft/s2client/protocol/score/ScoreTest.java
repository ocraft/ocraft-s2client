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
package com.github.ocraft.s2client.protocol.score;

import SC2APIProtocol.ScoreOuterClass;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.score.Score.Type.CURRICULUM;
import static com.github.ocraft.s2client.protocol.score.Score.Type.MELEE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class ScoreTest {

    @Test
    void throwsExceptionWhenSc2ApiScoreIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Score.from(nothing()))
                .withMessage("sc2api score is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiScore() {
        assertThatAllFieldsAreConverted(Score.from(sc2ApiScore()));
    }

    private void assertThatAllFieldsAreConverted(Score score) {
        assertThat(score.getType()).as("score: type").isEqualTo(MELEE);
        assertThat(score.getScore()).as("score: score").isEqualTo(SCORE);
        assertThat(score.getDetails()).as("score: details").isNotNull();
    }

    @Test
    void throwsExceptionWhenTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Score.from(without(
                        () -> sc2ApiScore().toBuilder(),
                        ScoreOuterClass.Score.Builder::clearScoreType).build()))
                .withMessage("type is required");
    }

    @Test
    void throwsExceptionWhenScoreIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Score.from(without(
                        () -> sc2ApiScore().toBuilder(),
                        ScoreOuterClass.Score.Builder::clearScore).build()))
                .withMessage("score is required");
    }

    @Test
    void throwsExceptionWhenDetailsAreNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Score.from(without(
                        () -> sc2ApiScore().toBuilder(),
                        ScoreOuterClass.Score.Builder::clearScoreDetails).build()))
                .withMessage("details is required");
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "scoreTypeMappings")
    void mapsSc2ApiScoreType(ScoreOuterClass.Score.ScoreType sc2ApiScoreType, Score.Type expectedScoreType) {
        assertThat(Score.Type.from(sc2ApiScoreType)).isEqualTo(expectedScoreType);
    }

    private static Stream<Arguments> scoreTypeMappings() {
        return Stream.of(
                of(ScoreOuterClass.Score.ScoreType.Curriculum, CURRICULUM),
                of(ScoreOuterClass.Score.ScoreType.Melee, MELEE));
    }

    @Test
    void throwsExceptionWhenSc2ApiScoreTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Score.Type.from(nothing()))
                .withMessage("sc2api score type is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Score.class).withNonnullFields("type", "details").verify();
    }

}