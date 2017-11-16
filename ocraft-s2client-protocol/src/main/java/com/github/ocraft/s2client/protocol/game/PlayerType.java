package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum PlayerType implements Sc2ApiSerializable<Sc2Api.PlayerType> {
    PARTICIPANT, COMPUTER, OBSERVER;

    public static PlayerType from(Sc2Api.PlayerType sc2ApiPlayerType) {
        require("sc2api player type", sc2ApiPlayerType);
        switch (sc2ApiPlayerType) {
            case Participant:
                return PARTICIPANT;
            case Computer:
                return COMPUTER;
            case Observer:
                return OBSERVER;
            default:
                throw new AssertionError("unknown sc2api player type: " + sc2ApiPlayerType);
        }
    }

    @Override
    public Sc2Api.PlayerType toSc2Api() {
        switch (this) {
            case PARTICIPANT:
                return Sc2Api.PlayerType.Participant;
            case COMPUTER:
                return Sc2Api.PlayerType.Computer;
            case OBSERVER:
                return Sc2Api.PlayerType.Observer;
            default:
                throw new AssertionError("unknown player type: " + this);
        }
    }
}
