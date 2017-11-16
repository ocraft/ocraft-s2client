package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Query;
import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseQueryTest {
    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveQuery() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseQuery.from(nothing()))
                .withMessage("provided argument doesn't have query response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseQuery.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have query response");
    }

    @Test
    void convertsSc2ApiResponseQueryToResponseQuery() {
        assertThatAllFieldsAreProperlyConverted(ResponseQuery.from(sc2ApiResponseWithQuery()));
    }

    private void assertThatAllFieldsAreProperlyConverted(ResponseQuery responseQuery) {
        assertThat(responseQuery.getType()).as("query: type").isEqualTo(ResponseType.QUERY);
        assertThat(responseQuery.getStatus()).as("query: status").isEqualTo(GameStatus.IN_GAME);

    }

    @Test
    void hasEmptyPathingListIfNotProvided() {
        assertThat(ResponseQuery.from(
                sc2ApiQueryWithout(Query.ResponseQuery.Builder::clearPathing)).getPathing())
                .as("query: default pathing").isEmpty();
    }

    private Sc2Api.Response sc2ApiQueryWithout(Consumer<Query.ResponseQuery.Builder> clear) {
        return Sc2Api.Response.newBuilder()
                .setQuery(without(() -> sc2ApiResponseQuery().toBuilder(), clear)).build();
    }

    @Test
    void hasEmptyAbilitiesListIfNotProvided() {
        assertThat(ResponseQuery.from(
                sc2ApiQueryWithout(Query.ResponseQuery.Builder::clearAbilities)).getAbilities())
                .as("query: default abilities").isEmpty();
    }

    @Test
    void hasEmptyPlacementListIfNotProvided() {
        assertThat(ResponseQuery.from(
                sc2ApiQueryWithout(Query.ResponseQuery.Builder::clearPlacements)).getPlacements())
                .as("query: default placements").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ResponseQuery.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status", "placements", "abilities", "pathing")
                .withRedefinedSuperclass()
                .verify();
    }
}