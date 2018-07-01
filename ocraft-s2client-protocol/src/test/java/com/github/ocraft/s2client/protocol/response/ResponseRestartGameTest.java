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
import static com.github.ocraft.s2client.protocol.response.ResponseRestartGame.Error.LAUNCH_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponseRestartGameTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveRestartGame() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseRestartGame.from(nothing()))
                .withMessage("provided argument doesn't have restart game response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseRestartGame.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have restart game response");
    }

    @Test
    void convertsSc2ApiResponseRestartGameToResponseRestartGame() {
        ResponseRestartGame responseRestartGame = ResponseRestartGame.from(sc2ApiResponseWithRestartGame());

        assertThatResponseDoesNotHaveError(responseRestartGame);
        assertThatResponseIsInValidState(responseRestartGame);
    }

    private void assertThatResponseDoesNotHaveError(ResponseRestartGame responseRestartGame) {
        assertThat(responseRestartGame.getError())
                .as("response restart game doesn't have error")
                .isEqualTo(Optional.empty());

        assertThat(responseRestartGame.getErrorDetails())
                .as("response restart game doesn't have error details")
                .isEqualTo(Optional.empty());
    }

    private void assertThatResponseIsInValidState(ResponseRestartGame responseRestartGame) {
        assertThat(responseRestartGame).as("converted response restart game").isNotNull();
        assertThat(responseRestartGame.getType()).as("type of restart game response")
                .isEqualTo(ResponseType.RESTART_GAME);
        assertThat(responseRestartGame.getStatus()).as("status of restart game response").isEqualTo(GameStatus.IN_GAME);
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "responseRestartGameErrorMappings")
    void mapsSc2ApiResponseGameError(
            Sc2Api.ResponseRestartGame.Error sc2ApiResponseRestartGameError,
            ResponseRestartGame.Error expectedResponseRestartGameError) {
        assertThat(ResponseRestartGame.Error.from(sc2ApiResponseRestartGameError))
                .isEqualTo(expectedResponseRestartGameError);
    }

    private static Stream<Arguments> responseRestartGameErrorMappings() {
        return Stream.of(Arguments.of(Sc2Api.ResponseRestartGame.Error.LaunchError, LAUNCH_ERROR));
    }

    @Test
    void throwsExceptionWhenResponseRestartGameErrorIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponseRestartGame.Error.from(nothing()))
                .withMessage("sc2api response restart game error is required");
    }

    @Test
    void convertsSc2ApiResponseRestartGameWithErrorToResponseRestartGame() {
        ResponseRestartGame responseRestartGame = ResponseRestartGame.from(sc2ApiResponseWithRestartGameWithError());

        assertThatResponseIsInValidState(responseRestartGame);
        assertThatErrorsAreMapped(responseRestartGame);
    }

    private void assertThatErrorsAreMapped(ResponseRestartGame responseRestartGame) {
        assertThat(responseRestartGame.getError())
                .as("response restart game error")
                .isEqualTo(Optional.of(ResponseRestartGame.Error.LAUNCH_ERROR));

        assertThat(responseRestartGame.getErrorDetails())
                .as("response restart game error details")
                .isEqualTo(Optional.of(ERROR_RESTART_GAME));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ResponseRestartGame.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status")
                .withRedefinedSuperclass()
                .verify();
    }
}
