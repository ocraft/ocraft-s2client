package com.github.ocraft.s2client.protocol.data;

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

import SC2APIProtocol.Data;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.Race;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toSet;

public final class UnitTypeData implements Serializable {

    private static final long serialVersionUID = 5682626094549300576L;

    private final UnitType unitType;         // Stable ID.
    private final String name;               // Catalog name of the unit.
    private final boolean available;         // If true, the ability may be used by this set of mods/map.
    private final Integer cargoSize;         // Number of cargo slots it occupies in transports.
    private final Integer mineralCost;
    private final Integer vespeneCost;
    private final Float foodRequired;
    private final Float foodProvided;
    private final Ability ability;           // The ability that builds this unit.
    private final Race race;
    private final Float buildTime;
    private final boolean hasVespene;
    private final boolean hasMinerals;
    // since 4.0
    private final Float sightRange;          // Range unit reveals vision.

    private final Set<UnitType> techAliases; // Other units that satisfy the same tech requirement.
    private final UnitType unitAlias;        // The morphed variant of this unit.

    private final UnitType techRequirement;  // Structure required to build this unit. (Or any with the same tech_alias)
    private final boolean requireAttached;   // Whether tech_requirement is an add-on.

    // Values include changes from upgrades
    private final Set<UnitAttribute> attributes;
    private final Float movementSpeed;
    private final Float armor;
    private final Set<Weapon> weapons;

    private UnitTypeData(Data.UnitTypeData sc2ApiUnitTypeData) {
        unitType = tryGet(Data.UnitTypeData::getUnitId, Data.UnitTypeData::hasUnitId)
                .apply(sc2ApiUnitTypeData).map(Units::from).orElseThrow(required("unit type"));

        name = tryGet(Data.UnitTypeData::getName, Data.UnitTypeData::hasName)
                .apply(sc2ApiUnitTypeData).orElseThrow(required("name"));

        available = tryGet(Data.UnitTypeData::getAvailable, Data.UnitTypeData::hasAvailable)
                .apply(sc2ApiUnitTypeData).orElse(false);

        cargoSize = tryGet(Data.UnitTypeData::getCargoSize, Data.UnitTypeData::hasCargoSize)
                .apply(sc2ApiUnitTypeData).orElse(nothing());

        mineralCost = tryGet(Data.UnitTypeData::getMineralCost, Data.UnitTypeData::hasMineralCost)
                .apply(sc2ApiUnitTypeData).orElse(nothing());

        vespeneCost = tryGet(Data.UnitTypeData::getVespeneCost, Data.UnitTypeData::hasVespeneCost)
                .apply(sc2ApiUnitTypeData).orElse(nothing());

        foodRequired = tryGet(Data.UnitTypeData::getFoodRequired, Data.UnitTypeData::hasFoodRequired)
                .apply(sc2ApiUnitTypeData).orElse(nothing());

        foodProvided = tryGet(Data.UnitTypeData::getFoodProvided, Data.UnitTypeData::hasFoodProvided)
                .apply(sc2ApiUnitTypeData).orElse(nothing());

        ability = tryGet(Data.UnitTypeData::getAbilityId, Data.UnitTypeData::hasAbilityId)
                .apply(sc2ApiUnitTypeData).map(Abilities::from).orElse(nothing());

        race = tryGet(Data.UnitTypeData::getRace, Data.UnitTypeData::hasRace)
                .apply(sc2ApiUnitTypeData).map(Race::from).orElse(nothing());

        buildTime = tryGet(Data.UnitTypeData::getBuildTime, Data.UnitTypeData::hasBuildTime)
                .apply(sc2ApiUnitTypeData).orElse(nothing());

        hasVespene = tryGet(Data.UnitTypeData::getHasVespene, Data.UnitTypeData::hasHasVespene)
                .apply(sc2ApiUnitTypeData).orElse(false);

        hasMinerals = tryGet(Data.UnitTypeData::getHasVespene, Data.UnitTypeData::hasHasMinerals)
                .apply(sc2ApiUnitTypeData).orElse(false);

        sightRange = tryGet(Data.UnitTypeData::getSightRange, Data.UnitTypeData::hasSightRange)
                .apply(sc2ApiUnitTypeData).orElse(nothing());

        techAliases = sc2ApiUnitTypeData.getTechAliasList().stream().map(Units::from).collect(toSet());

        unitAlias = tryGet(Data.UnitTypeData::getUnitAlias, Data.UnitTypeData::hasUnitAlias)
                .apply(sc2ApiUnitTypeData).map(Units::from).orElse(nothing());

        techRequirement = tryGet(Data.UnitTypeData::getTechRequirement, Data.UnitTypeData::hasTechRequirement)
                .apply(sc2ApiUnitTypeData).map(Units::from).orElse(nothing());

        requireAttached = tryGet(Data.UnitTypeData::getRequireAttached, Data.UnitTypeData::hasRequireAttached)
                .apply(sc2ApiUnitTypeData).orElse(false);

        attributes = sc2ApiUnitTypeData.getAttributesList().stream().map(UnitAttribute::from).collect(toSet());

        movementSpeed = tryGet(Data.UnitTypeData::getMovementSpeed, Data.UnitTypeData::hasMovementSpeed)
                .apply(sc2ApiUnitTypeData).orElse(nothing());

        armor = tryGet(Data.UnitTypeData::getArmor, Data.UnitTypeData::hasArmor)
                .apply(sc2ApiUnitTypeData).orElse(nothing());

        weapons = sc2ApiUnitTypeData.getWeaponsList().stream().map(Weapon::from).collect(toSet());
    }

    public static UnitTypeData from(Data.UnitTypeData sc2ApiUnitTypeData) {
        require("sc2api unit type data", sc2ApiUnitTypeData);
        return new UnitTypeData(sc2ApiUnitTypeData);
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public Optional<Integer> getCargoSize() {
        return Optional.ofNullable(cargoSize);
    }

    public Optional<Integer> getMineralCost() {
        return Optional.ofNullable(mineralCost);
    }

    public Optional<Integer> getVespeneCost() {
        return Optional.ofNullable(vespeneCost);
    }

    public Optional<Float> getFoodRequired() {
        return Optional.ofNullable(foodRequired);
    }

    public Optional<Float> getFoodProvided() {
        return Optional.ofNullable(foodProvided);
    }

    public Optional<Ability> getAbility() {
        return Optional.ofNullable(ability);
    }

    public Optional<Race> getRace() {
        return Optional.ofNullable(race);
    }

    public Optional<Float> getBuildTime() {
        return Optional.ofNullable(buildTime);
    }

    public boolean isHasVespene() {
        return hasVespene;
    }

    public boolean isHasMinerals() {
        return hasMinerals;
    }

    public Optional<Float> getSightRange() {
        return Optional.ofNullable(sightRange);
    }

    public Set<UnitType> getTechAliases() {
        return new HashSet<>(techAliases);
    }

    public Optional<UnitType> getUnitAlias() {
        return Optional.ofNullable(unitAlias);
    }

    public Optional<UnitType> getTechRequirement() {
        return Optional.ofNullable(techRequirement);
    }

    public boolean isRequireAttached() {
        return requireAttached;
    }

    public Set<UnitAttribute> getAttributes() {
        return new HashSet<>(attributes);
    }

    public Optional<Float> getMovementSpeed() {
        return Optional.ofNullable(movementSpeed);
    }

    public Optional<Float> getArmor() {
        return Optional.ofNullable(armor);
    }

    public Set<Weapon> getWeapons() {
        return new HashSet<>(weapons);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitTypeData that = (UnitTypeData) o;

        return available == that.available &&
                hasVespene == that.hasVespene &&
                hasMinerals == that.hasMinerals &&
                requireAttached == that.requireAttached &&
                unitType == that.unitType &&
                name.equals(that.name) &&
                (cargoSize != null ? cargoSize.equals(that.cargoSize) : that.cargoSize == null) &&
                (mineralCost != null ? mineralCost.equals(that.mineralCost) : that.mineralCost == null) &&
                (vespeneCost != null ? vespeneCost.equals(that.vespeneCost) : that.vespeneCost == null) &&
                (foodRequired != null ? foodRequired.equals(that.foodRequired) : that.foodRequired == null) &&
                (foodProvided != null ? foodProvided.equals(that.foodProvided) : that.foodProvided == null) &&
                (sightRange != null ? sightRange.equals(that.sightRange) : that.sightRange == null) &&
                ability == that.ability &&
                race == that.race &&
                (buildTime != null ? buildTime.equals(that.buildTime) : that.buildTime == null) &&
                techAliases.equals(that.techAliases) &&
                unitAlias == that.unitAlias &&
                techRequirement == that.techRequirement &&
                attributes.equals(that.attributes) &&
                (movementSpeed != null ? movementSpeed.equals(that.movementSpeed) : that.movementSpeed == null) &&
                (armor != null ? armor.equals(that.armor) : that.armor == null) &&
                weapons.equals(that.weapons);
    }

    @Override
    public int hashCode() {
        int result = unitType.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (available ? 1 : 0);
        result = 31 * result + (cargoSize != null ? cargoSize.hashCode() : 0);
        result = 31 * result + (mineralCost != null ? mineralCost.hashCode() : 0);
        result = 31 * result + (vespeneCost != null ? vespeneCost.hashCode() : 0);
        result = 31 * result + (foodRequired != null ? foodRequired.hashCode() : 0);
        result = 31 * result + (foodProvided != null ? foodProvided.hashCode() : 0);
        result = 31 * result + (ability != null ? ability.hashCode() : 0);
        result = 31 * result + (race != null ? race.hashCode() : 0);
        result = 31 * result + (buildTime != null ? buildTime.hashCode() : 0);
        result = 31 * result + (hasVespene ? 1 : 0);
        result = 31 * result + (hasMinerals ? 1 : 0);
        result = 31 * result + (sightRange != null ? sightRange.hashCode() : 0);
        result = 31 * result + techAliases.hashCode();
        result = 31 * result + (unitAlias != null ? unitAlias.hashCode() : 0);
        result = 31 * result + (techRequirement != null ? techRequirement.hashCode() : 0);
        result = 31 * result + (requireAttached ? 1 : 0);
        result = 31 * result + attributes.hashCode();
        result = 31 * result + (movementSpeed != null ? movementSpeed.hashCode() : 0);
        result = 31 * result + (armor != null ? armor.hashCode() : 0);
        result = 31 * result + weapons.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
