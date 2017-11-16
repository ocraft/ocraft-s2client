package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.debug.DebugSetUnitValue.setUnitValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class DebugSetUnitValueTest {
    @Test
    void serializesToSc2ApiDebugSetUnitValue() {
        assertThatAllFieldsAreSerialized(debugSetUnitValue().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugSetUnitValue sc2ApiSetUnitValue) {
        assertThat(sc2ApiSetUnitValue.hasUnitValue()).as("sc2api set unit value: has unit value").isTrue();
        assertThat(sc2ApiSetUnitValue.hasUnitTag()).as("sc2api set unit value: has unit tag").isTrue();
        assertThat(sc2ApiSetUnitValue.getValue()).as("sc2api set unit value: value").isEqualTo(VITAL_SCORE_ENERGY);
    }

    @Test
    void throwsExceptionWhenUnitValueIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(setUnitValue().forUnit(Tag.from(UNIT_TAG)))
                        .to(VITAL_SCORE_ENERGY).build())
                .withMessage("unit value is required");
    }

    private DebugSetUnitValue.Builder fullAccessTo(Object obj) {
        return (DebugSetUnitValue.Builder) obj;
    }

    @Test
    void throwsExceptionWhenUnitTagIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(setUnitValue())
                        .set(DebugSetUnitValue.UnitValue.ENERGY).to(VITAL_SCORE_ENERGY).build())
                .withMessage("unit tag is required");
    }

    @Test
    void throwsExceptionWhenValueIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(setUnitValue().forUnit(Tag.from(UNIT_TAG))
                        .set(DebugSetUnitValue.UnitValue.ENERGY)).build())
                .withMessage("value is required");
    }

    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "resultMappings")
    void serializesToSc2ApiTest(
            Debug.DebugSetUnitValue.UnitValue expectedSc2ApiUnitValue, DebugSetUnitValue.UnitValue unitValue) {
        assertThat(unitValue.toSc2Api()).isEqualTo(expectedSc2ApiUnitValue);
    }

    private static Stream<Arguments> resultMappings() {
        return Stream.of(
                of(Debug.DebugSetUnitValue.UnitValue.Energy, DebugSetUnitValue.UnitValue.ENERGY),
                of(Debug.DebugSetUnitValue.UnitValue.Shields, DebugSetUnitValue.UnitValue.SHIELDS),
                of(Debug.DebugSetUnitValue.UnitValue.Life, DebugSetUnitValue.UnitValue.LIFE));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugSetUnitValue.class).withNonnullFields("unitValue", "unitTag").verify();
    }
}