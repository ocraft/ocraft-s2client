package com.github.ocraft.s2client.bot.syntax;

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

import com.github.ocraft.s2client.bot.S2Coordinator;

public interface StartGameSyntax {

    /**
     * Uses settings gathered from LoadSettings, specifically the path to the executable, to run StarCraft II.
     * Uses the PortStart to select a port for the game to listen on.
     */
    S2Coordinator launchStarcraft();

    /**
     * Uses settings gathered from LoadSettings, specifically the path to the executable, to run StarCraft II.
     * Starts the game listening on a specific port.
     */
    S2Coordinator launchStarcraft(Integer port);

    /**
     * Attaches to a running Starcraft.
     */
    S2Coordinator connect(String ip, Integer port);

    /**
     * Attaches to a ladder server;
     */
    S2Coordinator connectToLadder();
}
