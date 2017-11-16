package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.debug.DebugGameState;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.debug.DebugCommand.command;
import static com.github.ocraft.s2client.protocol.request.RequestDebug.debug;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestDebugTest {
    @Test
    void serializesToSc2ApiRequestDebug() {
        assertThatAllFieldsInRequestAreSerialized(
                debug().with(command().of(DebugGameState.SHOW_MAP)).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Sc2Api.Request sc2ApiRequest) {
        assertThat(sc2ApiRequest.hasDebug()).as("sc2api request has debug").isTrue();

        Sc2Api.RequestDebug sc2ApiRequestDebug = sc2ApiRequest.getDebug();
        assertThat(sc2ApiRequestDebug.getDebugList()).as("sc2api debug: command list").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenMapIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((RequestDebug.Builder) debug()).build())
                .withMessage("command list is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestDebug.class)
                .withIgnoredFields("nanoTime").withNonnullFields("commands").verify();
    }
}