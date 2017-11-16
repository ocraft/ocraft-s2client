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
package com.github.ocraft.s2client.protocol.action.spatial;

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialCameraMoveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialCameraMoveSyntax;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionSpatialCameraMove implements Sc2ApiSerializable<Spatial.ActionSpatialCameraMove> {

    private static final long serialVersionUID = -8289717003624241498L;

    private final PointI centerInMinimap;

    public static final class Builder implements ActionSpatialCameraMoveBuilder, ActionSpatialCameraMoveSyntax {

        private PointI centerInMinimap;

        @Override
        public ActionSpatialCameraMoveBuilder to(PointI pointOnMinimap) {
            centerInMinimap = pointOnMinimap;
            return this;
        }

        @Override
        public ActionSpatialCameraMove build() {
            require("center in minimap", centerInMinimap);
            return new ActionSpatialCameraMove(this);
        }
    }

    private ActionSpatialCameraMove(Builder builder) {
        centerInMinimap = builder.centerInMinimap;
    }

    private ActionSpatialCameraMove(Spatial.ActionSpatialCameraMove sc2ApiActionSpatialCameraMove) {
        centerInMinimap = tryGet(
                Spatial.ActionSpatialCameraMove::getCenterMinimap, Spatial.ActionSpatialCameraMove::hasCenterMinimap
        ).apply(sc2ApiActionSpatialCameraMove).map(PointI::from).orElseThrow(required("center in minimap"));
    }

    public static Builder cameraMove() {
        return new Builder();
    }

    public static ActionSpatialCameraMove from(Spatial.ActionSpatialCameraMove sc2ApiActionSpatialCameraMove) {
        require("sc2api action spatial camera move", sc2ApiActionSpatialCameraMove);
        return new ActionSpatialCameraMove(sc2ApiActionSpatialCameraMove);
    }

    @Override
    public Spatial.ActionSpatialCameraMove toSc2Api() {
        return Spatial.ActionSpatialCameraMove.newBuilder().setCenterMinimap(centerInMinimap.toSc2Api()).build();
    }

    public PointI getCenterInMinimap() {
        return centerInMinimap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionSpatialCameraMove that = (ActionSpatialCameraMove) o;

        return centerInMinimap.equals(that.centerInMinimap);
    }

    @Override
    public int hashCode() {
        return centerInMinimap.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
