package com.github.ocraft.s2client.protocol.request;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.request.RequestObservation.observation;
import static org.assertj.core.api.Assertions.assertThat;

class RequestObservationTest {

    @Test
    void serializesToSc2ApiRequestObservation() {
        Sc2Api.Request sc2ApiRequest = observation().disableFog().gameLoop(2).build().toSc2Api();

        assertThat(sc2ApiRequest.hasObservation()).as("sc2api request has observation").isTrue();
        assertThat(sc2ApiRequest.getObservation().getDisableFog()).as("request observation: disable fog").isTrue();
        assertThat(sc2ApiRequest.getObservation().getGameLoop()).as("request observation: game loop").isEqualTo(2);
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
        EqualsVerifier.forClass(RequestObservation.class)
                .withIgnoredFields("nanoTime")
                .withRedefinedSuperclass()
                .verify();
    }

}
