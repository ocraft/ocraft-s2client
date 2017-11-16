package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Common;
import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;
import com.github.ocraft.s2client.protocol.game.Observer;
import com.github.ocraft.s2client.protocol.game.PortSet;
import com.github.ocraft.s2client.protocol.game.Race;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Defaults.defaultInterfaces;
import static com.github.ocraft.s2client.protocol.Defaults.defaultSpatialSetup;
import static com.github.ocraft.s2client.protocol.Fixtures.PLAYER_ID;
import static com.github.ocraft.s2client.protocol.Fixtures.aSpatialSetup;
import static com.github.ocraft.s2client.protocol.game.InterfaceOptions.interfaces;
import static com.github.ocraft.s2client.protocol.game.MultiplayerOptions.multiplayerSetup;
import static com.github.ocraft.s2client.protocol.request.RequestJoinGame.joinGame;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestJoinGameTest {

    @Test
    void serializesToSc2ApiRequestJoinGame() {
        assertThatAreFieldsInRequestAreSerialized(
                joinGame()
                        .as(Race.TERRAN)
                        .use(interfaces().raw().score()
                                .featureLayer(defaultSpatialSetup())
                                .render(defaultSpatialSetup()).build())
                        .with(multiplayerSetup()
                                .sharedPort(1)
                                .serverPort(PortSet.of(2, 3))
                                .clientPorts(PortSet.of(4, 5), PortSet.of(6, 7)).build())
                        .build().toSc2Api());
    }

    private void assertThatAreFieldsInRequestAreSerialized(Sc2Api.Request sc2ApiRequest) {
        assertThat(sc2ApiRequest.hasJoinGame()).as("sc2api request has join game").isTrue();
        Sc2Api.RequestJoinGame sc2ApiRequestJoinGame = sc2ApiRequest.getJoinGame();
        assertThat(sc2ApiRequestJoinGame.hasRace()).as("case of participant is race").isTrue();
        assertThat(sc2ApiRequestJoinGame.getRace()).as("race of participant").isEqualTo(Common.Race.Terran);
        assertThat(sc2ApiRequestJoinGame.hasOptions()).as("interface options are set").isTrue();

        assertThatMultiplayerOptionsAreSerialized(sc2ApiRequestJoinGame);
    }

    private void assertThatMultiplayerOptionsAreSerialized(Sc2Api.RequestJoinGame sc2ApiRequestJoinGame) {
        assertThat(sc2ApiRequestJoinGame.hasServerPorts()).as("server ports are serialized").isTrue();
        assertThat(sc2ApiRequestJoinGame.hasSharedPort()).as("shared port is serialized").isTrue();
        assertThat(sc2ApiRequestJoinGame.getClientPortsList()).as("client ports are serialized").isNotEmpty();
    }

    @Test
    void serializesToSc2ApiRequestJoinGameUsingBuilders() {
        assertThatAreFieldsInRequestAreSerialized(
                joinGame()
                        .as(Race.TERRAN)
                        .use(interfaces().raw().score()
                                .featureLayer(aSpatialSetup())
                                .render(aSpatialSetup()))
                        .with(multiplayerSetup()
                                .sharedPort(1)
                                .serverPort(PortSet.of(2, 3))
                                .clientPorts(PortSet.of(4, 5), PortSet.of(6, 7)))
                        .build().toSc2Api());
    }

    @Test
    void serializesOnlyRecentlyAddedParticipant() {
        assertCorrectParticipantCase(
                fullAccessTo(joinGame().as(Race.TERRAN)).as(Observer.of(PLAYER_ID)).build().toSc2Api());
        assertCorrectParticipantCaseAfterOrderChange(
                fullAccessTo(joinGame().as(Observer.of(PLAYER_ID))).as(Race.ZERG).build().toSc2Api());
    }

    private RequestJoinGame.Builder fullAccessTo(Object obj) {
        return (RequestJoinGame.Builder) obj;
    }

    private void assertCorrectParticipantCase(Sc2Api.Request sc2ApiRequest) {
        Sc2Api.RequestJoinGame sc2ApiRequestJoinGame = sc2ApiRequest.getJoinGame();
        assertThat(sc2ApiRequestJoinGame.hasRace()).as("case of participant is race").isFalse();
        assertThat(sc2ApiRequestJoinGame.hasObservedPlayerId()).as("case of participant is observer").isTrue();
        assertThat(sc2ApiRequestJoinGame.getObservedPlayerId()).as("observed player id").isEqualTo(PLAYER_ID);
    }

    private void assertCorrectParticipantCaseAfterOrderChange(Sc2Api.Request sc2ApiRequest) {
        Sc2Api.RequestJoinGame sc2ApiRequestJoinGame = sc2ApiRequest.getJoinGame();

        assertThat(sc2ApiRequestJoinGame.hasRace()).as("case of participant is race").isTrue();
        assertThat(sc2ApiRequestJoinGame.hasObservedPlayerId()).as("case of participant is observer").isFalse();
        assertThat(sc2ApiRequestJoinGame.getRace()).as("race of participant").isEqualTo(Common.Race.Zerg);
    }

    @Test
    void throwsExceptionWhenParticipantCaseIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(joinGame()).build())
                .withMessage("participant case is required");
    }

    @Test
    void serializesDefaultInterfaceOptionsIfNotSet() {
        assertThat(joinGame().as(Race.ZERG).build().toSc2Api().getJoinGame().getOptions())
                .as("default interface options are serialized")
                .isEqualTo(defaultInterfaces().toSc2Api());
    }

    @Test
    void throwsExceptionIfInterfaceOptionsAreNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> joinGame().as(Race.RANDOM).use((InterfaceOptions) nothing()).build())
                .withMessage("interface options are required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RequestJoinGame.class)
                .withIgnoredFields("nanoTime").withNonnullFields("interfaceOptions").verify();
    }
}