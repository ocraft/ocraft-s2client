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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.bot.GameServerResponses;
import com.github.ocraft.s2client.bot.gateway.ActionFeatureLayerInterface;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
class ActionFeatureLayerInterfaceImplIT {
    @Test
    void handlesActionFeatureLayer() {
        GameSetup gameSetup = new GameSetup().start();
        gameSetup.server().onRequest(Sc2Api.Request::hasAction, GameServerResponses::action);

        ActionFeatureLayerInterface featureLayerInterface = gameSetup.control().agentControl().actionsFeatureLayer();
        featureLayerInterface
                .cameraMove(PointI.of(1, 2))
                .select(PointI.of(1, 1), ActionSpatialUnitSelectionPoint.Type.ALL_TYPE)
                .select(PointI.of(1, 1), PointI.of(2, 2), true)
                .unitCommand(Abilities.STOP)
                .unitCommand(Abilities.ATTACK, PointI.of(1, 1), true);

        assertThat(featureLayerInterface.sendActions())
                .as("sending action feature layer status").isTrue();
        assertThat(featureLayerInterface.sendActions())
                .as("sending empty action feature layer status").isFalse();

        gameSetup.stop();
    }
}
