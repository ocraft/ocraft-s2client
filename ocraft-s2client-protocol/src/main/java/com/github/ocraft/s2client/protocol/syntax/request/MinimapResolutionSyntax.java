package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.spatial.Size2dI;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;

public interface MinimapResolutionSyntax {
    BuilderSyntax<SpatialCameraSetup> minimap(int x, int y);

    BuilderSyntax<SpatialCameraSetup> minimap(Size2dI minimap);
}
