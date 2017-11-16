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