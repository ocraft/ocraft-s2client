package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.action.Action;
import com.github.ocraft.s2client.protocol.request.RequestAction;
import com.github.ocraft.s2client.protocol.syntax.action.ActionBuilder;

public interface RequestActionSyntax {
    RequestAction.Builder of(Action... actions);

    RequestAction.Builder of(ActionBuilder... actions);
}
