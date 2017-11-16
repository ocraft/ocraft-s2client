package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.spatial.Point;

public interface DebugSphereSyntax {
    WithRadiusSyntax on(Point center);
}
