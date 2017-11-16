package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.game.Observer;
import com.github.ocraft.s2client.protocol.game.Race;

public interface RequestJoinGameSyntax {
    UseInterfaceSyntax as(Race race);

    UseInterfaceSyntax as(Observer observer);
}
