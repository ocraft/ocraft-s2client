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

import com.github.ocraft.s2client.protocol.data.UnitType;
import com.github.ocraft.s2client.protocol.debug.Color;
import com.github.ocraft.s2client.protocol.debug.DebugTestProcess;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.unit.Unit;

/**
 * DebugInterface draws debug text, lines and shapes. Available at any time after the game starts.
 * Guaranteed to be valid when the OnStep event is called.
 * All debug actions are queued and dispatched when SendDebug is called. All drawn primitives
 * continue to draw without resending until another SendDebug is called.
 */
public interface DebugInterface {

    // Debug drawing primitives.

    /**
     * Outputs text at the top, left of the screen.
     *
     * @param out   The string of text to display.
     * @param color (Optional) Color of the text.
     */
    DebugInterface debugTextOut(String out, Color color);

    /**
     * Outputs text at any 2D point on the screen. Coordinate ranges are 0..1 in X and Y.
     *
     * @param out         The string of text to display.
     * @param ptVirtual2d The screen position to draw text at.
     * @param color       (Optional) Color of the text.
     * @param size        (Optional) Pixel height of the text.
     */
    DebugInterface debugTextOut(String out, Point2d ptVirtual2d, Color color, int size);

    /**
     * Outputs text at any 3D point in the game world. Map coordinates are used.
     *
     * @param out   The string of text to display.
     * @param pt3d  The world position to draw text at.
     * @param color (Optional) Color of the text.
     * @param size  (Optional) Pixel height of the text.
     */
    DebugInterface debugTextOut(String out, Point pt3d, Color color, int size);

    /**
     * Outputs a line between two 3D points in the game world. Map coordinates are used.
     *
     * @param p0    The starting position of the line.
     * @param p1    The ending position of the line.
     * @param color (Optional) Color of the line.
     */
    DebugInterface debugLineOut(Point p0, Point p1, Color color);

    /**
     * Outputs a box specified as two 3D points in the game world. Map coordinates are used.
     *
     * @param p0    One corner of the box.
     * @param p1    The far corner of the box.
     * @param color (Optional) Color of the lines.
     */
    DebugInterface debugBoxOut(Point p0, Point p1, Color color);

    /**
     * Outputs a sphere specified as a 3D point in the game world and a radius. Map coordinates are used.
     *
     * @param p     Center of the sphere.
     * @param r     Radius of the sphere.
     * @param color (Optional) Color of the lines.
     */
    DebugInterface debugSphereOut(Point p, float r, Color color);

    // Cheats.

    /**
     * @see #debugCreateUnit(UnitType, Point2d, int, int)
     */
    DebugInterface debugCreateUnit(UnitType unitType, PointI p, int playerId, int count);

    /**
     * Creates a unit at the given position.
     *
     * @param unitType Type of unit to create.
     * @param p        Position to create the unit at.
     * @param playerId Player the unit should belong to.
     * @param count    Number of units to create.
     */
    DebugInterface debugCreateUnit(UnitType unitType, Point2d p, int playerId, int count);

    /**
     * Destroy a unit.
     *
     * @param unit Unit to destroy.
     */
    DebugInterface debugKillUnit(Unit unit);

    /**
     * Makes the entire map visible, i.e., removes the fog-of-war.
     */
    DebugInterface debugShowMap();

    /**
     * Enables commands to be issued to enemy units.
     */
    DebugInterface debugEnemyControl();

    /**
     * Disables the food check.
     */
    DebugInterface debugIgnoreFood();

    /**
     * Disables resource checks.
     */
    DebugInterface debugIgnoreResourceCost();

    /**
     * Gives a bunch of minerals and gas.
     */
    DebugInterface debugGiveAllResources();

    /**
     * Makes the units of a player indestructible.
     */
    DebugInterface debugGodMode();

    /**
     * Ignores mineral costs.
     */
    DebugInterface debugIgnoreMineral();

    /**
     * Cooldowns become instant.
     */
    DebugInterface debugNoCooldowns();

    /**
     * All tech becomes available.
     */
    DebugInterface debugGiveAllTech();

    /**
     * All upgrades are available.
     */
    DebugInterface debugGiveAllUpgrades();

    /**
     * Structures and units are built much faster.
     */
    DebugInterface debugFastBuild();

    /**
     * Sets the scripted "curriculum" score.
     */
    DebugInterface debugSetScore(float score);

    /**
     * Ends a game.
     *
     * @param victory If true, this player is victorious. If false, this player surrenders.
     */
    DebugInterface debugEndGame(boolean victory);

    /**
     * Sets the energy level on a unit.
     *
     * @param value The new energy level.
     * @param unit  The unit.
     */
    DebugInterface debugSetEnergy(float value, Unit unit);

    /**
     * Sets the life on a unit.
     *
     * @param value The new life.
     * @param unit  The unit.
     */
    DebugInterface debugSetLife(float value, Unit unit);

    /**
     * Sets shields on a unit.
     *
     * @param value The new shields.
     * @param unit  The unit.
     */
    DebugInterface debugSetShields(float value, Unit unit);

    /**
     * @see #debugMoveCamera(Point)
     */
    DebugInterface debugMoveCamera(PointI pos);

    /**
     * Sets the position of the camera.
     *
     * @param pos The camera position in world space.
     */
    DebugInterface debugMoveCamera(Point pos);


    /**
     * Cause the game to fail; useful to test library behavior.
     *
     * @param appTest State to put the game into.
     * @param delayMs Time to elapse before invoking the game state.
     */
    DebugInterface debugTestApp(DebugTestProcess.Test appTest, int delayMs);

    /**
     * Dispatch all queued debug commands. No debug commands will be sent until this is called.
     * This will also clear or set new debug primitives like text and lines.
     */
    boolean sendDebug();
}
