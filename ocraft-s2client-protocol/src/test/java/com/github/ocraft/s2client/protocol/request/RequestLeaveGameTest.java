package com.github.ocraft.s2client.protocol.request;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestLeaveGameTest {

    @Test
    void serializesToSc2ApiRequestLeaveGame() {
        assertThat(RequestLeaveGame.leaveGame().toSc2Api().hasLeaveGame()).as("sc2api request has leave game").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestLeaveGame.class).withIgnoredFields("nanoTime").verify();
    }

}