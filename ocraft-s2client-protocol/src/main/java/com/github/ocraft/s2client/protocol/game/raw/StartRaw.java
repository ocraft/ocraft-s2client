package com.github.ocraft.s2client.protocol.game.raw;

/*-
 * #%L
 * ocraft-s2client-protocol
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

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.observation.spatial.ImageData;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.spatial.RectangleI;
import com.github.ocraft.s2client.protocol.spatial.Size2dI;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public final class StartRaw implements Serializable {

    private static final long serialVersionUID = -281483860654181069L;

    private final Size2dI mapSize;              /** Width and height of the map. */
    private final ImageData pathingGrid;        /** 1 bit bitmap of the pathing grid. */
    private final ImageData terrainHeight;      /** 1 byte bitmap of the terrain height. */
    private final ImageData placementGrid;      /** 1 bit bitmap of the building placement grid. */
    private final RectangleI playableArea;      /** The playable cells. */
    private final Set<Point2d> startLocations;  /** Possible start locations for enemy players (not self start location). */

    private StartRaw(Raw.StartRaw sc2ApiStartRaw) {
        mapSize = tryGet(Raw.StartRaw::getMapSize, Raw.StartRaw::hasMapSize)
                .apply(sc2ApiStartRaw).map(Size2dI::from).orElseThrow(required("map size"));

        pathingGrid = tryGet(Raw.StartRaw::getPathingGrid, Raw.StartRaw::hasPathingGrid)
                .apply(sc2ApiStartRaw).map(ImageData::from).orElseThrow(required("pathing grid"));

        terrainHeight = tryGet(Raw.StartRaw::getTerrainHeight, Raw.StartRaw::hasTerrainHeight)
                .apply(sc2ApiStartRaw).map(ImageData::from).orElseThrow(required("terrain height"));

        placementGrid = tryGet(Raw.StartRaw::getPlacementGrid, Raw.StartRaw::hasPlacementGrid)
                .apply(sc2ApiStartRaw).map(ImageData::from).orElseThrow(required("placement grid"));

        playableArea = tryGet(Raw.StartRaw::getPlayableArea, Raw.StartRaw::hasPlayableArea)
                .apply(sc2ApiStartRaw).map(RectangleI::from).orElseThrow(required("playable area"));

        startLocations = sc2ApiStartRaw.getStartLocationsList().stream().map(Point2d::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    public static StartRaw from(Raw.StartRaw sc2ApiStartRaw) {
        require("sc2api start raw", sc2ApiStartRaw);
        return new StartRaw(sc2ApiStartRaw);
    }

    public Size2dI getMapSize() {
        return mapSize;
    }

    public ImageData getPathingGrid() {
        return pathingGrid;
    }

    public ImageData getTerrainHeight() {
        return terrainHeight;
    }

    public ImageData getPlacementGrid() {
        return placementGrid;
    }

    public RectangleI getPlayableArea() {
        return playableArea;
    }

    public Set<Point2d> getStartLocations() {
        return startLocations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StartRaw startRaw = (StartRaw) o;

        return mapSize.equals(startRaw.mapSize) &&
                pathingGrid.equals(startRaw.pathingGrid) &&
                terrainHeight.equals(startRaw.terrainHeight) &&
                placementGrid.equals(startRaw.placementGrid) &&
                playableArea.equals(startRaw.playableArea) &&
                startLocations.equals(startRaw.startLocations);
    }

    @Override
    public int hashCode() {
        int result = mapSize.hashCode();
        result = 31 * result + pathingGrid.hashCode();
        result = 31 * result + terrainHeight.hashCode();
        result = 31 * result + placementGrid.hashCode();
        result = 31 * result + playableArea.hashCode();
        result = 31 * result + startLocations.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
