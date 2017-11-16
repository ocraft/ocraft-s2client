package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.request.RequestObservation;

public interface RequestObservationSyntax extends BuilderSyntax<RequestObservation> {
    BuilderSyntax<RequestObservation> disableFog();
}
