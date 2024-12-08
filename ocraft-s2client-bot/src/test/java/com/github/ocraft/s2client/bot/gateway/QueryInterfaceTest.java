package com.github.ocraft.s2client.bot.gateway;

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

import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.github.ocraft.s2client.bot.Fixtures.mockUnit;
import static org.assertj.core.api.Assertions.assertThat;

class QueryInterfaceTest {

    @Test
    void clustersUnits() {

        UnitInPool unit01 = new UnitInPool(Tag.of(1L)).update(mockUnit(Point.of(10.0f, 5.0f, 3.0f)), 0, true);
        UnitInPool unit02 = new UnitInPool(Tag.of(2L)).update(mockUnit(Point.of(0.3f, 0.0f, 0.2f)), 0, true);
        UnitInPool unit03 = new UnitInPool(Tag.of(3L)).update(mockUnit(Point.of(0.5f, 0.1f, 0.0f)), 0, true);
        UnitInPool unit04 = new UnitInPool(Tag.of(4L)).update(mockUnit(Point.of(9.5f, 5.2f, 3.1f)), 0, true);

        Map<Point2d, List<UnitInPool>> clusters = QueryInterface.cluster(List.of(unit01, unit02, unit03, unit04), 1.0f);

        Map<Point2d, List<UnitInPool>> expectedClusters = Map.of(
                Point2d.of(0.4f, 0.05f), List.of(unit02, unit03),
                Point2d.of(9.75f, 5.1f), List.of(unit01, unit04)
        );

        assertThat(clusters).containsAllEntriesOf(expectedClusters);
    }
}
