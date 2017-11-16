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