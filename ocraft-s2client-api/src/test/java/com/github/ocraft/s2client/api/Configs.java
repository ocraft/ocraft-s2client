package com.github.ocraft.s2client.api;

import com.typesafe.config.ConfigFactory;

public class Configs {

    private Configs() {
        throw new AssertionError("private constructor");
    }

    public static void refreshConfig() {
        ConfigFactory.invalidateCaches();
        OcraftConfig.inject(ConfigFactory.load());
    }
}
