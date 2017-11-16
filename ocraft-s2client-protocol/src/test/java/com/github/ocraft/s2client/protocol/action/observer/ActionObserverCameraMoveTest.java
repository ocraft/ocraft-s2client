package com.github.ocraft.s2client.protocol.action.observer;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.CAMERA_DISTANCE;
import static com.github.ocraft.s2client.protocol.Fixtures.observerCameraMove;
import static com.github.ocraft.s2client.protocol.action.Actions.Observer.cameraMove;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionObserverCameraMoveTest {
    @Test
    void serializesToSc2ApiActionObserverCameraMove() {
        assertThatAllFieldsInRequestAreSerialized(observerCameraMove().build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Sc2Api.ActionObserverCameraMove sc2ApiObserverCameraMove) {
        assertThat(sc2ApiObserverCameraMove.hasWorldPos()).as("sc2api observer camera move: has world pos")
                .isTrue();
        assertThat(sc2ApiObserverCameraMove.getDistance()).as("sc2api observer camera move: distance")
                .isEqualTo(CAMERA_DISTANCE);
    }

    @Test
    void throwsExceptionWhenPositionIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionObserverCameraMove.Builder) cameraMove()).build())
                .withMessage("position is required");
    }

    @Test
    void serializesDefaultDistanceIfNotSet() {
        assertThat(cameraMove().to(Point2d.of(1, 1)).build().toSc2Api().getDistance())
                .as("sc2api observer camera move: default distance").isEqualTo(0);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionObserverCameraMove.class).withNonnullFields("position").verify();
    }
}