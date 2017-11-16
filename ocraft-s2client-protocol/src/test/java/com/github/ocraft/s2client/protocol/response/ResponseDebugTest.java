package com.github.ocraft.s2client.protocol.response;

import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.aSc2ApiResponse;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiResponseWithDebug;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseDebugTest {
    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveDebug() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseDebug.from(nothing()))
                .withMessage("provided argument doesn't have debug response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseDebug.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have debug response");
    }

    @Test
    void convertsSc2ApiResponseDebugToResponseDebug() {
        ResponseDebug responseDebug = ResponseDebug.from(sc2ApiResponseWithDebug());

        assertThat(responseDebug).as("converted response debug").isNotNull();
        assertThat(responseDebug.getType()).as("type of debug response")
                .isEqualTo(ResponseType.DEBUG);
        assertThat(responseDebug.getStatus()).as("status of debug response").isEqualTo(GameStatus.IN_GAME);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseDebug.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}