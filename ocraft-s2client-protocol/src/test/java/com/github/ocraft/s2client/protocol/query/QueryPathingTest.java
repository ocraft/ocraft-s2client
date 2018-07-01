package com.github.ocraft.s2client.protocol.query;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
