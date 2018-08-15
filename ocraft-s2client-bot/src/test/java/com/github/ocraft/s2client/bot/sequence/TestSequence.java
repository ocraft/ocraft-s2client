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

import com.github.ocraft.s2client.bot.ClientEvents;
import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class TestSequence implements ClientEvents {

    private S2Agent agent;
    private long waitGameLoops = 5;
    private List<String> errors = new ArrayList<>();
    private String testName = getClass().getName();

    public boolean didSucceed() {
        return errors.isEmpty();
    }

    public abstract void onTestStart();

    public abstract void onTestFinish();

    public void reportError(String error) {
        errors.add(error);
    }

    public void killAllUnits() {
        agent.observation().getUnits().forEach(unitInPool -> {
            Optional<Unit> unit = unitInPool.getUnit();
            if (unit.isPresent()) {
                agent.debug().debugKillUnit(unit.get());
                unit.get().getPassengers().forEach(passengerUnit ->
                        agent.observation()
                                .getUnit(passengerUnit.getTag())
                                .getUnit()
                                .ifPresent(passenger -> agent.debug().debugKillUnit(passenger)));
            }
        });
        agent.debug().sendDebug();
    }

    public void setAgent(S2Agent testAgent) {
        this.agent = testAgent;
    }

    public String getTestName() {
        return testName;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setWaitGameLoops(long waitGameLoops) {
        this.waitGameLoops = waitGameLoops;
    }

    public long getWaitGameLoops() {
        return waitGameLoops;
    }

    public S2Agent agent() {
        return agent;
    }
}
