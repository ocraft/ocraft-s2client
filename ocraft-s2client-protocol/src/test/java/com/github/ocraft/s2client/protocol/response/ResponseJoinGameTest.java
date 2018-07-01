package com.github.ocraft.s2client.protocol.response;

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
import static com.github.ocraft.s2client.protocol.response.ResponseJoinGame.Error.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class ResponseJoinGameTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveJoinGame() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseJoinGame.from(nothing()))
                .withMessage("provided argument doesn't have join game response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseJoinGame.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have join game response");
    }

    @Test
    void convertsSc2ApiResponseJoinGameToResponseJoinGame() {
        assertThatResponseJoinGameDoesNotHaveError(ResponseJoinGame.from(sc2ApiResponseWithJoinGame()));
    }

    private void assertThatResponseJoinGameDoesNotHaveError(ResponseJoinGame responseJoinGame) {
        assertThatResponseIsInValidState(responseJoinGame);

        assertThat(responseJoinGame.getError())
                .as("response join game doesn't have error")
                .isEqualTo(Optional.empty());

        assertThat(responseJoinGame.getErrorDetails())
                .as("response join game doesn't have error details")
                .isEqualTo(Optional.empty());

        assertThat(responseJoinGame.getPlayerId())
                .as("player id is mapped")
                .isEqualTo(PLAYER_ID);
    }

    private void assertThatResponseIsInValidState(ResponseJoinGame responseJoinGame) {
        assertThat(responseJoinGame.getType()).as("type of join map response").isEqualTo(ResponseType.JOIN_GAME);
        assertThat(responseJoinGame.getStatus()).as("status of join map response").isEqualTo(GameStatus.IN_GAME);
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "responseJoinGameErrorMappings")
    void mapsSc2ApiResponseGameError(
            Sc2Api.ResponseJoinGame.Error sc2ApiResponseJoinGameError,
            ResponseJoinGame.Error expectedResponseJoinGameError) {
        assertThat(ResponseJoinGame.Error.from(sc2ApiResponseJoinGameError)).isEqualTo(expectedResponseJoinGameError);
    }

    private static Stream<Arguments> responseJoinGameErrorMappings() {
        return Stream.of(
                of(Sc2Api.ResponseJoinGame.Error.MissingParticipation, MISSING_PARTICIPATION),
                of(Sc2Api.ResponseJoinGame.Error.InvalidObservedPlayerId, INVALID_OBSERVED_PLAYER_ID),
                of(Sc2Api.ResponseJoinGame.Error.MissingOptions, MISSING_OPTIONS),
                of(Sc2Api.ResponseJoinGame.Error.MissingPorts, MISSING_PORTS),
                of(Sc2Api.ResponseJoinGame.Error.GameFull, GAME_FULL),
                of(Sc2Api.ResponseJoinGame.Error.LaunchError, LAUNCH_ERROR),
                of(Sc2Api.ResponseJoinGame.Error.FeatureUnsupported, FEATURE_UNSUPPORTED),
                of(Sc2Api.ResponseJoinGame.Error.NoSpaceForUser, NO_SPACE_FOR_USER),
                of(Sc2Api.ResponseJoinGame.Error.MapDoesNotExist, MAP_DOES_NOT_EXIST),
                of(Sc2Api.ResponseJoinGame.Error.CannotOpenMap, CANNOT_OPEN_MAP),
                of(Sc2Api.ResponseJoinGame.Error.ChecksumError, CHECKSUM_ERROR),
                of(Sc2Api.ResponseJoinGame.Error.NetworkError, NETWORK_ERROR),
                of(Sc2Api.ResponseJoinGame.Error.OtherError, OTHER_ERROR));
    }

    @Test
    void throwsExceptionWhenResponseJoinGameErrorIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseJoinGame.Error.from(nothing()))
                .withMessage("sc2api response join game error is required");
    }

    @Test
    void convertsSc2ApiResponseJoinGameWithErrorToResponseJoinGame() {
        ResponseJoinGame responseJoinGame = ResponseJoinGame.from(sc2ApiResponseWithJoinGameWithError());

        assertThatResponseIsInValidState(responseJoinGame);
        assertThatErrorsAreMapped(responseJoinGame);
    }

    private void assertThatErrorsAreMapped(ResponseJoinGame responseJoinGame) {
        assertThat(responseJoinGame.getError())
                .as("response join game error")
                .isEqualTo(Optional.of(ResponseJoinGame.Error.MISSING_PARTICIPATION));

        assertThat(responseJoinGame.getErrorDetails())
                .as("response join game error details")
                .isEqualTo(Optional.of(ERROR_JOIN_GAME));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ResponseJoinGame.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}
