package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

public final class RequestGameInfo extends Request {

    private static final long serialVersionUID = -5889437657091684444L;

    private RequestGameInfo() {
    }

    public static RequestGameInfo gameInfo() {
        return new RequestGameInfo();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setGameInfo(Sc2Api.RequestGameInfo.newBuilder().build())
                .build();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof RequestGameInfo;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}