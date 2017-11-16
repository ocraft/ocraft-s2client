package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;

public interface FeatureLayerSyntax extends RenderSyntax, BuilderSyntax<InterfaceOptions> {
    RenderSyntax featureLayer();

    RenderSyntax featureLayer(SpatialCameraSetup featureLayer);

    RenderSyntax featureLayer(BuilderSyntax<SpatialCameraSetup> featureLayer);
}
