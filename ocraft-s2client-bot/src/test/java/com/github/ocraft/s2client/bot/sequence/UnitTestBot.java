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

import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.data.Upgrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class UnitTestBot extends S2Agent {

    private Logger log = LoggerFactory.getLogger(UnitTestBot.class);

    private boolean success = true;
    private List<TestSequence> sequences = new ArrayList<>();
    private int currentSequence = -1;
    private long gameLoopDone = 2;

    public boolean isFinished() {
        return currentSequence >= sequences.size();
    }

    private boolean hasSequence() {
        return currentSequence != -1 && currentSequence < sequences.size();
    }

    public boolean success() {
        return success;
    }

    protected <T extends TestSequence> void add(T test) {
        test.setAgent(this);
        sequences.add(test);
    }

    protected abstract void onTestsBegin();

    protected abstract void onTestsEnd();

    protected void onPostStep() {
    }

    @Override
    public void onError(List<ClientError> clientErrors, List<String> protocolErrors) {
        success = false;
    }

    @Override
    public void onStep() {
        long gameLoop = observation().getGameLoop();
        // Move to the next sequence if this sequence is done.
        if (gameLoop >= gameLoopDone) {
            if (currentSequence == -1) {
                currentSequence = 0;
                onTestsBegin();
            } else {
                TestSequence testSequence = currentSequence();
                testSequence.onTestFinish();
                if (!testSequence.didSucceed()) {
                    success = false;
                    log.error("Test: {} failed!", testSequence.getTestName());
                    testSequence.getErrors().forEach(error -> log.error("\t Error: {}", error));
                }
                ++currentSequence;
            }
            if (isFinished()) {
                onTestsEnd();
                return;
            }
            currentSequence().onTestStart();
            gameLoopDone = gameLoop + currentSequence().getWaitGameLoops();
            return;
        }
        if (currentSequence == -1) {
            return;
        }
        currentSequence().onStep();
        onPostStep();
    }

    private TestSequence currentSequence() {
        return sequences.get(currentSequence);
    }

    @Override
    public void onGameStart() {
        if (hasSequence()) {
            currentSequence().onGameStart();
        }
    }

    @Override
    public void onGameEnd() {
        if (hasSequence()) {
            currentSequence().onGameEnd();
        }
        if (!isFinished()) {
            success = false;
            currentSequence = sequences.size() + 1;
            log.error("Game ended before tests finished.");
        }
    }

    @Override
    public void onGameFullStart() {
        if (hasSequence()) {
            currentSequence().onGameFullStart();
        }
    }

    @Override
    public void onUnitDestroyed(UnitInPool unit) {
        if (hasSequence()) {
            currentSequence().onUnitDestroyed(unit);
        }
    }

    @Override
    public void onUnitCreated(UnitInPool unit) {
        if (hasSequence()) {
            currentSequence().onUnitCreated(unit);
        }
    }

    @Override
    public void onUnitIdle(UnitInPool unit) {
        if (hasSequence()) {
            currentSequence().onUnitIdle(unit);
        }
    }

    @Override
    public void onUnitEnterVision(UnitInPool unit) {
        if (hasSequence()) {
            currentSequence().onUnitEnterVision(unit);
        }
    }

    @Override
    public void onUpgradeCompleted(Upgrade upgrade) {
        if (hasSequence()) {
            currentSequence().onUpgradeCompleted(upgrade);
        }
    }

    @Override
    public void onBuildingConstructionComplete(UnitInPool unit) {
        if (hasSequence()) {
            currentSequence().onBuildingConstructionComplete(unit);
        }
    }

    @Override
    public void onNydusDetected() {
        if (hasSequence()) {
            currentSequence().onNydusDetected();
        }
    }

    @Override
    public void onNuclearLaunchDetected() {
        if (hasSequence()) {
            currentSequence().onNuclearLaunchDetected();
        }
    }
}
