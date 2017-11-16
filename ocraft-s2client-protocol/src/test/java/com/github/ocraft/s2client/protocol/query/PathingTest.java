package com.github.ocraft.s2client.protocol.query;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.DISTANCE;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiPathing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PathingTest {

    @Test
    void throwsExceptionWhenSc2ApiPathingIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Pathing.from(nothing()))
                .withMessage("sc2api response query pathing is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiPathing() {
        assertThatAllFieldsAreConverted(Pathing.from(sc2ApiPathing()));
    }

    private void assertThatAllFieldsAreConverted(Pathing pathing) {
        assertThat(pathing.getDistance()).as("pathing: distance").hasValue(DISTANCE);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Pathing.class).verify();
    }
}