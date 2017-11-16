package com.github.ocraft.s2client.protocol.score;

import SC2APIProtocol.ScoreOuterClass;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class VitalScoreDetails implements Serializable {

    private static final long serialVersionUID = 8030995589662399111L;

    private final float life;
    private final float shields;
    private final float energy;

    private VitalScoreDetails(ScoreOuterClass.VitalScoreDetails sc2ApiVitalScoreDetails) {
        life = tryGet(
                ScoreOuterClass.VitalScoreDetails::getLife, ScoreOuterClass.VitalScoreDetails::hasLife
        ).apply(sc2ApiVitalScoreDetails).orElseThrow(required("life"));

        shields = tryGet(
                ScoreOuterClass.VitalScoreDetails::getShields, ScoreOuterClass.VitalScoreDetails::hasShields
        ).apply(sc2ApiVitalScoreDetails).orElseThrow(required("shields"));

        energy = tryGet(
                ScoreOuterClass.VitalScoreDetails::getEnergy, ScoreOuterClass.VitalScoreDetails::hasEnergy
        ).apply(sc2ApiVitalScoreDetails).orElseThrow(required("energy"));
    }

    public static VitalScoreDetails from(ScoreOuterClass.VitalScoreDetails sc2ApiVitalScoreDetails) {
        require("sc2api vital score details", sc2ApiVitalScoreDetails);
        return new VitalScoreDetails(sc2ApiVitalScoreDetails);
    }

    public float getLife() {
        return life;
    }

    public float getShields() {
        return shields;
    }

    public float getEnergy() {
        return energy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VitalScoreDetails that = (VitalScoreDetails) o;

        return Float.compare(that.life, life) == 0 &&
                Float.compare(that.shields, shields) == 0 &&
                Float.compare(that.energy, energy) == 0;
    }

    @Override
    public int hashCode() {
        int result = (life != +0.0f ? Float.floatToIntBits(life) : 0);
        result = 31 * result + (shields != +0.0f ? Float.floatToIntBits(shields) : 0);
        result = 31 * result + (energy != +0.0f ? Float.floatToIntBits(energy) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
