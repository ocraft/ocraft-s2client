package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class PlayerCommon implements Serializable {

    private static final long serialVersionUID = 2942230039841152735L;

    private final int playerId;
    private final int minerals;
    private final int vespene;
    private final int foodCap;
    private final int foodUsed;
    private final int foodArmy;
    private final int foodWorkers;
    private final int idleWorkerCount;
    private final int armyCount;
    private final Integer warpGateCount;
    private final Integer larvaCount;

    private PlayerCommon(Sc2Api.PlayerCommon sc2ApiPlayerCommon) {
        playerId = tryGet(
                Sc2Api.PlayerCommon::getPlayerId, Sc2Api.PlayerCommon::hasPlayerId
        ).apply(sc2ApiPlayerCommon).orElseThrow(required("player id"));

        minerals = tryGet(
                Sc2Api.PlayerCommon::getMinerals, Sc2Api.PlayerCommon::hasMinerals
        ).apply(sc2ApiPlayerCommon).orElseThrow(required("minerals"));

        vespene = tryGet(
                Sc2Api.PlayerCommon::getVespene, Sc2Api.PlayerCommon::hasVespene
        ).apply(sc2ApiPlayerCommon).orElseThrow(required("vespene"));

        foodCap = tryGet(
                Sc2Api.PlayerCommon::getFoodCap, Sc2Api.PlayerCommon::hasFoodCap
        ).apply(sc2ApiPlayerCommon).orElseThrow(required("food cap"));

        foodUsed = tryGet(
                Sc2Api.PlayerCommon::getFoodUsed, Sc2Api.PlayerCommon::hasFoodUsed
        ).apply(sc2ApiPlayerCommon).orElseThrow(required("food used"));

        foodArmy = tryGet(
                Sc2Api.PlayerCommon::getFoodArmy, Sc2Api.PlayerCommon::hasFoodArmy
        ).apply(sc2ApiPlayerCommon).orElseThrow(required("food army"));

        foodWorkers = tryGet(
                Sc2Api.PlayerCommon::getFoodWorkers, Sc2Api.PlayerCommon::hasFoodWorkers
        ).apply(sc2ApiPlayerCommon).orElseThrow(required("food workers"));

        idleWorkerCount = tryGet(
                Sc2Api.PlayerCommon::getIdleWorkerCount, Sc2Api.PlayerCommon::hasIdleWorkerCount
        ).apply(sc2ApiPlayerCommon).orElseThrow(required("idle worker count"));

        armyCount = tryGet(
                Sc2Api.PlayerCommon::getArmyCount, Sc2Api.PlayerCommon::hasArmyCount
        ).apply(sc2ApiPlayerCommon).orElseThrow(required("army count"));

        warpGateCount = tryGet(
                Sc2Api.PlayerCommon::getWarpGateCount, Sc2Api.PlayerCommon::hasWarpGateCount
        ).apply(sc2ApiPlayerCommon).orElse(nothing());

        larvaCount = tryGet(
                Sc2Api.PlayerCommon::getLarvaCount, Sc2Api.PlayerCommon::hasLarvaCount
        ).apply(sc2ApiPlayerCommon).orElse(nothing());
    }

    public static PlayerCommon from(Sc2Api.PlayerCommon sc2ApiPlayerCommon) {
        require("sc2api player common", sc2ApiPlayerCommon);
        return new PlayerCommon(sc2ApiPlayerCommon);
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getMinerals() {
        return minerals;
    }

    public int getVespene() {
        return vespene;
    }

    public int getFoodCap() {
        return foodCap;
    }

    public int getFoodUsed() {
        return foodUsed;
    }

    public int getFoodArmy() {
        return foodArmy;
    }

    public int getFoodWorkers() {
        return foodWorkers;
    }

    public int getIdleWorkerCount() {
        return idleWorkerCount;
    }

    public int getArmyCount() {
        return armyCount;
    }

    public Optional<Integer> getWarpGateCount() {
        return Optional.ofNullable(warpGateCount);
    }

    public Optional<Integer> getLarvaCount() {
        return Optional.ofNullable(larvaCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerCommon that = (PlayerCommon) o;

        return playerId == that.playerId &&
                minerals == that.minerals &&
                vespene == that.vespene &&
                foodCap == that.foodCap &&
                foodUsed == that.foodUsed &&
                foodArmy == that.foodArmy &&
                foodWorkers == that.foodWorkers &&
                idleWorkerCount == that.idleWorkerCount &&
                armyCount == that.armyCount &&
                (warpGateCount != null ? warpGateCount.equals(that.warpGateCount) : that.warpGateCount == null) &&
                (larvaCount != null ? larvaCount.equals(that.larvaCount) : that.larvaCount == null);
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + minerals;
        result = 31 * result + vespene;
        result = 31 * result + foodCap;
        result = 31 * result + foodUsed;
        result = 31 * result + foodArmy;
        result = 31 * result + foodWorkers;
        result = 31 * result + idleWorkerCount;
        result = 31 * result + armyCount;
        result = 31 * result + (warpGateCount != null ? warpGateCount.hashCode() : 0);
        result = 31 * result + (larvaCount != null ? larvaCount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
