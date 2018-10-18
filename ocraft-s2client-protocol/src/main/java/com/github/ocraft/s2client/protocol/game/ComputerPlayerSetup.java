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

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;

public final class ComputerPlayerSetup extends PlayerSetup {

    private static final long serialVersionUID = -8466918343240025565L;

    private final Race race;
    private final Difficulty difficulty;
    private final String playerName;

    private ComputerPlayerSetup(Race race, Difficulty difficulty) {
        super(PlayerType.COMPUTER);
        this.race = race;
        this.difficulty = difficulty;
        this.playerName = nothing();
    }

    private ComputerPlayerSetup(Race race, Difficulty difficulty, String playerName) {
        super(PlayerType.COMPUTER);
        this.race = race;
        this.difficulty = difficulty;
        this.playerName = playerName;
    }

    public static ComputerPlayerSetup computer(Race race, Difficulty difficulty) {
        if (race == null) throw new IllegalArgumentException("race is required");
        if (difficulty == null) throw new IllegalArgumentException("difficulty level is required");
        return new ComputerPlayerSetup(race, difficulty);
    }

    public static ComputerPlayerSetup computer(Race race, Difficulty difficulty, String playerName) {
        if (race == null) throw new IllegalArgumentException("race is required");
        if (difficulty == null) throw new IllegalArgumentException("difficulty level is required");
        return new ComputerPlayerSetup(race, difficulty, playerName);
    }

    @Override
    public Sc2Api.PlayerSetup toSc2Api() {
        Sc2Api.PlayerSetup.Builder builder = Sc2Api.PlayerSetup.newBuilder()
                .setType(Sc2Api.PlayerType.Computer)
                .setDifficulty(difficulty.toSc2Api())
                .setRace(race.toSc2Api());

        getPlayerName().ifPresent(builder::setPlayerName);

        return builder.build();
    }

    public Race getRace() {
        return race;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Optional<String> getPlayerName() {
        return Optional.ofNullable(playerName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComputerPlayerSetup)) return false;
        if (!super.equals(o)) return false;

        ComputerPlayerSetup that = (ComputerPlayerSetup) o;

        if (!that.canEqual(this)) return false;
        if (race != that.race) return false;
        if (difficulty != that.difficulty) return false;
        return playerName != null ? playerName.equals(that.playerName) : that.playerName == null;
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ComputerPlayerSetup;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + race.hashCode();
        result = 31 * result + difficulty.hashCode();
        result = 31 * result + (playerName != null ? playerName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
