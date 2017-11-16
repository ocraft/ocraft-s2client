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
package com.github.ocraft.s2client.protocol.response;

import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.aSc2ApiResponse;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiResponseWithDebug;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseDebugTest {
    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveDebug() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseDebug.from(nothing()))
                .withMessage("provided argument doesn't have debug response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseDebug.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have debug response");
    }

    @Test
    void convertsSc2ApiResponseDebugToResponseDebug() {
        ResponseDebug responseDebug = ResponseDebug.from(sc2ApiResponseWithDebug());

        assertThat(responseDebug).as("converted response debug").isNotNull();
        assertThat(responseDebug.getType()).as("type of debug response")
                .isEqualTo(ResponseType.DEBUG);
        assertThat(responseDebug.getStatus()).as("status of debug response").isEqualTo(GameStatus.IN_GAME);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseDebug.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}