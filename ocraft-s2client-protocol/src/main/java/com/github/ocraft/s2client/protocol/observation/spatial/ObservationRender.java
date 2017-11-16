package com.github.ocraft.s2client.protocol.observation.spatial;

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ObservationRender implements Serializable {

    private static final long serialVersionUID = -3164970033791841768L;

    private final ImageData map;
    private final ImageData minimap;

    private ObservationRender(Spatial.ObservationRender sc2ApiObservationRender) {
        map = tryGet(Spatial.ObservationRender::getMap, Spatial.ObservationRender::hasMap)
                .apply(sc2ApiObservationRender).map(ImageData::from).orElseThrow(required("map"));

        minimap = tryGet(Spatial.ObservationRender::getMinimap, Spatial.ObservationRender::hasMinimap)
                .apply(sc2ApiObservationRender).map(ImageData::from).orElseThrow(required("minimap"));
    }

    public static ObservationRender from(Spatial.ObservationRender sc2ApiObservationRender) {
        require("sc2api observation render", sc2ApiObservationRender);
        return new ObservationRender(sc2ApiObservationRender);
    }

    public ImageData getMap() {
        return map;
    }

    public ImageData getMinimap() {
        return minimap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObservationRender that = (ObservationRender) o;

        return map.equals(that.map) && minimap.equals(that.minimap);
    }

    @Override
    public int hashCode() {
        int result = map.hashCode();
        result = 31 * result + minimap.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
