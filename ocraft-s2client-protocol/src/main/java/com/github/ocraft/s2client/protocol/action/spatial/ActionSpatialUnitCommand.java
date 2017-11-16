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
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitCommandBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitCommandSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.QueuedSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.TargetSyntax;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionSpatialUnitCommand implements Sc2ApiSerializable<Spatial.ActionSpatialUnitCommand> {

    private static final long serialVersionUID = 971041079611803562L;

    private final Ability ability;
    private final boolean queued;
    private final PointI targetInScreenCoord;
    private final PointI targetInMinimapCoord;

    public static final class Builder implements ActionSpatialUnitCommandSyntax, TargetSyntax, QueuedSyntax {

        private Ability ability;
        private PointI targetInScreenCoord;
        private PointI targetInMinimapCoord;
        private boolean queued;

        @Override
        public TargetSyntax useAbility(Ability ability) {
            this.ability = ability;
            return this;
        }

        @Override
        public QueuedSyntax onScreen(PointI targetOnScreen) {
            targetInScreenCoord = targetOnScreen;
            targetInMinimapCoord = nothing();
            return this;
        }

        @Override
        public QueuedSyntax onMinimap(PointI targetOnMinimap) {
            targetInMinimapCoord = targetOnMinimap;
            targetInScreenCoord = nothing();
            return this;
        }

        @Override
        public ActionSpatialUnitCommandBuilder queued() {
            queued = true;
            return this;
        }

        @Override
        public ActionSpatialUnitCommand build() {
            require("ability", ability);
            return new ActionSpatialUnitCommand(this);
        }

    }

    public static ActionSpatialUnitCommandSyntax unitCommand() {
        return new Builder();
    }

    private ActionSpatialUnitCommand(Builder builder) {
        this.ability = builder.ability;
        this.targetInScreenCoord = builder.targetInScreenCoord;
        this.targetInMinimapCoord = builder.targetInMinimapCoord;
        this.queued = builder.queued;
    }

    private ActionSpatialUnitCommand(Spatial.ActionSpatialUnitCommand sc2ApiActionSpatialUnitCommand) {
        this.ability = tryGet(
                Spatial.ActionSpatialUnitCommand::getAbilityId, Spatial.ActionSpatialUnitCommand::hasAbilityId
        ).apply(sc2ApiActionSpatialUnitCommand).map(Abilities::from).orElseThrow(required("ability"));

        this.targetInScreenCoord = tryGet(
                Spatial.ActionSpatialUnitCommand::getTargetScreenCoord,
                Spatial.ActionSpatialUnitCommand::hasTargetScreenCoord
        ).apply(sc2ApiActionSpatialUnitCommand).map(PointI::from).orElse(nothing());

        this.targetInMinimapCoord = tryGet(
                Spatial.ActionSpatialUnitCommand::getTargetMinimapCoord,
                Spatial.ActionSpatialUnitCommand::hasTargetMinimapCoord
        ).apply(sc2ApiActionSpatialUnitCommand).map(PointI::from).orElse(nothing());

        this.queued = tryGet(
                Spatial.ActionSpatialUnitCommand::getQueueCommand, Spatial.ActionSpatialUnitCommand::hasQueueCommand
        ).apply(sc2ApiActionSpatialUnitCommand).orElse(false);
    }

    public static ActionSpatialUnitCommand from(Spatial.ActionSpatialUnitCommand sc2apiActionSpatialUnitCommand) {
        require("sc2api action spatial unit command", sc2apiActionSpatialUnitCommand);
        return new ActionSpatialUnitCommand(sc2apiActionSpatialUnitCommand);
    }

    @Override
    public Spatial.ActionSpatialUnitCommand toSc2Api() {
        Spatial.ActionSpatialUnitCommand.Builder aSc2ApiUnitCommand = Spatial.ActionSpatialUnitCommand.newBuilder()
                .setAbilityId(ability.toSc2Api())
                .setQueueCommand(queued);

        getTargetInScreenCoord().map(PointI::toSc2Api).ifPresent(aSc2ApiUnitCommand::setTargetScreenCoord);
        getTargetInMinimapCoord().map(PointI::toSc2Api).ifPresent(aSc2ApiUnitCommand::setTargetMinimapCoord);

        return aSc2ApiUnitCommand.build();
    }

    public Ability getAbility() {
        return ability;
    }

    public Optional<PointI> getTargetInScreenCoord() {
        return Optional.ofNullable(targetInScreenCoord);
    }

    public Optional<PointI> getTargetInMinimapCoord() {
        return Optional.ofNullable(targetInMinimapCoord);
    }

    public boolean isQueued() {
        return queued;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionSpatialUnitCommand that = (ActionSpatialUnitCommand) o;

        return queued == that.queued &&
                ability == that.ability &&
                (targetInScreenCoord != null
                        ? targetInScreenCoord.equals(that.targetInScreenCoord)
                        : that.targetInScreenCoord == null) &&
                (targetInMinimapCoord != null
                        ? targetInMinimapCoord.equals(that.targetInMinimapCoord)
                        : that.targetInMinimapCoord == null);
    }

    @Override
    public int hashCode() {
        int result = ability.hashCode();
        result = 31 * result + (queued ? 1 : 0);
        result = 31 * result + (targetInScreenCoord != null ? targetInScreenCoord.hashCode() : 0);
        result = 31 * result + (targetInMinimapCoord != null ? targetInMinimapCoord.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
