package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

public final class RequestLeaveGame extends Request {

    private static final long serialVersionUID = -6506844164479922929L;

    private RequestLeaveGame() {
    }

    public static RequestLeaveGame leaveGame() {
        return new RequestLeaveGame();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setLeaveGame(Sc2Api.RequestLeaveGame.newBuilder().build())
                .build();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof RequestLeaveGame;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
