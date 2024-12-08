package com.github.ocraft.s2client.bot.gateway;

/*-
 * #%L
 * ocraft-s2client-bot
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

import java.util.ArrayList;
import java.util.List;

/**
 * Some nice parameters that generally work but may require tuning for certain maps.
 * @deprecated this class is no longer used in the calculateExpansionLocations() algorithm
 */
@Deprecated
public final class ExpansionParameters {

    private final List<Double> radiuses = new ArrayList<>();

    private final double circleStepSize;

    private final double clusterDistance;

    private ExpansionParameters(List<Double> radiuses, double circleStepSize, double clusterDistance) {
        this.radiuses.addAll(radiuses);
        this.circleStepSize = circleStepSize;
        this.clusterDistance = clusterDistance;
    }

    /**
     * @param radiuses        The various radius to check at from the center of an expansion.
     * @param circleStepSize  With what granularity to step the circumference of the circle.
     * @param clusterDistance With what distance to cluster mineral/vespene in, this will be used for center of mass
     *                        calculation.
     * @return Instance of ExpansionParameters.
     */
    public static ExpansionParameters from(List<Double> radiuses, double circleStepSize, double clusterDistance) {
        return new ExpansionParameters(radiuses, circleStepSize, clusterDistance);
    }

    public static ExpansionParameters preset() {
        return new ExpansionParameters(List.of(6.4, 5.3, 5.1), 0.5, 15.0);
    }

    public List<Double> getRadiuses() {
        return new ArrayList<>(radiuses);
    }

    public double getCircleStepSize() {
        return circleStepSize;
    }

    public double getClusterDistance() {
        return clusterDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpansionParameters that = (ExpansionParameters) o;

        if (Double.compare(that.circleStepSize, circleStepSize) != 0) return false;
        if (Double.compare(that.clusterDistance, clusterDistance) != 0) return false;
        return radiuses.equals(that.radiuses);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = radiuses.hashCode();
        temp = Double.doubleToLongBits(circleStepSize);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(clusterDistance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
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
