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
package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.syntax.debug.*;

import static com.github.ocraft.s2client.protocol.Preconditions.*;

public final class DebugCreateUnit implements Sc2ApiSerializable<Debug.DebugCreateUnit> {

    private static final long serialVersionUID = 7745323270922370957L;
    private static final int DEFAULT_QUANTITY = 1;

    private final UnitType type;
    private final int owner;
    private final Point2d position;
    private final int quantity;

    public static final class Builder implements DebugCreateUnitSyntax, ForPlayerSyntax, UnitPositionSyntax,
            WithQuantitySyntax, DebugCreateUnitBuilder {

        private UnitType type;
        private Integer owner;
        private Point2d position;
        private int quantity = DEFAULT_QUANTITY;

        @Override
        public ForPlayerSyntax ofType(UnitType type) {
            this.type = type;
            return this;
        }

        @Override
        public UnitPositionSyntax forPlayer(int owner) {
            this.owner = owner;
            return this;
        }

        @Override
        public WithQuantitySyntax on(Point2d position) {
            this.position = position;
            return this;
        }

        @Override
        public DebugCreateUnitBuilder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        @Override
        public DebugCreateUnit build() {
            require("type", type);
            require("owner", owner);
            require("position", position);
            if (isSet(quantity)) {
                greaterOrEqual("quantity", quantity, 1);
            }
            return new DebugCreateUnit(this);
        }
    }

    private DebugCreateUnit(Builder builder) {
        type = builder.type;
        owner = builder.owner;
        position = builder.position;
        quantity = builder.quantity;
    }

    public static DebugCreateUnitSyntax createUnit() {
        return new Builder();
    }

    @Override
    public Debug.DebugCreateUnit toSc2Api() {
        return Debug.DebugCreateUnit.newBuilder()
                .setUnitType(type.toSc2Api())
                .setOwner(owner)
                .setPos(position.toSc2Api())
                .setQuantity(quantity)
                .build();
    }

    public UnitType getType() {
        return type;
    }

    public int getOwner() {
        return owner;
    }

    public Point2d getPosition() {
        return position;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugCreateUnit that = (DebugCreateUnit) o;

        return owner == that.owner && quantity == that.quantity && type == that.type && position.equals(that.position);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + owner;
        result = 31 * result + position.hashCode();
        result = 31 * result + quantity;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
