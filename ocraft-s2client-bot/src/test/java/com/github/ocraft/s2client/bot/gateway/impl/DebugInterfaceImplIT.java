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

import SC2APIProtocol.Raw;
import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.bot.GameServerResponses;
import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.debug.Color;
import com.github.ocraft.s2client.protocol.debug.DebugTestProcess;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Unit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.bot.Fixtures.OLD_01_UNIT_TAG;
import static com.github.ocraft.s2client.bot.Fixtures.mockUnit;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
class DebugInterfaceImplIT {
    @Test
    void handlesDebugCommands() {
        GameSetup gameSetup = new GameSetup().mockObservation(false).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasAction, GameServerResponses::action);
        gameSetup.server().onRequest(Sc2Api.Request::hasDebug, GameServerResponses::debug);

        Unit unit01 = mockUnit(OLD_01_UNIT_TAG, Raw.Alliance.Self, false, 1.0f);
        Point2d point2d = Point2d.of(1.0f, 1.0f);
        Point point = Point.of(1.0f, 1.0f, 1.0f);

        assertThat(gameSetup.control().debug()
                .debugTextOut("test 01", Color.RED)
                .debugTextOut("test 02", point2d, Color.WHITE, 3)
                .debugTextOut("test 02", point, Color.YELLOW, 6)
                .debugLineOut(point, point, Color.GRAY)
                .debugBoxOut(point, point, Color.BLUE)
                .debugSphereOut(point, 1.0f, Color.TEAL)
                .debugCreateUnit(Units.TERRAN_COMMAND_CENTER, point2d, 2, 3)
                .debugKillUnit(unit01)
                .debugShowMap()
                .debugEnemyControl()
                .debugIgnoreFood()
                .debugIgnoreResourceCost()
                .debugGiveAllResources()
                .debugGodMode()
                .debugIgnoreMineral()
                .debugNoCooldowns()
                .debugGiveAllTech()
                .debugGiveAllUpgrades()
                .debugFastBuild()
                .debugSetScore(5.0f)
                .debugEndGame(true)
                .debugSetEnergy(10, unit01)
                .debugSetLife(20, unit01)
                .debugSetShields(40, unit01)
                .debugMoveCamera(point)
                .debugTestApp(DebugTestProcess.Test.HANG, 100)
                .sendDebug()).as("send debug status").isTrue();
        assertThat(gameSetup.control().debug().sendDebug()).as("send empty debug status").isFalse();

        gameSetup.stop();
    }
}
