package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.spatial.Point;

public interface TextPositionSyntax extends DebugTextBuilder {
    DebugTextBuilder on(Point point2d);

    DebugTextBuilder onMap(Point point3d);
}
