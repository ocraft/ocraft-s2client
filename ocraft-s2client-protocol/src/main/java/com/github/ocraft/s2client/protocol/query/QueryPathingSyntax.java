package com.github.ocraft.s2client.protocol.query;

import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.syntax.query.ToSyntax;
import com.github.ocraft.s2client.protocol.unit.Tag;

public interface QueryPathingSyntax {
    ToSyntax from(Point2d start);

    ToSyntax from(Tag unitTag);
}
