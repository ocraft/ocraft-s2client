package com.github.ocraft.s2client.protocol.response;

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