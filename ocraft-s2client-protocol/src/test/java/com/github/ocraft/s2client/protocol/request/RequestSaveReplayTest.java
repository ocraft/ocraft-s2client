package com.github.ocraft.s2client.protocol.request;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestSaveReplayTest {
    @Test
    void serializesToSc2ApiRequestSaveReplay() {
        assertThat(RequestSaveReplay.saveReplay().toSc2Api().hasSaveReplay())
                .as("sc2api request has save replay").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestSaveReplay.class).withIgnoredFields("nanoTime").verify();
    }
}