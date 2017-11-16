package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Sc2Api;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum Alert {
    NUCLEAR_LAUNCH_DETECTED,
    NYDUS_WORM_DETECTED;

    public static Alert from(Sc2Api.Alert sc2ApiAlert) {
        require("sc2api alert", sc2ApiAlert);
        switch (sc2ApiAlert) {
            case NuclearLaunchDetected:
                return NUCLEAR_LAUNCH_DETECTED;
            case NydusWormDetected:
                return NYDUS_WORM_DETECTED;
            default:
                throw new AssertionError("unknown alert: " + sc2ApiAlert);
        }
    }
}
