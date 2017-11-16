package com.github.ocraft.s2client.protocol.score;

import SC2APIProtocol.ScoreOuterClass;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ScoreDetailsTest {

    @Test
    void throwsExceptionWhenSc2ApiScoreDetailsIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(nothing()))
                .withMessage("sc2api score details is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiScoreDetails() {
        assertThatAllFieldsAreConverted(ScoreDetails.from(sc2ApiScoreDetails()));
    }

    private void assertThatAllFieldsAreConverted(ScoreDetails scoreDetails) {
        assertThat(scoreDetails.getIdleProductionTime()).as("score details: idle production time")
                .isEqualTo(IDLE_PRODUCTION_TIME);
        assertThat(scoreDetails.getIdleWorkerTime()).as("score details: idle worker time")
                .isEqualTo(IDLE_WORKER_TIME);
        assertThat(scoreDetails.getTotalValueUnits()).as("score details: total value units")
                .isEqualTo(TOTAL_VALUE_UNITS);
        assertThat(scoreDetails.getTotalValueStructures()).as("score details: total value structures")
                .isEqualTo(TOTAL_VALUE_STRUCTURES);
        assertThat(scoreDetails.getKilledValueUnits()).as("score details: killed value units")
                .isEqualTo(KILLED_VALUE_UNITS);
        assertThat(scoreDetails.getKilledValueStructures()).as("score details: killed value structures")
                .isEqualTo(KILLED_VALUE_STRUCTURES);
        assertThat(scoreDetails.getCollectedMinerals()).as("score details: collected minerals")
                .isEqualTo(COLLECTED_MINERALS);
        assertThat(scoreDetails.getCollectedVespene()).as("score details: collected vespene")
                .isEqualTo(COLLECTED_VESPENE);
        assertThat(scoreDetails.getCollectionRateMinerals()).as("score details: collection rate minerals")
                .isEqualTo(COLLECTION_RATE_MINERALS);
        assertThat(scoreDetails.getCollectionRateVespene()).as("score details: collection rate vespene")
                .isEqualTo(COLLECTION_RATE_VESPENE);
        assertThat(scoreDetails.getSpentMinerals()).as("score details: spent minerals").isEqualTo(SPENT_MINERALS);
        assertThat(scoreDetails.getSpentVespene()).as("score details: spent vespene").isEqualTo(SPENT_VESPENE);
        assertThat(scoreDetails.getFoodUsed()).as("score details: food used").isNotNull();
        assertThat(scoreDetails.getKilledMinerals()).as("score details: killed minerals").isNotNull();
        assertThat(scoreDetails.getKilledVespene()).as("score details: killed vespene").isNotNull();
        assertThat(scoreDetails.getLostMinerals()).as("score details: lost minerals").isNotNull();
        assertThat(scoreDetails.getLostVespene()).as("score details: lost vespene").isNotNull();
        assertThat(scoreDetails.getFriendlyFireMinerals()).as("score details: friendly fire minerals").isNotNull();
        assertThat(scoreDetails.getFriendlyFireVespene()).as("score details: friendly fire vespene").isNotNull();
        assertThat(scoreDetails.getUsedMinerals()).as("score details: used minerals").isNotNull();
        assertThat(scoreDetails.getUsedVespene()).as("score details: used vespene").isNotNull();
        assertThat(scoreDetails.getTotalUsedMinerals()).as("score details: total used minerals").isNotNull();
        assertThat(scoreDetails.getTotalUsedVespene()).as("score details: total used vespene").isNotNull();
        assertThat(scoreDetails.getTotalDamageDealt()).as("score details: total damage dealt").isNotNull();
        assertThat(scoreDetails.getTotalDamageTaken()).as("score details: total damage taken").isNotNull();
        assertThat(scoreDetails.getTotalHealed()).as("score details: total healed").isNotNull();
    }

    @Test
    void throwsExceptionWhenIdleProductionTimeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearIdleProductionTime).build()))
                .withMessage("idle production time is required");
    }

    @Test
    void throwsExceptionWhenIdleWorkerTimeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearIdleWorkerTime).build()))
                .withMessage("idle worker time is required");
    }

    @Test
    void throwsExceptionWhenTotalValueUnitsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearTotalValueUnits).build()))
                .withMessage("total value units is required");
    }

    @Test
    void throwsExceptionWhenTotalValueStructuresIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearTotalValueStructures).build()))
                .withMessage("total value structures is required");
    }

    @Test
    void throwsExceptionWhenKilledValueUnitsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearKilledValueUnits).build()))
                .withMessage("killed value units is required");
    }

    @Test
    void throwsExceptionWhenKilledValueStructuresIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearKilledValueStructures).build()))
                .withMessage("killed value structures is required");
    }

    @Test
    void throwsExceptionWhenCollectedMineralsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearCollectedMinerals).build()))
                .withMessage("collected minerals is required");
    }

    @Test
    void throwsExceptionWhenCollectedVespeneIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearCollectedVespene).build()))
                .withMessage("collected vespene is required");
    }

    @Test
    void throwsExceptionWhenCollectionRateMineralsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearCollectionRateMinerals).build()))
                .withMessage("collection rate minerals is required");
    }

    @Test
    void throwsExceptionWhenCollectionRateVespeneIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearCollectionRateVespene).build()))
                .withMessage("collection rate vespene is required");
    }

    @Test
    void throwsExceptionWhenSpentMineralsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearSpentMinerals).build()))
                .withMessage("spent minerals is required");
    }

    @Test
    void throwsExceptionWhenSpentVespeneIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearSpentVespene).build()))
                .withMessage("spent vespene is required");
    }

    @Test
    void throwsExceptionWhenFoodUsedIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearFoodUsed).build()))
                .withMessage("food used is required");
    }

    @Test
    void throwsExceptionWhenKilledMineralsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearKilledMinerals).build()))
                .withMessage("killed minerals is required");
    }

    @Test
    void throwsExceptionWhenKilledVespeneIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearKilledVespene).build()))
                .withMessage("killed vespene is required");
    }

    @Test
    void throwsExceptionWhenLostMineralsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearLostMinerals).build()))
                .withMessage("lost minerals is required");
    }

    @Test
    void throwsExceptionWhenLostVespeneIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearLostVespene).build()))
                .withMessage("lost vespene is required");
    }

    @Test
    void throwsExceptionWhenFriendlyFireMineralsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearFriendlyFireMinerals).build()))
                .withMessage("friendly fire minerals is required");
    }

    @Test
    void throwsExceptionWhenFriendlyFireVespeneIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearFriendlyFireVespene).build()))
                .withMessage("friendly fire vespene is required");
    }

    @Test
    void throwsExceptionWhenUsedMineralsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearUsedMinerals).build()))
                .withMessage("used minerals is required");
    }

    @Test
    void throwsExceptionWhenUsedVespeneIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearUsedVespene).build()))
                .withMessage("used vespene is required");
    }

    @Test
    void throwsExceptionWhenTotalUsedMineralsIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearTotalUsedMinerals).build()))
                .withMessage("total used minerals is required");
    }

    @Test
    void throwsExceptionWhenTotalUsedVespeneIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearTotalUsedVespene).build()))
                .withMessage("total used vespene is required");
    }

    @Test
    void throwsExceptionWhenTotalDamageDealtIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearTotalDamageDealt).build()))
                .withMessage("total damage dealt is required");
    }

    @Test
    void throwsExceptionWhenTotalDamageTakenIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearTotalDamageTaken).build()))
                .withMessage("total damage taken is required");
    }

    @Test
    void throwsExceptionWhenTotalHealedIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ScoreDetails.from(without(
                        () -> sc2ApiScoreDetails().toBuilder(),
                        ScoreOuterClass.ScoreDetails.Builder::clearTotalHealed).build()))
                .withMessage("total healed is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ScoreDetails.class)
                .withNonnullFields("foodUsed", "killedMinerals", "killedVespene", "lostMinerals", "lostVespene",
                        "friendlyFireMinerals", "friendlyFireVespene", "usedMinerals", "usedVespene",
                        "totalUsedMinerals", "totalUsedVespene", "totalDamageDealt", "totalDamageTaken", "totalHealed")
                .verify();
    }
}