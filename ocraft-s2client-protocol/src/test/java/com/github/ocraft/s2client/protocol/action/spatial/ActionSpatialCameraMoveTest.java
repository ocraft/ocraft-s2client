package com.github.ocraft.s2client.protocol.action.spatial;

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiActionSpatialCameraMove;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialCameraMove.cameraMove;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionSpatialCameraMoveTest {

    @Test
    void throwsExceptionWhenSc2ApiActionSpatialCameraMoveIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatialCameraMove.from(nothing()))
                .withMessage("sc2api action spatial camera move is required");
    }

    @Test
    void convertsSc2ApiActionSpatialCameraMoveToActionSpatialCameraMove() {
        assertThatAllFieldsAreConverted(ActionSpatialCameraMove.from(sc2ApiActionSpatialCameraMove()));
    }

    private void assertThatAllFieldsAreConverted(ActionSpatialCameraMove cameraMove) {
        assertThat(cameraMove.getCenterInMinimap()).as("action spatial camera move: center in minimap").isNotNull();
    }

    @Test
    void throwsExceptionWhenCenterInWorldSpaceIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatialCameraMove.from(without(
                        () -> sc2ApiActionSpatialCameraMove().toBuilder(),
                        Spatial.ActionSpatialCameraMove.Builder::clearCenterMinimap).build()))
                .withMessage("center in minimap is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> cameraMove().build())
                .withMessage("center in minimap is required");
    }

    @Test
    void serializesToSc2ApiActionSpatialCameraMove() {
        assertThatAllFieldsInRequestAreSerialized(cameraMove().to(PointI.of(10, 20)).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Spatial.ActionSpatialCameraMove sc2ApiActionSpatialCameraMove) {
        assertThat(sc2ApiActionSpatialCameraMove.hasCenterMinimap()).as("sc2api spatial camera move: center minimap")
                .isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionSpatialCameraMove.class).withNonnullFields("centerInMinimap").verify();
    }

}