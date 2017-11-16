package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.game.MultiplayerOptions;
import com.github.ocraft.s2client.protocol.game.PortSet;

import java.util.Collection;

public interface ClientPortsSyntax {
    BuilderSyntax<MultiplayerOptions> clientPorts(PortSet... clientPorts);

    BuilderSyntax<MultiplayerOptions> clientPorts(Collection<PortSet> clientPorts);
}
