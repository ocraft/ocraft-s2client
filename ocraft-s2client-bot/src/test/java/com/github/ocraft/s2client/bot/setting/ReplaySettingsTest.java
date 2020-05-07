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

import com.github.ocraft.s2client.test.TemporaryFolder;
import com.github.ocraft.s2client.test.TemporaryFolderExtension;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(TemporaryFolderExtension.class)
class ReplaySettingsTest {

    private static final Path REPLAYS_DIR_PATH = Paths.get("replays");
    private static final Path REPLAY_PATH_01 = Paths.get("tmp01.SC2Replay");
    private static final Path REPLAY_PATH_02 = Paths.get("tmp02.SC2Replay");
    private static final Path REPLAY_PATH_03 = Paths.get("tmp03.SC2Replay");

    private TemporaryFolder replayDir;

    @Test
    void throwsExceptionIfEmptyPathIsProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ReplaySettings().setReplayPath(null))
                .withMessage("replay path is required");
    }

    @Test
    void storesReplayFileThatWasProvided() throws IOException {
        assertThat(new ReplaySettings().setReplayPath(REPLAY_PATH_01).getReplayFiles())
                .containsExactly(REPLAY_PATH_01.toAbsolutePath());
    }

    @Test
    void clearsOldReplayFilesBeforeAddingNew() throws IOException {
        ReplaySettings replaySettings = new ReplaySettings().setReplayPath(REPLAY_PATH_01);

        assertThat(replaySettings.setReplayPath(REPLAY_PATH_02).getReplayFiles())
                .containsExactly(REPLAY_PATH_02.toAbsolutePath());
    }

    @Test
    void loadsAllReplayFilesFromDirectory() throws IOException {
        replayDir.newFile(REPLAYS_DIR_PATH, REPLAY_PATH_01.getFileName().toString());
        replayDir.newFile(REPLAYS_DIR_PATH, REPLAY_PATH_02.getFileName().toString());
        Path replays = replayDir.getRootFolder().resolve(REPLAYS_DIR_PATH);

        assertThat(new ReplaySettings().setReplayPath(replays).getReplayFiles())
                .containsExactlyInAnyOrder(replays.resolve(REPLAY_PATH_01), replays.resolve(REPLAY_PATH_02));
    }

    @Test
    void throwsExceptionIfEmptyFileIsProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ReplaySettings().loadReplayList(null))
                .withMessage("file with replay list is required");
    }

    @Test
    void loadsReplayListFromFile() throws IOException {
        Path replayList = prepareReplayFileList();

        assertThat(new ReplaySettings().loadReplayList(replayList).getReplayFiles())
                .containsExactly(REPLAY_PATH_01.toAbsolutePath(), REPLAY_PATH_02.toAbsolutePath());
    }

    private Path prepareReplayFileList() throws IOException {
        Path replayList = replayDir.newFile(Paths.get(""), "replays.txt");
        Files.write(
                replayList,
                List.of(REPLAY_PATH_01.toAbsolutePath().toString(), REPLAY_PATH_02.toAbsolutePath().toString()));
        return replayList;
    }

    @Test
    void clearsOldReplayFilesBeforeLoadingNew() throws IOException {
        ReplaySettings replaySettings = new ReplaySettings().setReplayPath(REPLAY_PATH_03);
        Path replayList = prepareReplayFileList();

        assertThat(replaySettings.loadReplayList(replayList).getReplayFiles())
                .containsExactly(REPLAY_PATH_01.toAbsolutePath(), REPLAY_PATH_02.toAbsolutePath());
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ReplaySettings.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withNonnullFields("replayFiles")
                .verify();
    }

}
