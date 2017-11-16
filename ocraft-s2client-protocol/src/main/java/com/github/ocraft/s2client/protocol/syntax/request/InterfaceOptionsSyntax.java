package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;

public interface InterfaceOptionsSyntax {
    ScoreSyntax raw();

    FeatureLayerSyntax score();

    RenderSyntax featureLayer();

    RenderSyntax featureLayer(SpatialCameraSetup featureLayer);

    RenderSyntax featureLayer(BuilderSyntax<SpatialCameraSetup> featureLayer);

    BuilderSyntax<InterfaceOptions> render();

    BuilderSyntax<InterfaceOptions> render(SpatialCameraSetup render);

    BuilderSyntax<InterfaceOptions> render(BuilderSyntax<SpatialCameraSetup> render);

}
