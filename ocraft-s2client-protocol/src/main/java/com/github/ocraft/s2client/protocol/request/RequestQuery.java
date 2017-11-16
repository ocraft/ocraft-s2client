package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Query;
import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.query.QueryAvailableAbilities;
import com.github.ocraft.s2client.protocol.query.QueryBuildingPlacement;
import com.github.ocraft.s2client.protocol.query.QueryPathing;
import com.github.ocraft.s2client.protocol.query.QueryPathingBuilder;
import com.github.ocraft.s2client.protocol.syntax.query.QueryAvailableAbilitiesBuilder;
import com.github.ocraft.s2client.protocol.syntax.query.QueryBuildingPlacementBuilder;
import com.github.ocraft.s2client.protocol.syntax.request.RequestQuerySyntax;

import java.util.ArrayList;
import java.util.List;

import static com.github.ocraft.s2client.protocol.Preconditions.oneOfIsNotEmpty;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public final class RequestQuery extends Request {

    private static final long serialVersionUID = 6640942197738457010L;

    private final List<QueryPathing> pathings;
    private final List<QueryAvailableAbilities> abilities;
    private final List<QueryBuildingPlacement> placements;
    private final boolean ignoreResourceRequirements;

    public static final class Builder implements RequestQuerySyntax, BuilderSyntax<RequestQuery> {

        private List<QueryPathing> pathings = new ArrayList<>();
        private List<QueryAvailableAbilities> abilities = new ArrayList<>();
        private List<QueryBuildingPlacement> placements = new ArrayList<>();
        private boolean ignoreResourceRequirements;

        @Override
        public Builder ofPathings(QueryPathing... pathings) {
            this.pathings.addAll(asList(pathings));
            return this;
        }

        @Override
        public Builder ofPathings(QueryPathingBuilder... pathings) {
            this.pathings.addAll(BuilderSyntax.buildAll(pathings));
            return this;
        }

        @Override
        public Builder ofAbilities(QueryAvailableAbilities... abilities) {
            this.abilities.addAll(asList(abilities));
            return this;
        }

        @Override
        public Builder ofAbilities(QueryAvailableAbilitiesBuilder... abilities) {
            this.abilities.addAll(BuilderSyntax.buildAll(abilities));
            return this;
        }

        @Override
        public Builder ofPlacements(QueryBuildingPlacement... placements) {
            this.placements.addAll(asList(placements));
            return this;
        }

        @Override
        public Builder ofPlacements(QueryBuildingPlacementBuilder... placements) {
            this.placements.addAll(BuilderSyntax.buildAll(placements));
            return this;
        }

        public BuilderSyntax<RequestQuery> ignoreResourceRequirements() {
            this.ignoreResourceRequirements = true;
            return this;
        }

        @Override
        public RequestQuery build() {
            oneOfIsNotEmpty("query", pathings, abilities, placements);
            return new RequestQuery(this);
        }
    }

    private RequestQuery(Builder builder) {
        pathings = builder.pathings;
        abilities = builder.abilities;
        placements = builder.placements;
        ignoreResourceRequirements = builder.ignoreResourceRequirements;
    }

    public static RequestQuerySyntax query() {
        return new Builder();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setQuery(Query.RequestQuery.newBuilder()
                        .addAllPathing(pathings.stream().map(QueryPathing::toSc2Api).collect(toList()))
                        .addAllAbilities(
                                abilities.stream().map(QueryAvailableAbilities::toSc2Api).collect(toList()))
                        .addAllPlacements(
                                placements.stream().map(QueryBuildingPlacement::toSc2Api).collect(toList()))
                        .setIgnoreResourceRequirements(ignoreResourceRequirements)
                        .build())
                .build();
    }

    public List<QueryPathing> getPathings() {
        return new ArrayList<>(pathings);
    }

    public List<QueryAvailableAbilities> getAbilities() {
        return new ArrayList<>(abilities);
    }

    public List<QueryBuildingPlacement> getPlacements() {
        return new ArrayList<>(placements);
    }

    public boolean isIgnoreResourceRequirements() {
        return ignoreResourceRequirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestQuery that = (RequestQuery) o;

        return ignoreResourceRequirements == that.ignoreResourceRequirements &&
                pathings.equals(that.pathings) &&
                abilities.equals(that.abilities) &&
                placements.equals(that.placements);
    }

    @Override
    public int hashCode() {
        int result = pathings.hashCode();
        result = 31 * result + abilities.hashCode();
        result = 31 * result + placements.hashCode();
        result = 31 * result + (ignoreResourceRequirements ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
