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
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;

import java.util.function.UnaryOperator;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class Unit extends UnitSnapshot {

    private static final long serialVersionUID = 2752475641175136550L;

    // TODO p.picheta should probably return value as Optional instead but it will be breaking change in api
    public static final int CLOAKED = -1000;

    private final Tag tag;
    private final UnitType type;
    private final int owner;
    private final float facing;
    private final float radius;
    private final float buildProgress;  // Range: [0.0, 1.0]

    private Unit(Raw.Unit sc2ApiUnit) {
        super(sc2ApiUnit);

        tag = tryGet(Raw.Unit::getTag, Raw.Unit::hasTag).apply(sc2ApiUnit).map(Tag::from).orElseThrow(required("tag"));

        type = tryGet(Raw.Unit::getUnitType, Raw.Unit::hasUnitType)
                .apply(sc2ApiUnit).map(Units::from).orElseThrow(required("unit type"));

        // owner, facing, radius and buildProgress not given for enemy cloaked/undetected units
        owner = tryGet(Raw.Unit::getOwner, Raw.Unit::hasOwner).apply(sc2ApiUnit).orElse(CLOAKED);

        facing = tryGet(Raw.Unit::getFacing, Raw.Unit::hasFacing).apply(sc2ApiUnit).orElse((float) CLOAKED);

        radius = tryGet(Raw.Unit::getRadius, Raw.Unit::hasRadius).apply(sc2ApiUnit).orElse((float) CLOAKED);

        buildProgress = tryGet(Raw.Unit::getBuildProgress, Raw.Unit::hasBuildProgress)
                .apply(sc2ApiUnit).orElse((float) CLOAKED);

    }

    private Unit(Unit original, UnaryOperator<Ability> generalize) {
        super(original, generalize);

        this.tag = original.tag;
        this.type = original.type;
        this.owner = original.owner;
        this.facing = original.facing;
        this.radius = original.radius;
        this.buildProgress = original.buildProgress;
    }

    public static Unit from(Raw.Unit sc2ApiUnit) {
        require("sc2api unit", sc2ApiUnit);
        return new Unit(sc2ApiUnit);
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

    public float getFacing() {
        return facing;
    }

    public float getRadius() {
        return radius;
    }

    public float getBuildProgress() {
        return buildProgress;
    }

    @Override
    public Unit generalizeAbility(UnaryOperator<Ability> generalize) {
        return new Unit(this, generalize);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof Unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit)) return false;
        if (!super.equals(o)) return false;

        Unit unit = (Unit) o;

        if (owner != unit.owner) return false;
        if (Float.compare(unit.facing, facing) != 0) return false;
        if (Float.compare(unit.radius, radius) != 0) return false;
        if (Float.compare(unit.buildProgress, buildProgress) != 0) return false;
        if (!tag.equals(unit.tag)) return false;
        if (!unit.canEqual(this)) return false;

        return type.equals(unit.type);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + tag.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + owner;
        result = 31 * result + (facing != +0.0f ? Float.floatToIntBits(facing) : 0);
        result = 31 * result + (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
        result = 31 * result + (buildProgress != +0.0f ? Float.floatToIntBits(buildProgress) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
