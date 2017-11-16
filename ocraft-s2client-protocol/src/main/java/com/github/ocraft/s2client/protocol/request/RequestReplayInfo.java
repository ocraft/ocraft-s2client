package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Immutables;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.request.DownloadSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.RequestReplayInfoSyntax;
import com.google.protobuf.ByteString;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class RequestReplayInfo extends Request {

    private static final long serialVersionUID = -651154978430188284L;

    private final String replayPath;
    private final byte[] replayDataInBytes;
    private final boolean downloadData;

    private RequestReplayInfo(Builder builder) {
        this.replayPath = builder.replayPath;
        this.replayDataInBytes = builder.replayDataInBytes;
        this.downloadData = builder.downloadData;
    }

    public static final class Builder implements RequestReplayInfoSyntax, DownloadSyntax {

        private String replayPath;
        private byte[] replayDataInBytes;
        private boolean downloadData;

        @Override
        public DownloadSyntax of(Path replayPath) {
            this.replayPath = replayPath.toString();
            this.replayDataInBytes = nothing();
            return this;
        }

        @Override
        public DownloadSyntax of(byte[] replayDataInBytes) {
            this.replayDataInBytes = replayDataInBytes;
            this.replayPath = nothing();
            return this;
        }

        @Override
        public BuilderSyntax<RequestReplayInfo> download() {
            this.downloadData = true;
            return this;
        }

        @Override
        public RequestReplayInfo build() {
            if (!oneOfReplayCaseIsSet()) {
                throw new IllegalArgumentException("replay case is required");
            }
            return new RequestReplayInfo(this);
        }

        private boolean oneOfReplayCaseIsSet() {
            return isSet(replayPath) || isSet(replayDataInBytes);
        }
    }

    public static RequestReplayInfoSyntax replayInfo() {
        return new Builder();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        Sc2Api.RequestReplayInfo.Builder aSc2ApiRequestReplayInfo = Sc2Api.RequestReplayInfo.newBuilder()
                .setDownloadData(downloadData);

        getReplayPath().map(Path::toString).ifPresent(aSc2ApiRequestReplayInfo::setReplayPath);
        getReplayDataInBytes().map(ByteString::copyFrom).ifPresent(aSc2ApiRequestReplayInfo::setReplayData);

        return Sc2Api.Request.newBuilder()
                .setReplayInfo(aSc2ApiRequestReplayInfo.build())
                .build();
    }

    public Optional<Path> getReplayPath() {
        return Optional.ofNullable(replayPath).map(Paths::get);
    }

    public Optional<byte[]> getReplayDataInBytes() {
        return Optional.ofNullable(replayDataInBytes).map(Immutables::copyOf);
    }

    public boolean isDownloadData() {
        return downloadData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestReplayInfo that = (RequestReplayInfo) o;

        return downloadData == that.downloadData &&
                (replayPath != null ? replayPath.equals(that.replayPath) : that.replayPath == null) &&
                Arrays.equals(replayDataInBytes, that.replayDataInBytes);
    }

    @Override
    public int hashCode() {
        int result = replayPath != null ? replayPath.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(replayDataInBytes);
        result = 31 * result + (downloadData ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
