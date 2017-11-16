package com.github.ocraft.s2client.protocol.observation.raw;

import SC2APIProtocol.Raw;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiEvent;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class EventTest {
    @Test
    void throwsExceptionWhenSc2ApiEventIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Event.from(nothing()))
                .withMessage("sc2api event is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiEvent() {
        assertThatAllFieldsAreConverted(Event.from(sc2ApiEvent()));
    }

    private void assertThatAllFieldsAreConverted(Event event) {
        assertThat(event.getDeadUnits()).as("event: dead units").isNotEmpty();
    }

    @Test
    void hasEmptySetOfEffectsWhenNotProvided() {
        assertThat(Event.from(
                without(() -> sc2ApiEvent().toBuilder(), Raw.Event.Builder::clearDeadUnits).build()
        ).getDeadUnits()).as("event: empty dead units set").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(Event.class).withNonnullFields("deadUnits").verify();
    }
}