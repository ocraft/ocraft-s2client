package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

public final class RequestAvailableMaps extends Request {

    private static final long serialVersionUID = -5752532115407670132L;

    private RequestAvailableMaps() {
    }

    public static RequestAvailableMaps availableMaps() {
        return new RequestAvailableMaps();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setAvailableMaps(Sc2Api.RequestAvailableMaps.newBuilder().build())
                .build();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof RequestAvailableMaps;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
