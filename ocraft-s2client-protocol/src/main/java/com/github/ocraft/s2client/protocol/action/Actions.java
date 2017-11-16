package com.github.ocraft.s2client.protocol.action;

import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraFollowPlayer;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraFollowUnits;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverCameraMove;
import com.github.ocraft.s2client.protocol.action.observer.ActionObserverPlayerPerspective;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawCameraMove;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawToggleAutocast;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawUnitCommand;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialCameraMove;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitCommand;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionRect;
import com.github.ocraft.s2client.protocol.action.ui.*;
import com.github.ocraft.s2client.protocol.syntax.action.ActionChatSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraFollowPlayerSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraFollowUnitsSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraMoveSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverPlayerPerspectiveSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawCameraMoveSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawToggleAutocastSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawUnitCommandSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitCommandSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionPointSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionRectSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.ui.*;

public interface Actions {

    interface Raw {
        static ActionRawUnitCommandSyntax unitCommand() {
            return ActionRawUnitCommand.unitCommand();
        }

        static ActionRawCameraMoveSyntax cameraMove() {
            return ActionRawCameraMove.cameraMove();
        }

        static ActionRawToggleAutocastSyntax toggleAutocast() {
            return ActionRawToggleAutocast.toggleAutocast();
        }
    }

    interface Spatial {
        static ActionSpatialUnitCommandSyntax unitCommand() {
            return ActionSpatialUnitCommand.unitCommand();
        }

        static ActionSpatialCameraMove.Builder cameraMove() {
            return ActionSpatialCameraMove.cameraMove();
        }

        static ActionSpatialUnitSelectionPointSyntax click() {
            return ActionSpatialUnitSelectionPoint.click();
        }

        static ActionSpatialUnitSelectionRectSyntax select() {
            return ActionSpatialUnitSelectionRect.select();
        }

    }

    interface Ui {
        static ActionUiControlGroupSyntax controlGroup() {
            return ActionUiControlGroup.controlGroup();
        }

        static ActionUiSelectArmySyntax selectArmy() {
            return ActionUiSelectArmy.selectArmy();
        }

        static ActionUiSelectWarpGatesSyntax selectWarpGates() {
            return ActionUiSelectWarpGates.selectWarpGates();
        }

        static ActionUiSelectLarva selectLarva() {
            return ActionUiSelectLarva.selectLarva();
        }

        static ActionUiSelectIdleWorkerSyntax selectIdleWorker() {
            return ActionUiSelectIdleWorker.selectIdleWorker();
        }

        static ActionUiMultiPanelSyntax multiPanel() {
            return ActionUiMultiPanel.multiPanel();
        }

        static ActionUiCargoPanelUnloadSyntax cargoPanelUnload() {
            return ActionUiCargoPanelUnload.cargoPanelUnload();
        }

        static ActionUiProductionPanelRemoveFromQueueSyntax removeFromQueue() {
            return ActionUiProductionPanelRemoveFromQueue.removeFromQueue();
        }

        static ActionUiToggleAutocastSyntax toggleAutocast() {
            return ActionUiToggleAutocast.toggleAutocast();
        }

    }

    interface Observer {
        static ActionObserverPlayerPerspectiveSyntax playerPerspective() {
            return ActionObserverPlayerPerspective.playerPerspective();
        }

        static ActionObserverCameraMoveSyntax cameraMove() {
            return ActionObserverCameraMove.cameraMove();
        }

        static ActionObserverCameraFollowPlayerSyntax cameraFollowPlayer() {
            return ActionObserverCameraFollowPlayer.cameraFollowPlayer();
        }

        static ActionObserverCameraFollowUnitsSyntax cameraFollowUnits() {
            return ActionObserverCameraFollowUnits.cameraFollowUnits();
        }
    }

    static ActionChatSyntax message() {
        return ActionChat.message();
    }
}
