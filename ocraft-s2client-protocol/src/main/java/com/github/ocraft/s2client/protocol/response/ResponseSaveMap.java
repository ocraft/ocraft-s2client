package com.github.ocraft.s2client.protocol.response;

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
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ResponseSaveMap extends Response {

    private static final long serialVersionUID = -6627307761449061828L;

    private final Error error;

    public enum Error {
        INVALID_MAP_DATA;

        public static Error from(Sc2Api.ResponseSaveMap.Error sc2ApiResponseSaveMapError) {
            require("sc2api response save map error", sc2ApiResponseSaveMapError);
            switch (sc2ApiResponseSaveMapError) {
                case InvalidMapData:
                    return INVALID_MAP_DATA;
                default:
                    throw new AssertionError("unknown sc2api save map error: " + sc2ApiResponseSaveMapError);
            }
        }
    }

    private ResponseSaveMap(Sc2Api.ResponseSaveMap sc2ApiResponseSaveMap, Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.SAVE_MAP, GameStatus.from(sc2ApiStatus));

        this.error = tryGet(
                Sc2Api.ResponseSaveMap::getError, Sc2Api.ResponseSaveMap::hasError
        ).apply(sc2ApiResponseSaveMap).map(ResponseSaveMap.Error::from).orElse(nothing());
    }

    public static ResponseSaveMap from(Sc2Api.Response sc2ApiResponse) {
        if (!hasSaveMapResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have save map response");
        }
        return new ResponseSaveMap(sc2ApiResponse.getSaveMap(), sc2ApiResponse.getStatus());
    }

    private static boolean hasSaveMapResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasSaveMap();
    }

    public Optional<Error> getError() {
        return Optional.ofNullable(error);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResponseSaveMap that = (ResponseSaveMap) o;

        return that.canEqual(this) && error == that.error;
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseSaveMap;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
