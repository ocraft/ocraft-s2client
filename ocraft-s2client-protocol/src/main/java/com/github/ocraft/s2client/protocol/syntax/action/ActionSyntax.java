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
