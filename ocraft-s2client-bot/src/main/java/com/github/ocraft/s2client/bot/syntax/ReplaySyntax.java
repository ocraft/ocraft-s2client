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

import com.github.ocraft.s2client.bot.S2ReplayObserver;

import java.io.IOException;
import java.nio.file.Path;

public interface ReplaySyntax extends StartGameSyntax {
    ReplaySyntax setReplayRecovery(Boolean value);

    /**
     * Add an instance of S2ReplayObserver, each S2ReplayObserver will run a separate StarCraft II client.
     *
     * @param replayObserver A pointer to the replay observer to utilize.
     * @see S2ReplayObserver
     */
    ReplaySyntax addReplayObserver(S2ReplayObserver replayObserver);

    /**
     * Sets the path for to a folder of replays to analyze.
     *
     * @param path The folder path.
     */
    ReplaySyntax setReplayPath(Path path) throws IOException;

    /**
     * Loads replays from a file.
     *
     * @param path The file path.
     */
    ReplaySyntax loadReplayList(Path path) throws IOException;
}
