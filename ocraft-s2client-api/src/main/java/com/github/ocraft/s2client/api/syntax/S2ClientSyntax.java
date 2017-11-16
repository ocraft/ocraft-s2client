package com.github.ocraft.s2client.api.syntax;

import com.github.ocraft.s2client.api.controller.S2Controller;

public interface S2ClientSyntax {
    TracedSyntax connectTo(String gameListenIp, int gameListenPort);

    TracedSyntax connectTo(S2Controller theGame);
}
