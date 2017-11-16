package com.github.ocraft.s2client.protocol.syntax.action.raw;

import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.unit.Unit;

public interface ActionRawCameraMoveSyntax {
    ActionRawCameraMoveBuilder to(Point point);

    ActionRawCameraMoveBuilder to(Unit unit);
}
