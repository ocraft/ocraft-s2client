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
