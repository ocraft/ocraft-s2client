package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.P0;
import static com.github.ocraft.s2client.protocol.Fixtures.P1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LineTest {

    @Test
    void serializesToSc2ApiLine() {
        assertThatAllFieldsInRequestAreSerialized(Line.of(P0, P1).toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Debug.Line sc2ApiLine) {
        assertThat(sc2ApiLine.hasP0()).as("sc2api line: p0").isTrue();
        assertThat(sc2ApiLine.hasP1()).as("sc2api line: p1").isTrue();
    }

    @Test
    void throwsExceptionWhenPointsAreNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Line.of(nothing(), P1))
                .withMessage("p0 is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Line.of(P0, nothing()))
                .withMessage("p1 is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Line.class).withNonnullFields("p0", "p1").verify();
    }
}