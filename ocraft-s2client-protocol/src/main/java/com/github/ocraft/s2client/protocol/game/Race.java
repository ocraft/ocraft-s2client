package com.github.ocraft.s2client.protocol.game;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
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
 * #L%
 */

import SC2APIProtocol.Common;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum Race implements Sc2ApiSerializable<Common.Race> {
    NO_RACE, TERRAN, ZERG, PROTOSS, RANDOM;

    @Override
    public Common.Race toSc2Api() {
        switch (this) {
            case NO_RACE:
                return Common.Race.NoRace;
            case TERRAN:
                return Common.Race.Terran;
            case ZERG:
                return Common.Race.Zerg;
            case PROTOSS:
                return Common.Race.Protoss;
            case RANDOM:
                return Common.Race.Random;
            default:
                throw new AssertionError("unknown race: " + this);
        }
    }

    public static Race from(Common.Race sc2ApiRace) {
        require("sc2api race", sc2ApiRace);
        switch (sc2ApiRace) {
            case NoRace:
                return NO_RACE;
            case Zerg:
                return ZERG;
            case Protoss:
                return PROTOSS;
            case Terran:
                return TERRAN;
            case Random:
                return RANDOM;
            default:
                throw new AssertionError("unknown sc2api race: " + sc2ApiRace);
        }
    }
}
