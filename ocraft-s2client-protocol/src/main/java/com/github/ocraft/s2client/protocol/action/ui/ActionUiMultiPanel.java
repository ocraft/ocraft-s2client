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
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiMultiPanelBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiMultiPanelSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.ui.WithModeForMultiPanelSyntax;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionUiMultiPanel implements Sc2ApiSerializable<Ui.ActionMultiPanel> {

    private static final long serialVersionUID = 8154172840611178907L;

    private final Type type;
    private final int unitIndex;

    public enum Type implements Sc2ApiSerializable<Ui.ActionMultiPanel.Type> {
        SINGLE_SELECT,          // Click on icon
        DESELECT_UNIT,          // Shift Click on icon
        SELECT_ALL_OF_TYPE,     // Control Click on icon.
        DESELECT_ALL_OF_TYPE;   // Control+Shift Click on icon.

        public static Type from(Ui.ActionMultiPanel.Type sc2ApiMultiPanelType) {
            require("sc2api multi panel type", sc2ApiMultiPanelType);
            switch (sc2ApiMultiPanelType) {
                case SingleSelect:
                    return SINGLE_SELECT;
                case DeselectUnit:
                    return DESELECT_UNIT;
                case SelectAllOfType:
                    return SELECT_ALL_OF_TYPE;
                case DeselectAllOfType:
                    return DESELECT_ALL_OF_TYPE;
                default:
                    throw new AssertionError("unknown sc2api multi panel type: " + sc2ApiMultiPanelType);
            }
        }

        @Override
        public Ui.ActionMultiPanel.Type toSc2Api() {
            switch (this) {
                case SINGLE_SELECT:
                    return Ui.ActionMultiPanel.Type.SingleSelect;
                case DESELECT_UNIT:
                    return Ui.ActionMultiPanel.Type.DeselectUnit;
                case SELECT_ALL_OF_TYPE:
                    return Ui.ActionMultiPanel.Type.SelectAllOfType;
                case DESELECT_ALL_OF_TYPE:
                    return Ui.ActionMultiPanel.Type.DeselectAllOfType;
                default:
                    throw new AssertionError("unknown multi panel type: " + this);
            }
        }
    }

    public static final class Builder
            implements ActionUiMultiPanelSyntax, WithModeForMultiPanelSyntax, ActionUiMultiPanelBuilder {

        private Type type;
        private Integer unitIndex;

        @Override
        public WithModeForMultiPanelSyntax select(int unitIndex) {
            this.unitIndex = unitIndex;
            return this;
        }

        @Override
        public ActionUiMultiPanelBuilder withMode(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public ActionUiMultiPanel build() {
            require("type", type);
            require("unit index", unitIndex);
            return new ActionUiMultiPanel(this);
        }
    }

    private ActionUiMultiPanel(Builder builder) {
        type = builder.type;
        unitIndex = builder.unitIndex;
    }

    private ActionUiMultiPanel(Ui.ActionMultiPanel sc2ApiActionMultiPanel) {
        type = tryGet(
                Ui.ActionMultiPanel::getType, Ui.ActionMultiPanel::hasType
        ).apply(sc2ApiActionMultiPanel).map(Type::from).orElseThrow(required("type"));

        unitIndex = tryGet(
                Ui.ActionMultiPanel::getUnitIndex, Ui.ActionMultiPanel::hasUnitIndex
        ).apply(sc2ApiActionMultiPanel).orElseThrow(required("unit index"));
    }

    public static ActionUiMultiPanelSyntax multiPanel() {
        return new Builder();
    }

    public static ActionUiMultiPanel from(Ui.ActionMultiPanel sc2ApiActionMultiPanel) {
        require("sc2api action ui multi panel", sc2ApiActionMultiPanel);
        return new ActionUiMultiPanel(sc2ApiActionMultiPanel);
    }

    @Override
    public Ui.ActionMultiPanel toSc2Api() {
        return Ui.ActionMultiPanel.newBuilder().setUnitIndex(unitIndex).setType(type.toSc2Api()).build();
    }

    public Type getType() {
        return type;
    }

    public int getUnitIndex() {
        return unitIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionUiMultiPanel that = (ActionUiMultiPanel) o;

        return unitIndex == that.unitIndex && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + unitIndex;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
