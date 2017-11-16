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
