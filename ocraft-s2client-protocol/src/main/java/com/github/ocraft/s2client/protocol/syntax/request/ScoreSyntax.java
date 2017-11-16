package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;

public interface ScoreSyntax extends FeatureLayerSyntax, RenderSyntax, BuilderSyntax<InterfaceOptions> {
    FeatureLayerSyntax score();
}
