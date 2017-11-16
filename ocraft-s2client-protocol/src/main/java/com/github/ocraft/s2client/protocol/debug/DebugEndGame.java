package com.github.ocraft.s2client.protocol.debug;

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
