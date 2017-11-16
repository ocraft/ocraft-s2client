package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

public final class RequestQuickLoad extends Request {

    private static final long serialVersionUID = -6506844164479922929L;

    private RequestQuickLoad() {
    }

    public static RequestQuickLoad quickLoad() {
        return new RequestQuickLoad();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        return Sc2Api.Request.newBuilder()
                .setQuickLoad(Sc2Api.RequestQuickLoad.newBuilder().build())
                .build();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof RequestQuickLoad;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
