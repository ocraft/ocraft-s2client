package com.github.ocraft.s2client.bot;

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

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class Fixtures {

    public static final Path BEL_SHIR_VESTIGE_LE = Paths.get("Ladder", "(2)Bel'ShirVestigeLE (Void).SC2Map");
    public static final Path MAP_EMPTY = Paths.get("Test", "Empty.SC2Map");

    public static final Tag DEAD_UNIT_TAG = Tag.of(1L);
    public static final Tag NEW_UNIT_TAG = Tag.of(2L);
    public static final Tag ENEMY_UNIT_TAG = Tag.of(3L);
    public static final Tag OLD_01_UNIT_TAG = Tag.of(4L);
    public static final Tag OLD_02_UNIT_TAG = Tag.of(5L);

    private Fixtures() {
        throw new AssertionError("private constructor");
    }

    public static Unit mockUnit(
            com.github.ocraft.s2client.protocol.unit.Tag tag,
            Raw.Alliance alliance,
            boolean withOrders,
            float buildProgress) {
        return Unit.from(GameServerResponses.sc2ApiUnit(tag.getValue(), alliance, withOrders, buildProgress));
    }

    public static Unit mockUnit(Point position) {
        Unit unit = mock(Unit.class);
        when(unit.getPosition()).thenReturn(position);
        return unit;
    }
}
