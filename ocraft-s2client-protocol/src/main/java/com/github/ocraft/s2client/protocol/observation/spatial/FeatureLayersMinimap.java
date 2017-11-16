package com.github.ocraft.s2client.protocol.observation.spatial;

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class FeatureLayersMinimap implements Serializable {

    private static final long serialVersionUID = 5024615413306860431L;

    private final ImageData heightMap;      // uint8. Terrain height. World space units of [-200, 200] encoded into [0, 255].
    private final ImageData visibilityMap;  // uint8. 0=Hidden, 1=Fogged, 2=Visible, 3=FullHidden
    private final ImageData creep;          // 1-bit. Zerg creep.
    private final ImageData camera;         // 1-bit. Area covered by the camera.
    private final ImageData playerId;       // uint8. Participants: [1, 15] Neutral: 16
    private final ImageData playerRelative; // uint8. See "Alliance" enum in raw.proto. Range: [1, 4]
    private final ImageData selected;       // 1-bit. Selected units.

    // Cheat layers. Only populated in replays.
    private final ImageData unitType;       // int32. Unique identifier for type of unit.

    private FeatureLayersMinimap(Spatial.FeatureLayersMinimap sc2ApiFeatureLayersMinimap) {
        heightMap = tryGet(Spatial.FeatureLayersMinimap::getHeightMap, Spatial.FeatureLayersMinimap::hasHeightMap)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("height map"));

        visibilityMap = tryGet(
                Spatial.FeatureLayersMinimap::getVisibilityMap, Spatial.FeatureLayersMinimap::hasVisibilityMap
        ).apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("visibility map"));

        creep = tryGet(Spatial.FeatureLayersMinimap::getCreep, Spatial.FeatureLayersMinimap::hasCreep)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("creep"));

        camera = tryGet(Spatial.FeatureLayersMinimap::getCamera, Spatial.FeatureLayersMinimap::hasCamera)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("camera"));

        playerId = tryGet(Spatial.FeatureLayersMinimap::getPlayerId, Spatial.FeatureLayersMinimap::hasPlayerId)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("player id"));

        unitType = tryGet(Spatial.FeatureLayersMinimap::getUnitType, Spatial.FeatureLayersMinimap::hasUnitType)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElse(nothing());

        selected = tryGet(Spatial.FeatureLayersMinimap::getSelected, Spatial.FeatureLayersMinimap::hasSelected)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("selected"));

        playerRelative = tryGet(
                Spatial.FeatureLayersMinimap::getPlayerRelative, Spatial.FeatureLayersMinimap::hasPlayerRelative
        ).apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("player relative"));
    }

    public static FeatureLayersMinimap from(Spatial.FeatureLayersMinimap sc2ApiFeatureLayersMinimap) {
        require("sc2api feature layers minimap", sc2ApiFeatureLayersMinimap);
        return new FeatureLayersMinimap(sc2ApiFeatureLayersMinimap);
    }

    public ImageData getHeightMap() {
        return heightMap;
    }

    public ImageData getVisibilityMap() {
        return visibilityMap;
    }

    public ImageData getCreep() {
        return creep;
    }

    public ImageData getCamera() {
        return camera;
    }

    public ImageData getPlayerId() {
        return playerId;
    }

    public ImageData getPlayerRelative() {
        return playerRelative;
    }

    public ImageData getSelected() {
        return selected;
    }

    public Optional<ImageData> getUnitType() {
        return Optional.ofNullable(unitType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureLayersMinimap that = (FeatureLayersMinimap) o;

        return heightMap.equals(that.heightMap) &&
                visibilityMap.equals(that.visibilityMap) &&
                creep.equals(that.creep) &&
                camera.equals(that.camera) &&
                playerId.equals(that.playerId) &&
                playerRelative.equals(that.playerRelative) &&
                selected.equals(that.selected) &&
                (unitType != null ? unitType.equals(that.unitType) : that.unitType == null);
    }

    @Override
    public int hashCode() {
        int result = heightMap.hashCode();
        result = 31 * result + visibilityMap.hashCode();
        result = 31 * result + creep.hashCode();
        result = 31 * result + camera.hashCode();
        result = 31 * result + playerId.hashCode();
        result = 31 * result + playerRelative.hashCode();
        result = 31 * result + selected.hashCode();
        result = 31 * result + (unitType != null ? unitType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
