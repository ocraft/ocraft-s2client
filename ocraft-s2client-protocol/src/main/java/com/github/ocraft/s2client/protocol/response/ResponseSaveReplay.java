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
package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import com.google.protobuf.ByteString;

import java.util.Arrays;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class ResponseSaveReplay extends Response {

    private static final long serialVersionUID = 142530134174377403L;

    private final byte[] data;

    private ResponseSaveReplay(Sc2Api.ResponseSaveReplay sc2ApiResponseSaveReplay, Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.SAVE_REPLAY, GameStatus.from(sc2ApiStatus));

        this.data = tryGet(
                Sc2Api.ResponseSaveReplay::getData, Sc2Api.ResponseSaveReplay::hasData
        ).apply(sc2ApiResponseSaveReplay).map(ByteString::toByteArray).orElseThrow(required("data"));
    }

    public static ResponseSaveReplay from(Sc2Api.Response sc2ApiResponse) {
        if (!hasSaveReplayResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have save replay response");
        }
        return new ResponseSaveReplay(sc2ApiResponse.getSaveReplay(), sc2ApiResponse.getStatus());
    }

    private static boolean hasSaveReplayResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasSaveReplay();
    }

    public byte[] getData() {
        byte[] dataToGet = new byte[data.length];
        System.arraycopy(data, 0, dataToGet, 0, data.length);
        return dataToGet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseSaveReplay)) return false;
        if (!super.equals(o)) return false;

        ResponseSaveReplay that = (ResponseSaveReplay) o;

        return that.canEqual(this) && Arrays.equals(data, that.data);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseSaveReplay;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
