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
