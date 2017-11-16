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
package com.github.ocraft.s2client.protocol.action.raw;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionRaw implements Sc2ApiSerializable<Raw.ActionRaw> {

    private static final long serialVersionUID = 8861695161269129846L;

    private final ActionRawUnitCommand unitCommand;
    private final ActionRawCameraMove cameraMove;
    private final ActionRawToggleAutocast toggleAutocast;

    private ActionRaw(Raw.ActionRaw sc2ApiActionRaw) {
        this.unitCommand = tryGet(
                Raw.ActionRaw::getUnitCommand, Raw.ActionRaw::hasUnitCommand
        ).apply(sc2ApiActionRaw).map(ActionRawUnitCommand::from).orElse(nothing());

        this.cameraMove = tryGet(
                Raw.ActionRaw::getCameraMove, Raw.ActionRaw::hasCameraMove
        ).apply(sc2ApiActionRaw).map(ActionRawCameraMove::from).orElse(nothing());

        this.toggleAutocast = tryGet(
                Raw.ActionRaw::getToggleAutocast, Raw.ActionRaw::hasToggleAutocast
        ).apply(sc2ApiActionRaw).map(ActionRawToggleAutocast::from).orElse(nothing());

        if (!oneOfActionCaseIsSet()) {
            throw new IllegalArgumentException("one of action case is required");
        }
    }

    private ActionRaw(ActionRawUnitCommand unitCommand) {
        this.unitCommand = unitCommand;
        this.cameraMove = nothing();
        this.toggleAutocast = nothing();
    }

    private ActionRaw(ActionRawCameraMove cameraMove) {
        this.unitCommand = nothing();
        this.cameraMove = cameraMove;
        this.toggleAutocast = nothing();
    }

    private ActionRaw(ActionRawToggleAutocast toggleAutocast) {
        this.unitCommand = nothing();
        this.cameraMove = nothing();
        this.toggleAutocast = toggleAutocast;
    }

    private boolean oneOfActionCaseIsSet() {
        return isSet(unitCommand) || isSet(cameraMove) || isSet(toggleAutocast);
    }

    public static ActionRaw from(Raw.ActionRaw sc2ApiActionRaw) {
        require("sc2api action raw", sc2ApiActionRaw);
        return new ActionRaw(sc2ApiActionRaw);
    }

    public static ActionRaw of(ActionRawUnitCommand unitCommand) {
        require("unit command", unitCommand);
        return new ActionRaw(unitCommand);
    }

    public static ActionRaw of(ActionRawCameraMove cameraMove) {
        require("camera move", cameraMove);
        return new ActionRaw(cameraMove);
    }

    public static ActionRaw of(ActionRawToggleAutocast toggleAutocast) {
        require("toggle autocast", toggleAutocast);
        return new ActionRaw(toggleAutocast);
    }

    @Override
    public Raw.ActionRaw toSc2Api() {
        Raw.ActionRaw.Builder aSc2ApiActionRaw = Raw.ActionRaw.newBuilder();

        getUnitCommand().map(ActionRawUnitCommand::toSc2Api).ifPresent(aSc2ApiActionRaw::setUnitCommand);
        getCameraMove().map(ActionRawCameraMove::toSc2Api).ifPresent(aSc2ApiActionRaw::setCameraMove);
        getToggleAutocast().map(ActionRawToggleAutocast::toSc2Api).ifPresent(aSc2ApiActionRaw::setToggleAutocast);

        return aSc2ApiActionRaw.build();
    }

    public Optional<ActionRawUnitCommand> getUnitCommand() {
        return Optional.ofNullable(unitCommand);
    }

    public Optional<ActionRawCameraMove> getCameraMove() {
        return Optional.ofNullable(cameraMove);
    }

    public Optional<ActionRawToggleAutocast> getToggleAutocast() {
        return Optional.ofNullable(toggleAutocast);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionRaw)) return false;

        ActionRaw actionRaw = (ActionRaw) o;

        return (unitCommand != null ? unitCommand.equals(actionRaw.unitCommand) : actionRaw.unitCommand == null) &&
                (cameraMove != null ? cameraMove.equals(actionRaw.cameraMove) : actionRaw.cameraMove == null) &&
                (toggleAutocast != null
                        ? toggleAutocast.equals(actionRaw.toggleAutocast)
                        : actionRaw.toggleAutocast == null);
    }

    @Override
    public int hashCode() {
        int result = unitCommand != null ? unitCommand.hashCode() : 0;
        result = 31 * result + (cameraMove != null ? cameraMove.hashCode() : 0);
        result = 31 * result + (toggleAutocast != null ? toggleAutocast.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
