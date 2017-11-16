package com.github.ocraft.s2client.protocol.syntax.action.raw;

import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Tag;

public interface TargetSyntax extends ActionRawUnitCommandBuilder, QueuedSyntax {
    QueuedSyntax target(Tag unitTag);

    QueuedSyntax target(Point2d worldPosition);
}
