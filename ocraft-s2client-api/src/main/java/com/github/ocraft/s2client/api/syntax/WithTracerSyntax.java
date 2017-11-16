package com.github.ocraft.s2client.api.syntax;

import com.github.ocraft.s2client.api.S2Client;
import com.github.ocraft.s2client.api.log.DataFlowTracer;

public interface WithTracerSyntax extends StartSyntax<S2Client> {
    StartSyntax<S2Client> withTracer(DataFlowTracer tracer);
}
