package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.PlayerSetup;
import com.github.ocraft.s2client.protocol.request.RequestCreateGame;

public interface WithPlayerSetupSyntax extends BuilderSyntax<RequestCreateGame> {
    DisableFogSyntax withPlayerSetup(PlayerSetup... playerSetups);
}
