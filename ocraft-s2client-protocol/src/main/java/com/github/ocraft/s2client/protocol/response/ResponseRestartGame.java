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
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import java.util.Objects;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

/**
 * The defaultRestartGameLoops is specified to be {@literal (1<<18)} by default.
 */
public final class ResponseRestartGame extends Response {

    private static final long serialVersionUID = 4951702186011152162L;

    private final Error error;
    private final String errorDetails;
    private final Boolean needHardReset;

    public enum Error {
        LAUNCH_ERROR;

        public static Error from(Sc2Api.ResponseRestartGame.Error sc2ApiResponseRestartGameError) {
            require("sc2api response restart game error", sc2ApiResponseRestartGameError);
            switch (sc2ApiResponseRestartGameError) {
                case LaunchError:
                    return LAUNCH_ERROR;
                default:
                    throw new AssertionError("unknown response restart game error: " + sc2ApiResponseRestartGameError);
            }
        }
    }

    private ResponseRestartGame(
            Sc2Api.ResponseRestartGame sc2ApiResponseRestartGame, Sc2Api.Status sc2ApiStatus, int id) {
        super(ResponseType.RESTART_GAME, GameStatus.from(sc2ApiStatus), id);

        this.error = tryGet(
                Sc2Api.ResponseRestartGame::getError, Sc2Api.ResponseRestartGame::hasError
        ).apply(sc2ApiResponseRestartGame).map(ResponseRestartGame.Error::from).orElse(nothing());

        this.errorDetails = tryGet(
                Sc2Api.ResponseRestartGame::getErrorDetails, Sc2Api.ResponseRestartGame::hasErrorDetails
        ).apply(sc2ApiResponseRestartGame).orElse(nothing());

        this.needHardReset = tryGet(
                Sc2Api.ResponseRestartGame::getNeedHardReset, Sc2Api.ResponseRestartGame::hasNeedHardReset
        ).apply(sc2ApiResponseRestartGame).orElse(nothing());
    }

    public static ResponseRestartGame from(Sc2Api.Response sc2ApiResponse) {
        if (!hasRestartGameResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have restart game response");
        }
        return new ResponseRestartGame(
                sc2ApiResponse.getRestartGame(), sc2ApiResponse.getStatus(), sc2ApiResponse.getId());
    }

    private static boolean hasRestartGameResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasRestartGame();
    }

    public Optional<Error> getError() {
        return Optional.ofNullable(error);
    }

    public Optional<String> getErrorDetails() {
        return Optional.ofNullable(errorDetails);
    }

    /**
     * This will occur once the simulation_loop is greater then defaultRestartGameLoops.
     */
    public Optional<Boolean> getNeedHardReset() {
        return Optional.ofNullable(needHardReset);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseRestartGame)) return false;
        if (!super.equals(o)) return false;

        ResponseRestartGame that = (ResponseRestartGame) o;

        if (!that.canEqual(this)) return false;
        if (error != that.error) return false;
        if (!Objects.equals(errorDetails, that.errorDetails)) return false;
        return Objects.equals(needHardReset, that.needHardReset);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseRestartGame;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (errorDetails != null ? errorDetails.hashCode() : 0);
        result = 31 * result + (needHardReset != null ? needHardReset.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
