package com.github.ocraft.s2client.protocol.request;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestQuickLoadTest {

    @Test
    void serializesToSc2ApiRequestQuickLoad() {
        assertThat(RequestQuickLoad.quickLoad().toSc2Api().hasQuickLoad()).as("sc2api request has quick load").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestQuickLoad.class).withIgnoredFields("nanoTime").verify();
    }

}