package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;

public interface ReplayUseInterfaceSyntax {
    ToObserveSyntax use(InterfaceOptions interfaceOptions);

    ToObserveSyntax use(BuilderSyntax<InterfaceOptions> interfaceOptions);
}
