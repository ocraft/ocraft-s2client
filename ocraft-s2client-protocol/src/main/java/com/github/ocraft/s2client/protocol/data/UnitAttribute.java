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
package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum UnitAttribute {
    LIGHT,
    ARMORED,
    BIOLOGICAL,
    MECHANICAL,
    ROBOTIC,
    PSIONIC,
    MASSIVE,
    STRUCTURE,
    HOVER,
    HEROIC,
    SUMMONED;

    public static UnitAttribute from(Data.Attribute sc2ApiAttribute) {
        require("sc2api attribute", sc2ApiAttribute);
        switch (sc2ApiAttribute) {
            case Hover:
                return HOVER;
            case Light:
                return LIGHT;
            case Heroic:
                return HEROIC;
            case Armored:
                return ARMORED;
            case Massive:
                return MASSIVE;
            case Psionic:
                return PSIONIC;
            case Robotic:
                return ROBOTIC;
            case Summoned:
                return SUMMONED;
            case Structure:
                return STRUCTURE;
            case Biological:
                return BIOLOGICAL;
            case Mechanical:
                return MECHANICAL;
            default:
                throw new AssertionError("unknown sc2api attribute: " + sc2ApiAttribute);

        }
    }
}
