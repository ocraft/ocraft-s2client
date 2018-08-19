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
import com.github.ocraft.s2client.bot.gateway.ActionInterface;
import com.github.ocraft.s2client.protocol.action.ActionChat;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Unit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.ocraft.s2client.bot.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
class ActionInterfaceImplIT {
    @Test
    void handlesAction() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasAction, GameServerResponses::action);
        Unit unit01 = mockUnit(OLD_01_UNIT_TAG, Raw.Alliance.Self, false, 1.0f);
        Unit unit02 = mockUnit(OLD_02_UNIT_TAG, Raw.Alliance.Self, false, 1.0f);
        Unit enemyUnit = mockUnit(ENEMY_UNIT_TAG, Raw.Alliance.Enemy, false, 1.0f);

        ActionInterface actionInterface = gameSetup.control().agentControl().action();
        actionInterface
                .unitCommand(unit01, Abilities.STOP, true)
                .unitCommand(unit01, Abilities.ATTACK, Point2d.of(1.0f, 1.0f), false)
                .unitCommand(unit01, Abilities.ATTACK, enemyUnit, false)
                .unitCommand(List.of(unit01, unit02), Abilities.STOP, true)
                .unitCommand(List.of(unit01, unit02), Abilities.ATTACK, Point2d.of(1.0f, 1.0f), false)
                .unitCommand(List.of(unit01, unit02), Abilities.ATTACK, enemyUnit, false)
                .toggleAutocast(OLD_01_UNIT_TAG, Abilities.EFFECT_PSI_STORM)
                .toggleAutocast(List.of(OLD_01_UNIT_TAG, OLD_02_UNIT_TAG), Abilities.EFFECT_CHRONO_BOOST)
                .sendChat("gl", ActionChat.Channel.TEAM);

        assertThat(actionInterface.sendActions()).as("sending action status").isTrue();
        assertThat(actionInterface.commands()).containsExactly(
                OLD_01_UNIT_TAG,
                OLD_01_UNIT_TAG,
                OLD_01_UNIT_TAG,
                OLD_01_UNIT_TAG,
                OLD_02_UNIT_TAG,
                OLD_01_UNIT_TAG,
                OLD_02_UNIT_TAG,
                OLD_01_UNIT_TAG,
                OLD_02_UNIT_TAG);
        assertThat(actionInterface.sendActions()).as("sending empty action status").isFalse();
        assertThat(actionInterface.commands()).isEmpty();

        gameSetup.stop();
    }
}
