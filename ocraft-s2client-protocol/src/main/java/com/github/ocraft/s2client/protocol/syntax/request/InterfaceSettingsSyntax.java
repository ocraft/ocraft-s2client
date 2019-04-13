package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;

public interface InterfaceSettingsSyntax extends BuilderSyntax<InterfaceOptions> {
    /**
     * By default cloaked units are completely hidden. This shows some details.
     */
    InterfaceOptionsSyntax showCloaked(Boolean value);
}
