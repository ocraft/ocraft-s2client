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
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.unit.Alliance;
import com.github.ocraft.s2client.protocol.unit.Unit;
import com.github.ocraft.s2client.test.Threads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemapAbilitiesTestBot extends UnitTestBot {

    private RemapAbilitiesTestBot() {
        add(new TestRemapStart());
        add(new TestRemapSelectSCV());
        add(new TestRemapBuildCommandCenter());
        add(new TestRemapBuildCommandCenterHalt());
        add(new TestRemapBuildCommandCenterDetectHalt());
        add(new TestRemapBuildCommandCenterContinue());
        add(new WaitT(5));
        add(new TestRemapBuildCommandCenterHalt());
        add(new TestRemapBuildCommandCenterDetectHalt());
        add(new TestRemapBuildCommandCenterCancel());
        add(new TestRemapFinish());
    }

    @Override
    protected void onTestsBegin() {
        debug().debugShowMap();
    }

    @Override
    protected void onTestsEnd() {

    }

    public static boolean test() {
        RemapAbilitiesTestBot bot = new RemapAbilitiesTestBot();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(new String[]{})
                .setFeatureLayers(Defaults.defaultSpatialSetup())
                .setParticipants(S2Coordinator.createParticipant(Race.TERRAN, bot))
                .launchStarcraft()
                .startGame(LocalMap.of(Fixtures.MAP_EMPTY));

        while (!bot.isFinished()) {
            Threads.delay(20);
            s2Coordinator.update();
        }
        s2Coordinator.quit();
        return bot.success();
    }
}

abstract class TestRemap extends TestSequence {

    private Logger log = LoggerFactory.getLogger(TestRemap.class);

    private PointI commandCenterPt = PointI.of(24, 24);

    Unit getCommandCenter() {
        return agent().observation().getUnits(Alliance.SELF, UnitInPool.isUnit(Units.TERRAN_COMMAND_CENTER))
                .stream()
                .findFirst()
                .map(unitInPool -> unitInPool.getUnit().get()).orElseGet(() -> {
                    log.error("Could not find the command center");
                    return null;
                });
    }

    PointI getCommandCenterPt() {
        return commandCenterPt;
    }
}

class TestRemapStart extends TestRemap {

    @Override
    public void onTestStart() {
        ResponseGameInfo gameInfo = agent().observation().getGameInfo();

        gameInfo.getStartRaw().ifPresent(startRaw -> {
            PointI playableMin = startRaw.getPlayableArea().getP0();
            PointI playableMax = startRaw.getPlayableArea().getP1();
            PointI spawnPt = playableMin.add(playableMax.sub(playableMin)).div(2);

            agent().debug().debugCreateUnit(Units.TERRAN_SCV, spawnPt, agent().observation().getPlayerId(), 1);
            agent().debug().debugMoveCamera(spawnPt);
            agent().debug().debugGiveAllResources();
            agent().debug().debugIgnoreMineral();
            agent().debug().sendDebug();
        });
    }

    @Override
    public void onTestFinish() {

    }
}

class TestRemapSelectSCV extends TestRemap {

    @Override
    public void onTestStart() {
        agent().actionsFeatureLayer().select(PointI.of(0, 0), PointI.of(63, 63), false);
    }

    @Override
    public void onTestFinish() {

    }
}

class TestRemapBuildCommandCenter extends TestRemap {

    @Override
    public void onTestStart() {
        agent().actionsFeatureLayer().unitCommand(Abilities.BUILD_COMMAND_CENTER, getCommandCenterPt(), false);
        setWaitGameLoops(50);
    }

    @Override
    public void onTestFinish() {

    }
}

class TestRemapBuildCommandCenterHalt extends TestRemap {

    @Override
    public void onTestStart() {
        agent().actionsFeatureLayer().unitCommand(Abilities.HALT);
        setWaitGameLoops(10);
    }

    @Override
    public void onTestFinish() {

    }
}

class TestRemapBuildCommandCenterDetectHalt extends TestRemap {

    private float currentProgress;

    @Override
    public void onTestStart() {
        Unit commandCenter = getCommandCenter();
        currentProgress = commandCenter.getBuildProgress();
        if (currentProgress == 0.0f) {
            reportError("Command center did not start building");
            return;
        }
        if (currentProgress == 1.0f) {
            reportError("Command center finished building");
            return;
        }
        setWaitGameLoops(50);
    }

    @Override
    public void onTestFinish() {
        Unit commandCenter = getCommandCenter();
        if (currentProgress != commandCenter.getBuildProgress()) {
            reportError("Command center building continued");
        }
    }
}

class TestRemapBuildCommandCenterContinue extends TestRemap {

    private float currentProgress;

    @Override
    public void onTestStart() {
        Unit commandCenter = getCommandCenter();
        currentProgress = commandCenter.getBuildProgress();
        agent().actionsFeatureLayer().unitCommand(Abilities.SMART, getCommandCenterPt(), false);
        setWaitGameLoops(50);
    }

    @Override
    public void onTestFinish() {
        Unit commandCenter = getCommandCenter();
        if (currentProgress == commandCenter.getBuildProgress()) {
            reportError("Command center building would not continue");
            return;
        }
        agent().actionsFeatureLayer().select(getCommandCenterPt(), ActionSpatialUnitSelectionPoint.Type.SELECT);
    }
}

class TestRemapBuildCommandCenterCancel extends TestRemap {

    @Override
    public void onTestStart() {
        agent().actionsFeatureLayer().unitCommand(Abilities.CANCEL);
        setWaitGameLoops(10);
    }

    @Override
    public void onTestFinish() {
        agent().observation().getUnits(Alliance.SELF, UnitInPool.isUnit(Units.TERRAN_COMMAND_CENTER))
                .stream()
                .findFirst().ifPresent(unitInPool -> reportError("Command center was not destroyed!"));
    }
}

class TestRemapFinish extends TestRemap {

    @Override
    public void onTestStart() {
        setWaitGameLoops(5);
    }

    @Override
    public void onTestFinish() {

    }
}
