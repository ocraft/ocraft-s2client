package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.spatial.Size2dI;

public interface ResolutionSyntax {
    MinimapResolutionSyntax resolution(int x, int y);

    MinimapResolutionSyntax resolution(Size2dI map);
}
