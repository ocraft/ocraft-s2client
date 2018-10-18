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
    private final String playerName;

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

        this.playerName = tryGet(
                Sc2Api.PlayerInfo::getPlayerName, Sc2Api.PlayerInfo::hasPlayerName
        ).apply(sc2ApiPlayerInfo).orElse(nothing());
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

    public Optional<String> getPlayerName() {
        return Optional.ofNullable(playerName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerInfo that = (PlayerInfo) o;

        if (playerId != that.playerId) return false;
        if (playerType != that.playerType) return false;
        if (requestedRace != that.requestedRace) return false;
        if (actualRace != that.actualRace) return false;
        if (difficulty != that.difficulty) return false;
        return playerName != null ? playerName.equals(that.playerName) : that.playerName == null;
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + (playerType != null ? playerType.hashCode() : 0);
        result = 31 * result + requestedRace.hashCode();
        result = 31 * result + (actualRace != null ? actualRace.hashCode() : 0);
        result = 31 * result + (difficulty != null ? difficulty.hashCode() : 0);
        result = 31 * result + (playerName != null ? playerName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
