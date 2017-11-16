package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.response.ResponseSaveMap.Error.INVALID_MAP_DATA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseSaveMapTest {
    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveSaveMap() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseSaveMap.from(nothing()))
                .withMessage("provided argument doesn't have save map response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseSaveMap.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have save map response");
    }

    @Test
    void convertsSc2ApiResponseSaveMapToResponseSaveMap() {
        ResponseSaveMap responseSaveMap = ResponseSaveMap.from(sc2ApiResponseWithSaveMap());

        assertThatResponseDoesNotHaveError(responseSaveMap);
        assertThatResponseIsInValidState(responseSaveMap);
    }

    private void assertThatResponseDoesNotHaveError(ResponseSaveMap responseSaveMap) {
        assertThat(responseSaveMap.getError())
                .as("response save map doesn't have error")
                .isEqualTo(Optional.empty());
    }

    private void assertThatResponseIsInValidState(ResponseSaveMap responseSaveMap) {
        assertThat(responseSaveMap).as("converted response save map").isNotNull();
        assertThat(responseSaveMap.getType()).as("type of save map response")
                .isEqualTo(ResponseType.SAVE_MAP);
        assertThat(responseSaveMap.getStatus()).as("status of save map response").isEqualTo(GameStatus.IN_GAME);
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "responseSaveMapErrorMappings")
    void mapsSc2ApiResponseGameError(
            Sc2Api.ResponseSaveMap.Error sc2ApiResponseSaveMapError,
            ResponseSaveMap.Error expectedResponseSaveMapError) {
        assertThat(ResponseSaveMap.Error.from(sc2ApiResponseSaveMapError))
                .isEqualTo(expectedResponseSaveMapError);
    }

    private static Stream<Arguments> responseSaveMapErrorMappings() {
        return Stream.of(Arguments.of(Sc2Api.ResponseSaveMap.Error.InvalidMapData, INVALID_MAP_DATA));
    }

    @Test
    void throwsExceptionWhenResponseSaveMapErrorIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseSaveMap.Error.from(nothing()))
                .withMessage("sc2api response save map error is required");
    }

    @Test
    void convertsSc2ApiResponseSaveMapWithErrorToResponseSaveMap() {
        ResponseSaveMap responseSaveMap = ResponseSaveMap.from(sc2ApiResponseWithSaveMapWithError());

        assertThatResponseIsInValidState(responseSaveMap);
        assertThatErrorIsMapped(responseSaveMap);
    }

    private void assertThatErrorIsMapped(ResponseSaveMap responseSaveMap) {
        assertThat(responseSaveMap.getError())
                .as("response save map error")
                .isEqualTo(Optional.of(ResponseSaveMap.Error.INVALID_MAP_DATA));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseSaveMap.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}