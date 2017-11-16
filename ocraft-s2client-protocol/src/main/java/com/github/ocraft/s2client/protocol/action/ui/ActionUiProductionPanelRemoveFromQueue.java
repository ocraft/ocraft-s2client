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
