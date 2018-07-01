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

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;

public final class ResponsePing extends Response {

    private static final long serialVersionUID = -3820295964532004673L;

    private final String gameVersion;
    private final String dataVersion;
    private final Integer dataBuild;
    private final Integer baseBuild;

    private ResponsePing(Sc2Api.ResponsePing sc2ApiResponsePing, Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.PING, GameStatus.from(sc2ApiStatus));

        this.gameVersion = tryGet(
                Sc2Api.ResponsePing::getGameVersion, Sc2Api.ResponsePing::hasGameVersion
        ).apply(sc2ApiResponsePing).orElseThrow(required("game version"));

        this.dataVersion = tryGet(
                Sc2Api.ResponsePing::getDataVersion, Sc2Api.ResponsePing::hasDataVersion
        ).apply(sc2ApiResponsePing).orElseThrow(required("data version"));

        this.dataBuild = tryGet(
                Sc2Api.ResponsePing::getDataBuild, Sc2Api.ResponsePing::hasDataBuild
        ).apply(sc2ApiResponsePing).orElseThrow(required("data build"));

        this.baseBuild = tryGet(
                Sc2Api.ResponsePing::getBaseBuild, Sc2Api.ResponsePing::hasBaseBuild
        ).apply(sc2ApiResponsePing).orElseThrow(required("base build"));
    }

    public static ResponsePing from(Sc2Api.Response sc2ApiResponse) {
        if (!hasPingResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have ping response");
        }
        return new ResponsePing(sc2ApiResponse.getPing(), sc2ApiResponse.getStatus());
    }

    private static boolean hasPingResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasPing();
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public String getDataVersion() {
        return dataVersion;
    }

    public Integer getDataBuild() {
        return dataBuild;
    }

    public Integer getBaseBuild() {
        return baseBuild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponsePing)) return false;
        if (!super.equals(o)) return false;

        ResponsePing that = (ResponsePing) o;

        return that.canEqual(this) &&
                gameVersion.equals(that.gameVersion) &&
                dataVersion.equals(that.dataVersion) &&
                dataBuild.equals(that.dataBuild) &&
                baseBuild.equals(that.baseBuild);
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponsePing;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + gameVersion.hashCode();
        result = 31 * result + dataVersion.hashCode();
        result = 31 * result + dataBuild.hashCode();
        result = 31 * result + baseBuild.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
