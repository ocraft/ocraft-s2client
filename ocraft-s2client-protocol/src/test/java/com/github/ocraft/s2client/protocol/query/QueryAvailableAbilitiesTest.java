package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.UNIT_TAG;
import static com.github.ocraft.s2client.protocol.query.QueryAvailableAbilities.availableAbilities;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class QueryAvailableAbilitiesTest {
    @Test
    void serializesToSc2ApiQueryAvailableAbilities() {
        assertThatAllFieldsAreSerialized(availableAbilities().of(Tag.from(UNIT_TAG)).build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Query.RequestQueryAvailableAbilities sc2ApiText) {
        assertThat(sc2ApiText.getUnitTag()).as("sc2api available abilities: unit tag").isEqualTo(UNIT_TAG);
    }

    @Test
    void throwsExceptionWhenUnitTagIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((QueryAvailableAbilities.Builder) availableAbilities()).build())
                .withMessage("unit tag is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(QueryAvailableAbilities.class).withNonnullFields("unitTag").verify();
    }
}