package com.github.ocraft.s2client.protocol.request;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.request.RequestPing.ping;
import static org.assertj.core.api.Assertions.assertThat;

class RequestPingTest {

    @Test
    void serializesToSc2ApiRequestPing() {
        assertThat(ping().toSc2Api().hasPing()).as("sc2api request has ping").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestPing.class).withIgnoredFields("nanoTime").verify();
    }

}