package com.github.ocraft.s2client.protocol.observation.spatial;

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

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class FeatureLayers implements Serializable {

    private static final long serialVersionUID = 7267352011530663671L;

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

    // since 4.84
    private final ImageData hallucinations;         // 1-bit. Whether the unit here is a hallucination.
    private final ImageData cloaked;                // 1-bit. Whether the unit here is cloaked. Hidden units will show up too, but with less details in other layers.
    private final ImageData blip;                   // 1-bit. Whether the unit here is a blip.
    private final ImageData buffs;                  // int32. One of the buffs applied to this unit. Extras are ignored.
    private final ImageData buffDuration;           // uint8. Ratio of buff remaining. [0%, 100%] encoded into [0, 255].
    private final ImageData active;                 // 1-bit. Whether the unit here is active.
    private final ImageData buildProgress;          // uint8. How far along the building is building something. [0%, 100%] encoded into [0, 255].
    private final ImageData buildable;              // 1-bit. Whether a building can be built here.
    private final ImageData pathable;               // 1-bit. Whether a unit can walk here.

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

        hallucinations = tryGet(Spatial.FeatureLayers::getHallucinations, Spatial.FeatureLayers::hasHallucinations)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());

        cloaked = tryGet(Spatial.FeatureLayers::getCloaked, Spatial.FeatureLayers::hasCloaked)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());

        blip = tryGet(Spatial.FeatureLayers::getBlip, Spatial.FeatureLayers::hasBlip)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());

        buffs = tryGet(Spatial.FeatureLayers::getBuffs, Spatial.FeatureLayers::hasBuffs)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());

        buffDuration = tryGet(Spatial.FeatureLayers::getBuffDuration, Spatial.FeatureLayers::hasBuffDuration)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());

        active = tryGet(Spatial.FeatureLayers::getActive, Spatial.FeatureLayers::hasActive)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());

        buildProgress = tryGet(Spatial.FeatureLayers::getBuildProgress, Spatial.FeatureLayers::hasBuildProgress)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());

        buildable = tryGet(Spatial.FeatureLayers::getBuildable, Spatial.FeatureLayers::hasBuildable)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());

        pathable = tryGet(Spatial.FeatureLayers::getPathable, Spatial.FeatureLayers::hasPathable)
                .apply(sc2ApiFeatureLayers).map(ImageData::from).orElse(nothing());
    }

    public static FeatureLayers from(Spatial.FeatureLayers sc2ApiFeatureLayers) {
        require("sc2api feature layers", sc2ApiFeatureLayers);
        return new FeatureLayers(sc2ApiFeatureLayers);
    }

    /**
     * uint8. Terrain height. World space units of [-200, 200] encoded into [0, 255].
     */
    public ImageData getHeightMap() {
        return heightMap;
    }

    /**
     * uint8. 0=Hidden, 1=Fogged, 2=Visible, 3=FullHidden
     */
    public ImageData getVisibilityMap() {
        return visibilityMap;
    }

    /**
     * 1-bit. Zerg creep.
     */
    public ImageData getCreep() {
        return creep;
    }

    /**
     * 1-bit. Protoss power.
     */
    public ImageData getPower() {
        return power;
    }

    /**
     * uint8. Participants: [1, 15] Neutral: 16
     */
    public ImageData getPlayerId() {
        return playerId;
    }

    /**
     * int32. Unique identifier for type of unit.
     */
    public ImageData getUnitType() {
        return unitType;
    }

    /**
     * 1-bit. Selected units.
     */
    public ImageData getSelected() {
        return selected;
    }

    /**
     * int32.
     */
    public ImageData getUnitHitPoints() {
        return unitHitPoints;
    }

    /**
     * uint8. Ratio of current health to max health. [0%, 100%] encoded into [0, 255].
     */
    public ImageData getUnitHitPointsRatio() {
        return unitHitPointsRatio;
    }

    /**
     * int32.
     */
    public ImageData getUnitEnergy() {
        return unitEnergy;
    }

    /**
     * uint8. Ratio of current energy to max energy. [0%, 100%] encoded into [0, 255].
     */
    public Optional<ImageData> getUnitEnergyRatio() {
        return Optional.ofNullable(unitEnergyRatio);
    }

    /**
     * int32.
     */
    public ImageData getUnitShields() {
        return unitShields;
    }

    /**
     * uint8. Ratio of current shields to max shields. [0%, 100%] encoded into [0, 255].
     */
    public Optional<ImageData> getUnitShieldsRatio() {
        return Optional.ofNullable(unitShieldsRatio);
    }

    /**
     * uint8. See "Alliance" enum in raw.proto. Range: [1, 4]
     */
    public ImageData getPlayerRelative() {
        return playerRelative;
    }

    /**
     * uint8. Density of units overlapping a pixel, anti-aliased. [0.0, 16.0f] encoded into [0, 255].
     */
    public ImageData getUnitDensityAa() {
        return unitDensityAa;
    }

    /**
     * uint8. Count of units overlapping a pixel.
     */
    public ImageData getUnitDensity() {
        return unitDensity;
    }

    /**
     * uint8. Visuals of persistent abilities. (eg. Psistorm)
     */
    public Optional<ImageData> getEffects() {
        return Optional.ofNullable(effects);
    }

    /**
     * 1-bit. Whether the unit here is a hallucination.
     */
    public Optional<ImageData> getHallucinations() {
        return Optional.ofNullable(hallucinations);
    }

    /**
     * 1-bit. Whether the unit here is cloaked. Hidden units will show up too, but with less details in other layers.
     */
    public Optional<ImageData> getCloaked() {
        return Optional.ofNullable(cloaked);
    }

    /**
     * 1-bit. Whether the unit here is a blip.
     */
    public Optional<ImageData> getBlip() {
        return Optional.ofNullable(blip);
    }

    /**
     * int32. One of the buffs applied to this unit. Extras are ignored.
     */
    public Optional<ImageData> getBuffs() {
        return Optional.ofNullable(buffs);
    }

    /**
     * uint8. Ratio of buff remaining. [0%, 100%] encoded into [0, 255].
     */
    public Optional<ImageData> getBuffDuration() {
        return Optional.ofNullable(buffDuration);
    }

    /**
     * 1-bit. Whether the unit here is active.
     */
    public Optional<ImageData> getActive() {
        return Optional.ofNullable(active);
    }

    /**
     * uint8. How far along the building is building something. [0%, 100%] encoded into [0, 255].
     */
    public Optional<ImageData> getBuildProgress() {
        return Optional.ofNullable(buildProgress);
    }

    /**
     * 1-bit. Whether a building can be built here.
     */
    public Optional<ImageData> getBuildable() {
        return Optional.ofNullable(buildable);
    }

    /**
     * 1-bit. Whether a unit can walk here.
     */
    public Optional<ImageData> getPathable() {
        return Optional.ofNullable(pathable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureLayers that = (FeatureLayers) o;

        if (!heightMap.equals(that.heightMap)) return false;
        if (!visibilityMap.equals(that.visibilityMap)) return false;
        if (!creep.equals(that.creep)) return false;
        if (!power.equals(that.power)) return false;
        if (!playerId.equals(that.playerId)) return false;
        if (!unitType.equals(that.unitType)) return false;
        if (!selected.equals(that.selected)) return false;
        if (!unitHitPoints.equals(that.unitHitPoints)) return false;
        if (!unitHitPointsRatio.equals(that.unitHitPointsRatio)) return false;
        if (!unitEnergy.equals(that.unitEnergy)) return false;
        if (!Objects.equals(unitEnergyRatio, that.unitEnergyRatio))
            return false;
        if (!Objects.equals(unitShields, that.unitShields)) return false;
        if (!Objects.equals(unitShieldsRatio, that.unitShieldsRatio))
            return false;
        if (!Objects.equals(playerRelative, that.playerRelative))
            return false;
        if (!Objects.equals(unitDensityAa, that.unitDensityAa))
            return false;
        if (!Objects.equals(unitDensity, that.unitDensity)) return false;
        if (!Objects.equals(effects, that.effects)) return false;
        if (!Objects.equals(hallucinations, that.hallucinations))
            return false;
        if (!Objects.equals(cloaked, that.cloaked)) return false;
        if (!Objects.equals(blip, that.blip)) return false;
        if (!Objects.equals(buffs, that.buffs)) return false;
        if (!Objects.equals(buffDuration, that.buffDuration)) return false;
        if (!Objects.equals(active, that.active)) return false;
        if (!Objects.equals(buildProgress, that.buildProgress))
            return false;
        if (!Objects.equals(buildable, that.buildable)) return false;
        return Objects.equals(pathable, that.pathable);
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
        result = 31 * result + (unitShields != null ? unitShields.hashCode() : 0);
        result = 31 * result + (unitShieldsRatio != null ? unitShieldsRatio.hashCode() : 0);
        result = 31 * result + (playerRelative != null ? playerRelative.hashCode() : 0);
        result = 31 * result + (unitDensityAa != null ? unitDensityAa.hashCode() : 0);
        result = 31 * result + (unitDensity != null ? unitDensity.hashCode() : 0);
        result = 31 * result + (effects != null ? effects.hashCode() : 0);
        result = 31 * result + (hallucinations != null ? hallucinations.hashCode() : 0);
        result = 31 * result + (cloaked != null ? cloaked.hashCode() : 0);
        result = 31 * result + (blip != null ? blip.hashCode() : 0);
        result = 31 * result + (buffs != null ? buffs.hashCode() : 0);
        result = 31 * result + (buffDuration != null ? buffDuration.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (buildProgress != null ? buildProgress.hashCode() : 0);
        result = 31 * result + (buildable != null ? buildable.hashCode() : 0);
        result = 31 * result + (pathable != null ? pathable.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
