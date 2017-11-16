package com.github.ocraft.s2client.protocol.unit;

import SC2APIProtocol.Raw;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum CloakState {
    CLOAKED,
    NOT_CLOAKED,
    CLOAKED_DETECTED;

    public static CloakState from(Raw.CloakState sc2ApiCloakState) {
        require("sc2api cloak state", sc2ApiCloakState);
        switch (sc2ApiCloakState) {
            case Cloaked:
                return CLOAKED;
            case NotCloaked:
                return NOT_CLOAKED;
            case CloakedDetected:
                return CLOAKED_DETECTED;
            default:
                throw new AssertionError("unknown sc2api cloak state: " + sc2ApiCloakState);
        }
    }
}
