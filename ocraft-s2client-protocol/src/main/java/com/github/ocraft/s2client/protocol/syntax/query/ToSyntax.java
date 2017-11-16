package com.github.ocraft.s2client.protocol.syntax.query;

import com.github.ocraft.s2client.protocol.query.QueryPathingBuilder;
import com.github.ocraft.s2client.protocol.spatial.Point2d;

public interface ToSyntax {
    QueryPathingBuilder to(Point2d end);
}
