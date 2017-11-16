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
import static com.github.ocraft.s2client.protocol.response.ResponseStartReplay.Error.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class ResponseStartReplayTest {
    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveStartReplay() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseStartReplay.from(nothing()))
                .withMessage("provided argument doesn't have start replay response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseStartReplay.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have start replay response");
    }

    @Test
    void convertsSc2ApiResponseStartReplayToResponseStartReplay() {
        ResponseStartReplay responseStartReplay = ResponseStartReplay.from(sc2ApiResponseWithStartReplay());

        assertThatResponseDoesNotHaveError(responseStartReplay);
        assertThatResponseIsInValidState(responseStartReplay);
    }

    private void assertThatResponseDoesNotHaveError(ResponseStartReplay responseStartReplay) {
        assertThat(responseStartReplay.getError())
                .as("response start replay doesn't have error")
                .isEqualTo(Optional.empty());

        assertThat(responseStartReplay.getErrorDetails())
                .as("response start replay doesn't have error details")
                .isEqualTo(Optional.empty());
    }

    private void assertThatResponseIsInValidState(ResponseStartReplay responseStartReplay) {
        assertThat(responseStartReplay).as("converted response start replay").isNotNull();
        assertThat(responseStartReplay.getType()).as("type of start replay response")
                .isEqualTo(ResponseType.START_REPLAY);
        assertThat(responseStartReplay.getStatus()).as("status of start replay response")
                .isEqualTo(GameStatus.IN_REPLAY);
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "responseStartReplayErrorMappings")
    void mapsSc2ApiResponseGameError(
            Sc2Api.ResponseStartReplay.Error sc2ApiResponseStartReplayError,
            ResponseStartReplay.Error expectedResponseStartReplayError) {
        assertThat(ResponseStartReplay.Error.from(sc2ApiResponseStartReplayError))
                .isEqualTo(expectedResponseStartReplayError);
    }

    private static Stream<Arguments> responseStartReplayErrorMappings() {
        return Stream.of(
                of(Sc2Api.ResponseStartReplay.Error.MissingReplay, MISSING_REPLAY),
                of(Sc2Api.ResponseStartReplay.Error.InvalidReplayPath, INVALID_REPLAY_PATH),
                of(Sc2Api.ResponseStartReplay.Error.InvalidReplayData, INVALID_REPLAY_DATA),
                of(Sc2Api.ResponseStartReplay.Error.InvalidMapData, INVALID_MAP_DATA),
                of(Sc2Api.ResponseStartReplay.Error.InvalidObservedPlayerId, INVALID_OBSERVED_PLAYER_ID),
                of(Sc2Api.ResponseStartReplay.Error.MissingOptions, MISSING_OPTIONS),
                of(Sc2Api.ResponseStartReplay.Error.LaunchError, LAUNCH_ERROR));
    }

    @Test
    void throwsExceptionWhenResponseStartReplayErrorIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseStartReplay.Error.from(nothing()))
                .withMessage("sc2api response start replay error is required");
    }

    @Test
    void convertsSc2ApiResponseStartReplayWithErrorToResponseStartReplay() {
        ResponseStartReplay responseStartReplay = ResponseStartReplay.from(sc2ApiResponseWithStartReplayWithError());

        assertThatResponseIsInValidState(responseStartReplay);

        assertThat(responseStartReplay.getError())
                .as("response start replay error")
                .isEqualTo(Optional.of(ResponseStartReplay.Error.LAUNCH_ERROR));

        assertThat(responseStartReplay.getErrorDetails())
                .as("response start replay error details")
                .isEqualTo(Optional.of(ERROR_START_REPLAY));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseStartReplay.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}