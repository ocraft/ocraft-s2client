package com.github.ocraft.s2client.protocol.syntax.action.spatial;

import com.github.ocraft.s2client.protocol.spatial.PointI;

public interface ActionSpatialCameraMoveSyntax {
    ActionSpatialCameraMoveBuilder to(PointI pointOnMinimap);
}
