package com.github.ocraft.s2client.protocol.debug;

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
