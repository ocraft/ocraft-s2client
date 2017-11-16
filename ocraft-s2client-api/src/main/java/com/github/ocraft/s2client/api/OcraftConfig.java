package com.github.ocraft.s2client.api;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public final class OcraftConfig {
    public static final String ROOT = "ocraft";
    public static final String GAME = ROOT + ".game";
    public static final String CLIENT = ROOT + ".client";
    public static final String GAME_EXE = GAME + ".executable";
    public static final String GAME_NET = GAME + ".net";
    public static final String GAME_WINDOW = GAME + ".window";
    public static final String GAME_EXE_PATH = GAME_EXE + ".path";
    public static final String GAME_EXE_DATA_VER = GAME_EXE + ".dataVersion";
    public static final String GAME_EXE_BUILD = GAME_EXE + ".baseBuild";
    public static final String GAME_EXE_FILE = GAME_EXE + ".exeFileName";
    public static final String GAME_EXE_IS_64 = GAME_EXE + ".is64Bit";
    public static final String GAME_NET_IP = GAME_NET + ".ip";
    public static final String GAME_NET_PORT = GAME_NET + ".port";
    public static final String GAME_NET_TIMEOUT = GAME_NET + ".timeoutInMillis";
    public static final String GAME_NET_RETRY_COUNT = GAME_NET + ".retry";
    public static final String GAME_WINDOW_W = GAME_WINDOW + ".width";
    public static final String GAME_WINDOW_H = GAME_WINDOW + ".height";
    public static final String GAME_WINDOW_X = GAME_WINDOW + ".x";
    public static final String GAME_WINDOW_Y = GAME_WINDOW + ".y";
    public static final String GAME_WINDOW_MODE = GAME_WINDOW + ".mode";
    public static final String GAME_CLI = GAME + ".cli";
    public static final String GAME_CLI_VERBOSE = GAME_CLI + ".verbose";
    public static final String GAME_CLI_TEMP_DIR = GAME_CLI + ".tempDir";
    public static final String GAME_CLI_DATA_DIR = GAME_CLI + ".dataDir";
    public static final String GAME_CLI_OS_MESA_PATH = GAME_CLI + ".osmesapath";
    public static final String GAME_CLI_EGL_PATH = GAME_CLI + ".eglpath";
    public static final String CLIENT_NET = CLIENT + ".net";
    public static final String CLIENT_BUFFER = CLIENT + ".buffer";
    public static final String CLIENT_NET_RETRY = CLIENT_NET + ".retry";
    public static final String CLIENT_NET_FRAME_SIZE = CLIENT_NET + ".maxWebSocketFrameSizeInBytes";
    public static final String CLIENT_NET_CONNECT_TIMEOUT = CLIENT_NET + ".connectTimeoutInMillis";
    public static final String CLIENT_BUFFER_SIZE = CLIENT_BUFFER + ".size";
    public static final String CLIENT_BUFFER_SIZE_REQUEST = CLIENT_BUFFER_SIZE + ".request";
    public static final String CLIENT_BUFFER_SIZE_REQUEST_EVENT_BUS = CLIENT_BUFFER_SIZE_REQUEST + ".eventBus";
    public static final String CLIENT_BUFFER_SIZE_REQUEST_QUEUE = CLIENT_BUFFER_SIZE_REQUEST + ".queue";
    public static final String CLIENT_BUFFER_SIZE_RESPONSE = CLIENT_BUFFER_SIZE + ".response";
    public static final String CLIENT_BUFFER_SIZE_RESPONSE_EVENT_BUS = CLIENT_BUFFER_SIZE_RESPONSE + ".eventBus";
    public static final String CLIENT_BUFFER_SIZE_RESPONSE_STREAM = CLIENT_BUFFER_SIZE_RESPONSE + ".stream";
    public static final String CLIENT_BUFFER_SIZE_RESPONSE_BACKPRESSURE = CLIENT_BUFFER_SIZE_RESPONSE + ".backpressure";
    public static final String CLIENT_TRACED = CLIENT + ".traced";

    private static Config config = ConfigFactory.load();

    private OcraftConfig() {
        throw new AssertionError("private constructor");
    }

    public static Config cfg() {
        return config;
    }

    static void inject(Config config) {
        OcraftConfig.config = config;
    }
}
