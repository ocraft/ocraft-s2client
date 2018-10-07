package com.github.ocraft.s2client.protocol.observation.ui;

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
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.unit.UnitInfo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.collectingAndThen;
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

        passengers = sc2ApiCargoPanel.getPassengersList().stream().map(UnitInfo::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));

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
        return passengers;
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
