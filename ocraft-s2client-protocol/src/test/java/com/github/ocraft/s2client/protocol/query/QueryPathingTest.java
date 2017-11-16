package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.query.QueryPathing.path;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class QueryPathingTest {

    @Test
    void serializesToSc2ApiQueryPathing() {
        assertThatAllFieldsAreSerialized(path().from(START).to(END).build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Query.RequestQueryPathing sc2ApiText) {
        assertThat(sc2ApiText.hasStartPos()).as("sc2api pathing: has start pos").isTrue();
        assertThat(sc2ApiText.hasEndPos()).as("sc2api pathing: has end pos").isTrue();
    }

    @Test
    void serializesOnlyRecentlyAddedStart() {
        assertCorrectStartCase(fullAccessTo(path().from(Tag.from(UNIT_TAG))).from(START).to(END).build().toSc2Api());
        assertCorrectStartCaseAfterOrderChange(
                fullAccessTo(path().from(START)).from(Tag.from(UNIT_TAG)).to(END).build().toSc2Api());
    }

    private QueryPathing.Builder fullAccessTo(Object obj) {
        return (QueryPathing.Builder) obj;
    }

    private void assertCorrectStartCase(Query.RequestQueryPathing sc2ApiText) {
        assertThat(sc2ApiText.hasStartPos()).as("sc2api pathing: has start post").isTrue();
        assertThat(sc2ApiText.hasUnitTag()).as("sc2api pathing: has unit tag").isFalse();
    }

    private void assertCorrectStartCaseAfterOrderChange(Query.RequestQueryPathing sc2ApiText) {
        assertThat(sc2ApiText.hasStartPos()).as("sc2api pathing: has start post").isFalse();
        assertThat(sc2ApiText.hasUnitTag()).as("sc2api pathing: has unit tag").isTrue();
    }

    @Test
    void throwsExceptionWhenEndIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(path().from(START)).build())
                .withMessage("end is required");
    }

    @Test
    void throwsExceptionWhenOneOfStartCaseIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(path()).to(END).build())
                .withMessage("one of start case is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(QueryPathing.class).withNonnullFields("end").verify();
    }
}