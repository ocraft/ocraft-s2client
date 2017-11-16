package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.spatial.Point;

public interface DebugBoxSyntax {
    WithBoxColorSyntax of(Point min, Point max);
}
