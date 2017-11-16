package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.SCORE;
import static com.github.ocraft.s2client.protocol.Fixtures.debugSetScore;
import static com.github.ocraft.s2client.protocol.debug.DebugSetScore.setScore;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DebugSetScoreTest {
    @Test
    void serializesToSc2ApiDebugSetScore() {
        assertThatAllFieldsAreSerialized(debugSetScore().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugSetScore sc2ApiSetScore) {
        assertThat(sc2ApiSetScore.getScore()).as("sc2api debug set score: score").isEqualTo(SCORE);
    }

    @Test
    void throwsExceptionWhenUnitTagSetIsEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((DebugSetScore.Builder) setScore()).build())
                .withMessage("score is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugSetScore.class).verify();
    }
}