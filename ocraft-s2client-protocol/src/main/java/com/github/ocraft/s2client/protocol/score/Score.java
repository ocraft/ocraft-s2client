package com.github.ocraft.s2client.protocol.score;

import SC2APIProtocol.ScoreOuterClass;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class Score implements Serializable {

    private static final long serialVersionUID = 2576957697484636638L;

    private final Type type;
    private final int score;
    private final ScoreDetails details;

    public enum Type {
        CURRICULUM, // map generated score (from curriculum maps with special scoring)
        MELEE;      // summation of in-progress and current units/buildings value + minerals + vespene

        public static Type from(ScoreOuterClass.Score.ScoreType sc2ApiScoreType) {
            require("sc2api score type", sc2ApiScoreType);
            switch (sc2ApiScoreType) {
                case Curriculum:
                    return CURRICULUM;
                case Melee:
                    return MELEE;
                default:
                    throw new AssertionError("unknown sc2api score type: " + sc2ApiScoreType);
            }
        }
    }

    private Score(ScoreOuterClass.Score sc2ApiScore) {
        type = tryGet(
                ScoreOuterClass.Score::getScoreType, ScoreOuterClass.Score::hasScoreType
        ).apply(sc2ApiScore).map(Type::from).orElseThrow(required("type"));

        score = tryGet(
                ScoreOuterClass.Score::getScore, ScoreOuterClass.Score::hasScore
        ).apply(sc2ApiScore).orElseThrow(required("score"));

        details = tryGet(
                ScoreOuterClass.Score::getScoreDetails, ScoreOuterClass.Score::hasScoreDetails
        ).apply(sc2ApiScore).map(ScoreDetails::from).orElseThrow(required("details"));
    }

    public static Score from(ScoreOuterClass.Score sc2ApiScore) {
        require("sc2api score", sc2ApiScore);
        return new Score(sc2ApiScore);
    }

    public Type getType() {
        return type;
    }

    public int getScore() {
        return score;
    }

    public ScoreDetails getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score1 = (Score) o;

        return score == score1.score && type == score1.type && details.equals(score1.details);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + score;
        result = 31 * result + details.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
