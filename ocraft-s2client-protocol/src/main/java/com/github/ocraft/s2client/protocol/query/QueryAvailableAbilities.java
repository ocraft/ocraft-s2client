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
