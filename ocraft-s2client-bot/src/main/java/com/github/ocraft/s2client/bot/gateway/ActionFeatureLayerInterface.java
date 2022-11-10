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

import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint;
import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.spatial.PointI;

/**
 * The ActionFeatureLayerInterface emulates UI actions in feature layer. Not available in replays.
 * Guaranteed to be valid when the OnStep event is called.
 */
public interface ActionFeatureLayerInterface {

    /**
     * Issues a command to whatever is selected. Self targeting.
     *
     * @param ability The ability id of the command.
     */
    ActionFeatureLayerInterface unitCommand(Ability ability);

    /**
     * Issues a command to whatever is selected. Uses a point as a target for the command.
     *
     * @param ability The ability id of the command.
     * @param point   The 2D world position to target.
     * @param minimap Target in the minimap instead of the map.
     */
    ActionFeatureLayerInterface unitCommand(Ability ability, PointI point, boolean minimap);

    /**
     * Moves the camera to be centered around a position. Coordinate is position on minimap feature layer.
     */
    ActionFeatureLayerInterface cameraMove(PointI center);

    /**
     * Selection of a point, equivalent to clicking the mouse on a unit.
     *
     * @param center        The feature layer 'pixel' being clicked on.
     * @param selectionType Any modifier keys, for example if 'shift-click' is desired.
     */
    ActionFeatureLayerInterface select(PointI center, ActionSpatialUnitSelectionPoint.Type selectionType);

    /**
     * Selection of an area, equivalent to click-dragging the mouse over an area of the screen.
     *
     * @param p0             The feature layer pixel where the first click occurs (mouse button down).
     * @param p1             The feature layer pixel where the drag release occurs (mouse button up).
     * @param addToSelection Will add newly selected units to an existing selection.
     */
    ActionFeatureLayerInterface select(PointI p0, PointI p1, boolean addToSelection);

    /**
     * Unloads a cargo unit from the currently selected unit.
     *
     * @param unitIndex      The 0-indexed position of the unit to unload.
     */
    ActionFeatureLayerInterface unloadCargo(int unitIndex);

    /**
     * This function sends out all batched selection and unit commands. You DO NOT need to call this function in non
     * real time simulations since it is automatically called when stepping the simulation forward. You only need to
     * call this function in a real time simulation.
     */
    boolean sendActions();
}
