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
package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.query.QueryAvailableAbilitiesBuilder;
import com.github.ocraft.s2client.protocol.syntax.query.QueryAvailableAbilitiesSyntax;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class QueryAvailableAbilities implements Sc2ApiSerializable<Query.RequestQueryAvailableAbilities> {

    private static final long serialVersionUID = 6386140845508573069L;

    private final Tag unitTag;

    public static final class Builder implements QueryAvailableAbilitiesSyntax, QueryAvailableAbilitiesBuilder {

        private Tag unitTag;

        @Override
        public QueryAvailableAbilitiesBuilder of(Tag unitTag) {
            this.unitTag = unitTag;
            return this;
        }

        @Override
        public QueryAvailableAbilitiesBuilder of(Unit unit) {
            this.unitTag = unit.getTag();
            return this;
        }

        @Override
        public QueryAvailableAbilities build() {
            require("unit tag", unitTag);
            return new QueryAvailableAbilities(this);
        }
    }

    private QueryAvailableAbilities(Builder builder) {
        unitTag = builder.unitTag;
    }

    public static QueryAvailableAbilitiesSyntax availableAbilities() {
        return new Builder();
    }

    @Override
    public Query.RequestQueryAvailableAbilities toSc2Api() {
        return Query.RequestQueryAvailableAbilities.newBuilder().setUnitTag(unitTag.toSc2Api()).build();
    }

    public Tag getUnitTag() {
        return unitTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryAvailableAbilities that = (QueryAvailableAbilities) o;

        return unitTag.equals(that.unitTag);
    }

    @Override
    public int hashCode() {
        return unitTag.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
