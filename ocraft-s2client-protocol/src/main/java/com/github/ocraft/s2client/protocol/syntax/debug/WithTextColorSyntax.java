package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.debug.Color;

public interface WithTextColorSyntax extends WithSizeSyntax {
    WithSizeSyntax withColor(Color color);
}
