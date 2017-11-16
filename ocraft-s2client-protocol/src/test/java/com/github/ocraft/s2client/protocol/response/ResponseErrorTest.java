package com.github.ocraft.s2client.protocol.response;

import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseErrorTest {

    @Test
    void throwsIllegalArgumentExceptionForNullArgumentsOrEmptyErrorListInSc2ApiResponse() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseError.from(nothing()))
                .withMessage("provided argument doesn't have error response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseError.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have error response");
    }

    @Test
    void convertsSc2ApiResponseWithErrorsToResponseError() {
        ResponseError responseError = ResponseError.from(sc2ApiResponseWithError());

        assertThat(responseError.getErrors()).containsExactlyElementsOf(ERRORS);
        assertThat(responseError.getType()).as("type of error response").isEqualTo(ResponseType.ERROR);
        assertThat(responseError.getStatus()).as("status of error response").isEqualTo(GameStatus.LAUNCHED);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ResponseError.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status", "errors")
                .withRedefinedSuperclass()
                .verify();
    }

}