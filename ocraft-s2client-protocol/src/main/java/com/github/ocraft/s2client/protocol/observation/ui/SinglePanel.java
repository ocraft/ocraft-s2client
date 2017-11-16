package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.unit.UnitInfo;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class SinglePanel implements Serializable {

    private static final long serialVersionUID = -4279366565953511871L;

    private final UnitInfo unit;

    private SinglePanel(Ui.SinglePanel sc2ApiSinglePanel) {
        unit = tryGet(
                Ui.SinglePanel::getUnit, Ui.SinglePanel::hasUnit
        ).apply(sc2ApiSinglePanel).map(UnitInfo::from).orElseThrow(required("unit"));
    }

    public static SinglePanel from(Ui.SinglePanel sc2ApiSinglePanel) {
        require("sc2api single panel", sc2ApiSinglePanel);
        return new SinglePanel(sc2ApiSinglePanel);
    }

    public UnitInfo getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SinglePanel that = (SinglePanel) o;

        return unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        return unit.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
