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
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiProductionPanelRemoveFromQueueBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ui.ActionUiProductionPanelRemoveFromQueueSyntax;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionUiProductionPanelRemoveFromQueue
        implements Sc2ApiSerializable<Ui.ActionProductionPanelRemoveFromQueue> {

    private static final long serialVersionUID = 1543959889890602277L;

    private final int unitIndex;

    public static final class Builder
            implements ActionUiProductionPanelRemoveFromQueueSyntax, ActionUiProductionPanelRemoveFromQueueBuilder {

        private Integer unitIndex;

        @Override
        public ActionUiProductionPanelRemoveFromQueueBuilder of(int unitIndex) {
            this.unitIndex = unitIndex;
            return this;
        }

        @Override
        public ActionUiProductionPanelRemoveFromQueue build() {
            require("unit index", unitIndex);
            return new ActionUiProductionPanelRemoveFromQueue(this);
        }
    }

    private ActionUiProductionPanelRemoveFromQueue(Builder builder) {
        unitIndex = builder.unitIndex;
    }

    private ActionUiProductionPanelRemoveFromQueue(
            Ui.ActionProductionPanelRemoveFromQueue sc2ApiActionProductionPanelRemoveFromQueue) {
        unitIndex = tryGet(
                Ui.ActionProductionPanelRemoveFromQueue::getUnitIndex,
                Ui.ActionProductionPanelRemoveFromQueue::hasUnitIndex
        ).apply(sc2ApiActionProductionPanelRemoveFromQueue).orElseThrow(required("unit index"));
    }

    public static ActionUiProductionPanelRemoveFromQueue from(
            Ui.ActionProductionPanelRemoveFromQueue sc2ApiActionProductionPanelRemoveFromQueue) {
        require("sc2api action ui production panel remove from queue", sc2ApiActionProductionPanelRemoveFromQueue);
        return new ActionUiProductionPanelRemoveFromQueue(sc2ApiActionProductionPanelRemoveFromQueue);
    }

    public static ActionUiProductionPanelRemoveFromQueueSyntax removeFromQueue() {
        return new Builder();
    }

    public int getUnitIndex() {
        return unitIndex;
    }

    @Override
    public Ui.ActionProductionPanelRemoveFromQueue toSc2Api() {
        return Ui.ActionProductionPanelRemoveFromQueue.newBuilder().setUnitIndex(unitIndex).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionUiProductionPanelRemoveFromQueue that = (ActionUiProductionPanelRemoveFromQueue) o;

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
