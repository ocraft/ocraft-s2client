package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.spatial.Point2d;

public interface UnitPositionSyntax {
    WithQuantitySyntax on(Point2d position);
}
