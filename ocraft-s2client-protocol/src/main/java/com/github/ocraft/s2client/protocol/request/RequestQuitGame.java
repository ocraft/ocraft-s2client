package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

public final class RequestQuitGame extends Request {

    private static final long serialVersionUID = -2769708443808189584L;

    private RequestQuitGame() {
    }

    public static RequestQuitGame quitGame() {
        return new RequestQuitGame();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setQuit(Sc2Api.RequestQuit.newBuilder().build())
                .build();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof RequestQuitGame;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
