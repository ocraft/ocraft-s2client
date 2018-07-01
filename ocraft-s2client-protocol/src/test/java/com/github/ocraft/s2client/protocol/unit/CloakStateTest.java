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

class CloakStateTest {

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "cloakStateMappings")
    void mapsSc2ApiCloakState(Raw.CloakState sc2ApiCloakState, CloakState expectedCloakState) {
        assertThat(CloakState.from(sc2ApiCloakState)).isEqualTo(expectedCloakState);
    }

    private static Stream<Arguments> cloakStateMappings() {
        return Stream.of(
                of(Raw.CloakState.NotCloaked, CloakState.NOT_CLOAKED),
                of(Raw.CloakState.Cloaked, CloakState.CLOAKED),
                of(Raw.CloakState.CloakedDetected, CloakState.CLOAKED_DETECTED));
    }

    @Test
    void throwsExceptionWhenCloakStateIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CloakState.from(nothing()))
                .withMessage("sc2api cloak state is required");
    }


}
