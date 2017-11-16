package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.action.ObserverAction;
import com.github.ocraft.s2client.protocol.request.RequestObserverAction;

public interface RequestObserverActionSyntax {
    RequestObserverAction.Builder with(ObserverAction... actions);
}
