package com.github.ocraft.s2client.protocol.syntax.action.ui;

import com.github.ocraft.s2client.protocol.action.ui.ActionUiMultiPanel;

public interface WithModeForMultiPanelSyntax {
    ActionUiMultiPanelBuilder withMode(ActionUiMultiPanel.Type type);
}
