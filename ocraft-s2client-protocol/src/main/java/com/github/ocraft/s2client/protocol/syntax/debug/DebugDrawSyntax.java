package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.debug.*;

public interface DebugDrawSyntax {
    DebugDraw.Builder texts(DebugText... texts);

    DebugDraw.Builder texts(DebugTextBuilder... texts);

    DebugDraw.Builder lines(DebugLine... lines);

    DebugDraw.Builder lines(DebugLineBuilder... lines);

    DebugDraw.Builder boxes(DebugBox... boxes);

    DebugDraw.Builder boxes(DebugBoxBuilder... boxes);

    DebugDraw.Builder spheres(DebugSphere... spheres);

    DebugDraw.Builder spheres(DebugSphereBuilder... spheres);
}
