package com.github.ocraft.s2client.protocol.observation;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class PlayerResult implements Serializable {

    private static final long serialVersionUID = -9025162774544968860L;

    private final int playerId;
    private final Result result;

    private PlayerResult(Sc2Api.PlayerResult sc2ApiPlayerResult) {
        this.playerId = tryGet(
                Sc2Api.PlayerResult::getPlayerId, Sc2Api.PlayerResult::hasPlayerId
        ).apply(sc2ApiPlayerResult).orElseThrow(required("player id"));

        this.result = tryGet(
                Sc2Api.PlayerResult::getResult, Sc2Api.PlayerResult::hasResult
        ).apply(sc2ApiPlayerResult).map(Result::from).orElseThrow(required("result"));
    }

    public static PlayerResult from(Sc2Api.PlayerResult sc2ApiPlayerResult) {
        require("sc2api player result", sc2ApiPlayerResult);
        return new PlayerResult(sc2ApiPlayerResult);
    }

    public int getPlayerId() {
        return playerId;
    }

    public Result getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerResult)) return false;

        PlayerResult that = (PlayerResult) o;

        return playerId == that.playerId && result == that.result;
    }

    @Override
    public int hashCode() {
        int result1 = playerId;
        result1 = 31 * result1 + result.hashCode();
        return result1;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
