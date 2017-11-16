package com.github.ocraft.s2client.protocol.request;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.*;
import com.github.ocraft.s2client.protocol.syntax.request.MultiplayerSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.RequestJoinGameSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.UseInterfaceSyntax;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Defaults.defaultInterfaces;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static java.util.stream.Collectors.toSet;

public final class RequestJoinGame extends Request {

    private static final long serialVersionUID = 6978407850554692095L;

    private final Race race;
    private final Observer observerOf;
    private final InterfaceOptions interfaceOptions;
    private final MultiplayerOptions multiplayerOptions;

    private RequestJoinGame(Builder builder) {
        this.race = builder.race;
        this.observerOf = builder.observer;
        this.interfaceOptions = builder.interfaceOptions;
        this.multiplayerOptions = builder.multiplayerOptions;
    }

    public static final class Builder implements RequestJoinGameSyntax, UseInterfaceSyntax {

        private Race race;
        private Observer observer;
        private InterfaceOptions interfaceOptions = defaultInterfaces();
        private MultiplayerOptions multiplayerOptions;

        @Override
        public UseInterfaceSyntax as(Race race) {
            this.observer = nothing();
            this.race = race;
            return this;
        }

        @Override
        public UseInterfaceSyntax as(Observer observer) {
            this.observer = observer;
            this.race = nothing();
            return this;
        }

        @Override
        public MultiplayerSyntax use(InterfaceOptions interfaceOptions) {
            this.interfaceOptions = interfaceOptions;
            return this;
        }

        @Override
        public MultiplayerSyntax use(BuilderSyntax<InterfaceOptions> interfaceOptions) {
            return use(interfaceOptions.build());
        }

        @Override
        public BuilderSyntax<RequestJoinGame> with(MultiplayerOptions multiplayerOptions) {
            this.multiplayerOptions = multiplayerOptions;
            return this;
        }

        @Override
        public BuilderSyntax<RequestJoinGame> with(
                BuilderSyntax<MultiplayerOptions> multiplayerOptions) {
            return with(multiplayerOptions.build());
        }

        @Override
        public RequestJoinGame build() {
            if (!oneOfParticipantIsSet()) throw new IllegalArgumentException("participant case is required");
            if (!interfaceOptionsAreSet()) throw new IllegalArgumentException("interface options are required");
            return new RequestJoinGame(this);
        }

        private boolean oneOfParticipantIsSet() {
            return isSet(observer) || isSet(race);
        }

        private boolean interfaceOptionsAreSet() {
            return isSet(interfaceOptions);
        }

    }

    public static RequestJoinGameSyntax joinGame() {
        return new Builder();
    }

    @Override
    public Sc2Api.Request toSc2Api() {
        Sc2Api.RequestJoinGame.Builder aSc2ApiRequestJoinGame = Sc2Api.RequestJoinGame.newBuilder()
                .setOptions(interfaceOptions.toSc2Api());

        getRace().map(Race::toSc2Api).ifPresent(aSc2ApiRequestJoinGame::setRace);
        getObserverOf().map(Observer::toSc2Api).ifPresent(aSc2ApiRequestJoinGame::setObservedPlayerId);
        getMultiplayerOptions().ifPresent(options -> aSc2ApiRequestJoinGame
                .setSharedPort(options.getSharedPort())
                .setServerPorts(options.getServerPort().toSc2Api())
                .addAllClientPorts(options.getClientPorts().stream().map(PortSet::toSc2Api).collect(toSet())));

        return Sc2Api.Request.newBuilder()
                .setJoinGame(aSc2ApiRequestJoinGame.build())
                .build();
    }

    public Optional<Race> getRace() {
        return Optional.ofNullable(race);
    }

    public Optional<Observer> getObserverOf() {
        return Optional.ofNullable(observerOf);
    }

    public InterfaceOptions getInterfaceOptions() {
        return interfaceOptions;
    }

    public Optional<MultiplayerOptions> getMultiplayerOptions() {
        return Optional.ofNullable(multiplayerOptions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestJoinGame that = (RequestJoinGame) o;

        return race == that.race &&
                (observerOf != null ? observerOf.equals(that.observerOf) : that.observerOf == null) &&
                interfaceOptions.equals(that.interfaceOptions) &&
                (multiplayerOptions != null
                        ? multiplayerOptions.equals(that.multiplayerOptions)
                        : that.multiplayerOptions == null);
    }

    @Override
    public int hashCode() {
        int result = race != null ? race.hashCode() : 0;
        result = 31 * result + (observerOf != null ? observerOf.hashCode() : 0);
        result = 31 * result + interfaceOptions.hashCode();
        result = 31 * result + (multiplayerOptions != null ? multiplayerOptions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
