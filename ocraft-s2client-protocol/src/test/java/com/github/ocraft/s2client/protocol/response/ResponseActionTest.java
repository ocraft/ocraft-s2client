package com.github.ocraft.s2client.protocol.response;

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

import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseActionTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveAction() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseAction.from(nothing()))
                .withMessage("provided argument doesn't have action response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseAction.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have action response");
    }

    @Test
    void hasEmptyListOfResultsForEmptySc2ApiResponseAction() {
        ResponseAction responseAction = ResponseAction.from(emptySc2ApiResponseWithAction());
        assertThat(responseAction.getResults()).as("response action: default result list").isEmpty();
    }

    @Test
    void convertsSc2ApiResponseActionToResponseAction() {
        assertThatAllFieldsAreProperlyConverted(ResponseAction.from(sc2ApiResponseWithAction()));
    }

    private void assertThatAllFieldsAreProperlyConverted(ResponseAction responseAction) {
        assertThat(responseAction.getResults()).as("response action: results").isNotEmpty();
        assertThat(responseAction.getType()).as("response action: type").isEqualTo(ResponseType.ACTION);
        assertThat(responseAction.getStatus()).as("response action: status").isEqualTo(GameStatus.IN_GAME);
    }


    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ResponseAction.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status", "results")
                .withRedefinedSuperclass()
                .verify();
    }
}
