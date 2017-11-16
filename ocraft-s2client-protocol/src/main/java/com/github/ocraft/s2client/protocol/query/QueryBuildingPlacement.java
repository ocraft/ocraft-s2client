package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.syntax.query.QueryBuildingPlacementBuilder;
import com.github.ocraft.s2client.protocol.syntax.query.QueryBuildingPlacementSyntax;
import com.github.ocraft.s2client.protocol.syntax.query.UseAbilitySyntax;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class QueryBuildingPlacement implements Sc2ApiSerializable<Query.RequestQueryBuildingPlacement> {

    private static final long serialVersionUID = 6586392064204810059L;

    private final Ability ability;
    private final Point2d target;
    private final Tag placingUnitTag;

    public static final class Builder
            implements QueryBuildingPlacementSyntax, UseAbilitySyntax, TargetSyntax, QueryBuildingPlacementBuilder {

        private Ability ability;
        private Point2d target;
        private Tag placingUnitTag;

        @Override
        public UseAbilitySyntax withUnit(Tag unitTag) {
            this.placingUnitTag = unitTag;
            return this;
        }

        @Override
        public UseAbilitySyntax withUnit(Unit unit) {
            this.placingUnitTag = unit.getTag();
            return this;
        }

        @Override
        public TargetSyntax useAbility(Ability ability) {
            this.ability = ability;
            return this;
        }

        @Override
        public QueryBuildingPlacementBuilder on(Point2d target) {
            this.target = target;
            return this;
        }

        @Override
        public QueryBuildingPlacement build() {
            require("unit", placingUnitTag);
            require("ability", ability);
            require("target", target);
            return new QueryBuildingPlacement(this);
        }
    }

    private QueryBuildingPlacement(Builder builder) {
        ability = builder.ability;
        target = builder.target;
        placingUnitTag = builder.placingUnitTag;
    }

    public static Builder placeBuilding() {
        return new Builder();
    }

    @Override
    public Query.RequestQueryBuildingPlacement toSc2Api() {
        Query.RequestQueryBuildingPlacement.Builder aSc2ApiPlacement = Query.RequestQueryBuildingPlacement.newBuilder()
                .setAbilityId(ability.toSc2Api())
                .setTargetPos(target.toSc2Api());

        getPlacingUnitTag().map(Tag::toSc2Api).ifPresent(aSc2ApiPlacement::setPlacingUnitTag);

        return aSc2ApiPlacement.build();
    }

    public Ability getAbility() {
        return ability;
    }

    public Point2d getTarget() {
        return target;
    }

    public Optional<Tag> getPlacingUnitTag() {
        return Optional.ofNullable(placingUnitTag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryBuildingPlacement that = (QueryBuildingPlacement) o;

        return ability == that.ability &&
                target.equals(that.target) &&
                (placingUnitTag != null ? placingUnitTag.equals(that.placingUnitTag) : that.placingUnitTag == null);
    }

    @Override
    public int hashCode() {
        int result = ability.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + (placingUnitTag != null ? placingUnitTag.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
