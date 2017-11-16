/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
