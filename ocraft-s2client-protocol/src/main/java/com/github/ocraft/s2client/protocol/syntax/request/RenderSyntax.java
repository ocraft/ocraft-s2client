package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;

public interface RenderSyntax extends BuilderSyntax<InterfaceOptions> {
    BuilderSyntax<InterfaceOptions> render();

    BuilderSyntax<InterfaceOptions> render(SpatialCameraSetup render);

    BuilderSyntax<InterfaceOptions> render(BuilderSyntax<SpatialCameraSetup> render);
}
