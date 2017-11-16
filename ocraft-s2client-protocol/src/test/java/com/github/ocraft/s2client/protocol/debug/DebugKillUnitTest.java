package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.debugKillUnit;
import static com.github.ocraft.s2client.protocol.debug.DebugKillUnit.killUnit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DebugKillUnitTest {
    @Test
    void serializesToSc2ApiDebugKillUnit() {
        assertThatAllFieldsAreSerialized(debugKillUnit().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugKillUnit sc2ApiKillUnit) {
        assertThat(sc2ApiKillUnit.getTagList()).as("sc2api debug kill unit: unit tags").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenUnitTagSetIsEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((DebugKillUnit.Builder) killUnit()).build())
                .withMessage("unit tag set is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugKillUnit.class).withNonnullFields("unitTags").verify();
    }

}