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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class ResponseCreateGameTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveCreateGame() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseCreateGame.from(nothing()))
                .withMessage("provided argument doesn't have create game response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseCreateGame.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have create game response");
    }

    @Test
    void convertsSc2ApiResponseCreateGameToResponseCreateGame() {
        assertThatResponseCreateGameDoesNotHaveError(ResponseCreateGame.from(sc2ApiResponseWithCreateGame()));
    }

    private void assertThatResponseCreateGameDoesNotHaveError(ResponseCreateGame responseCreateGame) {
        assertThatResponseIsInValidState(responseCreateGame);

        assertThat(responseCreateGame.getError())
                .as("response create game doesn't have error")
                .isEqualTo(Optional.empty());

        assertThat(responseCreateGame.getErrorDetails())
                .as("response create game doesn't have error details")
                .isEqualTo(Optional.empty());
    }

    private void assertThatResponseIsInValidState(ResponseCreateGame responseCreateGame) {
        assertThat(responseCreateGame.getType()).as("type of create map response").isEqualTo(ResponseType.CREATE_GAME);
        assertThat(responseCreateGame.getStatus()).as("status of create map response").isEqualTo(GameStatus.IN_GAME);
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "responseCreateGameErrorMappings")
    void mapsSc2ApiResponseCreateGameError(
            Sc2Api.ResponseCreateGame.Error sc2ApiResponseCreateGameError,
            ResponseCreateGame.Error expectedResponseCreateGameError) {
        assertThat(ResponseCreateGame.Error.from(sc2ApiResponseCreateGameError))
                .isEqualTo(expectedResponseCreateGameError);
    }

    private static Stream<Arguments> responseCreateGameErrorMappings() {
        return Stream.of(
                of(Sc2Api.ResponseCreateGame.Error.MissingMap, ResponseCreateGame.Error.MISSING_MAP),
                of(Sc2Api.ResponseCreateGame.Error.InvalidMapPath, ResponseCreateGame.Error.INVALID_MAP_PATH),
                of(Sc2Api.ResponseCreateGame.Error.InvalidMapData, ResponseCreateGame.Error.INVALID_MAP_DATA),
                of(Sc2Api.ResponseCreateGame.Error.InvalidMapName, ResponseCreateGame.Error.INVALID_MAP_NAME),
                of(Sc2Api.ResponseCreateGame.Error.InvalidMapHandle, ResponseCreateGame.Error.INVALID_MAP_HANDLE),
                of(Sc2Api.ResponseCreateGame.Error.MissingPlayerSetup, ResponseCreateGame.Error.MISSING_PLAYER_SETUP),
                of(Sc2Api.ResponseCreateGame.Error.InvalidPlayerSetup, ResponseCreateGame.Error.INVALID_PLAYER_SETUP),
                of(Sc2Api.ResponseCreateGame.Error.MultiplayerUnsupported, ResponseCreateGame.Error.MULTIPLAYER_UNSUPPORTED));
    }

    @Test
    void throwsExceptionWhenResponseCreateGameErrorIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseCreateGame.Error.from(nothing()))
                .withMessage("sc2api response create game error is required");
    }

    @Test
    void convertsSc2ApiResponseCreateGameWithErrorToResponseCreateGame() {
        ResponseCreateGame responseCreateGame = ResponseCreateGame.from(sc2ApiResponseWithCreateGameWithError());

        assertThatResponseIsInValidState(responseCreateGame);
        assertThatErrorsAreMapped(responseCreateGame);
    }

    private void assertThatErrorsAreMapped(ResponseCreateGame responseCreateGame) {
        assertThat(responseCreateGame.getError())
                .as("response create game error")
                .isEqualTo(Optional.of(ResponseCreateGame.Error.INVALID_MAP_NAME));

        assertThat(responseCreateGame.getErrorDetails())
                .as("response create game error details")
                .isEqualTo(Optional.of(ERROR_CREATE_GAME));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ResponseCreateGame.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}
