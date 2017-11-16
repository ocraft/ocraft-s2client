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

public final class ComputerPlayerSetup extends PlayerSetup {

    private static final long serialVersionUID = -8466918343240025565L;

    private final Race race;
    private final Difficulty difficulty;

    private ComputerPlayerSetup(Race race, Difficulty difficulty) {
        super(PlayerType.COMPUTER);
        this.race = race;
        this.difficulty = difficulty;
    }

    public static ComputerPlayerSetup computer(Race race, Difficulty difficulty) {
        if (race == null) throw new IllegalArgumentException("race is required");
        if (difficulty == null) throw new IllegalArgumentException("difficulty level is required");
        return new ComputerPlayerSetup(race, difficulty);
    }

    @Override
    public Sc2Api.PlayerSetup toSc2Api() {
        return Sc2Api.PlayerSetup.newBuilder()
                .setType(Sc2Api.PlayerType.Computer)
                .setDifficulty(difficulty.toSc2Api())
                .setRace(race.toSc2Api())
                .build();
    }

    public Race getRace() {
        return race;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComputerPlayerSetup)) return false;
        if (!super.equals(o)) return false;

        ComputerPlayerSetup that = (ComputerPlayerSetup) o;

        return that.canEqual(this) && race == that.race && difficulty == that.difficulty;
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
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
