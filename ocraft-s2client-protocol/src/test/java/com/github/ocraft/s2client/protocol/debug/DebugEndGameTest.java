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
package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Fixtures.debugEndGame;
import static com.github.ocraft.s2client.protocol.debug.DebugEndGame.endGame;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class DebugEndGameTest {
    @Test
    void serializesToSc2ApiDebugEndGame() {
        assertThatAllFieldsAreSerialized(debugEndGame().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugEndGame sc2ApiEndGame) {
        assertThat(sc2ApiEndGame.hasEndResult()).as("sc2api end game: has result").isTrue();
    }

    @Test
    void throwsExceptionWhenResultIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((DebugEndGame.Builder) endGame()).build())
                .withMessage("result is required");
    }

    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "resultMappings")
    void serializesToSc2ApiTest(
            Debug.DebugEndGame.EndResult expectedSc2ApiEndResult, DebugEndGame.EndResult endResult) {
        assertThat(endResult.toSc2Api()).isEqualTo(expectedSc2ApiEndResult);
    }

    private static Stream<Arguments> resultMappings() {
        return Stream.of(
                of(Debug.DebugEndGame.EndResult.Surrender, DebugEndGame.EndResult.SURRENDER),
                of(Debug.DebugEndGame.EndResult.DeclareVictory, DebugEndGame.EndResult.DECLARE_VICTORY));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugEndGame.class).withNonnullFields("result").verify();
    }
}