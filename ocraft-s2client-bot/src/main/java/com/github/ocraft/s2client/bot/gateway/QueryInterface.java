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

import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.debug.Color;
import com.github.ocraft.s2client.protocol.query.AvailableAbilities;
import com.github.ocraft.s2client.protocol.query.QueryBuildingPlacement;
import com.github.ocraft.s2client.protocol.query.QueryPathing;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.*;

import static java.util.Arrays.asList;

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

    /**
     * Calculates expansion locations, this call can take on the order of 100ms since it makes blocking queries to SC2
     * so call it once and cache the results.
     *
     * @param debug If filled out CalculateExpansionLocations will render spheres to show what it calculated.
     */
    default List<Point> calculateExpansionLocations(
            ObservationInterface observation, DebugInterface debug, ExpansionParameters parameters) {
        List<UnitInPool> resources = observation.getUnits(unitInPool -> {
            Set<UnitType> fields = new HashSet<>(asList(
                    Units.NEUTRAL_MINERAL_FIELD, Units.NEUTRAL_MINERAL_FIELD750,
                    Units.NEUTRAL_RICH_MINERAL_FIELD, Units.NEUTRAL_RICH_MINERAL_FIELD750,
                    Units.NEUTRAL_PURIFIER_MINERAL_FIELD, Units.NEUTRAL_PURIFIER_MINERAL_FIELD750,
                    Units.NEUTRAL_PURIFIER_RICH_MINERAL_FIELD, Units.NEUTRAL_PURIFIER_RICH_MINERAL_FIELD750,
                    Units.NEUTRAL_LAB_MINERAL_FIELD, Units.NEUTRAL_LAB_MINERAL_FIELD750,
                    Units.NEUTRAL_BATTLE_STATION_MINERAL_FIELD, Units.NEUTRAL_BATTLE_STATION_MINERAL_FIELD750,
                    Units.NEUTRAL_VESPANE_GEYSER, Units.NEUTRAL_PROTOSS_VESPANE_GEYSER,
                    Units.NEUTRAL_SPACE_PLATFORM_GEYSER, Units.NEUTRAL_PURIFIER_VESPENE_GEYSER,
                    Units.NEUTRAL_SHAKURAS_VESPENE_GEYSER, Units.NEUTRAL_RICH_VESPENE_GEYSER
            ));
            return fields.contains(unitInPool.unit().getType());
        });

        List<Point> expansionLocations = new ArrayList<>();
        Map<Point, List<UnitInPool>> clusters = cluster(resources, parameters.getClusterDistance());

        Map<Point, Integer> querySize = new LinkedHashMap<>();
        List<QueryBuildingPlacement> queries = new ArrayList<>();
        for (Map.Entry<Point, List<UnitInPool>> cluster : clusters.entrySet()) {
            if (debug != null) {
                for (double r : parameters.getRadiuses()) {
                    debug.debugSphereOut(cluster.getKey(), (float) r, Color.GREEN);
                }
            }

            // Get the required queries for this cluster.
            int queryCount = 0;
            for (double r : parameters.getRadiuses()) {
                List<QueryBuildingPlacement> calculatedQueries = calculateQueries(
                        r, parameters.getCircleStepSize(), cluster.getKey().toPoint2d());
                queries.addAll(calculatedQueries);
                queryCount += calculatedQueries.size();
            }

            querySize.put(cluster.getKey(), queryCount);
        }

        List<Boolean> results = this.placement(queries);
        int startIndex = 0;
        for (Map.Entry<Point, List<UnitInPool>> cluster : clusters.entrySet()) {
            double distance = Double.MAX_VALUE;
            Point2d closest = Point2d.of(0.0f, 0.0f);

            // For each query for the cluster minimum distance location that is valid.
            for (int j = startIndex, e = startIndex + querySize.get(cluster.getKey()); j < e; ++j) {
                if (!results.get(j)) {
                    continue;
                }

                Point2d p = queries.get(j).getTarget();

                double d = p.distance(cluster.getKey().toPoint2d());
                if (d < distance) {
                    distance = d;
                    closest = p;
                }
            }

            Point expansion = Point.of(
                    closest.getX(),
                    closest.getY(),
                    cluster.getValue().get(0).unit().getPosition().getZ());
            if (debug != null) {
                debug.debugSphereOut(expansion, 0.35f, Color.RED);
            }

            expansionLocations.add(expansion);

            startIndex += querySize.get(cluster.getKey());
        }

        return expansionLocations;
    }

    default List<Point> calculateExpansionLocations(ObservationInterface observation) {
        return calculateExpansionLocations(observation, null, ExpansionParameters.preset());
    }

    default List<Point> calculateExpansionLocations(ObservationInterface observation, DebugInterface debug) {
        return calculateExpansionLocations(observation, debug, ExpansionParameters.preset());
    }

    private List<QueryBuildingPlacement> calculateQueries(double radius, double stepSize, Point2d center) {
        List<QueryBuildingPlacement> queries = new ArrayList<>();

        Point2d previousGrid = Point2d.of(Float.MAX_VALUE, Float.MAX_VALUE);
        // Find a buildable location on the circumference of the sphere
        for (double degree = 0.0; degree < 360.0; degree += stepSize) {
            Point2d point = pointOnCircle(radius, center, degree);

            QueryBuildingPlacement query = QueryBuildingPlacement
                    .placeBuilding()
                    .useAbility(Abilities.BUILD_COMMAND_CENTER)
                    .on(point)
                    .build();

            Point2d currentGrid = Point2d.of((float) Math.floor(point.getX()), (float) Math.floor(point.getY()));

            if (!previousGrid.equals(currentGrid)) {
                queries.add(query);
            }

            previousGrid = currentGrid;
        }

        return queries;
    }

    private static Point2d pointOnCircle(double radius, Point2d center, double degree) {
        return Point2d.of(
                (float) (radius * Math.cos(Math.toRadians(degree)) + center.getX()),
                (float) (radius * Math.sin(Math.toRadians(degree)) + center.getY()));
    }

    /**
     * Clusters units within some distance of each other and returns a list of them and their center of mass.
     */
    static Map<Point, List<UnitInPool>> cluster(List<UnitInPool> units, double distanceApart) {
        Map<Point, List<UnitInPool>> clusters = new LinkedHashMap<>();
        for (UnitInPool u : units) {
            double distance = Double.MAX_VALUE;
            Map.Entry<Point, List<UnitInPool>> targetCluster = null;
            // Find the cluster this mineral patch is closest to.
            for (Map.Entry<Point, List<UnitInPool>> cluster : clusters.entrySet()) {
                double d = u.unit().getPosition().distance(cluster.getKey());
                if (d < distance) {
                    distance = d;
                    targetCluster = cluster;
                }
            }

            // If the target cluster is some distance away don't use it.
            if (targetCluster == null || distance > distanceApart) {
                ArrayList<UnitInPool> unitsInCluster = new ArrayList<>();
                unitsInCluster.add(u);
                clusters.put(u.unit().getPosition(), unitsInCluster);
                continue;
            }

            // Otherwise append to that cluster and update it's center of mass.

            if (targetCluster.getValue() == null) {
                targetCluster.setValue(new ArrayList<>());
            }
            targetCluster.getValue().add(u);

            int size = targetCluster.getValue().size();
            Point centerOfMass = targetCluster.getKey().mul(size - 1.0f).add(u.unit().getPosition()).div(size);
            clusters.put(centerOfMass, clusters.remove(targetCluster.getKey()));
        }

        return clusters;
    }

}
