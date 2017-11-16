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
package com.github.ocraft.s2client.protocol.syntax.action;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.action.ActionChat;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawCameraMove;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawToggleAutocast;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawUnitCommand;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialCameraMove;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitCommand;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionRect;
import com.github.ocraft.s2client.protocol.action.ui.*;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawCameraMoveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawToggleAutocastBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawUnitCommandBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialCameraMoveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitCommandBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionPointBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionRectBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ui.*;

public interface ActionSyntax {
    ActionBuilder raw(ActionRawUnitCommand unitCommand);

    ActionBuilder raw(ActionRawUnitCommandBuilder unitCommand);

    ActionBuilder raw(ActionRawCameraMove cameraMove);

    ActionBuilder raw(ActionRawCameraMoveBuilder cameraMove);

    ActionBuilder raw(ActionRawToggleAutocast toggleAutocast);

    ActionBuilder raw(ActionRawToggleAutocastBuilder toggleAutocast);

    ActionBuilder featureLayer(ActionSpatialUnitCommand unitCommand);

    ActionBuilder featureLayer(ActionSpatialUnitCommandBuilder unitCommand);

    ActionBuilder featureLayer(ActionSpatialCameraMove cameraMove);

    ActionBuilder featureLayer(ActionSpatialCameraMoveBuilder cameraMove);

    ActionBuilder featureLayer(ActionSpatialUnitSelectionPoint unitSelectionPoint);

    ActionBuilder featureLayer(ActionSpatialUnitSelectionPointBuilder unitSelectionPoint);

    ActionBuilder featureLayer(ActionSpatialUnitSelectionRect unitSelectionRect);

    ActionBuilder featureLayer(ActionSpatialUnitSelectionRectBuilder unitSelectionRect);

    ActionBuilder render(ActionSpatialUnitCommand unitCommand);

    ActionBuilder render(ActionSpatialUnitCommandBuilder unitCommand);

    ActionBuilder render(ActionSpatialCameraMove cameraMove);

    ActionBuilder render(ActionSpatialCameraMoveBuilder cameraMove);

    ActionBuilder render(ActionSpatialUnitSelectionPoint unitSelectionPoint);

    ActionBuilder render(ActionSpatialUnitSelectionPointBuilder unitSelectionPoint);

    ActionBuilder render(ActionSpatialUnitSelectionRect unitSelectionRect);

    ActionBuilder render(ActionSpatialUnitSelectionRectBuilder unitSelectionRect);

    ActionBuilder ui(ActionUiControlGroup controlGroup);

    ActionBuilder ui(ActionUiControlGroupBuilder controlGroup);

    ActionBuilder ui(ActionUiSelectArmy selectArmy);

    ActionBuilder ui(ActionUiSelectArmyBuilder selectArmy);

    ActionBuilder ui(ActionUiSelectWarpGates selectWarpGates);

    ActionBuilder ui(ActionUiSelectWarpGatesBuilder selectWarpGates);

    ActionBuilder ui(ActionUiSelectLarva selectLarva);

    ActionBuilder ui(ActionUiSelectIdleWorker selectIdleWorker);

    ActionBuilder ui(ActionUiSelectIdleWorkerBuilder selectIdleWorker);

    ActionBuilder ui(ActionUiMultiPanel multiPanel);

    ActionBuilder ui(ActionUiMultiPanelBuilder multiPanel);

    ActionBuilder ui(ActionUiCargoPanelUnload cargoPanelUnload);

    ActionBuilder ui(ActionUiCargoPanelUnloadBuilder cargoPanelUnload);

    ActionBuilder ui(ActionUiProductionPanelRemoveFromQueue productionPanelRemoveFromQueue);

    ActionBuilder ui(ActionUiProductionPanelRemoveFromQueueBuilder productionPanelRemoveFromQueue);

    ActionBuilder ui(ActionUiToggleAutocast toggleAutocast);

    ActionBuilder ui(ActionUiToggleAutocastBuilder toggleAutocast);

    ActionBuilder chat(ActionChat chat);

    ActionBuilder chat(BuilderSyntax<ActionChat> chat);
}
