package com.github.ocraft.s2client.protocol.game;

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
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;
import com.github.ocraft.s2client.protocol.syntax.request.*;

import java.util.Objects;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Defaults.defaultSpatialSetup;
import static com.github.ocraft.s2client.protocol.Defaults.defaultSpatialSetupForRender;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class InterfaceOptions implements Sc2ApiSerializable<Sc2Api.InterfaceOptions> {

    private static final long serialVersionUID = -1255698885776689016L;

    private final boolean raw;
    private final boolean score;
    private final SpatialCameraSetup featureLayer;
    private final SpatialCameraSetup render;
    private final Boolean showCloaked;
    private final Boolean rawAffectsSelection;
    private final Boolean rawCropToPlayableArea;

    private InterfaceOptions(Builder builder) {
        this.raw = builder.raw;
        this.score = builder.score;
        this.featureLayer = builder.featureLayer;
        this.render = builder.render;
        this.showCloaked = builder.showCloaked;
        this.rawAffectsSelection = builder.rawAffectsSelection;
        this.rawCropToPlayableArea = builder.rawCropToPlayableArea;
    }

    private InterfaceOptions(Sc2Api.InterfaceOptions sc2ApiInterfaceOptions) {
        raw = tryGet(Sc2Api.InterfaceOptions::getRaw, Sc2Api.InterfaceOptions::hasRaw)
                .apply(sc2ApiInterfaceOptions).orElse(false);

        score = tryGet(Sc2Api.InterfaceOptions::getScore, Sc2Api.InterfaceOptions::hasScore)
                .apply(sc2ApiInterfaceOptions).orElse(false);

        featureLayer = tryGet(Sc2Api.InterfaceOptions::getFeatureLayer, Sc2Api.InterfaceOptions::hasFeatureLayer)
                .apply(sc2ApiInterfaceOptions).map(SpatialCameraSetup::from).orElse(nothing());

        render = tryGet(Sc2Api.InterfaceOptions::getRender, Sc2Api.InterfaceOptions::hasRender)
                .apply(sc2ApiInterfaceOptions).map(SpatialCameraSetup::from).orElse(nothing());

        showCloaked = tryGet(Sc2Api.InterfaceOptions::getShowCloaked, Sc2Api.InterfaceOptions::hasShowCloaked)
                .apply(sc2ApiInterfaceOptions).orElse(nothing());

        rawAffectsSelection = tryGet(
                Sc2Api.InterfaceOptions::getRawAffectsSelection, Sc2Api.InterfaceOptions::hasRawAffectsSelection
        ).apply(sc2ApiInterfaceOptions).orElse(nothing());

        rawCropToPlayableArea = tryGet(
                Sc2Api.InterfaceOptions::getRawCropToPlayableArea, Sc2Api.InterfaceOptions::hasRawCropToPlayableArea
        ).apply(sc2ApiInterfaceOptions).orElse(nothing());
    }

    public static final class Builder implements InterfaceOptionsSyntax, ScoreSyntax, FeatureLayerSyntax, RenderSyntax,
            RawSyntax, InterfaceSettingsSyntax {

        private boolean raw;
        private boolean score;
        private SpatialCameraSetup featureLayer;
        private SpatialCameraSetup render;
        private Boolean showCloaked;
        private Boolean rawAffectsSelection;
        private Boolean rawCropToPlayableArea;

        @Override
        public RawSyntax raw() {
            this.raw = true;
            return this;
        }

        @Override
        public FeatureLayerSyntax score() {
            this.score = true;
            return this;
        }

        @Override
        public RenderSyntax featureLayer() {
            featureLayer = defaultSpatialSetup();
            return this;
        }

        @Override
        public RenderSyntax featureLayer(SpatialCameraSetup featureLayer) {
            this.featureLayer = featureLayer;
            return this;
        }

        @Override
        public RenderSyntax featureLayer(BuilderSyntax<SpatialCameraSetup> featureLayer) {
            return featureLayer(featureLayer.build());
        }

        @Override
        public BuilderSyntax<InterfaceOptions> render() {
            this.render = defaultSpatialSetupForRender();
            return this;
        }

        @Override
        public BuilderSyntax<InterfaceOptions> render(SpatialCameraSetup render) {
            this.render = render;
            return this;
        }

        @Override
        public RawSyntax rawAffectsSelection(Boolean value) {
            rawAffectsSelection = value;
            return this;
        }

        @Override
        public RawSyntax rawCropToPlayableArea(Boolean value) {
            rawCropToPlayableArea = true;
            return this;
        }

        @Override
        public InterfaceOptionsSyntax showCloaked(Boolean value) {
            showCloaked = value;
            return this;
        }

        @Override
        public BuilderSyntax<InterfaceOptions> render(BuilderSyntax<SpatialCameraSetup> render) {
            return render(render.build());
        }

        @Override
        public InterfaceOptions build() {
            if (!oneOfInterfaceOptionIsSet()) {
                throw new IllegalArgumentException("one of interface options is required");
            }
            return new InterfaceOptions(this);
        }

        private boolean oneOfInterfaceOptionIsSet() {
            return raw || score || isSet(featureLayer) || isSet(render);
        }
    }

    public static InterfaceOptionsSyntax interfaces() {
        return new Builder();
    }

    public static InterfaceOptions from(Sc2Api.InterfaceOptions sc2ApiInterfaceOptions) {
        require("sc2api interface options", sc2ApiInterfaceOptions);
        return new InterfaceOptions(sc2ApiInterfaceOptions);
    }

    @Override
    public Sc2Api.InterfaceOptions toSc2Api() {
        Sc2Api.InterfaceOptions.Builder aSc2ApiInterfaceOptions = Sc2Api.InterfaceOptions.newBuilder()
                .setRaw(raw)
                .setScore(score);

        getFeatureLayer().map(SpatialCameraSetup::toSc2Api).ifPresent(aSc2ApiInterfaceOptions::setFeatureLayer);
        getRender().map(SpatialCameraSetup::toSc2Api).ifPresent(aSc2ApiInterfaceOptions::setRender);
        getShowCloaked().ifPresent(aSc2ApiInterfaceOptions::setShowCloaked);
        getRawAffectsSelection().ifPresent(aSc2ApiInterfaceOptions::setRawAffectsSelection);
        getRawCropToPlayableArea().ifPresent(aSc2ApiInterfaceOptions::setRawCropToPlayableArea);

        return aSc2ApiInterfaceOptions.build();
    }

    public boolean isRaw() {
        return raw;
    }

    public boolean isScore() {
        return score;
    }

    public Optional<SpatialCameraSetup> getFeatureLayer() {
        return Optional.ofNullable(featureLayer);
    }

    public Optional<SpatialCameraSetup> getRender() {
        return Optional.ofNullable(render);
    }

    public Optional<Boolean> getShowCloaked() {
        return Optional.ofNullable(showCloaked);
    }

    public Optional<Boolean> getRawAffectsSelection() {
        return Optional.ofNullable(rawAffectsSelection);
    }

    public Optional<Boolean> getRawCropToPlayableArea() {
        return Optional.ofNullable(rawCropToPlayableArea);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterfaceOptions that = (InterfaceOptions) o;

        if (raw != that.raw) return false;
        if (score != that.score) return false;
        if (!Objects.equals(featureLayer, that.featureLayer)) return false;
        if (!Objects.equals(render, that.render)) return false;
        if (!Objects.equals(showCloaked, that.showCloaked)) return false;
        if (!Objects.equals(rawAffectsSelection, that.rawAffectsSelection))
            return false;
        return Objects.equals(rawCropToPlayableArea, that.rawCropToPlayableArea);
    }

    @Override
    public int hashCode() {
        int result = (raw ? 1 : 0);
        result = 31 * result + (score ? 1 : 0);
        result = 31 * result + (featureLayer != null ? featureLayer.hashCode() : 0);
        result = 31 * result + (render != null ? render.hashCode() : 0);
        result = 31 * result + (showCloaked != null ? showCloaked.hashCode() : 0);
        result = 31 * result + (rawAffectsSelection != null ? rawAffectsSelection.hashCode() : 0);
        result = 31 * result + (rawCropToPlayableArea != null ? rawCropToPlayableArea.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
