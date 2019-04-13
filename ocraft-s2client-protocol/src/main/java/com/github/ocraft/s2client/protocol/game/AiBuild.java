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

public enum AiBuild implements Sc2ApiSerializable<Sc2Api.AIBuild> {
    RANDOM_BUILD,
    RUSH,
    TIMING,
    POWER,
    MACRO,
    AIR;

    public static AiBuild from(Sc2Api.AIBuild sc2ApiAiBuild) {
        require("sc2api ai build", sc2ApiAiBuild);
        switch (sc2ApiAiBuild) {
            case RandomBuild:
                return RANDOM_BUILD;
            case Rush:
                return RUSH;
            case Timing:
                return TIMING;
            case Power:
                return POWER;
            case Macro:
                return MACRO;
            case Air:
                return AIR;
            default:
                throw new AssertionError("unknown sc2api ai build: " + sc2ApiAiBuild);
        }
    }

    @Override
    public Sc2Api.AIBuild toSc2Api() {
        switch (this) {
            case RANDOM_BUILD:
                return Sc2Api.AIBuild.RandomBuild;
            case RUSH:
                return Sc2Api.AIBuild.Rush;
            case TIMING:
                return Sc2Api.AIBuild.Timing;
            case POWER:
                return Sc2Api.AIBuild.Power;
            case MACRO:
                return Sc2Api.AIBuild.Macro;
            case AIR:
                return Sc2Api.AIBuild.Air;
            default:
                throw new AssertionError("unknown ai build: " + this);
        }
    }
}
