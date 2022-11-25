package com.github.ocraft.s2client.bot.gateway.impl;

/*-
 * #%L
 * ocraft-s2client-bot
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

import SC2APIProtocol.Data;
import com.github.ocraft.s2client.bot.gateway.ActionInterface;
import com.github.ocraft.s2client.protocol.action.Action;
import com.github.ocraft.s2client.protocol.action.ActionChat;
import com.github.ocraft.s2client.protocol.action.raw.ActionRaw;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawToggleAutocast;
import com.github.ocraft.s2client.protocol.action.raw.ActionRawUnitCommand;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.request.RequestAction;
import com.github.ocraft.s2client.protocol.request.Requests;
import com.github.ocraft.s2client.protocol.response.ResponseAction;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.action.Action.action;

class ActionInterfaceImpl implements ActionInterface {

    private final ControlInterfaceImpl controlInterface;
    private final List<Action> actions = new ArrayList<>();
    private final List<Tag> commands = new ArrayList<>();

    ActionInterfaceImpl(ControlInterfaceImpl controlInterface) {
        this.controlInterface = controlInterface;
    }

    private ControlInterfaceImpl control() {
        return controlInterface;
    }

    @Override
    public ActionInterface unitCommand(Unit unit, Ability ability, boolean queuedCommand) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(unit)
                .useAbility(ability)
                .queued(queuedCommand)).build());
        return this;
    }

    @Override
    public ActionInterface unitCommand(Unit unit, Ability ability, Point2d point, boolean queuedCommand) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(unit)
                .useAbility(ability)
                .target(point)
                .queued(queuedCommand)).build());
        return this;
    }

    @Override
    public ActionInterface unitCommand(Unit unit, Ability ability, Unit target, boolean queuedCommand) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(unit)
                .useAbility(ability)
                .target(target.getTag())
                .queued(queuedCommand)).build());
        return this;
    }

    @Override
    public ActionInterface unitCommand(List<Unit> units, Ability ability, boolean queuedMove) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(units.toArray(new Unit[0]))
                .useAbility(ability)
                .queued(queuedMove)).build());
        return this;
    }

    @Override
    public ActionInterface unitCommand(List<Unit> units, Ability ability, Point2d point, boolean queuedCommand) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(units.toArray(new Unit[0]))
                .useAbility(ability)
                .target(point)
                .queued(queuedCommand)).build());
        return this;
    }

    @Override
    public ActionInterface unitCommand(List<Unit> units, Ability ability, Unit target, boolean queuedCommand) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(units.toArray(new Unit[0]))
                .useAbility(ability)
                .target(target.getTag())
                .queued(queuedCommand)).build());
        return this;
    }

    @Override
    public ActionInterface unitCommand(Tag unit, Ability ability, boolean queuedCommand) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(unit)
                .useAbility(ability)
                .queued(queuedCommand)).build());
        return this;
    }

    @Override
    public ActionInterface unitCommand(Tag unit, Ability ability, Point2d point, boolean queuedCommand) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(unit)
                .useAbility(ability)
                .target(point)
                .queued(queuedCommand)).build());
        return this;
    }

    @Override
    public ActionInterface unitCommand(Tag unit, Ability ability, Unit target, boolean queuedCommand) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(unit)
                .useAbility(ability)
                .target(target.getTag())
                .queued(queuedCommand)).build());
        return this;
    }

    @Override
    public ActionInterface unitCommand(Set<Tag> units, Ability ability, boolean queuedMove) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(units.toArray(new Tag[0]))
                .useAbility(ability)
                .queued(queuedMove)).build());
        return this;
    }

    @Override
    public ActionInterface unitCommand(Set<Tag> units, Ability ability, Point2d point, boolean queuedCommand) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(units.toArray(new Tag[0]))
                .useAbility(ability)
                .target(point)
                .queued(queuedCommand)).build());
        return this;
    }

    @Override
    public ActionInterface unitCommand(Set<Tag> units, Ability ability, Unit target, boolean queuedCommand) {
        actions.add(action().raw(ActionRawUnitCommand.unitCommand()
                .forUnits(units.toArray(new Tag[0]))
                .useAbility(ability)
                .target(target.getTag())
                .queued(queuedCommand)).build());
        return this;
    }

    @Override
    public List<Tag> commands() {
        return new ArrayList<>(commands);
    }

    @Override
    public ActionInterface toggleAutocast(Tag unitTag, Ability ability) {
        actions.add(action().raw(ActionRawToggleAutocast.toggleAutocast()
                .ofAbility(ability).forUnits(unitTag)).build());
        return this;
    }

    @Override
    public ActionInterface toggleAutocast(List<Tag> unitTags, Ability ability) {
        actions.add(action().raw(ActionRawToggleAutocast.toggleAutocast()
                .ofAbility(ability).forUnits(unitTags.toArray(new Tag[0]))).build());
        return this;
    }

    @Override
    public ActionInterface sendChat(String message, ActionChat.Channel channel) {
        actions.add(action().chat(ActionChat.message().of(message).to(channel)).build());
        return this;
    }

    @Override
    public ActionInterface select(Tag... unitTag) {
        actions.add(action()
                .raw(ActionRawUnitCommand.unitCommand()
                        .forUnits(unitTag)
                        .useAbility(Abilities.from(0))
                        .build())
                .build());
        return this;
    }

    @Override
    public ActionInterface select(Unit... unit) {
        actions.add(action()
                .raw(ActionRawUnitCommand.unitCommand()
                        .forUnits(unit)
                        .useAbility(Abilities.from(0))
                        .build())
                .build());
        return this;
    }

    @Override
    public boolean sendActions() {
        commands.clear();
        if (actions.isEmpty()) return false;
        RequestAction.Builder request = Requests.actions().of(actions.toArray(new Action[0]));

        actions.forEach(action -> action.getRaw()
                .flatMap(ActionRaw::getUnitCommand)
                .map(ActionRawUnitCommand::getUnitTags)
                .ifPresent(commands::addAll));

        actions.clear();
        return control()
                .waitForResponse(control().proto().sendRequest(request))
                .flatMap(response -> response.as(ResponseAction.class))
                .isPresent();
    }
}
