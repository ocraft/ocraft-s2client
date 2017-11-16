package com.github.ocraft.s2client.protocol.observation.raw;

import SC2APIProtocol.Raw;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Upgrade;
import com.github.ocraft.s2client.protocol.data.Upgrades;
import com.github.ocraft.s2client.protocol.spatial.Point;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toSet;

public final class PlayerRaw implements Serializable {

    private static final long serialVersionUID = -8494614928781661147L;

    private final Set<PowerSource> powerSources;
    private final Point camera;
    private final Set<Upgrade> upgrades;

    private PlayerRaw(Raw.PlayerRaw sc2ApiPlayerRaw) {
        powerSources = sc2ApiPlayerRaw.getPowerSourcesList().stream().map(PowerSource::from).collect(toSet());

        camera = tryGet(
                Raw.PlayerRaw::getCamera, Raw.PlayerRaw::hasCamera
        ).apply(sc2ApiPlayerRaw).map(Point::from).orElseThrow(required("camera"));

        upgrades = sc2ApiPlayerRaw.getUpgradeIdsList().stream().map(Upgrades::from).collect(toSet());
    }

    public static PlayerRaw from(Raw.PlayerRaw sc2ApiPlayerRaw) {
        require("sc2api player raw", sc2ApiPlayerRaw);
        return new PlayerRaw(sc2ApiPlayerRaw);
    }

    public Set<PowerSource> getPowerSources() {
        return new HashSet<>(powerSources);
    }

    public Point getCamera() {
        return camera;
    }

    public Set<Upgrade> getUpgrades() {
        return new HashSet<>(upgrades);
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
