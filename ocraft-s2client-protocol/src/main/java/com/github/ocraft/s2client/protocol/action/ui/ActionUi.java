package com.github.ocraft.s2client.protocol.action.ui;

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

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionUi implements Sc2ApiSerializable<Ui.ActionUI> {

    private static final long serialVersionUID = -5086461085176516116L;

    private final ActionUiControlGroup controlGroup;
    private final ActionUiSelectArmy selectArmy;
    private final ActionUiSelectWarpGates selectWarpGates;
    private final ActionUiSelectLarva selectLarva;
    private final ActionUiSelectIdleWorker selectIdleWorker;
    private final ActionUiMultiPanel multiPanel;
    private final ActionUiCargoPanelUnload cargoPanelUnload;
    private final ActionUiProductionPanelRemoveFromQueue productionPanelRemoveFromQueue;
    private final ActionUiToggleAutocast toggleAutocast;

    private ActionUi(Ui.ActionUI sc2ApiActionUi) {
        controlGroup = tryGet(
                Ui.ActionUI::getControlGroup, Ui.ActionUI::hasControlGroup
        ).apply(sc2ApiActionUi).map(ActionUiControlGroup::from).orElse(nothing());

        selectArmy = tryGet(
                Ui.ActionUI::getSelectArmy, Ui.ActionUI::hasSelectArmy
        ).apply(sc2ApiActionUi).map(ActionUiSelectArmy::from).orElse(nothing());

        selectWarpGates = tryGet(
                Ui.ActionUI::getSelectWarpGates, Ui.ActionUI::hasSelectWarpGates
        ).apply(sc2ApiActionUi).map(ActionUiSelectWarpGates::from).orElse(nothing());

        selectLarva = tryGet(
                Ui.ActionUI::getSelectLarva, Ui.ActionUI::hasSelectLarva
        ).apply(sc2ApiActionUi).map(ActionUiSelectLarva::from).orElse(nothing());

        selectIdleWorker = tryGet(
                Ui.ActionUI::getSelectIdleWorker, Ui.ActionUI::hasSelectIdleWorker
        ).apply(sc2ApiActionUi).map(ActionUiSelectIdleWorker::from).orElse(nothing());

        multiPanel = tryGet(
                Ui.ActionUI::getMultiPanel, Ui.ActionUI::hasMultiPanel
        ).apply(sc2ApiActionUi).map(ActionUiMultiPanel::from).orElse(nothing());

        cargoPanelUnload = tryGet(
                Ui.ActionUI::getCargoPanel, Ui.ActionUI::hasCargoPanel
        ).apply(sc2ApiActionUi).map(ActionUiCargoPanelUnload::from).orElse(nothing());

        productionPanelRemoveFromQueue = tryGet(
                Ui.ActionUI::getProductionPanel, Ui.ActionUI::hasProductionPanel
        ).apply(sc2ApiActionUi).map(ActionUiProductionPanelRemoveFromQueue::from).orElse(nothing());

        toggleAutocast = tryGet(
                Ui.ActionUI::getToggleAutocast, Ui.ActionUI::hasToggleAutocast
        ).apply(sc2ApiActionUi).map(ActionUiToggleAutocast::from).orElse(nothing());

        if (!oneOfActionCaseIsSet()) {
            throw new IllegalArgumentException("one of action case is required");
        }
    }

    private ActionUi(ActionUiControlGroup controlGroup) {
        require("control group", controlGroup);
        this.controlGroup = controlGroup;
        this.selectArmy = nothing();
        this.selectWarpGates = nothing();
        this.selectLarva = nothing();
        this.selectIdleWorker = nothing();
        this.multiPanel = nothing();
        this.cargoPanelUnload = nothing();
        this.productionPanelRemoveFromQueue = nothing();
        this.toggleAutocast = nothing();
    }

    private ActionUi(ActionUiSelectArmy selectArmy) {
        require("select army", selectArmy);
        this.controlGroup = nothing();
        this.selectArmy = selectArmy;
        this.selectWarpGates = nothing();
        this.selectLarva = nothing();
        this.selectIdleWorker = nothing();
        this.multiPanel = nothing();
        this.cargoPanelUnload = nothing();
        this.productionPanelRemoveFromQueue = nothing();
        this.toggleAutocast = nothing();
    }

    private ActionUi(ActionUiSelectWarpGates selectWarpGates) {
        require("select warp gates", selectWarpGates);
        this.controlGroup = nothing();
        this.selectArmy = nothing();
        this.selectWarpGates = selectWarpGates;
        this.selectLarva = nothing();
        this.selectIdleWorker = nothing();
        this.multiPanel = nothing();
        this.cargoPanelUnload = nothing();
        this.productionPanelRemoveFromQueue = nothing();
        this.toggleAutocast = nothing();
    }

    private ActionUi(ActionUiSelectLarva selectLarva) {
        require("select larva", selectLarva);
        this.controlGroup = nothing();
        this.selectArmy = nothing();
        this.selectWarpGates = nothing();
        this.selectLarva = selectLarva;
        this.selectIdleWorker = nothing();
        this.multiPanel = nothing();
        this.cargoPanelUnload = nothing();
        this.productionPanelRemoveFromQueue = nothing();
        this.toggleAutocast = nothing();
    }

    private ActionUi(ActionUiSelectIdleWorker selectIdleWorker) {
        require("select idle worker", selectIdleWorker);
        this.controlGroup = nothing();
        this.selectArmy = nothing();
        this.selectWarpGates = nothing();
        this.selectLarva = nothing();
        this.selectIdleWorker = selectIdleWorker;
        this.multiPanel = nothing();
        this.cargoPanelUnload = nothing();
        this.productionPanelRemoveFromQueue = nothing();
        this.toggleAutocast = nothing();
    }

    private ActionUi(ActionUiMultiPanel multiPanel) {
        require("multi panel", multiPanel);
        this.controlGroup = nothing();
        this.selectArmy = nothing();
        this.selectWarpGates = nothing();
        this.selectLarva = nothing();
        this.selectIdleWorker = nothing();
        this.multiPanel = multiPanel;
        this.cargoPanelUnload = nothing();
        this.productionPanelRemoveFromQueue = nothing();
        this.toggleAutocast = nothing();
    }

    private ActionUi(ActionUiCargoPanelUnload cargoPanelUnload) {
        require("cargo panel unload", cargoPanelUnload);
        this.controlGroup = nothing();
        this.selectArmy = nothing();
        this.selectWarpGates = nothing();
        this.selectLarva = nothing();
        this.selectIdleWorker = nothing();
        this.multiPanel = nothing();
        this.cargoPanelUnload = cargoPanelUnload;
        this.productionPanelRemoveFromQueue = nothing();
        this.toggleAutocast = nothing();
    }

    private ActionUi(ActionUiProductionPanelRemoveFromQueue productionPanelRemoveFromQueue) {
        require("production panel remove from queue", productionPanelRemoveFromQueue);
        this.controlGroup = nothing();
        this.selectArmy = nothing();
        this.selectWarpGates = nothing();
        this.selectLarva = nothing();
        this.selectIdleWorker = nothing();
        this.multiPanel = nothing();
        this.cargoPanelUnload = nothing();
        this.productionPanelRemoveFromQueue = productionPanelRemoveFromQueue;
        this.toggleAutocast = nothing();
    }

    private ActionUi(ActionUiToggleAutocast toggleAutocast) {
        require("toggle autocast", toggleAutocast);
        this.controlGroup = nothing();
        this.selectArmy = nothing();
        this.selectWarpGates = nothing();
        this.selectLarva = nothing();
        this.selectIdleWorker = nothing();
        this.multiPanel = nothing();
        this.cargoPanelUnload = nothing();
        this.productionPanelRemoveFromQueue = nothing();
        this.toggleAutocast = toggleAutocast;
    }

    private boolean oneOfActionCaseIsSet() {
        return isSet(controlGroup) || isSet(selectArmy) || isSet(selectWarpGates) || isSet(selectLarva) ||
                isSet(selectIdleWorker) || isSet(multiPanel) || isSet(cargoPanelUnload) ||
                isSet(productionPanelRemoveFromQueue) || isSet(toggleAutocast);
    }

    public static ActionUi of(ActionUiControlGroup controlGroup) {
        return new ActionUi(controlGroup);
    }

    public static ActionUi of(ActionUiSelectArmy selectArmy) {
        return new ActionUi(selectArmy);
    }

    public static ActionUi of(ActionUiSelectWarpGates selectWarpGates) {
        return new ActionUi(selectWarpGates);
    }

    public static ActionUi of(ActionUiSelectLarva selectLarva) {
        return new ActionUi(selectLarva);
    }

    public static ActionUi of(ActionUiSelectIdleWorker selectIdleWorker) {
        return new ActionUi(selectIdleWorker);
    }

    public static ActionUi of(ActionUiMultiPanel multiPanel) {
        return new ActionUi(multiPanel);
    }

    public static ActionUi of(ActionUiCargoPanelUnload cargoPanelUnload) {
        return new ActionUi(cargoPanelUnload);
    }

    public static ActionUi of(ActionUiProductionPanelRemoveFromQueue productionPanelRemoveFromQueue) {
        return new ActionUi(productionPanelRemoveFromQueue);
    }

    public static ActionUi of(ActionUiToggleAutocast toggleAutocast) {
        return new ActionUi(toggleAutocast);
    }

    public static ActionUi from(Ui.ActionUI sc2ApiActionUi) {
        require("sc2api action ui", sc2ApiActionUi);
        return new ActionUi(sc2ApiActionUi);
    }

    @Override
    public Ui.ActionUI toSc2Api() {
        Ui.ActionUI.Builder aSc2ApiActionUi = Ui.ActionUI.newBuilder();

        getControlGroup().map(ActionUiControlGroup::toSc2Api).ifPresent(aSc2ApiActionUi::setControlGroup);
        getSelectArmy().map(ActionUiSelectArmy::toSc2Api).ifPresent(aSc2ApiActionUi::setSelectArmy);
        getSelectWarpGates().map(ActionUiSelectWarpGates::toSc2Api).ifPresent(aSc2ApiActionUi::setSelectWarpGates);
        getSelectLarva().map(ActionUiSelectLarva::toSc2Api).ifPresent(aSc2ApiActionUi::setSelectLarva);
        getSelectIdleWorker().map(ActionUiSelectIdleWorker::toSc2Api).ifPresent(aSc2ApiActionUi::setSelectIdleWorker);
        getMultiPanel().map(ActionUiMultiPanel::toSc2Api).ifPresent(aSc2ApiActionUi::setMultiPanel);
        getCargoPanelUnload().map(ActionUiCargoPanelUnload::toSc2Api).ifPresent(aSc2ApiActionUi::setCargoPanel);
        getProductionPanelRemoveFromQueue()
                .map(ActionUiProductionPanelRemoveFromQueue::toSc2Api).ifPresent(aSc2ApiActionUi::setProductionPanel);
        getToggleAutocast().map(ActionUiToggleAutocast::toSc2Api).ifPresent(aSc2ApiActionUi::setToggleAutocast);

        return aSc2ApiActionUi.build();
    }

    public Optional<ActionUiControlGroup> getControlGroup() {
        return Optional.ofNullable(controlGroup);
    }

    public Optional<ActionUiSelectArmy> getSelectArmy() {
        return Optional.ofNullable(selectArmy);
    }

    public Optional<ActionUiSelectWarpGates> getSelectWarpGates() {
        return Optional.ofNullable(selectWarpGates);
    }

    public Optional<ActionUiSelectLarva> getSelectLarva() {
        return Optional.ofNullable(selectLarva);
    }

    public Optional<ActionUiSelectIdleWorker> getSelectIdleWorker() {
        return Optional.ofNullable(selectIdleWorker);
    }

    public Optional<ActionUiMultiPanel> getMultiPanel() {
        return Optional.ofNullable(multiPanel);
    }

    public Optional<ActionUiCargoPanelUnload> getCargoPanelUnload() {
        return Optional.ofNullable(cargoPanelUnload);
    }

    public Optional<ActionUiProductionPanelRemoveFromQueue> getProductionPanelRemoveFromQueue() {
        return Optional.ofNullable(productionPanelRemoveFromQueue);
    }

    public Optional<ActionUiToggleAutocast> getToggleAutocast() {
        return Optional.ofNullable(toggleAutocast);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionUi actionUi = (ActionUi) o;

        return (controlGroup != null ? controlGroup.equals(actionUi.controlGroup) : actionUi.controlGroup == null) &&
                (selectArmy != null ? selectArmy.equals(actionUi.selectArmy) : actionUi.selectArmy == null) &&
                (selectWarpGates != null
                        ? selectWarpGates.equals(actionUi.selectWarpGates)
                        : actionUi.selectWarpGates == null) &&
                (selectIdleWorker != null
                        ? selectIdleWorker.equals(actionUi.selectIdleWorker)
                        : actionUi.selectIdleWorker == null) &&
                (multiPanel != null ? multiPanel.equals(actionUi.multiPanel) : actionUi.multiPanel == null) &&
                (cargoPanelUnload != null
                        ? cargoPanelUnload.equals(actionUi.cargoPanelUnload)
                        : actionUi.cargoPanelUnload == null) &&
                (productionPanelRemoveFromQueue != null
                        ? productionPanelRemoveFromQueue.equals(actionUi.productionPanelRemoveFromQueue)
                        : actionUi.productionPanelRemoveFromQueue == null) &&
                (toggleAutocast != null
                        ? toggleAutocast.equals(actionUi.toggleAutocast)
                        : actionUi.toggleAutocast == null);
    }

    @Override
    public int hashCode() {
        int result = controlGroup != null ? controlGroup.hashCode() : 0;
        result = 31 * result + (selectArmy != null ? selectArmy.hashCode() : 0);
        result = 31 * result + (selectWarpGates != null ? selectWarpGates.hashCode() : 0);
        result = 31 * result + (selectIdleWorker != null ? selectIdleWorker.hashCode() : 0);
        result = 31 * result + (multiPanel != null ? multiPanel.hashCode() : 0);
        result = 31 * result + (cargoPanelUnload != null ? cargoPanelUnload.hashCode() : 0);
        result = 31 * result + (productionPanelRemoveFromQueue != null ? productionPanelRemoveFromQueue.hashCode() : 0);
        result = 31 * result + (toggleAutocast != null ? toggleAutocast.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
