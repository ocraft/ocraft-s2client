package com.github.ocraft.s2client.protocol.action.spatial;

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

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.GeneralizableAbility;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Ability;

import java.util.Optional;
import java.util.function.UnaryOperator;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionSpatial
        implements Sc2ApiSerializable<Spatial.ActionSpatial>, GeneralizableAbility<ActionSpatial> {

    private static final long serialVersionUID = -1843366180196100686L;

    private final ActionSpatialUnitCommand unitCommand;
    private final ActionSpatialCameraMove cameraMove;
    private final ActionSpatialUnitSelectionPoint unitSelectionPoint;
    private final ActionSpatialUnitSelectionRect unitSelectionRect;

    private ActionSpatial(Spatial.ActionSpatial sc2ApiActionSpatial) {
        this.unitCommand = tryGet(
                Spatial.ActionSpatial::getUnitCommand, Spatial.ActionSpatial::hasUnitCommand
        ).apply(sc2ApiActionSpatial).map(ActionSpatialUnitCommand::from).orElse(nothing());

        this.cameraMove = tryGet(
                Spatial.ActionSpatial::getCameraMove, Spatial.ActionSpatial::hasCameraMove
        ).apply(sc2ApiActionSpatial).map(ActionSpatialCameraMove::from).orElse(nothing());

        this.unitSelectionPoint = tryGet(
                Spatial.ActionSpatial::getUnitSelectionPoint, Spatial.ActionSpatial::hasUnitSelectionPoint
        ).apply(sc2ApiActionSpatial).map(ActionSpatialUnitSelectionPoint::from).orElse(nothing());

        this.unitSelectionRect = tryGet(
                Spatial.ActionSpatial::getUnitSelectionRect, Spatial.ActionSpatial::hasUnitSelectionRect
        ).apply(sc2ApiActionSpatial).map(ActionSpatialUnitSelectionRect::from).orElse(nothing());

        if (!oneOfActionCaseIsSet()) {
            throw new IllegalArgumentException("one of action case is required");
        }
    }

    private ActionSpatial(ActionSpatialUnitCommand unitCommand) {
        this.unitCommand = unitCommand;
        this.cameraMove = nothing();
        this.unitSelectionPoint = nothing();
        this.unitSelectionRect = nothing();
    }

    private ActionSpatial(ActionSpatialCameraMove cameraMove) {
        this.unitCommand = nothing();
        this.cameraMove = cameraMove;
        this.unitSelectionPoint = nothing();
        this.unitSelectionRect = nothing();
    }

    private ActionSpatial(ActionSpatialUnitSelectionPoint unitSelectionPoint) {
        this.unitCommand = nothing();
        this.cameraMove = nothing();
        this.unitSelectionPoint = unitSelectionPoint;
        this.unitSelectionRect = nothing();
    }

    private ActionSpatial(ActionSpatialUnitSelectionRect unitSelectionRect) {
        this.unitCommand = nothing();
        this.cameraMove = nothing();
        this.unitSelectionPoint = nothing();
        this.unitSelectionRect = unitSelectionRect;
    }

    private boolean oneOfActionCaseIsSet() {
        return isSet(unitCommand) || isSet(cameraMove) || isSet(unitSelectionPoint) || isSet(unitSelectionRect);
    }

    public static ActionSpatial of(ActionSpatialUnitCommand unitCommand) {
        require("unit command", unitCommand);
        return new ActionSpatial(unitCommand);
    }

    public static ActionSpatial of(ActionSpatialCameraMove cameraMove) {
        require("camera move", cameraMove);
        return new ActionSpatial(cameraMove);
    }

    public static ActionSpatial of(ActionSpatialUnitSelectionPoint unitSelectionPoint) {
        require("unit selection point", unitSelectionPoint);
        return new ActionSpatial(unitSelectionPoint);
    }

    public static ActionSpatial of(ActionSpatialUnitSelectionRect unitSelectionRect) {
        require("unit selection rect", unitSelectionRect);
        return new ActionSpatial(unitSelectionRect);
    }

    public static ActionSpatial from(Spatial.ActionSpatial sc2ApiActionSpatial) {
        require("sc2api action spatial", sc2ApiActionSpatial);
        return new ActionSpatial(sc2ApiActionSpatial);
    }

    @Override
    public Spatial.ActionSpatial toSc2Api() {
        Spatial.ActionSpatial.Builder aSc2ApiActionSpatial = Spatial.ActionSpatial.newBuilder();

        getUnitCommand().map(ActionSpatialUnitCommand::toSc2Api).ifPresent(aSc2ApiActionSpatial::setUnitCommand);
        getCameraMove().map(ActionSpatialCameraMove::toSc2Api).ifPresent(aSc2ApiActionSpatial::setCameraMove);
        getUnitSelectionPoint().map(ActionSpatialUnitSelectionPoint::toSc2Api)
                .ifPresent(aSc2ApiActionSpatial::setUnitSelectionPoint);
        getUnitSelectionRect().map(ActionSpatialUnitSelectionRect::toSc2Api)
                .ifPresent(aSc2ApiActionSpatial::setUnitSelectionRect);

        return aSc2ApiActionSpatial.build();
    }

    public Optional<ActionSpatialUnitCommand> getUnitCommand() {
        return Optional.ofNullable(unitCommand);
    }

    public Optional<ActionSpatialCameraMove> getCameraMove() {
        return Optional.ofNullable(cameraMove);
    }

    public Optional<ActionSpatialUnitSelectionPoint> getUnitSelectionPoint() {
        return Optional.ofNullable(unitSelectionPoint);
    }

    public Optional<ActionSpatialUnitSelectionRect> getUnitSelectionRect() {
        return Optional.ofNullable(unitSelectionRect);
    }

    @Override
    public ActionSpatial generalizeAbility(UnaryOperator<Ability> generalize) {
        return getUnitCommand()
                .map(command -> ActionSpatial.of(command.generalizeAbility(generalize)))
                .orElse(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionSpatial that = (ActionSpatial) o;

        return (unitCommand != null ? unitCommand.equals(that.unitCommand) : that.unitCommand == null) &&
                (cameraMove != null ? cameraMove.equals(that.cameraMove) : that.cameraMove == null) &&
                (unitSelectionPoint != null
                        ? unitSelectionPoint.equals(that.unitSelectionPoint)
                        : that.unitSelectionPoint == null) &&
                (unitSelectionRect != null
                        ? unitSelectionRect.equals(that.unitSelectionRect)
                        : that.unitSelectionRect == null);
    }

    @Override
    public int hashCode() {
        int result = unitCommand != null ? unitCommand.hashCode() : 0;
        result = 31 * result + (cameraMove != null ? cameraMove.hashCode() : 0);
        result = 31 * result + (unitSelectionPoint != null ? unitSelectionPoint.hashCode() : 0);
        result = 31 * result + (unitSelectionRect != null ? unitSelectionRect.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
