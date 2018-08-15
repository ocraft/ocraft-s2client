package com.github.ocraft.s2client.sample;

/*-
 * #%L
 * ocraft-s2client-sample
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
