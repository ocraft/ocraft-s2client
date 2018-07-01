package com.github.ocraft.s2client.protocol.debug;

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

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugSetUnitValueBuilder;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugSetUnitValueSyntax;
import com.github.ocraft.s2client.protocol.syntax.debug.ValueSyntax;
import com.github.ocraft.s2client.protocol.syntax.debug.ValueTypeSyntax;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class DebugSetUnitValue implements Sc2ApiSerializable<Debug.DebugSetUnitValue> {

    private static final long serialVersionUID = 4378296713045737791L;

    private final UnitValue unitValue;
    private final Tag unitTag;
    private final float value;

    public enum UnitValue implements Sc2ApiSerializable<Debug.DebugSetUnitValue.UnitValue> {
        ENERGY,
        LIFE,
        SHIELDS;

        @Override
        public Debug.DebugSetUnitValue.UnitValue toSc2Api() {
            switch (this) {
                case LIFE:
                    return Debug.DebugSetUnitValue.UnitValue.Life;
                case ENERGY:
                    return Debug.DebugSetUnitValue.UnitValue.Energy;
                case SHIELDS:
                    return Debug.DebugSetUnitValue.UnitValue.Shields;
                default:
                    throw new AssertionError("unknown unit value: " + this);
            }
        }
    }

    public static final class Builder
            implements DebugSetUnitValueSyntax, ValueTypeSyntax, ValueSyntax, DebugSetUnitValueBuilder {

        private DebugSetUnitValue.UnitValue unitValue;
        private Tag unitTag;
        private Float value;

        @Override
        public ValueTypeSyntax forUnit(Tag unitTag) {
            this.unitTag = unitTag;
            return this;
        }

        @Override
        public ValueTypeSyntax forUnit(Unit unit) {
            this.unitTag = unit.getTag();
            return this;
        }

        @Override
        public ValueSyntax set(UnitValue unitValue) {
            this.unitValue = unitValue;
            return this;
        }

        @Override
        public DebugSetUnitValueBuilder to(float value) {
            this.value = value;
            return this;
        }

        @Override
        public DebugSetUnitValue build() {
            require("unit value", unitValue);
            require("unit tag", unitTag);
            require("value", value);
            return new DebugSetUnitValue(this);
        }

    }

    private DebugSetUnitValue(DebugSetUnitValue.Builder builder) {
        unitValue = builder.unitValue;
        unitTag = builder.unitTag;
        value = builder.value;
    }

    public static DebugSetUnitValueSyntax setUnitValue() {
        return new DebugSetUnitValue.Builder();
    }

    @Override
    public Debug.DebugSetUnitValue toSc2Api() {
        return Debug.DebugSetUnitValue.newBuilder()
                .setUnitTag(unitTag.toSc2Api())
                .setUnitValue(unitValue.toSc2Api())
                .setValue(value).build();
    }

    public UnitValue getUnitValue() {
        return unitValue;
    }

    public Tag getUnitTag() {
        return unitTag;
    }

    public float getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugSetUnitValue that = (DebugSetUnitValue) o;

        return Float.compare(that.value, value) == 0 && unitValue == that.unitValue && unitTag.equals(that.unitTag);
    }

    @Override
    public int hashCode() {
        int result = unitValue.hashCode();
        result = 31 * result + unitTag.hashCode();
        result = 31 * result + (value != +0.0f ? Float.floatToIntBits(value) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
