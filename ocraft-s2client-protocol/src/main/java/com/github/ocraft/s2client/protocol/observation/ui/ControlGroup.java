package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ControlGroup implements Serializable {

    private static final long serialVersionUID = -3070737797455925941L;

    private final int index;
    private final UnitType leaderUnitType;
    private final int count;

    private ControlGroup(Ui.ControlGroup sc2ApiControlGroup) {
        index = tryGet(Ui.ControlGroup::getControlGroupIndex, Ui.ControlGroup::hasControlGroupIndex)
                .apply(sc2ApiControlGroup).orElseThrow(required("index"));

        leaderUnitType = tryGet(Ui.ControlGroup::getLeaderUnitType, Ui.ControlGroup::hasLeaderUnitType)
                .apply(sc2ApiControlGroup).map(Units::from).orElseThrow(required("leader unit type"));

        count = tryGet(Ui.ControlGroup::getCount, Ui.ControlGroup::hasCount)
                .apply(sc2ApiControlGroup).orElseThrow(required("count"));
    }

    public static ControlGroup from(Ui.ControlGroup sc2ApiControlGroup) {
        require("sc2api control group", sc2ApiControlGroup);
        return new ControlGroup(sc2ApiControlGroup);
    }

    public int getIndex() {
        return index;
    }

    public UnitType getLeaderUnitType() {
        return leaderUnitType;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ControlGroup that = (ControlGroup) o;

        return index == that.index && count == that.count && leaderUnitType == that.leaderUnitType;
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + leaderUnitType.hashCode();
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
