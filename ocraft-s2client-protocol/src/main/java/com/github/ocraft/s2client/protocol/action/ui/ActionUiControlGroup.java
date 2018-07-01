package com.github.ocraft.s2client.protocol.action.ui;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiControlGroupBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiControlGroupSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.ui.WithModeForControlGroupSyntax;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.between;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionUiControlGroup implements Sc2ApiSerializable<Ui.ActionControlGroup> {

    private static final long serialVersionUID = 3550729726112904442L;

    private static final int MIN_INDEX = 0;
    private static final int MAX_INDEX = 9;

    private final Action action;
    private final int index;

    public enum Action implements Sc2ApiSerializable<Ui.ActionControlGroup.ControlGroupAction> {
        RECALL,             // Equivalent to number hotkey. Replaces current selection with control group.
        SET,                // Equivalent to Control + number hotkey. Sets control group to current selection.
        APPEND,             // Equivalent to Shift + number hotkey. Adds current selection into control group.
        SET_AND_STEAL,      /* Equivalent to Control + Alt + number hotkey. Sets control group to current selection.
        Units are removed from other control groups. */
        APPEND_AND_STEAL;   /* Equivalent to Shift + Alt + number hotkey. Adds current selection into control group.
        Units are removed from other control groups. */

        public static Action from(Ui.ActionControlGroup.ControlGroupAction sc2ApiControlGroupAction) {
            require("sc2api control group action", sc2ApiControlGroupAction);
            switch (sc2ApiControlGroupAction) {
                case Recall:
                    return RECALL;
                case Set:
                    return SET;
                case Append:
                    return APPEND;
                case SetAndSteal:
                    return SET_AND_STEAL;
                case AppendAndSteal:
                    return APPEND_AND_STEAL;
                default:
                    throw new AssertionError("unknown sc2api control group action: " + sc2ApiControlGroupAction);
            }
        }

        @Override
        public Ui.ActionControlGroup.ControlGroupAction toSc2Api() {
            switch (this) {
                case RECALL:
                    return Ui.ActionControlGroup.ControlGroupAction.Recall;
                case SET:
                    return Ui.ActionControlGroup.ControlGroupAction.Set;
                case APPEND:
                    return Ui.ActionControlGroup.ControlGroupAction.Append;
                case SET_AND_STEAL:
                    return Ui.ActionControlGroup.ControlGroupAction.SetAndSteal;
                case APPEND_AND_STEAL:
                    return Ui.ActionControlGroup.ControlGroupAction.AppendAndSteal;
                default:
                    throw new AssertionError("unknown control group action: " + this);
            }
        }
    }

    public static final class Builder
            implements ActionUiControlGroupBuilder, ActionUiControlGroupSyntax, WithModeForControlGroupSyntax {

        private Action action;
        private Integer index;

        @Override
        public WithModeForControlGroupSyntax on(int controlGroupIndex) {
            this.index = controlGroupIndex;
            return this;
        }

        @Override
        public ActionUiControlGroupBuilder withMode(Action action) {
            this.action = action;
            return this;
        }

        @Override
        public ActionUiControlGroup build() {
            require("action", action);
            require("index", index);
            between("control group index", index, MIN_INDEX, MAX_INDEX);
            return new ActionUiControlGroup(this);
        }
    }

    private ActionUiControlGroup(Builder builder) {
        action = builder.action;
        index = builder.index;
    }

    private ActionUiControlGroup(Ui.ActionControlGroup sc2ApiActionControlGroup) {
        action = tryGet(
                Ui.ActionControlGroup::getAction, Ui.ActionControlGroup::hasAction
        ).apply(sc2ApiActionControlGroup).map(Action::from).orElseThrow(required("action"));

        index = tryGet(
                Ui.ActionControlGroup::getControlGroupIndex, Ui.ActionControlGroup::hasControlGroupIndex
        ).apply(sc2ApiActionControlGroup).orElseThrow(required("index"));
    }

    public static ActionUiControlGroupSyntax controlGroup() {
        return new Builder();
    }

    public static ActionUiControlGroup from(Ui.ActionControlGroup sc2ApiActionControlGroup) {
        require("sc2api action ui control group", sc2ApiActionControlGroup);
        return new ActionUiControlGroup(sc2ApiActionControlGroup);
    }

    @Override
    public Ui.ActionControlGroup toSc2Api() {
        return Ui.ActionControlGroup.newBuilder().setAction(action.toSc2Api()).setControlGroupIndex(index).build();
    }

    public Action getAction() {
        return action;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionUiControlGroup that = (ActionUiControlGroup) o;

        return index == that.index && action == that.action;
    }

    @Override
    public int hashCode() {
        int result = action.hashCode();
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
