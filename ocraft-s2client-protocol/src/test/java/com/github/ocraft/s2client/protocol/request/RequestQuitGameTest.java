package com.github.ocraft.s2client.protocol.request;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RequestQuitGameTest {

    @Test
    void serializesToSc2ApiRequestQuit() {
        Assertions.assertThat(RequestQuitGame.quitGame().toSc2Api().hasQuit()).as("sc2api request has quit").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestQuitGame.class).withIgnoredFields("nanoTime").verify();
    }
}