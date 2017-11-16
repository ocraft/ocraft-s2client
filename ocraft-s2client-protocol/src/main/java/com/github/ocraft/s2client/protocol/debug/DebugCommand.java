package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.syntax.debug.*;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class DebugCommand implements Sc2ApiSerializable<Debug.DebugCommand> {

    private static final long serialVersionUID = -7056183638001306339L;

    private final DebugDraw draw;
    private final DebugGameState gameState;
    private final DebugCreateUnit createUnit;
    private final DebugKillUnit killUnit;
    private final DebugTestProcess testProcess;
    private final DebugSetScore setScore; // Useful only for single-player "curriculum" maps.
    private final DebugEndGame endGame;
    private final DebugSetUnitValue setUnitValue;

    public static final class Builder {

        public DebugCommand of(DebugDraw draw) {
            require("draw", draw);
            return new DebugCommand(draw);
        }

        public DebugCommand of(DebugDraw.Builder draw) {
            require("draw builder", draw);
            return of(draw.build());
        }

        public DebugCommand of(DebugGameState gameState) {
            require("game state", gameState);
            return new DebugCommand(gameState);
        }

        public DebugCommand of(DebugCreateUnit createUnit) {
            require("create unit", createUnit);
            return new DebugCommand(createUnit);
        }

        public DebugCommand of(DebugCreateUnitBuilder createUnit) {
            require("create unit builder", createUnit);
            return of(createUnit.build());
        }

        public DebugCommand of(DebugKillUnit killUnit) {
            require("kill unit", killUnit);
            return new DebugCommand(killUnit);
        }

        public DebugCommand of(DebugKillUnitBuilder killUnit) {
            require("kill unit builder", killUnit);
            return of(killUnit.build());
        }

        public DebugCommand of(DebugTestProcess testProcess) {
            require("test process", testProcess);
            return new DebugCommand(testProcess);
        }

        public DebugCommand of(DebugTestProcessBuilder testProcess) {
            require("test process builder", testProcess);
            return of(testProcess.build());
        }

        public DebugCommand of(DebugSetScore setScore) {
            require("set score", setScore);
            return new DebugCommand(setScore);
        }

        public DebugCommand of(DebugSetScoreBuilder setScore) {
            require("set score builder", setScore);
            return of(setScore.build());
        }

        public DebugCommand of(DebugEndGame endGame) {
            require("end game", endGame);
            return new DebugCommand(endGame);
        }

        public DebugCommand of(DebugEndGameBuilder endGame) {
            require("end game builder", endGame);
            return of(endGame.build());
        }

        public DebugCommand of(DebugSetUnitValue setUnitValue) {
            require("set unit value", setUnitValue);
            return new DebugCommand(setUnitValue);
        }

        public DebugCommand of(DebugSetUnitValueBuilder setUnitValue) {
            require("set unit value builder", setUnitValue);
            return of(setUnitValue.build());
        }

    }

    private DebugCommand(DebugDraw draw) {
        this.draw = draw;
        this.gameState = nothing();
        this.createUnit = nothing();
        this.killUnit = nothing();
        this.testProcess = nothing();
        this.setScore = nothing();
        this.endGame = nothing();
        this.setUnitValue = nothing();
    }

    private DebugCommand(DebugGameState gameState) {
        this.draw = nothing();
        this.gameState = gameState;
        this.createUnit = nothing();
        this.killUnit = nothing();
        this.testProcess = nothing();
        this.setScore = nothing();
        this.endGame = nothing();
        this.setUnitValue = nothing();
    }

    private DebugCommand(DebugCreateUnit createUnit) {
        this.draw = nothing();
        this.gameState = nothing();
        this.createUnit = createUnit;
        this.killUnit = nothing();
        this.testProcess = nothing();
        this.setScore = nothing();
        this.endGame = nothing();
        this.setUnitValue = nothing();
    }

    private DebugCommand(DebugKillUnit killUnit) {
        this.draw = nothing();
        this.gameState = nothing();
        this.createUnit = nothing();
        this.killUnit = killUnit;
        this.testProcess = nothing();
        this.setScore = nothing();
        this.endGame = nothing();
        this.setUnitValue = nothing();
    }

    private DebugCommand(DebugTestProcess testProcess) {
        this.draw = nothing();
        this.gameState = nothing();
        this.createUnit = nothing();
        this.killUnit = nothing();
        this.testProcess = testProcess;
        this.setScore = nothing();
        this.endGame = nothing();
        this.setUnitValue = nothing();
    }

    private DebugCommand(DebugSetScore setScore) {
        this.draw = nothing();
        this.gameState = nothing();
        this.createUnit = nothing();
        this.killUnit = nothing();
        this.testProcess = nothing();
        this.setScore = setScore;
        this.endGame = nothing();
        this.setUnitValue = nothing();
    }

    private DebugCommand(DebugEndGame endGame) {
        this.draw = nothing();
        this.gameState = nothing();
        this.createUnit = nothing();
        this.killUnit = nothing();
        this.testProcess = nothing();
        this.setScore = nothing();
        this.endGame = endGame;
        this.setUnitValue = nothing();
    }

    private DebugCommand(DebugSetUnitValue setUnitValue) {
        this.draw = nothing();
        this.gameState = nothing();
        this.createUnit = nothing();
        this.killUnit = nothing();
        this.testProcess = nothing();
        this.setScore = nothing();
        this.endGame = nothing();
        this.setUnitValue = setUnitValue;
    }

    public static Builder command() {
        return new Builder();
    }

    @Override
    public Debug.DebugCommand toSc2Api() {
        Debug.DebugCommand.Builder aSc2ApiDebugCommand = Debug.DebugCommand.newBuilder();

        getDraw().map(DebugDraw::toSc2Api).ifPresent(aSc2ApiDebugCommand::setDraw);
        getGameState().map(DebugGameState::toSc2Api).ifPresent(aSc2ApiDebugCommand::setGameState);
        getCreateUnit().map(DebugCreateUnit::toSc2Api).ifPresent(aSc2ApiDebugCommand::setCreateUnit);
        getKillUnit().map(DebugKillUnit::toSc2Api).ifPresent(aSc2ApiDebugCommand::setKillUnit);
        getTestProcess().map(DebugTestProcess::toSc2Api).ifPresent(aSc2ApiDebugCommand::setTestProcess);
        getSetScore().map(DebugSetScore::toSc2Api).ifPresent(aSc2ApiDebugCommand::setScore);
        getEndGame().map(DebugEndGame::toSc2Api).ifPresent(aSc2ApiDebugCommand::setEndGame);
        getSetUnitValue().map(DebugSetUnitValue::toSc2Api).ifPresent(aSc2ApiDebugCommand::setUnitValue);

        return aSc2ApiDebugCommand.build();
    }

    public Optional<DebugDraw> getDraw() {
        return Optional.ofNullable(draw);
    }

    public Optional<DebugGameState> getGameState() {
        return Optional.ofNullable(gameState);
    }

    public Optional<DebugCreateUnit> getCreateUnit() {
        return Optional.ofNullable(createUnit);
    }

    public Optional<DebugKillUnit> getKillUnit() {
        return Optional.ofNullable(killUnit);
    }

    public Optional<DebugTestProcess> getTestProcess() {
        return Optional.ofNullable(testProcess);
    }

    public Optional<DebugSetScore> getSetScore() {
        return Optional.ofNullable(setScore);
    }

    public Optional<DebugEndGame> getEndGame() {
        return Optional.ofNullable(endGame);
    }

    public Optional<DebugSetUnitValue> getSetUnitValue() {
        return Optional.ofNullable(setUnitValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugCommand that = (DebugCommand) o;

        return (draw != null ? draw.equals(that.draw) : that.draw == null) &&
                (gameState != null ? gameState.equals(that.gameState) : that.gameState == null) &&
                (createUnit != null ? createUnit.equals(that.createUnit) : that.createUnit == null) &&
                (killUnit != null ? killUnit.equals(that.killUnit) : that.killUnit == null) &&
                (testProcess != null ? testProcess.equals(that.testProcess) : that.testProcess == null) &&
                (setScore != null ? setScore.equals(that.setScore) : that.setScore == null) &&
                (endGame != null ? endGame.equals(that.endGame) : that.endGame == null) &&
                (setUnitValue != null ? setUnitValue.equals(that.setUnitValue) : that.setUnitValue == null);
    }

    @Override
    public int hashCode() {
        int result = draw != null ? draw.hashCode() : 0;
        result = 31 * result + (gameState != null ? gameState.hashCode() : 0);
        result = 31 * result + (createUnit != null ? createUnit.hashCode() : 0);
        result = 31 * result + (killUnit != null ? killUnit.hashCode() : 0);
        result = 31 * result + (testProcess != null ? testProcess.hashCode() : 0);
        result = 31 * result + (setScore != null ? setScore.hashCode() : 0);
        result = 31 * result + (endGame != null ? endGame.hashCode() : 0);
        result = 31 * result + (setUnitValue != null ? setUnitValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
