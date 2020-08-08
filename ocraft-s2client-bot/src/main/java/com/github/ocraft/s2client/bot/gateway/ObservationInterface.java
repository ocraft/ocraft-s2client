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

import com.github.ocraft.s2client.protocol.action.ActionError;
import com.github.ocraft.s2client.protocol.action.raw.ActionRaw;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatial;
import com.github.ocraft.s2client.protocol.data.*;
import com.github.ocraft.s2client.protocol.observation.ChatReceived;
import com.github.ocraft.s2client.protocol.observation.Observation;
import com.github.ocraft.s2client.protocol.observation.PlayerResult;
import com.github.ocraft.s2client.protocol.observation.raw.EffectLocations;
import com.github.ocraft.s2client.protocol.observation.raw.PowerSource;
import com.github.ocraft.s2client.protocol.observation.raw.Visibility;
import com.github.ocraft.s2client.protocol.response.ResponseGameInfo;
import com.github.ocraft.s2client.protocol.score.Score;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Alliance;
import com.github.ocraft.s2client.protocol.unit.Tag;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The ObservationInterface reflects the current state of the game. Guaranteed to be valid when OnGameStart or OnStep
 * is called.
 */
public interface ObservationInterface {

    /**
     * Gets a unique ID that represents the player.
     *
     * @return The player ID.
     */
    int getPlayerId();

    /**
     * Get the current game loop for this observation.
     *
     * @return The game loop.
     */
    long getGameLoop();

    /**
     * Get a list of all known units in the game.
     *
     * @return List of all ally and visible enemy and neutral units.
     */
    List<UnitInPool> getUnits();

    /**
     * Get all units belonging to a certain alliance and meet the conditions provided by the filter.
     * The unit structure is data only.
     * Therefore editing that data will not change any in game state. See the ActionInterface for changing Unit state.
     *
     * @param alliance The faction the units belong to.
     * @param filter   A functor or lambda used to filter out any unneeded units in the list.
     * @return A list of units that meet the conditions provided by alliance and filter.
     */
    List<UnitInPool> getUnits(Alliance alliance, Predicate<UnitInPool> filter);

    /**
     * @see #getUnits(Alliance, Predicate)
     */
    List<UnitInPool> getUnits(Alliance alliance);

    /**
     * Get all units belonging to self that meet the conditions provided by the filter. The unit structure is data only.
     * Therefore editing that data will not change any in game state. See the ActionInterface for changing Unit state.
     *
     * @param filter A functor or lambda used to filter out any unneeded units in the list.
     * @return A list of units that meet the conditions provided by the filter.
     */
    List<UnitInPool> getUnits(Predicate<UnitInPool> filter);

    /**
     * Get the unit state as represented by the last call to getObservation.
     *
     * @param tag Unique tag of the unit.
     * @return Pointer to the Unit object.
     */
    UnitInPool getUnit(Tag tag);

    /**
     * Gets a list of actions performed as abilities applied to units. For use with the raw option.
     *
     * @return List of raw actions.
     */
    List<ActionRaw> getRawActions();

    /**
     * Gets a list of actions performed. For use with the feature layer options.
     *
     * @return List of actions.
     */
    List<ActionSpatial> getFeatureLayerActions();

    /**
     * Gets a list of actions performed. For use with the rendered options.
     *
     * @return List of actions.
     */
    List<ActionSpatial> getRenderedActions();

    /**
     * Gets new chat messages.
     *
     * @return List of chat messages.
     */
    List<ChatReceived> getChatMessages();

    /**
     * Gets all power sources associated with the current player.
     *
     * @return List of power sources.
     */
    List<PowerSource> getPowerSources();

    /**
     * Gets all active effects in vision of the current player.
     *
     * @return List of effects.
     */
    List<EffectLocations> getEffects();

    /**
     * Gets all upgrades.
     *
     * @return List of upgrades.
     */
    List<Upgrade> getUpgrades();

    /**
     * Gets the detailed current set of scores.
     *
     * @return The current score structure.
     */
    Score getScore();

    /**
     * Gets metadata of abilities. Array can be indexed directly by AbilityID.
     *
     * @param forceRefresh forces a full query from the game, may otherwise cache data from a previous call.
     * @return Data about all abilities possible for the current game session.
     */
    Map<Ability, AbilityData> getAbilityData(boolean forceRefresh);

    /**
     * Gets metadata of units. Array can be indexed directly by UnitID.
     *
     * @param forceRefresh forces a full query from the game, may otherwise cache data from a previous call.
     * @return Data about all units possible for the current game session.
     */
    Map<UnitType, UnitTypeData> getUnitTypeData(boolean forceRefresh);

    /**
     * Gets metadata of upgrades. Array can be indexed directly by UpgradeID.
     *
     * @param forceRefresh forces a full query from the game, may otherwise cache data from a previous call.
     * @return Data about all upgrades possible for the current game session.
     */
    Map<Upgrade, UpgradeData> getUpgradeData(boolean forceRefresh);

    /**
     * Gets metadata of buffs. Array can be indexed directly by BuffID.
     *
     * @param forceRefresh forces a full query from the game, may otherwise cache data from a previous call.
     * @return Data about all buffs possible for the current game session.
     */
    Map<Buff, BuffData> getBuffData(boolean forceRefresh);

    /**
     * Gets metadata of effects. Array can be indexed directly by EffectID.
     *
     * @param forceRefresh forces a full query from the game, may otherwise cache data from a previous call.
     * @return Data about all effects possible for the current game session.
     */
    Map<Effect, EffectData> getEffectData(boolean forceRefresh);

    /**
     * Gets the GameInfo struct for the current map.
     *
     * @return The current GameInfo struct.
     */
    ResponseGameInfo getGameInfo();

    /**
     * The mineral count of the player.
     *
     * @return The mineral count.
     */
    int getMinerals();

    /**
     * The vespene count of the player.
     *
     * @return The vespene count.
     */
    int getVespene();

    /**
     * The total supply cap given the players max possible supply.
     *
     * @return Food cap.
     * @see #getFoodUsed()
     * @see #getFoodArmy()
     * @see #getFoodWorkers()
     */
    int getFoodCap();

    /**
     * The total supply used by the player as defined: getFoodArmy() + getFoodWorkers().
     *
     * @return Food used.
     * @see #getFoodArmy()
     * @see #getFoodWorkers()
     */
    int getFoodUsed();

    /**
     * The total supply consumed by army units alone.
     *
     * @return Food used by army units.
     * @see #getFoodUsed()
     * @see #getFoodWorkers()
     */
    int getFoodArmy();

    /**
     * The total supply consumed by workers units alone.
     *
     * @return Food used by worker units.
     * @see #getFoodArmy()
     * @see #getFoodUsed()
     */
    int getFoodWorkers();

    /**
     * The number of workers that currently have no orders.
     *
     * @return Count of idle workers.
     */
    int getIdleWorkerCount();

    /**
     * The number of army units.
     *
     * @return Count of army units.
     */
    int getArmyCount();

    /**
     * Number of warp gates owned by the player. This value should only be nonzero for Protoss.
     *
     * @return Count of warp gates.
     */
    int getWarpGateCount();

    /**
     * Position of the center of the camera.
     *
     * @return Camera position.
     */
    Point getCameraPos();

    /**
     * Gets the initial start location of the player.
     *
     * @return Player start position.
     */
    Point getStartLocation();

    /**
     * Gets the results of the game.
     *
     * @return Player results if the game ended, an empty vector otherwise.
     */
    List<PlayerResult> getResults();

    /**
     * Returns 'true' if the given point has creep.
     *
     * @param point Position to sample.
     * @return Creep.
     */
    boolean hasCreep(Point2d point);

    /**
     * Returns visibility value of the given point for the current player.
     *
     * @param point Position to sample.
     * @return Visibility.
     */
    Visibility getVisibility(Point2d point);

    /**
     * Returns 'true' if the given point on the terrain is pathable. This does not
     * include pathing blockers like structures. For more accurate pathing results
     * use QueryInterface::PathingDistance.
     *
     * @param point Position to sample.
     * @return Pathable.
     */
    boolean isPathable(Point2d point);

    /**
     * Returns 'true' if the given point on the terrain is buildable. This does not
     * include blockers like other structures. For more accurate building placement
     * results use QueryInterface#placement.
     *
     * @param point Position to sample.
     * @return Placable.
     */
    boolean isPlacable(Point2d point);

    /**
     * Returns terrain height of the given point.
     *
     * @param point Position to sample.
     * @return Height.
     */
    float terrainHeight(Point2d point);

    /**
     * Equivalent of UI "red text" errors.
     *
     * @return Action errors.
     */
    List<ActionError> getActionErrors();

    /**
     * A pointer to the low-level protocol data for the current observation. While it's possible to extract most in-game
     * data from this pointer it is highly discouraged. It should only be used for extracting feature layers because it
     * would be inefficient to copy these each frame.
     *
     * @return A pointer to the Observation.
     * @see ControlInterface#getObservation
     */
    Observation getRawObservation();

}
