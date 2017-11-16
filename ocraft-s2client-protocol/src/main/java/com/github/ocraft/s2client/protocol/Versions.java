package com.github.ocraft.s2client.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.stream;

public final class Versions {

    public static final String API_VERSION = "4.0.0.59587";

    private static Map<Integer, GameVersion> gameVersions = new HashMap<>();

    static {
        loadGameVersions();
    }

    private Versions() {
        throw new AssertionError("private constructor");
    }

    private static void loadGameVersions() {
        try {
            GameVersion[] versions = new ObjectMapper().readValue(
                    Versions.class.getClassLoader().getResourceAsStream("versions.json"),
                    GameVersion[].class);
            stream(versions).forEach(ver -> gameVersions.put(ver.getVersion(), ver));
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }

    public static Optional<GameVersion> versionFor(int baseBuild) {
        return Optional.ofNullable(gameVersions.get(baseBuild));
    }

}
