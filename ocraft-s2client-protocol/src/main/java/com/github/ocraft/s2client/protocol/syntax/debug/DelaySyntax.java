package com.github.ocraft.s2client.protocol.syntax.debug;

public interface DelaySyntax extends DebugTestProcessBuilder {
    DebugTestProcessBuilder delayInMillis(int delayInMillis);
}
