package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class ResponseQuitGame extends Response {

    private static final long serialVersionUID = -7537052774671405649L;

    private ResponseQuitGame(Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.QUIT_GAME, GameStatus.from(sc2ApiStatus));
    }

    public static ResponseQuitGame from(Sc2Api.Response sc2ApiResponse) {
        if (!hasQuitResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have quit response");
        }
        return new ResponseQuitGame(sc2ApiResponse.getStatus());
    }

    private static boolean hasQuitResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasQuit();
    }

    @Override
    public boolean equals(Object o) {
        return this == o ||
                (o instanceof ResponseQuitGame && super.equals(o) && ((ResponseQuitGame) o).canEqual(this));
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseQuitGame;
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
