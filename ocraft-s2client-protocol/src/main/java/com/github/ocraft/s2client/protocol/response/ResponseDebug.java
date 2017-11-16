package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class ResponseDebug extends Response {

    private static final long serialVersionUID = -6077963672255615287L;

    private ResponseDebug(Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.DEBUG, GameStatus.from(sc2ApiStatus));
    }

    public static ResponseDebug from(Sc2Api.Response sc2ApiResponse) {
        if (!hasDebugResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have debug response");
        }
        return new ResponseDebug(sc2ApiResponse.getStatus());
    }

    private static boolean hasDebugResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasDebug();
    }

    @Override
    public boolean equals(Object o) {
        return this == o ||
                (o instanceof ResponseDebug && super.equals(o) && ((ResponseDebug) o).canEqual(this));
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseDebug;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
