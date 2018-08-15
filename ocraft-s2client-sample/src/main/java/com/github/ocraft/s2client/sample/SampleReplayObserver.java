package com.github.ocraft.s2client.sample;

import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.bot.S2ReplayObserver;
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.bot.setting.GameSettings;
import com.github.ocraft.s2client.protocol.data.UnitType;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SampleReplayObserver {

    private static class TestReplayObserver extends S2ReplayObserver {

        private Map<UnitType, Integer> countUnitsBuild = new HashMap<>();

        @Override
        public void onGameStart() {
            System.out.println("Hello world of Starcraft II replays!");
        }

        @Override
        public void onUnitCreated(UnitInPool unitInPool) {
            countUnitsBuild.compute(unitInPool.unit().getType(), (units, count) -> count == null ? 1 : ++count);
        }

        @Override
        public void onStep() {

        }

        @Override
        public void onGameEnd() {
            System.out.println("Units created:");
            observation().getUnitTypeData(false).forEach((unitType, unitTypeData) -> {
                if (!countUnitsBuild.containsKey(unitType)) return;
                System.out.println(unitType + ": " + countUnitsBuild.get(unitType));
            });
            System.out.println("Finished");
        }
    }

    public static void main(String[] args) throws Exception {
        TestReplayObserver replayObserver = new TestReplayObserver();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .setReplayPath(GameSettings.libraryPath(Paths.get("replays"), Paths.get("/"))
                        .orElseThrow(() -> new AssertionError("replay path is needed")))
                .addReplayObserver(replayObserver)
                .launchStarcraft();

        while (s2Coordinator.update()) {
        }

        s2Coordinator.quit();
    }
}
