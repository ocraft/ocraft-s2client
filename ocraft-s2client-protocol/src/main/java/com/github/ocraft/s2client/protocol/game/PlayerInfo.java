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
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class PlayerInfo implements Serializable {

    private static final long serialVersionUID = 7579487844984509366L;

    private final int playerId;
    private final PlayerType playerType;
    private final Race requestedRace;
    private final Race actualRace;
    private final Difficulty difficulty;

    private PlayerInfo(Sc2Api.PlayerInfo sc2ApiPlayerInfo) {
        this.playerId = tryGet(
                Sc2Api.PlayerInfo::getPlayerId, Sc2Api.PlayerInfo::hasPlayerId
        ).apply(sc2ApiPlayerInfo).orElseThrow(required("player id"));

        this.playerType = tryGet(
                Sc2Api.PlayerInfo::getType, Sc2Api.PlayerInfo::hasType
        ).apply(sc2ApiPlayerInfo).map(PlayerType::from).orElse(nothing());

        this.requestedRace = tryGet(
                Sc2Api.PlayerInfo::getRaceRequested, Sc2Api.PlayerInfo::hasRaceRequested
        ).apply(sc2ApiPlayerInfo).map(Race::from).orElseThrow(required("requested race"));

        this.actualRace = tryGet(
                Sc2Api.PlayerInfo::getRaceActual, Sc2Api.PlayerInfo::hasRaceActual
        ).apply(sc2ApiPlayerInfo).map(Race::from).orElse(nothing());

        this.difficulty = tryGet(
                Sc2Api.PlayerInfo::getDifficulty, Sc2Api.PlayerInfo::hasDifficulty
        ).apply(sc2ApiPlayerInfo).map(Difficulty::from).orElse(nothing());
    }

    public static PlayerInfo from(Sc2Api.PlayerInfo sc2ApiPlayerInfo) {
        require("sc2api player info", sc2ApiPlayerInfo);
        return new PlayerInfo(sc2ApiPlayerInfo);
    }

    public int getPlayerId() {
        return playerId;
    }

    public Optional<PlayerType> getPlayerType() {
        return Optional.ofNullable(playerType);
    }

    public Race getRequestedRace() {
        return requestedRace;
    }

    public Optional<Race> getActualRace() {
        return Optional.ofNullable(actualRace);
    }

    public Optional<Difficulty> getDifficulty() {
        return Optional.ofNullable(difficulty);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerInfo)) return false;

        PlayerInfo that = (PlayerInfo) o;

        return playerId == that.playerId &&
                playerType == that.playerType &&
                requestedRace == that.requestedRace &&
                actualRace == that.actualRace &&
                difficulty == that.difficulty;
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + (playerType != null ? playerType.hashCode() : 0);
        result = 31 * result + requestedRace.hashCode();
        result = 31 * result + (actualRace != null ? actualRace.hashCode() : 0);
        result = 31 * result + (difficulty != null ? difficulty.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
