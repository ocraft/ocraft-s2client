package com.github.ocraft.s2client.protocol.action.observer;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.PLAYER_ID;
import static com.github.ocraft.s2client.protocol.action.Actions.Observer.playerPerspective;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionObserverPlayerPerspectiveTest {

    @Test
    void serializesToSc2ApiActionObserverPlayerPerspective() {
        assertThatAllFieldsInRequestAreSerialized(
                playerPerspective().ofPlayer(PLAYER_ID).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Sc2Api.ActionObserverPlayerPerspective sc2ApiObserverPlayerPerspective) {
        assertThat(sc2ApiObserverPlayerPerspective.getPlayerId()).as("sc2api observer player perspective: player id")
                .isEqualTo(PLAYER_ID);
    }

    @Test
    void throwsExceptionWhenPlayerIdIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionObserverPlayerPerspective.Builder) playerPerspective()).build())
                .withMessage("player id is required");
    }

    @Test
    void serializesZeroAsValueForAllPlayers() {
        assertThat(playerPerspective().ofAll().build().toSc2Api().getPlayerId())
                .as("sc2api observer player perspective: all player").isEqualTo(0);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionObserverPlayerPerspective.class).verify();
    }
}