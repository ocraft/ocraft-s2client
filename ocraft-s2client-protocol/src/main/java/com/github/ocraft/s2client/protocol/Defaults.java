package com.github.ocraft.s2client.protocol;

import com.github.ocraft.s2client.protocol.debug.Color;
import com.github.ocraft.s2client.protocol.game.InterfaceOptions;
import com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup;

import static com.github.ocraft.s2client.protocol.game.InterfaceOptions.interfaces;
import static com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup.spatialSetup;

public final class Defaults {

    public static final Color COLOR = Color.of(255, 255, 255);

    private Defaults() {
        throw new AssertionError("private constructor");
    }

    public static InterfaceOptions defaultInterfaces() {
        return interfaces().raw().score().featureLayer().build();
    }

    public static SpatialCameraSetup defaultSpatialSetup() {
        return spatialSetup().width(24.0f).resolution(64, 64).minimap(64, 64).build();
    }

    public static SpatialCameraSetup defaultSpatialSetupForRender() {
        return spatialSetup().resolution(800, 600).minimap(300, 300).build();
    }
}
