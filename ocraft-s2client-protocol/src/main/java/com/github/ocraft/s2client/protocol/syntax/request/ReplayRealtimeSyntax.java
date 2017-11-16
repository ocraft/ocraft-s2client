package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.request.RequestStartReplay;

public interface ReplayRealtimeSyntax extends BuilderSyntax<RequestStartReplay> {
    BuilderSyntax<RequestStartReplay> realtime();
}
