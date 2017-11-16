package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.debug.Color;

public interface WithBoxColorSyntax extends DebugBoxBuilder {
    DebugBoxBuilder withColor(Color color);
}
