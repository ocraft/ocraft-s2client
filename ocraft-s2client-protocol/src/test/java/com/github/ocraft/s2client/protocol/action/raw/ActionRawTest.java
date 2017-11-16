package com.github.ocraft.s2client.protocol.action.raw;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.raw.ActionRawCameraMove.cameraMove;
import static com.github.ocraft.s2client.protocol.action.raw.ActionRawToggleAutocast.toggleAutocast;
import static com.github.ocraft.s2client.protocol.action.raw.ActionRawUnitCommand.unitCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionRawTest {

    @Test
    void throwsExceptionWhenSc2ApiActionRawIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRaw.from(nothing()))
                .withMessage("sc2api action raw is required");
    }

    @Test
    void convertsSc2ApiActionRawUnitCommand() {
        ActionRaw actionRaw = ActionRaw.from(sc2ApiActionRawWithUnitCommand());
        assertThat(actionRaw.getUnitCommand()).as("action raw: unit command").isNotEmpty();
        assertThat(actionRaw.getCameraMove()).as("action raw: camera move").isEmpty();
        assertThat(actionRaw.getToggleAutocast()).as("action raw: toggle autocast").isEmpty();
    }

    @Test
    void convertsSc2ApiActionRawCameraMove() {
        ActionRaw actionRaw = ActionRaw.from(sc2ApiActionRawWithCameraMove());
        assertThat(actionRaw.getUnitCommand()).as("action raw: unit command").isEmpty();
        assertThat(actionRaw.getCameraMove()).as("action raw: camera move").isNotEmpty();
        assertThat(actionRaw.getToggleAutocast()).as("action raw: toggle autocast").isEmpty();
    }

    @Test
    void convertsSc2ApiActionRawToggleAutocast() {
        ActionRaw actionRaw = ActionRaw.from(sc2ApiActionRawWithToggleAutocast());
        assertThat(actionRaw.getUnitCommand()).as("action raw: unit command").isEmpty();
        assertThat(actionRaw.getCameraMove()).as("action raw: camera move").isEmpty();
        assertThat(actionRaw.getToggleAutocast()).as("action raw: toggle autocast").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenThereIsNoActionCase() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRaw.from(aSc2ApiActionRaw().build()))
                .withMessage("one of action case is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRaw.of((ActionRawUnitCommand) nothing()))
                .withMessage("unit command is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRaw.of((ActionRawCameraMove) nothing()))
                .withMessage("camera move is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRaw.of((ActionRawToggleAutocast) nothing()))
                .withMessage("toggle autocast is required");
    }

    @Test
    void serializesToSc2ApiActionRawWithToggleAutocast() {
        Raw.ActionRaw sc2ApiActionRaw = ActionRaw.of(
                toggleAutocast().ofAbility(Abilities.ATTACK).forUnits(Tag.of(UNIT_TAG)).build()).toSc2Api();
        assertThat(sc2ApiActionRaw.hasToggleAutocast()).as("sc2api action raw: case of action is toggle autocast")
                .isTrue();
        assertThat(sc2ApiActionRaw.hasCameraMove()).as("sc2api action raw: case of action is camera move").isFalse();
        assertThat(sc2ApiActionRaw.hasUnitCommand()).as("sc2api action raw: case of action is unit command").isFalse();
    }

    @Test
    void serializesToSc2ApiActionRawWithUnitCommand() {
        Raw.ActionRaw sc2ApiActionRaw = ActionRaw.of(
                unitCommand().forUnits(Tag.of(UNIT_TAG)).useAbility(Abilities.ATTACK).build()).toSc2Api();
        assertThat(sc2ApiActionRaw.hasToggleAutocast()).as("sc2api action raw: case of action is toggle autocast")
                .isFalse();
        assertThat(sc2ApiActionRaw.hasCameraMove()).as("sc2api action raw: case of action is camera move").isFalse();
        assertThat(sc2ApiActionRaw.hasUnitCommand()).as("sc2api action raw: case of action is unit command").isTrue();
    }

    @Test
    void serializesToSc2ApiActionRawWithCameraMove() {
        Raw.ActionRaw sc2ApiActionRaw = ActionRaw.of(cameraMove().to(Point.of(1, 1, 1)).build()).toSc2Api();
        assertThat(sc2ApiActionRaw.hasToggleAutocast()).as("sc2api action raw: case of action is toggle autocast")
                .isFalse();
        assertThat(sc2ApiActionRaw.hasCameraMove()).as("sc2api action raw: case of action is camera move").isTrue();
        assertThat(sc2ApiActionRaw.hasUnitCommand()).as("sc2api action raw: case of action is unit command").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionRaw.class).verify();
    }


}