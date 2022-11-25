package com.github.ocraft.s2client.bot.gateway;

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

import com.github.ocraft.s2client.protocol.action.ActionChat;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.List;
import java.util.Set;

/**
 * The ActionInterface issues actions to units in a game. Not available in replays.
 * Guaranteed to be valid when the OnStep event is called.
 * <p>
 * <br>
 * <b>void unitCommand(Tag unitTag, Ability ability)</b> - batches a unitCommand that will be dispatched when
 * #sendActions() is called. unitCommand has many overloaded functions, you can call it with most combinations of Unit
 * types (the Unit object or tag), ability types and targets (a 2d position or tag).
 * <ul>
 * <li>unitTag: The unique id that represents the unit.</li>
 * <li>ability: The unique id that represents the ability, see Abilities for ids.</li>
 * </ul>
 *
 * @see com.github.ocraft.s2client.protocol.data.Abilities
 * @see Unit
 * @see Point2d
 * @see #sendActions()
 */
public interface ActionInterface {

    /**
     * Issues a command to a unit. Self targeting.
     *
     * @param unit    The unit to send the command to.
     * @param ability The ability id of the command.
     */
    ActionInterface unitCommand(Unit unit, Ability ability, boolean queuedCommand);

    /**
     * Issues a command to a unit. Targets a point.
     *
     * @param unit    The unit to send the command to.
     * @param ability The ability id of the command.
     * @param point   The 2D world position to target.
     */
    ActionInterface unitCommand(Unit unit, Ability ability, Point2d point, boolean queuedCommand);

    /**
     * Issues a command to a unit. Targets another unit.
     *
     * @param unit    The unit to send the command to.
     * @param ability The ability id of the command.
     * @param target  The unit that is a target of the unit getting the command.
     */
    ActionInterface unitCommand(Unit unit, Ability ability, Unit target, boolean queuedCommand);

    /**
     * Issues a command to multiple units (prefer this where possible). Same as unitCommand(Unit, Ability).
     */
    ActionInterface unitCommand(List<Unit> units, Ability ability, boolean queuedMove);

    /**
     * Issues a command to multiple units (prefer this where possible). Same as unitCommand(Unit, Ability, Point2d).
     */
    ActionInterface unitCommand(List<Unit> units, Ability ability, Point2d point, boolean queuedCommand);

    /**
     * Issues a command to multiple units (prefer this where possible). Same as unitCommand(Unit, Ability, Unit).
     */
    ActionInterface unitCommand(List<Unit> units, Ability ability, Unit target, boolean queuedCommand);

    /**
     * @see #unitCommand(Unit, Ability, boolean)
     */
    ActionInterface unitCommand(Tag unit, Ability ability, boolean queuedCommand);

    /**
     * @see #unitCommand(Unit, Ability, Point2d, boolean)
     */
    ActionInterface unitCommand(Tag unit, Ability ability, Point2d point, boolean queuedCommand);

    /**
     * @see #unitCommand(Unit, Ability, Unit, boolean)
     */
    ActionInterface unitCommand(Tag unit, Ability ability, Unit target, boolean queuedCommand);

    /**
     * @see #unitCommand(List, Ability, boolean)
     */
    ActionInterface unitCommand(Set<Tag> units, Ability ability, boolean queuedMove);

    /**
     * @see #unitCommand(List, Ability, Point2d, boolean)
     */
    ActionInterface unitCommand(Set<Tag> units, Ability ability, Point2d point, boolean queuedCommand);

    /**
     * @see #unitCommand(List, Ability, Unit, boolean)
     */
    ActionInterface unitCommand(Set<Tag> units, Ability ability, Unit target, boolean queuedCommand);

    /**
     * Returns a list of unit tags that have sent commands out in the last call to SendActions. This will be used to
     * determine if a unit actually has a command when the observation is received.
     *
     * @return Array of units that have sent commands.
     */
    List<Tag> commands();

    /**
     * Enables or disables autocast of an ability on a unit.
     *
     * @param unitTag The unit to toggle the ability on.
     * @param ability The ability to be toggled.
     */
    ActionInterface toggleAutocast(Tag unitTag, Ability ability);

    /**
     * Enables or disables autocast of an ability on a list of units.
     *
     * @param unitTags The units to toggle the ability on.
     * @param ability  The ability to be toggled.
     */
    ActionInterface toggleAutocast(List<Tag> unitTags, Ability ability);

    /**
     * Sends a message to the game chat.
     *
     * @param message Text of message to send.
     * @param channel Which players will see the message.
     */
    ActionInterface sendChat(String message, ActionChat.Channel channel);

    /**
     * Selects a specific unit. This is used for Ui Actions such as {@link ActionFeatureLayerInterface#unloadCargo},
     * and rawAffectsSelection must be set to true, otherwise the selection will be reverted.
     *
     * @param unitTag Tag of the unit or units to select.
     */
    ActionInterface select(Tag... unitTag);

    /**
     * Selects a specific unit. This is used for Ui Actions such as {@link ActionFeatureLayerInterface#unloadCargo},
     * and rawAffectsSelection must be set to true, otherwise the selection will be reverted.
     *
     * @param unit The unit or units to select.
     */
    ActionInterface select(Unit... unit);

    /**
     * This function sends out all batched unit commands. You DO NOT need to call this function in non real time
     * simulations since it is automatically called when stepping the simulation forward. You only need to call this
     * function in a real time simulation. For example, if you wanted to move 20 marines to some position on the map
     * you'd want to batch all of those unit commands and send them at once.
     */
    boolean sendActions();
}
