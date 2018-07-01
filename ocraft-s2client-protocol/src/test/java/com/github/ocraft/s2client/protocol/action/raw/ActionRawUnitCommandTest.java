package com.github.ocraft.s2client.protocol.action.raw;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.raw.ActionRawUnitCommand.unitCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionRawUnitCommandTest {

    @Test
    void throwsExceptionWhenSc2ApiActionRawUnitCommandIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRawUnitCommand.from(nothing()))
                .withMessage("sc2api action raw unit command is required");
    }

    @Test
    void convertsSc2ApiActionRawUnitCommandToActionRawUnitCommand() {
        assertThatAllFieldsAreConverted(ActionRawUnitCommand.from(sc2ApiActionRawUnitCommand()));
    }

    private void assertThatAllFieldsAreConverted(ActionRawUnitCommand unitCommand) {
        assertThat(unitCommand.getAbility()).as("action raw unit command: ability")
                .isEqualTo(Abilities.EFFECT_PSI_STORM);
        assertThat(unitCommand.getTargetedUnitTag()).as("action raw unit command: targeted unit tag")
                .hasValue(Tag.from(UNIT_TAG));
        assertThat(unitCommand.getTargetedWorldSpacePosition())
                .as("action raw unit command: targeted world space position").isEmpty();
        assertThat(unitCommand.getUnitTags()).as("action raw unit command: unit tags")
                .containsOnlyElementsOf(UNIT_TAGS);
        assertThat(unitCommand.isQueued()).as("action raw unit command: queued").isTrue();
    }

    @Test
    void throwsExceptionWhenAbilityIdIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRawUnitCommand.from(sc2ApiActionRawUnitCommandWithoutAbilityId()))
                .withMessage("ability id is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> unitCommandBuilder().build())
                .withMessage("ability id is required");
    }

    private ActionRawUnitCommand.Builder unitCommandBuilder() {
        return (ActionRawUnitCommand.Builder) unitCommand();
    }

    private Raw.ActionRawUnitCommand sc2ApiActionRawUnitCommandWithoutAbilityId() {
        return without(
                () -> sc2ApiActionRawUnitCommand().toBuilder(),
                Raw.ActionRawUnitCommand.Builder::clearAbilityId).build();
    }

    @Test
    void convertsSc2ApiTargetWorldSpacePositionIfProvided() {
        ActionRawUnitCommand unitCommand = ActionRawUnitCommand.from(
                sc2ApiActionRawUnitCommandWithTargetedWorldSpacePosition());

        assertThat(unitCommand.getTargetedWorldSpacePosition())
                .as("action raw unit command: targeted word space position").isNotEmpty();
        assertThat(unitCommand.getTargetedUnitTag())
                .as("action raw unit command: targeted word space position").isEmpty();
    }

    @Test
    void throwsExceptionIfUnitTagSetIsEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionRawUnitCommand.from(without(
                        () -> sc2ApiActionRawUnitCommand().toBuilder(),
                        Raw.ActionRawUnitCommand.Builder::clearUnitTags).build()))
                .withMessage("unit tag list is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> unitCommandBuilder().useAbility(Abilities.ATTACK).build())
                .withMessage("unit tag list is required");
    }

    @Test
    void hasDefaultValueForQueuedFieldIfNotProvided() {
        ActionRawUnitCommand unitCommand = ActionRawUnitCommand.from(without(
                () -> sc2ApiActionRawUnitCommand().toBuilder(),
                Raw.ActionRawUnitCommand.Builder::clearQueueCommand).build());
        assertThat(unitCommand.isQueued()).as("action raw unit command: default queued value").isFalse();
    }

    @Test
    void serializedDefaultValueForQueuedFieldIsNotProvided() {
        assertThat(defaultUnitCommand().build().toSc2Api().getQueueCommand())
                .as("sc2api action raw unit command: default queued value").isFalse();
    }

    private ActionRawUnitCommand.Builder defaultUnitCommand() {
        return (ActionRawUnitCommand.Builder) unitCommand().forUnits(Tag.from(UNIT_TAG)).useAbility(Abilities.ATTACK);
    }

    @Test
    void serializesToSc2ApiActionRawUnitCommand() {
        assertThatAllFieldsInRequestAreSerialized(
                unitCommand()
                        .forUnits(Tag.from(UNIT_TAG))
                        .useAbility(Abilities.EFFECT_PSI_STORM)
                        .target(Tag.from(UNIT_ENGAGED_TARGET_TAG))
                        .queued()
                        .build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Raw.ActionRawUnitCommand sc2ApiUnitCommand) {
        assertThat(sc2ApiUnitCommand.getAbilityId()).as("sc2api raw unit command: ability id")
                .isEqualTo(PSI_STORM_ABILITY_ID);
        assertThat(sc2ApiUnitCommand.getUnitTagsList()).as("sc2api raw unit command: unit tag list")
                .containsExactly(UNIT_TAG);
        assertThat(sc2ApiUnitCommand.getTargetUnitTag()).as("sc2api raw unit command: target unit tag")
                .isEqualTo(UNIT_ENGAGED_TARGET_TAG);
        assertThat(sc2ApiUnitCommand.getQueueCommand()).as("sc2api raw unit command: queued").isTrue();
    }

    @Test
    void serializesOnlyRecentlyAddedTarget() {
        assertCorrectTarget(fullAccessTo(defaultUnitCommand().target(Tag.from(UNIT_TAG)))
                .target(Point2d.of(10, 20)).build());
        assertCorrectTargetAfterOrderChange(fullAccessTo(defaultUnitCommand().target(Point2d.of(10, 20)))
                .target(Tag.from(UNIT_TAG)).build());
    }

    private ActionRawUnitCommand.Builder fullAccessTo(Object obj) {
        return (ActionRawUnitCommand.Builder) obj;
    }

    private void assertCorrectTarget(ActionRawUnitCommand unitCommand) {
        Raw.ActionRawUnitCommand sc2ApiActionRawUnitCommand = unitCommand.toSc2Api();
        assertThat(sc2ApiActionRawUnitCommand.hasTargetUnitTag()).as("case of target is unit").isFalse();
        assertThat(sc2ApiActionRawUnitCommand.hasTargetWorldSpacePos()).as("case of target is world position").isTrue();
    }

    private void assertCorrectTargetAfterOrderChange(ActionRawUnitCommand unitCommand) {
        Raw.ActionRawUnitCommand sc2ApiActionRawUnitCommand = unitCommand.toSc2Api();
        assertThat(sc2ApiActionRawUnitCommand.getTargetUnitTag()).as("case of target is unit").isEqualTo(UNIT_TAG);
        assertThat(sc2ApiActionRawUnitCommand.hasTargetWorldSpacePos()).as("case of target is world position")
                .isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionRawUnitCommand.class).withNonnullFields("ability", "unitTags").verify();
    }

}
