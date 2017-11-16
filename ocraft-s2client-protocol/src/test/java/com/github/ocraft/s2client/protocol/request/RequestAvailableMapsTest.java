package com.github.ocraft.s2client.protocol.request;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.request.RequestAvailableMaps.availableMaps;
import static org.assertj.core.api.Assertions.assertThat;

class RequestAvailableMapsTest {

    @Test
    void serializesToSc2ApiRequestAvailableMaps() {
        assertThat(availableMaps().toSc2Api().hasAvailableMaps())
                .as("sc2api request has available maps")
                .isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestAvailableMaps.class).withIgnoredFields("nanoTime").verify();
    }
}