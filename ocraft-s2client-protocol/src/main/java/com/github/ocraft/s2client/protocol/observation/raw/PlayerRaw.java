package com.github.ocraft.s2client.protocol.observation.raw;

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

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Upgrade;
import com.github.ocraft.s2client.protocol.data.Upgrades;
import com.github.ocraft.s2client.protocol.spatial.Point;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public final class PlayerRaw implements Serializable {

    private static final long serialVersionUID = -8494614928781661147L;

    private final Set<PowerSource> powerSources;
    private final Point camera;
    private final Set<Upgrade> upgrades;

    private PlayerRaw(Raw.PlayerRaw sc2ApiPlayerRaw) {
        powerSources = sc2ApiPlayerRaw.getPowerSourcesList().stream().map(PowerSource::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));

        camera = tryGet(
                Raw.PlayerRaw::getCamera, Raw.PlayerRaw::hasCamera
        ).apply(sc2ApiPlayerRaw).map(Point::from).orElseThrow(required("camera"));

        upgrades = sc2ApiPlayerRaw.getUpgradeIdsList().stream().map(Upgrades::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    public static PlayerRaw from(Raw.PlayerRaw sc2ApiPlayerRaw) {
        require("sc2api player raw", sc2ApiPlayerRaw);
        return new PlayerRaw(sc2ApiPlayerRaw);
    }

    public Set<PowerSource> getPowerSources() {
        return powerSources;
    }

    public Point getCamera() {
        return camera;
    }

    public Set<Upgrade> getUpgrades() {
        return upgrades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerRaw playerRaw = (PlayerRaw) o;

        return powerSources.equals(playerRaw.powerSources) &&
                camera.equals(playerRaw.camera) &&
                upgrades.equals(playerRaw.upgrades);
    }

    @Override
    public int hashCode() {
        int result = powerSources.hashCode();
        result = 31 * result + camera.hashCode();
        result = 31 * result + upgrades.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
