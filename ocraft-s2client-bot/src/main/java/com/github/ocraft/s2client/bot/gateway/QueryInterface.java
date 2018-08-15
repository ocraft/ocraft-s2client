package com.github.ocraft.s2client.bot.gateway;

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

import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.query.AvailableAbilities;
import com.github.ocraft.s2client.protocol.query.QueryBuildingPlacement;
import com.github.ocraft.s2client.protocol.query.QueryPathing;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.List;

/**
 * The QueryInterface provides additional data not contained in the observation.
 * <p>
 * Performance note:
 * - Always try and batch things up. These queries are effectively synchronous and will block until returned.
 */
public interface QueryInterface {

    /**
     * Returns a list of abilities represented as a AvailableAbilities see the Abilities enum for their corresponding,
     * named, representations.
     *
     * @param unit                       Unit.
     * @param ignoreResourceRequirements Ignores food, mineral and gas costs, as well as cooldowns.
     * @return Abilities for the unit.
     */
    AvailableAbilities getAbilitiesForUnit(Unit unit, boolean ignoreResourceRequirements);

    /**
     * Issues multiple available abilities queries.
     * Batch version.
     *
     * @param units                      Units.
     * @param ignoreResourceRequirements Ignores food, mineral and gas costs, as well as cooldowns.
     * @return Abilities for the units.
     */
    List<AvailableAbilities> getAbilitiesForUnits(List<Unit> units, boolean ignoreResourceRequirements);

    /**
     * Returns pathing distance between two locations. Takes into account unit movement properties (e.g. Flying).
     *
     * @param start Starting point.
     * @param end   End point.
     * @return Distance between the two points.
     */
    float pathingDistance(Point2d start, Point2d end);

    /**
     * Returns pathing distance between a unit and a target location. Takes into account unit movement properties
     * (e.g. Flying).
     * Batch version.
     *
     * @param start Starting points.
     * @param end   End points.
     * @return Distances between the two points.
     */
    float pathingDistance(Unit start, Point2d end);

    /**
     * Issues multiple pathing queries.
     */
    List<Float> pathingDistance(List<QueryPathing> queries);

    /**
     * @see #placement(Ability, Point2d, Unit)
     */
    boolean placement(Ability ability, Point2d target);

    /**
     * Returns whether a building can be placed at a location.
     * The placing unit field is optional. This is only used for cases where the placing unit plays a role in the
     * placement grid test (e.g. A flying barracks building an add-on requires room for both the barracks and add-on).
     *
     * @param ability Ability for building or moving a structure.
     * @param target  Position to attempt placement on.
     * @param unit    (Optional) The unit that is moving, if moving a structure.
     * @return If placement is possible.
     */
    boolean placement(Ability ability, Point2d target, Unit unit);

    /**
     * A batch version of the above Placement query. Takes an array of abilities, positions and
     * optional unit tags and returns a matching array of booleans indicating if placement is possible.
     *
     * @param queries Placement queries.
     * @return Array of booleans indicating if placement is possible.
     */
    List<Boolean> placement(List<QueryBuildingPlacement> queries);

}
