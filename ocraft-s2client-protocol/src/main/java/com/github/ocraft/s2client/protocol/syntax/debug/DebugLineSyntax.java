package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.debug.Line;
import com.github.ocraft.s2client.protocol.spatial.Point;

public interface DebugLineSyntax {
    WithLineColorSyntax of(Line line);

    WithLineColorSyntax of(Point p0, Point p1);
}
