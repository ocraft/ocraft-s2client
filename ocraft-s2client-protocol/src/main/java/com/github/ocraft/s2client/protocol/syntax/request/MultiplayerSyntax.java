package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.MultiplayerOptions;
import com.github.ocraft.s2client.protocol.request.RequestJoinGame;

public interface MultiplayerSyntax extends BuilderSyntax<RequestJoinGame> {
    BuilderSyntax<RequestJoinGame> with(MultiplayerOptions multiplayerOptions);

    BuilderSyntax<RequestJoinGame> with(BuilderSyntax<MultiplayerOptions> multiplayerOptions);
}
