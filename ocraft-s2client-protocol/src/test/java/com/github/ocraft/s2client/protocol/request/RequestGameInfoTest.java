package com.github.ocraft.s2client.protocol.request;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.request.RequestGameInfo.gameInfo;
import static org.assertj.core.api.Assertions.assertThat;

class RequestGameInfoTest {
    @Test
    void serializesToSc2ApiRequestGameInfo() {
        assertThat(gameInfo().toSc2Api().hasGameInfo()).as("sc2api request has game info").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestGameInfo.class).withIgnoredFields("nanoTime").verify();
    }
}