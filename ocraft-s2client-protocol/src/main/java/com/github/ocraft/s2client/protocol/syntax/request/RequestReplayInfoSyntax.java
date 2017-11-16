package com.github.ocraft.s2client.protocol.syntax.request;

import java.nio.file.Path;

public interface RequestReplayInfoSyntax {
    DownloadSyntax of(Path replayPath);

    DownloadSyntax of(byte[] replayDataInBytes);
}
