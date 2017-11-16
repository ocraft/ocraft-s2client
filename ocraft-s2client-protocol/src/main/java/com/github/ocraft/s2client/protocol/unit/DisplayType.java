package com.github.ocraft.s2client.protocol.unit;

import SC2APIProtocol.Raw;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum DisplayType {
    VISIBLE,
    HIDDEN,
    SNAPSHOT;

    public static DisplayType from(Raw.DisplayType sc2ApiDisplayType) {
        require("sc2api display type", sc2ApiDisplayType);
        switch (sc2ApiDisplayType) {
            case Visible:
                return VISIBLE;
            case Hidden:
                return HIDDEN;
            case Snapshot:
                return SNAPSHOT;
            default:
                throw new AssertionError("unknown sc2api display type: " + sc2ApiDisplayType);
        }
    }
}
