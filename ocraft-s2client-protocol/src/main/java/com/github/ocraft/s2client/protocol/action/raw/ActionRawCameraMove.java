package com.github.ocraft.s2client.protocol.action.raw;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawCameraMoveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawCameraMoveSyntax;
import com.github.ocraft.s2client.protocol.unit.Unit;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionRawCameraMove implements Sc2ApiSerializable<Raw.ActionRawCameraMove> {

    private static final long serialVersionUID = -3836968510395318745L;

    private final Point centerInWorldSpace;

    public static final class Builder implements ActionRawCameraMoveBuilder, ActionRawCameraMoveSyntax {

        private Point centerInWorldSpace;

        @Override
        public ActionRawCameraMoveBuilder to(Point point) {
            centerInWorldSpace = point;
            return this;
        }

        @Override
        public ActionRawCameraMoveBuilder to(Unit unit) {
            centerInWorldSpace = unit.getPosition();
            return this;
        }

        @Override
        public ActionRawCameraMove build() {
            require("center in world space", centerInWorldSpace);
            return new ActionRawCameraMove(this);
        }
    }

    private ActionRawCameraMove(Builder builder) {
        centerInWorldSpace = builder.centerInWorldSpace;
    }

    private ActionRawCameraMove(Raw.ActionRawCameraMove sc2ApiActionRawCameraMove) {
        centerInWorldSpace = tryGet(
                Raw.ActionRawCameraMove::getCenterWorldSpace, Raw.ActionRawCameraMove::hasCenterWorldSpace
        ).apply(sc2ApiActionRawCameraMove).map(Point::from).orElseThrow(required("center in world space"));
    }

    public static ActionRawCameraMoveSyntax cameraMove() {
        return new Builder();
    }

    public static ActionRawCameraMove from(Raw.ActionRawCameraMove sc2ApiActionRawCameraMove) {
        require("sc2api action raw camera move", sc2ApiActionRawCameraMove);
        return new ActionRawCameraMove(sc2ApiActionRawCameraMove);
    }

    @Override
    public Raw.ActionRawCameraMove toSc2Api() {
        return Raw.ActionRawCameraMove.newBuilder().setCenterWorldSpace(centerInWorldSpace.toSc2Api()).build();
    }

    public Point getCenterInWorldSpace() {
        return centerInWorldSpace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionRawCameraMove that = (ActionRawCameraMove) o;

        return centerInWorldSpace.equals(that.centerInWorldSpace);
    }

    @Override
    public int hashCode() {
        return centerInWorldSpace.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
