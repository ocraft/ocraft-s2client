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
import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FastRestartSinglePlayerTestBot extends S2Agent {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final int NUM_RESTARTS_TO_TEST = 5;
    private int countRestarts = 0;
    private long triggerOnGameLoop;
    private long finishByGameLoop;
    private Point2d battlePt;
    private boolean success = true;
    private int countFullStart = 0;

    private FastRestartSinglePlayerTestBot() {
    }

    private boolean isFinished() {
        return countRestarts >= NUM_RESTARTS_TO_TEST;
    }

    @Override
    public void onGameFullStart() {
        ++countFullStart;
    }

    @Override
    public void onGameStart() {
        debug().debugShowMap();
        debug().debugEnemyControl();
        debug().sendDebug();

        triggerOnGameLoop = observation().getGameLoop() + 10;
        finishByGameLoop = observation().getGameLoop() + 500;

        Point2d friendlyRallyPt = observation().getGameInfo().findRandomLocation();
        Point2d enemyRallyPt = observation().getGameInfo().findRandomLocation();

        battlePt = friendlyRallyPt.add(enemyRallyPt).div(2.0f);

        debug().debugCreateUnit(Units.TERRAN_MARINE, friendlyRallyPt, observation().getPlayerId(), 20);
        debug().debugCreateUnit(Units.TERRAN_MARINE, enemyRallyPt, observation().getPlayerId() + 1, 20);
        debug().sendDebug();
    }

    @Override
    public void onStep() {
        if (observation().getGameLoop() >= finishByGameLoop) {
            debug().debugEndGame(true);
            debug().sendDebug();
            return;
        }

        if (observation().getGameLoop() != triggerOnGameLoop) return;

        observation().getUnits().forEach(unitInPool -> unitInPool.getUnit().ifPresent(unit -> {
            actions().unitCommand(unit, Abilities.SMART, battlePt, false);
        }));
    }

    @Override
    public void onGameEnd() {
        ++countRestarts;
        log.info("Restart test: {} of {} complete", countRestarts, NUM_RESTARTS_TO_TEST);
        if (!isFinished()) agentControl().restart();
    }

    private boolean isSuccess() {
        return success && countFullStart == 1;
    }

    public static boolean test() {
        FastRestartSinglePlayerTestBot bot = new FastRestartSinglePlayerTestBot();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(new String[]{})
                .setParticipants(S2Coordinator.createParticipant(Race.TERRAN, bot))
                .launchStarcraft()
                .startGame(LocalMap.of(Fixtures.MAP_EMPTY));

        while (!bot.isFinished()) {
            s2Coordinator.update();
        }
        s2Coordinator.quit();
        return bot.isSuccess();
    }
}
