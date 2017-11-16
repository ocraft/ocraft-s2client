package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.debug.DebugTestProcess;

public interface DebugTestProcessSyntax {
    DelaySyntax with(DebugTestProcess.Test test);
}
