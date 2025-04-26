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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum Difficulty implements Sc2ApiSerializable<Sc2Api.Difficulty> {
    VERY_EASY,
    EASY,
    MEDIUM,
    HARD,
    HARDER,
    VERY_HARD,
    ELITE,
    CHEAT_VISION,
    CHEAT_MONEY,
    CHEAT_INSANE;

    @Override
    public Sc2Api.Difficulty toSc2Api() {
        switch (this) {
            case VERY_EASY:
                return Sc2Api.Difficulty.VeryEasy;
            case EASY:
                return Sc2Api.Difficulty.Easy;
            case MEDIUM:
                return Sc2Api.Difficulty.Medium;
            case HARD:
                return Sc2Api.Difficulty.Hard;
            case HARDER:
                return Sc2Api.Difficulty.Harder;
            case VERY_HARD:
                return Sc2Api.Difficulty.VeryHard;
            case ELITE:
                return Sc2Api.Difficulty.Elite;
            case CHEAT_VISION:
                return Sc2Api.Difficulty.CheatVision;
            case CHEAT_MONEY:
                return Sc2Api.Difficulty.CheatMoney;
            case CHEAT_INSANE:
                return Sc2Api.Difficulty.CheatInsane;
            default:
                throw new AssertionError("unknown difficulty: " + this);
        }
    }

    public static Difficulty from(Sc2Api.Difficulty sc2ApiDifficulty) {
        require("sc2api difficulty", sc2ApiDifficulty);
        switch (sc2ApiDifficulty) {
            case VeryEasy:
                return VERY_EASY;
            case Easy:
                return EASY;
            case Medium:
                return MEDIUM;
            case Hard:
                return HARD;
            case Harder:
                return HARDER;
            case VeryHard:
                return VERY_HARD;
            case Elite:
                return ELITE;
            case CheatVision:
                return CHEAT_VISION;
            case CheatMoney:
                return CHEAT_MONEY;
            case CheatInsane:
                return CHEAT_INSANE;
            default:
                throw new AssertionError("unknown sc2api difficulty: " + sc2ApiDifficulty);
        }
    }

    public static Difficulty forName(String difficultyName) {
        require("name of difficulty", difficultyName);
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.name().replaceAll("_", "").equalsIgnoreCase(difficultyName)) {
                return difficulty;
            }
        }
        throw new AssertionError("unknown difficulty: " + difficultyName);
    }
}
