package com.github.ocraft.s2client.api.controller;

import static com.github.ocraft.s2client.api.OcraftConfig.GAME_NET_PORT;
import static com.github.ocraft.s2client.api.OcraftConfig.cfg;

final class PortSetup {
    private static int portCounter = 0;
    private static int lastPort;

    private PortSetup() {
        throw new AssertionError("private constructor");
    }

    static synchronized int fetchPort() {
        lastPort = cfg().getInt(GAME_NET_PORT) + portCounter++;
        return lastPort;
    }

    static synchronized void reset() {
        portCounter = 0;
    }

    static int getLastPort() {
        return lastPort;
    }
}
