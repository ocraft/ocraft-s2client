package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class ResponseLeaveGame extends Response {

    private static final long serialVersionUID = -3809601289626843992L;

    private ResponseLeaveGame(Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.LEAVE_GAME, GameStatus.from(sc2ApiStatus));
    }

    public static ResponseLeaveGame from(Sc2Api.Response sc2ApiResponse) {
        if (!hasLeaveGameResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have leave game response");
        }
        return new ResponseLeaveGame(sc2ApiResponse.getStatus());
    }

    private static boolean hasLeaveGameResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasLeaveGame();
    }

    @Override
    public boolean equals(Object o) {
        return this == o ||
                (o instanceof ResponseLeaveGame && super.equals(o) && ((ResponseLeaveGame) o).canEqual(this));
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseLeaveGame;
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
