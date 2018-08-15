package com.github.ocraft.s2client.protocol.action.raw;

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
import com.github.ocraft.s2client.protocol.GeneralizableAbility;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.syntax.action.raw.*;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

public final class ActionRawUnitCommand
        implements Sc2ApiSerializable<Raw.ActionRawUnitCommand>, GeneralizableAbility<ActionRawUnitCommand> {

    private static final long serialVersionUID = -4206327934043657298L;

    private final Ability ability;
    private final Tag targetedUnitTag;
    private final Point2d targetedWorldSpacePosition;
    private final Set<Tag> unitTags;
    private final boolean queued;

    public static final class Builder
            implements ActionRawUnitCommandSyntax, UseAbilitySyntax, TargetSyntax, QueuedSyntax {

        private Ability ability;
        private Tag targetedUnitTag;
        private Point2d targetedWorldSpacePosition;
        private Set<Tag> unitTags = new HashSet<>();
        private boolean queued;

        @Override
        public UseAbilitySyntax forUnits(Unit... units) {
            unitTags.addAll(stream(units).map(Unit::getTag).collect(toSet()));
            return this;
        }

        @Override
        public UseAbilitySyntax forUnits(Collection<Tag> units) {
            unitTags.addAll(units);
            return this;
        }

        @Override
        public UseAbilitySyntax forUnits(Tag... units) {
            unitTags.addAll(asList(units));
            return this;
        }

        @Override
        public TargetSyntax useAbility(Ability ability) {
            this.ability = ability;
            return this;
        }

        @Override
        public QueuedSyntax target(Tag unitTag) {
            targetedUnitTag = unitTag;
            targetedWorldSpacePosition = nothing();
            return this;
        }

        @Override
        public QueuedSyntax target(Point2d worldPosition) {
            targetedWorldSpacePosition = worldPosition;
            targetedUnitTag = nothing();
            return this;
        }

        @Override
        public ActionRawUnitCommandBuilder queued() {
            queued = true;
            return this;
        }

        @Override
        public ActionRawUnitCommandBuilder queued(boolean value) {
            queued = value;
            return this;
        }

        @Override
        public ActionRawUnitCommand build() {
            require("ability id", ability);
            requireNotEmpty("unit tag list", unitTags);
            return new ActionRawUnitCommand(this);
        }
    }

    private ActionRawUnitCommand(Builder builder) {
        ability = builder.ability;
        targetedUnitTag = builder.targetedUnitTag;
        targetedWorldSpacePosition = builder.targetedWorldSpacePosition;
        unitTags = builder.unitTags;
        queued = builder.queued;
    }

    private ActionRawUnitCommand(Raw.ActionRawUnitCommand sc2ApiActionRawUnitCommand) {
        this.ability = tryGet(
                Raw.ActionRawUnitCommand::getAbilityId, Raw.ActionRawUnitCommand::hasAbilityId
        ).apply(sc2ApiActionRawUnitCommand).map(Abilities::from).orElseThrow(required("ability id"));

        this.targetedUnitTag = tryGet(
                Raw.ActionRawUnitCommand::getTargetUnitTag, Raw.ActionRawUnitCommand::hasTargetUnitTag
        ).apply(sc2ApiActionRawUnitCommand).map(Tag::from).orElse(nothing());

        this.targetedWorldSpacePosition = tryGet(
                Raw.ActionRawUnitCommand::getTargetWorldSpacePos, Raw.ActionRawUnitCommand::hasTargetWorldSpacePos
        ).apply(sc2ApiActionRawUnitCommand).map(Point2d::from).orElse(nothing());

        this.unitTags = sc2ApiActionRawUnitCommand.getUnitTagsList().stream()
                .map(Tag::from).collect(toSet());

        requireNotEmpty("unit tag list", unitTags);

        this.queued = tryGet(
                Raw.ActionRawUnitCommand::getQueueCommand, Raw.ActionRawUnitCommand::hasQueueCommand
        ).apply(sc2ApiActionRawUnitCommand).orElse(false);
    }

    public static ActionRawUnitCommandSyntax unitCommand() {
        return new Builder();
    }

    public static ActionRawUnitCommand from(Raw.ActionRawUnitCommand sc2ApiActionRawUnitCommand) {
        require("sc2api action raw unit command", sc2ApiActionRawUnitCommand);
        return new ActionRawUnitCommand(sc2ApiActionRawUnitCommand);
    }

    @Override
    public Raw.ActionRawUnitCommand toSc2Api() {
        Raw.ActionRawUnitCommand.Builder aSc2ApiUnitCommand = Raw.ActionRawUnitCommand.newBuilder()
                .setAbilityId(ability.toSc2Api())
                .addAllUnitTags(unitTags.stream().map(Tag::toSc2Api).collect(toSet()))
                .setQueueCommand(queued);

        getTargetedUnitTag().map(Tag::toSc2Api).ifPresent(aSc2ApiUnitCommand::setTargetUnitTag);
        getTargetedWorldSpacePosition().map(Point2d::toSc2Api).ifPresent(aSc2ApiUnitCommand::setTargetWorldSpacePos);

        return aSc2ApiUnitCommand.build();
    }

    public Ability getAbility() {
        return ability;
    }

    public Optional<Tag> getTargetedUnitTag() {
        return Optional.ofNullable(targetedUnitTag);
    }

    public Optional<Point2d> getTargetedWorldSpacePosition() {
        return Optional.ofNullable(targetedWorldSpacePosition);
    }

    public Set<Tag> getUnitTags() {
        return new HashSet<>(unitTags);
    }

    public boolean isQueued() {
        return queued;
    }

    @Override
    public ActionRawUnitCommand generalizeAbility(UnaryOperator<Ability> generalize) {
        Builder builder = new Builder();

        builder.ability = generalize.apply(this.ability);
        builder.targetedUnitTag = this.targetedUnitTag;
        builder.targetedWorldSpacePosition = this.targetedWorldSpacePosition;
        builder.unitTags = this.unitTags;
        builder.queued = this.queued;

        return new ActionRawUnitCommand(builder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionRawUnitCommand that = (ActionRawUnitCommand) o;

        return queued == that.queued &&
                ability == that.ability &&
                (targetedUnitTag != null
                        ? targetedUnitTag.equals(that.targetedUnitTag)
                        : that.targetedUnitTag == null) &&
                (targetedWorldSpacePosition != null
                        ? targetedWorldSpacePosition.equals(that.targetedWorldSpacePosition)
                        : that.targetedWorldSpacePosition == null) &&
                unitTags.equals(that.unitTags);
    }

    @Override
    public int hashCode() {
        int result = ability.hashCode();
        result = 31 * result + (targetedUnitTag != null ? targetedUnitTag.hashCode() : 0);
        result = 31 * result + (targetedWorldSpacePosition != null ? targetedWorldSpacePosition.hashCode() : 0);
        result = 31 * result + unitTags.hashCode();
        result = 31 * result + (queued ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
