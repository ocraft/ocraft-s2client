package com.github.ocraft.s2client.protocol.response;

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

import SC2APIProtocol.Query;
import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.query.AvailableAbilities;
import com.github.ocraft.s2client.protocol.query.BuildingPlacement;
import com.github.ocraft.s2client.protocol.query.Pathing;

import java.util.Collections;
import java.util.List;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public final class ResponseQuery extends Response {

    private static final long serialVersionUID = 2017992705975895238L;

    private final List<Pathing> pathing;
    private final List<AvailableAbilities> abilities;
    private final List<BuildingPlacement> placements;

    private ResponseQuery(Query.ResponseQuery sc2ApiResponseQuery, Sc2Api.Status status) {
        super(ResponseType.QUERY, GameStatus.from(status));

        pathing = sc2ApiResponseQuery.getPathingList().stream().map(Pathing::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
        abilities = sc2ApiResponseQuery.getAbilitiesList().stream().map(AvailableAbilities::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
        placements = sc2ApiResponseQuery.getPlacementsList().stream().map(BuildingPlacement::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public static ResponseQuery from(Sc2Api.Response sc2ApiResponse) {
        if (!hasQueryResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have query response");
        }
        return new ResponseQuery(sc2ApiResponse.getQuery(), sc2ApiResponse.getStatus());
    }

    private static boolean hasQueryResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasQuery();
    }

    public List<Pathing> getPathing() {
        return pathing;
    }

    public List<AvailableAbilities> getAbilities() {
        return abilities;
    }

    public List<BuildingPlacement> getPlacements() {
        return placements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResponseQuery that = (ResponseQuery) o;

        return that.canEqual(this) &&
                pathing.equals(that.pathing) && abilities.equals(that.abilities) && placements.equals(that.placements);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseQuery;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + pathing.hashCode();
        result = 31 * result + abilities.hashCode();
        result = 31 * result + placements.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
