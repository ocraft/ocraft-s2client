package com.github.ocraft.s2client.protocol.observation;

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
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.observation.raw.ObservationRaw;
import com.github.ocraft.s2client.protocol.observation.spatial.ObservationFeatureLayer;
import com.github.ocraft.s2client.protocol.observation.spatial.ObservationRender;
import com.github.ocraft.s2client.protocol.observation.ui.ObservationUi;
import com.github.ocraft.s2client.protocol.score.Score;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toSet;

public final class Observation implements Serializable {

    private static final long serialVersionUID = 5875158852342073808L;

    private final int gameLoop;
    private final PlayerCommon playerCommon;
    private final Set<Alert> alerts;
    private final Set<AvailableAbility> availableAbilities;
    private final Score score;
    private final ObservationRaw raw;
    private final ObservationFeatureLayer featureLayer;
    private final ObservationRender render;
    private final ObservationUi ui;

    private Observation(Sc2Api.Observation sc2ApiObservation) {
        gameLoop = tryGet(
                Sc2Api.Observation::getGameLoop, Sc2Api.Observation::hasGameLoop
        ).apply(sc2ApiObservation).orElseThrow(required("game loop"));

        playerCommon = tryGet(
                Sc2Api.Observation::getPlayerCommon, Sc2Api.Observation::hasPlayerCommon
        ).apply(sc2ApiObservation).map(PlayerCommon::from).orElseThrow(required("player common"));

        alerts = sc2ApiObservation.getAlertsList().stream().map(Alert::from).collect(toSet());

        availableAbilities = sc2ApiObservation.getAbilitiesList().stream().map(AvailableAbility::from).collect(toSet());

        score = tryGet(
                Sc2Api.Observation::getScore, Sc2Api.Observation::hasScore
        ).apply(sc2ApiObservation).map(Score::from).orElse(nothing());

        raw = tryGet(
                Sc2Api.Observation::getRawData, Sc2Api.Observation::hasRawData
        ).apply(sc2ApiObservation).map(ObservationRaw::from).orElse(nothing());

        featureLayer = tryGet(
                Sc2Api.Observation::getFeatureLayerData, Sc2Api.Observation::hasFeatureLayerData
        ).apply(sc2ApiObservation).map(ObservationFeatureLayer::from).orElse(nothing());

        render = tryGet(
                Sc2Api.Observation::getRenderData, Sc2Api.Observation::hasRenderData
        ).apply(sc2ApiObservation).map(ObservationRender::from).orElse(nothing());

        if (!oneOfInterfacesIsSet()) throw new IllegalArgumentException("one of interfaces is required");

        ui = tryGet(
                Sc2Api.Observation::getUiData, Sc2Api.Observation::hasUiData
        ).apply(sc2ApiObservation).map(ObservationUi::from).orElse(nothing());
    }

    private boolean oneOfInterfacesIsSet() {
        return isSet(raw) || isSet(featureLayer) || isSet(render);
    }

    public static Observation from(Sc2Api.Observation sc2ApiObservation) {
        require("sc2api observation", sc2ApiObservation);
        return new Observation(sc2ApiObservation);
    }

    public int getGameLoop() {
        return gameLoop;
    }

    public PlayerCommon getPlayerCommon() {
        return playerCommon;
    }

    public Set<Alert> getAlerts() {
        return new HashSet<>(alerts);
    }

    public Set<AvailableAbility> getAvailableAbilities() {
        return new HashSet<>(availableAbilities);
    }

    public Optional<Score> getScore() {
        return Optional.ofNullable(score);
    }

    public Optional<ObservationRaw> getRaw() {
        return Optional.ofNullable(raw);
    }

    public Optional<ObservationFeatureLayer> getFeatureLayer() {
        return Optional.ofNullable(featureLayer);
    }

    public Optional<ObservationRender> getRender() {
        return Optional.ofNullable(render);
    }

    public Optional<ObservationUi> getUi() {
        return Optional.ofNullable(ui);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Observation that = (Observation) o;

        return gameLoop == that.gameLoop &&
                playerCommon.equals(that.playerCommon) &&
                alerts.equals(that.alerts) &&
                availableAbilities.equals(that.availableAbilities) &&
                (score != null ? score.equals(that.score) : that.score == null) &&
                (raw != null ? raw.equals(that.raw) : that.raw == null) &&
                (featureLayer != null ? featureLayer.equals(that.featureLayer) : that.featureLayer == null) &&
                (render != null ? render.equals(that.render) : that.render == null) &&
                (ui != null ? ui.equals(that.ui) : that.ui == null);
    }

    @Override
    public int hashCode() {
        int result = gameLoop;
        result = 31 * result + playerCommon.hashCode();
        result = 31 * result + alerts.hashCode();
        result = 31 * result + availableAbilities.hashCode();
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (raw != null ? raw.hashCode() : 0);
        result = 31 * result + (featureLayer != null ? featureLayer.hashCode() : 0);
        result = 31 * result + (render != null ? render.hashCode() : 0);
        result = 31 * result + (ui != null ? ui.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
