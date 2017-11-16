/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;
import com.github.ocraft.s2client.protocol.syntax.request.FeatureLayerSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.InterfaceOptionsSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.RenderSyntax;
import com.github.ocraft.s2client.protocol.syntax.request.ScoreSyntax;

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

    private InterfaceOptions(Builder builder) {
        this.raw = builder.raw;
        this.score = builder.score;
        this.featureLayer = builder.featureLayer;
        this.render = builder.render;
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
    }

    public static final class Builder implements InterfaceOptionsSyntax, ScoreSyntax, FeatureLayerSyntax, RenderSyntax {

        private boolean raw;
        private boolean score;
        private SpatialCameraSetup featureLayer;
        private SpatialCameraSetup render;

        @Override
        public ScoreSyntax raw() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterfaceOptions that = (InterfaceOptions) o;

        return raw == that.raw &&
                score == that.score &&
                (featureLayer != null ? featureLayer.equals(that.featureLayer) : that.featureLayer == null) &&
                (render != null ? render.equals(that.render) : that.render == null);
    }

    @Override
    public int hashCode() {
        int result = (raw ? 1 : 0);
        result = 31 * result + (score ? 1 : 0);
        result = 31 * result + (featureLayer != null ? featureLayer.hashCode() : 0);
        result = 31 * result + (render != null ? render.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
