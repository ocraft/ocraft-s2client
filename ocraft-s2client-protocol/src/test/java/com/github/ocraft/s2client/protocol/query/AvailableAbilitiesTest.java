package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiAvailableAbilities;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AvailableAbilitiesTest {
    @Test
    void throwsExceptionWhenSc2ApiAvailableAbilitiesIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AvailableAbilities.from(nothing()))
                .withMessage("sc2api response query available abilities is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiAvailableAbilities() {
        assertThatAllFieldsAreConverted(AvailableAbilities.from(sc2ApiAvailableAbilities()));
    }

    private void assertThatAllFieldsAreConverted(AvailableAbilities availableAbilities) {
        assertThat(availableAbilities.getAbilities()).as("available abilities: abilities").isNotEmpty();
        assertThat(availableAbilities.getUnitType()).as("available abilities: unit type").isNotNull();
        assertThat(availableAbilities.getUnitTag()).as("available abilities: unit tag").isNotNull();
    }

    @Test
    void throwsExceptionWhenUnitTagIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AvailableAbilities.from(without(
                        () -> sc2ApiAvailableAbilities().toBuilder(),
                        Query.ResponseQueryAvailableAbilities.Builder::clearUnitTag).build()))
                .withMessage("unit tag is required");
    }

    @Test
    void throwsExceptionWhenUnitTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> AvailableAbilities.from(without(
                        () -> sc2ApiAvailableAbilities().toBuilder(),
                        Query.ResponseQueryAvailableAbilities.Builder::clearUnitTypeId).build()))
                .withMessage("unit type is required");
    }

    @Test
    void hasEmptySetOfAbilitiesIfNotProvided() {
        assertThat(AvailableAbilities.from(without(
                () -> sc2ApiAvailableAbilities().toBuilder(),
                Query.ResponseQueryAvailableAbilities.Builder::clearAbilities).build()).getAbilities())
                .as("available abilities: default abilities set").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(AvailableAbilities.class)
                .withNonnullFields("abilities", "unitTag", "unitType").verify();
    }
}