package com.github.ocraft.s2client.protocol.response;

import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.aSc2ApiResponse;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiResponseWithQuickLoad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseQuickLoadTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveQuickLoad() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseQuickLoad.from(nothing()))
                .withMessage("provided argument doesn't have quick load response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseQuickLoad.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have quick load response");
    }

    @Test
    void convertsSc2ApiResponseQuickLoadToResponseQuickLoad() {
        ResponseQuickLoad responseQuickLoad = ResponseQuickLoad.from(sc2ApiResponseWithQuickLoad());

        assertThat(responseQuickLoad).as("converted response quick load").isNotNull();
        assertThat(responseQuickLoad.getType()).as("type of quick load response").isEqualTo(ResponseType.QUICK_LOAD);
        assertThat(responseQuickLoad.getStatus()).as("status of quick load response").isEqualTo(GameStatus.IN_GAME);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseQuickLoad.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}