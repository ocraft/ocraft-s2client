package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.unit.UnitInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toList;

public final class ProductionPanel implements Serializable {

    private static final long serialVersionUID = 4298757821756614388L;

    private final UnitInfo unit;
    private final List<UnitInfo> buildQueue;

    private ProductionPanel(Ui.ProductionPanel sc2ApiProductionPanel) {
        unit = tryGet(
                Ui.ProductionPanel::getUnit, Ui.ProductionPanel::hasUnit
        ).apply(sc2ApiProductionPanel).map(UnitInfo::from).orElseThrow(required("unit"));

        buildQueue = sc2ApiProductionPanel.getBuildQueueList().stream().map(UnitInfo::from).collect(toList());
    }

    public static ProductionPanel from(Ui.ProductionPanel sc2ApiProductionPanel) {
        require("sc2api production panel", sc2ApiProductionPanel);
        return new ProductionPanel(sc2ApiProductionPanel);
    }

    public UnitInfo getUnit() {
        return unit;
    }

    public List<UnitInfo> getBuildQueue() {
        return new ArrayList<>(buildQueue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductionPanel that = (ProductionPanel) o;

        return unit.equals(that.unit) && buildQueue.equals(that.buildQueue);
    }

    @Override
    public int hashCode() {
        int result = unit.hashCode();
        result = 31 * result + buildQueue.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
