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
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiCargoPanelUnloadBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiCargoPanelUnloadSyntax;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionUiCargoPanelUnload implements Sc2ApiSerializable<Ui.ActionCargoPanelUnload> {

    private static final long serialVersionUID = 626351730810600714L;

    private final int unitIndex;

    public static final class Builder implements ActionUiCargoPanelUnloadSyntax, ActionUiCargoPanelUnloadBuilder {

        private Integer unitIndex;

        @Override
        public ActionUiCargoPanelUnloadBuilder of(int unitIndex) {
            this.unitIndex = unitIndex;
            return this;
        }

        @Override
        public ActionUiCargoPanelUnload build() {
            require("unit index", unitIndex);
            return new ActionUiCargoPanelUnload(this);
        }
    }

    private ActionUiCargoPanelUnload(Builder builder) {
        unitIndex = builder.unitIndex;
    }

    private ActionUiCargoPanelUnload(Ui.ActionCargoPanelUnload sc2ApiActionCargoPanelUnload) {
        unitIndex = tryGet(
                Ui.ActionCargoPanelUnload::getUnitIndex, Ui.ActionCargoPanelUnload::hasUnitIndex
        ).apply(sc2ApiActionCargoPanelUnload).orElseThrow(required("unit index"));
    }

    public static ActionUiCargoPanelUnload from(Ui.ActionCargoPanelUnload sc2ApiActionCargoPanelUnload) {
        require("sc2api action ui cargo panel unload", sc2ApiActionCargoPanelUnload);
        return new ActionUiCargoPanelUnload(sc2ApiActionCargoPanelUnload);
    }

    public static ActionUiCargoPanelUnloadSyntax cargoPanelUnload() {
        return new Builder();
    }

    public int getUnitIndex() {
        return unitIndex;
    }

    @Override
    public Ui.ActionCargoPanelUnload toSc2Api() {
        return Ui.ActionCargoPanelUnload.newBuilder().setUnitIndex(unitIndex).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionUiCargoPanelUnload that = (ActionUiCargoPanelUnload) o;

        return unitIndex == that.unitIndex;
    }

    @Override
    public int hashCode() {
        return unitIndex;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
