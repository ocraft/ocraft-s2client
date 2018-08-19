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
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Unit;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.bot.Fixtures.OLD_01_UNIT_TAG;
import static com.github.ocraft.s2client.bot.Fixtures.mockUnit;
import static org.assertj.core.api.Assertions.assertThat;

@org.junit.jupiter.api.Tag("integration")
class QueryInterfaceImplIT {
    @Test
    void handlesQueries() {
        GameSetup gameSetup = new GameSetup().mockObservation(false).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasData, GameServerResponses::abilityData);
        gameSetup.server().onRequest(Sc2Api.Request::hasQuery, GameServerResponses::query);
        gameSetup.control().useGeneralizedAbility(true);

        Unit unit01 = mockUnit(OLD_01_UNIT_TAG, Raw.Alliance.Self, false, 1.0f);
        Point2d point2d = Point2d.of(1.0f, 1.0f);

        assertThat(gameSetup.control().query().pathingDistance(unit01, point2d)).isGreaterThan(0.0f);
        assertThat(gameSetup.control().query().pathingDistance(point2d, point2d)).isGreaterThan(0.0f);
        assertThat(gameSetup.control().query().placement(Abilities.EFFECT_PSI_STORM, point2d, unit01)).isTrue();
        assertThat(gameSetup.control().query().placement(Abilities.EFFECT_PSI_STORM, point2d, unit01)).isTrue();
        assertThat(gameSetup.control().query().getAbilitiesForUnit(unit01, true)).isNotNull();

        gameSetup.stop();
    }
}
