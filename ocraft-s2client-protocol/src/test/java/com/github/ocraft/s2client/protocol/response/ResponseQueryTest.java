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