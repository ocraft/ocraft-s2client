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
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.data.Buff;
import com.github.ocraft.s2client.protocol.data.Buffs;
import com.github.ocraft.s2client.protocol.spatial.Point;

import java.io.Serializable;
import java.util.*;
import java.util.function.UnaryOperator;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.*;

public class UnitSnapshot implements Serializable, GeneralizableAbility<UnitSnapshot> {

    private static final long serialVersionUID = 1274479237806224281L;

    private static final Boolean DEFAULT_ON_SCREEN = false;
    private static final Boolean DEFAULT_BLIP = false;

    private final DisplayType displayType;
    private final Alliance alliance;
    private final Point position;
    private final CloakState cloakState;
    private final Float detectRange;
    private final Float radarRange;
    private final Boolean selected;
    private final boolean onScreen;
    private final boolean blip;
    private final Boolean powered;
    private final Set<Buff> buffs;
    private final Boolean active;
    private final Integer attackUpgradeLevel;
    private final Integer armorUpgradeLevel;
    private final Integer shieldUpgradeLevel;

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
    private final Boolean hallucination;

    // Not populated for enemies
    private final List<UnitOrder> orders;
    private final Tag addOnTag;
    private final List<PassengerUnit> passengers;
    private final Integer cargoSpaceTaken;
    private final Integer cargoSpaceMax;
    private final Integer assignedHarvesters;
    private final Integer idealHarvesters;
    private final Float weaponCooldown;
    private final Tag engagedTargetTag;
    private final Integer buffDurationRemain;
    private final Integer buffDurationMax;

    UnitSnapshot(Raw.Unit sc2ApiUnit) {
        displayType = tryGet(Raw.Unit::getDisplayType, Raw.Unit::hasDisplayType)
                .apply(sc2ApiUnit).map(DisplayType::from).orElseThrow(required("display type"));

        alliance = tryGet(Raw.Unit::getAlliance, Raw.Unit::hasAlliance)
                .apply(sc2ApiUnit).map(Alliance::from).orElseThrow(required("alliance"));

        position = tryGet(Raw.Unit::getPos, Raw.Unit::hasPos)
                .apply(sc2ApiUnit).map(Point::from).orElseThrow(required("position"));

        cloakState = tryGet(Raw.Unit::getCloak, Raw.Unit::hasCloak)
                .apply(sc2ApiUnit).map(CloakState::from).orElse(nothing());

        detectRange = tryGet(Raw.Unit::getDetectRange, Raw.Unit::hasDetectRange).apply(sc2ApiUnit).orElse(nothing());

        radarRange = tryGet(Raw.Unit::getRadarRange, Raw.Unit::hasRadarRange).apply(sc2ApiUnit).orElse(nothing());

        selected = tryGet(Raw.Unit::getIsSelected, Raw.Unit::hasIsSelected).apply(sc2ApiUnit).orElse(nothing());

        onScreen = tryGet(Raw.Unit::getIsOnScreen, Raw.Unit::hasIsOnScreen).apply(sc2ApiUnit).orElse(DEFAULT_ON_SCREEN);

        blip = tryGet(Raw.Unit::getIsBlip, Raw.Unit::hasIsBlip).apply(sc2ApiUnit).orElse(DEFAULT_BLIP);

        powered = tryGet(Raw.Unit::getIsPowered, Raw.Unit::hasIsPowered).apply(sc2ApiUnit).orElse(nothing());

        buffs = sc2ApiUnit.getBuffIdsList().stream().map(Buffs::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));

        active = tryGet(Raw.Unit::getIsActive, Raw.Unit::hasIsActive).apply(sc2ApiUnit).orElse(nothing());

        attackUpgradeLevel = tryGet(Raw.Unit::getAttackUpgradeLevel, Raw.Unit::hasAttackUpgradeLevel)
                .apply(sc2ApiUnit).orElse(nothing());

        armorUpgradeLevel = tryGet(Raw.Unit::getArmorUpgradeLevel, Raw.Unit::hasArmorUpgradeLevel)
                .apply(sc2ApiUnit).orElse(nothing());

        shieldUpgradeLevel = tryGet(Raw.Unit::getShieldUpgradeLevel, Raw.Unit::hasShieldUpgradeLevel)
                .apply(sc2ApiUnit).orElse(nothing());

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

        hallucination = tryGet(Raw.Unit::getIsHallucination, Raw.Unit::hasIsHallucination).apply(sc2ApiUnit)
                .orElse(nothing());

        orders = sc2ApiUnit.getOrdersList().stream().map(UnitOrder::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));

        addOnTag = tryGet(Raw.Unit::getAddOnTag, Raw.Unit::hasAddOnTag)
                .apply(sc2ApiUnit).map(Tag::from).orElse(nothing());

        passengers = sc2ApiUnit.getPassengersList().stream().map(PassengerUnit::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));

        cargoSpaceTaken = tryGet(Raw.Unit::getCargoSpaceTaken, Raw.Unit::hasCargoSpaceTaken)
                .apply(sc2ApiUnit).orElse(nothing());

        cargoSpaceMax = tryGet(Raw.Unit::getCargoSpaceMax, Raw.Unit::hasCargoSpaceMax)
                .apply(sc2ApiUnit).orElse(nothing());

        assignedHarvesters = tryGet(Raw.Unit::getAssignedHarvesters, Raw.Unit::hasAssignedHarvesters)
                .apply(sc2ApiUnit).orElse(nothing());

        idealHarvesters = tryGet(Raw.Unit::getIdealHarvesters, Raw.Unit::hasIdealHarvesters)
                .apply(sc2ApiUnit).orElse(nothing());

        weaponCooldown = tryGet(Raw.Unit::getWeaponCooldown, Raw.Unit::hasWeaponCooldown)
                .apply(sc2ApiUnit).orElse(nothing());

        engagedTargetTag = tryGet(Raw.Unit::getEngagedTargetTag, Raw.Unit::hasEngagedTargetTag)
                .apply(sc2ApiUnit).map(Tag::from).orElse(nothing());

        buffDurationRemain = tryGet(Raw.Unit::getBuffDurationRemain, Raw.Unit::hasBuffDurationRemain)
                .apply(sc2ApiUnit).orElse(nothing());

        buffDurationMax = tryGet(Raw.Unit::getBuffDurationMax, Raw.Unit::hasBuffDurationMax)
                .apply(sc2ApiUnit).orElse(nothing());
    }

    UnitSnapshot(UnitSnapshot original, UnaryOperator<Ability> generalize) {
        this.displayType = original.displayType;
        this.alliance = original.alliance;
        this.position = original.position;
        this.cloakState = original.cloakState;
        this.buffs = original.buffs;
        this.detectRange = original.detectRange;
        this.radarRange = original.radarRange;
        this.selected = original.selected;
        this.onScreen = original.onScreen;
        this.blip = original.blip;
        this.powered = original.powered;
        this.active = original.active;
        this.attackUpgradeLevel = original.attackUpgradeLevel;
        this.armorUpgradeLevel = original.armorUpgradeLevel;
        this.shieldUpgradeLevel = original.shieldUpgradeLevel;
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
        this.hallucination = original.hallucination;
        this.orders = original.orders.stream()
                .map(order -> order.generalizeAbility(generalize))
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
        this.addOnTag = original.addOnTag;
        this.passengers = original.passengers;
        this.cargoSpaceTaken = original.cargoSpaceTaken;
        this.cargoSpaceMax = original.cargoSpaceMax;
        this.assignedHarvesters = original.assignedHarvesters;
        this.idealHarvesters = original.idealHarvesters;
        this.weaponCooldown = original.weaponCooldown;
        this.engagedTargetTag = original.engagedTargetTag;
        this.buffDurationRemain = original.buffDurationRemain;
        this.buffDurationMax = original.buffDurationMax;
    }

    public static UnitSnapshot from(Raw.Unit sc2ApiUnit) {
        require("sc2api unit", sc2ApiUnit);
        return new UnitSnapshot(sc2ApiUnit);
    }

    public DisplayType getDisplayType() {
        return displayType;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public Point getPosition() {
        return position;
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

    /**
     * Visible and within the camera frustrum.
     */
    public boolean isOnScreen() {
        return onScreen;
    }

    /**
     * Detected by sensor tower
     */
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
        return orders;
    }

    public Optional<Tag> getAddOnTag() {
        return Optional.ofNullable(addOnTag);
    }

    public List<PassengerUnit> getPassengers() {
        return passengers;
    }

    public Optional<Integer> getCargoSpaceTaken() {
        return Optional.ofNullable(cargoSpaceTaken);
    }

    public Optional<Integer> getCargoSpaceMax() {
        return Optional.ofNullable(cargoSpaceMax);
    }

    public Set<Buff> getBuffs() {
        return buffs;
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

    /**
     * Building is training/researching (ie animated).
     */
    public Optional<Boolean> getActive() {
        return Optional.ofNullable(active);
    }

    public Optional<Integer> getAttackUpgradeLevel() {
        return Optional.ofNullable(attackUpgradeLevel);
    }

    public Optional<Integer> getArmorUpgradeLevel() {
        return Optional.ofNullable(armorUpgradeLevel);
    }

    public Optional<Integer> getShieldUpgradeLevel() {
        return Optional.ofNullable(shieldUpgradeLevel);
    }

    /**
     * Unit is your own or detected as a hallucination.
     */
    public Optional<Boolean> getHallucination() {
        return Optional.ofNullable(hallucination);
    }

    /**
     * How long a buff or unit is still around (eg mule, broodling, chronoboost).
     */
    public Optional<Integer> getBuffDurationRemain() {
        return Optional.ofNullable(buffDurationRemain);
    }

    /**
     * How long the buff or unit is still around (eg mule, broodling, chronoboost).
     */
    public Optional<Integer> getBuffDurationMax() {
        return Optional.ofNullable(buffDurationMax);
    }

    @Override
    public UnitSnapshot generalizeAbility(UnaryOperator<Ability> generalize) {
        return new UnitSnapshot(this, generalize);
    }

    public boolean canEqual(Object other) {
        return other instanceof UnitSnapshot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitSnapshot that = (UnitSnapshot) o;

        if (onScreen != that.onScreen) return false;
        if (blip != that.blip) return false;
        if (displayType != that.displayType) return false;
        if (alliance != that.alliance) return false;
        if (!position.equals(that.position)) return false;
        if (cloakState != that.cloakState) return false;
        if (!Objects.equals(detectRange, that.detectRange)) return false;
        if (!Objects.equals(radarRange, that.radarRange)) return false;
        if (!Objects.equals(selected, that.selected)) return false;
        if (!Objects.equals(powered, that.powered)) return false;
        if (!buffs.equals(that.buffs)) return false;
        if (!Objects.equals(active, that.active)) return false;
        if (!Objects.equals(attackUpgradeLevel, that.attackUpgradeLevel))
            return false;
        if (!Objects.equals(armorUpgradeLevel, that.armorUpgradeLevel))
            return false;
        if (!Objects.equals(shieldUpgradeLevel, that.shieldUpgradeLevel))
            return false;
        if (!Objects.equals(health, that.health)) return false;
        if (!Objects.equals(healthMax, that.healthMax)) return false;
        if (!Objects.equals(shield, that.shield)) return false;
        if (!Objects.equals(shieldMax, that.shieldMax)) return false;
        if (!Objects.equals(energy, that.energy)) return false;
        if (!Objects.equals(energyMax, that.energyMax)) return false;
        if (!Objects.equals(mineralContents, that.mineralContents))
            return false;
        if (!Objects.equals(vespeneContents, that.vespeneContents))
            return false;
        if (!Objects.equals(flying, that.flying)) return false;
        if (!Objects.equals(burrowed, that.burrowed)) return false;
        if (!Objects.equals(hallucination, that.hallucination))
            return false;
        if (!orders.equals(that.orders)) return false;
        if (!Objects.equals(addOnTag, that.addOnTag)) return false;
        if (!passengers.equals(that.passengers)) return false;
        if (!Objects.equals(cargoSpaceTaken, that.cargoSpaceTaken))
            return false;
        if (!Objects.equals(cargoSpaceMax, that.cargoSpaceMax))
            return false;
        if (!Objects.equals(assignedHarvesters, that.assignedHarvesters))
            return false;
        if (!Objects.equals(idealHarvesters, that.idealHarvesters))
            return false;
        if (!Objects.equals(weaponCooldown, that.weaponCooldown))
            return false;
        if (!Objects.equals(engagedTargetTag, that.engagedTargetTag))
            return false;
        if (!Objects.equals(buffDurationRemain, that.buffDurationRemain))
            return false;
        return Objects.equals(buffDurationMax, that.buffDurationMax);
    }

    @Override
    public int hashCode() {
        int result = displayType.hashCode();
        result = 31 * result + alliance.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + (cloakState != null ? cloakState.hashCode() : 0);
        result = 31 * result + (detectRange != null ? detectRange.hashCode() : 0);
        result = 31 * result + (radarRange != null ? radarRange.hashCode() : 0);
        result = 31 * result + (selected != null ? selected.hashCode() : 0);
        result = 31 * result + (onScreen ? 1 : 0);
        result = 31 * result + (blip ? 1 : 0);
        result = 31 * result + (powered != null ? powered.hashCode() : 0);
        result = 31 * result + buffs.hashCode();
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (attackUpgradeLevel != null ? attackUpgradeLevel.hashCode() : 0);
        result = 31 * result + (armorUpgradeLevel != null ? armorUpgradeLevel.hashCode() : 0);
        result = 31 * result + (shieldUpgradeLevel != null ? shieldUpgradeLevel.hashCode() : 0);
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
        result = 31 * result + (hallucination != null ? hallucination.hashCode() : 0);
        result = 31 * result + orders.hashCode();
        result = 31 * result + (addOnTag != null ? addOnTag.hashCode() : 0);
        result = 31 * result + passengers.hashCode();
        result = 31 * result + (cargoSpaceTaken != null ? cargoSpaceTaken.hashCode() : 0);
        result = 31 * result + (cargoSpaceMax != null ? cargoSpaceMax.hashCode() : 0);
        result = 31 * result + (assignedHarvesters != null ? assignedHarvesters.hashCode() : 0);
        result = 31 * result + (idealHarvesters != null ? idealHarvesters.hashCode() : 0);
        result = 31 * result + (weaponCooldown != null ? weaponCooldown.hashCode() : 0);
        result = 31 * result + (engagedTargetTag != null ? engagedTargetTag.hashCode() : 0);
        result = 31 * result + (buffDurationRemain != null ? buffDurationRemain.hashCode() : 0);
        result = 31 * result + (buffDurationMax != null ? buffDurationMax.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
