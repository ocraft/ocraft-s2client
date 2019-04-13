package com.github.ocraft.s2client.protocol.request;

/*-
 * #%L
 * ocraft-s2client-protocol
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

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Immutables;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import com.github.ocraft.s2client.protocol.syntax.request.DownloadSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.RequestReplayInfoSyntax;
import com.google.protobuf.ByteString;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
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
        public BuilderSyntax<RequestReplayInfo> download(boolean value) {
            this.downloadData = value;
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

    @Override
    public ResponseType responseType() {
        return ResponseType.REPLAY_INFO;
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
        if (!super.equals(o)) return false;

        RequestReplayInfo that = (RequestReplayInfo) o;

        if (downloadData != that.downloadData) return false;
        if (!Objects.equals(replayPath, that.replayPath)) return false;
        return Arrays.equals(replayDataInBytes, that.replayDataInBytes);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (replayPath != null ? replayPath.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(replayDataInBytes);
        result = 31 * result + (downloadData ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
