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

import com.github.ocraft.s2client.bot.gateway.ActionFeatureLayerInterface;
import com.github.ocraft.s2client.bot.gateway.ActionInterface;
import com.github.ocraft.s2client.bot.gateway.AgentControlInterface;

/**
 * The base class for user defined bots.
 */
public abstract class S2Agent extends Client {

    private AgentControlInterface agentControlInterface = control().agentControl();

    /**
     * Interface for issuing actions to units. Actions should be batched via the UnitCommand functions
     * then eventually dispatched with SendActions. If you are stepping the simulation yourself the Step
     * will automatically call SendActions. If your bot is running in real time you must call SendActions yourself.
     *
     * @return The raw (basic) action interface.
     */
    public ActionInterface actions() {
        return agentControlInterface.action();
    }

    /**
     * Interface for issuing actions in feature layers.
     *
     * @return The feature layer action interface.
     */
    public ActionFeatureLayerInterface actionsFeatureLayer() {
        return agentControlInterface.actionsFeatureLayer();
    }

    /**
     * The AgentControlInterface is only currently used for restarting a game.
     * For internal use.
     *
     * @return The agent control interface.
     */
    public AgentControlInterface agentControl() {
        return agentControlInterface;
    }

}
