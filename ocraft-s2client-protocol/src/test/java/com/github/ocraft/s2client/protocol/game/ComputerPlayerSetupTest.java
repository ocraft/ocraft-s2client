package com.github.ocraft.s2client.protocol.game;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.game.ComputerPlayerSetup.computer;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ComputerPlayerSetupTest {

    @Test
    void throwsExceptionWhenRaceIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> computer(nothing(), Difficulty.MEDIUM))
                .withMessage("race is required");
    }

    @Test
    void throwsExceptionWhenDifficultyIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> computer(Race.PROTOSS, nothing()))
                .withMessage("difficulty level is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ComputerPlayerSetup.class)
                .withRedefinedSuperclass()
                .withNonnullFields("difficulty", "race", "playerType")
                .verify();
    }

}