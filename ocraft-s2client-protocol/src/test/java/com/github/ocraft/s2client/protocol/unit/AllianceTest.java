package com.github.ocraft.s2client.protocol.unit;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class AllianceTest {
    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "allianceMappings")
    void mapsSc2ApiAlliance(Raw.Alliance sc2ApiAlliance, Alliance expectedAlliance) {
        assertThat(Alliance.from(sc2ApiAlliance)).isEqualTo(expectedAlliance);
    }

    private static Stream<Arguments> allianceMappings() {
        return Stream.of(
                of(Raw.Alliance.Self, Alliance.SELF),
                of(Raw.Alliance.Ally, Alliance.ALLY),
                of(Raw.Alliance.Enemy, Alliance.ENEMY),
                of(Raw.Alliance.Neutral, Alliance.NEUTRAL));
    }

    @Test
    void throwsExceptionWhenAllianceIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Alliance.from(nothing()))
                .withMessage("sc2api alliance is required");
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "playerRelativeMappings")
    void mapsSc2ApiPlayerRelative(int sc2ApiPlayerRellative, Alliance expectedAlliance) {
        assertThat(Alliance.from(sc2ApiPlayerRellative)).isEqualTo(expectedAlliance);
    }

    private static Stream<Arguments> playerRelativeMappings() {
        return Stream.of(
                of(1, Alliance.SELF),
                of(2, Alliance.ALLY),
                of(3, Alliance.NEUTRAL),
                of(4, Alliance.ENEMY));
    }

}
