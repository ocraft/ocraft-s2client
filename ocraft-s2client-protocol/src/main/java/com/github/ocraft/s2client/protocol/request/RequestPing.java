package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

public final class RequestPing extends Request {

    private static final long serialVersionUID = -263332372977220816L;

    private RequestPing() {
    }

    public static RequestPing ping() {
        return new RequestPing();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setPing(Sc2Api.RequestPing.newBuilder().build())
                .build();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof RequestPing;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
