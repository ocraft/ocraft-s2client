package com.github.ocraft.s2client.protocol.debug;

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