package com.github.ocraft.s2client.bot.gateway.impl;

/*-
 * #%L
 * ocraft-s2client-bot
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

import com.github.ocraft.s2client.bot.gateway.ActionFeatureLayerInterface;
import com.github.ocraft.s2client.protocol.action.Action;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialCameraMove;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitCommand;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionRect;
import com.github.ocraft.s2client.protocol.action.ui.ActionUiCargoPanelUnload;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.request.RequestAction;
import com.github.ocraft.s2client.protocol.request.Requests;
import com.github.ocraft.s2client.protocol.response.ResponseAction;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.spatial.RectangleI;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.AddSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.TargetSyntax;
import com.github.ocraft.s2client.protocol.unit.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.action.Action.action;

class ActionFeatureLayerInterfaceImpl implements ActionFeatureLayerInterface {

    private final ControlInterfaceImpl controlInterface;
    private final List<Action> actions = new ArrayList<>();

    ActionFeatureLayerInterfaceImpl(ControlInterfaceImpl controlInterface) {
        this.controlInterface = controlInterface;
    }

    private ControlInterfaceImpl control() {
        return controlInterface;
    }

    @Override
    public ActionFeatureLayerInterface unitCommand(Ability ability) {
        actions.add(action().featureLayer(ActionSpatialUnitCommand.unitCommand().useAbility(ability)).build());
        return this;
    }

    @Override
    public ActionFeatureLayerInterface unitCommand(Ability ability, PointI point, boolean minimap) {
        TargetSyntax targetSyntax = ActionSpatialUnitCommand.unitCommand().useAbility(ability);
        if (minimap) {
            targetSyntax.onMinimap(point);
        } else {
            targetSyntax.onScreen(point);
        }
        actions.add(action().featureLayer(targetSyntax).build());
        return this;
    }

    @Override
    public ActionFeatureLayerInterface cameraMove(PointI center) {
        actions.add(action().featureLayer(ActionSpatialCameraMove.cameraMove().to(center)).build());
        return this;
    }

    @Override
    public ActionFeatureLayerInterface select(PointI center, ActionSpatialUnitSelectionPoint.Type selectionType) {
        actions.add(action()
                .featureLayer(ActionSpatialUnitSelectionPoint.click().on(center).withMode(selectionType))
                .build());
        return this;
    }

    @Override
    public ActionFeatureLayerInterface select(PointI p0, PointI p1, boolean addToSelection) {
        AddSyntax unitSelectionRect = ActionSpatialUnitSelectionRect.select().of(RectangleI.of(p0, p1));
        if (addToSelection) unitSelectionRect.add();
        actions.add(action()
                .featureLayer(unitSelectionRect)
                .build());
        return this;
    }

    @Override
    public ActionFeatureLayerInterface unloadCargo(int unitIndex) {
        actions.add(action()
                .ui(ActionUiCargoPanelUnload.cargoPanelUnload().of(unitIndex).build())
                .build());
        return this;
    }

    @Override
    public boolean sendActions() {
        if (actions.isEmpty()) return false;
        RequestAction.Builder request = Requests.actions().of(actions.toArray(new Action[0]));
        actions.clear();
        return control()
                .waitForResponse(control().proto().sendRequest(request))
                .flatMap(response -> response.as(ResponseAction.class))
                .isPresent();
    }
}
