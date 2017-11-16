package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.debug.DebugEndGame;

public interface DebugEndGameSyntax {
    DebugEndGameBuilder withResult(DebugEndGame.EndResult result);
}
