package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.Defaults;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.debug.DebugLine.line;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DebugLineTest {

    @Test
    void serializesToSc2ApiDebugLine() {
        assertThatAllFieldsAreSerialized(debugLine().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugLine sc2ApiLine) {
        assertThat(sc2ApiLine.hasColor()).as("sc2api line: has color").isTrue();
        assertThat(sc2ApiLine.hasLine()).as("sc2api line: has line").isTrue();
    }

    @Test
    void throwsExceptionWhenColorIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(line().of(P0, P1).withColor(nothing())).build())
                .withMessage("color is required");
    }

    private DebugLine.Builder fullAccessTo(Object obj) {
        return (DebugLine.Builder) obj;
    }

    @Test
    void throwsExceptionWhenLineIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(line()).withColor(SAMPLE_COLOR).build())
                .withMessage("line is required");
    }

    @Test
    void hasDefaultColorIfNotSet() {
        assertThat(line().of(P0, P1).build().getColor()).as("default color").isEqualTo(Defaults.COLOR);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugLine.class).withNonnullFields("color", "line").verify();
    }

}