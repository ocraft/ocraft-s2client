package com.github.ocraft.s2client.bot;

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

import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.data.Upgrade;
import com.github.ocraft.s2client.protocol.observation.Alert;

import java.util.List;

public interface ClientEvents {

    /**
     * Called when a game is started after a load. Fast restarting will not call this.
     */
    default void onGameFullStart() {
    }

    /**
     * Called when a game is started or restarted.
     */
    default void onGameStart() {
    }

    /**
     * Called when a game has ended.
     */
    default void onGameEnd() {
    }

    /**
     * In non realtime games this function gets called after each step as indicated by step size.
     * In realtime this function gets called as often as possible after request/responses are received from the game
     * gathering observation state.
     */
    default void onStep() {
    }

    /**
     * Called whenever one of the player's units has been destroyed.
     *
     * @param unitInPool The destroyed unit.
     */
    default void onUnitDestroyed(UnitInPool unitInPool) {
    }

    /**
     * Called when a Unit has been created by the player.
     *
     * @param unitInPool The created unit.
     */
    default void onUnitCreated(UnitInPool unitInPool) {
    }

    /**
     * Called when a unit becomes idle, this will only occur as an event so will only be called when the unit becomes
     * idle and not a second time. Being idle is defined by having orders in the previous step and not currently having
     * orders or if it did not exist in the previous step and now does, a unit being created, for instance, will call
     * both onUnitCreated and onUnitIdle if it does not have a rally set.
     *
     * @param unitInPool The idle unit.
     */
    default void onUnitIdle(UnitInPool unitInPool) {
    }

    /**
     * Called when an upgrade is finished, warp gate, ground weapons, baneling speed, etc.
     *
     * @param upgrade The completed upgrade.
     */
    default void onUpgradeCompleted(Upgrade upgrade) {
    }

    /**
     * Called when the unit in the previous step had a build progress less than 1.0 but is greater than or equal to
     * 1.0 in the current step.
     *
     * @param unitInPool The constructed unit.
     */
    default void onBuildingConstructionComplete(UnitInPool unitInPool) {
    }

    /**
     * Called when a nydus is placed.
     */
    default void onNydusDetected() {
    }

    /**
     * Called when a nuclear launch is detected.
     */
    default void onNuclearLaunchDetected() {
    }

    /**
     * Called when an enemy unit enters vision from out of fog of war.
     *
     * @param unitInPool The unit entering vision.
     */
    default void onUnitEnterVision(UnitInPool unitInPool) {
    }

    /**
     * Called for various errors the library can encounter. See ClientError enum for possible errors.
     */
    default void onError(List<ClientError> clientErrors, List<String> protocolErrors) {
    }

    /**
     * Called on alert.
     * WARNING: for NYDUS_DETECTED and NUCLEAR_LAUNCH_DETECTED alert there are defined separate client events.
     *
     * @see #onNydusDetected()
     * @see #onNuclearLaunchDetected()
     */
    default void onAlert(Alert alert) {
    }
}
