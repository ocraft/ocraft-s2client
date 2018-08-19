package com.github.ocraft.s2client.bot.gateway;

import java.util.ArrayList;
import java.util.List;

/**
 * Some nice parameters that generally work but may require tuning for certain maps.
 */
public final class ExpansionParameters {
    /**
     * The various radius to check at from the center of an expansion.
     */
    private final List<Float> radiuses = new ArrayList<>();

    /**
     * With what granularity to step the circumference of the circle.
     */
    private final float circleStepSize;

    /**
     * With what distance to cluster mineral/vespene in, this will be used for center of mass calulcation.
     */
    private final float clusterDistance;

    private ExpansionParameters(List<Float> radiuses, float circleStepSize, float clusterDistance) {
        this.radiuses.addAll(radiuses);
        this.circleStepSize = circleStepSize;
        this.clusterDistance = clusterDistance;
    }

    public static ExpansionParameters from(List<Float> radiuses, float circleStepSize, float clusterDistance) {
        return new ExpansionParameters(radiuses, circleStepSize, clusterDistance);
    }

    public List<Float> getRadiuses() {
        return new ArrayList<>(radiuses);
    }

    public float getCircleStepSize() {
        return circleStepSize;
    }

    public float getClusterDistance() {
        return clusterDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpansionParameters that = (ExpansionParameters) o;

        if (Float.compare(that.circleStepSize, circleStepSize) != 0) return false;
        if (Float.compare(that.clusterDistance, clusterDistance) != 0) return false;
        return radiuses.equals(that.radiuses);
    }

    @Override
    public int hashCode() {
        int result = radiuses.hashCode();
        result = 31 * result + (circleStepSize != +0.0f ? Float.floatToIntBits(circleStepSize) : 0);
        result = 31 * result + (clusterDistance != +0.0f ? Float.floatToIntBits(clusterDistance) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExpansionParameters{" +
                "radiuses=" + radiuses +
                ", circleStepSize=" + circleStepSize +
                ", clusterDistance=" + clusterDistance +
                '}';
    }
}
