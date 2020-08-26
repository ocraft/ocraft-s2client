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
import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.GameServerResponses;
import com.github.ocraft.s2client.protocol.data.*;
import com.github.ocraft.s2client.protocol.observation.raw.Visibility;
import com.github.ocraft.s2client.protocol.response.ResponseGameInfo;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@org.junit.jupiter.api.Tag("integration")
class ObservationInterfaceImplIT {

    @Test
    void throwsExceptionWhenControlInterfaceIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ObservationInterfaceImpl(null))
                .withMessage("control interface is required");
    }

    @Test
    void providesAbilityData() {
        GameSetup gameSetup = new GameSetup().mockObservation(false).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasData, GameServerResponses::abilityData);

        Map<Ability, AbilityData> abilityData = gameSetup.observation().getAbilityData(true);
        assertThat(abilityData).as("provided ability data").isNotEmpty();

        assertThat(gameSetup.observation().getAbilityData(false))
                .as("returns cached ability data").isSameAs(abilityData);

        gameSetup.stop();
    }

    @Test
    void emitsClientErrorWhenAbilityToRemapIsUnknown() {
        GameSetup gameSetup = new GameSetup().mockObservation(false).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasData, GameServerResponses::abilityDataWithUnknownRemap);

        gameSetup.observation().getAbilityData(true);

        assertThat(gameSetup.control().getClientErrors())
                .as("contains client error")
                .containsExactly(ClientError.INVALID_ABILITY_REMAP);

        gameSetup.stop();
    }

    @Test
    void providesUnitTypesData() {
        GameSetup gameSetup = new GameSetup().mockObservation(false).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasData, GameServerResponses::unitTypeData);

        Map<UnitType, UnitTypeData> unitTypeData = gameSetup.observation().getUnitTypeData(true);
        assertThat(unitTypeData).as("provided unit type data").isNotEmpty();

        assertThat(gameSetup.observation().getUnitTypeData(false))
                .as("returns cached unit type data").isSameAs(unitTypeData);

        gameSetup.stop();
    }

    @Test
    void providesUpgradesData() {
        GameSetup gameSetup = new GameSetup().mockObservation(false).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasData, GameServerResponses::upgradeData);

        Map<Upgrade, UpgradeData> upgradeData = gameSetup.observation().getUpgradeData(true);
        assertThat(upgradeData).as("provided upgrade data").isNotEmpty();

        assertThat(gameSetup.observation().getUpgradeData(false))
                .as("returns cached upgrade data").isSameAs(upgradeData);

        gameSetup.stop();
    }

    @Test
    void providesBuffsData() {
        GameSetup gameSetup = new GameSetup().mockObservation(false).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasData, GameServerResponses::buffData);

        Map<Buff, BuffData> buffData = gameSetup.observation().getBuffData(true);
        assertThat(buffData).as("provided buff data").isNotEmpty();

        assertThat(gameSetup.observation().getBuffData(false))
                .as("returns cached buff data").isSameAs(buffData);

        gameSetup.stop();
    }

    @Test
    void providesEffectsData() {
        GameSetup gameSetup = new GameSetup().mockObservation(false).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasData, GameServerResponses::effectData);

        Map<Effect, EffectData> effectData = gameSetup.observation().getEffectData(true);
        assertThat(effectData).as("provided effect data").isNotEmpty();

        assertThat(gameSetup.observation().getEffectData(false))
                .as("returns cached effect data").isSameAs(effectData);

        gameSetup.stop();
    }

    @Test
    void providesGameInfo() {
        GameSetup gameSetup = new GameSetup().mockObservation(false).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasGameInfo, GameServerResponses::gameInfo);

        ResponseGameInfo gameInfo = gameSetup.observation().getGameInfo(true);
        assertThat(gameInfo).as("provided game info").isNotNull();

        assertThat(gameSetup.observation().getGameInfo()).as("returns cached game info").isSameAs(gameInfo);

        gameSetup.stop();
    }

    @Test
    void providesInformationFromImageData() {
        GameSetup gameSetup = new GameSetup().mockObservation(false).start();
        gameSetup.server().onRequest(Sc2Api.Request::hasObservation, GameServerResponses::observation);
        gameSetup.server().onRequest(Sc2Api.Request::hasGameInfo, GameServerResponses::gameInfo);
        gameSetup.server().onRequest(Sc2Api.Request::hasData, GameServerResponses::abilityData);

        assertThat(gameSetup.control().getObservation()).as("status of update observation").isTrue();

        Point2d p01 = Point2d.of(10.0f, 10.0f);
        Point2d p02 = Point2d.of(11.0f, 10.0f);
        assertThat(gameSetup.observation().hasCreep(p01)).isTrue();
        assertThat(gameSetup.observation().getVisibility(p01)).isEqualTo(Visibility.VISIBLE);
        assertThat(gameSetup.observation().isPathable(p01)).isTrue();
        assertThat(gameSetup.observation().isPlacable(p02)).isTrue();
        assertThat(gameSetup.observation().terrainHeight(p01)).isGreaterThan(-100.0f);

        gameSetup.stop();
    }

}
