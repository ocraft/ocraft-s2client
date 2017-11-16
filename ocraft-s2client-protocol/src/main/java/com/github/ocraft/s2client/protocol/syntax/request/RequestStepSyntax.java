package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.request.RequestStep;

public interface RequestStepSyntax extends BuilderSyntax<RequestStep> {
    BuilderSyntax<RequestStep> withCount(int gameLoopCount);
}
