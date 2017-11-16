package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;
import com.github.ocraft.s2client.protocol.request.RequestJoinGame;

public interface UseInterfaceSyntax extends MultiplayerSyntax, BuilderSyntax<RequestJoinGame> {
    MultiplayerSyntax use(InterfaceOptions interfaceOptions);

    MultiplayerSyntax use(BuilderSyntax<InterfaceOptions> interfaceOptions);
}
