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
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class PassengerUnit implements Serializable {

    private static final long serialVersionUID = -1401597859772600576L;

    private final Tag tag;
    private final float health;
    private final float healthMax;
    private final Float shield;
    private final Float shieldMax;
    private final Float energy;
    private final Float energyMax;
    private final UnitType type;

    private PassengerUnit(Raw.PassengerUnit sc2ApiPassengerUnit) {
        tag = tryGet(Raw.PassengerUnit::getTag, Raw.PassengerUnit::hasTag)
                .apply(sc2ApiPassengerUnit).map(Tag::from).orElseThrow(required("tag"));

        health = tryGet(Raw.PassengerUnit::getHealth, Raw.PassengerUnit::hasHealth)
                .apply(sc2ApiPassengerUnit).orElseThrow(required("health"));

        healthMax = tryGet(Raw.PassengerUnit::getHealthMax, Raw.PassengerUnit::hasHealthMax)
                .apply(sc2ApiPassengerUnit).orElseThrow(required("health max"));

        shield = tryGet(Raw.PassengerUnit::getShield, Raw.PassengerUnit::hasShield)
                .apply(sc2ApiPassengerUnit).orElse(nothing());

        shieldMax = tryGet(Raw.PassengerUnit::getShieldMax, Raw.PassengerUnit::hasShieldMax)
                .apply(sc2ApiPassengerUnit).orElse(nothing());

        energy = tryGet(Raw.PassengerUnit::getEnergy, Raw.PassengerUnit::hasEnergy)
                .apply(sc2ApiPassengerUnit).orElse(nothing());

        energyMax = tryGet(Raw.PassengerUnit::getEnergyMax, Raw.PassengerUnit::hasEnergyMax)
                .apply(sc2ApiPassengerUnit).orElse(nothing());

        type = tryGet(Raw.PassengerUnit::getUnitType, Raw.PassengerUnit::hasUnitType)
                .apply(sc2ApiPassengerUnit).map(Units::from).orElseThrow(required("unit type"));

    }

    public static PassengerUnit from(Raw.PassengerUnit sc2ApiPassengerUnit) {
        require("sc2api passenger unit", sc2ApiPassengerUnit);
        return new PassengerUnit(sc2ApiPassengerUnit);
    }

    public Tag getTag() {
        return tag;
    }

    public float getHealth() {
        return health;
    }

    public float getHealthMax() {
        return healthMax;
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

    public UnitType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PassengerUnit that = (PassengerUnit) o;

        return Float.compare(that.health, health) == 0 &&
                Float.compare(that.healthMax, healthMax) == 0 &&
                tag.equals(that.tag) &&
                (shield != null ? shield.equals(that.shield) : that.shield == null) &&
                (shieldMax != null ? shieldMax.equals(that.shieldMax) : that.shieldMax == null) &&
                (energy != null ? energy.equals(that.energy) : that.energy == null) &&
                (energyMax != null ? energyMax.equals(that.energyMax) : that.energyMax == null) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        int result = tag.hashCode();
        result = 31 * result + (health != +0.0f ? Float.floatToIntBits(health) : 0);
        result = 31 * result + (healthMax != +0.0f ? Float.floatToIntBits(healthMax) : 0);
        result = 31 * result + (shield != null ? shield.hashCode() : 0);
        result = 31 * result + (shieldMax != null ? shieldMax.hashCode() : 0);
        result = 31 * result + (energy != null ? energy.hashCode() : 0);
        result = 31 * result + (energyMax != null ? energyMax.hashCode() : 0);
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
