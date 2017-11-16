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
package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class UpgradeData implements Serializable {

    private static final long serialVersionUID = -2163565534750216972L;

    private final Upgrade upgrade;
    private final String name;
    private final Integer mineralCost;
    private final Integer vespeneCost;
    private final Float researchTime;
    private final Ability ability;

    private UpgradeData(Data.UpgradeData sc2ApiUpgradeData) {
        upgrade = tryGet(Data.UpgradeData::getUpgradeId, Data.UpgradeData::hasUpgradeId)
                .apply(sc2ApiUpgradeData).map(Upgrades::from).orElseThrow(required("upgrade"));

        name = tryGet(Data.UpgradeData::getName, Data.UpgradeData::hasName)
                .apply(sc2ApiUpgradeData).orElseThrow(required("name"));

        mineralCost = tryGet(Data.UpgradeData::getMineralCost, Data.UpgradeData::hasMineralCost)
                .apply(sc2ApiUpgradeData).orElse(nothing());

        vespeneCost = tryGet(Data.UpgradeData::getVespeneCost, Data.UpgradeData::hasVespeneCost)
                .apply(sc2ApiUpgradeData).orElse(nothing());

        researchTime = tryGet(Data.UpgradeData::getResearchTime, Data.UpgradeData::hasResearchTime)
                .apply(sc2ApiUpgradeData).orElse(nothing());

        ability = tryGet(Data.UpgradeData::getAbilityId, Data.UpgradeData::hasAbilityId)
                .apply(sc2ApiUpgradeData).map(Abilities::from).orElse(nothing());
    }

    public static UpgradeData from(Data.UpgradeData sc2ApiUpgradeData) {
        require("sc2api upgrade data", sc2ApiUpgradeData);
        return new UpgradeData(sc2ApiUpgradeData);
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }

    public String getName() {
        return name;
    }

    public Optional<Integer> getMineralCost() {
        return Optional.ofNullable(mineralCost);
    }

    public Optional<Integer> getVespeneCost() {
        return Optional.ofNullable(vespeneCost);
    }

    public Optional<Float> getResearchTime() {
        return Optional.ofNullable(researchTime);
    }

    public Optional<Ability> getAbility() {
        return Optional.ofNullable(ability);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpgradeData that = (UpgradeData) o;

        return upgrade == that.upgrade &&
                name.equals(that.name) &&
                (mineralCost != null ? mineralCost.equals(that.mineralCost) : that.mineralCost == null) &&
                (vespeneCost != null ? vespeneCost.equals(that.vespeneCost) : that.vespeneCost == null) &&
                (researchTime != null ? researchTime.equals(that.researchTime) : that.researchTime == null) &&
                ability == that.ability;
    }

    @Override
    public int hashCode() {
        int result = upgrade.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (mineralCost != null ? mineralCost.hashCode() : 0);
        result = 31 * result + (vespeneCost != null ? vespeneCost.hashCode() : 0);
        result = 31 * result + (researchTime != null ? researchTime.hashCode() : 0);
        result = 31 * result + (ability != null ? ability.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
