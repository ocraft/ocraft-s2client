/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Immutables;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;
import com.github.ocraft.s2client.protocol.syntax.request.*;
import com.google.protobuf.ByteString;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Preconditions.oneOfIsSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class RequestStartReplay extends Request {

    private static final long serialVersionUID = 7586245376525770910L;

    private final String replayPath;
    private final byte[] replayDataInBytes;
    private final byte[] mapDataInBytes;
    private final InterfaceOptions interfaceOptions;
    private final int observedPlayerId;
    private final boolean disableFog;
    // since 4.0
    private final boolean realtime;

    private RequestStartReplay(Builder builder) {
        this.replayPath = builder.replayPath;
        this.replayDataInBytes = builder.replayDataInBytes;
        this.mapDataInBytes = builder.mapDataInBytes;
        this.interfaceOptions = builder.interfaceOptions;
        this.observedPlayerId = builder.observedPlayerId;
        this.disableFog = builder.disableFog;
        this.realtime = builder.realtime;
    }

    public static final class Builder implements RequestStartReplaySyntax, OverrideMapSyntax, ToObserveSyntax,
            ReplayDisableFogSyntax, ReplayRealtimeSyntax {

        private String replayPath;
        private byte[] replayDataInBytes;
        private byte[] mapDataInBytes;
        private InterfaceOptions interfaceOptions;
        private Integer observedPlayerId;
        private boolean disableFog;
        private boolean realtime;

        @Override
        public OverrideMapSyntax from(Path replayPath) {
            this.replayPath = replayPath.toString();
            this.replayDataInBytes = nothing();
            return this;
        }

        @Override
        public OverrideMapSyntax from(byte[] replayDataInBytes) {
            this.replayDataInBytes = replayDataInBytes;
            this.replayPath = nothing();
            return this;
        }

        @Override
        public ReplayUseInterfaceSyntax overrideMapData(byte[] mapDataInBytes) {
            this.mapDataInBytes = mapDataInBytes;
            return this;
        }

        @Override
        public ToObserveSyntax use(InterfaceOptions interfaceOptions) {
            this.interfaceOptions = interfaceOptions;
            return this;
        }

        @Override
        public ToObserveSyntax use(BuilderSyntax<InterfaceOptions> interfaceOptions) {
            return use(interfaceOptions.build());
        }

        @Override
        public ReplayDisableFogSyntax toObserve(int playerId) {
            this.observedPlayerId = playerId;
            return this;
        }

        @Override
        public ReplayRealtimeSyntax disableFog() {
            this.disableFog = true;
            return this;
        }

        @Override
        public BuilderSyntax<RequestStartReplay> realtime() {
            this.realtime = true;
            return this;
        }

        @Override
        public RequestStartReplay build() {
            oneOfIsSet("replay case", replayDataInBytes, replayPath);
            require("interface option", interfaceOptions);
            require("observed player", observedPlayerId);
            return new RequestStartReplay(this);
        }
    }

    public static RequestStartReplaySyntax startReplay() {
        return new Builder();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        Sc2Api.RequestStartReplay.Builder aSc2ApiRequestStartReplay = Sc2Api.RequestStartReplay.newBuilder()
                .setOptions(interfaceOptions.toSc2Api())
                .setObservedPlayerId(observedPlayerId)
                .setDisableFog(disableFog)
                .setRealtime(realtime);

        getReplayPath().map(Path::toString).ifPresent(aSc2ApiRequestStartReplay::setReplayPath);
        getReplayDataInBytes().map(ByteString::copyFrom).ifPresent(aSc2ApiRequestStartReplay::setReplayData);
        getMapDataInBytes().map(ByteString::copyFrom).ifPresent(aSc2ApiRequestStartReplay::setMapData);

        return Sc2Api.Request.newBuilder()
                .setStartReplay(aSc2ApiRequestStartReplay.build())
                .build();
    }

    public Optional<Path> getReplayPath() {
        return Optional.ofNullable(replayPath).map(Paths::get);
    }

    public Optional<byte[]> getReplayDataInBytes() {
        return Optional.ofNullable(replayDataInBytes).map(Immutables::copyOf);
    }

    public Optional<byte[]> getMapDataInBytes() {
        return Optional.ofNullable(mapDataInBytes).map(Immutables::copyOf);
    }

    public InterfaceOptions getInterfaceOptions() {
        return interfaceOptions;
    }

    public int getObservedPlayerId() {
        return observedPlayerId;
    }

    public boolean isDisableFog() {
        return disableFog;
    }

    public boolean isRealtime() {
        return realtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestStartReplay that = (RequestStartReplay) o;

        return observedPlayerId == that.observedPlayerId &&
                disableFog == that.disableFog &&
                realtime == that.realtime &&
                (replayPath != null ? replayPath.equals(that.replayPath) : that.replayPath == null) &&
                Arrays.equals(replayDataInBytes, that.replayDataInBytes) &&
                Arrays.equals(mapDataInBytes, that.mapDataInBytes) &&
                interfaceOptions.equals(that.interfaceOptions);
    }

    @Override
    public int hashCode() {
        int result = replayPath != null ? replayPath.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(replayDataInBytes);
        result = 31 * result + Arrays.hashCode(mapDataInBytes);
        result = 31 * result + interfaceOptions.hashCode();
        result = 31 * result + observedPlayerId;
        result = 31 * result + (disableFog ? 1 : 0);
        result = 31 * result + (realtime ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
