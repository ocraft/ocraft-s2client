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

import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.bot.S2Coordinator;
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.game.Race;
import com.github.ocraft.s2client.protocol.response.ResponseGameInfo;
import com.github.ocraft.s2client.protocol.spatial.Size2dI;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;
import com.github.ocraft.s2client.protocol.unit.Alliance;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

// Bot loads and unloads SCVs from the command centre
public class SampleFeatureLayerBot {

    private static class TestBot extends S2Agent {

        private ResponseGameInfo gameInfo;

        @Override
        public void onGameStart() {
        }

        @Override
        public void onStep() {
            long gameLoop = observation().getGameLoop();

            if (gameLoop % 100 == 0) {
                getRandomUnit(Units.TERRAN_COMMAND_CENTER).ifPresent(commandCenter -> {
                    actions().unitCommand(commandCenter.unit(), Abilities.LOAD_ALL_COMMAND_CENTER, false);
                });
            } else if (gameLoop % 20 == 0) {
                getRandomUnit(Units.TERRAN_COMMAND_CENTER).ifPresent(commandCenter -> {
                    int cargoUnitsSize = commandCenter.unit().getPassengers().size();
                    if (cargoUnitsSize > 0) {
                        this.actions().select(commandCenter.getTag());
                        this.actionsFeatureLayer().unloadCargo(ThreadLocalRandom.current().nextInt(cargoUnitsSize));
                    }
                });
            }
        }

        private Optional<UnitInPool> getRandomUnit(UnitType unitType) {
            List<UnitInPool> units = observation().getUnits(Alliance.SELF, UnitInPool.isUnit(unitType));
            return units.isEmpty()
                    ? Optional.empty()
                    : Optional.of(units.get(ThreadLocalRandom.current().nextInt(units.size())));
        }
    }

    public static void main(String[] args) {
        TestBot bot = new TestBot();
        S2Coordinator s2Coordinator = S2Coordinator.setup()
                .loadSettings(args)
                .setFeatureLayers(
                        SpatialCameraSetup.spatialSetup()
                                .width(10f)
                                .resolution(Size2dI.of(128, 128))
                                .minimap(Size2dI.of(16, 16))
                                .allowCheatingLayers(false)
                                .cropToPlayableArea(true)
                                .build())
                // Important, or else the UI action won't work.
                .setRawAffectsSelection(true)
                .setParticipants(
                        S2Coordinator.createParticipant(Race.TERRAN, bot),
                        S2Coordinator.createComputer(Race.TERRAN, Difficulty.VERY_EASY))
                .launchStarcraft()
                .startGame(BattlenetMap.of("Lava Flow"));

        while (s2Coordinator.update()) {
        }

        s2Coordinator.quit();
    }

}
