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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ResponseCreateGame extends Response {

    private static final long serialVersionUID = -1855364611997214141L;

    private final Error error;
    private final String errorDetails;

    public enum Error {
        MISSING_MAP,
        INVALID_MAP_PATH,
        INVALID_MAP_DATA,
        INVALID_MAP_NAME,
        INVALID_MAP_HANDLE,
        MISSING_PLAYER_SETUP,
        INVALID_PLAYER_SETUP,
        MULTIPLAYER_UNSUPPORTED;

        public static Error from(Sc2Api.ResponseCreateGame.Error sc2ApiResponseCreateGameError) {
            require("sc2api response create game error", sc2ApiResponseCreateGameError);
            switch (sc2ApiResponseCreateGameError) {
                case MissingMap:
                    return MISSING_MAP;
                case InvalidMapPath:
                    return INVALID_MAP_PATH;
                case InvalidMapData:
                    return INVALID_MAP_DATA;
                case InvalidMapName:
                    return INVALID_MAP_NAME;
                case InvalidMapHandle:
                    return INVALID_MAP_HANDLE;
                case MissingPlayerSetup:
                    return MISSING_PLAYER_SETUP;
                case InvalidPlayerSetup:
                    return INVALID_PLAYER_SETUP;
                case MultiplayerUnsupported:
                    return MULTIPLAYER_UNSUPPORTED;
                default:
                    throw new AssertionError("unknown sc2api response create game error: " +
                            sc2ApiResponseCreateGameError);
            }
        }
    }

    private ResponseCreateGame(Sc2Api.ResponseCreateGame sc2ApiResponseCreateGame, Sc2Api.Status status) {
        super(ResponseType.CREATE_GAME, GameStatus.from(status));

        this.error = tryGet(
                Sc2Api.ResponseCreateGame::getError, Sc2Api.ResponseCreateGame::hasError
        ).apply(sc2ApiResponseCreateGame).map(Error::from).orElse(nothing());

        this.errorDetails = tryGet(
                Sc2Api.ResponseCreateGame::getErrorDetails, Sc2Api.ResponseCreateGame::hasErrorDetails
        ).apply(sc2ApiResponseCreateGame).orElse(nothing());
    }

    public static ResponseCreateGame from(Sc2Api.Response sc2ApiResponse) {
        if (!hasCreateGameResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have create game response");
        }
        return new ResponseCreateGame(sc2ApiResponse.getCreateGame(), sc2ApiResponse.getStatus());
    }

    private static boolean hasCreateGameResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasCreateGame();
    }

    public Optional<Error> getError() {
        return Optional.ofNullable(error);
    }

    public Optional<String> getErrorDetails() {
        return Optional.ofNullable(errorDetails);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseCreateGame)) return false;
        if (!super.equals(o)) return false;

        ResponseCreateGame that = (ResponseCreateGame) o;

        return canEqual(that) &&
                error == that.error &&
                (errorDetails != null ? errorDetails.equals(that.errorDetails) : that.errorDetails == null);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseCreateGame;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (errorDetails != null ? errorDetails.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
