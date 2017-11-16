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
package com.github.ocraft.s2client.protocol.unit;

import SC2APIProtocol.Raw;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum Alliance {
    SELF,
    ALLY,
    ENEMY,
    NEUTRAL;

    public static Alliance from(Raw.Alliance sc2ApiAlliance) {
        require("sc2api alliance", sc2ApiAlliance);
        switch (sc2ApiAlliance) {
            case Self:
                return SELF;
            case Ally:
                return ALLY;
            case Enemy:
                return ENEMY;
            case Neutral:
                return NEUTRAL;
            default:
                throw new AssertionError("unknown sc2api alliance: " + sc2ApiAlliance);
        }
    }

    public static Alliance from(int sc2ApiPlayerRelative) {
        switch (sc2ApiPlayerRelative) {
            case 1:
                return SELF;
            case 2:
                return ALLY;
            case 3:
                return NEUTRAL;
            case 4:
                return ENEMY;
            default:
                throw new AssertionError("unknown sc2api alliance: " + sc2ApiPlayerRelative);
        }
    }
}
