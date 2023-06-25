package com.github.ocraft.s2client.bot.gateway.impl;

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

import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.gateway.QueryInterface;
import com.github.ocraft.s2client.protocol.action.ActionResult;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.query.*;
import com.github.ocraft.s2client.protocol.request.RequestQuery;
import com.github.ocraft.s2client.protocol.request.Requests;
import com.github.ocraft.s2client.protocol.response.ResponseQuery;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class QueryInterfaceImpl implements QueryInterface {

    private final ControlInterfaceImpl controlInterface;

    QueryInterfaceImpl(ControlInterfaceImpl controlInterface) {
        this.controlInterface = controlInterface;
    }

    private ControlInterfaceImpl control() {
        return controlInterface;
    }

    @Override
    public AvailableAbilities getAbilitiesForUnit(Unit unit, boolean ignoreResourceRequirements) {
        List<AvailableAbilities> abilitiesForUnits = getAbilitiesForUnits(List.of(unit), ignoreResourceRequirements);
        return abilitiesForUnits.isEmpty() ? null : abilitiesForUnits.get(0);
    }

    @Override
    public List<AvailableAbilities> getAbilitiesForUnits(List<Unit> units, boolean ignoreResourceRequirements) {
        RequestQuery request = Requests.query()
                .ofAbilities(abilitiesFor(units))
                .ignoreResourceRequirements(ignoreResourceRequirements)
                .build();

        Optional<ResponseQuery> responseQuery = sendRequest(request);

        // TODO p.picheta ClientError::ErrorSC2 - if unit tag does not match abilities????

        List<AvailableAbilities> availableAbilities = responseQuery
                .map(ResponseQuery::getAbilities)
                .orElseGet(ArrayList::new);

        // TODO p.picheta to test
        if (control().isUseGeneralizedAbilityId()) {
            availableAbilities = availableAbilities.stream()
                    .map(ability -> ability.generalizeAbility(control().observationInternal()::getGeneralizedAbility))
                    .collect(Collectors.toList());
        }

        return availableAbilities;
    }

    private Optional<ResponseQuery> sendRequest(RequestQuery request) {
        return control()
                .waitForResponse(control().proto().sendRequest(request))
                .flatMap(response -> response.as(ResponseQuery.class));
    }

    private QueryAvailableAbilities[] abilitiesFor(List<Unit> units) {
        return units.stream()
                .map(unit -> Queries.availableAbilities().of(unit).build())
                .toArray(QueryAvailableAbilities[]::new);
    }

    @Override
    public float pathingDistance(Point2d start, Point2d end) {
        List<Float> distances = pathingDistance(List.of(QueryPathing.path().from(start).to(end).build()));
        return distances.isEmpty() ? 0.0f : distances.get(0);
    }

    @Override
    public float pathingDistance(Unit start, Point2d end) {
        List<Float> distances = pathingDistance(List.of(QueryPathing.path().from(start.getTag()).to(end).build()));
        return distances.isEmpty() ? 0.0f : distances.get(0);
    }

    @Override
    public List<Float> pathingDistance(List<QueryPathing> queries) {
        RequestQuery request = Requests.query()
                .ofPathings(queries.toArray(new QueryPathing[0]))
                .build();
        Optional<ResponseQuery> responseQuery = sendRequest(request);

        List<Float> distances = new ArrayList<>();
        responseQuery.ifPresent(response -> response.getPathing().forEach(pathing -> {
            if (pathing.getDistance().isPresent()) {
                distances.add(pathing.getDistance().get());
            } else {
                distances.add(0.0f);
            }
        }));
        return distances;
    }

    @Override
    public boolean placement(Ability ability, Point2d target) {
        List<Boolean> placement = placement(List.of(
                QueryBuildingPlacement.placeBuilding().useAbility(ability).on(target).build()
        ));
        return placement.isEmpty() ? false : placement.get(0);
    }

    @Override
    public boolean placement(Ability ability, Point2d target, Unit unit) {
        List<Boolean> placement = placement(List.of(
                QueryBuildingPlacement.placeBuilding().withUnit(unit).useAbility(ability).on(target).build()
        ));
        return placement.isEmpty() ? false : placement.get(0);
    }

    @Override
    public List<Boolean> placement(List<QueryBuildingPlacement> queries) {
        RequestQuery request = Requests.query()
                .ofPlacements(queries.toArray(new QueryBuildingPlacement[0]))
                .build();
        Optional<ResponseQuery> responseQuery = sendRequest(request);

        List<Boolean> placements = new ArrayList<>();
        responseQuery.ifPresent(response -> response.getPlacements().forEach(pathing -> {
            placements.add(pathing.getResult().equals(ActionResult.SUCCESS));
        }));
        return placements;
    }
}
