package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ResponseStartReplay extends Response {

    private static final long serialVersionUID = -1244304257996965692L;

    private final ResponseStartReplay.Error error;
    private final String errorDetails;

    public enum Error {
        MISSING_REPLAY,
        INVALID_REPLAY_PATH,
        INVALID_REPLAY_DATA,
        INVALID_MAP_DATA,
        INVALID_OBSERVED_PLAYER_ID,
        MISSING_OPTIONS,
        LAUNCH_ERROR;

        public static ResponseStartReplay.Error from(Sc2Api.ResponseStartReplay.Error sc2ApiResponseStartReplayError) {
            require("sc2api response start replay error", sc2ApiResponseStartReplayError);
            switch (sc2ApiResponseStartReplayError) {
                case MissingReplay:
                    return MISSING_REPLAY;
                case InvalidReplayPath:
                    return INVALID_REPLAY_PATH;
                case InvalidReplayData:
                    return INVALID_REPLAY_DATA;
                case InvalidMapData:
                    return INVALID_MAP_DATA;
                case InvalidObservedPlayerId:
                    return INVALID_OBSERVED_PLAYER_ID;
                case MissingOptions:
                    return MISSING_OPTIONS;
                case LaunchError:
                    return LAUNCH_ERROR;
                default:
                    throw new AssertionError("unknown sc2api response start replay error: " +
                            sc2ApiResponseStartReplayError);
            }
        }
    }

    private ResponseStartReplay(Sc2Api.ResponseStartReplay sc2ApiResponseStartReplay, Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.START_REPLAY, GameStatus.from(sc2ApiStatus));

        this.error = tryGet(
                Sc2Api.ResponseStartReplay::getError, Sc2Api.ResponseStartReplay::hasError
        ).apply(sc2ApiResponseStartReplay).map(ResponseStartReplay.Error::from).orElse(nothing());

        this.errorDetails = tryGet(
                Sc2Api.ResponseStartReplay::getErrorDetails, Sc2Api.ResponseStartReplay::hasErrorDetails
        ).apply(sc2ApiResponseStartReplay).orElse(nothing());
    }

    public static ResponseStartReplay from(Sc2Api.Response sc2ApiResponse) {
        if (!hasStartReplayResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have start replay response");
        }
        return new ResponseStartReplay(sc2ApiResponse.getStartReplay(), sc2ApiResponse.getStatus());
    }

    private static boolean hasStartReplayResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasStartReplay();
    }

    public Optional<ResponseStartReplay.Error> getError() {
        return Optional.ofNullable(error);
    }

    public Optional<String> getErrorDetails() {
        return Optional.ofNullable(errorDetails);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseStartReplay)) return false;
        if (!super.equals(o)) return false;

        ResponseStartReplay that = (ResponseStartReplay) o;

        return that.canEqual(this) &&
                error == that.error &&
                (errorDetails != null ? errorDetails.equals(that.errorDetails) : that.errorDetails == null);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseStartReplay;
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
