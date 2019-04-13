package com.github.ocraft.s2client.protocol.action;

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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.action.Action.action;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionTest {

    @Test
    void throwsExceptionWhenSc2ApiActionIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Action.from(nothing()))
                .withMessage("sc2api action is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiAction() {
        assertThatAllFieldsAreConverted(Action.from(sc2ApiAction()));
    }

    private void assertThatAllFieldsAreConverted(Action action) {
        assertThat(action.getRaw()).as("action: raw").isNotEmpty();
        assertThat(action.getFeatureLayer()).as("action: feature layer").isNotEmpty();
        assertThat(action.getRender()).as("action: render").isNotEmpty();
        assertThat(action.getUi()).as("action: ui").isNotEmpty();
        assertThat(action.getChat()).as("action: chat").isNotEmpty();
        assertThat(action.getGameLoop()).as("action: game loop").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenThereIsNoActionCase() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Action.from(aSc2ApiAction().build()))
                .withMessage("one of action case is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((Action.Builder) action()).build())
                .withMessage("one of action case is required");
    }

    @Test
    void hasEmptyChatListIfThereIsNoneInSc2ApiAction() {
        assertThat(Action.from(aSc2ApiAction().setActionRaw(sc2ApiActionRawWithUnitCommand()).build()).getChat())
                .as("converted chat list from empty sc2api chat list")
                .isEmpty();
    }

    @Test
    void serializesToSc2ApiActionUsingRawInterface() {
        assertThat(action().raw(rawUnitCommand().build()).build().toSc2Api().getActionRaw().hasUnitCommand())
                .as("sc2api action: has raw action unit command").isTrue();

        assertThat(action().raw(rawCameraMove().build()).build().toSc2Api().getActionRaw().hasCameraMove())
                .as("sc2api action: has raw action camera move").isTrue();

        assertThat(action().raw(rawToggleAutocast().build()).build().toSc2Api().getActionRaw().hasToggleAutocast())
                .as("sc2api action: has raw action toggle autocast").isTrue();
    }

    @Test
    void serializesToSc2ApiActionUsingRawInterfaceWithBuilder() {
        assertThat(action().raw(rawUnitCommand()).build().toSc2Api().getActionRaw().hasUnitCommand())
                .as("(builder ver.) sc2api action: has raw action unit command").isTrue();

        assertThat(action().raw(rawCameraMove()).build().toSc2Api().getActionRaw().hasCameraMove())
                .as("(builder ver.) sc2api action: has raw action camera move").isTrue();

        assertThat(action().raw(rawToggleAutocast()).build().toSc2Api().getActionRaw().hasToggleAutocast())
                .as("(builder ver.) sc2api action: has raw action toggle autocast").isTrue();
    }

    @Test
    void serializesToSc2ApiActionUsingFeatureLayerInterface() {
        assertThat(action().featureLayer(spatialUnitCommand().build()).build().toSc2Api().hasActionFeatureLayer())
                .as("sc2api action: has feature layer action unit command").isTrue();

        assertThat(
                action().featureLayer(spatialCameraMove().build()).build().toSc2Api().getActionFeatureLayer()
                        .hasCameraMove()
        ).as("sc2api action: has feature layer action camera move").isTrue();

        assertThat(
                action().featureLayer(spatialUnitSelectionPoint().build()).build().toSc2Api().getActionFeatureLayer()
                        .hasUnitSelectionPoint()
        ).as("sc2api action: has feature layer action unit selection point").isTrue();

        assertThat(
                action().featureLayer(spatialUnitSelectionRect().build()).build().toSc2Api().getActionFeatureLayer()
                        .hasUnitSelectionRect()
        ).as("sc2api action: has feature layer action unit selection rect").isTrue();
    }

    @Test
    void serializesToSc2ApiActionUsingFeatureLayerInterfaceWithBuilder() {

        assertThat(action().featureLayer(spatialUnitCommand()).build().toSc2Api().hasActionFeatureLayer())
                .as("sc2api action: has feature layer action unit command").isTrue();

        assertThat(
                action().featureLayer(spatialCameraMove()).build().toSc2Api().getActionFeatureLayer().hasCameraMove()
        ).as("sc2api action: has feature layer action camera move").isTrue();

        assertThat(
                action().featureLayer(spatialUnitSelectionPoint()).build().toSc2Api().getActionFeatureLayer()
                        .hasUnitSelectionPoint()
        ).as("sc2api action: has feature layer action unit selection point").isTrue();

        assertThat(
                action().featureLayer(spatialUnitSelectionRect()).build().toSc2Api().getActionFeatureLayer()
                        .hasUnitSelectionRect()
        ).as("sc2api action: has feature layer action unit selection rect").isTrue();
    }

    @Test
    void serializesToSc2ApiActionUsingRenderInterface() {
        assertThat(action().render(spatialUnitCommand().build()).build().toSc2Api().getActionRender().hasUnitCommand())
                .as("sc2api action: has render action unit command").isTrue();

        assertThat(action().render(spatialCameraMove().build()).build().toSc2Api().getActionRender().hasCameraMove())
                .as("sc2api action: has render action camera move").isTrue();

        assertThat(
                action().render(spatialUnitSelectionPoint().build()).build().toSc2Api().getActionRender()
                        .hasUnitSelectionPoint()
        ).as("sc2api action: has render action").isTrue();

        assertThat(
                action().render(spatialUnitSelectionRect().build()).build().toSc2Api().getActionRender()
                        .hasUnitSelectionRect()
        ).as("sc2api action: has render action selection rect").isTrue();
    }

    @Test
    void serializesToSc2ApiActionUsingRenderInterfaceWithBuilder() {
        assertThat(action().render(spatialUnitCommand()).build().toSc2Api().getActionRender().hasUnitCommand())
                .as("sc2api action: has render action unit command").isTrue();

        assertThat(action().render(spatialCameraMove()).build().toSc2Api().getActionRender().hasCameraMove())
                .as("sc2api action: has render action camera move").isTrue();

        assertThat(
                action().render(spatialUnitSelectionPoint()).build().toSc2Api().getActionRender()
                        .hasUnitSelectionPoint()
        ).as("sc2api action: has render action").isTrue();

        assertThat(
                action().render(spatialUnitSelectionRect()).build().toSc2Api().getActionRender().hasUnitSelectionRect()
        ).as("sc2api action: has render action selection rect").isTrue();
    }

    @Test
    void serializesToSc2ApiActionUsingUiInterface() {
        assertThat(action().ui(uiControlGroup().build()).build().toSc2Api().getActionUi().hasControlGroup())
                .as("sc2api action: has ui action control group").isTrue();

        assertThat(action().ui(uiSelectArmy().build()).build().toSc2Api().getActionUi().hasSelectArmy())
                .as("sc2api action: has ui action select army").isTrue();

        assertThat(action().ui(uiSelectWarpGates().build()).build().toSc2Api().getActionUi().hasSelectWarpGates())
                .as("sc2api action: has ui action select warp gates").isTrue();

        assertThat(action().ui(uiSelectLarva()).build().toSc2Api().getActionUi().hasSelectLarva())
                .as("sc2api action: has ui action select larva").isTrue();

        assertThat(action().ui(uiSelectIdleWorker().build()).build().toSc2Api().getActionUi().hasSelectIdleWorker())
                .as("sc2api action: has ui action select idle worker").isTrue();

        assertThat(action().ui(uiMultiPanel().build()).build().toSc2Api().getActionUi().hasMultiPanel())
                .as("sc2api action: has ui action multi panel").isTrue();

        assertThat(action().ui(uiCargoPanelUnload().build()).build().toSc2Api().getActionUi().hasCargoPanel())
                .as("sc2api action: has ui action cargo panel upload").isTrue();

        assertThat(
                action().ui(uiProductionPanelRemoveFromQueue().build()).build().toSc2Api().getActionUi().hasProductionPanel()
        ).as("sc2api action: has ui action production panel").isTrue();

        assertThat(action().ui(uiToggleAutocast().build()).build().toSc2Api().getActionUi().hasToggleAutocast())
                .as("sc2api action: has ui action toggle autocast").isTrue();
    }

    @Test
    void serializesToSc2ApiActionUsingUiInterfaceWithBuilder() {
        assertThat(action().ui(uiControlGroup()).build().toSc2Api().getActionUi().hasControlGroup())
                .as("sc2api action: has ui action control group").isTrue();

        assertThat(action().ui(uiSelectArmy()).build().toSc2Api().getActionUi().hasSelectArmy())
                .as("sc2api action: has ui action select army").isTrue();

        assertThat(action().ui(uiSelectWarpGates()).build().toSc2Api().getActionUi().hasSelectWarpGates())
                .as("sc2api action: has ui action select warp gates").isTrue();

        assertThat(action().ui(uiSelectIdleWorker()).build().toSc2Api().getActionUi().hasSelectIdleWorker())
                .as("sc2api action: has ui action select idle worker").isTrue();

        assertThat(action().ui(uiMultiPanel()).build().toSc2Api().getActionUi().hasMultiPanel())
                .as("sc2api action: has ui action multi panel").isTrue();

        assertThat(action().ui(uiCargoPanelUnload()).build().toSc2Api().getActionUi().hasCargoPanel())
                .as("sc2api action: has ui action cargo panel upload").isTrue();

        assertThat(
                action().ui(uiProductionPanelRemoveFromQueue()).build().toSc2Api().getActionUi().hasProductionPanel()
        ).as("sc2api action: has ui action production panel").isTrue();

        assertThat(action().ui(uiToggleAutocast()).build().toSc2Api().getActionUi().hasToggleAutocast())
                .as("sc2api action: has ui action toggle autocast").isTrue();
    }

    @Test
    void serializesToSc2ApiActionWithChat() {
        assertThat(action().chat(message()).build().toSc2Api().hasActionChat())
                .as("sc2api action: has chat").isTrue();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Action.class).verify();
    }
}
