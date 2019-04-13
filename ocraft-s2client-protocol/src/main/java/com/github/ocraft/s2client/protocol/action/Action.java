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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.action.raw.ActionRaw;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawCameraMove;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawToggleAutocast;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawUnitCommand;
import com.github.ocraft.s2client.protocol.action.spatial.*;
import com.github.ocraft.s2client.protocol.action.ui.*;
import com.github.ocraft.s2client.protocol.syntax.action.ActionBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ActionSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawCameraMoveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawToggleAutocastBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawUnitCommandBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialCameraMoveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitCommandBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionPointBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionRectBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ui.*;

import java.util.Objects;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class Action implements Sc2ApiSerializable<Sc2Api.Action> {

    private static final long serialVersionUID = -6488033709974072822L;

    private final ActionRaw raw;
    private final ActionSpatial featureLayer;
    private final ActionSpatial render;
    private final ActionUi ui;
    private final ActionChat chat;
    private final Integer gameLoop;

    public static final class Builder implements ActionBuilder, ActionSyntax {
        private ActionRaw raw;
        private ActionSpatial featureLayer;
        private ActionSpatial render;
        private ActionUi ui;
        private ActionChat chat;

        @Override
        public ActionBuilder raw(ActionRawUnitCommand unitCommand) {
            raw = ActionRaw.of(unitCommand);
            return this;
        }

        @Override
        public ActionBuilder raw(ActionRawUnitCommandBuilder unitCommand) {
            raw = ActionRaw.of(unitCommand.build());
            return this;
        }

        @Override
        public ActionBuilder raw(ActionRawCameraMove cameraMove) {
            raw = ActionRaw.of(cameraMove);
            return this;
        }

        @Override
        public ActionBuilder raw(ActionRawCameraMoveBuilder cameraMove) {
            raw = ActionRaw.of(cameraMove.build());
            return this;
        }

        @Override
        public ActionBuilder raw(ActionRawToggleAutocast toggleAutocast) {
            raw = ActionRaw.of(toggleAutocast);
            return this;
        }

        @Override
        public ActionBuilder raw(ActionRawToggleAutocastBuilder toggleAutocast) {
            raw = ActionRaw.of(toggleAutocast.build());
            return this;
        }

        @Override
        public ActionBuilder featureLayer(ActionSpatialUnitCommand unitCommand) {
            featureLayer = ActionSpatial.of(unitCommand);
            return this;
        }

        @Override
        public ActionBuilder featureLayer(ActionSpatialUnitCommandBuilder unitCommand) {
            featureLayer = ActionSpatial.of(unitCommand.build());
            return this;
        }

        @Override
        public ActionBuilder featureLayer(ActionSpatialCameraMove cameraMove) {
            featureLayer = ActionSpatial.of(cameraMove);
            return this;
        }

        @Override
        public ActionBuilder featureLayer(ActionSpatialCameraMoveBuilder cameraMove) {
            featureLayer = ActionSpatial.of(cameraMove.build());
            return this;
        }

        @Override
        public ActionBuilder featureLayer(ActionSpatialUnitSelectionPoint unitSelectionPoint) {
            featureLayer = ActionSpatial.of(unitSelectionPoint);
            return this;
        }

        @Override
        public ActionBuilder featureLayer(ActionSpatialUnitSelectionPointBuilder unitSelectionPoint) {
            featureLayer = ActionSpatial.of(unitSelectionPoint.build());
            return this;
        }

        @Override
        public ActionBuilder featureLayer(ActionSpatialUnitSelectionRect unitSelectionRect) {
            featureLayer = ActionSpatial.of(unitSelectionRect);
            return this;
        }

        @Override
        public ActionBuilder featureLayer(ActionSpatialUnitSelectionRectBuilder unitSelectionRect) {
            featureLayer = ActionSpatial.of(unitSelectionRect.build());
            return this;
        }

        @Override
        public ActionBuilder render(ActionSpatialUnitCommand unitCommand) {
            render = ActionSpatial.of(unitCommand);
            return this;
        }

        @Override
        public ActionBuilder render(ActionSpatialUnitCommandBuilder unitCommand) {
            render = ActionSpatial.of(unitCommand.build());
            return this;
        }

        @Override
        public ActionBuilder render(ActionSpatialCameraMove cameraMove) {
            render = ActionSpatial.of(cameraMove);
            return this;
        }

        @Override
        public ActionBuilder render(ActionSpatialCameraMoveBuilder cameraMove) {
            render = ActionSpatial.of(cameraMove.build());
            return this;
        }

        @Override
        public ActionBuilder render(ActionSpatialUnitSelectionPoint unitSelectionPoint) {
            render = ActionSpatial.of(unitSelectionPoint);
            return this;
        }

        @Override
        public ActionBuilder render(ActionSpatialUnitSelectionPointBuilder unitSelectionPoint) {
            render = ActionSpatial.of(unitSelectionPoint.build());
            return this;
        }

        @Override
        public ActionBuilder render(ActionSpatialUnitSelectionRect unitSelectionRect) {
            render = ActionSpatial.of(unitSelectionRect);
            return this;
        }

        @Override
        public ActionBuilder render(ActionSpatialUnitSelectionRectBuilder unitSelectionRect) {
            render = ActionSpatial.of(unitSelectionRect.build());
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiControlGroup controlGroup) {
            ui = ActionUi.of(controlGroup);
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiControlGroupBuilder controlGroup) {
            ui = ActionUi.of(controlGroup.build());
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiSelectArmy selectArmy) {
            ui = ActionUi.of(selectArmy);
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiSelectArmyBuilder selectArmy) {
            ui = ActionUi.of(selectArmy.build());
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiSelectWarpGates selectWarpGates) {
            ui = ActionUi.of(selectWarpGates);
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiSelectWarpGatesBuilder selectWarpGates) {
            ui = ActionUi.of(selectWarpGates.build());
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiSelectLarva selectLarva) {
            ui = ActionUi.of(selectLarva);
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiSelectIdleWorker selectIdleWorker) {
            ui = ActionUi.of(selectIdleWorker);
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiSelectIdleWorkerBuilder selectIdleWorker) {
            ui = ActionUi.of(selectIdleWorker.build());
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiMultiPanel multiPanel) {
            ui = ActionUi.of(multiPanel);
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiMultiPanelBuilder multiPanel) {
            ui = ActionUi.of(multiPanel.build());
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiCargoPanelUnload cargoPanelUnload) {
            ui = ActionUi.of(cargoPanelUnload);
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiCargoPanelUnloadBuilder cargoPanelUnload) {
            ui = ActionUi.of(cargoPanelUnload.build());
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiProductionPanelRemoveFromQueue productionPanelRemoveFromQueue) {
            ui = ActionUi.of(productionPanelRemoveFromQueue);
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiProductionPanelRemoveFromQueueBuilder productionPanelRemoveFromQueue) {
            ui = ActionUi.of(productionPanelRemoveFromQueue.build());
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiToggleAutocast toggleAutocast) {
            ui = ActionUi.of(toggleAutocast);
            return this;
        }

        @Override
        public ActionBuilder ui(ActionUiToggleAutocastBuilder toggleAutocast) {
            ui = ActionUi.of(toggleAutocast.build());
            return this;
        }

        @Override
        public ActionBuilder chat(ActionChat chat) {
            this.chat = chat;
            return this;
        }

        @Override
        public ActionBuilder chat(BuilderSyntax<ActionChat> chat) {
            return chat(chat.build());
        }

        @Override
        public Action build() {
            return new Action(this);
        }
    }

    private Action(Builder builder) {
        raw = builder.raw;
        featureLayer = builder.featureLayer;
        render = builder.render;
        ui = builder.ui;
        chat = builder.chat;
        gameLoop = null;
        validateActionCase();
    }

    private void validateActionCase() {
        if (!oneOfActionCaseIsSet()) {
            throw new IllegalArgumentException("one of action case is required");
        }
    }

    private Action(Sc2Api.Action sc2ApiAction) {
        this.raw = tryGet(
                Sc2Api.Action::getActionRaw, Sc2Api.Action::hasActionRaw
        ).apply(sc2ApiAction).map(ActionRaw::from).orElse(nothing());

        this.featureLayer = tryGet(
                Sc2Api.Action::getActionFeatureLayer, Sc2Api.Action::hasActionFeatureLayer
        ).apply(sc2ApiAction).map(ActionSpatial::from).orElse(nothing());

        this.render = tryGet(
                Sc2Api.Action::getActionRender, Sc2Api.Action::hasActionRender
        ).apply(sc2ApiAction).map(ActionSpatial::from).orElse(nothing());

        this.ui = tryGet(
                Sc2Api.Action::getActionUi, Sc2Api.Action::hasActionUi
        ).apply(sc2ApiAction).map(ActionUi::from).orElse(nothing());

        this.chat = tryGet(
                Sc2Api.Action::getActionChat, Sc2Api.Action::hasActionChat
        ).apply(sc2ApiAction).map(ActionChat::from).orElse(nothing());

        this.gameLoop = tryGet(
                Sc2Api.Action::getGameLoop, Sc2Api.Action::hasGameLoop
        ).apply(sc2ApiAction).orElse(nothing());

        validateActionCase();
    }

    private boolean oneOfActionCaseIsSet() {
        return isSet(raw) || isSet(featureLayer) || isSet(render) || isSet(ui) || isSet(chat);
    }

    public static ActionSyntax action() {
        return new Builder();
    }

    public static Action from(Sc2Api.Action sc2ApiAction) {
        require("sc2api action", sc2ApiAction);
        return new Action(sc2ApiAction);
    }

    @Override
    public Sc2Api.Action toSc2Api() {
        Sc2Api.Action.Builder aSc2ApiAction = Sc2Api.Action.newBuilder();

        getRaw().map(ActionRaw::toSc2Api).ifPresent(aSc2ApiAction::setActionRaw);
        getFeatureLayer().map(ActionSpatial::toSc2Api).ifPresent(aSc2ApiAction::setActionFeatureLayer);
        getRender().map(ActionSpatial::toSc2Api).ifPresent(aSc2ApiAction::setActionRender);
        getUi().map(ActionUi::toSc2Api).ifPresent(aSc2ApiAction::setActionUi);
        getChat().map(ActionChat::toSc2Api).ifPresent(aSc2ApiAction::setActionChat);

        return aSc2ApiAction.build();
    }

    public Optional<ActionRaw> getRaw() {
        return Optional.ofNullable(raw);
    }

    public Optional<ActionSpatial> getFeatureLayer() {
        return Optional.ofNullable(featureLayer);
    }

    public Optional<ActionSpatial> getRender() {
        return Optional.ofNullable(render);
    }

    public Optional<ActionUi> getUi() {
        return Optional.ofNullable(ui);
    }

    public Optional<ActionChat> getChat() {
        return Optional.ofNullable(chat);
    }

    /**
     * Populated for actions in ResponseObservation. The game loop on which the action was executed.
     */
    public Optional<Integer> getGameLoop() {
        return Optional.ofNullable(gameLoop);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Action action = (Action) o;

        if (!Objects.equals(raw, action.raw)) return false;
        if (!Objects.equals(featureLayer, action.featureLayer))
            return false;
        if (!Objects.equals(render, action.render)) return false;
        if (!Objects.equals(ui, action.ui)) return false;
        if (!Objects.equals(chat, action.chat)) return false;
        return Objects.equals(gameLoop, action.gameLoop);

    }

    @Override
    public int hashCode() {
        int result = raw != null ? raw.hashCode() : 0;
        result = 31 * result + (featureLayer != null ? featureLayer.hashCode() : 0);
        result = 31 * result + (render != null ? render.hashCode() : 0);
        result = 31 * result + (ui != null ? ui.hashCode() : 0);
        result = 31 * result + (chat != null ? chat.hashCode() : 0);
        result = 31 * result + (gameLoop != null ? gameLoop.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
