package com.github.ocraft.s2client.protocol.request;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.BattlenetMap;
import com.github.ocraft.s2client.protocol.game.LocalMap;
import com.github.ocraft.s2client.protocol.game.PlayerSetup;
import com.github.ocraft.s2client.protocol.response.ResponseType;
import com.github.ocraft.s2client.protocol.syntax.request.*;

import java.util.*;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Preconditions.oneOfIsSet;
import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.Arrays.asList;

public final class RequestCreateGame extends Request {

    private static final long serialVersionUID = -505916634411290169L;

    private final boolean disableFog;
    private final boolean realTime;
    private final Integer randomSeed;
    private final List<PlayerSetup> playerSetups;
    private final BattlenetMap battlenetMap;
    private final LocalMap localMap;

    private RequestCreateGame(Builder builder) {
        this.disableFog = builder.disableFog;
        this.realTime = builder.realTime;
        this.randomSeed = builder.randomSeed;
        this.playerSetups = Collections.unmodifiableList(builder.playerSetups);
        this.battlenetMap = builder.battlenetMap;
        this.localMap = builder.localMap;
    }

    public static RequestCreateGameSyntax createGame() {
        return new Builder();
    }

    public static final class Builder implements RequestCreateGameSyntax, WithPlayerSetupSyntax, DisableFogSyntax,
            RealtimeSyntax, WithRandomSeedSyntax {

        private boolean disableFog;
        private boolean realTime;
        private Integer randomSeed;
        private List<PlayerSetup> playerSetups = new ArrayList<>();
        private BattlenetMap battlenetMap;
        private LocalMap localMap;

        @Override
        public WithPlayerSetupSyntax onBattlenetMap(BattlenetMap battlenetMap) {
            this.battlenetMap = battlenetMap;
            this.localMap = nothing();
            return this;
        }

        @Override
        public WithPlayerSetupSyntax onLocalMap(LocalMap localMap) {
            this.battlenetMap = nothing();
            this.localMap = localMap;
            return this;
        }

        @Override
        public DisableFogSyntax withPlayerSetup(PlayerSetup... playerSetups) {
            this.playerSetups.addAll(asList(playerSetups));
            return this;
        }

        @Override
        public RealtimeSyntax disableFog() {
            this.disableFog = true;
            return this;
        }

        @Override
        public WithRandomSeedSyntax realTime() {
            this.realTime = true;
            return this;
        }

        @Override
        public WithRandomSeedSyntax realTime(boolean value) {
            this.realTime = value;
            return this;
        }

        @Override
        public BuilderSyntax<RequestCreateGame> withRandomSeed(int randomSeed) {
            this.randomSeed = randomSeed;
            return this;
        }

        @Override
        public RequestCreateGame build() {
            oneOfIsSet("map data", battlenetMap, localMap);
            requireNotEmpty("player setup", playerSetups);
            return new RequestCreateGame(this);
        }

    }

    @Override
    public Sc2Api.Request toSc2Api() {
        Sc2Api.RequestCreateGame.Builder aSc2ApiCreateGame = Sc2Api.RequestCreateGame.newBuilder()
                .setDisableFog(disableFog)
                .setRealtime(realTime);

        getRandomSeed().ifPresent(aSc2ApiCreateGame::setRandomSeed);
        getBattlenetMap().map(BattlenetMap::toSc2Api).ifPresent(aSc2ApiCreateGame::setBattlenetMapName);
        getLocalMap().map(LocalMap::toSc2Api).ifPresent(aSc2ApiCreateGame::setLocalMap);

        playerSetups.stream().map(PlayerSetup::toSc2Api).forEach(aSc2ApiCreateGame::addPlayerSetup);

        return Sc2Api.Request.newBuilder().setCreateGame(aSc2ApiCreateGame.build()).build();
    }

    @Override
    public ResponseType responseType() {
        return ResponseType.CREATE_GAME;
    }

    public boolean isDisableFog() {
        return disableFog;
    }

    public boolean isRealTime() {
        return realTime;
    }

    public Optional<Integer> getRandomSeed() {
        return Optional.ofNullable(randomSeed);
    }

    public List<PlayerSetup> getPlayerSetups() {
        return playerSetups;
    }

    public Optional<BattlenetMap> getBattlenetMap() {
        return Optional.ofNullable(battlenetMap);
    }

    public Optional<LocalMap> getLocalMap() {
        return Optional.ofNullable(localMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RequestCreateGame that = (RequestCreateGame) o;

        if (disableFog != that.disableFog) return false;
        if (realTime != that.realTime) return false;
        if (!Objects.equals(randomSeed, that.randomSeed)) return false;
        if (!playerSetups.equals(that.playerSetups)) return false;
        if (!Objects.equals(battlenetMap, that.battlenetMap)) return false;
        return Objects.equals(localMap, that.localMap);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (disableFog ? 1 : 0);
        result = 31 * result + (realTime ? 1 : 0);
        result = 31 * result + (randomSeed != null ? randomSeed.hashCode() : 0);
        result = 31 * result + playerSetups.hashCode();
        result = 31 * result + (battlenetMap != null ? battlenetMap.hashCode() : 0);
        result = 31 * result + (localMap != null ? localMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
