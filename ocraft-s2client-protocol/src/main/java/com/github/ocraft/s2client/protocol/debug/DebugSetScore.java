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
import com.github.ocraft.s2client.protocol.syntax.debug.DebugSetScoreBuilder;
import com.github.ocraft.s2client.protocol.syntax.debug.DebugSetScoreSyntax;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class DebugSetScore implements Sc2ApiSerializable<Debug.DebugSetScore> {

    private static final long serialVersionUID = 399658033918031081L;

    private final float score;

    public static final class Builder implements DebugSetScoreSyntax, DebugSetScoreBuilder {

        private Float score;

        @Override
        public DebugSetScoreBuilder to(float score) {
            this.score = score;
            return this;
        }

        @Override
        public DebugSetScore build() {
            require("score", score);
            return new DebugSetScore(this);
        }

    }

    private DebugSetScore(Builder builder) {
        score = builder.score;
    }

    public static DebugSetScoreSyntax setScore() {
        return new Builder();
    }

    @Override
    public Debug.DebugSetScore toSc2Api() {
        return Debug.DebugSetScore.newBuilder().setScore(score).build();
    }

    public float getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugSetScore that = (DebugSetScore) o;

        return Float.compare(that.score, score) == 0;
    }

    @Override
    public int hashCode() {
        return (score != +0.0f ? Float.floatToIntBits(score) : 0);
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
