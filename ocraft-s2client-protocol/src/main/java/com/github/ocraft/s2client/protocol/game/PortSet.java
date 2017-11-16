package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

public final class PortSet implements Sc2ApiSerializable<Sc2Api.PortSet> {

    private static final long serialVersionUID = -1934268334937302438L;

    private final int gamePort;
    private final int basePort;

    private PortSet(int gamePort, int basePort) {
        this.gamePort = gamePort;
        this.basePort = basePort;
    }

    public static PortSet of(int gamePort, int basePort) {
        if (!portIsValid(gamePort) || !portIsValid(basePort))
            throw new IllegalArgumentException("port must be greater than 0");
        return new PortSet(gamePort, basePort);
    }

    private static boolean portIsValid(int portValue) {
        return portValue > 0;
    }

    @Override
    public Sc2Api.PortSet toSc2Api() {
        return Sc2Api.PortSet.newBuilder()
                .setGamePort(gamePort)
                .setBasePort(basePort)
                .build();
    }

    public int getGamePort() {
        return gamePort;
    }

    public int getBasePort() {
        return basePort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PortSet portSet = (PortSet) o;

        return gamePort == portSet.gamePort && basePort == portSet.basePort;
    }

    @Override
    public int hashCode() {
        int result = gamePort;
        result = 31 * result + basePort;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
