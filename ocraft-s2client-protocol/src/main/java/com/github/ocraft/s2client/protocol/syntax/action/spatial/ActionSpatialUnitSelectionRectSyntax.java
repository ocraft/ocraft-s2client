package com.github.ocraft.s2client.protocol.syntax.action.spatial;

import com.github.ocraft.s2client.protocol.spatial.RectangleI;

public interface ActionSpatialUnitSelectionRectSyntax {
    AddSyntax of(RectangleI... selections);
}
