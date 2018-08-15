package com.github.ocraft.s2client.bot.setting;

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

import com.github.ocraft.s2client.bot.OcraftBotConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ReplaySettings {

    private static final String SC2_REPLAY_EXTENSION = ".SC2Replay";

    private boolean replayRecovery = OcraftBotConfig.cfg().getBoolean(OcraftBotConfig.BOT_REPLAY_RECOVERY);
    private final List<Path> replayFiles = new ArrayList<>();

    public ReplaySettings setReplayRecovery(boolean replayRecovery) {
        this.replayRecovery = replayRecovery;
        return this;
    }

    public boolean isReplayRecovery() {
        return replayRecovery;
    }

    public ReplaySettings setReplayPath(Path replayPath) throws IOException {
        require("replay path", replayPath);
        replayFiles.clear();
        if (replayPath.toFile().isDirectory()) {
            try (Stream<Path> files = Files.walk(replayPath)) {
                replayFiles.addAll(files.filter(p -> !Files.isDirectory(p)).collect(Collectors.toList()));
            }
        } else if (replayPath.toString().endsWith(SC2_REPLAY_EXTENSION)) {
            replayFiles.add(replayPath.toAbsolutePath());
        }
        return this;
    }

    public ReplaySettings loadReplayList(Path path) throws IOException {
        require("file with replay list", path);
        replayFiles.clear();
        try (Stream<String> lines = Files.lines(path)) {
            replayFiles.addAll(lines.map(Paths::get).collect(Collectors.toList()));
        }
        return this;
    }

    public List<Path> getReplayFiles() {
        return replayFiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReplaySettings that = (ReplaySettings) o;

        if (replayRecovery != that.replayRecovery) return false;
        return replayFiles.equals(that.replayFiles);
    }

    @Override
    public int hashCode() {
        int result = (replayRecovery ? 1 : 0);
        result = 31 * result + replayFiles.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ReplaySettings{" +
                "replayRecovery=" + replayRecovery +
                ", replayFiles=" + replayFiles +
                '}';
    }
}
