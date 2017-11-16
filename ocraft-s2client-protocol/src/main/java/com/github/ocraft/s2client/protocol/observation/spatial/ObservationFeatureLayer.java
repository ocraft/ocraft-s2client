/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.observation.spatial;

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ObservationFeatureLayer implements Serializable {

    private static final long serialVersionUID = -3259507037284735217L;

    private final FeatureLayers renders;
    private final FeatureLayersMinimap minimapRenders;

    private ObservationFeatureLayer(Spatial.ObservationFeatureLayer sc2ApiObservationFeatureLayer) {
        renders = tryGet(
                Spatial.ObservationFeatureLayer::getRenders, Spatial.ObservationFeatureLayer::hasRenders
        ).apply(sc2ApiObservationFeatureLayer).map(FeatureLayers::from).orElseThrow(required("renders"));

        minimapRenders = tryGet(
                Spatial.ObservationFeatureLayer::getMinimapRenders, Spatial.ObservationFeatureLayer::hasMinimapRenders
        ).apply(sc2ApiObservationFeatureLayer).map(FeatureLayersMinimap::from).orElseThrow(required("minimap renders"));
    }

    public static ObservationFeatureLayer from(Spatial.ObservationFeatureLayer sc2ApiObservationFeatureLayer) {
        require("sc2api observation feature layer", sc2ApiObservationFeatureLayer);
        return new ObservationFeatureLayer(sc2ApiObservationFeatureLayer);
    }

    public FeatureLayers getRenders() {
        return renders;
    }

    public FeatureLayersMinimap getMinimapRenders() {
        return minimapRenders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObservationFeatureLayer that = (ObservationFeatureLayer) o;

        return renders.equals(that.renders) && minimapRenders.equals(that.minimapRenders);
    }

    @Override
    public int hashCode() {
        int result = renders.hashCode();
        result = 31 * result + minimapRenders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
