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

import com.github.ocraft.s2client.bot.gateway.ObserverActionInterface;
import com.github.ocraft.s2client.bot.gateway.ReplayControlInterface;
import com.github.ocraft.s2client.protocol.game.ReplayInfo;

public class S2ReplayObserver extends Client {

    private static final float MINIMUM_REPLAY_DURATION = 30.0f;

    private ReplayControlInterface replayControlInterface = control().replayControl(this);

    /**
     * Obtains the replay control interface.
     *
     * @return The replay control interface.
     */
    ReplayControlInterface replayControl() {
        return replayControlInterface;
    }

    /**
     * Obtains the observer action interface.
     *
     * @return The observer action interface.
     */
    ObserverActionInterface observerAction() {
        return control().observerAction();
    }

    /**
     * Determines if the replay should be filtered out.
     *
     * @param replayInfo Replay information used to decide if the replay should be filtered.
     * @return If 'true', the replay will be rejected and not analyzed.
     */
    boolean ignoreReplay(ReplayInfo replayInfo, int playerId) {
        return replayInfo.getGameDurationSeconds() < MINIMUM_REPLAY_DURATION;
    }

    @Override
    void onReset() {
        replayControlInterface = control().replayControl(this);
    }
}
