package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionUiSelectLarva implements Sc2ApiSerializable<Ui.ActionSelectLarva> {

    private static final long serialVersionUID = -5834863791532766043L;

    private ActionUiSelectLarva() {

    }

    public static ActionUiSelectLarva from(Ui.ActionSelectLarva sc2ApiActionSelectLarva) {
        require("sc2api action ui select larva", sc2ApiActionSelectLarva);
        return new ActionUiSelectLarva();
    }

    public static ActionUiSelectLarva selectLarva() {
        return new ActionUiSelectLarva();
    }

    @Override
    public Ui.ActionSelectLarva toSc2Api() {
        return Ui.ActionSelectLarva.newBuilder().build();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof ActionUiSelectLarva;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
