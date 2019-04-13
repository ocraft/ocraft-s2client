package com.github.ocraft.s2client.protocol.request;

/*-
 * #%L
 * ocraft-s2client-protocol
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
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.request.RequestData.Type.*;
import static com.github.ocraft.s2client.protocol.request.RequestData.data;
import static org.assertj.core.api.Assertions.assertThat;

class RequestDataTest {
    @Test
    void serializesToSc2ApiRequestData() {
        Sc2Api.Request sc2ApiRequest = data().of(ABILITIES, BUFFS, UPGRADES, EFFECTS, UNITS).build().toSc2Api();

        assertThat(sc2ApiRequest.hasData()).as("sc2api request has data").isTrue();

        Sc2Api.RequestData sc2ApiRequestData = sc2ApiRequest.getData();

        assertThat(sc2ApiRequestData.getAbilityId()).as("data: ability").isTrue();
        assertThat(sc2ApiRequestData.getBuffId()).as("data: buff").isTrue();
        assertThat(sc2ApiRequestData.getEffectId()).as("data: effect").isTrue();
        assertThat(sc2ApiRequestData.getUnitTypeId()).as("data: unit type").isTrue();
        assertThat(sc2ApiRequestData.getUpgradeId()).as("data: upgrade").isTrue();
    }

    @Test
    void serializesDefaultAbilityValueIfNotSet() {
        Sc2Api.RequestData aSc2ApiRequestData = defaultRequestData().toSc2Api().getData();
        assertThat(aSc2ApiRequestData.hasAbilityId()).as("data: ability value is set").isTrue();
        assertThat(aSc2ApiRequestData.getAbilityId()).as("data: default ability value").isFalse();
    }

    private RequestData defaultRequestData() {
        return ((RequestData.Builder) data()).build();
    }

    @Test
    void serializesDefaultBuffValueIfNotSet() {
        Sc2Api.RequestData aSc2ApiRequestData = defaultRequestData().toSc2Api().getData();
        assertThat(aSc2ApiRequestData.hasBuffId()).as("data: buff value is set").isTrue();
        assertThat(aSc2ApiRequestData.getBuffId()).as("data: default buff value").isFalse();
    }

    @Test
    void serializesDefaultEffectValueIfNotSet() {
        Sc2Api.RequestData aSc2ApiRequestData = defaultRequestData().toSc2Api().getData();
        assertThat(aSc2ApiRequestData.hasEffectId()).as("data: effect value is set").isTrue();
        assertThat(aSc2ApiRequestData.getEffectId()).as("data: default effect value").isFalse();
    }

    @Test
    void serializesDefaultUnitTypeValueIfNotSet() {
        Sc2Api.RequestData aSc2ApiRequestData = defaultRequestData().toSc2Api().getData();
        assertThat(aSc2ApiRequestData.hasUnitTypeId()).as("data: unit type value is set").isTrue();
        assertThat(aSc2ApiRequestData.getUnitTypeId()).as("data: default unit type value").isFalse();
    }

    @Test
    void serializesDefaultUpgradeValueIfNotSet() {
        Sc2Api.RequestData aSc2ApiRequestData = defaultRequestData().toSc2Api().getData();
        assertThat(aSc2ApiRequestData.hasUpgradeId()).as("data: upgrade value is set").isTrue();
        assertThat(aSc2ApiRequestData.getUpgradeId()).as("data: default upgrade value").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestData.class)
                .withIgnoredFields("nanoTime")
                .withRedefinedSuperclass()
                .verify();
    }
}
