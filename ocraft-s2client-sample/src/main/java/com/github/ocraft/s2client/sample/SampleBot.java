package com.github.ocraft.s2client.sample;

/*-
 * #%L
 * ocraft-s2client-sample
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

import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.debug.Color;
import com.github.ocraft.s2client.protocol.game.*;
import com.github.ocraft.s2client.protocol.game.raw.StartRaw;
import com.github.ocraft.s2client.protocol.response.ResponseGameInfo;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Alliance;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Predicate;

// Bot builds supply depots as required.
// Bot builds 15 SCVs.
// Bot builds a few barracks.
// Bot builds marines.
// Bot finds enemy, sends marines.
public class SampleBot {

    private static class TestBot extends S2Agent {

        private static final int TARGET_SCV_COUNT = 15;
        private static final int TARGET_BARRACKS_COUNT = 3;

        private ResponseGameInfo gameInfo;

        @Override
        public void onGameStart() {
            System.out.println("Hello world of Starcraft II bots!");
            debug().debugTextOut("Welcome to the Ocraft SDK!", Color.BLUE);
            gameInfo = observation().getGameInfo();

            debug().sendDebug();
        }

        @Override
        public void onStep() {
            // If there are marines and the command center is not found, send them scouting.
            scoutWithMarines();
            tryBuildSupplyDepot(); // Build supply depots if they are needed.
            tryBuildSCV(); // Build TERRAN_SCV's if they are needed.
            tryBuildBarracks(); // Build Barracks if they are ready to be built.
            tryBuildMarine(); // Just keep building marines if possible.
        }

        private void scoutWithMarines() {
            observation()
                    .getUnits(Alliance.SELF, isIdleTerranMarine())
                    .forEach(unitInPool -> findEnemyStructure().ifPresentOrElse(
                            attackEnemyStructureWith(unitInPool),
                            attackEnemyPositionWith(unitInPool)));
        }

        private Predicate<UnitInPool> isIdleTerranMarine() {
            return unitInPool -> UnitInPool.isUnit(Units.TERRAN_MARINE).test(unitInPool) &&
                    unitInPool.unit().getOrders().isEmpty();
        }

        private Optional<UnitInPool> findEnemyStructure() {
            return observation().getUnits(Alliance.ENEMY, isStructure()).stream().findFirst();
        }

        private Predicate<UnitInPool> isStructure() {
            return unitInPool -> Set
                    .of((UnitType) Units.TERRAN_COMMAND_CENTER, Units.TERRAN_SUPPLY_DEPOT, Units.TERRAN_BARRACKS)
                    .contains(unitInPool.unit().getType());
        }

        private Consumer<UnitInPool> attackEnemyStructureWith(UnitInPool unitInPool) {
            return enemyUnit -> actions().unitCommand(unitInPool.unit(), Abilities.ATTACK, enemyUnit.unit(), false);
        }

        private Runnable attackEnemyPositionWith(UnitInPool unitInPool) {
            return () -> findEnemyPosition()
                    .ifPresent(point2d -> actions().unitCommand(unitInPool.unit(), Abilities.SMART, point2d, false));
        }

        // Tries to find a random location that can be pathed to on the map.
        // Returns Point2d if a new, random location has been found that is pathable by the unit.
        private Optional<Point2d> findEnemyPosition() {
            Optional<StartRaw> startRaw = gameInfo.getStartRaw();
            if (startRaw.isPresent()) {
                Set<Point2d> startLocations = new HashSet<>(startRaw.get().getStartLocations());
                startLocations.remove(observation().getStartLocation().toPoint2d());
                if (startLocations.isEmpty()) return Optional.empty();
                return Optional.of(new ArrayList<>(startLocations)
                        .get(ThreadLocalRandom.current().nextInt(startLocations.size())));
            } else {
                return Optional.empty();
            }
        }

        private boolean tryBuildSupplyDepot() {
            // If we are not supply capped, don't build a supply depot.
            if (observation().getFoodUsed() <= observation().getFoodCap() - 2) {
                return false;
            }

            // Try and build a depot. Find a random TERRAN_SCV and give it the order.
            return tryBuildStructure(Abilities.BUILD_SUPPLY_DEPOT, Units.TERRAN_SCV);
        }

        private boolean tryBuildStructure(Ability abilityTypeForStructure, UnitType unitType) {
            // If a unit already is building a supply structure of this type, do nothing.
            if (!observation().getUnits(Alliance.SELF, doesBuildWith(abilityTypeForStructure)).isEmpty()) {
                return false;
            }

            // Just try a random location near the unit.
            Optional<UnitInPool> unitInPool = getRandomUnit(unitType);
            if (unitInPool.isPresent()) {
                Unit unit = unitInPool.get().unit();
                actions().unitCommand(
                        unit,
                        abilityTypeForStructure,
                        unit.getPosition().toPoint2d().add(Point2d.of(getRandomScalar(), getRandomScalar()).mul(15.0f)),
                        false);
                return true;
            } else {
                return false;
            }

        }

        private Optional<UnitInPool> getRandomUnit(UnitType unitType) {
            List<UnitInPool> units = observation().getUnits(Alliance.SELF, UnitInPool.isUnit(unitType));
            return units.isEmpty()
                    ? Optional.empty()
                    : Optional.of(units.get(ThreadLocalRandom.current().nextInt(units.size())));
        }

        private float getRandomScalar() {
            return ThreadLocalRandom.current().nextFloat() * 2 - 1;
        }

        private Predicate<UnitInPool> doesBuildWith(Ability abilityTypeForStructure) {
            return unitInPool -> unitInPool.unit()
                    .getOrders()
                    .stream()
                    .anyMatch(unitOrder -> abilityTypeForStructure.equals(unitOrder.getAbility()));
        }

        private boolean tryBuildBarracks() {
            // Wait until we have our quota of TERRAN_SCV's.
            if (countUnitType(Units.TERRAN_SCV) < TARGET_SCV_COUNT) {
                return false;
            }

            // One build 3 barracks.
            if (countUnitType(Units.TERRAN_BARRACKS) > TARGET_BARRACKS_COUNT) {
                return false;
            }

            return tryBuildStructure(Abilities.BUILD_BARRACKS, Units.TERRAN_SCV);
        }

        private int countUnitType(Units unitType) {
            return observation().getUnits(Alliance.SELF, UnitInPool.isUnit(unitType)).size();
        }

        private boolean tryBuildUnit(Ability abilityTypeForUnit, UnitType unitType) {
            Optional<UnitInPool> unitInPool = getRandomUnit(unitType);
            if (unitInPool.isPresent()) {
                Unit unit = unitInPool.get().unit();
                if (!unit.getOrders().isEmpty()) {
                    return false;
                }
                actions().unitCommand(unit, abilityTypeForUnit, false);
                return true;
            } else {
                return false;
            }
        }

        private boolean tryBuildSCV() {
            if (countUnitType(Units.TERRAN_SCV) >= TARGET_SCV_COUNT) {
                return false;
            }
            return tryBuildUnit(Abilities.TRAIN_SCV, Units.TERRAN_COMMAND_CENTER);
        }

        private boolean tryBuildMarine() {
            return tryBuildUnit(Abilities.TRAIN_MARINE, Units.TERRAN_BARRACKS);
        }

        @Override
        public void onUnitIdle(UnitInPool unitInPool) {
            switch ((Units) unitInPool.unit().getType()) {
                case TERRAN_SCV:
                    getRandomUnit(Units.TERRAN_COMMAND_CENTER).ifPresent(mineIdleWorker(unitInPool));
                    break;
            }
        }

        private Consumer<UnitInPool> mineIdleWorker(UnitInPool unitInPool) {
            return commandCenter -> findNearestMineralPatch(commandCenter.unit().getPosition().toPoint2d())
                    .ifPresent(mineralPath ->
                            actions().unitCommand(unitInPool.unit(), Abilities.HARVEST_GATHER, mineralPath, false));
        }

        private Optional<Unit> findNearestMineralPatch(Point2d start) {
            List<UnitInPool> units = observation().getUnits(Alliance.NEUTRAL);
            double distance = Double.MAX_VALUE;
            Unit target = null;
            for (UnitInPool unitInPool : units) {
                Unit unit = unitInPool.unit();
                if (unit.getType().equals(Units.NEUTRAL_MINERAL_FIELD)) {
                    double d = unit.getPosition().toPoint2d().distance(start);
                    if (d < distance) {
                        distance = d;
                        target = unit;
                    }
                }
            }
            return Optional.ofNullable(target);
        }

        @Override
        public void onGameEnd() {
            control().dumpProtoUsage();
        }
    }

    public static void main(String[] args) {
        TestBot bot = new TestBot();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(args)
                .setParticipants(
                        S2Coordinator.createParticipant(Race.TERRAN, bot),
                        S2Coordinator.createComputer(Race.TERRAN, Difficulty.VERY_EASY))
                .launchStarcraft()
                .startGame(BattlenetMap.of("Lava Flow"));

        while (s2Coordinator.update()) {
        }

        s2Coordinator.quit();
    }

    static void runAsHost(String[] args) {
        TestBot bot = new TestBot();
        // A clientPorts set must exist for each client, even though one of them is the server.
        // Both agents must use the same MultiplayerOptions in their JoinGame requests.
        MultiplayerOptions multiplayerOptions = MultiplayerOptions.multiplayerSetup()
                .sharedPort(5000)
                .serverPort(PortSet.of(5001, 5002))
                .clientPorts(
                        PortSet.of(5003, 5004),
                        PortSet.of(5005, 5006))
                .build();
        // One agent must create and join the game. The other agent only needs to join the game.
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(args)
                .setTimeoutMS(120000)
                .setMultiplayerOptions(multiplayerOptions)
                .setParticipants(
                        S2Coordinator.createParticipant(Race.TERRAN, bot),
                        S2Coordinator.createParticipant(Race.TERRAN))
                .launchStarcraft(4000)
                .startGame(BattlenetMap.of("Lava Flow"));

        while (s2Coordinator.update()) {
        }

        s2Coordinator.quit();
    }

    static void runAsClient(String[] args) {
        TestBot bot = new TestBot();
        // A clientPorts set must exist for each client, even though one of them is the server.
        // Both agents must use the same MultiplayerOptions in their JoinGame requests.
        MultiplayerOptions multiplayerOptions = MultiplayerOptions.multiplayerSetup()
                .sharedPort(5000)
                .serverPort(PortSet.of(5001, 5002))
                .clientPorts(
                        PortSet.of(5003, 5004),
                        PortSet.of(5005, 5006))
                .build();
        // One agent must create and join the game. The other agent only needs to join the game.
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(args)
                .setTimeoutMS(120000)
                .setMultiplayerOptions(multiplayerOptions)
                .setParticipants(
                        S2Coordinator.createParticipant(Race.TERRAN, bot),
                        S2Coordinator.createParticipant(Race.TERRAN))
                .launchStarcraft(4001)
                .joinGame();

        while (s2Coordinator.update()) {
        }

        s2Coordinator.quit();
    }

}
