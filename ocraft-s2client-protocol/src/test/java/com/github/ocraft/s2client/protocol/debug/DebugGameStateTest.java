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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DebugGameStateTest {
    @ParameterizedTest(name = "\"{0}\" serializes to {1}")
    @MethodSource(value = "gameStateMappings")
    void serializesToSc2Api(DebugGameState gameState, Debug.DebugGameState expectedSc2ApiDebugGameState) {
        assertThat(gameState.toSc2Api()).isEqualTo(expectedSc2ApiDebugGameState);
    }

    private static Stream<Arguments> gameStateMappings() {
        return Stream.of(
                Arguments.of(DebugGameState.SHOW_MAP, Debug.DebugGameState.show_map),
                Arguments.of(DebugGameState.CONTROL_ENEMY, Debug.DebugGameState.control_enemy),
                Arguments.of(DebugGameState.FOOD, Debug.DebugGameState.food),
                Arguments.of(DebugGameState.FREE, Debug.DebugGameState.free),
                Arguments.of(DebugGameState.ALL_RESOURCES, Debug.DebugGameState.all_resources),
                Arguments.of(DebugGameState.GOD, Debug.DebugGameState.god),
                Arguments.of(DebugGameState.MINERALS, Debug.DebugGameState.minerals),
                Arguments.of(DebugGameState.GAS, Debug.DebugGameState.gas),
                Arguments.of(DebugGameState.COOLDOWN, Debug.DebugGameState.cooldown),
                Arguments.of(DebugGameState.TECH_TREE, Debug.DebugGameState.tech_tree),
                Arguments.of(DebugGameState.UPGRADE, Debug.DebugGameState.upgrade),
                Arguments.of(DebugGameState.FAST_BUILD, Debug.DebugGameState.fast_build));
    }
}
