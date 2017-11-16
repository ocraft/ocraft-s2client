package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.observation.PlayerResult;

import java.io.Serializable;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class PlayerInfoExtra implements Serializable {

    private static final long serialVersionUID = -3464030523342592347L;

    private final PlayerInfo playerInfo;
    private final PlayerResult playerResult;
    private final Integer playerMmr;
    private final int playerApm;

    private PlayerInfoExtra(Sc2Api.PlayerInfoExtra sc2ApiPlayerInfoExtra) {
        this.playerInfo = tryGet(
                Sc2Api.PlayerInfoExtra::getPlayerInfo, Sc2Api.PlayerInfoExtra::hasPlayerInfo
        ).apply(sc2ApiPlayerInfoExtra).map(PlayerInfo::from).orElseThrow(required("player info"));

        this.playerResult = tryGet(
                Sc2Api.PlayerInfoExtra::getPlayerResult, Sc2Api.PlayerInfoExtra::hasPlayerResult
        ).apply(sc2ApiPlayerInfoExtra).map(PlayerResult::from).orElseThrow(required("player result"));

        this.playerMmr = tryGet(
                Sc2Api.PlayerInfoExtra::getPlayerMmr, Sc2Api.PlayerInfoExtra::hasPlayerMmr
        ).apply(sc2ApiPlayerInfoExtra).orElse(nothing());

        this.playerApm = tryGet(
                Sc2Api.PlayerInfoExtra::getPlayerApm, Sc2Api.PlayerInfoExtra::hasPlayerApm
        ).apply(sc2ApiPlayerInfoExtra).orElseThrow(required("player apm"));
    }

    public static PlayerInfoExtra from(Sc2Api.PlayerInfoExtra sc2ApiPlayerInfoExtra) {
        require("sc2api player info extra", sc2ApiPlayerInfoExtra);
        return new PlayerInfoExtra(sc2ApiPlayerInfoExtra);
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public PlayerResult getPlayerResult() {
        return playerResult;
    }

    public Optional<Integer> getPlayerMatchMakingRating() {
        return Optional.ofNullable(playerMmr);
    }

    public int getPlayerActionPerMinute() {
        return playerApm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerInfoExtra)) return false;

        PlayerInfoExtra that = (PlayerInfoExtra) o;

        return playerApm == that.playerApm &&
                playerInfo.equals(that.playerInfo) &&
                playerResult.equals(that.playerResult) &&
                (playerMmr != null ? playerMmr.equals(that.playerMmr) : that.playerMmr == null);
    }

    @Override
    public int hashCode() {
        int result = playerInfo.hashCode();
        result = 31 * result + playerResult.hashCode();
        result = 31 * result + (playerMmr != null ? playerMmr.hashCode() : 0);
        result = 31 * result + playerApm;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
