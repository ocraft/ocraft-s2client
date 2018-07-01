package com.github.ocraft.s2client.protocol.game;

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

import java.io.Serializable;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toSet;

public final class ReplayInfo implements Serializable {

    private static final long serialVersionUID = -632231702723497L;

    private final BattlenetMap battlenetMap;
    private final LocalMap localMap;
    private final Set<PlayerInfoExtra> playerInfo;
    private final int gameDurationLoops;
    private final float gameDurationSeconds;
    private final String gameVersion;
    private final String dataVersion;
    private final int baseBuild;
    private final int dataBuild;

    private ReplayInfo(Sc2Api.ResponseReplayInfo sc2ApiResponseReplayInfo) {
        this.battlenetMap = tryGet(
                Sc2Api.ResponseReplayInfo::getMapName, Sc2Api.ResponseReplayInfo::hasMapName
        ).apply(sc2ApiResponseReplayInfo).map(BattlenetMap::of).orElse(nothing());

        this.localMap = tryGet(
                Sc2Api.ResponseReplayInfo::getLocalMapPath, Sc2Api.ResponseReplayInfo::hasLocalMapPath
        ).apply(sc2ApiResponseReplayInfo).map(Paths::get).map(LocalMap::of).orElse(nothing());

        if (!mapInfoIsSet()) throw new IllegalArgumentException("map info (local or battlenet) is required");

        this.playerInfo = sc2ApiResponseReplayInfo.getPlayerInfoList().stream()
                .map(PlayerInfoExtra::from).collect(toSet());

        this.gameDurationLoops = tryGet(
                Sc2Api.ResponseReplayInfo::getGameDurationLoops, Sc2Api.ResponseReplayInfo::hasGameDurationLoops
        ).apply(sc2ApiResponseReplayInfo).orElseThrow(required("game duration loops"));

        this.gameDurationSeconds = tryGet(
                Sc2Api.ResponseReplayInfo::getGameDurationSeconds, Sc2Api.ResponseReplayInfo::hasGameDurationSeconds
        ).apply(sc2ApiResponseReplayInfo).orElseThrow(required("game duration seconds"));

        this.gameVersion = tryGet(
                Sc2Api.ResponseReplayInfo::getGameVersion, Sc2Api.ResponseReplayInfo::hasGameVersion
        ).apply(sc2ApiResponseReplayInfo).orElseThrow(required("game version"));

        this.dataVersion = tryGet(
                Sc2Api.ResponseReplayInfo::getDataVersion, Sc2Api.ResponseReplayInfo::hasDataVersion
        ).apply(sc2ApiResponseReplayInfo).orElseThrow(required("data version"));

        this.dataBuild = tryGet(
                Sc2Api.ResponseReplayInfo::getDataBuild, Sc2Api.ResponseReplayInfo::hasDataBuild
        ).apply(sc2ApiResponseReplayInfo).orElseThrow(required("data build"));

        this.baseBuild = tryGet(
                Sc2Api.ResponseReplayInfo::getBaseBuild, Sc2Api.ResponseReplayInfo::hasBaseBuild
        ).apply(sc2ApiResponseReplayInfo).orElseThrow(required("base build"));
    }

    public static ReplayInfo from(Sc2Api.ResponseReplayInfo sc2ApiResponseReplayInfo) {
        require("sc2api response replay info", sc2ApiResponseReplayInfo);
        return new ReplayInfo(sc2ApiResponseReplayInfo);
    }


    private boolean mapInfoIsSet() {
        return isSet(localMap) || isSet(battlenetMap);
    }

    public Optional<BattlenetMap> getBattlenetMap() {
        return Optional.ofNullable(battlenetMap);
    }

    public Optional<LocalMap> getLocalMap() {
        return Optional.ofNullable(localMap);
    }

    public Set<PlayerInfoExtra> getPlayerInfo() {
        return new HashSet<>(playerInfo);
    }

    public int getGameDurationLoops() {
        return gameDurationLoops;
    }

    public float getGameDurationSeconds() {
        return gameDurationSeconds;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public String getDataVersion() {
        return dataVersion;
    }

    public int getBaseBuild() {
        return baseBuild;
    }

    public int getDataBuild() {
        return dataBuild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReplayInfo that = (ReplayInfo) o;

        return gameDurationLoops == that.gameDurationLoops &&
                Float.compare(that.gameDurationSeconds, gameDurationSeconds) == 0 &&
                baseBuild == that.baseBuild &&
                dataBuild == that.dataBuild &&
                (battlenetMap != null ? battlenetMap.equals(that.battlenetMap) : that.battlenetMap == null) &&
                (localMap != null ? localMap.equals(that.localMap) : that.localMap == null) &&
                playerInfo.equals(that.playerInfo) &&
                gameVersion.equals(that.gameVersion) &&
                dataVersion.equals(that.dataVersion);
    }

    @Override
    public int hashCode() {
        int result = battlenetMap != null ? battlenetMap.hashCode() : 0;
        result = 31 * result + (localMap != null ? localMap.hashCode() : 0);
        result = 31 * result + playerInfo.hashCode();
        result = 31 * result + gameDurationLoops;
        result = 31 * result + (gameDurationSeconds != +0.0f ? Float.floatToIntBits(gameDurationSeconds) : 0);
        result = 31 * result + gameVersion.hashCode();
        result = 31 * result + dataVersion.hashCode();
        result = 31 * result + baseBuild;
        result = 31 * result + dataBuild;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
