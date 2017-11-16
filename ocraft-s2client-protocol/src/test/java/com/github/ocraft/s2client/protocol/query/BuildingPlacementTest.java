package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiBuildingPlacement;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BuildingPlacementTest {
    @Test
    void throwsExceptionWhenSc2ApiBuildingPlacementIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BuildingPlacement.from(nothing()))
                .withMessage("sc2api response query building placement is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiBuildingPlacement() {
        assertThatAllFieldsAreConverted(BuildingPlacement.from(sc2ApiBuildingPlacement()));
    }

    private void assertThatAllFieldsAreConverted(BuildingPlacement pathing) {
        assertThat(pathing.getResult()).as("building placement: result").isNotNull();
    }

    @Test
    void throwsExceptionWhenTagIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BuildingPlacement.from(without(
                        () -> sc2ApiBuildingPlacement().toBuilder(),
                        Query.ResponseQueryBuildingPlacement.Builder::clearResult).build()))
                .withMessage("result is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(BuildingPlacement.class).withNonnullFields("result").verify();
    }
}