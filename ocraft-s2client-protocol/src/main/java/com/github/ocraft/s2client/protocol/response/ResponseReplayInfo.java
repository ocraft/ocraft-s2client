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
import com.github.ocraft.s2client.protocol.game.ReplayInfo;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ResponseReplayInfo extends Response {

    private static final long serialVersionUID = -4941777617976006893L;

    private final ResponseReplayInfo.Error error;
    private final String errorDetails;
    private final ReplayInfo replayInfo;

    public enum Error {
        MISSING_REPLAY,
        INVALID_REPLAY_PATH,
        INVALID_REPLAY_DATA,
        PARSING_ERROR,
        DOWNLOAD_ERROR;

        public static ResponseReplayInfo.Error from(Sc2Api.ResponseReplayInfo.Error sc2ApiResponseReplayInfoError) {
            require("sc2api response replay info error", sc2ApiResponseReplayInfoError);
            switch (sc2ApiResponseReplayInfoError) {
                case MissingReplay:
                    return MISSING_REPLAY;
                case InvalidReplayPath:
                    return INVALID_REPLAY_PATH;
                case InvalidReplayData:
                    return INVALID_REPLAY_DATA;
                case ParsingError:
                    return PARSING_ERROR;
                case DownloadError:
                    return DOWNLOAD_ERROR;
                default:
                    throw new AssertionError("unknown sc2api response replay info error: " +
                            sc2ApiResponseReplayInfoError);
            }
        }
    }

    private ResponseReplayInfo(Sc2Api.ResponseReplayInfo sc2ApiResponseReplayInfo, Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.REPLAY_INFO, GameStatus.from(sc2ApiStatus));

        if (!sc2ApiResponseReplayInfo.hasError()) {
            this.replayInfo = ReplayInfo.from(sc2ApiResponseReplayInfo);
            this.error = nothing();
            this.errorDetails = nothing();
        } else {
            this.error = tryGet(
                    Sc2Api.ResponseReplayInfo::getError, Sc2Api.ResponseReplayInfo::hasError
            ).apply(sc2ApiResponseReplayInfo).map(ResponseReplayInfo.Error::from).orElse(nothing());

            this.errorDetails = tryGet(
                    Sc2Api.ResponseReplayInfo::getErrorDetails, Sc2Api.ResponseReplayInfo::hasErrorDetails
            ).apply(sc2ApiResponseReplayInfo).orElse(nothing());

            this.replayInfo = nothing();
        }
    }


    public static ResponseReplayInfo from(Sc2Api.Response sc2ApiResponse) {
        if (!hasReplayInfoResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have replay info response");
        }
        return new ResponseReplayInfo(sc2ApiResponse.getReplayInfo(), sc2ApiResponse.getStatus());
    }

    private static boolean hasReplayInfoResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasReplayInfo();
    }

    public Optional<ResponseReplayInfo.Error> getError() {
        return Optional.ofNullable(error);
    }

    public Optional<String> getErrorDetails() {
        return Optional.ofNullable(errorDetails);
    }

    public Optional<ReplayInfo> getReplayInfo() {
        return Optional.ofNullable(replayInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResponseReplayInfo that = (ResponseReplayInfo) o;

        return that.canEqual(this) &&
                error == that.error &&
                (errorDetails != null ? errorDetails.equals(that.errorDetails) : that.errorDetails == null) &&
                (replayInfo != null ? replayInfo.equals(that.replayInfo) : that.replayInfo == null);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseReplayInfo;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (errorDetails != null ? errorDetails.hashCode() : 0);
        result = 31 * result + (replayInfo != null ? replayInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
