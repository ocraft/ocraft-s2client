package com.github.ocraft.s2client.bot.setting;

/*-
 * #%L
 * ocraft-s2client-bot
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

import com.github.ocraft.s2client.bot.S2Agent;
import com.github.ocraft.s2client.protocol.game.*;

import java.util.Objects;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class PlayerSettings {

    private final PlayerSetup playerSetup;
    private final Race race;
    private final Difficulty difficulty;
    private final S2Agent agent;
    private final String playerName;
    private final AiBuild aiBuild;

    private PlayerSettings(
            PlayerSetup playerSetup, Race race, Difficulty difficulty, S2Agent agent, String playerName,
            AiBuild aiBuild) {
        this.playerSetup = playerSetup;
        this.race = race;
        this.difficulty = difficulty;
        this.agent = agent;
        this.playerName = playerName;
        this.aiBuild = aiBuild;
    }

    public static PlayerSettings participant(Race race, S2Agent agent) {
        require("race", race);
        require("agent", agent);
        return new PlayerSettings(PlayerSetup.participant(), race, null, agent, null, null);
    }

    public static PlayerSettings computer(Race race, Difficulty difficulty) {
        require("race", race);
        require("difficulty", difficulty);
        return new PlayerSettings(ComputerPlayerSetup.computer(race, difficulty), race, difficulty, null, null, null);
    }

    public static PlayerSettings participant(Race race, S2Agent agent, String playerName) {
        require("race", race);
        require("agent", agent);
        return new PlayerSettings(PlayerSetup.participant(), race, null, agent, playerName, null);
    }

    public static PlayerSettings computer(Race race, Difficulty difficulty, String playerName) {
        require("race", race);
        require("difficulty", difficulty);
        return new PlayerSettings(
                ComputerPlayerSetup.computer(race, difficulty, playerName),
                race,
                difficulty,
                null,
                playerName,
                null);
    }

    public static PlayerSettings computer(Race race, Difficulty difficulty, String playerName, AiBuild aiBuild) {
        require("race", race);
        require("difficulty", difficulty);
        return new PlayerSettings(
                ComputerPlayerSetup.computer(race, difficulty, playerName, aiBuild),
                race,
                difficulty,
                null,
                playerName,
                aiBuild);
    }

    public static PlayerSettings computer(Race race, Difficulty difficulty, AiBuild aiBuild) {
        require("race", race);
        require("difficulty", difficulty);
        return new PlayerSettings(
                ComputerPlayerSetup.computer(race, difficulty, null, aiBuild),
                race,
                difficulty,
                null,
                null,
                aiBuild);
    }

    public PlayerSetup getPlayerSetup() {
        return playerSetup;
    }

    public Race getRace() {
        return race;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public S2Agent getAgent() {
        return agent;
    }

    public String getPlayerName() {
        return playerName;
    }

    public AiBuild getAiBuild() {
        return aiBuild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerSettings that = (PlayerSettings) o;

        if (!playerSetup.equals(that.playerSetup)) return false;
        if (race != that.race) return false;
        if (difficulty != that.difficulty) return false;
        if (!Objects.equals(agent, that.agent)) return false;
        if (!Objects.equals(playerName, that.playerName)) return false;
        return aiBuild == that.aiBuild;
    }

    @Override
    public int hashCode() {
        int result = playerSetup.hashCode();
        result = 31 * result + race.hashCode();
        result = 31 * result + difficulty.hashCode();
        result = 31 * result + (agent != null ? agent.hashCode() : 0);
        result = 31 * result + (playerName != null ? playerName.hashCode() : 0);
        result = 31 * result + (aiBuild != null ? aiBuild.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PlayerSettings{" +
                "playerSetup=" + playerSetup +
                ", race=" + race +
                ", difficulty=" + difficulty +
                ", agent=" + agent +
                ", playerName='" + playerName + '\'' +
                ", aiBuild=" + aiBuild +
                '}';
    }
}
