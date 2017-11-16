package com.github.ocraft.s2client.protocol.unit;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.UNIT_TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TagTest {
    @Test
    void throwsExceptionWhenSc2ApiUnitTagIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Tag.from(nothing()))
                .withMessage("sc2api unit tag is required");
    }

    @Test
    void convertsSc2ApiUnitTagToTag() {
        assertThat(Tag.from(UNIT_TAG).getValue()).as("converted unit tag").isEqualTo(UNIT_TAG);
    }

    @Test
    void serializesToSc2ApiUnitTag() {
        assertThat(Tag.from(UNIT_TAG).toSc2Api()).as("serialized unit tag").isEqualTo(UNIT_TAG);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Tag.class).withNonnullFields("value").verify();
    }

}