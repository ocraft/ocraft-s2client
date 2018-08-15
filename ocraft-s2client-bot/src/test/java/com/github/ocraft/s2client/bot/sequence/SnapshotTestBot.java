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
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.Defaults;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.response.ResponseGameInfo;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.unit.Alliance;
import com.github.ocraft.s2client.protocol.unit.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class SnapshotTestBot extends UnitTestBot {

    Point2d basePos;
    Point2d mineralPos;
    Point2d scvPos;
    Unit mineral;

    private SnapshotTestBot() {
        add(new TestSnapshot1(this));    // Spawn TERRAN_SCV, mineral field and command center.
        add(new TestSnapshot2(this));    // Get the mineral field tag and toggle off vision.
        add(new TestSnapshot3(this));    // Destroy the mineral field and select the TERRAN_SCV.
        add(new TestSnapshot4(this));    // Target something other than the mineral field. TERRAN_SCV should not move.
        add(new TestSnapshot5(this));    // Target the mineral field. TERRAN_SCV should move.
        add(new TestSnapshot6(this));
    }

    boolean isNearPos(Point2d a, Point2d b) {
        return a.distance(b) < 0.5;
    }

    @Override
    protected void onTestsBegin() {

    }

    @Override
    protected void onTestsEnd() {

    }

    public static boolean test() {
        SnapshotTestBot bot = new SnapshotTestBot();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(new String[]{})
                .setFeatureLayers(Defaults.defaultSpatialSetup())
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

abstract class TestSnapshot extends TestSequence {

    protected SnapshotTestBot bot;

    TestSnapshot(SnapshotTestBot bot) {
        this.bot = bot;
    }

    Unit findSCV() {
        return agent().observation()
                .getUnits(Alliance.SELF, UnitInPool.isUnit(Units.TERRAN_SCV))
                .stream()
                .findFirst()
                .map(UnitInPool::getUnit)
                .map(Optional::get)
                .orElseThrow(() -> {
                    reportError("Can't find a friendly TERRAN_SCV!");
                    return new AssertionError("Can't find a friendly TERRAN_SCV!");
                });
    }
}

class TestSnapshot1 extends TestSnapshot {

    TestSnapshot1(SnapshotTestBot bot) {
        super(bot);
    }

    @Override
    public void onTestStart() {
        setWaitGameLoops(10);
        ResponseGameInfo gameInfo = agent().observation().getGameInfo();

        gameInfo.getStartRaw().ifPresent(startRaw -> {
            Point2d playableMin = startRaw.getPlayableArea().getP0().toPoint2d();
            Point2d playableMax = startRaw.getPlayableArea().getP1().toPoint2d();
            Point2d center = playableMax.sub(playableMin).mul(0.5f).add(playableMin);

            int playerId = agent().observation().getPlayerId();

            // Build a command center and an TERRAN_SCV nearby.
            Point2d base = center;
            //base.x_ += (playable_max.x_ - center.x_) * 0.5f;
            agent().debug().debugCreateUnit(Units.TERRAN_COMMAND_CENTER, base, playerId, 1);
            Point2d scvPt = base.add(Point2d.of(0.0f, 4.0f));
            agent().debug().debugCreateUnit(Units.TERRAN_SCV, scvPt, playerId, 1);

            // Build a mineral patch.
            Point2d mineralPt = base.sub(12.0f, -12.0f);
            agent().debug().debugCreateUnit(Units.NEUTRAL_MINERAL_FIELD, mineralPt, 0, 1);

            // Create the units.
            agent().debug().debugShowMap();
            agent().debug().sendDebug();

            // Center the camera.
            Point2d midpoint = mineralPt.add(base).mul(0.5f);
            PointI minimapPos = gameInfo.convertWorldToMinimap(midpoint);
            agent().actionsFeatureLayer().cameraMove(minimapPos);

            bot.basePos = base;
            bot.mineralPos = mineralPt;
        });
    }

    @Override
    public void onTestFinish() {

    }
}

class TestSnapshot2 extends TestSnapshot {

    TestSnapshot2(SnapshotTestBot bot) {
        super(bot);
    }

    @Override
    public void onTestStart() {
        setWaitGameLoops(10);
        bot.mineral = agent().observation()
                .getUnits(UnitInPool.isUnit(Units.NEUTRAL_MINERAL_FIELD))
                .stream()
                .findFirst()
                .map(UnitInPool::getUnit)
                .map(Optional::get)
                .orElseThrow(() -> {
                    reportError("Can't find the mineral patch!");
                    return new AssertionError("Can't find the mineral patch!");
                });
        agent().debug().debugShowMap();
        agent().debug().sendDebug();
    }

    @Override
    public void onTestFinish() {

    }
}

class TestSnapshot3 extends TestSnapshot {

    private Logger log = LoggerFactory.getLogger(TestSnapshot3.class);

    TestSnapshot3(SnapshotTestBot bot) {
        super(bot);
    }

    @Override
    public void onTestStart() {
        setWaitGameLoops(10);
        agent().debug().debugKillUnit(bot.mineral);
        agent().debug().sendDebug();

        Unit unitScv = findSCV();
        bot.scvPos = unitScv.getPosition().toPoint2d();
        PointI scvPt = agent()
                .observation()
                .getGameInfo()
                .convertWorldToCamera(agent().observation().getCameraPos().toPoint2d(), bot.scvPos);

        agent().actionsFeatureLayer().select(scvPt, ActionSpatialUnitSelectionPoint.Type.SELECT);
        log.info("Selecting an scv at world pt: {}", unitScv);
        log.info("Selecting an scv at screen pt: {}", scvPt);
    }

    @Override
    public void onTestFinish() {

    }
}

class TestSnapshot4 extends TestSnapshot {

    TestSnapshot4(SnapshotTestBot bot) {
        super(bot);
    }

    @Override
    public void onTestStart() {
        setWaitGameLoops(20);

        PointI emptyPosScreen = agent()
                .observation()
                .getGameInfo()
                .convertWorldToCamera(
                        agent().observation().getCameraPos().toPoint2d(),
                        bot.mineralPos.add(Point2d.of(0.0f, 5.0f)));

        agent().actionsFeatureLayer().unitCommand(Abilities.HARVEST_GATHER, emptyPosScreen, false);
    }

    @Override
    public void onTestFinish() {
        Unit unitScv = findSCV();
        if (!bot.isNearPos(bot.scvPos, unitScv.getPosition().toPoint2d())) {
            reportError("The TERRAN_SCV should not have moved!");
        }
    }
}

class TestSnapshot5 extends TestSnapshot {

    TestSnapshot5(SnapshotTestBot bot) {
        super(bot);
    }

    @Override
    public void onTestStart() {
        setWaitGameLoops(20);

        PointI mineralPos = agent()
                .observation()
                .getGameInfo()
                .convertWorldToCamera(agent().observation().getCameraPos().toPoint2d(), bot.mineralPos);

        agent().actionsFeatureLayer().unitCommand(Abilities.HARVEST_GATHER, mineralPos, false);
    }

    @Override
    public void onTestFinish() {
        Unit unitScv = findSCV();
        if (bot.isNearPos(bot.scvPos, unitScv.getPosition().toPoint2d())) {
            reportError("The TERRAN_SCV should have moved!");
        }
    }
}

class TestSnapshot6 extends TestSnapshot {

    TestSnapshot6(SnapshotTestBot bot) {
        super(bot);
    }

    @Override
    public void onTestStart() {
        setWaitGameLoops(20);
    }

    @Override
    public void onTestFinish() {
    }
}
