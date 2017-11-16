package com.github.ocraft.s2client.protocol.response;

import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseSaveReplayTest {
    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveSaveReplay() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseSaveReplay.from(nothing()))
                .withMessage("provided argument doesn't have save replay response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseSaveReplay.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have save replay response");
    }

    @Test
    void convertsSc2ApiResponseSaveReplayToResponseSaveReplay() {
        ResponseSaveReplay responseSaveReplay = ResponseSaveReplay.from(sc2ApiResponseWithSaveReplay());

        assertThatResponseIsInValidState(responseSaveReplay);
        assertThat(responseSaveReplay.getData()).as("response save replay: data").isEqualTo(DATA_IN_BYTES);

    }

    private void assertThatResponseIsInValidState(ResponseSaveReplay responseSaveReplay) {
        assertThat(responseSaveReplay).as("converted response save replay").isNotNull();
        assertThat(responseSaveReplay.getType()).as("type of save replay response")
                .isEqualTo(ResponseType.SAVE_REPLAY);
        assertThat(responseSaveReplay.getStatus()).as("status of save replay response").isEqualTo(GameStatus.ENDED);
    }

    @Test
    void throwsExceptionWhenDataIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseSaveReplay.from(sc2ApiResponseWithSaveReplayWithoutData()))
                .withMessage("data is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseSaveReplay.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status", "data")
                .withRedefinedSuperclass()
                .verify();
    }
}