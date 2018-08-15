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

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public final class OcraftBotConfig {
    public static final String ROOT = "ocraft";
    public static final String BOT = ROOT + ".bot";
    public static final String BOT_MULTITHREADED = BOT + ".multithreaded";
    public static final String BOT_REALTIME = BOT + ".realtime";
    public static final String BOT_STEP_SIZE = BOT + ".stepSize";
    public static final String BOT_MAP = BOT + ".map";
    public static final String BOT_REPLAY_RECOVERY = BOT + ".replayRecovery";
    public static final String BOT_TRACED = BOT + ".traced";

    private static Config config = ConfigFactory.load();

    private OcraftBotConfig() {
        throw new AssertionError("private constructor");
    }

    public static Config cfg() {
        return config;
    }

    static void inject(Config config) {
        com.github.ocraft.s2client.bot.OcraftBotConfig.config = config;
    }
}
