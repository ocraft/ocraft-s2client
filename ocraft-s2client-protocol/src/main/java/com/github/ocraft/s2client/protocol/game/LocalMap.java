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
package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.google.protobuf.ByteString;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.lang.String.format;

public final class LocalMap implements Sc2ApiSerializable<Sc2Api.LocalMap> {

    private static final long serialVersionUID = -4260546580710244708L;

    private static final int PATH_MAX_LENGTH = 260;

    private final String path;
    private final byte[] dataInBytes;

    private LocalMap(Path path) {
        this.path = path.toString();
        this.dataInBytes = null;
    }

    private LocalMap(byte[] dataInBytes) {
        this.dataInBytes = dataInBytes;
        this.path = null;
    }

    private LocalMap(Path path, byte[] dataInBytes) {
        this.path = path.toString();
        this.dataInBytes = dataInBytes;
    }

    public static LocalMap of(Path localMapPath, byte[] localMapDataInBytes) {
        validatePath(localMapPath);
        validateDataInBytes(localMapDataInBytes);
        return new LocalMap(localMapPath, localMapDataInBytes);
    }

    private static void validatePath(Path localMapPath) {
        require("path", localMapPath);
        if (lengthOf(localMapPath) > PATH_MAX_LENGTH) {
            throw new IllegalArgumentException(format(
                    "Maximum length of path (%d) exceeded. Actual was %d.", PATH_MAX_LENGTH, lengthOf(localMapPath)));
        }
    }

    private static int lengthOf(Path localMapPath) {
        return localMapPath.toString().length();
    }

    private static void validateDataInBytes(byte[] localMapDataInBytes) {
        require("data in bytes", localMapDataInBytes);
    }

    public static LocalMap of(Path localMapPath) {
        validatePath(localMapPath);
        return new LocalMap(localMapPath);
    }

    public static LocalMap of(byte[] localMapDataInBytes) {
        validateDataInBytes(localMapDataInBytes);
        return new LocalMap(localMapDataInBytes);
    }

    @Override
    public Sc2Api.LocalMap toSc2Api() {
        Sc2Api.LocalMap.Builder localMapBuilder = Sc2Api.LocalMap.newBuilder();

        getPath().map(Path::toString).ifPresent(localMapBuilder::setMapPath);
        getDataInBytes().map(ByteString::copyFrom).ifPresent(localMapBuilder::setMapData);

        return localMapBuilder.build();
    }

    public Optional<Path> getPath() {
        return Optional.ofNullable(path).map(Paths::get);
    }

    public Optional<byte[]> getDataInBytes() {
        return Optional.ofNullable(dataInBytes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalMap localMap = (LocalMap) o;

        return (path != null ? path.equals(localMap.path) : localMap.path == null) &&
                Arrays.equals(dataInBytes, localMap.dataInBytes);
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(dataInBytes);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
