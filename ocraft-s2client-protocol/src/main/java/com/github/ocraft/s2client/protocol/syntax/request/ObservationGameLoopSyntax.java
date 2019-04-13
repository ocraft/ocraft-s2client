package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.request.RequestObservation;

public interface ObservationGameLoopSyntax extends BuilderSyntax<RequestObservation> {

    /**
     * In realtime the request will only return once the simulation game loop has reached this value.
     * When not realtime this value is ignored.
     */
    BuilderSyntax<RequestObservation> gameLoop(int loop);
}
