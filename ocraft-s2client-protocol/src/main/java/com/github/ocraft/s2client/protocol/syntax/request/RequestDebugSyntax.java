package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.debug.DebugCommand;
import com.github.ocraft.s2client.protocol.request.RequestDebug;

public interface RequestDebugSyntax {
    BuilderSyntax<RequestDebug> with(DebugCommand... commands);
}
