package com.github.ocraft.s2client.protocol.response;

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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.PlayerInfo;
import com.github.ocraft.s2client.protocol.game.raw.StartRaw;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.spatial.PointI;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public final class ResponseGameInfo extends Response {

    private static final long serialVersionUID = -137398200343824705L;

    private final String mapName;
    private final Set<String> modNames;
    private final LocalMap localMap;
    private final Set<PlayerInfo> playersInfo;
    private final StartRaw startRaw; // Populated if Raw interface is enabled.
    private final InterfaceOptions interfaceOptions;

    private ResponseGameInfo(Sc2Api.ResponseGameInfo sc2ApiResponseGameInfo, Sc2Api.Status status) {
        super(ResponseType.GAME_INFO, GameStatus.from(status));

        mapName = tryGet(Sc2Api.ResponseGameInfo::getMapName, Sc2Api.ResponseGameInfo::hasMapName)
                .apply(sc2ApiResponseGameInfo).orElseThrow(required("map name"));

        modNames = new HashSet<>(sc2ApiResponseGameInfo.getModNamesList());

        localMap = tryGet(Sc2Api.ResponseGameInfo::getLocalMapPath, Sc2Api.ResponseGameInfo::hasLocalMapPath)
                .apply(sc2ApiResponseGameInfo).map(Paths::get).map(LocalMap::of).orElse(nothing());

        playersInfo = sc2ApiResponseGameInfo.getPlayerInfoList().stream().map(PlayerInfo::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));

        requireNotEmpty("players info", playersInfo);

        startRaw = tryGet(Sc2Api.ResponseGameInfo::getStartRaw, Sc2Api.ResponseGameInfo::hasStartRaw)
                .apply(sc2ApiResponseGameInfo).map(StartRaw::from).orElse(nothing());

        interfaceOptions = tryGet(Sc2Api.ResponseGameInfo::getOptions, Sc2Api.ResponseGameInfo::hasOptions)
                .apply(sc2ApiResponseGameInfo).map(InterfaceOptions::from).orElseThrow(required("interface options"));
    }

    public static ResponseGameInfo from(Sc2Api.Response sc2ApiResponse) {
        if (!hasGameInfoResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have game info response");
        }
        return new ResponseGameInfo(sc2ApiResponse.getGameInfo(), sc2ApiResponse.getStatus());
    }

    private static boolean hasGameInfoResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasGameInfo();
    }

    public String getMapName() {
        return mapName;
    }

    public Set<String> getModNames() {
        return modNames;
    }

    public Optional<LocalMap> getLocalMap() {
        return Optional.ofNullable(localMap);
    }

    public Set<PlayerInfo> getPlayersInfo() {
        return playersInfo;
    }

    public Optional<StartRaw> getStartRaw() {
        return Optional.ofNullable(startRaw);
    }

    public InterfaceOptions getInterfaceOptions() {
        return interfaceOptions;
    }

    public PointI convertWorldToMinimap(Point2d world) {
        return getInterfaceOptions()
                .getFeatureLayer()
                .filter(spatialCameraSetup -> getStartRaw().isPresent())
                .map(spatialCameraSetup -> {
                    int imageWidth = spatialCameraSetup.getMinimap().getX();
                    int imageHeight = spatialCameraSetup.getMinimap().getY();
                    float mapWidth = (float) getStartRaw().get().getMapSize().getX();
                    float mapHeight = (float) getStartRaw().get().getMapSize().getY();

                    // Pixels always cover a square amount of world space. The scale is determined
                    // by the largest axis of the map.
                    float pixelSize = Math.max(mapWidth / imageWidth, mapHeight / imageHeight);

                    // Origin of world space is bottom left. Origin of image space is top left.
                    // Upper left corner of the map corresponds to the upper left corner of the upper
                    // left pixel of the feature layer.
                    float imageOriginX = 0;
                    float imageOriginY = mapHeight;
                    float imageRelativeX = world.getX() - imageOriginX;
                    float imageRelativeY = imageOriginY - world.getY();

                    int imageX = (int) (imageRelativeX / pixelSize);
                    int imageY = (int) (imageRelativeY / pixelSize);

                    return PointI.of(imageX, imageY);
                }).orElseThrow(() -> new IllegalStateException("Feature layer interface is required."));
    }

    public PointI convertWorldToCamera(Point2d cameraWorld, Point2d world) {
        return getInterfaceOptions()
                .getFeatureLayer()
                .filter(spatialCameraSetup -> getStartRaw().isPresent())
                .filter(spatialCameraSetup -> spatialCameraSetup.getWidth().isPresent())
                .map(spatialCameraSetup -> {
                    float cameraSize = spatialCameraSetup.getWidth().get();
                    int imageWidth = spatialCameraSetup.getMap().getX();
                    int imageHeight = spatialCameraSetup.getMap().getY();

                    // Pixels always cover a square amount of world space. The scale is determined
                    // by making the shortest axis of the camera match the requested cameraSize.
                    float pixelSize = cameraSize / Math.min(imageWidth, imageHeight);
                    float imageWidthWorld = pixelSize * imageWidth;
                    float imageHeightWorld = pixelSize * imageHeight;

                    // Origin of world space is bottom left. Origin of image space is top left.
                    // The feature layer is centered around the camera target position.
                    float imageOriginX = cameraWorld.getX() - imageWidthWorld / 2.0f;
                    float imageOriginY = cameraWorld.getY() + imageHeightWorld / 2.0f;
                    float imageRelativeX = world.getX() - imageOriginX;
                    float imageRelativeY = imageOriginY - world.getY();

                    int imageX = (int) (imageRelativeX / pixelSize);
                    int imageY = (int) (imageRelativeY / pixelSize);

                    return PointI.of(imageX, imageY);
                }).orElseThrow(() -> new IllegalStateException("Feature layer interface is required."));
    }

    public Point2d findRandomLocation() {
        return getStartRaw().map(start -> {
            PointI playableMin = start.getPlayableArea().getP0();
            PointI playableMax = start.getPlayableArea().getP1();
            return findRandomLocation(playableMin.toPoint2d(), playableMax.toPoint2d());
        }).orElseGet(() -> Point2d.of(0.0f, 0.0f));
    }

    public Point2d findRandomLocation(Point2d min, Point2d max) {
        return Point2d.of(
                (float) ((max.getX() - min.getX()) * Math.random() + min.getX()),
                (float) ((max.getY() - min.getY()) * Math.random() + min.getY()));
    }

    public PointI findCenterOfMap() {
        return getStartRaw().map(start -> start.getPlayableArea().getP1().div(2)).orElseGet(() -> PointI.of(0, 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseGameInfo)) return false;
        if (!super.equals(o)) return false;

        ResponseGameInfo that = (ResponseGameInfo) o;

        return that.canEqual(this) &&
                mapName.equals(that.mapName) &&
                modNames.equals(that.modNames) &&
                (localMap != null ? localMap.equals(that.localMap) : that.localMap == null) &&
                playersInfo.equals(that.playersInfo) &&
                (startRaw != null ? startRaw.equals(that.startRaw) : that.startRaw == null) &&
                interfaceOptions.equals(that.interfaceOptions);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseGameInfo;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mapName.hashCode();
        result = 31 * result + modNames.hashCode();
        result = 31 * result + (localMap != null ? localMap.hashCode() : 0);
        result = 31 * result + playersInfo.hashCode();
        result = 31 * result + (startRaw != null ? startRaw.hashCode() : 0);
        result = 31 * result + interfaceOptions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
