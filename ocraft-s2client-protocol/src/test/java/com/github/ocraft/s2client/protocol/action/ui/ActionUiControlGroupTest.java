package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiControlGroup.Action.*;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiControlGroup.controlGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class ActionUiControlGroupTest {

    @Test
    void throwsExceptionWhenSc2ApiActionUiControlGroupIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiControlGroup.from(nothing()))
                .withMessage("sc2api action ui control group is required");
    }

    @Test
    void convertsSc2ApiActionUiControlGroupToActionUiControlGroup() {
        assertThatAllFieldsAreConverted(ActionUiControlGroup.from(sc2ApiActionUiControlGroup()));
    }

    private void assertThatAllFieldsAreConverted(ActionUiControlGroup controlGroup) {
        assertThat(controlGroup.getAction()).as("action ui control group: action").isEqualTo(APPEND_AND_STEAL);
        assertThat(controlGroup.getIndex()).as("action ui control group: index").isEqualTo(CONTROL_GROUP_INDEX);
    }

    @Test
    void throwsExceptionWhenActionIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiControlGroup.from(without(
                        () -> sc2ApiActionUiControlGroup().toBuilder(),
                        Ui.ActionControlGroup.Builder::clearAction).build()))
                .withMessage("action is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(controlGroup().on(1)).build())
                .withMessage("action is required");
    }

    private ActionUiControlGroup.Builder fullAccessTo(Object obj) {
        return (ActionUiControlGroup.Builder) obj;
    }

    @Test
    void throwsExceptionWhenIndexIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiControlGroup.from(without(
                        () -> sc2ApiActionUiControlGroup().toBuilder(),
                        Ui.ActionControlGroup.Builder::clearControlGroupIndex).build()))
                .withMessage("index is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(controlGroup()).withMode(APPEND_AND_STEAL).build())
                .withMessage("index is required");
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "controlGroupActionMappings")
    void mapsSc2ApiControlGroupAction(
            Ui.ActionControlGroup.ControlGroupAction sc2ApiControlGroupAction,
            ActionUiControlGroup.Action expectedControlGroupAction) {
        assertThat(ActionUiControlGroup.Action.from(sc2ApiControlGroupAction)).isEqualTo(expectedControlGroupAction);
    }

    private static Stream<Arguments> controlGroupActionMappings() {
        return Stream.of(
                of(Ui.ActionControlGroup.ControlGroupAction.Recall, RECALL),
                of(Ui.ActionControlGroup.ControlGroupAction.Set, SET),
                of(Ui.ActionControlGroup.ControlGroupAction.Append, APPEND),
                of(Ui.ActionControlGroup.ControlGroupAction.SetAndSteal, SET_AND_STEAL),
                of(Ui.ActionControlGroup.ControlGroupAction.AppendAndSteal, APPEND_AND_STEAL));
    }

    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "controlGroupActionMappings")
    void serializesToSc2ApiControlGroupAction(
            Ui.ActionControlGroup.ControlGroupAction expectedSc2ApiControlGroupAction,
            ActionUiControlGroup.Action controlGroupAction) {
        assertThat(controlGroupAction.toSc2Api()).isEqualTo(expectedSc2ApiControlGroupAction);
    }

    @Test
    void throwsExceptionWhenControlGroupActionIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiControlGroup.Action.from(nothing()))
                .withMessage("sc2api control group action is required");
    }

    @Test
    void serializesToSc2ApiActionUiControlGroup() {
        assertThatAllFieldsInRequestAreSerialized(
                controlGroup()
                        .on(CONTROL_GROUP_INDEX)
                        .withMode(ActionUiControlGroup.Action.APPEND)
                        .build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Ui.ActionControlGroup sc2ApiUiControlGroup) {
        assertThat(sc2ApiUiControlGroup.getControlGroupIndex()).as("sc2api ui control group: index")
                .isEqualTo(CONTROL_GROUP_INDEX);
        assertThat(sc2ApiUiControlGroup.getAction()).as("sc2api ui control group: action")
                .isEqualTo(Ui.ActionControlGroup.ControlGroupAction.Append);
    }

    @Test
    void throwsExceptionWhenIndexIsNotInValidRange() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> controlGroup().on(-1).withMode(APPEND).build())
                .withMessage("control group index has value -1 and is lower than 0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> controlGroup().on(10).withMode(APPEND).build())
                .withMessage("control group index has value 10 and is greater than 9");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUiControlGroup.class).withNonnullFields("action").verify();
    }

}