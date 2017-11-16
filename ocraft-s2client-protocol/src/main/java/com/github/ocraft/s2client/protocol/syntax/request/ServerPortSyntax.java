package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.game.PortSet;

public interface ServerPortSyntax {
    ClientPortsSyntax serverPort(PortSet serverPort);
}
