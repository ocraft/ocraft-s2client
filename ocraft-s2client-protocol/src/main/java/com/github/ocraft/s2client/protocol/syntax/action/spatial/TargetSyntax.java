package com.github.ocraft.s2client.protocol.syntax.action.spatial;

import com.github.ocraft.s2client.protocol.spatial.PointI;

public interface TargetSyntax extends QueuedSyntax {
    QueuedSyntax onScreen(PointI targetOnScreen);

    QueuedSyntax onMinimap(PointI targetOnMinimap);
}
