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

import com.github.ocraft.s2client.bot.gateway.ControlInterface;
import com.github.ocraft.s2client.bot.gateway.DebugInterface;
import com.github.ocraft.s2client.bot.gateway.ObservationInterface;
import com.github.ocraft.s2client.bot.gateway.QueryInterface;
import com.github.ocraft.s2client.bot.gateway.impl.GatewayFactory;

abstract class Client implements ClientEvents {

    private ControlInterface controlInterface = GatewayFactory.control(this);

    /**
     * The ObservationInterface is used to query game state.
     */
    public ObservationInterface observation() {
        return controlInterface.observation();
    }

    /**
     * The QueryInterface interface is used to issue commands to units.
     */
    public QueryInterface query() {
        return control().query();
    }

    /**
     * The DebugInterface allows a derived class to print text, draw primitive shapes and spawn/destroy units.
     */
    public DebugInterface debug() {
        return control().debug();
    }

    /**
     * The ControlInterface is only meant to be used by the coordinator as it provides functionality for connecting
     * to Starcraft2, setting up a websocket connection and issuing blocking commands via SC2's protocol.
     */
    public ControlInterface control() {
        return controlInterface;
    }

    void reset() {
        // TODO p.picheta to test
        control().quit();
        controlInterface = GatewayFactory.control(this);
        onReset();
    }

    void onReset() {

    }
}
