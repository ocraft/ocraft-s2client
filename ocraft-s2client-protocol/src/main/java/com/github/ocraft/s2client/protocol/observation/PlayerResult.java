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
package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class PlayerResult implements Serializable {

    private static final long serialVersionUID = -9025162774544968860L;

    private final int playerId;
    private final Result result;

    private PlayerResult(Sc2Api.PlayerResult sc2ApiPlayerResult) {
        this.playerId = tryGet(
                Sc2Api.PlayerResult::getPlayerId, Sc2Api.PlayerResult::hasPlayerId
        ).apply(sc2ApiPlayerResult).orElseThrow(required("player id"));

        this.result = tryGet(
                Sc2Api.PlayerResult::getResult, Sc2Api.PlayerResult::hasResult
        ).apply(sc2ApiPlayerResult).map(Result::from).orElseThrow(required("result"));
    }

    public static PlayerResult from(Sc2Api.PlayerResult sc2ApiPlayerResult) {
        require("sc2api player result", sc2ApiPlayerResult);
        return new PlayerResult(sc2ApiPlayerResult);
    }

    public int getPlayerId() {
        return playerId;
    }

    public Result getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerResult)) return false;

        PlayerResult that = (PlayerResult) o;

        return playerId == that.playerId && result == that.result;
    }

    @Override
    public int hashCode() {
        int result1 = playerId;
        result1 = 31 * result1 + result.hashCode();
        return result1;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
