package com.github.ocraft.s2client.protocol.debug;

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

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugEndGameBuilder;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugEndGameSyntax;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class DebugEndGame implements Sc2ApiSerializable<Debug.DebugEndGame> {

    private static final long serialVersionUID = 7303006997923985004L;

    private final EndResult result;

    public enum EndResult implements Sc2ApiSerializable<Debug.DebugEndGame.EndResult> {
        SURRENDER,
        DECLARE_VICTORY;

        @Override
        public Debug.DebugEndGame.EndResult toSc2Api() {
            switch (this) {
                case SURRENDER:
                    return Debug.DebugEndGame.EndResult.Surrender;
                case DECLARE_VICTORY:
                    return Debug.DebugEndGame.EndResult.DeclareVictory;
                default:
                    throw new AssertionError("unknown end result: " + this);
            }
        }
    }

    public static final class Builder implements DebugEndGameSyntax, DebugEndGameBuilder {

        private EndResult result;

        @Override
        public DebugEndGameBuilder withResult(EndResult result) {
            this.result = result;
            return this;
        }

        @Override
        public DebugEndGame build() {
            require("result", result);
            return new DebugEndGame(this);
        }

    }

    private DebugEndGame(Builder builder) {
        result = builder.result;
    }

    public static DebugEndGameSyntax endGame() {
        return new Builder();
    }

    @Override
    public Debug.DebugEndGame toSc2Api() {
        return Debug.DebugEndGame.newBuilder().setEndResult(result.toSc2Api()).build();
    }

    public EndResult getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugEndGame that = (DebugEndGame) o;

        return result == that.result;
    }

    @Override
    public int hashCode() {
        return result.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
