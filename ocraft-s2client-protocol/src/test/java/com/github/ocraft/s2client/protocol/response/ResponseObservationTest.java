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
import com.github.ocraft.s2client.protocol.unit.UnitInfo;
import com.google.protobuf.ByteString;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseObservationTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveObservation() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseObservation.from(nothing()))
                .withMessage("provided argument doesn't have observation response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseObservation.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have observation response");
    }

    @Test
    void convertsSc2ApiResponseObservationToResponseObservation() {
        ResponseObservation responseObservation = ResponseObservation.from(sc2ApiResponseWithObservation());

        assertThatResponseIsInValidState(responseObservation);
        assertThatAllFieldsAreConverted(responseObservation);
    }

    private void assertThatResponseIsInValidState(ResponseObservation responseObservation) {
        assertThat(responseObservation).as("converted response observation").isNotNull();
        assertThat(responseObservation.getType()).as("type of observation response")
                .isEqualTo(ResponseType.OBSERVATION);
        assertThat(responseObservation.getStatus()).as("status of observation response").isEqualTo(GameStatus.IN_GAME);
    }

    private void assertThatAllFieldsAreConverted(ResponseObservation responseObservation) {
        assertThat(responseObservation.getActions()).as("response observation: actions").isNotEmpty();
        assertThat(responseObservation.getActionErrors()).as("response observation: action errors").isNotEmpty();
        assertThat(responseObservation.getObservation()).as("response observation: observation").isNotNull();
        assertThat(responseObservation.getPlayerResults()).as("response observation: player results").isNotEmpty();
        assertThat(responseObservation.getChat()).as("response observation: chat").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenObservationDoesNotExist() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseObservation.from(emptySc2ApiResponseObservation()))
                .withMessage("observation is required");
    }

    @Test
    void hasEmptyListsWhenSc2ApiResponseObservationIsEmpty() {
        ResponseObservation responseObservation = ResponseObservation.from(sc2ApiResponseObservationWithEmptyLists());
        assertThat(responseObservation.getActions()).as("response observation: actions").isEmpty();
        assertThat(responseObservation.getActionErrors()).as("response observation: action errors").isEmpty();
        assertThat(responseObservation.getPlayerResults()).as("response observation: player results").isEmpty();
        assertThat(responseObservation.getChat()).as("response observation: chat").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() throws UnsupportedEncodingException {
        EqualsVerifier.forClass(ResponseObservation.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status", "actions", "actionErrors", "observation", "playerResults", "chat")
                .withRedefinedSuperclass()
                .withPrefabValues(UnitInfo.class, UnitInfo.from(sc2ApiUnitInfoAddOn()), UnitInfo.from(sc2ApiUnitInfo()))
                .withPrefabValues(
                        ByteString.class,
                        ByteString.copyFrom("test", "UTF-8"),
                        ByteString.copyFrom("test2", "UTF-8"))
                .verify();
    }

}
