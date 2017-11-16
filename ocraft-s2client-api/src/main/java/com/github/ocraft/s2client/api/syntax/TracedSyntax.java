package com.github.ocraft.s2client.api.syntax;

import com.github.ocraft.s2client.api.S2Client;

public interface TracedSyntax extends StartSyntax<S2Client> {
    WithTracerSyntax traced(boolean traced);
}
