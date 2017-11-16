package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PlayerInfoExtraTest {

    @Test
    void throwsExceptionWhenSc2ApiPlayerInfoExtraIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerInfoExtra.from(nothing()))
                .withMessage("sc2api player info extra is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiPlayerInfoExtra() {
        assertThatAllFieldsAreConverted(PlayerInfoExtra.from(sc2ApiPlayerInfoExtra()));
    }

    private void assertThatAllFieldsAreConverted(PlayerInfoExtra playerInfoExtra) {
        assertThat(playerInfoExtra.getPlayerInfo()).as("player info").isNotNull();
        assertThat(playerInfoExtra.getPlayerResult()).as("player result").isNotNull();
        assertThat(playerInfoExtra.getPlayerMatchMakingRating()).as("player mmr").hasValue(PLAYER_MMR);
        assertThat(playerInfoExtra.getPlayerActionPerMinute()).as("player apm").isEqualTo(PLAYER_APM);
    }

    @Test
    void throwsExceptionWhenSc2ApiPlayerInfoDoesNotExist() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerInfoExtra.from(sc2ApiPlayerInfoExtraWithoutPlayerInfo()))
                .withMessage("player info is required");
    }

    private Sc2Api.PlayerInfoExtra sc2ApiPlayerInfoExtraWithoutPlayerInfo() {
        return without(
                () -> sc2ApiPlayerInfoExtra().toBuilder(),
                Sc2Api.PlayerInfoExtra.Builder::clearPlayerInfo).build();
    }

    @Test
    void throwsExceptionWhenSc2ApiPlayerApmDoesNotExist() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PlayerInfoExtra.from(without(
                        () -> sc2ApiPlayerInfoExtra().toBuilder(),
                        Sc2Api.PlayerInfoExtra.Builder::clearPlayerApm).build()))
                .withMessage("player apm is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(PlayerInfoExtra.class)
                .withNonnullFields("playerInfo", "playerResult")
                .verify();
    }

}