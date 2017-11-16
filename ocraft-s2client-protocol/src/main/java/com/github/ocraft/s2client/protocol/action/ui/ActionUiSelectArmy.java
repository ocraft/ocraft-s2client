package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiSelectArmyBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiSelectArmySyntax;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionUiSelectArmy implements Sc2ApiSerializable<Ui.ActionSelectArmy> {

    private static final long serialVersionUID = -2687523542295753665L;

    private static final Boolean DEFAULT_SELECTION_ADD = false;

    private final boolean selectionAdd;

    public static final class Builder implements ActionUiSelectArmySyntax {

        private boolean selectionAdd;

        @Override
        public ActionUiSelectArmyBuilder add() {
            this.selectionAdd = true;
            return this;
        }

        @Override
        public ActionUiSelectArmy build() {
            return new ActionUiSelectArmy(this);
        }
    }

    private ActionUiSelectArmy(Builder builder) {
        selectionAdd = builder.selectionAdd;
    }

    private ActionUiSelectArmy(Ui.ActionSelectArmy sc2ApiActionSelectArmy) {
        selectionAdd = tryGet(
                Ui.ActionSelectArmy::getSelectionAdd,
                Ui.ActionSelectArmy::hasSelectionAdd
        ).apply(sc2ApiActionSelectArmy).orElse(DEFAULT_SELECTION_ADD);
    }

    public static ActionUiSelectArmySyntax selectArmy() {
        return new Builder();
    }

    public static ActionUiSelectArmy from(Ui.ActionSelectArmy sc2ApiActionSelectArmy) {
        require("sc2api action ui select army", sc2ApiActionSelectArmy);
        return new ActionUiSelectArmy(sc2ApiActionSelectArmy);
    }

    @Override
    public Ui.ActionSelectArmy toSc2Api() {
        return Ui.ActionSelectArmy.newBuilder().setSelectionAdd(selectionAdd).build();
    }

    public boolean isSelectionAdd() {
        return selectionAdd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionUiSelectArmy that = (ActionUiSelectArmy) o;

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
