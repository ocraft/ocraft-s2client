package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseAvailableMapsTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveAvailableMaps() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseAvailableMaps.from(nothing()))
                .withMessage("provided argument doesn't have available maps response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseAvailableMaps.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have available maps response");
    }

    @Test
    void hasEmptySetOfMapsForEmptySc2ApiResponseAvailableMaps() {
        ResponseAvailableMaps responseAvailableMaps =
                ResponseAvailableMaps.from(emptySc2ApiResponseWithAvailableMaps());

        assertThatAllFieldsAreProperlyConverted(
                responseAvailableMaps, new ExpectedResponseData().withResponseStatus(GameStatus.QUIT));
    }

    private Sc2Api.Response emptySc2ApiResponseWithAvailableMaps() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.quit)
                .setAvailableMaps(emptySc2ApiResponseAvailableMaps()).build();
    }

    @Test
    void convertsSc2ApiResponseAvailableMapsToResponseAvailableMaps() {
        ResponseAvailableMaps responseAvailableMaps = ResponseAvailableMaps.from(sc2ApiResponseWithAvailableMaps());

        assertThatAllFieldsAreProperlyConverted(
                responseAvailableMaps,
                new ExpectedResponseData()
                        .withResponseStatus(GameStatus.IN_GAME)
                        .withBattlenetMapNames(BATTLENET_MAPS)
                        .withLocalMapPaths(LOCAL_MAP_PATHS));
    }

    private void assertThatAllFieldsAreProperlyConverted(
            ResponseAvailableMaps responseAvailableMaps, ExpectedResponseData expectedResponseData) {
        assertThat(responseAvailableMaps.getType())
                .as("type of available maps response").isEqualTo(ResponseType.AVAILABLE_MAPS);

        assertThat(responseAvailableMaps.getStatus())
                .as("status of available maps response").isEqualTo(expectedResponseData.responseStatus);

        assertThat(responseAvailableMaps.getBattlenetMaps())
                .as("battle.net map names in available maps response")
                .containsOnlyElementsOf(expectedResponseData.battlenetMaps);

        assertThat(responseAvailableMaps.getLocalMaps())
                .as("local map paths in available maps response")
                .containsOnlyElementsOf(expectedResponseData.localMaps);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ResponseAvailableMaps.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status", "battlenetMaps", "localMaps")
                .withRedefinedSuperclass()
                .verify();
    }

    private class ExpectedResponseData {
        private Set<BattlenetMap> battlenetMaps = new HashSet<>();
        private Set<LocalMap> localMaps = new HashSet<>();
        private GameStatus responseStatus;

        ExpectedResponseData withBattlenetMapNames(Set<String> battlenetMapNames) {
            this.battlenetMaps = battlenetMapNames.stream().map(BattlenetMap::of).collect(toSet());
            return this;
        }

        ExpectedResponseData withLocalMapPaths(Set<Path> localMapPaths) {
            this.localMaps = localMapPaths.stream().map(LocalMap::of).collect(toSet());
            return this;
        }

        ExpectedResponseData withResponseStatus(GameStatus responseStatus) {
            this.responseStatus = responseStatus;
            return this;
        }
    }
}