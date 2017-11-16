package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class UnitAttributeTest {
    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "attributeMappings")
    void mapsSc2ApiAttribute(Data.Attribute sc2ApiAttribute, UnitAttribute expectedAttribute) {
        assertThat(UnitAttribute.from(sc2ApiAttribute)).isEqualTo(expectedAttribute);
    }

    private static Stream<Arguments> attributeMappings() {
        return Stream.of(
                of(Data.Attribute.Light, UnitAttribute.LIGHT),
                of(Data.Attribute.Armored, UnitAttribute.ARMORED),
                of(Data.Attribute.Biological, UnitAttribute.BIOLOGICAL),
                of(Data.Attribute.Mechanical, UnitAttribute.MECHANICAL),
                of(Data.Attribute.Robotic, UnitAttribute.ROBOTIC),
                of(Data.Attribute.Psionic, UnitAttribute.PSIONIC),
                of(Data.Attribute.Massive, UnitAttribute.MASSIVE),
                of(Data.Attribute.Structure, UnitAttribute.STRUCTURE),
                of(Data.Attribute.Hover, UnitAttribute.HOVER),
                of(Data.Attribute.Heroic, UnitAttribute.HEROIC),
                of(Data.Attribute.Summoned, UnitAttribute.SUMMONED));
    }

    @Test
    void throwsExceptionWhenAttributeIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> UnitAttribute.from(nothing()))
                .withMessage("sc2api attribute is required");
    }

}