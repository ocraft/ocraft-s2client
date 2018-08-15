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

import java.util.Optional;

public final class InterfaceSettings {
    private final SpatialCameraSetup featureLayerSettings;
    private final SpatialCameraSetup renderSettings;

    public InterfaceSettings(SpatialCameraSetup featureLayerSettings, SpatialCameraSetup renderSettings) {
        this.featureLayerSettings = featureLayerSettings;
        this.renderSettings = renderSettings;
    }

    public Optional<SpatialCameraSetup> getFeatureLayerSettings() {
        return Optional.ofNullable(featureLayerSettings);
    }

    public Optional<SpatialCameraSetup> getRenderSettings() {
        return Optional.ofNullable(renderSettings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterfaceSettings that = (InterfaceSettings) o;

        if (featureLayerSettings != null
                ? !featureLayerSettings.equals(that.featureLayerSettings)
                : that.featureLayerSettings != null)
            return false;
        return renderSettings != null ? renderSettings.equals(that.renderSettings) : that.renderSettings == null;
    }

    @Override
    public int hashCode() {
        int result = featureLayerSettings != null ? featureLayerSettings.hashCode() : 0;
        result = 31 * result + (renderSettings != null ? renderSettings.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InterfaceSettings{" +
                "featureLayerSettings=" + featureLayerSettings +
                ", renderSettings=" + renderSettings +
                '}';
    }
}
