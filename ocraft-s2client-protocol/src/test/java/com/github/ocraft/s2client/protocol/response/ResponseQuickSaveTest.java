package com.github.ocraft.s2client.protocol.response;

import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.aSc2ApiResponse;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiResponseWithQuickSave;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseQuickSaveTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveQuickSave() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseQuickSave.from(nothing()))
                .withMessage("provided argument doesn't have quick save response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseQuickSave.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have quick save response");
    }

    @Test
    void convertsSc2ApiResponseQuickSaveToResponseQuickSave() {
        ResponseQuickSave responseQuickSave = ResponseQuickSave.from(sc2ApiResponseWithQuickSave());

        assertThat(responseQuickSave).as("converted response quick save").isNotNull();
        assertThat(responseQuickSave.getType()).as("type of quick save response").isEqualTo(ResponseType.QUICK_SAVE);
        assertThat(responseQuickSave.getStatus()).as("status of quick save response").isEqualTo(GameStatus.IN_GAME);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseQuickSave.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}