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
import java.util.stream.Collectors;

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
     * @return List of booleans indicating if placement is possible.
     */
    List<Boolean> placement(List<QueryBuildingPlacement> queries);

    /**
     * Calculates expansion locations
     * Note: bases that are blocked by destructible rocks or small minerals are included in this list
     *
     * @param debug If provided CalculateExpansionLocations will render boxes to show what it calculated.
     */
    default List<Point> calculateExpansionLocations(ObservationInterface observation, DebugInterface debug) {
        List<UnitInPool> resources = observation.getUnits(unitInPool -> {
            Set<UnitType> nodes = new HashSet<>(asList(
                    Units.NEUTRAL_MINERAL_FIELD, Units.NEUTRAL_MINERAL_FIELD750,
                    Units.NEUTRAL_RICH_MINERAL_FIELD, Units.NEUTRAL_RICH_MINERAL_FIELD750,
                    Units.NEUTRAL_PURIFIER_MINERAL_FIELD, Units.NEUTRAL_PURIFIER_MINERAL_FIELD750,
                    Units.NEUTRAL_PURIFIER_RICH_MINERAL_FIELD, Units.NEUTRAL_PURIFIER_RICH_MINERAL_FIELD750,
                    Units.NEUTRAL_LAB_MINERAL_FIELD, Units.NEUTRAL_LAB_MINERAL_FIELD750,
                    Units.NEUTRAL_BATTLE_STATION_MINERAL_FIELD, Units.NEUTRAL_BATTLE_STATION_MINERAL_FIELD750,
                    Units.NEUTRAL_VESPENE_GEYSER, Units.NEUTRAL_PROTOSS_VESPENE_GEYSER,
                    Units.NEUTRAL_SPACE_PLATFORM_GEYSER, Units.NEUTRAL_PURIFIER_VESPENE_GEYSER,
                    Units.NEUTRAL_SHAKURAS_VESPENE_GEYSER, Units.NEUTRAL_RICH_VESPENE_GEYSER
            ));
            return nodes.contains(unitInPool.unit().getType());
        });

        List<Point> expansionLocations = new ArrayList<>();
        Map<Point2d, List<UnitInPool>> clusters = cluster(resources, 15);
        for (Map.Entry<Point2d, List<UnitInPool>> cluster : clusters.entrySet()) {

            Point2d basePos = cluster.getKey();
            List<UnitInPool> nodes = cluster.getValue();

            //estimate base position
            basePos = estimateBasePos(basePos, nodes);

            //adjust basePos by grid restraints on each resource node in the cluster
            while (true) {
                Point2d finalBasePos = basePos;
                nodes = nodes.stream()
                        .sorted(Comparator.comparing(u -> u.unit().getPosition().toPoint2d().distance(finalBasePos)))
                        .collect(Collectors.toList());
                Point2d adjustedPoint = pushAwayFromNodes(basePos, nodes);
                if (adjustedPoint != null) {
                    basePos = adjustedPoint;
                    continue;
                }
                adjustedPoint = pullTowardsNodes(basePos, nodes);
                if (adjustedPoint != null) {
                    basePos = adjustedPoint;
                    continue;
                }
                break;
            }
            Point basePoint = basePos.toPoint(observation.terrainHeight(basePos));
            if (debug != null) {
                debug.debugBoxOut(basePoint.add(2.5f, 2.5f, 0), basePoint.sub(2.5f, 2.5f, 0), Color.RED);
            }
            expansionLocations.add(basePoint);
        }
        return expansionLocations;
    }


    /**
     * Calculates expansion locations
     * Note: bases that are blocked by destructible rocks or small minerals are included in this list
     *
     * @param debug If provided CalculateExpansionLocations will render boxes to show what it calculated.
     * @deprecated use {@link #calculateExpansionLocations(ObservationInterface observation, DebugInterface debug)} instead.
     */
    @Deprecated
    default List<Point> calculateExpansionLocations(
            ObservationInterface observation, DebugInterface debug, ExpansionParameters parameters) {
        return calculateExpansionLocations(observation, debug);
    }

    /**
     * Calculates expansion locations
     * Note: bases that are blocked by destructible rocks or small minerals are included in this list
     */
    default List<Point> calculateExpansionLocations(ObservationInterface observation) {
        return calculateExpansionLocations(observation, null);
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
     * @param units List of minerals and vespian geyser units
     * @param distanceApart Range to consider as apart of the same base
     * @return Map of minerals/gas clusters (one entry for each base)
     */
    static Map<Point2d, List<UnitInPool>> cluster(List<UnitInPool> units, double distanceApart) {
        return cluster(units, distanceApart,true);
    }


    /**
     * Clusters units within some distance of each other and returns a list of them and their center of mass.
     * @param units List of minerals and vespian geyser units
     * @param distanceApart Range to consider as apart of the same base
     * @param isElevationResticted if true, minerals/gas will only be apart of the same cluster if they are at the same elevation
     * @return Map of minerals/gas clusters (one entry for each base)
     */
    static Map<Point2d, List<UnitInPool>> cluster(List<UnitInPool> units, double distanceApart, boolean isElevationResticted) {
        Map<Point2d, List<UnitInPool>> clusters = new LinkedHashMap<>();
        for (UnitInPool u : units) {
            double distance = Double.MAX_VALUE;
            Point2d unitPos = u.unit().getPosition().toPoint2d();
            Map.Entry<Point2d, List<UnitInPool>> targetCluster = null;

            // Find the cluster this mineral patch is closest to.
            for (Map.Entry<Point2d, List<UnitInPool>> cluster : clusters.entrySet()) {
                double d = unitPos.distance(cluster.getKey());
                if (d < distance && (!isElevationResticted || isSameElevation(u.unit().getPosition(), cluster))) {
                    distance = d;
                    targetCluster = cluster;
                }
            }

            // If the target cluster is some distance away don't use it.
            if (targetCluster == null || distance > distanceApart) {
                ArrayList<UnitInPool> unitsInCluster = new ArrayList<>();
                unitsInCluster.add(u);
                clusters.put(unitPos, unitsInCluster);
                continue;
            }

            // Otherwise append to that cluster and update it's center of mass.
            if (targetCluster.getValue() == null) {
                targetCluster.setValue(new ArrayList<>());
            }
            targetCluster.getValue().add(u);
            Point2d centerOfCluster = getCenterPos(targetCluster.getValue());
            clusters.put(centerOfCluster, clusters.remove(targetCluster.getKey()));
        }
        return clusters;
    }

    private static Point2d getCenterPos(List<UnitInPool> unitList) {
        float minX, maxX, minY, maxY;
        minX = minY = Float.MAX_VALUE;
        maxX = maxY = 0;
        for (UnitInPool u : unitList) {
            Point2d p = u.unit().getPosition().toPoint2d();
            minX = Math.min(p.getX(), minX);
            maxX = Math.max(p.getX(), maxX);
            minY = Math.min(p.getY(), minY);
            maxY = Math.max(p.getY(), maxY);
        }
        return Point2d.of((minX+maxX)/2f, (minY+maxY)/2f);
    }

    private static Point2d estimateBasePos(Point2d basePos, List<UnitInPool> nodes) {
        for (int i=0; i<6; i++) {
            Point2d finalBestGuess = basePos;
            Point2d closestNodePos = nodes.stream()
                    .min(Comparator.comparing(node -> node.unit().getPosition().toPoint2d().distance(finalBestGuess))).get()
                    .unit().getPosition().toPoint2d();
            basePos = closestNodePos.towards(basePos, 6.2f);
        }
        basePos = Point2d.of(basePos.getX(), basePos.getY()).toNearestHalfPoint();
        return basePos;
    }

    private static Point2d pullTowardsNodes(Point2d basePos, List<UnitInPool> nodes) {
        for (int i = nodes.size() - 1; i >= 0; i--) {
            UnitInPool node = nodes.get(i);
            Point2d nodePos = node.unit().getPosition().toPoint2d().roundToHalfPointAccuracy();
            boolean isMineralNode = node.unit().getType().toString().contains("MINERAL");
            float xMinDistCenter = isMineralNode ? 6.5f : 7;
            float yMinDistCenter = isMineralNode ? 6f : 7;
            float xMaxDist = isMineralNode ? 7.5f : 7;
            float yMaxDist = 7;
            float xDist = Math.abs(nodePos.getX() - basePos.getX());
            float yDist = Math.abs(nodePos.getY() - basePos.getY());
            if (xDist < yDist) {
                if (xDist < xMinDistCenter && yDist > yMaxDist) {
                    return moveYFromNodeBy(basePos, nodePos, yMaxDist);
                }
            }
            else {
                if (yDist < yMinDistCenter && xDist > xMaxDist) {
                    return moveXFromNodeBy(basePos, nodePos, xMaxDist);
                }
            }
        }
        return null;
    }

    private static Point2d pushAwayFromNodes(Point2d basePos, List<UnitInPool> nodes) {
        for (UnitInPool node : nodes) {
            Point2d nodePos = node.unit().getPosition().toPoint2d().roundToHalfPointAccuracy();
            boolean isMineralNode = node.unit().getType().toString().contains("MINERAL");
            float xMinDistCenter = isMineralNode ? 6.5f : 7;
            float xMinDistCorner = isMineralNode ? 5.5f : 6;
            float yMinDistCenter = isMineralNode ? 6f : 7;
            float yMinDistCorner = isMineralNode ? 5f : 6;
            float xDist = Math.abs(nodePos.getX() - basePos.getX());
            float yDist = Math.abs(nodePos.getY() - basePos.getY());
            if (xDist < yDist) {
                if (xDist < xMinDistCorner && yDist < yMinDistCenter) {
                    return moveYFromNodeBy(basePos, nodePos, yMinDistCenter);
                }
                else if (xDist < xMinDistCenter && yDist < yMinDistCorner) {
                    return moveYFromNodeBy(basePos, nodePos, yMinDistCorner);
                }
            }
            else {
                if (yDist < yMinDistCorner && xDist < xMinDistCenter) {
                    return moveXFromNodeBy(basePos, nodePos, xMinDistCenter);
                }
                else if (yDist < yMinDistCenter && xDist < xMinDistCorner) {
                    return moveXFromNodeBy(basePos, nodePos, xMinDistCorner);
                }
            }
        }
        return null;
    }

    private static Point2d moveXFromNodeBy(Point2d origin, Point2d nodePos, float distance) {
        float newX;
        if (origin.getX() < nodePos.getX()) {
            newX = nodePos.getX() - distance;
        }
        else {
            newX = nodePos.getX() + distance;
        }
        return Point2d.of(newX, origin.getY());
    }

    private static Point2d moveYFromNodeBy(Point2d origin, Point2d nodePos, float distance) {
        float newY;
        if (origin.getY() < nodePos.getY()) {
            newY = nodePos.getY() - distance;
        }
        else {
            newY = nodePos.getY() + distance;
        }
        return Point2d.of(origin.getX(), newY);
    }

    private static boolean isSameElevation(Point newNode, Map.Entry<Point2d, List<UnitInPool>> cluster) {
        return cluster.getValue().stream().allMatch(node -> Math.abs(node.unit().getPosition().getZ() - newNode.getZ()) < 1.2);
    }
}
