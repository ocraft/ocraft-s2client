package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiSelectWarpGatesBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiSelectWarpGatesSyntax;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionUiSelectWarpGates implements Sc2ApiSerializable<Ui.ActionSelectWarpGates> {

    private static final long serialVersionUID = -6349750071466892582L;

    private static final Boolean DEFAULT_SELECTION_ADD = false;

    private final boolean selectionAdd;

    public static final class Builder implements ActionUiSelectWarpGatesSyntax {

        private boolean selectionAdd;

        @Override
        public ActionUiSelectWarpGatesBuilder add() {
            this.selectionAdd = true;
            return this;
        }

        @Override
        public ActionUiSelectWarpGates build() {
            return new ActionUiSelectWarpGates(this);
        }
    }

    private ActionUiSelectWarpGates(Builder builder) {
        selectionAdd = builder.selectionAdd;
    }

    private ActionUiSelectWarpGates(Ui.ActionSelectWarpGates sc2ApiActionSelectWarpGates) {
        selectionAdd = tryGet(
                Ui.ActionSelectWarpGates::getSelectionAdd,
                Ui.ActionSelectWarpGates::hasSelectionAdd
        ).apply(sc2ApiActionSelectWarpGates).orElse(DEFAULT_SELECTION_ADD);
    }

    public static ActionUiSelectWarpGates from(Ui.ActionSelectWarpGates sc2ApiActionSelectWarpGates) {
        require("sc2api action ui select warp gates", sc2ApiActionSelectWarpGates);
        return new ActionUiSelectWarpGates(sc2ApiActionSelectWarpGates);
    }

    public static ActionUiSelectWarpGatesSyntax selectWarpGates() {
        return new Builder();
    }

    @Override
    public Ui.ActionSelectWarpGates toSc2Api() {
        return Ui.ActionSelectWarpGates.newBuilder().setSelectionAdd(selectionAdd).build();
    }

    public boolean isSelectionAdd() {
        return selectionAdd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionUiSelectWarpGates that = (ActionUiSelectWarpGates) o;

        return selectionAdd == that.selectionAdd;
    }

    @Override
    public int hashCode() {
        return (selectionAdd ? 1 : 0);
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
