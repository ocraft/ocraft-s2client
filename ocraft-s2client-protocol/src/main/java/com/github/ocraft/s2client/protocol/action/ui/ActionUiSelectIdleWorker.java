/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiSelectIdleWorkerBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiSelectIdleWorkerSyntax;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionUiSelectIdleWorker implements Sc2ApiSerializable<Ui.ActionSelectIdleWorker> {

    private static final long serialVersionUID = -260827473295985198L;

    private final Type type;

    public enum Type implements Sc2ApiSerializable<Ui.ActionSelectIdleWorker.Type> {
        SET,        // Equivalent to click with no modifiers. Replaces selection with single idle worker.
        ADD,        // Equivalent to shift+click. Adds single idle worker to current selection.
        ALL,        // Equivalent to control+click. Selects all idle workers.
        ADD_ALL;    // Equivalent to shift+control+click. Adds all idle workers to current selection.

        public static Type from(Ui.ActionSelectIdleWorker.Type sc2ApiSelectIdleWorkerType) {
            require("sc2api select idle worker type", sc2ApiSelectIdleWorkerType);
            switch (sc2ApiSelectIdleWorkerType) {
                case Set:
                    return SET;
                case Add:
                    return ADD;
                case All:
                    return ALL;
                case AddAll:
                    return ADD_ALL;
                default:
                    throw new AssertionError("unknown sc2api select idle worker type: " + sc2ApiSelectIdleWorkerType);
            }
        }

        @Override
        public Ui.ActionSelectIdleWorker.Type toSc2Api() {
            switch (this) {
                case SET:
                    return Ui.ActionSelectIdleWorker.Type.Set;
                case ADD:
                    return Ui.ActionSelectIdleWorker.Type.Add;
                case ALL:
                    return Ui.ActionSelectIdleWorker.Type.All;
                case ADD_ALL:
                    return Ui.ActionSelectIdleWorker.Type.AddAll;
                default:
                    throw new AssertionError("unknown select idle worker type: " + this);
            }
        }
    }

    public static final class Builder implements ActionUiSelectIdleWorkerSyntax, ActionUiSelectIdleWorkerBuilder {
        private Type type;

        @Override
        public ActionUiSelectIdleWorkerBuilder withMode(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public ActionUiSelectIdleWorker build() {
            require("type", type);
            return new ActionUiSelectIdleWorker(this);
        }
    }

    private ActionUiSelectIdleWorker(Builder builder) {
        this.type = builder.type;
    }

    private ActionUiSelectIdleWorker(Ui.ActionSelectIdleWorker sc2ApiActionSelectIdleWorker) {
        type = tryGet(
                Ui.ActionSelectIdleWorker::getType, Ui.ActionSelectIdleWorker::hasType
        ).apply(sc2ApiActionSelectIdleWorker).map(ActionUiSelectIdleWorker.Type::from).orElseThrow(required("type"));
    }

    public static ActionUiSelectIdleWorkerSyntax selectIdleWorker() {
        return new Builder();
    }

    public static ActionUiSelectIdleWorker from(Ui.ActionSelectIdleWorker sc2ApiActionSelectIdleWorker) {
        require("sc2api action ui select idle worker", sc2ApiActionSelectIdleWorker);
        return new ActionUiSelectIdleWorker(sc2ApiActionSelectIdleWorker);
    }

    @Override
    public Ui.ActionSelectIdleWorker toSc2Api() {
        return Ui.ActionSelectIdleWorker.newBuilder().setType(type.toSc2Api()).build();
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionUiSelectIdleWorker that = (ActionUiSelectIdleWorker) o;

        return type == that.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
