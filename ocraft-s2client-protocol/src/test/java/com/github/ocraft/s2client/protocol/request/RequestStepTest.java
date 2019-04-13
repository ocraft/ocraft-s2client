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

import static com.github.ocraft.s2client.protocol.request.RequestStep.nextStep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestStepTest {

    private static final int GAME_LOOP_COUNT = 10;
    private static final int DEFAULT_GAME_LOOP_COUNT = 1;

    @Test
    void serializesToSc2ApiRequestStep() {
        Sc2Api.Request sc2ApiRequest = nextStep().withCount(GAME_LOOP_COUNT).build().toSc2Api();

        assertThat(sc2ApiRequest.hasStep()).as("sc2api request has step").isTrue();
        assertThat(sc2ApiRequest.getStep().getCount()).as("request step: count").isEqualTo(GAME_LOOP_COUNT);
    }

    @Test
    void serializesDefaultValueForCountWhenNotSet() {
        assertThat(requestStepWithDefaultValues().getCount()).as("count default value")
                .isEqualTo(DEFAULT_GAME_LOOP_COUNT);
    }

    private Sc2Api.RequestStep requestStepWithDefaultValues() {
        return nextStep().build().toSc2Api().getStep();
    }

    @Test
    void throwsExceptionWhenCountValueIsLowerThanOne() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> nextStep().withCount(0))
                .withMessage("count must be greater than 0");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestStep.class)
                .withIgnoredFields("nanoTime")
                .withRedefinedSuperclass()
                .verify();
    }

}
