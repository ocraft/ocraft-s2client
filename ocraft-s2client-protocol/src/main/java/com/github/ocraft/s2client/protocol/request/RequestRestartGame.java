package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

public final class RequestRestartGame extends Request {

    private static final long serialVersionUID = 3461285034931238422L;

    private RequestRestartGame() {
    }

    public static RequestRestartGame restartGame() {
        return new RequestRestartGame();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setRestartGame(Sc2Api.RequestRestartGame.newBuilder().build())
                .build();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof RequestRestartGame;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
