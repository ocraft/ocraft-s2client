package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.unit.UnitInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toList;

public final class MultiPanel implements Serializable {

    private static final long serialVersionUID = -9050006820587053858L;

    private final List<UnitInfo> units;

    private MultiPanel(Ui.MultiPanel sc2ApiMultiPanel) {
        units = sc2ApiMultiPanel.getUnitsList().stream().map(UnitInfo::from).collect(toList());
    }

    public static MultiPanel from(Ui.MultiPanel sc2ApiMultiPanel) {
        require("sc2api multi panel", sc2ApiMultiPanel);
        return new MultiPanel(sc2ApiMultiPanel);
    }

    public List<UnitInfo> getUnits() {
        return new ArrayList<>(units);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiPanel that = (MultiPanel) o;

        return units.equals(that.units);
    }

    @Override
    public int hashCode() {
        return units.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
