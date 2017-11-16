package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;

public enum DebugGameState implements Sc2ApiSerializable<Debug.DebugGameState> {
    SHOW_MAP,
    CONTROL_ENEMY,
    FOOD,
    FREE,
    ALL_RESOURCES,
    GOD,
    MINERALS,
    GAS,
    COOLDOWN,
    TECH_TREE,
    UPGRADE,
    FAST_BUILD;

    @Override
    public Debug.DebugGameState toSc2Api() {
        switch (this) {
            case GAS:
                return Debug.DebugGameState.gas;
            case GOD:
                return Debug.DebugGameState.god;
            case FOOD:
                return Debug.DebugGameState.food;
            case FREE:
                return Debug.DebugGameState.free;
            case UPGRADE:
                return Debug.DebugGameState.upgrade;
            case COOLDOWN:
                return Debug.DebugGameState.cooldown;
            case MINERALS:
                return Debug.DebugGameState.minerals;
            case SHOW_MAP:
                return Debug.DebugGameState.show_map;
            case TECH_TREE:
                return Debug.DebugGameState.tech_tree;
            case FAST_BUILD:
                return Debug.DebugGameState.fast_build;
            case ALL_RESOURCES:
                return Debug.DebugGameState.all_resources;
            case CONTROL_ENEMY:
                return Debug.DebugGameState.control_enemy;
            default:
                throw new AssertionError("unknown game state: " + this);
        }
    }
}
