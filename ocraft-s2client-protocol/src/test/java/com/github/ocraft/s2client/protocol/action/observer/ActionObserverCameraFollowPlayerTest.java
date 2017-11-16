package com.github.ocraft.s2client.protocol.action.observer;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.PLAYER_ID;
import static com.github.ocraft.s2client.protocol.action.Actions.Observer.cameraFollowPlayer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionObserverCameraFollowPlayerTest {
    @Test
    void serializesToSc2ApiActionObserverCameraFollowPlayer() {
        assertThatAllFieldsInRequestAreSerialized(
                cameraFollowPlayer().withId(PLAYER_ID).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Sc2Api.ActionObserverCameraFollowPlayer sc2ApiObserverCameraFollowPlayer) {
        assertThat(sc2ApiObserverCameraFollowPlayer.getPlayerId()).as("sc2api observer camera follow player: player id")
                .isEqualTo(PLAYER_ID);
    }

    @Test
    void throwsExceptionWhenPlayerIdIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionObserverCameraFollowPlayer.Builder) cameraFollowPlayer()).build())
                .withMessage("player id is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionObserverCameraFollowPlayer.class).verify();
    }
}