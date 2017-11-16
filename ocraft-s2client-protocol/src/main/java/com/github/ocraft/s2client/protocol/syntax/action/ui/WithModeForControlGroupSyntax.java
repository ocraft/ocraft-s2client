package com.github.ocraft.s2client.protocol.syntax.action.ui;

import com.github.ocraft.s2client.protocol.action.ui.ActionUiControlGroup;

public interface WithModeForControlGroupSyntax {
    ActionUiControlGroupBuilder withMode(ActionUiControlGroup.Action action);
}
