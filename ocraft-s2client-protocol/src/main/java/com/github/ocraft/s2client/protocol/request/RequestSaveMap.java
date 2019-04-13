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
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.response.ResponseType;
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

    @Override
    public ResponseType responseType() {
        return ResponseType.SAVE_MAP;
    }

    public LocalMap getMap() {
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RequestSaveMap that = (RequestSaveMap) o;

        return map.equals(that.map);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + map.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
