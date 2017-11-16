package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.google.protobuf.ByteString;

import java.nio.file.Path;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class RequestSaveMap extends Request {

    private static final long serialVersionUID = -7758280351468974091L;

    private final LocalMap map; // Path the game process will write to, relative to the temp directory.

    public static final class Builder implements BuilderSyntax<RequestSaveMap> {

        private LocalMap map;

        public Builder to(LocalMap map) {
            this.map = map;
            return this;
        }

        @Override
        public RequestSaveMap build() {
            require("map", map);
            return new RequestSaveMap(this);
        }
    }

    private RequestSaveMap(Builder builder) {
        this.map = builder.map;
    }

    public static Builder saveMap() {
        return new Builder();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        Sc2Api.RequestSaveMap.Builder aSc2ApiSaveMap = Sc2Api.RequestSaveMap.newBuilder();

        map.getDataInBytes().map(ByteString::copyFrom).ifPresent(aSc2ApiSaveMap::setMapData);
        map.getPath().map(Path::toString).ifPresent(aSc2ApiSaveMap::setMapPath);

        return Sc2Api.Request.newBuilder()
                .setSaveMap(aSc2ApiSaveMap.build())
                .build();
    }

    public LocalMap getMap() {
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestSaveMap that = (RequestSaveMap) o;

        return map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
