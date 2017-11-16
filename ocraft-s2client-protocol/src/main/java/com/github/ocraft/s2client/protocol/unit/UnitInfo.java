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
package com.github.ocraft.s2client.protocol.unit;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class UnitInfo implements Serializable {

    private static final long serialVersionUID = -833743333125242491L;

    private final UnitType unitType;
    private final Alliance playerRelative;
    private final Integer health;
    private final Integer shields;
    private final Integer energy;
    private final Integer transportSlotsTaken;
    private final Float buildProgress;    // Range: [0.0, 1.0]
    private final UnitInfo addOn;

    private UnitInfo(Ui.UnitInfo sc2ApiUnitInfo) {
        unitType = tryGet(
                Ui.UnitInfo::getUnitType, Ui.UnitInfo::hasUnitType
        ).apply(sc2ApiUnitInfo).map(Units::from).orElseThrow(required("unit type"));

        playerRelative = tryGet(
                Ui.UnitInfo::getPlayerRelative, Ui.UnitInfo::hasPlayerRelative
        ).apply(sc2ApiUnitInfo).map(Alliance::from).orElse(nothing());

        health = tryGet(
                Ui.UnitInfo::getHealth, Ui.UnitInfo::hasHealth
        ).apply(sc2ApiUnitInfo).orElse(nothing());

        shields = tryGet(
                Ui.UnitInfo::getShields, Ui.UnitInfo::hasShields
        ).apply(sc2ApiUnitInfo).orElse(nothing());

        energy = tryGet(
                Ui.UnitInfo::getEnergy, Ui.UnitInfo::hasEnergy
        ).apply(sc2ApiUnitInfo).orElse(nothing());

        transportSlotsTaken = tryGet(
                Ui.UnitInfo::getTransportSlotsTaken, Ui.UnitInfo::hasTransportSlotsTaken
        ).apply(sc2ApiUnitInfo).orElse(nothing());

        buildProgress = tryGet(
                Ui.UnitInfo::getBuildProgress, Ui.UnitInfo::hasBuildProgress
        ).apply(sc2ApiUnitInfo).orElse(nothing());

        addOn = tryGet(
                Ui.UnitInfo::getAddOn, Ui.UnitInfo::hasAddOn
        ).apply(sc2ApiUnitInfo).map(UnitInfo::from).orElse(nothing());
    }

    public static UnitInfo from(Ui.UnitInfo sc2ApiUnitInfo) {
        require("sc2api unit info", sc2ApiUnitInfo);
        return new UnitInfo(sc2ApiUnitInfo);
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public Optional<Alliance> getPlayerRelative() {
        return Optional.ofNullable(playerRelative);
    }

    public Optional<Integer> getHealth() {
        return Optional.ofNullable(health);
    }

    public Optional<Integer> getShields() {
        return Optional.ofNullable(shields);
    }

    public Optional<Integer> getEnergy() {
        return Optional.ofNullable(energy);
    }

    public Optional<Integer> getTransportSlotsTaken() {
        return Optional.ofNullable(transportSlotsTaken);
    }

    public Optional<Float> getBuildProgress() {
        return Optional.ofNullable(buildProgress);
    }

    public Optional<UnitInfo> getAddOn() {
        return Optional.ofNullable(addOn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitInfo unitInfo = (UnitInfo) o;

        return unitType == unitInfo.unitType &&
                playerRelative == unitInfo.playerRelative &&
                (health != null ? health.equals(unitInfo.health) : unitInfo.health == null) &&
                (shields != null ? shields.equals(unitInfo.shields) : unitInfo.shields == null) &&
                (energy != null ? energy.equals(unitInfo.energy) : unitInfo.energy == null) &&
                (transportSlotsTaken != null
                        ? transportSlotsTaken.equals(unitInfo.transportSlotsTaken)
                        : unitInfo.transportSlotsTaken == null) &&
                (buildProgress != null
                        ? buildProgress.equals(unitInfo.buildProgress)
                        : unitInfo.buildProgress == null) &&
                (addOn != null ? addOn.equals(unitInfo.addOn) : unitInfo.addOn == null);
    }

    @Override
    public int hashCode() {
        int result = unitType.hashCode();
        result = 31 * result + (playerRelative != null ? playerRelative.hashCode() : 0);
        result = 31 * result + (health != null ? health.hashCode() : 0);
        result = 31 * result + (shields != null ? shields.hashCode() : 0);
        result = 31 * result + (energy != null ? energy.hashCode() : 0);
        result = 31 * result + (transportSlotsTaken != null ? transportSlotsTaken.hashCode() : 0);
        result = 31 * result + (buildProgress != null ? buildProgress.hashCode() : 0);
        result = 31 * result + (addOn != null ? addOn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
