package com.github.ocraft.s2client.protocol.action.spatial;

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionPointBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionPointSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.WithModeSyntax;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionSpatialUnitSelectionPoint
        implements Sc2ApiSerializable<Spatial.ActionSpatialUnitSelectionPoint> {

    private static final long serialVersionUID = 683716571852920799L;

    private final PointI selectionInScreenCoord;
    private final Type type;

    public enum Type implements Sc2ApiSerializable<Spatial.ActionSpatialUnitSelectionPoint.Type> {
        SELECT,             // Equivalent to normal click. Changes selection to unit.
        TOGGLE,             // Equivalent to shift+click. Toggle selection of unit.
        ALL_TYPE,           // Equivalent to control+click. Selects all units of a given type.
        ADD_ALL_TYPE;       // Equivalent to shift+control+click. Selects all units of a given type.

        public static Type from(Spatial.ActionSpatialUnitSelectionPoint.Type sc2ApiUnitSelectionPointType) {
            require("sc2api unit selection point type", sc2ApiUnitSelectionPointType);
            switch (sc2ApiUnitSelectionPointType) {
                case Select:
                    return SELECT;
                case Toggle:
                    return TOGGLE;
                case AllType:
                    return ALL_TYPE;
                case AddAllType:
                    return ADD_ALL_TYPE;
                default:
                    throw new AssertionError("unknown unit selection point type: " + sc2ApiUnitSelectionPointType);
            }
        }

        @Override
        public Spatial.ActionSpatialUnitSelectionPoint.Type toSc2Api() {
            switch (this) {
                case SELECT:
                    return Spatial.ActionSpatialUnitSelectionPoint.Type.Select;
                case TOGGLE:
                    return Spatial.ActionSpatialUnitSelectionPoint.Type.Toggle;
                case ALL_TYPE:
                    return Spatial.ActionSpatialUnitSelectionPoint.Type.AllType;
                case ADD_ALL_TYPE:
                    return Spatial.ActionSpatialUnitSelectionPoint.Type.AddAllType;
                default:
                    throw new AssertionError("unknown selection point type: " + this);
            }
        }
    }

    public static final class Builder
            implements ActionSpatialUnitSelectionPointSyntax, WithModeSyntax, ActionSpatialUnitSelectionPointBuilder {

        private PointI selectionInScreenCoord;
        private Type type;

        @Override
        public WithModeSyntax on(PointI pointOnScreen) {
            selectionInScreenCoord = pointOnScreen;
            return this;
        }

        @Override
        public ActionSpatialUnitSelectionPointBuilder withMode(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public ActionSpatialUnitSelectionPoint build() {
            require("selection in screen coord", selectionInScreenCoord);
            require("type", type);
            return new ActionSpatialUnitSelectionPoint(this);
        }
    }

    private ActionSpatialUnitSelectionPoint(Builder builder) {
        selectionInScreenCoord = builder.selectionInScreenCoord;
        type = builder.type;
    }

    private ActionSpatialUnitSelectionPoint(
            Spatial.ActionSpatialUnitSelectionPoint sc2apiActionSpatialUnitSelectionPoint) {

        this.selectionInScreenCoord = tryGet(
                Spatial.ActionSpatialUnitSelectionPoint::getSelectionScreenCoord,
                Spatial.ActionSpatialUnitSelectionPoint::hasSelectionScreenCoord
        ).apply(sc2apiActionSpatialUnitSelectionPoint)
                .map(PointI::from)
                .orElseThrow(required("selection in screen coord"));

        this.type = tryGet(
                Spatial.ActionSpatialUnitSelectionPoint::getType, Spatial.ActionSpatialUnitSelectionPoint::hasType
        ).apply(sc2apiActionSpatialUnitSelectionPoint).map(Type::from).orElseThrow(required("type"));
    }

    public static ActionSpatialUnitSelectionPointSyntax click() {
        return new Builder();
    }

    public static ActionSpatialUnitSelectionPoint from(
            Spatial.ActionSpatialUnitSelectionPoint sc2apiActionSpatialUnitSelectionPoint) {
        require("sc2api action spatial unit selection point", sc2apiActionSpatialUnitSelectionPoint);
        return new ActionSpatialUnitSelectionPoint(sc2apiActionSpatialUnitSelectionPoint);
    }

    @Override
    public Spatial.ActionSpatialUnitSelectionPoint toSc2Api() {
        return Spatial.ActionSpatialUnitSelectionPoint.newBuilder()
                .setType(type.toSc2Api())
                .setSelectionScreenCoord(selectionInScreenCoord.toSc2Api())
                .build();
    }

    public PointI getSelectionInScreenCoord() {
        return selectionInScreenCoord;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionSpatialUnitSelectionPoint that = (ActionSpatialUnitSelectionPoint) o;

        return selectionInScreenCoord.equals(that.selectionInScreenCoord) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = selectionInScreenCoord.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
