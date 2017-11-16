package com.github.ocraft.s2client.protocol.action.raw;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.unit.Unit;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.raw.ActionRawCameraMove.cameraMove;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionRawCameraMoveTest {

    @Test
    void throwsExceptionWhenSc2ApiActionRawCameraMoveIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRawCameraMove.from(nothing()))
                .withMessage("sc2api action raw camera move is required");
    }

    @Test
    void convertsSc2ApiActionRawCameraMoveToActionRawCameraMove() {
        assertThatAllFieldsAreConverted(ActionRawCameraMove.from(sc2ApiActionRawCameraMove()));
    }

    private void assertThatAllFieldsAreConverted(ActionRawCameraMove cameraMove) {
        assertThat(cameraMove.getCenterInWorldSpace()).as("action raw camera move: center in world space").isNotNull();
    }

    @Test
    void throwsExceptionWhenCenterInWorldSpaceIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRawCameraMove.from(without(
                        () -> sc2ApiActionRawCameraMove().toBuilder(),
                        Raw.ActionRawCameraMove.Builder::clearCenterWorldSpace).build()))
                .withMessage("center in world space is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionRawCameraMove.Builder) cameraMove()).build())
                .withMessage("center in world space is required");
    }

    @Test
    void serializesToSc2ApiActionRawCameraMove() {
        assertThatAllFieldsInRequestAreSerialized(cameraMove().to(Point.of(10, 20, 30)).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Raw.ActionRawCameraMove sc2ApiActionRawCameraMove) {
        assertThat(sc2ApiActionRawCameraMove.getCenterWorldSpace()).as("sc2api raw camera move: center in world space")
                .isNotNull();
    }

    @Test
    void serializesToSc2ApiActionRawCameraMoveUsingUnitPosition() {
        assertThatAllFieldsInRequestAreSerialized(cameraMove().to(Unit.from(sc2ApiUnit())).build().toSc2Api());
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionRawCameraMove.class).withNonnullFields("centerInWorldSpace").verify();
    }
}