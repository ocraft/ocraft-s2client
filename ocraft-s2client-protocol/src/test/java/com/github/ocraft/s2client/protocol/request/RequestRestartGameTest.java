package com.github.ocraft.s2client.protocol.request;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.request.RequestRestartGame.restartGame;
import static org.assertj.core.api.Assertions.assertThat;

class RequestRestartGameTest {

    @Test
    void serializesToSc2ApiRequestRestartGame() {
        assertThat(restartGame().toSc2Api().hasRestartGame()).as("sc2api request has restart game").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestRestartGame.class).withIgnoredFields("nanoTime").verify();
    }
}