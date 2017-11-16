package com.github.ocraft.s2client.protocol.request;

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
        EqualsVerifier.forClass(RequestData.class).withIgnoredFields("nanoTime").verify();
    }
}