package com.github.ocraft.s2client.bot.gateway;

/*-
 * #%L
 * ocraft-s2client-bot
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

import com.github.ocraft.s2client.protocol.data.Buffs;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.Optional;
import java.util.function.Predicate;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public class UnitInPool {
    private final Tag tag;
    private Unit unit;
    private boolean alive = true;
    private long lastSeenGameLoop;

    public UnitInPool(Tag tag) {
        require("unit tag", tag);
        this.tag = tag;
    }

    public UnitInPool update(Unit unit, long gameLoop, boolean alive) {
        this.unit = unit;
        this.lastSeenGameLoop = gameLoop;
        this.alive = alive;
        return this;
    }

    public UnitInPool update(Unit unit) {
        this.unit = unit;
        return this;
    }

    public Tag getTag() {
        return tag;
    }

    public Optional<Unit> getUnit() {
        return Optional.ofNullable(unit);
    }

    public Unit unit() {
        Optional<Unit> unitData = getUnit();
        if (unitData.isPresent()) {
            return unitData.get();
        } else {
            throw new IllegalStateException("Unit is not set");
        }
    }

    public void dead() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public long getLastSeenGameLoop() {
        return lastSeenGameLoop;
    }

    public static Predicate<UnitInPool> isUnit(UnitType type) {
        return unitInPool -> unitInPool.getUnit().isPresent() && unitInPool.getUnit().get().getType().equals(type);
    }

    /**
     * Helper function used to discover whether a unit is carrying minerals or not. You could use this function in
     * GetUnits to get all units carrying minerals:
     * List<UnitInPool> units = observation().getUnits(Alliance.SELF, UnitInPool.isCarryingMinerals());
     *
     * @return Returns true if the unit is carrying minerals, false otherwise.
     */
    public static Predicate<UnitInPool> isCarryingMinerals() {
        return unitInPool -> unitInPool.getUnit().isPresent() && (
                unitInPool.unit().getBuffs().contains(Buffs.CARRY_MINERAL_FIELD_MINERALS) ||
                        unitInPool.unit().getBuffs().contains(Buffs.CARRY_HIGH_YIELD_MINERAL_FIELD_MINERALS));
    }

    /**
     * Helper function used to discover whether a unit is carrying vespene or not. You could use this function in
     * GetUnits to get all units carrying vespene:
     * List<UnitInPool> units = observation().getUnits(Alliance.SELF, UnitInPool.isCarryingVespene);
     *
     * @return Returns true if the unit is carrying vespene, false otherwise.
     */
    public static Predicate<UnitInPool> isCarryingVespene() {
        return unitInPool -> unitInPool.getUnit().isPresent() && (
                unitInPool.unit().getBuffs().contains(Buffs.CARRY_HARVESTABLE_VESPANE_GEYSER_GAS) ||
                        unitInPool.unit().getBuffs().contains(Buffs.CARRY_HARVESTABLE_VESPANE_GEYSER_GAS_PROTOSS) ||
                        unitInPool.unit().getBuffs().contains(Buffs.CARRY_HARVESTABLE_VESPANE_GEYSER_GAS_ZERG));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitInPool that = (UnitInPool) o;

        return tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }

    @Override
    public String toString() {
        return "UnitInPool{" +
                "tag=" + tag +
                ", unit=" + unit +
                ", alive=" + alive +
                ", lastSeenGameLoop=" + lastSeenGameLoop +
                '}';
    }
}
