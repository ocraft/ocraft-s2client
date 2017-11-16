package com.github.ocraft.s2client.protocol.response;

import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.aSc2ApiResponse;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiResponseWithLeaveGame;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseLeaveGameTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveLeaveGame() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseLeaveGame.from(nothing()))
                .withMessage("provided argument doesn't have leave game response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseLeaveGame.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have leave game response");
    }

    @Test
    void convertsSc2ApiResponseLeaveGameToResponseLeaveGame() {
        ResponseLeaveGame responseLeaveGame = ResponseLeaveGame.from(sc2ApiResponseWithLeaveGame());

        assertThat(responseLeaveGame).as("converted response leave game").isNotNull();
        assertThat(responseLeaveGame.getType()).as("type of leave game response").isEqualTo(ResponseType.LEAVE_GAME);
        assertThat(responseLeaveGame.getStatus()).as("status of leave game response").isEqualTo(GameStatus.LAUNCHED);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseLeaveGame.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}