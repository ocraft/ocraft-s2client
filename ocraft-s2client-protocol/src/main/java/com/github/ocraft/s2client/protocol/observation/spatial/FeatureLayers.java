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
package com.github.ocraft.s2client.protocol.observation.spatial;

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class FeatureLayers implements Serializable {

    private static final long serialVersionUID = -7575896249211517448L;

    private final ImageData heightMap;              // uint8. Terrain height. World space units of [-200, 200] encoded into [0, 255].
    private final ImageData visibilityMap;          // uint8. 0=Hidden, 1=Fogged, 2=Visible, 3=FullHidden
    private final ImageData creep;                  // 1-bit. Zerg creep.
    private final ImageData power;                  // 1-bit. Protoss power.
    private final ImageData playerId;               // uint8. Participants: [1, 15] Neutral: 16
    private final ImageData unitType;               // int32. Unique identifier for type of unit.
    private final ImageData selected;               // 1-bit. Selected units.
    private final ImageData unitHitPoints;          // int32.
    private final ImageData unitHitPointsRatio;     // uint8. Ratio of current health to max health. [0%, 100%] encoded into [0, 255].
    private final ImageData unitEnergy;             // int32.

    // since 3.17
    private final ImageData unitEnergyRatio;        // uint8. Ratio of current energy to max energy. [0%, 100%] encoded into [0, 255].
    private final ImageData unitShields;            // int32.

    // since 3.17
    private final ImageData unitShieldsRatio;       // uint8. Ratio of current shields to max shields. [0%, 100%] encoded into [0, 255].
    private final ImageData playerRelative;         // uint8. See "Alliance" enum in raw.proto. Range: [1, 4]
    private final ImageData unitDensityAa;          // uint8. Density of units overlapping a pixel, anti-aliased. [0.0, 16.0f] encoded into [0, 255].
    private final ImageData unitDensity;            // uint8. Count of units overlapping a pixel.

    // since 3.17
    private final ImageData effects;                // uint8. Visuals of persistent abilities. (eg. Psi storm)

    private FeatureLayers(Spatial.FeatureLayers sc2ApiFeatureLayers) {
        heightMap = tryGet(Spatial.FeatureLayers::getHeightMap, Spatial.FeatureLayers::hasHeightMap)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("height map"));

        visibilityMap = tryGet(Spatial.FeatureLayers::getVisibilityMap, Spatial.FeatureLayers::hasVisibilityMap)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("visibility map"));

        creep = tryGet(Spatial.FeatureLayers::getCreep, Spatial.FeatureLayers::hasCreep)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("creep"));

        power = tryGet(Spatial.FeatureLayers::getPower, Spatial.FeatureLayers::hasPower)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("power"));

        playerId = tryGet(Spatial.FeatureLayers::getPlayerId, Spatial.FeatureLayers::hasPlayerId)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("player id"));

        unitType = tryGet(Spatial.FeatureLayers::getUnitType, Spatial.FeatureLayers::hasUnitType)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("unit type"));

        selected = tryGet(Spatial.FeatureLayers::getSelected, Spatial.FeatureLayers::hasSelected)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("selected"));

        unitHitPoints = tryGet(Spatial.FeatureLayers::getUnitHitPoints, Spatial.FeatureLayers::hasUnitHitPoints)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("unit hit points"));

        unitHitPointsRatio = tryGet(
                Spatial.FeatureLayers::getUnitHitPointsRatio, Spatial.FeatureLayers::hasUnitHitPointsRatio
        ).apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("unit hit points ratio"));

        unitEnergy = tryGet(Spatial.FeatureLayers::getUnitEnergy, Spatial.FeatureLayers::hasUnitEnergy)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("unit energy"));

        unitEnergyRatio = tryGet(Spatial.FeatureLayers::getUnitEnergyRatio, Spatial.FeatureLayers::hasUnitEnergyRatio)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());

        unitShields = tryGet(Spatial.FeatureLayers::getUnitShields, Spatial.FeatureLayers::hasUnitShields)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("unit shields"));

        unitShieldsRatio = tryGet(
                Spatial.FeatureLayers::getUnitShieldsRatio, Spatial.FeatureLayers::hasUnitShieldsRatio
        ).apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());

        playerRelative = tryGet(Spatial.FeatureLayers::getPlayerRelative, Spatial.FeatureLayers::hasPlayerRelative)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("player relative"));

        unitDensityAa = tryGet(Spatial.FeatureLayers::getUnitDensityAa, Spatial.FeatureLayers::hasUnitDensityAa)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("unit density aa"));

        unitDensity = tryGet(Spatial.FeatureLayers::getUnitDensity, Spatial.FeatureLayers::hasUnitDensity)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElseThrow(required("unit density"));

        effects = tryGet(Spatial.FeatureLayers::getEffects, Spatial.FeatureLayers::hasEffects)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());
    }

    public static FeatureLayers from(Spatial.FeatureLayers sc2ApiFeatureLayers) {
        require("sc2api feature layers", sc2ApiFeatureLayers);
        return new FeatureLayers(sc2ApiFeatureLayers);
    }

    public ImageData getHeightMap() {
        return heightMap;
    }

    public ImageData getVisibilityMap() {
        return visibilityMap;
    }

    public ImageData getCreep() {
        return creep;
    }

    public ImageData getPower() {
        return power;
    }

    public ImageData getPlayerId() {
        return playerId;
    }

    public ImageData getUnitType() {
        return unitType;
    }

    public ImageData getSelected() {
        return selected;
    }

    public ImageData getUnitHitPoints() {
        return unitHitPoints;
    }

    public ImageData getUnitHitPointsRatio() {
        return unitHitPointsRatio;
    }

    public ImageData getUnitEnergy() {
        return unitEnergy;
    }

    public Optional<ImageData> getUnitEnergyRatio() {
        return Optional.ofNullable(unitEnergyRatio);
    }

    public ImageData getUnitShields() {
        return unitShields;
    }

    public Optional<ImageData> getUnitShieldsRatio() {
        return Optional.ofNullable(unitShieldsRatio);
    }

    public ImageData getPlayerRelative() {
        return playerRelative;
    }

    public ImageData getUnitDensityAa() {
        return unitDensityAa;
    }

    public ImageData getUnitDensity() {
        return unitDensity;
    }

    public Optional<ImageData> getEffects() {
        return Optional.ofNullable(effects);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureLayers that = (FeatureLayers) o;

        return heightMap.equals(that.heightMap) &&
                visibilityMap.equals(that.visibilityMap) &&
                creep.equals(that.creep) &&
                power.equals(that.power) &&
                playerId.equals(that.playerId) &&
                unitType.equals(that.unitType) &&
                selected.equals(that.selected) &&
                unitHitPoints.equals(that.unitHitPoints) &&
                unitHitPointsRatio.equals(that.unitHitPointsRatio) &&
                unitEnergy.equals(that.unitEnergy) &&
                (unitEnergyRatio != null
                        ? unitEnergyRatio.equals(that.unitEnergyRatio)
                        : that.unitEnergyRatio == null) &&
                unitShields.equals(that.unitShields) &&
                (unitShieldsRatio != null
                        ? unitShieldsRatio.equals(that.unitShieldsRatio)
                        : that.unitShieldsRatio == null) &&
                playerRelative.equals(that.playerRelative) &&
                unitDensityAa.equals(that.unitDensityAa) &&
                unitDensity.equals(that.unitDensity) &&
                (effects != null ? effects.equals(that.effects) : that.effects == null);
    }

    @Override
    public int hashCode() {
        int result = heightMap.hashCode();
        result = 31 * result + visibilityMap.hashCode();
        result = 31 * result + creep.hashCode();
        result = 31 * result + power.hashCode();
        result = 31 * result + playerId.hashCode();
        result = 31 * result + unitType.hashCode();
        result = 31 * result + selected.hashCode();
        result = 31 * result + unitHitPoints.hashCode();
        result = 31 * result + unitHitPointsRatio.hashCode();
        result = 31 * result + unitEnergy.hashCode();
        result = 31 * result + (unitEnergyRatio != null ? unitEnergyRatio.hashCode() : 0);
        result = 31 * result + unitShields.hashCode();
        result = 31 * result + (unitShieldsRatio != null ? unitShieldsRatio.hashCode() : 0);
        result = 31 * result + playerRelative.hashCode();
        result = 31 * result + unitDensityAa.hashCode();
        result = 31 * result + unitDensity.hashCode();
        result = 31 * result + (effects != null ? effects.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
