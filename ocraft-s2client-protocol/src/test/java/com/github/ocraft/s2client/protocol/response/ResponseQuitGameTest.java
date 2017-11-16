package com.github.ocraft.s2client.protocol.response;

import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.aSc2ApiResponse;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiResponseWithQuit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseQuitGameTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveQuitGame() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseQuitGame.from(nothing()))
                .withMessage("provided argument doesn't have quit response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseQuitGame.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have quit response");
    }

    @Test
    void convertsSc2ApiResponseQuitGameToResponseQuitGame() {
        ResponseQuitGame responseQuitGame = ResponseQuitGame.from(sc2ApiResponseWithQuit());

        assertThat(responseQuitGame).as("converted response quit game").isNotNull();
        assertThat(responseQuitGame.getType()).as("type of quit game response")
                .isEqualTo(ResponseType.QUIT_GAME);
        assertThat(responseQuitGame.getStatus()).as("status of quit game response").isEqualTo(GameStatus.QUIT);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseQuitGame.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}