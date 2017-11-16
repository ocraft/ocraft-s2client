package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.request.RequestObservation.observation;
import static org.assertj.core.api.Assertions.assertThat;

class RequestObservationTest {

    @Test
    void serializesToSc2ApiRequestObservation() {
        Sc2Api.Request sc2ApiRequest = observation().disableFog().build().toSc2Api();

        assertThat(sc2ApiRequest.hasObservation()).as("sc2api request has observation").isTrue();
        assertThat(sc2ApiRequest.getObservation().getDisableFog()).as("request observation: disable fog").isTrue();
    }

    @Test
    void serializesDefaultValueForDisableFogWhenNotSet() {
        assertThat(requestObservationWithDefaultValues().getDisableFog()).as("disable fog default value").isFalse();
    }

    private Sc2Api.RequestObservation requestObservationWithDefaultValues() {
        return observation().build().toSc2Api().getObservation();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestObservation.class).withIgnoredFields("nanoTime").verify();
    }

}