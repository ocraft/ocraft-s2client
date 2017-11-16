package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.rawUnitCommand;
import static com.github.ocraft.s2client.protocol.action.Action.action;
import static com.github.ocraft.s2client.protocol.request.RequestAction.actions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestActionTest {

    @Test
    void serializesToSc2ApiRequestAction() {
        assertThatAllFieldsInRequestAreSerialized(
                actions().of(action().raw(rawUnitCommand().build()).build()).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Sc2Api.Request sc2ApiRequest) {
        assertThat(sc2ApiRequest.hasAction()).as("sc2api request has action").isTrue();

        Sc2Api.RequestAction sc2ApiRequestAction = sc2ApiRequest.getAction();
        assertThat(sc2ApiRequestAction.getActionsList()).as("sc2api request action: action list").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenActionListIsEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((RequestAction.Builder) actions()).build())
                .withMessage("action list is required");
    }

    @Test
    void serializesToSc2ApiRequestActionUsingBuilder() {
        assertThat(actions().of(action().raw(rawUnitCommand())).build().toSc2Api().hasAction())
                .as("(builder ver.) sc2api request has action").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestAction.class).withNonnullFields("actions").withIgnoredFields("nanoTime").verify();
    }
}