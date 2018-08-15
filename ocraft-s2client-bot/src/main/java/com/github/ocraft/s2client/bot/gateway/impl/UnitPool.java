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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

// TODO p.picheta ensure thread safety?
class UnitPool {

    private final Map<Tag, UnitInPool> pool = new HashMap<>();
    private final Map<Tag, UnitInPool> existingPool = new HashMap<>();
    private final Map<Tag, Unit> previous = new HashMap<>();

    UnitInPool createUnit(Tag tag) {
        Optional<UnitInPool> existing = getUnit(tag);
        if (existing.isPresent()) {
            UnitInPool unitInPool = existing.get();
            existingPool.put(tag, unitInPool);
            return unitInPool;
        }

        UnitInPool newUnitInPool = new UnitInPool(tag);
        pool.put(tag, newUnitInPool);
        existingPool.put(tag, newUnitInPool);
        return newUnitInPool;
    }

    Optional<UnitInPool> getUnit(Tag tag) {
        return Optional.ofNullable(pool.get(tag));
    }

    Optional<UnitInPool> getExistingUnit(Tag tag) {
        return Optional.ofNullable(existingPool.get(tag));
    }

    void markDead(Tag tag) {
        getUnit(tag).ifPresent(unitInPool -> {
            unitInPool.dead();
            existingPool.remove(tag);
        });
    }

    void forEachExistingUnit(Consumer<UnitInPool> unitConsumer) {
        existingPool.forEach((tag, unitInPool) -> unitConsumer.accept(unitInPool));
    }

    void clearExisting() {
        existingPool.clear();
    }

    boolean unitExists(Tag tag) {
        return getExistingUnit(tag).isPresent();
    }

    void switchExistingToPrevious() {
        previous.clear();
        forEachExistingUnit(unitInPool -> {
            if (unitInPool.getUnit().isPresent()) previous.put(unitInPool.getTag(), unitInPool.getUnit().get());
        });
        clearExisting();
    }

    Map<Tag, Unit> previous() {
        return previous;
    }
}
