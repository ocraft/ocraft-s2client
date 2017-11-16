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
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseSaveReplayTest {
    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveSaveReplay() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseSaveReplay.from(nothing()))
                .withMessage("provided argument doesn't have save replay response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseSaveReplay.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have save replay response");
    }

    @Test
    void convertsSc2ApiResponseSaveReplayToResponseSaveReplay() {
        ResponseSaveReplay responseSaveReplay = ResponseSaveReplay.from(sc2ApiResponseWithSaveReplay());

        assertThatResponseIsInValidState(responseSaveReplay);
        assertThat(responseSaveReplay.getData()).as("response save replay: data").isEqualTo(DATA_IN_BYTES);

    }

    private void assertThatResponseIsInValidState(ResponseSaveReplay responseSaveReplay) {
        assertThat(responseSaveReplay).as("converted response save replay").isNotNull();
        assertThat(responseSaveReplay.getType()).as("type of save replay response")
                .isEqualTo(ResponseType.SAVE_REPLAY);
        assertThat(responseSaveReplay.getStatus()).as("status of save replay response").isEqualTo(GameStatus.ENDED);
    }

    @Test
    void throwsExceptionWhenDataIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseSaveReplay.from(sc2ApiResponseWithSaveReplayWithoutData()))
                .withMessage("data is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseSaveReplay.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status", "data")
                .withRedefinedSuperclass()
                .verify();
    }
}