package com.github.ocraft.s2client.protocol.request;

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
        EqualsVerifier.forClass(RequestStep.class).withIgnoredFields("nanoTime").verify();
    }

}