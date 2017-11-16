/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseDataTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveData() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseData.from(nothing()))
                .withMessage("provided argument doesn't have data response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseData.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have data response");
    }

    @Test
    void convertsSc2ApiResponseDataToResponseData() {
        assertThatAllFieldsAreProperlyConverted(ResponseData.from(sc2ApiResponseWithData()));
    }

    private void assertThatAllFieldsAreProperlyConverted(ResponseData responseData) {
        assertThat(responseData.getType()).as("data: type").isEqualTo(ResponseType.DATA);
        assertThat(responseData.getStatus()).as("data: status").isEqualTo(GameStatus.LAUNCHED);
        assertThat(responseData.getAbilities()).as("data: abilities").isNotEmpty();
        assertThat(responseData.getEffects()).as("data: effects").isNotEmpty();
        assertThat(responseData.getBuffs()).as("data: buffs").isNotEmpty();
        assertThat(responseData.getUpgrades()).as("data: upgrades").isNotEmpty();
        assertThat(responseData.getUnitTypes()).as("data: unit types").isNotEmpty();
    }

    @Test
    void hasEmptyAbilityDataSetIfNotProvided() {
        assertThat(ResponseData.from(
                sc2ApiDataWithout(Sc2Api.ResponseData.Builder::clearAbilities)).getAbilities())
                .as("data: default ability set").isEmpty();
    }

    private Sc2Api.Response sc2ApiDataWithout(Consumer<Sc2Api.ResponseData.Builder> clear) {
        return Sc2Api.Response.newBuilder()
                .setData(without(() -> sc2ApiResponseData().toBuilder(), clear)).build();
    }

    @Test
    void hasEmptyEffectDataSetIfNotProvided() {
        assertThat(ResponseData.from(
                sc2ApiDataWithout(Sc2Api.ResponseData.Builder::clearEffects)).getEffects())
                .as("data: default effect set").isEmpty();
    }

    @Test
    void hasEmptyUpgradeDataSetIfNotProvided() {
        assertThat(ResponseData.from(
                sc2ApiDataWithout(Sc2Api.ResponseData.Builder::clearUpgrades)).getUpgrades())
                .as("data: default upgrade set").isEmpty();
    }

    @Test
    void hasEmptyBuffDataSetIfNotProvided() {
        assertThat(ResponseData.from(
                sc2ApiDataWithout(Sc2Api.ResponseData.Builder::clearBuffs)).getBuffs())
                .as("data: default buff set").isEmpty();
    }

    @Test
    void hasEmptyUnitTypeDataSetIfNotProvided() {
        assertThat(ResponseData.from(
                sc2ApiDataWithout(Sc2Api.ResponseData.Builder::clearUnits)).getUnitTypes())
                .as("data: default unit type set").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ResponseData.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status", "abilities", "effects", "upgrades", "buffs", "unitTypes")
                .withRedefinedSuperclass()
                .verify();
    }

}