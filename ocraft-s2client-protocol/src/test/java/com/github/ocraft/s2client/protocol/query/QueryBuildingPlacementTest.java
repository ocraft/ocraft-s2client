/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.query.QueryBuildingPlacement.placeBuilding;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class QueryBuildingPlacementTest {

    @Test
    void serializesToSc2ApiQueryBuildingPlacement() {
        assertThatAllFieldsAreSerialized(queryBuildingPlacement().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Query.RequestQueryBuildingPlacement sc2ApiText) {
        assertThat(sc2ApiText.hasAbilityId()).as("sc2api building placement: has ability").isTrue();
        assertThat(sc2ApiText.hasTargetPos()).as("sc2api building placement: has target pos").isTrue();
        assertThat(sc2ApiText.hasPlacingUnitTag()).as("sc2api building placement: has placing unit tag").isTrue();
    }

    @Test
    void throwsExceptionWhenUnitIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(placeBuilding().useAbility(Abilities.BUILD_ARMORY)).build())
                .withMessage("unit is required");
    }

    private QueryBuildingPlacement.Builder fullAccessTo(Object obj) {
        return (QueryBuildingPlacement.Builder) obj;
    }

    @Test
    void throwsExceptionWhenAbilityIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(placeBuilding().withUnit(Tag.from(UNIT_TAG))).on(START).build())
                .withMessage("ability is required");
    }

    @Test
    void throwsExceptionWhenTargetIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(placeBuilding().withUnit(Tag.from(UNIT_TAG))
                        .useAbility(Abilities.BUILD_ARMORY)).build())
                .withMessage("target is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(QueryBuildingPlacement.class).withNonnullFields("ability", "target").verify();
    }

}