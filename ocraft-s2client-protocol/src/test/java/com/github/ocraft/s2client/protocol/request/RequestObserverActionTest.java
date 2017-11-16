package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.action.Actions.Observer.playerPerspective;
import static com.github.ocraft.s2client.protocol.action.ObserverAction.observerAction;
import static com.github.ocraft.s2client.protocol.request.RequestObserverAction.observerActions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestObserverActionTest {

    @Test
    void serializesToSc2ApiRequestObserverAction() {
        assertThatAllFieldsInRequestAreSerialized(
                observerActions().with(observerAction().of(playerPerspective().ofAll())).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Sc2Api.Request sc2ApiRequest) {
        assertThat(sc2ApiRequest.hasObsAction()).as("sc2api request has observer action").isTrue();

        Sc2Api.RequestObserverAction sc2ApiRequestObserverAction = sc2ApiRequest.getObsAction();
        assertThat(sc2ApiRequestObserverAction.getActionsList())
                .as("sc2api request observer action: action list").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenObserverActionListIsEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((RequestObserverAction.Builder) observerActions()).build())
                .withMessage("action list is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(RequestObserverAction.class)
                .withNonnullFields("actions")
                .withIgnoredFields("nanoTime")
                .verify();
    }
}