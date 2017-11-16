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
package com.github.ocraft.s2client.protocol.action.observer;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.UNIT_TAG;
import static com.github.ocraft.s2client.protocol.action.Actions.Observer.cameraFollowUnits;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionObserverCameraFollowUnitsTest {
    @Test
    void serializesToSc2ApiActionObserverCameraFollowUnits() {
        assertThatAllFieldsInRequestAreSerialized(
                cameraFollowUnits().withTags(Tag.from(UNIT_TAG)).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(
            Sc2Api.ActionObserverCameraFollowUnits sc2ApiObserverCameraFollowUnits) {
        assertThat(sc2ApiObserverCameraFollowUnits.getUnitTagsList())
                .as("sc2api observer camera follow units: unit tags").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenUnitsAreNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionObserverCameraFollowUnits.Builder) cameraFollowUnits()).build())
                .withMessage("units is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionObserverCameraFollowUnits.class).withNonnullFields("units").verify();
    }
}