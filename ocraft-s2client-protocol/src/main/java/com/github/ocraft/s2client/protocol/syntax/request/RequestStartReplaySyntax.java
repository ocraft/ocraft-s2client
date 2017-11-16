package com.github.ocraft.s2client.protocol.syntax.request;

import java.nio.file.Path;

public interface RequestStartReplaySyntax {
    OverrideMapSyntax from(Path replayPath);

    OverrideMapSyntax from(byte[] replayDataInBytes);
}
