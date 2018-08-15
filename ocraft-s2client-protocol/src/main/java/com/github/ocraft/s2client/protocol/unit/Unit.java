package com.github.ocraft.s2client.protocol.unit;

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

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.GeneralizableAbility;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.*;
import com.github.ocraft.s2client.protocol.spatial.Point;

import java.io.Serializable;
import java.util.*;
import java.util.function.UnaryOperator;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public final class Unit implements Serializable, GeneralizableAbility<Unit> {

    private static final long serialVersionUID = 7550391852510629664L;

    private static final Boolean DEFAULT_ON_SCREEN = false;
    private static final Boolean DEFAULT_BLIP = false;

    private final DisplayType displayType;
    private final Alliance alliance;
    private final Tag tag;
    private final UnitType type;
    private final int owner;
    private final Point position;
    private final float facing;
    private final float radius;
    private final float buildProgress;  // Range: [0.0, 1.0]
    private final CloakState cloakState;
    private final Float detectRange;
    private final Float radarRange;
    private final Boolean selected;
    private final boolean onScreen;     // Visible and within the camera frustrum.
    private final boolean blip;         // Detected by sensor tower
    private final Boolean powered;

    // Not populated for snapshots
    private final Float health;
    private final Float healthMax;
    private final Float shield;
    private final Float shieldMax;
    private final Float energy;
    private final Float energyMax;
    private final Integer mineralContents;
    private final Integer vespeneContents;
    private final Boolean flying;
    private final Boolean burrowed;

    // Not populated for enemies
    private final List<UnitOrder> orders;
    private final Tag addOnTag;
    private final List<PassengerUnit> passengers;
    private final Integer cargoSpaceTaken;
    private final Integer cargoSpaceMax;
    private final Set<Buff> buffs;
    private final Integer assignedHarvesters;
    private final Integer idealHarvesters;
    private final Float weaponCooldown;
    private final Tag engagedTargetTag;

    private Unit(Raw.Unit sc2ApiUnit) {
        displayType = tryGet(Raw.Unit::getDisplayType, Raw.Unit::hasDisplayType)
                .apply(sc2ApiUnit).map(DisplayType::from).orElseThrow(required("display type"));

        alliance = tryGet(Raw.Unit::getAlliance, Raw.Unit::hasAlliance)
                .apply(sc2ApiUnit).map(Alliance::from).orElseThrow(required("alliance"));

        tag = tryGet(Raw.Unit::getTag, Raw.Unit::hasTag).apply(sc2ApiUnit).map(Tag::from).orElseThrow(required("tag"));

        type = tryGet(Raw.Unit::getUnitType, Raw.Unit::hasUnitType)
                .apply(sc2ApiUnit).map(Units::from).orElseThrow(required("unit type"));

        owner = tryGet(Raw.Unit::getOwner, Raw.Unit::hasOwner).apply(sc2ApiUnit).orElseThrow(required("owner"));
        position = tryGet(Raw.Unit::getPos, Raw.Unit::hasPos)
                .apply(sc2ApiUnit).map(Point::from).orElseThrow(required("position"));

        facing = tryGet(Raw.Unit::getFacing, Raw.Unit::hasFacing).apply(sc2ApiUnit).orElseThrow(required("facing"));

        radius = tryGet(Raw.Unit::getRadius, Raw.Unit::hasRadius).apply(sc2ApiUnit).orElseThrow(required("radius"));

        buildProgress = tryGet(Raw.Unit::getBuildProgress, Raw.Unit::hasBuildProgress)
                .apply(sc2ApiUnit).orElseThrow(required("build progress"));

        cloakState = tryGet(Raw.Unit::getCloak, Raw.Unit::hasCloak)
                .apply(sc2ApiUnit).map(CloakState::from).orElse(nothing());

        detectRange = tryGet(Raw.Unit::getDetectRange, Raw.Unit::hasDetectRange).apply(sc2ApiUnit).orElse(nothing());

        radarRange = tryGet(Raw.Unit::getRadarRange, Raw.Unit::hasRadarRange).apply(sc2ApiUnit).orElse(nothing());

        selected = tryGet(Raw.Unit::getIsSelected, Raw.Unit::hasIsSelected).apply(sc2ApiUnit).orElse(nothing());

        onScreen = tryGet(Raw.Unit::getIsOnScreen, Raw.Unit::hasIsOnScreen).apply(sc2ApiUnit).orElse(DEFAULT_ON_SCREEN);

        blip = tryGet(Raw.Unit::getIsBlip, Raw.Unit::hasIsBlip).apply(sc2ApiUnit).orElse(DEFAULT_BLIP);

        powered = tryGet(Raw.Unit::getIsPowered, Raw.Unit::hasIsPowered).apply(sc2ApiUnit).orElse(nothing());

        health = tryGet(Raw.Unit::getHealth, Raw.Unit::hasHealth).apply(sc2ApiUnit).orElse(nothing());

        healthMax = tryGet(Raw.Unit::getHealthMax, Raw.Unit::hasHealthMax).apply(sc2ApiUnit).orElse(nothing());

        shield = tryGet(Raw.Unit::getShield, Raw.Unit::hasShield).apply(sc2ApiUnit).orElse(nothing());

        shieldMax = tryGet(Raw.Unit::getShieldMax, Raw.Unit::hasShieldMax).apply(sc2ApiUnit).orElse(nothing());

        energy = tryGet(Raw.Unit::getEnergy, Raw.Unit::hasEnergy).apply(sc2ApiUnit).orElse(nothing());

        energyMax = tryGet(Raw.Unit::getEnergyMax, Raw.Unit::hasEnergyMax).apply(sc2ApiUnit).orElse(nothing());

        mineralContents = tryGet(Raw.Unit::getMineralContents, Raw.Unit::hasMineralContents)
                .apply(sc2ApiUnit).orElse(nothing());

        vespeneContents = tryGet(Raw.Unit::getVespeneContents, Raw.Unit::hasVespeneContents)
                .apply(sc2ApiUnit).orElse(nothing());

        flying = tryGet(Raw.Unit::getIsFlying, Raw.Unit::hasIsFlying).apply(sc2ApiUnit).orElse(nothing());

        burrowed = tryGet(Raw.Unit::getIsBurrowed, Raw.Unit::hasIsBurrowed).apply(sc2ApiUnit).orElse(nothing());

        orders = sc2ApiUnit.getOrdersList().stream().map(UnitOrder::from).collect(toList());

        addOnTag = tryGet(Raw.Unit::getAddOnTag, Raw.Unit::hasAddOnTag)
                .apply(sc2ApiUnit).map(Tag::from).orElse(nothing());

        passengers = sc2ApiUnit.getPassengersList().stream().map(PassengerUnit::from).collect(toList());

        cargoSpaceTaken = tryGet(Raw.Unit::getCargoSpaceTaken, Raw.Unit::hasCargoSpaceTaken)
                .apply(sc2ApiUnit).orElse(nothing());

        cargoSpaceMax = tryGet(Raw.Unit::getCargoSpaceMax, Raw.Unit::hasCargoSpaceMax)
                .apply(sc2ApiUnit).orElse(nothing());

        buffs = sc2ApiUnit.getBuffIdsList().stream().map(Buffs::from).collect(toSet());

        assignedHarvesters = tryGet(Raw.Unit::getAssignedHarvesters, Raw.Unit::hasAssignedHarvesters)
                .apply(sc2ApiUnit).orElse(nothing());

        idealHarvesters = tryGet(Raw.Unit::getIdealHarvesters, Raw.Unit::hasIdealHarvesters)
                .apply(sc2ApiUnit).orElse(nothing());

        weaponCooldown = tryGet(Raw.Unit::getWeaponCooldown, Raw.Unit::hasWeaponCooldown)
                .apply(sc2ApiUnit).orElse(nothing());

        engagedTargetTag = tryGet(Raw.Unit::getEngagedTargetTag, Raw.Unit::hasEngagedTargetTag)
                .apply(sc2ApiUnit).map(Tag::from).orElse(nothing());
    }

    private Unit(Unit original, UnaryOperator<Ability> generalize) {
        this.displayType = original.displayType;
        this.alliance = original.alliance;
        this.tag = original.tag;
        this.type = original.type;
        this.owner = original.owner;
        this.position = original.position;
        this.facing = original.facing;
        this.radius = original.radius;
        this.buildProgress = original.buildProgress;
        this.cloakState = original.cloakState;
        this.detectRange = original.detectRange;
        this.radarRange = original.radarRange;
        this.selected = original.selected;
        this.onScreen = original.onScreen;
        this.blip = original.blip;
        this.powered = original.powered;
        this.health = original.health;
        this.healthMax = original.healthMax;
        this.shield = original.shield;
        this.shieldMax = original.shieldMax;
        this.energy = original.energy;
        this.energyMax = original.energyMax;
        this.mineralContents = original.mineralContents;
        this.vespeneContents = original.vespeneContents;
        this.flying = original.flying;
        this.burrowed = original.burrowed;
        this.orders = original.orders.stream().map(order -> order.generalizeAbility(generalize)).collect(toList());
        this.addOnTag = original.addOnTag;
        this.passengers = original.passengers;
        this.cargoSpaceTaken = original.cargoSpaceTaken;
        this.cargoSpaceMax = original.cargoSpaceMax;
        this.buffs = original.buffs;
        this.assignedHarvesters = original.assignedHarvesters;
        this.idealHarvesters = original.idealHarvesters;
        this.weaponCooldown = original.weaponCooldown;
        this.engagedTargetTag = original.engagedTargetTag;
    }

    public static Unit from(Raw.Unit sc2ApiUnit) {
        require("sc2api unit", sc2ApiUnit);
        return new Unit(sc2ApiUnit);
    }

    public DisplayType getDisplayType() {
        return displayType;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public Tag getTag() {
        return tag;
    }

    public UnitType getType() {
        return type;
    }

    public int getOwner() {
        return owner;
    }

    public Point getPosition() {
        return position;
    }

    public float getFacing() {
        return facing;
    }

    public float getRadius() {
        return radius;
    }

    public float getBuildProgress() {
        return buildProgress;
    }

    public Optional<CloakState> getCloakState() {
        return Optional.ofNullable(cloakState);
    }

    public Optional<Float> getDetectRange() {
        return Optional.ofNullable(detectRange);
    }

    public Optional<Float> getRadarRange() {
        return Optional.ofNullable(radarRange);
    }

    public Optional<Boolean> getSelected() {
        return Optional.ofNullable(selected);
    }

    public boolean isOnScreen() {
        return onScreen;
    }

    public boolean isBlip() {
        return blip;
    }

    public Optional<Boolean> getPowered() {
        return Optional.ofNullable(powered);
    }

    public Optional<Float> getHealth() {
        return Optional.ofNullable(health);
    }

    public Optional<Float> getHealthMax() {
        return Optional.ofNullable(healthMax);
    }

    public Optional<Float> getShield() {
        return Optional.ofNullable(shield);
    }

    public Optional<Float> getShieldMax() {
        return Optional.ofNullable(shieldMax);
    }

    public Optional<Float> getEnergy() {
        return Optional.ofNullable(energy);
    }

    public Optional<Float> getEnergyMax() {
        return Optional.ofNullable(energyMax);
    }

    public Optional<Integer> getMineralContents() {
        return Optional.ofNullable(mineralContents);
    }

    public Optional<Integer> getVespeneContents() {
        return Optional.ofNullable(vespeneContents);
    }

    public Optional<Boolean> getFlying() {
        return Optional.ofNullable(flying);
    }

    public Optional<Boolean> getBurrowed() {
        return Optional.ofNullable(burrowed);
    }

    public List<UnitOrder> getOrders() {
        return new ArrayList<>(orders);
    }

    public Optional<Tag> getAddOnTag() {
        return Optional.ofNullable(addOnTag);
    }

    public List<PassengerUnit> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public Optional<Integer> getCargoSpaceTaken() {
        return Optional.ofNullable(cargoSpaceTaken);
    }

    public Optional<Integer> getCargoSpaceMax() {
        return Optional.ofNullable(cargoSpaceMax);
    }

    public Set<Buff> getBuffs() {
        return new HashSet<>(buffs);
    }

    public Optional<Integer> getAssignedHarvesters() {
        return Optional.ofNullable(assignedHarvesters);
    }

    public Optional<Integer> getIdealHarvesters() {
        return Optional.ofNullable(idealHarvesters);
    }

    public Optional<Float> getWeaponCooldown() {
        return Optional.ofNullable(weaponCooldown);
    }

    public Optional<Tag> getEngagedTargetTag() {
        return Optional.ofNullable(engagedTargetTag);
    }

    @Override
    public Unit generalizeAbility(UnaryOperator<Ability> generalize) {
        return new Unit(this, generalize);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unit unit = (Unit) o;

        return owner == unit.owner &&
                Float.compare(unit.facing, facing) == 0 &&
                Float.compare(unit.radius, radius) == 0 &&
                Float.compare(unit.buildProgress, buildProgress) == 0 &&
                onScreen == unit.onScreen &&
                blip == unit.blip &&
                displayType == unit.displayType &&
                alliance == unit.alliance &&
                tag.equals(unit.tag) &&
                type == unit.type &&
                position.equals(unit.position) &&
                cloakState == unit.cloakState &&
                (detectRange != null ? detectRange.equals(unit.detectRange) : unit.detectRange == null) &&
                (radarRange != null ? radarRange.equals(unit.radarRange) : unit.radarRange == null) &&
                (selected != null ? selected.equals(unit.selected) : unit.selected == null) &&
                (powered != null ? powered.equals(unit.powered) : unit.powered == null) &&
                (health != null ? health.equals(unit.health) : unit.health == null) &&
                (healthMax != null ? healthMax.equals(unit.healthMax) : unit.healthMax == null) &&
                (shield != null ? shield.equals(unit.shield) : unit.shield == null) &&
                (shieldMax != null ? shieldMax.equals(unit.shieldMax) : unit.shieldMax == null) &&
                (energy != null ? energy.equals(unit.energy) : unit.energy == null) &&
                (energyMax != null ? energyMax.equals(unit.energyMax) : unit.energyMax == null) &&
                (mineralContents != null
                        ? mineralContents.equals(unit.mineralContents)
                        : unit.mineralContents == null) &&
                (vespeneContents != null
                        ? vespeneContents.equals(unit.vespeneContents)
                        : unit.vespeneContents == null) &&
                (flying != null ? flying.equals(unit.flying) : unit.flying == null) &&
                (burrowed != null ? burrowed.equals(unit.burrowed) : unit.burrowed == null) &&
                orders.equals(unit.orders) &&
                (addOnTag != null ? addOnTag.equals(unit.addOnTag) : unit.addOnTag == null) &&
                passengers.equals(unit.passengers) &&
                (cargoSpaceTaken != null
                        ? cargoSpaceTaken.equals(unit.cargoSpaceTaken)
                        : unit.cargoSpaceTaken == null) &&
                (cargoSpaceMax != null ? cargoSpaceMax.equals(unit.cargoSpaceMax) : unit.cargoSpaceMax == null) &&
                buffs.equals(unit.buffs) &&
                (assignedHarvesters != null
                        ? assignedHarvesters.equals(unit.assignedHarvesters)
                        : unit.assignedHarvesters == null) &&
                (idealHarvesters != null
                        ? idealHarvesters.equals(unit.idealHarvesters)
                        : unit.idealHarvesters == null) &&
                (weaponCooldown != null
                        ? weaponCooldown.equals(unit.weaponCooldown)
                        : unit.weaponCooldown == null) &&
                (engagedTargetTag != null
                        ? engagedTargetTag.equals(unit.engagedTargetTag)
                        : unit.engagedTargetTag == null);
    }

    @Override
    public int hashCode() {
        int result = displayType.hashCode();
        result = 31 * result + alliance.hashCode();
        result = 31 * result + tag.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + owner;
        result = 31 * result + position.hashCode();
        result = 31 * result + (facing != +0.0f ? Float.floatToIntBits(facing) : 0);
        result = 31 * result + (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
        result = 31 * result + (buildProgress != +0.0f ? Float.floatToIntBits(buildProgress) : 0);
        result = 31 * result + (cloakState != null ? cloakState.hashCode() : 0);
        result = 31 * result + (detectRange != null ? detectRange.hashCode() : 0);
        result = 31 * result + (radarRange != null ? radarRange.hashCode() : 0);
        result = 31 * result + (selected != null ? selected.hashCode() : 0);
        result = 31 * result + (onScreen ? 1 : 0);
        result = 31 * result + (blip ? 1 : 0);
        result = 31 * result + (powered != null ? powered.hashCode() : 0);
        result = 31 * result + (health != null ? health.hashCode() : 0);
        result = 31 * result + (healthMax != null ? healthMax.hashCode() : 0);
        result = 31 * result + (shield != null ? shield.hashCode() : 0);
        result = 31 * result + (shieldMax != null ? shieldMax.hashCode() : 0);
        result = 31 * result + (energy != null ? energy.hashCode() : 0);
        result = 31 * result + (energyMax != null ? energyMax.hashCode() : 0);
        result = 31 * result + (mineralContents != null ? mineralContents.hashCode() : 0);
        result = 31 * result + (vespeneContents != null ? vespeneContents.hashCode() : 0);
        result = 31 * result + (flying != null ? flying.hashCode() : 0);
        result = 31 * result + (burrowed != null ? burrowed.hashCode() : 0);
        result = 31 * result + orders.hashCode();
        result = 31 * result + (addOnTag != null ? addOnTag.hashCode() : 0);
        result = 31 * result + passengers.hashCode();
        result = 31 * result + (cargoSpaceTaken != null ? cargoSpaceTaken.hashCode() : 0);
        result = 31 * result + (cargoSpaceMax != null ? cargoSpaceMax.hashCode() : 0);
        result = 31 * result + buffs.hashCode();
        result = 31 * result + (assignedHarvesters != null ? assignedHarvesters.hashCode() : 0);
        result = 31 * result + (idealHarvesters != null ? idealHarvesters.hashCode() : 0);
        result = 31 * result + (weaponCooldown != null ? weaponCooldown.hashCode() : 0);
        result = 31 * result + (engagedTargetTag != null ? engagedTargetTag.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
