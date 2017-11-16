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

public final class CargoPanel implements Serializable {

    private static final long serialVersionUID = -2535078896585216326L;

    private final UnitInfo unit;
    private final List<UnitInfo> passengers;
    private final int cargoSize;

    private CargoPanel(Ui.CargoPanel sc2ApiCargoPanel) {
        unit = tryGet(
                Ui.CargoPanel::getUnit, Ui.CargoPanel::hasUnit
        ).apply(sc2ApiCargoPanel).map(UnitInfo::from).orElseThrow(required("unit"));

        passengers = sc2ApiCargoPanel.getPassengersList().stream().map(UnitInfo::from).collect(toList());

        cargoSize = tryGet(
                Ui.CargoPanel::getSlotsAvailable, Ui.CargoPanel::hasSlotsAvailable
        ).apply(sc2ApiCargoPanel).orElseThrow(required("cargo size"));
    }

    public static CargoPanel from(Ui.CargoPanel sc2ApiCargoPanel) {
        require("sc2api cargo panel", sc2ApiCargoPanel);
        return new CargoPanel(sc2ApiCargoPanel);
    }

    public UnitInfo getUnit() {
        return unit;
    }

    public List<UnitInfo> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public int getCargoSize() {
        return cargoSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CargoPanel that = (CargoPanel) o;

        return cargoSize == that.cargoSize && unit.equals(that.unit) && passengers.equals(that.passengers);
    }

    @Override
    public int hashCode() {
        int result = unit.hashCode();
        result = 31 * result + passengers.hashCode();
        result = 31 * result + cargoSize;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
