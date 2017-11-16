package com.github.ocraft.s2client.protocol.score;

import SC2APIProtocol.ScoreOuterClass;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ScoreDetails implements Serializable {

    private static final long serialVersionUID = -8708308963625179126L;

    private final float idleProductionTime; // Interesting as a delta
    private final float idleWorkerTime;     // Interesting as a delta

    /* Note the "totalValue" fields are a combination of minerals, vespene and a human designer guess. Maybe useful as
    a delta. */
    private final float totalValueUnits;
    private final float totalValueStructures;

    /* Note the "killedValue" fields are a combination of minerals, vespene and a human designer guess. Maybe useful as
    a delta. The weighting of the combination and the human designer guess is different (not symmetric) with the
    "totalValue" fields! */
    private final float killedValueUnits;
    private final float killedValueStructures;

    private final float collectedMinerals;
    private final float collectedVespene;

    private final float collectionRateMinerals;
    private final float collectionRateVespene;

    private final float spentMinerals;
    private final float spentVespene;

    private final CategoryScoreDetails foodUsed;

    private final CategoryScoreDetails killedMinerals;
    private final CategoryScoreDetails killedVespene;

    private final CategoryScoreDetails lostMinerals;
    private final CategoryScoreDetails lostVespene;

    private final CategoryScoreDetails friendlyFireMinerals;
    private final CategoryScoreDetails friendlyFireVespene;

    private final CategoryScoreDetails usedMinerals;
    private final CategoryScoreDetails usedVespene;

    private final CategoryScoreDetails totalUsedMinerals;   // Interesting as a delta
    private final CategoryScoreDetails totalUsedVespene;    // Interesting as a delta

    private final VitalScoreDetails totalDamageDealt;   // Interesting as a delta
    private final VitalScoreDetails totalDamageTaken;   // Interesting as a delta
    private final VitalScoreDetails totalHealed;        // Interesting as a delta

    private ScoreDetails(ScoreOuterClass.ScoreDetails sc2ApiScoreDetails) {
        idleProductionTime = tryGet(
                ScoreOuterClass.ScoreDetails::getIdleProductionTime, ScoreOuterClass.ScoreDetails::hasIdleProductionTime
        ).apply(sc2ApiScoreDetails).orElseThrow(required("idle production time"));

        idleWorkerTime = tryGet(
                ScoreOuterClass.ScoreDetails::getIdleWorkerTime, ScoreOuterClass.ScoreDetails::hasIdleWorkerTime
        ).apply(sc2ApiScoreDetails).orElseThrow(required("idle worker time"));

        totalValueUnits = tryGet(
                ScoreOuterClass.ScoreDetails::getTotalValueUnits, ScoreOuterClass.ScoreDetails::hasTotalValueUnits
        ).apply(sc2ApiScoreDetails).orElseThrow(required("total value units"));

        totalValueStructures = tryGet(
                ScoreOuterClass.ScoreDetails::getTotalValueStructures,
                ScoreOuterClass.ScoreDetails::hasTotalValueStructures
        ).apply(sc2ApiScoreDetails).orElseThrow(required("total value structures"));

        killedValueUnits = tryGet(
                ScoreOuterClass.ScoreDetails::getKilledValueUnits, ScoreOuterClass.ScoreDetails::hasKilledValueUnits
        ).apply(sc2ApiScoreDetails).orElseThrow(required("killed value units"));

        killedValueStructures = tryGet(
                ScoreOuterClass.ScoreDetails::getKilledValueStructures,
                ScoreOuterClass.ScoreDetails::hasKilledValueStructures
        ).apply(sc2ApiScoreDetails).orElseThrow(required("killed value structures"));

        collectedMinerals = tryGet(
                ScoreOuterClass.ScoreDetails::getCollectedMinerals, ScoreOuterClass.ScoreDetails::hasCollectedMinerals
        ).apply(sc2ApiScoreDetails).orElseThrow(required("collected minerals"));

        collectedVespene = tryGet(
                ScoreOuterClass.ScoreDetails::getCollectedVespene, ScoreOuterClass.ScoreDetails::hasCollectedVespene
        ).apply(sc2ApiScoreDetails).orElseThrow(required("collected vespene"));

        collectionRateMinerals = tryGet(
                ScoreOuterClass.ScoreDetails::getCollectionRateMinerals,
                ScoreOuterClass.ScoreDetails::hasCollectionRateMinerals
        ).apply(sc2ApiScoreDetails).orElseThrow(required("collection rate minerals"));

        collectionRateVespene = tryGet(
                ScoreOuterClass.ScoreDetails::getCollectionRateVespene,
                ScoreOuterClass.ScoreDetails::hasCollectionRateVespene
        ).apply(sc2ApiScoreDetails).orElseThrow(required("collection rate vespene"));

        spentMinerals = tryGet(
                ScoreOuterClass.ScoreDetails::getSpentMinerals, ScoreOuterClass.ScoreDetails::hasSpentMinerals
        ).apply(sc2ApiScoreDetails).orElseThrow(required("spent minerals"));

        spentVespene = tryGet(
                ScoreOuterClass.ScoreDetails::getSpentVespene, ScoreOuterClass.ScoreDetails::hasSpentVespene
        ).apply(sc2ApiScoreDetails).orElseThrow(required("spent vespene"));

        foodUsed = tryGet(
                ScoreOuterClass.ScoreDetails::getFoodUsed, ScoreOuterClass.ScoreDetails::hasFoodUsed
        ).apply(sc2ApiScoreDetails).map(CategoryScoreDetails::from).orElseThrow(required("food used"));

        killedMinerals = tryGet(
                ScoreOuterClass.ScoreDetails::getKilledMinerals, ScoreOuterClass.ScoreDetails::hasKilledMinerals
        ).apply(sc2ApiScoreDetails).map(CategoryScoreDetails::from).orElseThrow(required("killed minerals"));

        killedVespene = tryGet(
                ScoreOuterClass.ScoreDetails::getKilledVespene, ScoreOuterClass.ScoreDetails::hasKilledVespene
        ).apply(sc2ApiScoreDetails).map(CategoryScoreDetails::from).orElseThrow(required("killed vespene"));

        lostMinerals = tryGet(
                ScoreOuterClass.ScoreDetails::getLostMinerals, ScoreOuterClass.ScoreDetails::hasLostMinerals
        ).apply(sc2ApiScoreDetails).map(CategoryScoreDetails::from).orElseThrow(required("lost minerals"));

        lostVespene = tryGet(
                ScoreOuterClass.ScoreDetails::getLostVespene, ScoreOuterClass.ScoreDetails::hasLostVespene
        ).apply(sc2ApiScoreDetails).map(CategoryScoreDetails::from).orElseThrow(required("lost vespene"));

        friendlyFireMinerals = tryGet(
                ScoreOuterClass.ScoreDetails::getFriendlyFireMinerals,
                ScoreOuterClass.ScoreDetails::hasFriendlyFireMinerals
        ).apply(sc2ApiScoreDetails).map(CategoryScoreDetails::from).orElseThrow(required("friendly fire minerals"));

        friendlyFireVespene = tryGet(
                ScoreOuterClass.ScoreDetails::getFriendlyFireVespene,
                ScoreOuterClass.ScoreDetails::hasFriendlyFireVespene
        ).apply(sc2ApiScoreDetails).map(CategoryScoreDetails::from).orElseThrow(required("friendly fire vespene"));

        usedMinerals = tryGet(
                ScoreOuterClass.ScoreDetails::getUsedMinerals, ScoreOuterClass.ScoreDetails::hasUsedMinerals
        ).apply(sc2ApiScoreDetails).map(CategoryScoreDetails::from).orElseThrow(required("used minerals"));

        usedVespene = tryGet(
                ScoreOuterClass.ScoreDetails::getUsedVespene, ScoreOuterClass.ScoreDetails::hasUsedVespene
        ).apply(sc2ApiScoreDetails).map(CategoryScoreDetails::from).orElseThrow(required("used vespene"));

        totalUsedMinerals = tryGet(
                ScoreOuterClass.ScoreDetails::getTotalUsedMinerals, ScoreOuterClass.ScoreDetails::hasTotalUsedMinerals
        ).apply(sc2ApiScoreDetails).map(CategoryScoreDetails::from).orElseThrow(required("total used minerals"));

        totalUsedVespene = tryGet(
                ScoreOuterClass.ScoreDetails::getTotalUsedVespene, ScoreOuterClass.ScoreDetails::hasTotalUsedVespene
        ).apply(sc2ApiScoreDetails).map(CategoryScoreDetails::from).orElseThrow(required("total used vespene"));

        totalDamageDealt = tryGet(
                ScoreOuterClass.ScoreDetails::getTotalDamageDealt, ScoreOuterClass.ScoreDetails::hasTotalDamageDealt
        ).apply(sc2ApiScoreDetails).map(VitalScoreDetails::from).orElseThrow(required("total damage dealt"));

        totalDamageTaken = tryGet(
                ScoreOuterClass.ScoreDetails::getTotalDamageTaken, ScoreOuterClass.ScoreDetails::hasTotalDamageTaken
        ).apply(sc2ApiScoreDetails).map(VitalScoreDetails::from).orElseThrow(required("total damage taken"));

        totalHealed = tryGet(
                ScoreOuterClass.ScoreDetails::getTotalHealed, ScoreOuterClass.ScoreDetails::hasTotalHealed
        ).apply(sc2ApiScoreDetails).map(VitalScoreDetails::from).orElseThrow(required("total healed"));
    }

    public static ScoreDetails from(ScoreOuterClass.ScoreDetails sc2ApiScoreDetails) {
        require("sc2api score details", sc2ApiScoreDetails);
        return new ScoreDetails(sc2ApiScoreDetails);
    }

    public float getIdleProductionTime() {
        return idleProductionTime;
    }

    public float getIdleWorkerTime() {
        return idleWorkerTime;
    }

    public float getTotalValueUnits() {
        return totalValueUnits;
    }

    public float getTotalValueStructures() {
        return totalValueStructures;
    }

    public float getKilledValueUnits() {
        return killedValueUnits;
    }

    public float getKilledValueStructures() {
        return killedValueStructures;
    }

    public float getCollectedMinerals() {
        return collectedMinerals;
    }

    public float getCollectedVespene() {
        return collectedVespene;
    }

    public float getCollectionRateMinerals() {
        return collectionRateMinerals;
    }

    public float getCollectionRateVespene() {
        return collectionRateVespene;
    }

    public float getSpentMinerals() {
        return spentMinerals;
    }

    public float getSpentVespene() {
        return spentVespene;
    }

    public CategoryScoreDetails getFoodUsed() {
        return foodUsed;
    }

    public CategoryScoreDetails getKilledMinerals() {
        return killedMinerals;
    }

    public CategoryScoreDetails getKilledVespene() {
        return killedVespene;
    }

    public CategoryScoreDetails getLostMinerals() {
        return lostMinerals;
    }

    public CategoryScoreDetails getLostVespene() {
        return lostVespene;
    }

    public CategoryScoreDetails getFriendlyFireMinerals() {
        return friendlyFireMinerals;
    }

    public CategoryScoreDetails getFriendlyFireVespene() {
        return friendlyFireVespene;
    }

    public CategoryScoreDetails getUsedMinerals() {
        return usedMinerals;
    }

    public CategoryScoreDetails getUsedVespene() {
        return usedVespene;
    }

    public CategoryScoreDetails getTotalUsedMinerals() {
        return totalUsedMinerals;
    }

    public CategoryScoreDetails getTotalUsedVespene() {
        return totalUsedVespene;
    }

    public VitalScoreDetails getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public VitalScoreDetails getTotalDamageTaken() {
        return totalDamageTaken;
    }

    public VitalScoreDetails getTotalHealed() {
        return totalHealed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoreDetails that = (ScoreDetails) o;

        return Float.compare(that.idleProductionTime, idleProductionTime) == 0 &&
                Float.compare(that.idleWorkerTime, idleWorkerTime) == 0 &&
                Float.compare(that.totalValueUnits, totalValueUnits) == 0 &&
                Float.compare(that.totalValueStructures, totalValueStructures) == 0 &&
                Float.compare(that.killedValueUnits, killedValueUnits) == 0 &&
                Float.compare(that.killedValueStructures, killedValueStructures) == 0 &&
                Float.compare(that.collectedMinerals, collectedMinerals) == 0 &&
                Float.compare(that.collectedVespene, collectedVespene) == 0 &&
                Float.compare(that.collectionRateMinerals, collectionRateMinerals) == 0 &&
                Float.compare(that.collectionRateVespene, collectionRateVespene) == 0 &&
                Float.compare(that.spentMinerals, spentMinerals) == 0 &&
                Float.compare(that.spentVespene, spentVespene) == 0 &&
                foodUsed.equals(that.foodUsed) &&
                killedMinerals.equals(that.killedMinerals) &&
                killedVespene.equals(that.killedVespene) &&
                lostMinerals.equals(that.lostMinerals) &&
                lostVespene.equals(that.lostVespene) &&
                friendlyFireMinerals.equals(that.friendlyFireMinerals) &&
                friendlyFireVespene.equals(that.friendlyFireVespene) &&
                usedMinerals.equals(that.usedMinerals) &&
                usedVespene.equals(that.usedVespene) &&
                totalUsedMinerals.equals(that.totalUsedMinerals) &&
                totalUsedVespene.equals(that.totalUsedVespene) &&
                totalDamageDealt.equals(that.totalDamageDealt) &&
                totalDamageTaken.equals(that.totalDamageTaken) &&
                totalHealed.equals(that.totalHealed);
    }

    @Override
    public int hashCode() {
        int result = (idleProductionTime != +0.0f ? Float.floatToIntBits(idleProductionTime) : 0);
        result = 31 * result + (idleWorkerTime != +0.0f ? Float.floatToIntBits(idleWorkerTime) : 0);
        result = 31 * result + (totalValueUnits != +0.0f ? Float.floatToIntBits(totalValueUnits) : 0);
        result = 31 * result + (totalValueStructures != +0.0f ? Float.floatToIntBits(totalValueStructures) : 0);
        result = 31 * result + (killedValueUnits != +0.0f ? Float.floatToIntBits(killedValueUnits) : 0);
        result = 31 * result + (killedValueStructures != +0.0f ? Float.floatToIntBits(killedValueStructures) : 0);
        result = 31 * result + (collectedMinerals != +0.0f ? Float.floatToIntBits(collectedMinerals) : 0);
        result = 31 * result + (collectedVespene != +0.0f ? Float.floatToIntBits(collectedVespene) : 0);
        result = 31 * result + (collectionRateMinerals != +0.0f ? Float.floatToIntBits(collectionRateMinerals) : 0);
        result = 31 * result + (collectionRateVespene != +0.0f ? Float.floatToIntBits(collectionRateVespene) : 0);
        result = 31 * result + (spentMinerals != +0.0f ? Float.floatToIntBits(spentMinerals) : 0);
        result = 31 * result + (spentVespene != +0.0f ? Float.floatToIntBits(spentVespene) : 0);
        result = 31 * result + foodUsed.hashCode();
        result = 31 * result + killedMinerals.hashCode();
        result = 31 * result + killedVespene.hashCode();
        result = 31 * result + lostMinerals.hashCode();
        result = 31 * result + lostVespene.hashCode();
        result = 31 * result + friendlyFireMinerals.hashCode();
        result = 31 * result + friendlyFireVespene.hashCode();
        result = 31 * result + usedMinerals.hashCode();
        result = 31 * result + usedVespene.hashCode();
        result = 31 * result + totalUsedMinerals.hashCode();
        result = 31 * result + totalUsedVespene.hashCode();
        result = 31 * result + totalDamageDealt.hashCode();
        result = 31 * result + totalDamageTaken.hashCode();
        result = 31 * result + totalHealed.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
