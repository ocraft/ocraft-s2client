package com.github.ocraft.s2client.bot.gateway.impl;

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

import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Errors.required;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UnitPoolTest {

    private final Tag TAG = Tag.of(1L);

    @Test
    void addsUnitToPools() {
        UnitPool unitPool = new UnitPool();

        UnitInPool unitInPool = unitPool.createUnit(TAG);

        assertThatUnitInPoolIsAddedCorrectly(unitPool, unitInPool);
    }

    private void assertThatUnitInPoolIsAddedCorrectly(UnitPool unitPool, UnitInPool unitInPool) {
        Optional<UnitInPool> poolUnit = unitPool.getUnit(TAG);
        Optional<UnitInPool> poolExistingUnit = unitPool.getExistingUnit(TAG);

        assertThat(unitInPool.getTag()).as("tag of unit added to pool").isEqualTo(TAG);
        assertThat(poolUnit).as("unit found in pool").isPresent();
        assertThat(poolExistingUnit).as("unit found in existing pool").isPresent();
        assertThat(poolUnit).containsSame(poolExistingUnit.orElseThrow(required("unit in existing pool")));
    }

    @Test
    void addsUnitToExistingPoolIfIsPresentInPool() {
        UnitPool unitPool = new UnitPool();

        unitPool.createUnit(TAG);
        UnitInPool unitInPool = unitPool.createUnit(TAG);

        assertThatUnitInPoolIsAddedCorrectly(unitPool, unitInPool);
    }

    @Test
    void marksUnitAsDead() {
        UnitPool unitPool = new UnitPool();
        UnitInPool unitInPool = unitPool.createUnit(TAG);

        unitPool.markDead(TAG);

        assertThat(unitPool.unitExists(TAG)).as("unit is in exiting pool").isFalse();
        assertThat(unitInPool.isAlive()).as("unit is marked as alive").isFalse();
    }

    @Test
    void movesExistingUnitToPreviousPool() {
        UnitPool unitPool = new UnitPool();
        unitPool.createUnit(TAG).update(mock(Unit.class), 1, true);

        unitPool.switchExistingToPrevious();

        assertThat(unitPool.unitExists(TAG)).as("unit is in exiting pool").isFalse();
        assertThat(unitPool.previous()).as("unit is moved to previous pool").containsKey(TAG);
    }

    @Test
    void updatesUnitData() {
        UnitPool unitPool = new UnitPool();
        UnitInPool unitInPool = unitPool.createUnit(TAG);
        Unit unit = mock(Unit.class);
        int gameLoop = 2;

        unitInPool.update(unit, gameLoop, true);

        assertThat(unitInPool.getUnit()).as("unit data").hasValue(unit);
        assertThat(unitInPool.getLastSeenGameLoop()).as("last seen game loop").isEqualTo(gameLoop);
        assertThat(unitInPool.isAlive()).as("unit is marked as alive").isTrue();

    }

}
