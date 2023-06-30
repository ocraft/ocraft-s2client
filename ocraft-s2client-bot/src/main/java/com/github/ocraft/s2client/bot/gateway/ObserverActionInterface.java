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

import com.github.ocraft.s2client.protocol.spatial.Point2I;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

/**
 * The ObserverActionInterface corresponds to the actions available in the observer UI.
 */
public interface ObserverActionInterface {
    /**
     * Moves the observer camera to a target location. Will cause the camera to stop following
     * the observed player's perspective.
     *
     * @param point    The 2D world position to target.
     * @param distance Distance between camera and terrain. Larger value zooms out camera. Defaults to standard camera
     *                 distance if set to 0.
     */
    ObserverActionInterface cameraMove(Point2I point, float distance);

    /**
     * Makes the observer camera follow the observed player's perspective.
     *
     * @param playerId Player to follow.
     */
    ObserverActionInterface cameraFollowPlayer(int playerId);

    /**
     * Makes the observer view the camera from the perspective of a player
     *
     * @param playerId Player to use perspective of.
     */
    ObserverActionInterface cameraSetPerspective(int playerId);

    /**
     * Moves the observer to follow the specific set of units.
     *
     * @param units The units to follow.
     */
    ObserverActionInterface cameraFollowUnits(Unit...units);

    /**
     * Moves the observer camera to follow a specific set of units.
     *
     * @param units The units to follow.
     */
    ObserverActionInterface cameraFollowUnits(Tag...units);

    /**
     * This function sends out all batched commands. You DO NOT need to call this function.
     * it is automatically called when stepping the simulation forward.
     */
    boolean sendActions();
}
