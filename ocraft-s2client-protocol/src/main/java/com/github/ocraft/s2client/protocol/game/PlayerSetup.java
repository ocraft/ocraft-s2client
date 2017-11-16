package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

public class PlayerSetup implements Sc2ApiSerializable<Sc2Api.PlayerSetup> {

    private static final long serialVersionUID = -6790202317629033299L;

    private final PlayerType playerType;

    PlayerSetup(PlayerType playerType) {
        this.playerType = playerType;
    }

    public static PlayerSetup observer() {
        return new PlayerSetup(PlayerType.OBSERVER);
    }

    public static PlayerSetup participant() {
        return new PlayerSetup(PlayerType.PARTICIPANT);
    }

    @Override
    public Sc2Api.PlayerSetup toSc2Api() {
        return Sc2Api.PlayerSetup.newBuilder()
                .setType(playerType.toSc2Api())
                .build();
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerSetup)) return false;

        PlayerSetup that = (PlayerSetup) o;

        return that.canEqual(this) && playerType == that.playerType;
    }

    public boolean canEqual(Object other) {
        return (other instanceof PlayerSetup);
    }

    @Override
    public int hashCode() {
        return playerType.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
