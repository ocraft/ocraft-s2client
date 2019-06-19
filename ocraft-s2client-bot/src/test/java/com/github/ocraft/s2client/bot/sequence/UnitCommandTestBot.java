package com.github.ocraft.s2client.bot.sequence;

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

import com.github.ocraft.s2client.bot.Fixtures;
import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.bot.gateway.ActionInterface;
import com.github.ocraft.s2client.bot.gateway.ObservationInterface;
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.data.*;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.unit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public class UnitCommandTestBot extends UnitTestBot {

    private UnitCommandTestBot() {
        add(new TestAttackAttack());
        add(new TestBehaviorCloakBanshee());
        add(new TestBehaviorSalvage());
        add(new TestBuildBarracks());
        add(new TestBuildBarracksTechLab());
        add(new TestBuildFlyingBarracksTechLab());
        add(new TestBuildBarracksTechLabDifferentPoint());
        add(new TestBuildBarracksTechLabSamePoint());
        add(new TestBuildExtractor());
        add(new TestBuildForge());
        add(new TestBuildGateway());
        add(new TestBuildPylon());
        add(new TestBuildSpineCrawler());
        add(new TestCancelBuildInProgressFactory());
        add(new TestCancelTrainingQueue());
//        add(new TestEffectArchon()); // TODO p.picheta bug -> https://github.com/Blizzard/s2client-api/issues/279
        add(new TestEffectBlink());
        add(new TestEffectHeal());
        add(new TestEffectScan());
        add(new TestEffectStimpack());
        add(new TestHarvestProbeHarvestMinerals());
//        add(new TestHarvestProbeHarvestVespene());
//        add(new TestMorphBroodLord());
//        add(new TestMorphLair());
//        add(new TestMorphSiegeTankSiegeMode());
//        add(new TestMorphStarportLiftOff());
//        add(new TestMorphWarpGate());
//        add(new TestEffectMoveMove());
//        add(new TestEffectMovePatrol());
//        add(new TestRallyRally());
//        add(new TestResearchTerranInfantryArmorLevel2());
//        add(new TestSensorTower());
//        add(new TestShields());
//        add(new TestStopStop());
//        add(new TestTrainWarpZealot());
//        add(new TestTrainBaneling());
//        add(new TestTrainMarine());
//        add(new TestTrainRoach());
//        add(new TestTransportBunkerLoad());
//        add(new TestTransportBunkerUnloadAll());
    }

    @Override
    protected void onTestsBegin() {
        PointI mapCenter = observation().getGameInfo().findCenterOfMap();

        debug().debugEnemyControl();
        debug().debugShowMap();
        debug().debugMoveCamera(mapCenter);
        debug().debugGiveAllResources();
        debug().debugIgnoreFood();
        debug().debugGiveAllTech();
        debug().debugGiveAllUpgrades();
        debug().debugFastBuild();
    }

    @Override
    protected void onTestsEnd() {

    }

    public static boolean test() {
        UnitCommandTestBot bot = new UnitCommandTestBot();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(new String[]{})
                .setParticipants(S2Coordinator.createParticipant(Race.TERRAN, bot))
                .launchStarcraft()
                .startGame(LocalMap.of(Fixtures.MAP_EMPTY));

        while (!bot.isFinished()) {
            s2Coordinator.update();
        }
        s2Coordinator.quit();
        return bot.success();
    }
}

abstract class TestUnitCommand extends TestSequence {

    long orderOnGameLoop;
    UnitType testUnitType;
    Ability testAbility;
    Point2d originPt;
    List<UnitInPool> testUnits = new ArrayList<>();
    UnitInPool testUnit;
    List<UnitInPool> idledUnits = new ArrayList<>();
    UnitType targetUnitType;
    boolean ordersVerified;
    boolean abilityCommandSent;
    boolean instantCast;
    boolean placingStructure;

    @Override
    public void onTestStart() {
        setTestTime();
        orderOnGameLoop = agent().observation().getGameLoop() + 10;
        setOriginPoint();

        agent().debug().debugCreateUnit(testUnitType, originPt, agent().observation().getPlayerId(), 1);
        agent().debug().sendDebug();
        additionalTestSetup();
    }

    @Override
    public void onStep() {
        if (agent().observation().getGameLoop() < orderOnGameLoop) return;
        testUnits = agent().observation().getUnits(
                Alliance.SELF,
                unitInPool -> unitInPool.unit().getType().equals(testUnitType));
        if (testUnits.size() > 0) {
            testUnit = testUnits.get(0);
        }
        issueUnitCommand(agent().actions());
    }

    @Override
    public void onTestFinish() {
        verifyUnitIdleAfterOrder(testUnitType);
        killAllUnits();
    }

    @Override
    public void onUnitIdle(UnitInPool unit) {
        if (agent().observation().getGameLoop() >= orderOnGameLoop) {
            idledUnits.add(unit);
        }
    }

    void additionalTestSetup() {

    }

    void issueUnitCommand(ActionInterface act) {
    }

    Point2d getPointOffsetX(Point2d startingPoint, float offset) {
        return startingPoint.add(offset, 0);
    }

    List<Tag> getTagListFromUnits(List<UnitInPool> units) {
        return units.stream().map(UnitInPool::getTag).collect(Collectors.toList());
    }

    void setOriginPoint() {
        originPt = agent().observation().getGameInfo().findCenterOfMap().toPoint2d();
    }

    void setTestTime() {
        setWaitGameLoops(150);
    }

    void verifyUnitOrders(UnitInPool unitInPool, Ability testAbility) {
        if (doesNotHaveOrder(unitInPool, testAbility)) {
            reportError("Expected order not in unit orders");
        }
        ordersVerified = true;
    }

    boolean doesNotHaveOrder(UnitInPool unitInPool, Ability testAbility) {
        return unitInPool.unit().getOrders().stream().noneMatch(unitOrder -> unitOrder.getAbility().equals(testAbility));
    }

    void verifyUnitExistsAndComplete(UnitType unitType) {
        verifyUnitExistsAndComplete(unitType, true);
    }

    void verifyUnitExistsAndComplete(UnitType unitType, boolean verifyComplete) {
        Optional<Unit> testUnit = Optional.empty();
        for (UnitInPool unitInPool : agent().observation().getUnits(Alliance.SELF)) {
            if (unitInPool.unit().getType().equals(unitType)) {
                testUnit = Optional.of(unitInPool.unit());
            }
        }

        if (verifyComplete && testUnit.isPresent() && testUnit.get().getBuildProgress() != 1.0f) {
            reportError("Unit building/training did not complete as expected.");
        }

        if (!testUnit.isPresent()) {
            reportError("Expected unit does not exist.");
        }
    }

    void verifyUnitDoesNotExist(UnitType unitType) {
        agent().observation().getUnits(Alliance.SELF, unitInPool -> unitInPool.unit().getType().equals(unitType))
                .stream()
                .findAny()
                .ifPresent(unitInPool -> reportError("Unexpected unit exists when it should not."));
    }

    void verifyUnitIdleAfterOrder(UnitType unitType) {
        if (idledUnits.stream().noneMatch(unitInPool -> unitInPool.unit().getType().equals(unitType))) {
            reportError("Unit did not trigger OnUnitIdle as expected.");
        }
    }

    void verifyUpgradeOwned(Upgrade testUpgrade) {
        verifyUpgradeOwned(testUpgrade, false);
    }

    void verifyUpgradeOwned(Upgrade testUpgrade, boolean verifyNotOwned) {
        if (agent().observation().getUpgradeData(false).containsKey(testUpgrade)) {
            reportError("Upgrade in test not found in upgrade data.");
        }
        boolean upgradeOwned = agent().observation().getUpgrades().contains(testUpgrade);
        if (!verifyNotOwned && !upgradeOwned) {
            reportError("Upgrade in test is not owned.");
        }
        if (verifyNotOwned && upgradeOwned) {
            reportError("Upgrade in test is already owned.");
        }
    }

    void verifyOwnerOfUnits(List<UnitInPool> units, int expectedOwner) {
        if (units.stream().anyMatch(unitInPool -> unitInPool.unit().getOwner() != expectedOwner)) {
            reportError("Owner of unit does not match expected value.");
        }
    }

    List<Unit> testUnits() {
        return testUnits.stream().map(UnitInPool::unit).collect(Collectors.toList());
    }

    boolean doesNotHaveMaxHealth(UnitInPool unitInPool) {
        return !unitInPool.unit().getHealth().isPresent() ||
                !unitInPool.unit().getHealthMax().isPresent() ||
                !unitInPool.unit().getHealth().get().equals(unitInPool.unit().getHealthMax().get());
    }

    Optional<UnitInPool> unitSelf(UnitType unitType) {
        List<UnitInPool> units = agent()
                .observation()
                .getUnits(Alliance.SELF, UnitInPool.isUnit(unitType));
        if (units.size() > 0) {
            return Optional.of(units.get(0));
        } else {
            return Optional.empty();
        }
    }

    Optional<UnitInPool> unit(UnitType unitType) {
        List<UnitInPool> units = agent()
                .observation()
                .getUnits(UnitInPool.isUnit(unitType));

        if (units.size() > 0) {
            return Optional.of(units.get(0));
        } else {
            return Optional.empty();
        }
    }
}

abstract class TestUnitCommandNoTarget extends TestUnitCommand {
    @Override
    void issueUnitCommand(ActionInterface act) {
        if (abilityCommandSent) return;
        verifyOwnerOfUnits(testUnits, 1);
        if (testUnits.size() > 1) {
            act.unitCommand(testUnits(), testAbility, false);
        } else {
            act.unitCommand(testUnit.unit(), testAbility, false);
        }
        abilityCommandSent = true;
    }
}

abstract class TestUnitCommandTargetingPoint extends TestUnitCommand {

    Point2d targetPoint;

    @Override
    void additionalTestSetup() {
        targetPoint = getPointOffsetX(originPt, 5);
    }

    @Override
    void issueUnitCommand(ActionInterface act) {
        if (!ordersVerified && abilityCommandSent) {
            if (!instantCast) {
                verifyUnitOrders(testUnit, testAbility);
            }
        }
        if (abilityCommandSent) return;
        verifyOwnerOfUnits(testUnits, 1);
        if (placingStructure && !agent().query().placement(testAbility, targetPoint)) {
            reportError("Cannot place structure, location not valid.");
            return;
        }
        if (testUnits.size() > 1) {
            act.unitCommand(testUnits(), testAbility, targetPoint, false);
        } else {
            act.unitCommand(testUnit.unit(), testAbility, targetPoint, false);
        }
        abilityCommandSent = true;
    }
}

abstract class TestUnitCommandTargetingUnit extends TestUnitCommand {
    Point2d targetPoint;
    UnitInPool targetUnit;

    @Override
    void additionalTestSetup() {
        targetPoint = getPointOffsetX(originPt, 5);
        agent().debug().debugCreateUnit(targetUnitType, targetPoint, agent().observation().getPlayerId(), 1);
        agent().debug().sendDebug();
    }

    @Override
    void issueUnitCommand(ActionInterface act) {
        if (!ordersVerified && abilityCommandSent) {
            if (!instantCast) {
                verifyUnitOrders(testUnit, testAbility);
            }
        }
        if (abilityCommandSent) return;
        targetUnit = agent().observation()
                .getUnits()
                .stream()
                .filter(unitInPool -> unitInPool.unit().getType().equals(targetUnitType))
                .findAny().orElseThrow(() -> {
                    reportError("Target unit not found");
                    return new AssertionError("Target unit not found");
                });
        if (testUnits.size() > 1) {
            act.unitCommand(testUnits(), testAbility, targetUnit.unit(), false);
        } else {
            act.unitCommand(testUnit.unit(), testAbility, targetUnit.unit(), false);
        }
        abilityCommandSent = true;
    }

}

class TestAttackAttack extends TestUnitCommandTargetingPoint {

    TestAttackAttack() {
        testUnitType = Units.TERRAN_MARINE;
        testAbility = Abilities.ATTACK;
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(50);
    }

}

class TestBehaviorCloakBanshee extends TestUnitCommandNoTarget {
    TestBehaviorCloakBanshee() {
        testUnitType = Units.TERRAN_BANSHEE;
        testAbility = Abilities.BEHAVIOR_CLOAK_ON;
    }

    @Override
    public void onTestFinish() {
        if (!isSet(testUnit)) {
            reportError("Could not find the test unit.");
        }
        if (isSet(testUnit) && (!testUnit.unit().getCloakState().isPresent() ||
                !testUnit.unit().getCloakState().get().equals(CloakState.CLOAKED_ALLIED))) {
            reportError("Unit is not cloaked as expected.");
        }
        verifyUnitIdleAfterOrder(testUnitType);
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(50);
    }
}

class TestBehaviorSalvage extends TestUnitCommandNoTarget {
    TestBehaviorSalvage() {
        testUnitType = Units.TERRAN_BUNKER;
        testAbility = Abilities.EFFECT_SALVAGE;
    }

    @Override
    public void onTestFinish() {
        verifyUnitDoesNotExist(testUnitType);
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(200);
    }
}

class TestBuildBarracks extends TestUnitCommandTargetingPoint {
    TestBuildBarracks() {
        testUnitType = Units.TERRAN_SCV;
        testAbility = Abilities.BUILD_BARRACKS;
        placingStructure = true;
    }

    @Override
    public void onTestFinish() {
        verifyUnitExistsAndComplete(Units.TERRAN_BARRACKS);
        verifyUnitIdleAfterOrder(testUnitType);
        verifyUnitIdleAfterOrder(Units.TERRAN_BARRACKS);
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(150);
    }
}

class TestBuildBarracksTechLab extends TestUnitCommandNoTarget {
    TestBuildBarracksTechLab() {
        testUnitType = Units.TERRAN_BARRACKS;
        testAbility = Abilities.BUILD_TECHLAB;
    }

    @Override
    public void onTestFinish() {
        List<UnitInPool> barracks = agent()
                .observation()
                .getUnits(Alliance.SELF, UnitInPool.isUnit(Units.TERRAN_BARRACKS));

        if (barracks.size() != 1) {
            reportError("Could not find landed barracks.");
        }

        // Find the tech lab.
        List<UnitInPool> techlab = agent()
                .observation()
                .getUnits(Alliance.SELF, UnitInPool.isUnit(Units.TERRAN_BARRACKS_TECHLAB));

        if (techlab.size() != 1) {
            reportError("Could not find a tech lab.");
        }

        if (barracks.size() == 1 && techlab.size() == 1) {
            Tag addOnTag = barracks.get(0).unit().getAddOnTag().orElse(Tag.of(0L));
            if (addOnTag.equals(Tag.of(0L))) {
                reportError("Barracks does not have an add-on.");
            }
            if (!addOnTag.equals(techlab.get(0).getTag())) {
                reportError("Barracks add on tag does not match tech lab tag.");
            }

        }

        verifyUnitExistsAndComplete(Units.TERRAN_BARRACKS_TECHLAB);
        verifyUnitIdleAfterOrder(testUnitType);
        verifyUnitIdleAfterOrder(Units.TERRAN_BARRACKS_TECHLAB);
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(200);
    }
}

class TestBuildFlyingBarracksTechLab extends TestUnitCommandTargetingPoint {
    TestBuildFlyingBarracksTechLab() {
        testUnitType = Units.TERRAN_BARRACKS_FLYING;
        testAbility = Abilities.BUILD_TECHLAB;
    }

    @Override
    public void onTestFinish() {
        // Barracks will have landed. Find it.
        List<UnitInPool> barracks = agent()
                .observation()
                .getUnits(Alliance.SELF, UnitInPool.isUnit(Units.TERRAN_BARRACKS));

        if (barracks.size() != 1) {
            reportError("Could not find landed barracks.");
        }

        // Find the tech lab.
        List<UnitInPool> techlab = agent()
                .observation()
                .getUnits(Alliance.SELF, UnitInPool.isUnit(Units.TERRAN_BARRACKS_TECHLAB));

        if (techlab.size() != 1) {
            reportError("Could not find a tech lab.");
        }

        if (barracks.size() == 1 && techlab.size() == 1) {
            Tag addOnTag = barracks.get(0).unit().getAddOnTag().orElse(Tag.of(0L));
            if (addOnTag.equals(Tag.of(0L))) {
                reportError("Barracks does not have an add-on.");
            }
            if (!addOnTag.equals(techlab.get(0).getTag())) {
                reportError("Barracks add on tag does not match tech lab tag.");
            }

        }

        verifyUnitExistsAndComplete(Units.TERRAN_BARRACKS_TECHLAB);
        verifyUnitIdleAfterOrder(Units.TERRAN_BARRACKS);
        verifyUnitIdleAfterOrder(Units.TERRAN_BARRACKS_TECHLAB);
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(200);
    }
}

class TestBuildBarracksTechLabDifferentPoint extends TestUnitCommandTargetingPoint {
    TestBuildBarracksTechLabDifferentPoint() {
        testUnitType = Units.TERRAN_BARRACKS;
        testAbility = Abilities.BUILD_TECHLAB;
    }

    @Override
    public void onTestFinish() {
        // Barracks will have landed. Find it.
        List<UnitInPool> barracks = agent()
                .observation()
                .getUnits(Alliance.SELF, UnitInPool.isUnit(Units.TERRAN_BARRACKS));

        if (barracks.size() != 1) {
            reportError("Could not find landed barracks.");
        }

        // Find the tech lab.
        List<UnitInPool> techlab = agent()
                .observation()
                .getUnits(Alliance.SELF, UnitInPool.isUnit(Units.TERRAN_BARRACKS_TECHLAB));

        if (techlab.size() != 1) {
            reportError("Could not find a tech lab.");
        }

        if (barracks.size() == 1 && techlab.size() == 1) {
            Tag addOnTag = barracks.get(0).unit().getAddOnTag().orElse(Tag.of(0L));
            if (addOnTag.equals(Tag.of(0L))) {
                reportError("Barracks does not have an add-on.");
            }
            if (!addOnTag.equals(techlab.get(0).getTag())) {
                reportError("Barracks add on tag does not match tech lab tag.");
            }

        }

        verifyUnitExistsAndComplete(Units.TERRAN_BARRACKS_TECHLAB);
        verifyUnitIdleAfterOrder(testUnitType);
        verifyUnitIdleAfterOrder(Units.TERRAN_BARRACKS_TECHLAB);
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(200);
    }
}

class TestBuildBarracksTechLabSamePoint extends TestBuildBarracksTechLabDifferentPoint {
    @Override
    void additionalTestSetup() {
        targetPoint = originPt;
    }
}

class TestBuildExtractor extends TestUnitCommandTargetingUnit {
    TestBuildExtractor() {
        testUnitType = Units.ZERG_DRONE;
        targetUnitType = Units.NEUTRAL_VESPENE_GEYSER;
        testAbility = Abilities.BUILD_EXTRACTOR;
        placingStructure = true;
    }

    @Override
    public void onTestFinish() {
        verifyUnitExistsAndComplete(Units.ZERG_EXTRACTOR);
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(200);
    }
}

class TestBuildForge extends TestUnitCommandTargetingPoint {
    TestBuildForge() {
        testUnitType = Units.PROTOSS_PROBE;
        testAbility = Abilities.BUILD_FORGE;
        placingStructure = true;
    }

    @Override
    void additionalTestSetup() {
        targetPoint = getPointOffsetX(originPt, 5);

        agent().debug().debugCreateUnit(Units.PROTOSS_PYLON, originPt, agent().observation().getPlayerId(), 1);
        agent().debug().sendDebug();
    }

    @Override
    public void onTestFinish() {
        verifyUnitExistsAndComplete(Units.PROTOSS_FORGE);
        verifyUnitIdleAfterOrder(testUnitType);
        verifyUnitIdleAfterOrder(Units.PROTOSS_FORGE);
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(150);
    }
}

class TestBuildGateway extends TestUnitCommandTargetingPoint {
    TestBuildGateway() {
        testUnitType = Units.PROTOSS_PROBE;
        testAbility = Abilities.BUILD_GATEWAY;
        placingStructure = true;
    }

    @Override
    void additionalTestSetup() {
        targetPoint = getPointOffsetX(originPt, 5);

        agent().debug().debugCreateUnit(Units.PROTOSS_PYLON, originPt, agent().observation().getPlayerId(), 1);
        agent().debug().sendDebug();
    }

    @Override
    public void onTestFinish() {
        verifyUnitExistsAndComplete(Units.PROTOSS_WARP_GATE);
        verifyUnitIdleAfterOrder(testUnitType);
        // Due to a balance change that causes Gateways to auto morph into WarpGates when the warpgate tech is
        // researched, and we unlock all research at the start of the test, so verify that the gateway has
        // transformed to a warpgate
        verifyUnitIdleAfterOrder(Units.PROTOSS_WARP_GATE);
        if (agent().observation().getWarpGateCount() != 1) {
            reportError("Expected Gateway to auto morph into Warpgate, but it has not.");
        }
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(150);
    }
}

class TestBuildPylon extends TestUnitCommandTargetingPoint {
    TestBuildPylon() {
        testUnitType = Units.PROTOSS_PROBE;
        testAbility = Abilities.BUILD_PYLON;
        placingStructure = true;
    }

    @Override
    public void onTestFinish() {
        verifyUnitExistsAndComplete(Units.PROTOSS_PYLON);
        verifyUnitIdleAfterOrder(testUnitType);
        if (agent().observation().getPowerSources().size() != 1) {
            reportError("Pylon is not being counted as a power source.");
        }
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(150);
    }
}

class TestBuildSpineCrawler extends TestUnitCommandTargetingPoint {
    TestBuildSpineCrawler() {
        testUnitType = Units.ZERG_DRONE;
        testAbility = Abilities.BUILD_SPINE_CRAWLER;
        placingStructure = true;
    }

    @Override
    void additionalTestSetup() {
        orderOnGameLoop += 300;
        targetPoint = getPointOffsetX(originPt, -8);

        agent().debug().debugCreateUnit(
                Units.ZERG_CREEP_TUMOR_BURROWED,
                getPointOffsetX(originPt, -10),
                agent().observation().getPlayerId(),
                1);
        agent().debug().sendDebug();
    }

    @Override
    public void onTestFinish() {
        verifyUnitExistsAndComplete(Units.ZERG_SPINE_CRAWLER);
        if (!agent().observation().hasCreep(targetPoint)) {
            reportError("Creep is not spread.");
        }
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(450);
    }
}

class TestCancelBuildInProgressFactory extends TestUnitCommandNoTarget {

    private boolean factoryBuild = false;
    private Unit testFactory;

    TestCancelBuildInProgressFactory() {
        testUnitType = Units.TERRAN_SCV;
        testAbility = Abilities.CANCEL;
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(100);
    }

    @Override
    public void onStep() {
        ObservationInterface obs = agent().observation();
        if (obs.getGameLoop() < orderOnGameLoop) return;
        testUnit = unitSelf(testUnitType).orElse(null);

        if (!factoryBuild) {
            agent().actions().unitCommand(testUnit.unit(), Abilities.BUILD_FACTORY, originPt, false);
            factoryBuild = true;
        }

        List<UnitInPool> units = agent().observation().getUnits(Alliance.SELF, UnitInPool.isUnit(Units.TERRAN_FACTORY));
        if (units.size() > 0) {
            testFactory = units.get(0).unit();
        }

        if (obs.getGameLoop() < orderOnGameLoop + 10) return;

        if (!ordersVerified && factoryBuild) {
            verifyUnitOrders(testUnit, Abilities.BUILD_FACTORY);
        }

        if (!abilityCommandSent) {
            verifyUnitExistsAndComplete(Units.TERRAN_FACTORY, false);
            agent().actions().unitCommand(testFactory, testAbility, false);
            abilityCommandSent = true;
        }

    }

    @Override
    public void onTestFinish() {
        verifyUnitIdleAfterOrder(testUnitType);
        verifyUnitDoesNotExist(Units.TERRAN_FACTORY);
        killAllUnits();
    }
}

class TestCancelTrainingQueue extends TestUnitCommandNoTarget {

    private boolean marineTrained = false;

    TestCancelTrainingQueue() {
        testUnitType = Units.TERRAN_BARRACKS;
        testAbility = Abilities.CANCEL_LAST;
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(100);
    }

    @Override
    public void onStep() {
        ObservationInterface obs = agent().observation();
        if (obs.getGameLoop() < orderOnGameLoop) return;
        testUnit = unitSelf(testUnitType).orElse(null);

        if (!marineTrained) {
            agent().actions().unitCommand(testUnit.unit(), Abilities.TRAIN_MARINE, false);
            marineTrained = true;
        }

        if (obs.getGameLoop() < orderOnGameLoop + 10) return;

        if (!ordersVerified && marineTrained) {
            verifyUnitOrders(testUnit, Abilities.TRAIN_MARINE);
        }

        issueUnitCommand(agent().actions());
    }

    @Override
    public void onTestFinish() {
        verifyUnitIdleAfterOrder(testUnitType);
        verifyUnitDoesNotExist(Units.TERRAN_MARINE);
        killAllUnits();
    }
}

class TestEffectArchon extends TestUnitCommandNoTarget {
    TestEffectArchon() {
        testUnitType = Units.PROTOSS_HIGH_TEMPLAR;
        testAbility = Abilities.MORPH_ARCHON;
    }

    @Override
    void additionalTestSetup() {
        agent().debug().debugCreateUnit(testUnitType, originPt, agent().observation().getPlayerId(), 1);
        agent().debug().sendDebug();
    }

    @Override
    public void onTestFinish() {
        verifyUnitExistsAndComplete(Units.PROTOSS_ARCHON);
        verifyUnitIdleAfterOrder(Units.PROTOSS_ARCHON);
        verifyUnitDoesNotExist(testUnitType);
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(50);
    }
}

class TestEffectBlink extends TestUnitCommandTargetingPoint {
    TestEffectBlink() {
        testUnitType = Units.PROTOSS_STALKER;
        testAbility = Abilities.EFFECT_BLINK;
        instantCast = true;
    }

    @Override
    public void onTestFinish() {
        if (!isSet(testUnit)) {
            reportError("Could not find the test unit.");
        }

        if (isSet(testUnit) && !testUnit.unit().getPosition().toPoint2d().equals(targetPoint)) {
            reportError("New stalker location is incorrect, blink did not fire.");
        }

        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(50);
    }
}

class TestEffectHeal extends TestUnitCommandTargetingUnit {

    private boolean targetUnitDamaged = false;

    TestEffectHeal() {
        testUnitType = Units.TERRAN_MEDIVAC;
        targetUnitType = Units.TERRAN_MARAUDER;
        testAbility = Abilities.EFFECT_HEAL;
    }

    @Override
    void additionalTestSetup() {
        targetPoint = getPointOffsetX(originPt, 5);

        agent().debug().debugCreateUnit(
                targetUnitType,
                getPointOffsetX(originPt, 10),
                agent().observation().getPlayerId(),
                1);
        agent().debug().sendDebug();
    }

    @Override
    public void onStep() {
        ObservationInterface obs = agent().observation();

        targetUnit = unitSelf(targetUnitType).orElse(null);

        if (obs.getGameLoop() < orderOnGameLoop) return;

        if (!targetUnitDamaged) {
            agent().debug().debugSetLife(50, targetUnit.unit());
            agent().debug().sendDebug();
            targetUnitDamaged = true;
        }

        if (obs.getGameLoop() < orderOnGameLoop + 5) return;

        if (!abilityCommandSent &&
                targetUnit.unit().getHealth().isPresent() &&
                targetUnit.unit().getHealth().get() != 50) {
            reportError("Pre-ability target unit health is not correct.");
        }

        testUnits = obs.getUnits(Alliance.SELF, UnitInPool.isUnit(testUnitType));
        testUnit = unitSelf(testUnitType).orElse(null);

        issueUnitCommand(agent().actions());

    }

    @Override
    public void onTestFinish() {
        if (doesNotHaveMaxHealth(targetUnit)) {
            reportError("Post-ability target unit health is not correct.");
        }
        killAllUnits();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(250);
    }
}

class TestEffectScan extends TestUnitCommandTargetingPoint {

    private Unit testHatchery;
    private boolean hatcherySpawned = false;
    private boolean verifyPreScan = false;
    private boolean verifyPostScan = false;

    TestEffectScan() {
        testUnitType = Units.TERRAN_ORBITAL_COMMAND;
        testAbility = Abilities.EFFECT_SCAN;
        instantCast = true;
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(300);
    }

    @Override
    void additionalTestSetup() {
        targetPoint = getPointOffsetX(originPt, 50);

        agent().debug().debugShowMap();
        agent().debug().sendDebug();
    }

    @Override
    public void onStep() {
        ObservationInterface obs = agent().observation();
        ActionInterface act = agent().actions();

        if (!hatcherySpawned) {
            agent().debug().debugCreateUnit(Units.ZERG_HATCHERY, targetPoint, obs.getPlayerId() + 1, 1);
            agent().debug().sendDebug();
            hatcherySpawned = true;
        }
        if (obs.getGameLoop() < orderOnGameLoop) return;

        testUnit = unit(testUnitType).orElse(null);
        List<UnitInPool> units = agent().observation().getUnits(UnitInPool.isUnit(Units.ZERG_HATCHERY));
        if (units.size() > 0) {
            testHatchery = units.get(0).unit();
        }
        if (!verifyPreScan) {
            if (obs.getUnits().size() != 1 || !obs.getUnits().get(0).unit().getType().equals(testUnitType)) {
                reportError("Enemy structure is not initially hidden.");
            }
            if (!testUnit.unit().getEnergy().isPresent() ||
                    testUnit.unit().getEnergy().get() < 50.30 ||
                    testUnit.unit().getEnergy().get() > 50.32) {
                reportError("Test pre-ability unit energy is not correct.");
            }
            verifyPreScan = true;
        }

        if (obs.getGameLoop() < orderOnGameLoop + 10) return;

        testUnit = unitSelf(testUnitType).orElse(null);

        issueUnitCommand(act);

        if (obs.getGameLoop() < orderOnGameLoop + 15) return;

        if (!verifyPostScan) {
            if (!testUnit.unit().getEnergy().isPresent() ||
                    testUnit.unit().getEnergy().get() < 0.83 ||
                    testUnit.unit().getEnergy().get() > 0.85) {
                reportError("Test post-ability unit energy is not correct.");
            }
            if (!testHatchery.getDisplayType().equals(DisplayType.VISIBLE)) {
                reportError("Enemy structure is not visible.");
            }
            if (testHatchery.isOnScreen()) {
                reportError("Enemy structure on screen is true.");
            }
            if (!testHatchery.getAlliance().equals(Alliance.ENEMY)) {
                reportError("Enemy alliance is incorrect.");
            }
            if (testHatchery.getOwner() != 2) {
                reportError("Owner of unit is incorrect.");
            }

            verifyPostScan = true;
        }
    }

    @Override
    public void onTestFinish() {
        if (!testHatchery.getDisplayType().equals(DisplayType.SNAPSHOT)) {
            reportError("On finish display type for hatchery is incorrect.");
        }
        if (testHatchery.getOwner() != 2) {
            reportError("Owner of unit is incorrect.");
        }
        killAllUnits();
        agent().debug().debugShowMap();
        agent().debug().sendDebug();
    }
}

class TestEffectStimpack extends TestUnitCommandNoTarget {
    TestEffectStimpack() {
        testUnitType = Units.TERRAN_MARINE;
        testAbility = Abilities.EFFECT_STIM;
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(50);
    }

    @Override
    public void onTestFinish() {
        if (!isSet(testUnit)) {
            reportError("Could not find the test unit.");
        }
        if (isSet(testUnit) && !testUnit.unit().getBuffs().contains(Buffs.STIMPACK)) {
            reportError("Stimpack buff is not present.");
        }
        if (isSet(testUnit) && !doesNotHaveMaxHealth(testUnit)) {
            reportError("Unit health at unexpected value, stimpack did not fire.");
        }
        verifyUnitIdleAfterOrder(testUnitType);
        killAllUnits();
    }
}

class TestHarvestProbeHarvestMinerals extends TestUnitCommandTargetingUnit {
    TestHarvestProbeHarvestMinerals() {
        testUnitType = Units.PROTOSS_PROBE;
        targetUnitType = Units.NEUTRAL_MINERAL_FIELD;
        testAbility = Abilities.HARVEST_GATHER;
    }

    @Override
    void additionalTestSetup() {
        targetPoint = getPointOffsetX(originPt, 5);
        agent().debug().debugCreateUnit(targetUnitType, targetPoint, agent().observation().getPlayerId(), 1);
        agent().debug().debugCreateUnit(
                Units.PROTOSS_NEXUS, getPointOffsetX(originPt, -20), agent().observation().getPlayerId(), 1);
        agent().debug().sendDebug();
    }

    @Override
    public void onStep() {
        super.onStep();
    }

    @Override
    void setTestTime() {
        setWaitGameLoops(150);
    }

    @Override
    public void onTestFinish() {
        if (targetUnit.unit().getMineralContents().orElse(0) < 1400) {
            reportError("Mineral patch does not contain any minerals.");
        }
        if (!isSet(testUnit)) {
            reportError("Could not find the test unit.");
        }
        if (isSet(testUnit) && doesNotHaveOrder(testUnit, Abilities.HARVEST_RETURN)) {
            reportError("Unit does not have correct post-harvest ability in orders.");
        }

        // This issue is specific to debug spawns, so this may not be fixed, and this test is not valid.
        //if (test_nexus_units.front().assigned_harvesters != 1) {
        //    ReportError("Harvester is not registering as assigned");
        //}
        //if (test_nexus_units.front().ideal_harvesters < 1) {
        //    ReportError("Ideal harvesters is not being set.");
        //}

        killAllUnits();
    }
}
