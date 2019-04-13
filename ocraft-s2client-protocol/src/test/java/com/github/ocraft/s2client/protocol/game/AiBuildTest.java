package com.github.ocraft.s2client.protocol.game;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2019 Ocraft Project
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

class AiBuildTest {
    @Test
    void throwsExceptionWhenSc2ApiAiBuildIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AiBuild.from(nothing()))
                .withMessage("sc2api ai build is required");
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "AiBuildMappings")
    void mapsSc2ApiAiBuild(Sc2Api.AIBuild sc2ApiAiBuild, AiBuild expectedAiBuild) {
        assertThat(AiBuild.from(sc2ApiAiBuild)).isEqualTo(expectedAiBuild);

    }

    private static Stream<Arguments> AiBuildMappings() {
        return Stream.of(
                Arguments.of(Sc2Api.AIBuild.RandomBuild, AiBuild.RANDOM_BUILD),
                Arguments.of(Sc2Api.AIBuild.Rush, AiBuild.RUSH),
                Arguments.of(Sc2Api.AIBuild.Timing, AiBuild.TIMING),
                Arguments.of(Sc2Api.AIBuild.Power, AiBuild.POWER),
                Arguments.of(Sc2Api.AIBuild.Macro, AiBuild.MACRO),
                Arguments.of(Sc2Api.AIBuild.Air, AiBuild.AIR)
        );
    }

    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "AiBuildMappings")
    void serializesToSc2ApiAiBuild(Sc2Api.AIBuild expectedSc2ApiAiBuild, AiBuild aiBuild) {
        assertThat(aiBuild.toSc2Api()).isEqualTo(expectedSc2ApiAiBuild);

    }
}
