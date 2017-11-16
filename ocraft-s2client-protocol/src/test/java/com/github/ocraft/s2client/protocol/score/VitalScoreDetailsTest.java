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
package com.github.ocraft.s2client.protocol.score;

import SC2APIProtocol.ScoreOuterClass;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class VitalScoreDetailsTest {

    @Test
    void throwsExceptionWhenSc2ApiVitalScoreDetailsIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> VitalScoreDetails.from(nothing()))
                .withMessage("sc2api vital score details is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiVitalScoreDetails() {
        assertThatAllFieldsAreConverted(VitalScoreDetails.from(sc2ApiVitalScoreDetails()));
    }

    private void assertThatAllFieldsAreConverted(VitalScoreDetails vitalScoreDetails) {
        assertThat(vitalScoreDetails.getLife()).as("vital score details: life").isEqualTo(VITAL_SCORE_LIFE);
        assertThat(vitalScoreDetails.getShields()).as("vital score details: shields").isEqualTo(VITAL_SCORE_SHIELDS);
        assertThat(vitalScoreDetails.getEnergy()).as("vital score details: energy").isEqualTo(VITAL_SCORE_ENERGY);
    }

    @Test
    void throwsExceptionWhenLifeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> VitalScoreDetails.from(without(
                        () -> sc2ApiVitalScoreDetails().toBuilder(),
                        ScoreOuterClass.VitalScoreDetails.Builder::clearLife).build()))
                .withMessage("life is required");
    }

    @Test
    void throwsExceptionWhenShieldsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> VitalScoreDetails.from(without(
                        () -> sc2ApiVitalScoreDetails().toBuilder(),
                        ScoreOuterClass.VitalScoreDetails.Builder::clearShields).build()))
                .withMessage("shields is required");
    }

    @Test
    void throwsExceptionWhenEnergyIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> VitalScoreDetails.from(without(
                        () -> sc2ApiVitalScoreDetails().toBuilder(),
                        ScoreOuterClass.VitalScoreDetails.Builder::clearEnergy).build()))
                .withMessage("energy is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(VitalScoreDetails.class).verify();
    }

}