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

import com.github.ocraft.s2client.protocol.game.Difficulty;
import com.github.ocraft.s2client.protocol.game.Race;
import picocli.CommandLine;

import java.util.Optional;

// TODO p.picheta add more fancy usage print
public final class LadderSettings {

    @CommandLine.Option(names = {"-g", "--GamePort"}, description = "Port of client to connect to", required = true)
    private Integer gamePort;

    @CommandLine.Option(names = {"-o", "--StartPort"}, description = "Starting server port", required = true)
    private Integer startPort;

    @CommandLine.Option(names = {"-l", "--LadderServer"}, description = "Ladder server address", required = true)
    private String ladderServer;

    @CommandLine.Option(names = {"-c", "--ComputerOpponent"}, arity = "0..1",
            description = "If we set up a computer opponent")
    private boolean computerOpponent;

    @CommandLine.Option(names = {"-a", "--ComputerRace"}, description = "Race of computer opponent")
    private Race computerRace;

    @CommandLine.Option(names = {"-d", "--ComputerDifficulty"}, description = "Difficulty of computer opponent")
    private Difficulty computerDifficulty;

    @CommandLine.Option(names = {"-h4l", "--help4Ladder"}, usageHelp = true, description = "Display this help message.")
    private boolean usageHelpRequested;

    @CommandLine.Option(names = {"-i", "--OpponentId"}, description = "Unique Id of the opponent.")
    private String opponentId;

    @CommandLine.Option(names = {"-r", "--RealTime"}, arity = "0..1",
            description = "Whether to run StarCraft II in  real time or not.")
    private Boolean realTime;

    public Integer getGamePort() {
        return gamePort;
    }

    public Integer getStartPort() {
        return startPort;
    }

    public String getLadderServer() {
        return ladderServer;
    }

    public boolean getComputerOpponent() {
        return computerOpponent;
    }

    public Optional<Race> getComputerRace() {
        return Optional.ofNullable(computerRace);
    }

    public Optional<Difficulty> getComputerDifficulty() {
        return Optional.ofNullable(computerDifficulty);
    }

    public boolean isUsageHelpRequested() {
        return usageHelpRequested;
    }

    public String getOpponentId() {
        return opponentId;
    }

    public Boolean isRealTime() {
        return realTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LadderSettings that = (LadderSettings) o;

        if (computerOpponent != that.computerOpponent) return false;
        if (usageHelpRequested != that.usageHelpRequested) return false;
        if (gamePort != null ? !gamePort.equals(that.gamePort) : that.gamePort != null) return false;
        if (startPort != null ? !startPort.equals(that.startPort) : that.startPort != null) return false;
        if (ladderServer != null ? !ladderServer.equals(that.ladderServer) : that.ladderServer != null) return false;
        if (computerRace != that.computerRace) return false;
        if (computerDifficulty != that.computerDifficulty) return false;
        if (realTime != null ? !realTime.equals(that.realTime) : that.realTime != null) return false;
        return opponentId != null ? opponentId.equals(that.opponentId) : that.opponentId == null;
    }

    @Override
    public int hashCode() {
        int result = gamePort != null ? gamePort.hashCode() : 0;
        result = 31 * result + (startPort != null ? startPort.hashCode() : 0);
        result = 31 * result + (ladderServer != null ? ladderServer.hashCode() : 0);
        result = 31 * result + (computerOpponent ? 1 : 0);
        result = 31 * result + (computerRace != null ? computerRace.hashCode() : 0);
        result = 31 * result + (computerDifficulty != null ? computerDifficulty.hashCode() : 0);
        result = 31 * result + (usageHelpRequested ? 1 : 0);
        result = 31 * result + (opponentId != null ? opponentId.hashCode() : 0);
        result = 31 * result + (realTime != null ? realTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LadderSettings{" +
                "gamePort=" + gamePort +
                ", startPort=" + startPort +
                ", ladderServer='" + ladderServer + '\'' +
                ", computerOpponent=" + computerOpponent +
                ", computerRace=" + computerRace +
                ", computerDifficulty=" + computerDifficulty +
                ", usageHelpRequested=" + usageHelpRequested +
                ", opponentId='" + opponentId + '\'' +
                ", realTime=" + realTime +
                '}';
    }
}
