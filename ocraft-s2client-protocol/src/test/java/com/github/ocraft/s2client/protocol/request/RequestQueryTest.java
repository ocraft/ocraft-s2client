package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Query;
import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.request.RequestQuery.query;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestQueryTest {
    @Test
    void serializesToSc2ApiRequestQuery() {
        assertThatAllFieldsAreSerialized(query()
                .ofAbilities(queryAvailableAbilities().build())
                .ofPathings(queryPathing().build())
                .ofPlacements(queryBuildingPlacement().build())
                .ignoreResourceRequirements()
                .build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Sc2Api.Request sc2ApiRequest) {
        assertThat(sc2ApiRequest.hasQuery()).as("sc2api request: has query").isTrue();

        Query.RequestQuery sc2ApiRequestQuery = sc2ApiRequest.getQuery();

        assertThat(sc2ApiRequestQuery.getAbilitiesList()).as("sc2api request: abilities").isNotEmpty();
        assertThat(sc2ApiRequestQuery.getPathingList()).as("sc2api request: pathing").isNotEmpty();
        assertThat(sc2ApiRequestQuery.getPlacementsList()).as("sc2api request: placements").isNotEmpty();
        assertThat(sc2ApiRequestQuery.getIgnoreResourceRequirements()).as("sc2api request: ignore resources").isTrue();
    }

    @Test
    void serializesToSc2ApiRequestQueryUsingBuilders() {
        assertThatAllFieldsAreSerialized(query()
                .ofAbilities(queryAvailableAbilities())
                .ofPathings(queryPathing())
                .ofPlacements(queryBuildingPlacement())
                .ignoreResourceRequirements()
                .build().toSc2Api());
    }

    @Test
    void throwsExceptionWhenQueriesAreEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((RequestQuery.Builder) query()).build())
                .withMessage("one of query is required");
    }

    @Test
    void serializesDefaultIgnoreResourcesValueIfNotSet() {
        Query.RequestQuery aSc2ApiRequestQuery = query().ofPathings(queryPathing()).build().toSc2Api().getQuery();
        assertThat(aSc2ApiRequestQuery.hasIgnoreResourceRequirements())
                .as("sc2pi query: has ignore resource requirements").isTrue();
        assertThat(aSc2ApiRequestQuery.getIgnoreResourceRequirements())
                .as("sc2pi query: ignore resource requirements").isFalse();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestQuery.class)
                .withIgnoredFields("nanoTime").withNonnullFields("abilities", "pathings", "placements").verify();
    }

}