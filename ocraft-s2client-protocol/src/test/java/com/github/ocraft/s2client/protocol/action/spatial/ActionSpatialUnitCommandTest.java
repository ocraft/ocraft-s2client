package com.github.ocraft.s2client.protocol.action.spatial;

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitCommand.unitCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionSpatialUnitCommandTest {

    @Test
    void throwsExceptionWhenSc2ApiActionSpatialUnitCommandIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatialUnitCommand.from(nothing()))
                .withMessage("sc2api action spatial unit command is required");
    }

    @Test
    void convertsSc2ApiActionSpatialUnitCommandToActionSpatialUnitCommand() {
        assertThatAllFieldsAreConverted(ActionSpatialUnitCommand.from(sc2ApiActionSpatialUnitCommand()));
    }

    private void assertThatAllFieldsAreConverted(ActionSpatialUnitCommand unitCommand) {
        assertThat(unitCommand.getAbility()).as("action spatial unit command: ability")
                .isEqualTo(Abilities.EFFECT_PSI_STORM);
        assertThat(unitCommand.getTargetInScreenCoord()).as("action spatial unit command: target in screen coord")
                .isNotEmpty();
        assertThat(unitCommand.getTargetInMinimapCoord()).as("action spatial unit command: target in minimap coord")
                .isEmpty();
        assertThat(unitCommand.isQueued()).as("action spatial unit command: queued").isTrue();
    }

    @Test
    void throwsExceptionWhenAbilityIdIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionSpatialUnitCommand.from(sc2ApiActionSpatialUnitCommandWithoutAbilityId()))
                .withMessage("ability is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(unitCommand()).build())
                .withMessage("ability is required");
    }

    private ActionSpatialUnitCommand.Builder fullAccessTo(Object obj) {
        return (ActionSpatialUnitCommand.Builder) obj;
    }

    private Spatial.ActionSpatialUnitCommand sc2ApiActionSpatialUnitCommandWithoutAbilityId() {
        return without(
                () -> sc2ApiActionSpatialUnitCommand().toBuilder(),
                Spatial.ActionSpatialUnitCommand.Builder::clearAbilityId).build();
    }

    @Test
    void convertsSc2ApiTargetInMinimapCoordIfProvided() {
        ActionSpatialUnitCommand unitCommand = ActionSpatialUnitCommand.from(
                sc2ApiActionSpatialUnitCommandWithTargetInMinimapCoord());

        assertThat(unitCommand.getTargetInScreenCoord()).as("action spatial unit command: target in screen coord")
                .isEmpty();
        assertThat(unitCommand.getTargetInMinimapCoord()).as("action spatial unit command: target in minimap coord")
                .isNotEmpty();
    }

    @Test
    void hasDefaultValueForQueuedFieldIfNotProvided() {
        ActionSpatialUnitCommand unitCommand = ActionSpatialUnitCommand.from(without(
                () -> sc2ApiActionSpatialUnitCommand().toBuilder(),
                Spatial.ActionSpatialUnitCommand.Builder::clearQueueCommand).build());
        assertThat(unitCommand.isQueued()).as("action spatial unit command: default queued value").isFalse();
    }

    @Test
    void serializedDefaultValueForQueuedFieldIsNotProvided() {
        assertThat(defaultUnitCommand().build().toSc2Api().getQueueCommand())
                .as("sc2api action spatial unit command: default queued value").isFalse();
    }

    private ActionSpatialUnitCommand.Builder defaultUnitCommand() {
        return fullAccessTo(unitCommand().useAbility(Abilities.ATTACK));
    }

    @Test
    void serializesToSc2ApiActionSpatialUnitCommand() {
        assertThatAllFieldsInRequestAreSerialized(
                unitCommand()
                        .useAbility(Abilities.EFFECT_PSI_STORM)
                        .onScreen(PointI.of(10, 15))
                        .queued()
                        .build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Spatial.ActionSpatialUnitCommand sc2ApiSpatialUnitCommand) {
        assertThat(sc2ApiSpatialUnitCommand.getAbilityId()).as("sc2api spatial unit command: ability id")
                .isEqualTo(PSI_STORM_ABILITY_ID);
        assertThat(sc2ApiSpatialUnitCommand.getTargetScreenCoord())
                .as("sc2api spatial unit command: target screen coord").isNotNull();
        assertThat(sc2ApiSpatialUnitCommand.hasTargetMinimapCoord())
                .as("sc2api spatial unit command: has target minimap coord").isFalse();
        assertThat(sc2ApiSpatialUnitCommand.getQueueCommand()).as("sc2api spatial unit command: queued").isTrue();
    }

    @Test
    void serializesOnlyRecentlyAddedTarget() {
        assertCorrectTarget(
                fullAccessTo(defaultUnitCommand().onMinimap(PointI.of(10, 20))).onScreen(PointI.of(5, 5)).build());
        assertCorrectTargetAfterOrderChange(
                fullAccessTo(defaultUnitCommand().onScreen(PointI.of(5, 5))).onMinimap(PointI.of(10, 20)).build());
    }

    private void assertCorrectTarget(ActionSpatialUnitCommand unitCommand) {
        Spatial.ActionSpatialUnitCommand sc2ApiActionSpatialUnitCommand = unitCommand.toSc2Api();
        assertThat(sc2ApiActionSpatialUnitCommand.hasTargetMinimapCoord()).as("case of target is on screen").isFalse();
        assertThat(sc2ApiActionSpatialUnitCommand.hasTargetScreenCoord()).as("case of target is on minimap").isTrue();
    }

    private void assertCorrectTargetAfterOrderChange(ActionSpatialUnitCommand unitCommand) {
        Spatial.ActionSpatialUnitCommand sc2ApiActionSpatialUnitCommand = unitCommand.toSc2Api();
        assertThat(sc2ApiActionSpatialUnitCommand.hasTargetMinimapCoord()).as("case of target is on screen").isTrue();
        assertThat(sc2ApiActionSpatialUnitCommand.hasTargetScreenCoord()).as("case of target is on minimap").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionSpatialUnitCommand.class).withNonnullFields("ability").verify();
    }


}