package com.github.ocraft.s2client.protocol.action.observer;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.UNIT_TAG;
import static com.github.ocraft.s2client.protocol.action.Actions.Observer.cameraFollowUnits;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionObserverCameraFollowUnitsTest {
    @Test
    void serializesToSc2ApiActionObserverCameraFollowUnits() {
        assertThatAllFieldsInRequestAreSerialized(
                cameraFollowUnits().withTags(Tag.from(UNIT_TAG)).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Sc2Api.ActionObserverCameraFollowUnits sc2ApiObserverCameraFollowUnits) {
        assertThat(sc2ApiObserverCameraFollowUnits.getUnitTagsList())
                .as("sc2api observer camera follow units: unit tags").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenUnitsAreNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionObserverCameraFollowUnits.Builder) cameraFollowUnits()).build())
                .withMessage("units is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionObserverCameraFollowUnits.class).withNonnullFields("units").verify();
    }
}