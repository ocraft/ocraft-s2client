package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.request.RequestReplayInfo;

public interface DownloadSyntax extends BuilderSyntax<RequestReplayInfo> {
    BuilderSyntax<RequestReplayInfo> download();
}
