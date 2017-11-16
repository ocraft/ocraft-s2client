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
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiResponseWithObserverAction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseObserverActionTest {
    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveObserverAction() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseObserverAction.from(nothing()))
                .withMessage("provided argument doesn't have observer action response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseObserverAction.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have observer action response");
    }

    @Test
    void convertsSc2ApiResponseObserverActionToResponseObserverAction() {
        ResponseObserverAction responseObserverAction = ResponseObserverAction.from(sc2ApiResponseWithObserverAction());

        assertThat(responseObserverAction).as("converted response observer action").isNotNull();
        assertThat(responseObserverAction.getType()).as("type of observer action response")
                .isEqualTo(ResponseType.OBSERVER_ACTION);
        assertThat(responseObserverAction.getStatus()).as("status of observer action response")
                .isEqualTo(GameStatus.IN_GAME);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseObserverAction.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}