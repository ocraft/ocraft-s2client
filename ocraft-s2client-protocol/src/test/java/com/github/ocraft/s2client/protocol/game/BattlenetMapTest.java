package com.github.ocraft.s2client.protocol.game;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BattlenetMapTest {

    @Test
    void throwsExceptionWhenMapNameIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BattlenetMap.of(nothing()))
                .withMessage("battlenet map name is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(BattlenetMap.class).withNonnullFields("name").verify();
    }

}