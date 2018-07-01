package com.github.ocraft.s2client.protocol.debug;

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

import SC2APIProtocol.Debug;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.debug.DebugCommand.command;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DebugCommandTest {
    private enum Command {
        DRAW,
        GAME_STATE,
        CREATE_UNIT,
        KILL_UNIT,
        TEST_PROCESS,
        SET_SCORE,
        END_GAME,
        SET_UNIT_VALUE
    }

    @Test
    void serializesToSc2ApiDebugCommandWithDraw() {
        assertThatCorrectActionIsSerialized(Command.DRAW, command().of(debugDraw()).toSc2Api());
        assertThatCorrectActionIsSerialized(Command.DRAW, command().of(debugDraw().build()).toSc2Api());
    }

    private void assertThatCorrectActionIsSerialized(Command command, Debug.DebugCommand sc2ApiDebug) {
        if (Command.DRAW.equals(command)) {
            assertThat(sc2ApiDebug.hasDraw()).as("sc2api debug: has draw").isTrue();
        } else {
            assertThat(sc2ApiDebug.hasDraw()).as("sc2api debug: has draw").isFalse();
        }
        if (Command.GAME_STATE.equals(command)) {
            assertThat(sc2ApiDebug.hasGameState()).as("sc2api debug: has game state").isTrue();
        } else {
            assertThat(sc2ApiDebug.hasGameState()).as("sc2api debug: has game state").isFalse();
        }
        if (Command.CREATE_UNIT.equals(command)) {
            assertThat(sc2ApiDebug.hasCreateUnit()).as("sc2api debug: has create unit").isTrue();
        } else {
            assertThat(sc2ApiDebug.hasCreateUnit()).as("sc2api debug: has create unit").isFalse();
        }
        if (Command.KILL_UNIT.equals(command)) {
            assertThat(sc2ApiDebug.hasKillUnit()).as("sc2api debug: has kill unit").isTrue();
        } else {
            assertThat(sc2ApiDebug.hasKillUnit()).as("sc2api debug: has kill unit").isFalse();
        }
        if (Command.TEST_PROCESS.equals(command)) {
            assertThat(sc2ApiDebug.hasTestProcess()).as("sc2api debug: has test process").isTrue();
        } else {
            assertThat(sc2ApiDebug.hasTestProcess()).as("sc2api debug: has test process").isFalse();
        }
        if (Command.SET_SCORE.equals(command)) {
            assertThat(sc2ApiDebug.hasScore()).as("sc2api debug: has set score").isTrue();
        } else {
            assertThat(sc2ApiDebug.hasScore()).as("sc2api debug: has set score").isFalse();
        }
        if (Command.END_GAME.equals(command)) {
            assertThat(sc2ApiDebug.hasEndGame()).as("sc2api debug: has end game").isTrue();
        } else {
            assertThat(sc2ApiDebug.hasEndGame()).as("sc2api debug: has end game").isFalse();
        }
        if (Command.SET_UNIT_VALUE.equals(command)) {
            assertThat(sc2ApiDebug.hasUnitValue()).as("sc2api debug: has set unit value").isTrue();
        } else {
            assertThat(sc2ApiDebug.hasUnitValue()).as("sc2api debug: has set unit value").isFalse();
        }
    }

    @Test
    void serializesToSc2ApiDebugCommandWithGameState() {
        assertThatCorrectActionIsSerialized(Command.GAME_STATE, command().of(DebugGameState.SHOW_MAP).toSc2Api());
    }

    @Test
    void serializesToSc2ApiDebugCommandWithCreateUnit() {
        assertThatCorrectActionIsSerialized(Command.CREATE_UNIT, command().of(debugCreateUnit()).toSc2Api());
        assertThatCorrectActionIsSerialized(Command.CREATE_UNIT, command().of(debugCreateUnit().build()).toSc2Api());
    }

    @Test
    void serializesToSc2ApiDebugCommandWithKillUnit() {
        assertThatCorrectActionIsSerialized(Command.KILL_UNIT, command().of(debugKillUnit()).toSc2Api());
        assertThatCorrectActionIsSerialized(Command.KILL_UNIT, command().of(debugKillUnit().build()).toSc2Api());
    }

    @Test
    void serializesToSc2ApiDebugCommandWithTestProcess() {
        assertThatCorrectActionIsSerialized(Command.TEST_PROCESS, command().of(debugTestProcess()).toSc2Api());
        assertThatCorrectActionIsSerialized(Command.TEST_PROCESS, command().of(debugTestProcess().build()).toSc2Api());
    }

    @Test
    void serializesToSc2ApiDebugCommandWithSetScore() {
        assertThatCorrectActionIsSerialized(Command.SET_SCORE, command().of(debugSetScore()).toSc2Api());
        assertThatCorrectActionIsSerialized(Command.SET_SCORE, command().of(debugSetScore().build()).toSc2Api());
    }

    @Test
    void serializesToSc2ApiDebugCommandWithEndGame() {
        assertThatCorrectActionIsSerialized(Command.END_GAME, command().of(debugEndGame()).toSc2Api());
        assertThatCorrectActionIsSerialized(Command.END_GAME, command().of(debugEndGame().build()).toSc2Api());
    }

    @Test
    void serializesToSc2ApiDebugCommandWithSetUnitValue() {
        assertThatCorrectActionIsSerialized(Command.SET_UNIT_VALUE, command().of(debugSetUnitValue()).toSc2Api());
        assertThatCorrectActionIsSerialized(
                Command.SET_UNIT_VALUE, command().of(debugSetUnitValue().build()).toSc2Api());
    }

    @Test
    void throwsExceptionWhenThereIsNoActionCase() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> command().of((DebugDraw) nothing()))
                .withMessage("draw is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> command().of((DebugGameState) nothing()))
                .withMessage("game state is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> command().of((DebugCreateUnit) nothing()))
                .withMessage("create unit is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> command().of((DebugKillUnit) nothing()))
                .withMessage("kill unit is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> command().of((DebugTestProcess) nothing()))
                .withMessage("test process is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> command().of((DebugSetScore) nothing()))
                .withMessage("set score is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> command().of((DebugEndGame) nothing()))
                .withMessage("end game is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> command().of((DebugSetUnitValue) nothing()))
                .withMessage("set unit value is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugCommand.class).verify();
    }

}
