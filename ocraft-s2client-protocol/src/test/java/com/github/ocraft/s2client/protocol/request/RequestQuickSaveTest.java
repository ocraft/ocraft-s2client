package com.github.ocraft.s2client.protocol.request;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestQuickSaveTest {

    @Test
    void serializesToSc2ApiRequestQuickSave() {
        assertThat(RequestQuickSave.quickSave().toSc2Api().hasQuickSave()).as("sc2api request has quick save").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestQuickSave.class).withIgnoredFields("nanoTime").verify();
    }

}