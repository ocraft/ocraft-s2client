package com.github.ocraft.s2client.protocol.spatial;

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
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.request.MinimapResolutionSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.MinimapSpatialSetupSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.ResolutionSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.SpatialCameraSetupSyntax;

import java.util.Objects;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.*;

public final class SpatialCameraSetup implements Sc2ApiSerializable<Sc2Api.SpatialCameraSetup> {

    private static final long serialVersionUID = -918846729088958249L;

    private static final double MIN_WIDTH = 0.0;

    private final Float width;
    private final Size2dI map;
    private final Size2dI minimap;
    private final Boolean cropToPlayableArea;
    private final Boolean allowCheatingLayers;

    public static final class Builder implements SpatialCameraSetupSyntax, ResolutionSyntax, MinimapResolutionSyntax,
            MinimapSpatialSetupSyntax, BuilderSyntax<SpatialCameraSetup> {

        private Float width;
        private Size2dI map;
        private Size2dI minimap;
        private Boolean cropToPlayableArea;
        private Boolean allowCheatingLayers;

        @Override
        public ResolutionSyntax width(float width) {
            this.width = width;
            return this;
        }

        @Override
        public MinimapResolutionSyntax resolution(int x, int y) {
            this.map = Size2dI.of(x, y);
            return this;
        }

        @Override
        public MinimapResolutionSyntax resolution(Size2dI map) {
            this.map = map;
            return this;
        }

        @Override
        public MinimapSpatialSetupSyntax minimap(int x, int y) {
            this.minimap = Size2dI.of(x, y);
            return this;
        }

        @Override
        public MinimapSpatialSetupSyntax minimap(Size2dI minimap) {
            this.minimap = minimap;
            return this;
        }

        @Override
        public MinimapSpatialSetupSyntax cropToPlayableArea(Boolean value) {
            this.cropToPlayableArea = value;
            return this;
        }

        @Override
        public MinimapSpatialSetupSyntax allowCheatingLayers(Boolean value) {
            this.allowCheatingLayers = value;
            return this;
        }

        @Override
        public SpatialCameraSetup build() {
            require("resolution", map);
            require("minimap", minimap);
            if (isSet(width)) {
                greaterThan("camera width", width, MIN_WIDTH);
            }
            return new SpatialCameraSetup(this);
        }
    }

    public static SpatialCameraSetupSyntax spatialSetup() {
        return new Builder();
    }

    private SpatialCameraSetup(Builder builder) {
        this.width = builder.width;
        this.map = builder.map;
        this.minimap = builder.minimap;
        this.cropToPlayableArea = builder.cropToPlayableArea;
        this.allowCheatingLayers = builder.allowCheatingLayers;
    }

    private SpatialCameraSetup(Sc2Api.SpatialCameraSetup sc2ApiSpatialCameraSetup) {
        width = tryGet(Sc2Api.SpatialCameraSetup::getWidth, Sc2Api.SpatialCameraSetup::hasWidth)
                .apply(sc2ApiSpatialCameraSetup).orElse(nothing());

        map = tryGet(Sc2Api.SpatialCameraSetup::getResolution, Sc2Api.SpatialCameraSetup::hasResolution)
                .apply(sc2ApiSpatialCameraSetup).map(Size2dI::from).orElseThrow(required("map resolution"));

        minimap = tryGet(
                Sc2Api.SpatialCameraSetup::getMinimapResolution, Sc2Api.SpatialCameraSetup::hasMinimapResolution
        ).apply(sc2ApiSpatialCameraSetup).map(Size2dI::from).orElseThrow(required("minimap resolution"));

        cropToPlayableArea = tryGet(
                Sc2Api.SpatialCameraSetup::getCropToPlayableArea, Sc2Api.SpatialCameraSetup::hasCropToPlayableArea
        ).apply(sc2ApiSpatialCameraSetup).orElse(nothing());

        allowCheatingLayers = tryGet(
                Sc2Api.SpatialCameraSetup::getAllowCheatingLayers, Sc2Api.SpatialCameraSetup::hasAllowCheatingLayers
        ).apply(sc2ApiSpatialCameraSetup).orElse(nothing());
    }

    public static SpatialCameraSetup from(Sc2Api.SpatialCameraSetup sc2ApiSpatialCameraSetup) {
        require("sc2api spatial camera setup", sc2ApiSpatialCameraSetup);
        return new SpatialCameraSetup(sc2ApiSpatialCameraSetup);
    }

    @Override
    public Sc2Api.SpatialCameraSetup toSc2Api() {
        Sc2Api.SpatialCameraSetup.Builder aCameraSetup = Sc2Api.SpatialCameraSetup.newBuilder()
                .setResolution(map.toSc2Api())
                .setMinimapResolution(minimap.toSc2Api());

        getWidth().ifPresent(aCameraSetup::setWidth);
        getCropToPlayableArea().ifPresent(aCameraSetup::setCropToPlayableArea);
        getAllowCheatingLayers().ifPresent(aCameraSetup::setAllowCheatingLayers);

        return aCameraSetup.build();
    }

    public Optional<Float> getWidth() {
        return Optional.ofNullable(width);
    }

    public Size2dI getMap() {
        return map;
    }

    public Size2dI getMinimap() {
        return minimap;
    }

    public Optional<Boolean> getCropToPlayableArea() {
        return Optional.ofNullable(cropToPlayableArea);
    }

    public Optional<Boolean> getAllowCheatingLayers() {
        return Optional.ofNullable(allowCheatingLayers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpatialCameraSetup that = (SpatialCameraSetup) o;

        if (!Objects.equals(width, that.width)) return false;
        if (!map.equals(that.map)) return false;
        if (!minimap.equals(that.minimap)) return false;
        if (!Objects.equals(cropToPlayableArea, that.cropToPlayableArea))
            return false;
        return Objects.equals(allowCheatingLayers, that.allowCheatingLayers);
    }

    @Override
    public int hashCode() {
        int result = width != null ? width.hashCode() : 0;
        result = 31 * result + map.hashCode();
        result = 31 * result + minimap.hashCode();
        result = 31 * result + (cropToPlayableArea != null ? cropToPlayableArea.hashCode() : 0);
        result = 31 * result + (allowCheatingLayers != null ? allowCheatingLayers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
