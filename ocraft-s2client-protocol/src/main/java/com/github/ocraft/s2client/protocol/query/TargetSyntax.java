package com.github.ocraft.s2client.protocol.query;

import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.syntax.query.QueryBuildingPlacementBuilder;

public interface TargetSyntax {
    QueryBuildingPlacementBuilder on(Point2d target);
}
