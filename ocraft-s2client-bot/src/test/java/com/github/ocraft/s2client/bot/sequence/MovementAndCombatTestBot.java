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
import com.github.ocraft.s2client.protocol.Defaults;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovementAndCombatTestBot extends UnitTestBot {

    private MovementAndCombatTestBot() {
        add(new TestMarinesVsMarines());
    }

    @Override
    protected void onTestsBegin() {
        debug().debugShowMap();
        debug().debugEnemyControl();
    }

    @Override
    protected void onTestsEnd() {

    }

    public static boolean test() {
        MovementAndCombatTestBot bot = new MovementAndCombatTestBot();
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

class TestMarinesVsMarines extends TestSequence {

    private Logger log = LoggerFactory.getLogger(TestMarinesVsMarines.class);

    private long attackOnGameLoop;
    private Point2d battlePt;

    @Override
    public void onTestStart() {
        setWaitGameLoops(1000);
        attackOnGameLoop = agent().observation().getGameLoop() + 10;

        Point2d friendlyRallyPt = agent().observation().getGameInfo().findRandomLocation();
        Point2d enemyRallyPt = agent().observation().getGameInfo().findRandomLocation();

        battlePt = friendlyRallyPt.add(enemyRallyPt).div(2.0f);

        agent().debug().debugCreateUnit(Units.TERRAN_MARINE, friendlyRallyPt, agent().observation().getPlayerId(), 20);
        agent().debug().debugCreateUnit(Units.TERRAN_MARINE, enemyRallyPt, agent().observation().getPlayerId() + 1, 20);
        agent().debug().sendDebug();
    }

    @Override
    public void onStep() {
        if (agent().observation().getGameLoop() != attackOnGameLoop) return;
        agent().observation().getUnits().forEach(unitInPool ->
                unitInPool.getUnit().ifPresent(
                        unit -> agent().actions().unitCommand(unit, Abilities.SMART, battlePt, false)));
    }

    @Override
    public void onTestFinish() {
        log.info("Finished on game loop: {}", agent().observation().getGameLoop());
        killAllUnits();
    }
}
