package com.github.ocraft.s2client.protocol.response;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.game.GameStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResponsePingTest {

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHavePing() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponsePing.from(nothing()))
                .withMessage("provided argument doesn't have ping response");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponsePing.from(aSc2ApiResponse().build()))
                .withMessage("provided argument doesn't have ping response");
    }

    @Test
    void throwsExceptionWhenSc2ApiResponseDoesNotHaveAllFieldsSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponsePing.from(
                        sc2ApiResponsePingWithout(Sc2Api.ResponsePing.Builder::clearGameVersion)))
                .withMessage("game version is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponsePing.from(
                        sc2ApiResponsePingWithout(Sc2Api.ResponsePing.Builder::clearDataVersion)))
                .withMessage("data version is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponsePing.from(
                        sc2ApiResponsePingWithout(Sc2Api.ResponsePing.Builder::clearBaseBuild)))
                .withMessage("base build is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ResponsePing.from(
                        sc2ApiResponsePingWithout(Sc2Api.ResponsePing.Builder::clearDataBuild)))
                .withMessage("data build is required");
    }

    private Sc2Api.Response sc2ApiResponsePingWithout(
            Function<Sc2Api.ResponsePing.Builder, Sc2Api.ResponsePing.Builder> clear) {
        return aSc2ApiResponse().setPing(clear.apply(sc2ApiResponsePing().toBuilder()).build()).build();
    }

    @Test
    void convertsSc2ApiResponsePingToResponsePing() {
        ResponsePing responsePing = ResponsePing.from(sc2ApiResponseWithPing());

        assertThatAllFieldsAreProperlyConverted(responsePing);
    }

    private void assertThatAllFieldsAreProperlyConverted(ResponsePing responsePing) {
        assertThat(responsePing.getType()).as("type of ping response").isEqualTo(ResponseType.PING);
        assertThat(responsePing.getStatus()).as("status of ping response").isEqualTo(GameStatus.IN_GAME);
        assertThat(responsePing.getGameVersion()).as("game version in ping response").isEqualTo(GAME_VERSION);
        assertThat(responsePing.getDataVersion()).as("data version in ping response").isEqualTo(DATA_VERSION);
        assertThat(responsePing.getDataBuild()).as("data build in ping response").isEqualTo(DATA_BUILD);
        assertThat(responsePing.getBaseBuild()).as("base build in ping response").isEqualTo(BASE_BUILD);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ResponsePing.class)
                .withIgnoredFields("nanoTime")
                .withNonnullFields("type", "status", "gameVersion", "dataVersion", "dataBuild", "baseBuild")
                .withRedefinedSuperclass()
                .verify();
    }

}