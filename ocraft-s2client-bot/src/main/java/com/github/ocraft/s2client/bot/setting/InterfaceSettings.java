package com.github.ocraft.s2client.bot.setting;

/*-
 * #%L
 * ocraft-s2client-bot
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

import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;

import java.util.Objects;
import java.util.Optional;

public final class InterfaceSettings {
    private final SpatialCameraSetup featureLayerSettings;
    private final SpatialCameraSetup renderSettings;
    private final Boolean showCloaked;
    private final Boolean rawAffectsSelection;
    private final Boolean rawCropToPlayableArea;

    public InterfaceSettings(SpatialCameraSetup featureLayerSettings, SpatialCameraSetup renderSettings) {
        this(featureLayerSettings, renderSettings, null, null, null);
    }

    public InterfaceSettings(
            SpatialCameraSetup featureLayerSettings,
            SpatialCameraSetup renderSettings,
            Boolean showCloaked,
            Boolean rawAffectsSelection,
            Boolean rawCropToPlayableArea) {
        this.featureLayerSettings = featureLayerSettings;
        this.renderSettings = renderSettings;
        this.showCloaked = showCloaked;
        this.rawAffectsSelection = rawAffectsSelection;
        this.rawCropToPlayableArea = rawCropToPlayableArea;
    }


    public Optional<SpatialCameraSetup> getFeatureLayerSettings() {
        return Optional.ofNullable(featureLayerSettings);
    }

    public Optional<SpatialCameraSetup> getRenderSettings() {
        return Optional.ofNullable(renderSettings);
    }

    public Boolean getShowCloaked() {
        return showCloaked;
    }

    public Boolean getRawAffectsSelection() {
        return rawAffectsSelection;
    }

    public Boolean getRawCropToPlayableArea() {
        return rawCropToPlayableArea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterfaceSettings that = (InterfaceSettings) o;

        if (!Objects.equals(featureLayerSettings, that.featureLayerSettings))
            return false;
        if (!Objects.equals(renderSettings, that.renderSettings))
            return false;
        if (!Objects.equals(showCloaked, that.showCloaked)) return false;
        if (!Objects.equals(rawAffectsSelection, that.rawAffectsSelection))
            return false;
        return Objects.equals(rawCropToPlayableArea, that.rawCropToPlayableArea);

    }

    @Override
    public int hashCode() {
        int result = featureLayerSettings != null ? featureLayerSettings.hashCode() : 0;
        result = 31 * result + (renderSettings != null ? renderSettings.hashCode() : 0);
        result = 31 * result + (showCloaked != null ? showCloaked.hashCode() : 0);
        result = 31 * result + (rawAffectsSelection != null ? rawAffectsSelection.hashCode() : 0);
        result = 31 * result + (rawCropToPlayableArea != null ? rawCropToPlayableArea.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InterfaceSettings{" +
                "featureLayerSettings=" + featureLayerSettings +
                ", renderSettings=" + renderSettings +
                ", showCloaked=" + showCloaked +
                ", rawAffectsSelection=" + rawAffectsSelection +
                ", rawCropToPlayableArea=" + rawCropToPlayableArea +
                '}';
    }
}
