package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.debug.Color;

public interface WithSphereColorSyntax extends DebugSphereBuilder {
    DebugSphereBuilder withColor(Color color);
}
