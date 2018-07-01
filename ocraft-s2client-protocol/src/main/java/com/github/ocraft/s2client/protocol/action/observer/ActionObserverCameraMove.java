package com.github.ocraft.s2client.protocol.action.observer;

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
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraMoveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraMoveSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.observer.WithDistanceSyntax;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

// since 4.0
public final class ActionObserverCameraMove implements Sc2ApiSerializable<Sc2Api.ActionObserverCameraMove> {

    private static final long serialVersionUID = 4700467945394062160L;

    private static final float DEFAULT_CAMERA_DISTANCE = 0;

    private final Point2d position;
    private final float distance;

    public static final class Builder implements ActionObserverCameraMoveSyntax, WithDistanceSyntax {

        private Point2d position;
        private float distance = DEFAULT_CAMERA_DISTANCE;

        @Override
        public WithDistanceSyntax to(Point2d point2d) {
            this.position = point2d;
            return this;
        }

        @Override
        public ActionObserverCameraMoveBuilder withDistance(float distance) {
            this.distance = distance;
            return this;
        }

        @Override
        public ActionObserverCameraMove build() {
            require("position", position);
            return new ActionObserverCameraMove(this);
        }
    }

    private ActionObserverCameraMove(Builder builder) {
        position = builder.position;
        distance = builder.distance;
    }

    public static ActionObserverCameraMoveSyntax cameraMove() {
        return new Builder();
    }

    @Override
    public Sc2Api.ActionObserverCameraMove toSc2Api() {
        return Sc2Api.ActionObserverCameraMove.newBuilder()
                .setWorldPos(position.toSc2Api())
                .setDistance(distance)
                .build();
    }

    public Point2d getPosition() {
        return position;
    }

    public float getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionObserverCameraMove that = (ActionObserverCameraMove) o;

        return Float.compare(that.distance, distance) == 0 && position.equals(that.position);
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + (distance != +0.0f ? Float.floatToIntBits(distance) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
