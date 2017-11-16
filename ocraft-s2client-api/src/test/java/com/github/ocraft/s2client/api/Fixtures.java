package com.github.ocraft.s2client.api;

import com.github.ocraft.s2client.protocol.data.Units;
import com.github.ocraft.s2client.protocol.debug.Color;
import com.github.ocraft.s2client.protocol.response.ResponseObservation;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.spatial.PointI;
import com.github.ocraft.s2client.protocol.spatial.RectangleI;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.data.Units.TERRAN_COMMAND_CENTER;
import static com.github.ocraft.s2client.protocol.data.Units.TERRAN_SCV;

public class Fixtures {

    static final Path REPLAY_PATH = Paths.get(
            relativeFile("ffd95a7de213d9fa6965ae049adc5e317cc4cc3cbed283c98b9a6f2ddd3bcbef.SC2Replay")
    ).toAbsolutePath();
    static final Path MAP_PATH = Paths.get(relativeFile("AbyssalReefLE.SC2Map")).toAbsolutePath();
    static final Path TMP_PATH = Paths.get("test.SC2Map");
    static final int PLAYER_ID = 1;
    static final int PLAYER_COUNT = 2;
    static final int GAME_LOOP_COUNT = 100;

    public static final String EXECUTE_INFO_TXT = "ExecuteInfo.txt";
    public static final Path GAME_ROOT = Paths.get("Games", "StarCraft II");

    public static final Path WIN_USER_DIR = Paths.get("Documents");
    public static final Path OSX_USER_DIR = Paths.get("Library", "Application Support", "Blizzard");
    public static final Path LINUX_USER_DIR = Paths.get("");
    public static final String CFG_EXE_FILE = "SC2_x64.exe";
    public static final String CFG_NET_IP = "127.0.0.1";
    public static final int CFG_NET_PORT = 5000;
    public static final String CFG_EXE_BUILD_NEW = "Base58400";
    public static final String CFG_EXE_BUILD_OLD = "Base55958";
    public static final String CFG_EXE_DATA_VER = "2B06AEE58017A7DF2A3D452D733F1019";
    public static final String GAME_DIR = "Starcraft II";
    public static final int CFG_WINDOW_W = 1024;
    public static final int CFG_WINDOW_H = 768;
    public static final int CFG_WINDOW_X = 0;
    public static final int CFG_WINDOW_Y = 0;

    public static final String CFG_NET_IP_CUSTOM = "192.168.1.1";
    public static final int CFG_NET_PORT_CUSTOM = 1000;
    public static final int CFG_WINDOW_W_CUSTOM = 400;
    public static final int CFG_WINDOW_H_CUSTOM = 300;
    public static final int CFG_WINDOW_X_CUSTOM = 100;
    public static final int CFG_WINDOW_Y_CUSTOM = 150;
    public static final String CFG_EXE_DATA_VER_CUSTOM = "5BD7C31B44525DAB46E64C4602A81DC2";

    private Fixtures() {
        throw new AssertionError("private constructor");
    }

    static class GameContext {
        final Unit commandCenter;
        final Unit scv;
        final PointI pointOnScreen = PointI.of(32, 32);
        final RectangleI area = RectangleI.of(PointI.of(0, 0), PointI.of(64, 64));
        final String welcome = "gl hf";
        final int key = 1;
        final int commandCenterIndex = 13;
        final int unitIndex = 1;
        final Point2d start;
        final Point2d end;
        final Point p0;
        final Point p1;
        final Point p2;
        final String debugTxt = "Ocraft debug";
        final Color white = Color.of(255, 255, 255);
        final int textSize = 12;
        final int radius = 3;
        final int terran = 1;
        final int two = 3;
        final int score = 10;
        final int lifeValue = 100;
        final float distance = 2.3f;

        private GameContext(ResponseObservation observation) {
            commandCenter = unit(TERRAN_COMMAND_CENTER, observation);
            scv = unit(TERRAN_SCV, observation);
            p0 = commandCenter.getPosition();
            p1 = Point.of(p0.getX() + 5, p0.getY() + 5, p0.getZ() + 5);
            p2 = Point.of(p1.getX() + 5, p1.getY() + 5, p1.getZ() + 5);
            start = Point2d.of(p1.getX(), p1.getY());
            end = Point2d.of(p2.getX(), p2.getY());
        }

        static GameContext from(ResponseObservation observe) {
            return new GameContext(observe);
        }
    }

    static Unit unit(Units unitType, ResponseObservation responseObservation) {
        return responseObservation
                .getObservation()
                .getRaw().orElseThrow(required("raw interface"))
                .getUnits().stream()
                .filter(unit -> unit.getType().equals(unitType))
                .findAny().orElseThrow(required(unitType.name()));
    }

    private static URI relativeFile(String fileName) {
        URL resource = Fixtures.class.getClassLoader().getResource(fileName);
        return Optional.ofNullable(resource).map(Fixtures::asUri).orElseThrow(required("file [" + fileName + "]"));
    }

    private static URI asUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
