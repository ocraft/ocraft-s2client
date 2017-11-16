package com.github.ocraft.s2client.protocol.response;

import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.aSc2ApiResponse;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiResponseWithStep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseStepTest {
    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveStep() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseStep.from(nothing()))
                .withMessage("provided argument doesn't have step response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseStep.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have step response");
    }

    @Test
    void convertsSc2ApiResponseStepToResponseStep() {
        ResponseStep responseStep = ResponseStep.from(sc2ApiResponseWithStep());

        assertThat(responseStep).as("converted response step").isNotNull();
        assertThat(responseStep.getType()).as("type of step response")
                .isEqualTo(ResponseType.STEP);
        assertThat(responseStep.getStatus()).as("status of step response").isEqualTo(GameStatus.IN_GAME);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseStep.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}