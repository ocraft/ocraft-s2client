/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
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
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiMultiPanel.Type.*;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiMultiPanel.multiPanel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class ActionUiMultiPanelTest {
    @Test
    void throwsExceptionWhenSc2ApiActionUiMultiPanelIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiMultiPanel.from(nothing()))
                .withMessage("sc2api action ui multi panel is required");
    }

    @Test
    void convertsSc2ApiActionUiMultiPanelToActionUiMultiPanel() {
        assertThatAllFieldsAreConverted(ActionUiMultiPanel.from(sc2ApiActionUiMultiPanel()));
    }

    private void assertThatAllFieldsAreConverted(ActionUiMultiPanel multiPanel) {
        assertThat(multiPanel.getType()).as("action ui multi panel: type").isEqualTo(SELECT_ALL_OF_TYPE);
        assertThat(multiPanel.getUnitIndex()).as("action ui multi panel: unit index").isEqualTo(UNIT_INDEX);
    }

    @Test
    void throwsExceptionWhenTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiMultiPanel.from(without(
                        () -> sc2ApiActionUiMultiPanel().toBuilder(),
                        Ui.ActionMultiPanel.Builder::clearType).build()))
                .withMessage("type is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(multiPanel().select(UNIT_INDEX)).build())
                .withMessage("type is required");
    }

    private ActionUiMultiPanel.Builder fullAccessTo(Object obj) {
        return (ActionUiMultiPanel.Builder) obj;
    }

    @Test
    void throwsExceptionWhenUnitIndexIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiMultiPanel.from(without(
                        () -> sc2ApiActionUiMultiPanel().toBuilder(),
                        Ui.ActionMultiPanel.Builder::clearUnitIndex).build()))
                .withMessage("unit index is required");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(multiPanel()).withMode(SELECT_ALL_OF_TYPE).build())
                .withMessage("unit index is required");
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "multiPanelTypeMappings")
    void mapsSc2ApiMultiPanelAction(
            Ui.ActionMultiPanel.Type sc2ApiMultiPanelType, ActionUiMultiPanel.Type expectedMultiPanelType) {
        assertThat(ActionUiMultiPanel.Type.from(sc2ApiMultiPanelType)).isEqualTo(expectedMultiPanelType);
    }

    private static Stream<Arguments> multiPanelTypeMappings() {
        return Stream.of(
                of(Ui.ActionMultiPanel.Type.SingleSelect, SINGLE_SELECT),
                of(Ui.ActionMultiPanel.Type.DeselectUnit, DESELECT_UNIT),
                of(Ui.ActionMultiPanel.Type.SelectAllOfType, SELECT_ALL_OF_TYPE),
                of(Ui.ActionMultiPanel.Type.DeselectAllOfType, DESELECT_ALL_OF_TYPE));
    }


    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "multiPanelTypeMappings")
    void serializesToSc2ApiMultiPanelAction(
            Ui.ActionMultiPanel.Type expectedSc2ApiMultiPanelType, ActionUiMultiPanel.Type multiPanelType) {
        assertThat(multiPanelType.toSc2Api()).isEqualTo(expectedSc2ApiMultiPanelType);
    }

    @Test
    void throwsExceptionWhenMultiPanelTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiMultiPanel.Type.from(nothing()))
                .withMessage("sc2api multi panel type is required");
    }

    @Test
    void serializesToSc2ApiActionUiMultiPanel() {
        assertThatAllFieldsInRequestAreSerialized(
                multiPanel().select(UNIT_INDEX).withMode(SELECT_ALL_OF_TYPE).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Ui.ActionMultiPanel sc2ApiUiMultiPanel) {
        assertThat(sc2ApiUiMultiPanel.getUnitIndex()).as("sc2api ui multi panel: unit index").isEqualTo(UNIT_INDEX);
        assertThat(sc2ApiUiMultiPanel.getType()).as("sc2api ui multi panel: type")
                .isEqualTo(Ui.ActionMultiPanel.Type.SelectAllOfType);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUiMultiPanel.class).withNonnullFields("type").verify();
    }
}