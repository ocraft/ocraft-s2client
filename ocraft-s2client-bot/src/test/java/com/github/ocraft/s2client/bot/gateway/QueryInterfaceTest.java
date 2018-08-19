package com.github.ocraft.s2client.bot.gateway;

import com.github.ocraft.s2client.protocol.spatial.Point;
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

        Map<Point, List<UnitInPool>> clusters = QueryInterface.cluster(List.of(unit01, unit02, unit03, unit04), 1.0f);

        Map<Point, List<UnitInPool>> expectedClusters = Map.of(
                Point.of(0.4f, 0.05f, 0.1f), List.of(unit02, unit03),
                Point.of(9.75f, 5.1f, 3.05f), List.of(unit01, unit04));

        assertThat(clusters).containsAllEntriesOf(expectedClusters);

    }
}