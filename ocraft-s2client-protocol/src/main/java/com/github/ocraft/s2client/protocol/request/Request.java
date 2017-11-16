package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;

public abstract class Request implements Sc2ApiSerializable<Sc2Api.Request> {
    private static final long serialVersionUID = 6516555820286086638L;

    private final long nanoTime = System.nanoTime();

    public long getNanoTime() {
        return nanoTime;
    }
}
