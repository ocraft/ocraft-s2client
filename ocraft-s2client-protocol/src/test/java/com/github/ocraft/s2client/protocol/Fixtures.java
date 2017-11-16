package com.github.ocraft.s2client.protocol;

import SC2APIProtocol.*;
import SC2APIProtocol.Error;
import com.github.ocraft.s2client.protocol.action.ActionChat;
import com.github.ocraft.s2client.protocol.action.Actions;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitCommand;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint;
import com.github.ocraft.s2client.protocol.action.ui.*;
import com.github.ocraft.s2client.protocol.data.*;
import com.github.ocraft.s2client.protocol.debug.*;
import com.github.ocraft.s2client.protocol.observation.Alert;
import com.github.ocraft.s2client.protocol.query.QueryPathingBuilder;
import com.github.ocraft.s2client.protocol.spatial.*;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraFollowPlayerBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraFollowUnitsBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverCameraMoveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.observer.ActionObserverPlayerPerspectiveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawCameraMoveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawToggleAutocastBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.raw.ActionRawUnitCommandBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialCameraMoveBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitCommandBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionPointBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionRectBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.ui.*;
import com.github.ocraft.s2client.protocol.syntax.debug.*;
import com.github.ocraft.s2client.protocol.syntax.query.QueryAvailableAbilitiesBuilder;
import com.github.ocraft.s2client.protocol.syntax.query.QueryBuildingPlacementBuilder;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.google.protobuf.ByteString;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.github.ocraft.s2client.protocol.Images.HEIGHT_MAP;
import static com.github.ocraft.s2client.protocol.action.Actions.Observer.*;
import static com.github.ocraft.s2client.protocol.action.raw.ActionRawCameraMove.cameraMove;
import static com.github.ocraft.s2client.protocol.action.raw.ActionRawToggleAutocast.toggleAutocast;
import static com.github.ocraft.s2client.protocol.action.raw.ActionRawUnitCommand.unitCommand;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionPoint.click;
import static com.github.ocraft.s2client.protocol.action.spatial.ActionSpatialUnitSelectionRect.select;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiCargoPanelUnload.cargoPanelUnload;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiControlGroup.controlGroup;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiMultiPanel.multiPanel;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiProductionPanelRemoveFromQueue.removeFromQueue;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectArmy.selectArmy;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectIdleWorker.selectIdleWorker;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectLarva.selectLarva;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectWarpGates.selectWarpGates;
import static com.github.ocraft.s2client.protocol.debug.DebugBox.box;
import static com.github.ocraft.s2client.protocol.debug.DebugCreateUnit.createUnit;
import static com.github.ocraft.s2client.protocol.debug.DebugDraw.draw;
import static com.github.ocraft.s2client.protocol.debug.DebugEndGame.endGame;
import static com.github.ocraft.s2client.protocol.debug.DebugKillUnit.killUnit;
import static com.github.ocraft.s2client.protocol.debug.DebugLine.line;
import static com.github.ocraft.s2client.protocol.debug.DebugSetScore.setScore;
import static com.github.ocraft.s2client.protocol.debug.DebugSetUnitValue.setUnitValue;
import static com.github.ocraft.s2client.protocol.debug.DebugSphere.sphere;
import static com.github.ocraft.s2client.protocol.debug.DebugTestProcess.testProcess;
import static com.github.ocraft.s2client.protocol.debug.DebugText.text;
import static com.github.ocraft.s2client.protocol.query.QueryAvailableAbilities.availableAbilities;
import static com.github.ocraft.s2client.protocol.query.QueryBuildingPlacement.placeBuilding;
import static com.github.ocraft.s2client.protocol.query.QueryPathing.path;
import static com.github.ocraft.s2client.protocol.spatial.SpatialCameraSetup.spatialSetup;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public final class Fixtures {

    public static final String GAME_VERSION = "3.17.1.57218";
    public static final String DATA_VERSION = "3F2FCED08798D83B873B5543BEFA6C4B";
    public static final int DATA_BUILD = 57218;
    public static final int BASE_BUILD = 56787;
    public static final List<String> ERRORS = List.of("error01", "error02");
    public static final Set<String> BATTLENET_MAPS = Set.of("Lava Flow", "Cursed Hollow");
    public static final Set<Path> LOCAL_MAP_PATHS = Set.of(
            Paths.get("Ladder2017Season1", "NewkirkPrecinctTE.SC2Map"),
            Paths.get("Ladder2017Season1", "CactusValleyLE.SC2Map"));
    public static final String LOCAL_MAP_PATH = "Ladder2017Season1\\NewkirkPrecinctTE.SC2Map";
    public static final byte[] DATA_IN_BYTES = new byte[0xFF];
    public static final int RANDOM_SEED = 1;
    public static final String BATTLENET_MAP_NAME = "Lava Flow";
    public static final String ERROR_CREATE_GAME = "Unable to find map with that name";
    public static final String ERROR_JOIN_GAME = "Missing participation";
    public static final String ERROR_RESTART_GAME = "Launch error";
    public static final String ERROR_START_REPLAY = "Unable to open replay.";
    public static final String ERROR_REPLAY_INFO = "Could not open the replay.";
    public static final int PLAYER_ID = 1;
    public static final String REPLAY_PATH =
            "Replays\\ffd95a7de213d9fa6965ae049adc5e317cc4cc3cbed283c98b9a6f2ddd3bcbef.SC2Replay";
    public static final byte[] REPLAY_DATA_IN_BYTES = new byte[0x0C];
    public static final int PLAYER_MMR = 2758;  // Match Making Rating (optional)
    public static final int PLAYER_APM = 85;  // Action Per Minute
    public static final int GAME_DURATION_LOOPS = 16268;
    public static final float GAME_DURATION_SECONDS = 726.30066f;
    public static final long UNIT_TAG = 100;
    private static final Set<Long> SC2API_UNIT_TAGS = Set.of(100L, 200L, 300L);
    public static final Set<Tag> UNIT_TAGS = Set.of(Tag.from(100L), Tag.from(200L), Tag.from(300L));
    public static final int PSI_STORM_ABILITY_ID = 1036;
    private static final int ATTACK_ABILITY_ID = 3674;
    public static final int CONTROL_GROUP_INDEX = 3;
    public static final int UNIT_INDEX = 5;
    public static final String CHAT_MESSAGE = "gl hf";
    public static final int GAME_LOOP = 123;
    public static final Set<Alert> ALERTS = Set.of(Alert.NUCLEAR_LAUNCH_DETECTED, Alert.NYDUS_WORM_DETECTED);
    public static final int MINERALS = 50;
    public static final int VESPENE = 25;
    public static final int FOOD_CAP = 15;
    public static final int FOOD_USED = 12;
    public static final int FOOD_ARMY = 2;
    public static final int FOOD_WORKERS = 10;
    public static final int IDLE_WORKER_COUNT = 0;
    public static final int ARMY_COUNT = 1;
    public static final Integer WARP_GATE_COUNT = 0;
    public static final Integer LARVA_COUNT = 0;
    public static final int SCORE = 1050;
    public static final float IDLE_PRODUCTION_TIME = 10;
    public static final float IDLE_WORKER_TIME = 20;
    public static final float TOTAL_VALUE_UNITS = 600;
    public static final float TOTAL_VALUE_STRUCTURES = 400;
    public static final float KILLED_VALUE_UNITS = 100;
    public static final float KILLED_VALUE_STRUCTURES = 200;
    public static final float COLLECTED_MINERALS = 1000;
    public static final float COLLECTED_VESPENE = 500;
    public static final float COLLECTION_RATE_MINERALS = 123.5f;
    public static final float COLLECTION_RATE_VESPENE = 65.7f;
    public static final float SPENT_MINERALS = 2000;
    public static final float SPENT_VESPENE = 1500;
    public static final float CATEGORY_SCORE_NONE = 10;
    public static final float CATEGORY_SCORE_ARMY = 20;
    public static final float CATEGORY_SCORE_ECONOMY = 30;
    public static final float CATEGORY_SCORE_TECHNOLOGY = 40;
    public static final float CATEGORY_SCORE_UPGRADE = 50;
    public static final float VITAL_SCORE_LIFE = 100;
    public static final float VITAL_SCORE_SHIELDS = 200;
    public static final float VITAL_SCORE_ENERGY = 300;
    public static final Set<Upgrade> UPGRADES = Set.of(Upgrades.PROTOSS_SHIELDS_LEVEL1, Upgrades.PHOENIX_RANGE_UPGRADE);
    public static final float POWER_SOURCE_RADIUS = 5.6f;
    public static final float UNIT_FACING = 0;
    public static final float UNIT_RADIUS = 1.125f;
    public static final float UNIT_BUILD_PROGRESS = 1.0f;
    public static final float UNIT_DETECT_RANGE = 1.1f;
    public static final float UNIT_RADAR_RANGE = 1.2f;
    public static final boolean UNIT_SELECTED = true;
    public static final boolean UNIT_POWERED = false;
    public static final float UNIT_HEALTH = 1450.0f;
    public static final float UNIT_HEALTH_MAX = 1500.0f;
    public static final float UNIT_SHIELD = 500.0f;
    public static final float UNIT_SHIELD_MAX = 600.0f;
    public static final float UNIT_ENERGY = 50.0f;
    public static final float UNIT_ENERGY_MAX = 100.0f;
    public static final int UNIT_MINERAL_CONTENTS = 120;
    public static final int UNIT_VESPENE_CONTENTS = 80;
    public static final boolean UNIT_FLYING = false;
    public static final boolean UNIT_BURROWED = false;
    public static final long UNIT_ADDON_TAG = 2L;
    public static final int UNIT_CARGO_SPACE_TAKEN = 1;
    public static final int UNIT_CARGO_SPACE_MAX = 3;
    public static final Set<Buff> UNIT_BUFFS = Set.of(Buffs.FUNGAL_GROWTH, Buffs.EMP_DECLOAK);
    public static final int UNIT_ASSIGNED_HARVESTERS = 17;
    public static final int UNIT_IDEAL_HARVESTERS = 26;
    public static final float UNIT_WEAPON_COOLDOWN = 6.0f;
    public static final long UNIT_ENGAGED_TARGET_TAG = 3L;
    public static final float TRAIN_PROGRESS = 1.0f;
    public static final int BITS_PER_PIXEL = 8;
    public static final int SCREEN_SIZE_X = 64;
    public static final int SCREEN_SIZE_Y = 64;
    public static final int EFFECT_ID = 1;
    public static final int UNIT_COUNT = 3;
    public static final int CARGO_SIZE = 5;
    private static final int UNIT_TYPE_COLOSSUS = 59;
    public static final Color SAMPLE_COLOR = Color.of(100, 100, 1);
    public static final String DEBUG_TEXT = "debug";
    public static final int DEBUG_TEXT_SIZE = 8;
    public static final Point P0 = Point.of(1.0f, 1.0f);
    public static final Point P1 = Point.of(2.0f, 2.0f);
    public static final Point2d POS = Point2d.of(2.0f, 2.0f);
    public static final int QUANTITY = 10;
    public static final int TYPE_ID_ARCHON = 141;
    public static final int DELAY = 100;
    public static final String MOD_NAME = "mod";
    public static final Point2d START = Point2d.of(1.0f, 1.0f);
    public static final Point2d END = Point2d.of(2.0f, 2.0f);
    public static final float DISTANCE = 10;
    public static final int ABILITY_ID = 652;
    public static final String ABILITY_LINK_NAME = "EngineeringBayResearch";
    public static final int ABILITY_LINK_INDEX = 2;
    public static final String ABILITY_BUTTON_NAME = "TerranInfantryWeaponsLevel1";
    public static final String ABILITY_FRIENDLY_NAME = "Research TerranInfantryWeaponsLevel1";
    public static final String ABILITY_HOTKEY = "E";
    public static final int ABILITY_REMAPS_TO_ID = 3698;
    public static final boolean ABILITY_AVAILABLE = true;
    public static final boolean ABILITY_ALLOW_MINIMAP = true;
    public static final boolean ABILITY_ALLOW_AUTOCAST = true;
    public static final boolean ABILITY_IS_BUILDING = true;
    public static final float ABILITY_FOOTPRINT_RADIUS = 1.0f;
    public static final boolean ABILITY_INSTANT_PLACEMENT = true;
    public static final float ABILITY_CAST_RANGE = 10.0f;
    public static final String UNIT_NAME = "Colossus";
    public static final boolean UNIT_AVAILABLE = true;
    public static final int UNIT_MINERAL_COST = 300;
    public static final int UNIT_VESPENE_COST = 100;
    public static final float UNIT_FOOD_REQUIRED = 5.0f;
    public static final float UNIT_FOOD_PROVIDED = 1.0f;
    public static final Common.Race UNIT_RACE = Common.Race.Protoss;
    public static final float UNIT_BUILD_TIME = 45.5f;
    public static final boolean UNIT_HAS_VESPENE = true;
    public static final boolean UNIT_HAS_MINERALS = true;
    public static final float UNIT_SIGHT_RANGE = 1.5f;
    public static final boolean UNIT_REQUIRE_ATTACHED = true;
    public static final Data.Attribute UNIT_ATTRIBUTE = Data.Attribute.Massive;
    public static final float UNIT_SPEED = 2.0f;
    public static final float UNIT_ARMOR = 2.5f;
    public static final float WEAPON_DAMAGE = 15.0f;
    public static final int WEAPON_ATTACKS = 2;
    public static final float WEAPON_RANGE = 8.0f;
    public static final float WEAPON_SPEED = 0.75f;
    public static final float WEAPON_DAMAGE_BONUS = 0.5f;
    public static final String UPGRADE_NAME = "BansheeCloak";
    public static final int UPGRADE_MINERAL_COST = 100;
    public static final int UPGRADE_VESPENE_COST = 100;
    public static final float UPGRADE_RESEARCH_TIME = 1760.0f;
    public static final int BUFF_ID = 5;
    public static final String BUFF_NAME = "GravitonBeam";
    public static final String EFFECT_NAME = "PsiStormPersistent";
    public static final String EFFECT_FRIENDLY_NAME = "PsiStorm";
    public static final float EFFECT_RADIUS = 1.5f;
    public static float RADIUS = 4.0f;
    public static float CAMERA_DISTANCE = 3.0f;

    private Fixtures() {
        throw new AssertionError("private constructor");
    }

    @SafeVarargs
    public static <T> T without(Supplier<T> supplier, Consumer<T>... clear) {
        T builder = supplier.get();
        stream(clear).forEach(c -> c.accept(builder));
        return builder;
    }

    public static Sc2Api.Response.Builder aSc2ApiResponse() {
        return Sc2Api.Response.newBuilder();
    }

    public static Sc2Api.Response emptySc2ApiResponse() {
        return Sc2Api.Response.getDefaultInstance();
    }

    public static Sc2Api.Response sc2ApiResponseWithError() {
        return aSc2ApiResponse().addAllError(ERRORS).setStatus(Sc2Api.Status.launched).build();
    }

    public static Sc2Api.Response sc2ApiResponseWithPing() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.in_game).setPing(sc2ApiResponsePing()).build();
    }

    public static Sc2Api.ResponsePing sc2ApiResponsePing() {
        return aSc2ApiResponsePing()
                .setGameVersion(GAME_VERSION)
                .setDataVersion(DATA_VERSION)
                .setDataBuild(DATA_BUILD)
                .setBaseBuild(BASE_BUILD).build();
    }

    private static Sc2Api.ResponsePing.Builder aSc2ApiResponsePing() {
        return Sc2Api.ResponsePing.newBuilder();
    }

    public static Sc2Api.Response sc2ApiResponseWithAvailableMaps() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setAvailableMaps(sc2ApiResponseAvailableMaps()).build();
    }

    private static Sc2Api.ResponseAvailableMaps sc2ApiResponseAvailableMaps() {
        return Sc2Api.ResponseAvailableMaps.newBuilder()
                .addAllBattlenetMapNames(BATTLENET_MAPS)
                .addAllLocalMapPaths(LOCAL_MAP_PATHS.stream().map(Path::toString).collect(Collectors.toSet())).build();
    }

    public static Sc2Api.ResponseAvailableMaps emptySc2ApiResponseAvailableMaps() {
        return Sc2Api.ResponseAvailableMaps.newBuilder().build();
    }

    public static Sc2Api.Response sc2ApiResponseWithCreateGame() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.in_game).setCreateGame(sc2ApiResponseCreateGame()).build();
    }

    private static Sc2Api.ResponseCreateGame sc2ApiResponseCreateGame() {
        return Sc2Api.ResponseCreateGame.newBuilder().build();
    }

    public static Sc2Api.Response sc2ApiResponseWithCreateGameWithError() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setCreateGame(Sc2Api.ResponseCreateGame.newBuilder()
                        .setError(Sc2Api.ResponseCreateGame.Error.InvalidMapName)
                        .setErrorDetails(ERROR_CREATE_GAME)
                        .build())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithJoinGame() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.in_game).setJoinGame(sc2ApiResponseJoinGame()).build();
    }

    private static Sc2Api.ResponseJoinGame sc2ApiResponseJoinGame() {
        return Sc2Api.ResponseJoinGame.newBuilder().setPlayerId(PLAYER_ID).build();
    }

    public static Sc2Api.Response sc2ApiResponseWithJoinGameWithError() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setJoinGame(Sc2Api.ResponseJoinGame.newBuilder()
                        .setError(Sc2Api.ResponseJoinGame.Error.MissingParticipation)
                        .setErrorDetails(ERROR_JOIN_GAME)
                        .build())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithLeaveGame() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.launched)
                .setLeaveGame(Sc2Api.ResponseLeaveGame.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithRestartGame() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setRestartGame(Sc2Api.ResponseRestartGame.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithRestartGameWithError() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setRestartGame(Sc2Api.ResponseRestartGame.newBuilder()
                        .setError(Sc2Api.ResponseRestartGame.Error.LaunchError)
                        .setErrorDetails(ERROR_RESTART_GAME)
                        .build())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithQuit() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.quit)
                .setQuit(Sc2Api.ResponseQuit.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithStartReplay() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_replay)
                .setStartReplay(Sc2Api.ResponseStartReplay.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithStartReplayWithError() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_replay)
                .setStartReplay(Sc2Api.ResponseStartReplay.newBuilder()
                        .setError(Sc2Api.ResponseStartReplay.Error.LaunchError)
                        .setErrorDetails(ERROR_START_REPLAY)
                        .build())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithReplayInfo() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.launched).setReplayInfo(sc2ApiResponseReplayInfo()).build();
    }

    public static Sc2Api.ResponseReplayInfo sc2ApiResponseReplayInfo() {
        return Sc2Api.ResponseReplayInfo.newBuilder()
                .setMapName(BATTLENET_MAP_NAME)
                .setLocalMapPath(LOCAL_MAP_PATH)
                .addPlayerInfo(sc2ApiPlayerInfoExtra())
                .addPlayerInfo(sc2ApiPlayerInfoExtra())
                .setGameDurationLoops(GAME_DURATION_LOOPS)
                .setGameDurationSeconds(GAME_DURATION_SECONDS)
                .setGameVersion(GAME_VERSION)
                .setDataVersion(DATA_VERSION)
                .setBaseBuild(BASE_BUILD)
                .setDataBuild(DATA_BUILD)
                .build();
    }

    public static Sc2Api.PlayerInfoExtra sc2ApiPlayerInfoExtra() {
        return Sc2Api.PlayerInfoExtra.newBuilder()
                .setPlayerInfo(sc2ApiPlayerInfo())
                .setPlayerResult(sc2ApiPlayerResult())
                .setPlayerMmr(PLAYER_MMR)
                .setPlayerApm(PLAYER_APM)
                .build();
    }

    public static Sc2Api.PlayerInfo sc2ApiPlayerInfo() {
        return Sc2Api.PlayerInfo.newBuilder()
                .setPlayerId(PLAYER_ID)
                .setType(Sc2Api.PlayerType.Participant)
                .setRaceRequested(Common.Race.Zerg)
                .setRaceActual(Common.Race.Protoss)
                .setDifficulty(Sc2Api.Difficulty.CheatVision)
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithStep() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setStep(Sc2Api.ResponseStep.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithAction() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setAction(Sc2Api.ResponseAction.newBuilder()
                        .addResult(Error.ActionResult.AlreadyTargeted)
                        .addResult(Error.ActionResult.BuildingIsUnderConstruction)
                        .build()).build();
    }

    public static Sc2Api.Response emptySc2ApiResponseWithAction() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setAction(Sc2Api.ResponseAction.newBuilder().build()).build();
    }

    public static Sc2Api.Response sc2ApiResponseWithReplayInfoWithError() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.launched)
                .setReplayInfo(Sc2Api.ResponseReplayInfo.newBuilder()
                        .setError(Sc2Api.ResponseReplayInfo.Error.InvalidReplayPath)
                        .setErrorDetails(ERROR_REPLAY_INFO)
                        .build())
                .build();
    }

    public static Sc2Api.Response emptySc2ApiResponseObservation() {
        return Sc2Api.Response.newBuilder().setObservation(Sc2Api.ResponseObservation.newBuilder().build()).build();
    }

    public static Sc2Api.Response sc2ApiResponseWithObservation() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.in_game).setObservation(sc2ApiResponseObservation()).build();
    }

    private static Sc2Api.ResponseObservation sc2ApiResponseObservation() {
        return Sc2Api.ResponseObservation.newBuilder()
                .addActions(sc2ApiAction())
                .addActionErrors(sc2ApiActionError())
                .setObservation(sc2ApiObservation())
                .addPlayerResult(sc2ApiPlayerResult())
                .addChat(sc2ApiChatReceived())
                .build();
    }

    public static Sc2Api.Observation sc2ApiObservation() {
        return Sc2Api.Observation.newBuilder()
                .setGameLoop(GAME_LOOP)
                .setPlayerCommon(sc2ApiPlayerCommon())
                .addAllAlerts(asList(Sc2Api.Alert.NydusWormDetected, Sc2Api.Alert.NuclearLaunchDetected))
                .addAbilities(sc2ApiAvailableAbility())
                .setScore(sc2ApiScore())
                .setRawData(sc2ApiObservationRaw())
                .setFeatureLayerData(sc2ApiObservationFeatureLayer())
                .setRenderData(sc2ApiObservationRender())
                .setUiData(sc2ApiObservationUiSingle())
                .build();
    }

    public static Sc2Api.PlayerResult sc2ApiPlayerResult() {
        return Sc2Api.PlayerResult.newBuilder().setPlayerId(PLAYER_ID).setResult(Sc2Api.Result.Victory).build();
    }

    public static Sc2Api.PlayerCommon sc2ApiPlayerCommon() {
        return Sc2Api.PlayerCommon.newBuilder()
                .setPlayerId(PLAYER_ID)
                .setMinerals(MINERALS)
                .setVespene(VESPENE)
                .setFoodCap(FOOD_CAP)
                .setFoodUsed(FOOD_USED)
                .setFoodArmy(FOOD_ARMY)
                .setFoodWorkers(FOOD_WORKERS)
                .setIdleWorkerCount(IDLE_WORKER_COUNT)
                .setArmyCount(ARMY_COUNT)
                .setWarpGateCount(WARP_GATE_COUNT)
                .setLarvaCount(LARVA_COUNT)
                .build();
    }

    public static Common.AvailableAbility sc2ApiAvailableAbility() {
        return Common.AvailableAbility.newBuilder().setAbilityId(PSI_STORM_ABILITY_ID).setRequiresPoint(true).build();
    }

    public static ScoreOuterClass.Score sc2ApiScore() {
        return ScoreOuterClass.Score.newBuilder()
                .setScoreType(ScoreOuterClass.Score.ScoreType.Melee)
                .setScore(SCORE)
                .setScoreDetails(sc2ApiScoreDetails())
                .build();
    }

    public static ScoreOuterClass.ScoreDetails sc2ApiScoreDetails() {
        return ScoreOuterClass.ScoreDetails.newBuilder()
                .setIdleProductionTime(IDLE_PRODUCTION_TIME)
                .setIdleWorkerTime(IDLE_WORKER_TIME)
                .setTotalValueUnits(TOTAL_VALUE_UNITS)
                .setTotalValueStructures(TOTAL_VALUE_STRUCTURES)
                .setKilledValueUnits(KILLED_VALUE_UNITS)
                .setKilledValueStructures(KILLED_VALUE_STRUCTURES)
                .setCollectedMinerals(COLLECTED_MINERALS)
                .setCollectedVespene(COLLECTED_VESPENE)
                .setCollectionRateMinerals(COLLECTION_RATE_MINERALS)
                .setCollectionRateVespene(COLLECTION_RATE_VESPENE)
                .setSpentMinerals(SPENT_MINERALS)
                .setSpentVespene(SPENT_VESPENE)
                .setFoodUsed(sc2ApiCategoryScoreDetails())
                .setKilledMinerals(sc2ApiCategoryScoreDetails())
                .setKilledVespene(sc2ApiCategoryScoreDetails())
                .setLostMinerals(sc2ApiCategoryScoreDetails())
                .setLostVespene(sc2ApiCategoryScoreDetails())
                .setFriendlyFireMinerals(sc2ApiCategoryScoreDetails())
                .setFriendlyFireVespene(sc2ApiCategoryScoreDetails())
                .setUsedMinerals(sc2ApiCategoryScoreDetails())
                .setUsedVespene(sc2ApiCategoryScoreDetails())
                .setTotalUsedMinerals(sc2ApiCategoryScoreDetails())
                .setTotalUsedVespene(sc2ApiCategoryScoreDetails())
                .setTotalDamageDealt(sc2ApiVitalScoreDetails())
                .setTotalDamageTaken(sc2ApiVitalScoreDetails())
                .setTotalHealed(sc2ApiVitalScoreDetails())
                .build();
    }

    public static ScoreOuterClass.CategoryScoreDetails sc2ApiCategoryScoreDetails() {
        return ScoreOuterClass.CategoryScoreDetails.newBuilder()
                .setNone(CATEGORY_SCORE_NONE)
                .setArmy(CATEGORY_SCORE_ARMY)
                .setEconomy(CATEGORY_SCORE_ECONOMY)
                .setTechnology(CATEGORY_SCORE_TECHNOLOGY)
                .setUpgrade(CATEGORY_SCORE_UPGRADE)
                .build();
    }

    public static ScoreOuterClass.VitalScoreDetails sc2ApiVitalScoreDetails() {
        return ScoreOuterClass.VitalScoreDetails.newBuilder()
                .setLife(VITAL_SCORE_LIFE)
                .setShields(VITAL_SCORE_SHIELDS)
                .setEnergy(VITAL_SCORE_ENERGY)
                .build();
    }

    public static Raw.ObservationRaw sc2ApiObservationRaw() {
        return Raw.ObservationRaw.newBuilder()
                .setPlayer(sc2ApiPlayerRaw())
                .addUnits(sc2ApiUnit())
                .setMapState(sc2ApiMapState())
                .setEvent(sc2ApiEvent())
                .addEffects(sc2ApiEffect())
                .build();
    }

    public static Raw.PlayerRaw sc2ApiPlayerRaw() {
        return Raw.PlayerRaw.newBuilder()
                .addPowerSources(sc2ApiPowerSource())
                .setCamera(sc2ApiPoint())
                .addAllUpgradeIds(List.of(45, 99))
                .build();
    }

    public static Raw.PowerSource sc2ApiPowerSource() {
        return Raw.PowerSource.newBuilder()
                .setPos(sc2ApiPoint())
                .setRadius(POWER_SOURCE_RADIUS)
                .setTag(UNIT_TAG).build();
    }

    public static Raw.Unit sc2ApiUnit() {
        return Raw.Unit.newBuilder()
                .setDisplayType(Raw.DisplayType.Visible)
                .setAlliance(Raw.Alliance.Self)
                .setTag(UNIT_TAG)
                .setUnitType(UNIT_TYPE_COLOSSUS)
                .setOwner(PLAYER_ID)
                .setPos(sc2ApiPoint())
                .setFacing(UNIT_FACING)
                .setRadius(UNIT_RADIUS)
                .setBuildProgress(UNIT_BUILD_PROGRESS)
                .setCloak(Raw.CloakState.NotCloaked)
                .setDetectRange(UNIT_DETECT_RANGE)
                .setRadarRange(UNIT_RADAR_RANGE)
                .setIsSelected(UNIT_SELECTED)
                .setIsOnScreen(true)
                .setIsBlip(true)
                .setIsPowered(UNIT_POWERED)
                .setHealth(UNIT_HEALTH)
                .setHealthMax(UNIT_HEALTH_MAX)
                .setShield(UNIT_SHIELD)
                .setShieldMax(UNIT_SHIELD_MAX)
                .setEnergy(UNIT_ENERGY)
                .setEnergyMax(UNIT_ENERGY_MAX)
                .setMineralContents(UNIT_MINERAL_CONTENTS)
                .setVespeneContents(UNIT_VESPENE_CONTENTS)
                .setIsFlying(UNIT_FLYING)
                .setIsBurrowed(UNIT_BURROWED)
                .addOrders(sc2ApiUnitOrder())
                .setAddOnTag(UNIT_ADDON_TAG)
                .addPassengers(sc2ApiPassengerUnit())
                .setCargoSpaceTaken(UNIT_CARGO_SPACE_TAKEN)
                .setCargoSpaceMax(UNIT_CARGO_SPACE_MAX)
                .addAllBuffIds(asList(16, 17))
                .setAssignedHarvesters(UNIT_ASSIGNED_HARVESTERS)
                .setIdealHarvesters(UNIT_IDEAL_HARVESTERS)
                .setWeaponCooldown(UNIT_WEAPON_COOLDOWN)
                .setEngagedTargetTag(UNIT_ENGAGED_TARGET_TAG)
                .build();
    }

    public static Raw.UnitOrder sc2ApiUnitOrder() {
        return Raw.UnitOrder.newBuilder()
                .setAbilityId(PSI_STORM_ABILITY_ID)
                .setTargetUnitTag(UNIT_TAG)
                .setProgress(TRAIN_PROGRESS)
                .build();
    }

    public static Raw.UnitOrder sc2ApiUnitOrderWithTargetedWorldSpacePosition() {
        return Raw.UnitOrder.newBuilder()
                .setAbilityId(PSI_STORM_ABILITY_ID)
                .setTargetWorldSpacePos(sc2ApiPoint())
                .setProgress(TRAIN_PROGRESS)
                .build();
    }

    public static Raw.PassengerUnit sc2ApiPassengerUnit() {
        return Raw.PassengerUnit.newBuilder()
                .setTag(UNIT_TAG)
                .setHealth(UNIT_HEALTH)
                .setHealthMax(UNIT_HEALTH_MAX)
                .setShield(UNIT_SHIELD)
                .setShieldMax(UNIT_SHIELD_MAX)
                .setEnergy(UNIT_ENERGY)
                .setEnergyMax(UNIT_ENERGY_MAX)
                .setUnitType(UNIT_TYPE_COLOSSUS)
                .build();
    }

    public static Raw.MapState sc2ApiMapState() {
        return Raw.MapState.newBuilder()
                .setVisibility(sc2ApiImageData())
                .setCreep(sc2ApiImageData())
                .build();
    }

    public static Common.ImageData sc2ApiImageData() {
        return Common.ImageData.newBuilder()
                .setBitsPerPixel(BITS_PER_PIXEL)
                .setSize(sc2ApiSize2dI())
                .setData(HEIGHT_MAP)
                .build();
    }

    public static Common.ImageData corruptedSc2ApiImageData() {
        return Common.ImageData.newBuilder()
                .setBitsPerPixel(BITS_PER_PIXEL)
                .setSize(sc2ApiSize2dI())
                .setData(ByteString.copyFrom(new byte[]{0xF}))
                .build();
    }

    public static Common.ImageData sc2ApiImageDataWithTwoBitPerPixel() {
        return Common.ImageData.newBuilder()
                .setBitsPerPixel(2)
                .setSize(sc2ApiSize2dI())
                .setData(HEIGHT_MAP.substring(0, 1024))
                .build();
    }

    public static Common.Size2DI sc2ApiSize2dI() {
        return Common.Size2DI.newBuilder().setX(SCREEN_SIZE_X).setY(SCREEN_SIZE_Y).build();
    }

    public static Raw.Event sc2ApiEvent() {
        return Raw.Event.newBuilder().addDeadUnits(UNIT_TAG).build();
    }

    public static Raw.Effect sc2ApiEffect() {
        return Raw.Effect.newBuilder().setEffectId(EFFECT_ID).addPos(sc2ApiPoint2d()).build();
    }

    public static Spatial.ObservationFeatureLayer sc2ApiObservationFeatureLayer() {
        return Spatial.ObservationFeatureLayer.newBuilder()
                .setRenders(sc2ApiFeatureLayers())
                .setMinimapRenders(sc2ApiFeatureLayersMinimap())
                .build();
    }

    public static Spatial.FeatureLayers sc2ApiFeatureLayers() {
        return Spatial.FeatureLayers.newBuilder()
                .setHeightMap(sc2ApiImageData())
                .setVisibilityMap(sc2ApiImageData())
                .setCreep(sc2ApiImageData())
                .setPower(sc2ApiImageData())
                .setPlayerId(sc2ApiImageData())
                .setUnitType(sc2ApiImageData())
                .setSelected(sc2ApiImageData())
                .setUnitHitPoints(sc2ApiImageData())
                .setUnitHitPointsRatio(sc2ApiImageData())
                .setUnitEnergy(sc2ApiImageData())
                .setUnitEnergyRatio(sc2ApiImageData())
                .setUnitShields(sc2ApiImageData())
                .setUnitShieldsRatio(sc2ApiImageData())
                .setPlayerRelative(sc2ApiImageData())
                .setUnitDensityAa(sc2ApiImageData())
                .setUnitDensity(sc2ApiImageData())
                .setEffects(sc2ApiImageData())
                .build();
    }

    public static Spatial.FeatureLayersMinimap sc2ApiFeatureLayersMinimap() {
        return Spatial.FeatureLayersMinimap.newBuilder()
                .setHeightMap(sc2ApiImageData())
                .setVisibilityMap(sc2ApiImageData())
                .setCreep(sc2ApiImageData())
                .setCamera(sc2ApiImageData())
                .setPlayerId(sc2ApiImageData())
                .setUnitType(sc2ApiImageData())
                .setSelected(sc2ApiImageData())
                .setPlayerRelative(sc2ApiImageData())
                .build();
    }

    public static Spatial.ObservationRender sc2ApiObservationRender() {
        return Spatial.ObservationRender.newBuilder().setMap(sc2ApiImageData()).setMinimap(sc2ApiImageData()).build();
    }

    public static Ui.ObservationUI sc2ApiObservationUiSingle() {
        return Ui.ObservationUI.newBuilder().addGroups(sc2ApiControlGroup()).setSingle(sc2ApiSinglePanel()).build();
    }

    public static Ui.SinglePanel sc2ApiSinglePanel() {
        return Ui.SinglePanel.newBuilder().setUnit(sc2ApiUnitInfo()).build();
    }

    public static Ui.UnitInfo sc2ApiUnitInfo() {
        return Ui.UnitInfo.newBuilder()
                .setUnitType(UNIT_TYPE_COLOSSUS)
                .setPlayerRelative(1)
                .setHealth((int) UNIT_HEALTH)
                .setShields((int) UNIT_SHIELD)
                .setEnergy((int) UNIT_ENERGY)
                .setTransportSlotsTaken(CARGO_SIZE)
                .setBuildProgress(UNIT_BUILD_PROGRESS)
                .setAddOn(sc2ApiUnitInfoAddOn())
                .build();
    }

    public static Ui.UnitInfo sc2ApiUnitInfoAddOn() {
        return Ui.UnitInfo.newBuilder()
                .setUnitType(UNIT_TYPE_COLOSSUS)
                .setPlayerRelative(1)
                .setHealth((int) UNIT_HEALTH)
                .setShields((int) UNIT_SHIELD)
                .setEnergy((int) UNIT_ENERGY)
                .setTransportSlotsTaken(CARGO_SIZE)
                .setBuildProgress(UNIT_BUILD_PROGRESS)
                .build();
    }

    public static Ui.ControlGroup sc2ApiControlGroup() {
        return Ui.ControlGroup.newBuilder()
                .setControlGroupIndex(CONTROL_GROUP_INDEX)
                .setLeaderUnitType(4)
                .setCount(UNIT_COUNT)
                .build();
    }

    public static Ui.ObservationUI sc2ApiObservationUiMulti() {
        return Ui.ObservationUI.newBuilder().addGroups(sc2ApiControlGroup()).setMulti(sc2ApiMultiPanel()).build();
    }

    public static Ui.MultiPanel sc2ApiMultiPanel() {
        return Ui.MultiPanel.newBuilder().addUnits(sc2ApiUnitInfo()).build();
    }

    public static Ui.ObservationUI sc2ApiObservationUiCargo() {
        return Ui.ObservationUI.newBuilder().addGroups(sc2ApiControlGroup()).setCargo(sc2ApiCargoPanel()).build();
    }

    public static Ui.CargoPanel sc2ApiCargoPanel() {
        return Ui.CargoPanel.newBuilder()
                .setUnit(sc2ApiUnitInfo())
                .addPassengers(sc2ApiUnitInfo())
                .setSlotsAvailable(CARGO_SIZE)
                .build();
    }

    public static Ui.ObservationUI sc2ApiObservationUiProduction() {
        return Ui.ObservationUI.newBuilder()
                .addGroups(sc2ApiControlGroup())
                .setProduction(sc2ApiProductionPanel())
                .build();
    }

    public static Ui.ProductionPanel sc2ApiProductionPanel() {
        return Ui.ProductionPanel.newBuilder().setUnit(sc2ApiUnitInfo()).addBuildQueue(sc2ApiUnitInfo()).build();
    }

    public static Sc2Api.Response sc2ApiResponseObservationWithEmptyLists() {
        return aSc2ApiResponse()
                .setObservation(Sc2Api.ResponseObservation.newBuilder().setObservation(sc2ApiObservation()).build())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithQuickSave() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setQuickSave(Sc2Api.ResponseQuickSave.newBuilder().build()).build();
    }

    public static Sc2Api.Response sc2ApiResponseWithQuickLoad() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setQuickLoad(Sc2Api.ResponseQuickLoad.newBuilder().build()).build();
    }

    public static Sc2Api.Response sc2ApiResponseWithSaveMap() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setSaveMap(Sc2Api.ResponseSaveMap.newBuilder().build()).build();
    }

    public static Sc2Api.Response sc2ApiResponseWithSaveMapWithError() {
        return aSc2ApiResponse()
                .setStatus(Sc2Api.Status.in_game)
                .setSaveMap(Sc2Api.ResponseSaveMap.newBuilder()
                        .setError(Sc2Api.ResponseSaveMap.Error.InvalidMapData).build()).build();
    }

    public static Sc2Api.Response sc2ApiResponseWithSaveReplay() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.ended)
                .setSaveReplay(Sc2Api.ResponseSaveReplay.newBuilder()
                        .setData(ByteString.copyFrom(DATA_IN_BYTES))
                        .build()).build();
    }

    public static Sc2Api.Response sc2ApiResponseWithSaveReplayWithoutData() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.ended)
                .setSaveReplay(Sc2Api.ResponseSaveReplay.newBuilder()
                        .build()).build();
    }

    public static Sc2Api.Response sc2ApiResponseWithDebug() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.in_game)
                .setDebug(Sc2Api.ResponseDebug.newBuilder().build()).build();
    }

    public static Sc2Api.Response sc2ApiResponseWithGameInfo() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.in_game)
                .setGameInfo(sc2ApiResponseGameInfo())
                .build();
    }

    public static Sc2Api.ResponseGameInfo sc2ApiResponseGameInfo() {
        return Sc2Api.ResponseGameInfo.newBuilder()
                .setMapName(BATTLENET_MAP_NAME)
                .addModNames(MOD_NAME)
                .setLocalMapPath(LOCAL_MAP_PATH)
                .addPlayerInfo(sc2ApiPlayerInfo())
                .setStartRaw(sc2ApiStartRaw())
                .setOptions(sc2ApiInterfaceOptions())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithData() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.launched)
                .setData(sc2ApiResponseData())
                .build();
    }

    public static Sc2Api.ResponseData sc2ApiResponseData() {
        return Sc2Api.ResponseData.newBuilder()
                .addAbilities(sc2ApiAbilityData())
                .addEffects(sc2ApiEffectData())
                .addBuffs(sc2ApiBuffData())
                .addUnits(sc2ApiUnitTypeData())
                .addUpgrades(sc2ApiUpgradeData())
                .build();
    }

    public static Data.AbilityData sc2ApiAbilityData() {
        return Data.AbilityData.newBuilder()
                .setAbilityId(ABILITY_ID)
                .setLinkName(ABILITY_LINK_NAME)
                .setLinkIndex(ABILITY_LINK_INDEX)
                .setButtonName(ABILITY_BUTTON_NAME)
                .setFriendlyName(ABILITY_FRIENDLY_NAME)
                .setHotkey(ABILITY_HOTKEY)
                .setRemapsToAbilityId(ABILITY_REMAPS_TO_ID)
                .setAvailable(ABILITY_AVAILABLE)
                .setTarget(Data.AbilityData.Target.None)
                .setAllowMinimap(ABILITY_ALLOW_MINIMAP)
                .setAllowAutocast(ABILITY_ALLOW_AUTOCAST)
                .setIsBuilding(ABILITY_IS_BUILDING)
                .setFootprintRadius(ABILITY_FOOTPRINT_RADIUS)
                .setIsInstantPlacement(ABILITY_INSTANT_PLACEMENT)
                .setCastRange(ABILITY_CAST_RANGE)
                .build();
    }

    public static Data.BuffData sc2ApiBuffData() {
        return Data.BuffData.newBuilder().setBuffId(BUFF_ID).setName(BUFF_NAME).build();
    }

    public static Data.EffectData sc2ApiEffectData() {
        return Data.EffectData.newBuilder()
                .setEffectId(EFFECT_ID)
                .setName(EFFECT_NAME)
                .setFriendlyName(EFFECT_FRIENDLY_NAME)
                .setRadius(EFFECT_RADIUS)
                .build();
    }

    public static Data.UnitTypeData sc2ApiUnitTypeData() {
        return Data.UnitTypeData.newBuilder()
                .setUnitId(UNIT_TYPE_COLOSSUS)
                .setName(UNIT_NAME)
                .setAvailable(UNIT_AVAILABLE)
                .setCargoSize(UNIT_CARGO_SPACE_TAKEN)
                .setMineralCost(UNIT_MINERAL_COST)
                .setVespeneCost(UNIT_VESPENE_COST)
                .setFoodRequired(UNIT_FOOD_REQUIRED)
                .setFoodProvided(UNIT_FOOD_PROVIDED)
                .setAbilityId(ABILITY_ID)
                .setRace(UNIT_RACE)
                .setBuildTime(UNIT_BUILD_TIME)
                .setHasVespene(UNIT_HAS_VESPENE)
                .setHasMinerals(UNIT_HAS_MINERALS)
                .setSightRange(UNIT_SIGHT_RANGE)
                .addTechAlias(TYPE_ID_ARCHON)
                .setUnitAlias(UNIT_TYPE_COLOSSUS)
                .setTechRequirement(UNIT_TYPE_COLOSSUS)
                .setRequireAttached(UNIT_REQUIRE_ATTACHED)
                .addAttributes(UNIT_ATTRIBUTE)
                .setMovementSpeed(UNIT_SPEED)
                .setArmor(UNIT_ARMOR)
                .addWeapons(sc2ApiWeapon())
                .build();
    }

    public static Data.Weapon sc2ApiWeapon() {
        return Data.Weapon.newBuilder()
                .setType(Data.Weapon.TargetType.Air)
                .setDamage(WEAPON_DAMAGE)
                .addDamageBonus(sc2ApiDamageBonus())
                .setAttacks(WEAPON_ATTACKS)
                .setRange(WEAPON_RANGE)
                .setSpeed(WEAPON_SPEED)
                .build();
    }

    public static Data.DamageBonus sc2ApiDamageBonus() {
        return Data.DamageBonus.newBuilder()
                .setAttribute(Data.Attribute.Biological)
                .setBonus(WEAPON_DAMAGE_BONUS)
                .build();
    }

    public static Data.UpgradeData sc2ApiUpgradeData() {
        return Data.UpgradeData.newBuilder()
                .setUpgradeId(20)
                .setName(UPGRADE_NAME)
                .setMineralCost(UPGRADE_MINERAL_COST)
                .setVespeneCost(UPGRADE_VESPENE_COST)
                .setResearchTime(UPGRADE_RESEARCH_TIME)
                .setAbilityId(790)
                .build();
    }

    public static Raw.StartRaw sc2ApiStartRaw() {
        return Raw.StartRaw.newBuilder()
                .setMapSize(sc2ApiSize2dI())
                .setPathingGrid(sc2ApiImageData())
                .setTerrainHeight(sc2ApiImageData())
                .setPlacementGrid(sc2ApiImageData())
                .setPlayableArea(sc2ApiRectangleI())
                .addStartLocations(sc2ApiPoint2d())
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithQuery() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.in_game)
                .setQuery(sc2ApiResponseQuery())
                .build();
    }

    public static Query.ResponseQuery sc2ApiResponseQuery() {
        return Query.ResponseQuery.newBuilder()
                .addAbilities(sc2ApiAvailableAbilities())
                .addPathing(sc2ApiPathing())
                .addPlacements(sc2ApiBuildingPlacement())
                .build();
    }

    public static Query.ResponseQueryAvailableAbilities sc2ApiAvailableAbilities() {
        return Query.ResponseQueryAvailableAbilities.newBuilder()
                .addAbilities(sc2ApiAvailableAbility())
                .setUnitTag(UNIT_TAG)
                .setUnitTypeId(TYPE_ID_ARCHON)
                .build();
    }

    public static Query.ResponseQueryPathing sc2ApiPathing() {
        return Query.ResponseQueryPathing.newBuilder().setDistance(DISTANCE).build();
    }

    public static Query.ResponseQueryBuildingPlacement sc2ApiBuildingPlacement() {
        return Query.ResponseQueryBuildingPlacement.newBuilder()
                .setResult(Error.ActionResult.AlreadyTargeted)
                .build();
    }

    public static Sc2Api.Response sc2ApiResponseWithObserverAction() {
        return aSc2ApiResponse().setStatus(Sc2Api.Status.in_game)
                .setObsAction(sc2ApiObserverAction())
                .build();
    }

    private static Sc2Api.ResponseObserverAction sc2ApiObserverAction() {
        return Sc2Api.ResponseObserverAction.newBuilder().build();
    }

    public static Sc2Api.Action sc2ApiAction() {
        return aSc2ApiAction()
                .setActionRaw(sc2ApiActionRawWithUnitCommand())
                .setActionFeatureLayer(sc2ApiActionSpatialWithUnitCommand())
                .setActionRender(sc2ApiActionSpatialWithCameraMove())
                .setActionUi(sc2ApiActionUiWithControlGroup())
                .setActionChat(sc2ApiActionWithChat())
                .build();
    }

    public static Sc2Api.Action.Builder aSc2ApiAction() {
        return Sc2Api.Action.newBuilder();
    }

    public static Raw.ActionRaw sc2ApiActionRawWithUnitCommand() {
        return aSc2ApiActionRaw().setUnitCommand(sc2ApiActionRawUnitCommand()).build();
    }

    public static Raw.ActionRawUnitCommand sc2ApiActionRawUnitCommand() {
        return Raw.ActionRawUnitCommand.newBuilder()
                .setAbilityId(PSI_STORM_ABILITY_ID)
                .setTargetUnitTag(UNIT_TAG)
                .addAllUnitTags(SC2API_UNIT_TAGS)
                .setQueueCommand(true)
                .build();
    }

    public static Raw.ActionRawUnitCommand sc2ApiActionRawUnitCommandWithTargetedWorldSpacePosition() {
        return sc2ApiActionRawUnitCommand().toBuilder().setTargetWorldSpacePos(sc2ApiPoint2d()).build();
    }

    private static Common.Point2D sc2ApiPoint2d() {
        return Common.Point2D.newBuilder().setX(100.5f).setY(200.1f).build();
    }

    public static Raw.ActionRaw.Builder aSc2ApiActionRaw() {
        return Raw.ActionRaw.newBuilder();
    }

    public static Raw.ActionRaw sc2ApiActionRawWithCameraMove() {
        return aSc2ApiActionRaw().setCameraMove(sc2ApiActionRawCameraMove()).build();
    }

    public static Raw.ActionRawCameraMove sc2ApiActionRawCameraMove() {
        return Raw.ActionRawCameraMove.newBuilder().setCenterWorldSpace(sc2ApiPoint()).build();
    }

    private static Common.Point sc2ApiPoint() {
        return Common.Point.newBuilder().setX(10.1f).setY(20.2f).setZ(30.3f).build();
    }

    public static Raw.ActionRaw sc2ApiActionRawWithToggleAutocast() {
        return aSc2ApiActionRaw().setToggleAutocast(sc2ApiActionRawToggleAutocast()).build();
    }

    public static Raw.ActionRawToggleAutocast sc2ApiActionRawToggleAutocast() {
        return Raw.ActionRawToggleAutocast.newBuilder()
                .setAbilityId(ATTACK_ABILITY_ID)
                .addAllUnitTags(SC2API_UNIT_TAGS)
                .build();
    }

    public static Spatial.ActionSpatial.Builder aSc2ApiActionSpatial() {
        return Spatial.ActionSpatial.newBuilder();
    }

    public static Spatial.ActionSpatial sc2ApiActionSpatialWithUnitCommand() {
        return aSc2ApiActionSpatial().setUnitCommand(sc2ApiActionSpatialUnitCommand()).build();
    }

    public static Spatial.ActionSpatialUnitCommand sc2ApiActionSpatialUnitCommand() {
        return Spatial.ActionSpatialUnitCommand.newBuilder()
                .setAbilityId(PSI_STORM_ABILITY_ID)
                .setTargetScreenCoord(sc2ApiPointI())
                .setQueueCommand(true)
                .build();
    }

    private static Common.PointI sc2ApiPointI() {
        return Common.PointI.newBuilder().setX(10).setY(20).build();
    }

    public static Spatial.ActionSpatial sc2ApiActionSpatialWithCameraMove() {
        return aSc2ApiActionSpatial().setCameraMove(sc2ApiActionSpatialCameraMove()).build();
    }

    public static Spatial.ActionSpatialCameraMove sc2ApiActionSpatialCameraMove() {
        return Spatial.ActionSpatialCameraMove.newBuilder().setCenterMinimap(sc2ApiPointI()).build();
    }

    public static Spatial.ActionSpatial sc2ApiActionSpatialWithUnitSelectionPoint() {
        return aSc2ApiActionSpatial().setUnitSelectionPoint(sc2ApiActionSpatialUnitSelectionPoint()).build();
    }

    public static Spatial.ActionSpatialUnitSelectionPoint sc2ApiActionSpatialUnitSelectionPoint() {
        return Spatial.ActionSpatialUnitSelectionPoint.newBuilder()
                .setSelectionScreenCoord(sc2ApiPointI())
                .setType(Spatial.ActionSpatialUnitSelectionPoint.Type.AllType)
                .build();
    }

    public static Spatial.ActionSpatial sc2ApiActionSpatialWithUnitSelectionRect() {
        return aSc2ApiActionSpatial().setUnitSelectionRect(sc2ApiActionSpatialUnitSelectionRect()).build();
    }

    public static Spatial.ActionSpatialUnitCommand sc2ApiActionSpatialUnitCommandWithTargetInMinimapCoord() {
        return sc2ApiActionSpatialUnitCommand().toBuilder().setTargetMinimapCoord(sc2ApiPointI()).build();
    }

    public static Spatial.ActionSpatialUnitSelectionRect sc2ApiActionSpatialUnitSelectionRect() {
        return Spatial.ActionSpatialUnitSelectionRect.newBuilder()
                .addSelectionScreenCoord(sc2ApiRectangleI())
                .addSelectionScreenCoord(sc2ApiRectangleI())
                .setSelectionAdd(true)
                .build();
    }

    public static Common.RectangleI sc2ApiRectangleI() {
        return Common.RectangleI.newBuilder().setP0(sc2ApiPointI()).setP1(sc2ApiPointI()).build();
    }

    public static Ui.ActionUI sc2ApiActionUiWithControlGroup() {
        return aSc2ApiActionUi().setControlGroup(sc2ApiActionUiControlGroup()).build();
    }

    public static Ui.ActionControlGroup sc2ApiActionUiControlGroup() {
        return Ui.ActionControlGroup.newBuilder()
                .setAction(Ui.ActionControlGroup.ControlGroupAction.AppendAndSteal)
                .setControlGroupIndex(CONTROL_GROUP_INDEX)
                .build();
    }

    public static Ui.ActionUI.Builder aSc2ApiActionUi() {
        return Ui.ActionUI.newBuilder();
    }

    public static Ui.ActionUI sc2ApiActionUiWithSelectArmy() {
        return aSc2ApiActionUi().setSelectArmy(sc2ApiActionUiSelectArmy()).build();
    }

    public static Ui.ActionSelectArmy sc2ApiActionUiSelectArmy() {
        return Ui.ActionSelectArmy.newBuilder().setSelectionAdd(true).build();
    }

    public static Ui.ActionUI sc2ApiActionUiWithSelectWarpGates() {
        return aSc2ApiActionUi().setSelectWarpGates(sc2ApiActionUiSelectWarpGates()).build();
    }

    public static Ui.ActionSelectWarpGates sc2ApiActionUiSelectWarpGates() {
        return Ui.ActionSelectWarpGates.newBuilder().setSelectionAdd(true).build();
    }

    public static Ui.ActionUI sc2ApiActionUiWithSelectLarva() {
        return aSc2ApiActionUi().setSelectLarva(sc2ApiActionUiSelectLarva()).build();
    }

    public static Ui.ActionSelectLarva sc2ApiActionUiSelectLarva() {
        return Ui.ActionSelectLarva.newBuilder().build();
    }

    public static Ui.ActionUI sc2ApiActionUiWithSelectIdleWorker() {
        return aSc2ApiActionUi().setSelectIdleWorker(sc2ApiActionUiSelectIdleWorker()).build();
    }

    public static Ui.ActionSelectIdleWorker sc2ApiActionUiSelectIdleWorker() {
        return Ui.ActionSelectIdleWorker.newBuilder().setType(Ui.ActionSelectIdleWorker.Type.AddAll).build();
    }

    public static Ui.ActionUI sc2ApiActionUiWithMultiPanel() {
        return aSc2ApiActionUi().setMultiPanel(sc2ApiActionUiMultiPanel()).build();
    }

    public static Ui.ActionMultiPanel sc2ApiActionUiMultiPanel() {
        return Ui.ActionMultiPanel.newBuilder()
                .setType(Ui.ActionMultiPanel.Type.SelectAllOfType).setUnitIndex(UNIT_INDEX).build();
    }

    public static Ui.ActionUI sc2ApiActionUiWithCargoPanelUnload() {
        return aSc2ApiActionUi().setCargoPanel(sc2ApiActionUiCargoPanelUnload()).build();
    }

    public static Ui.ActionCargoPanelUnload sc2ApiActionUiCargoPanelUnload() {
        return Ui.ActionCargoPanelUnload.newBuilder().setUnitIndex(UNIT_INDEX).build();
    }

    public static Ui.ActionUI sc2ApiActionUiWithProductionPanelRemoveFromQueue() {
        return aSc2ApiActionUi().setProductionPanel(sc2ApiActionUiProductionPanelRemoveFromQueue()).build();
    }

    public static Ui.ActionProductionPanelRemoveFromQueue sc2ApiActionUiProductionPanelRemoveFromQueue() {
        return Ui.ActionProductionPanelRemoveFromQueue.newBuilder().setUnitIndex(UNIT_INDEX).build();
    }

    public static Ui.ActionUI sc2ApiActionUiWithToggleAutocast() {
        return aSc2ApiActionUi().setToggleAutocast(sc2ApiActionUiToggleAutocast()).build();
    }

    public static Ui.ActionToggleAutocast sc2ApiActionUiToggleAutocast() {
        return Ui.ActionToggleAutocast.newBuilder().setAbilityId(PSI_STORM_ABILITY_ID).build();
    }

    public static Sc2Api.ActionChat sc2ApiActionWithChat() {
        return Sc2Api.ActionChat.newBuilder()
                .setChannel(Sc2Api.ActionChat.Channel.Broadcast).setMessage(CHAT_MESSAGE).build();
    }

    public static Sc2Api.ActionError sc2ApiActionError() {
        return Sc2Api.ActionError.newBuilder()
                .setUnitTag(UNIT_TAG)
                .setAbilityId(PSI_STORM_ABILITY_ID)
                .setResult(Error.ActionResult.CouldntReachTarget).build();
    }

    public static Sc2Api.ChatReceived sc2ApiChatReceived() {
        return Sc2Api.ChatReceived.newBuilder().setPlayerId(PLAYER_ID).setMessage(CHAT_MESSAGE).build();
    }

    public static ActionRawUnitCommandBuilder rawUnitCommand() {
        return unitCommand().forUnits(Tag.from(UNIT_TAG)).useAbility(Abilities.ATTACK);
    }

    public static ActionRawCameraMoveBuilder rawCameraMove() {
        return cameraMove().to(Point.of(1, 2, 3));
    }

    public static ActionRawToggleAutocastBuilder rawToggleAutocast() {
        return toggleAutocast().ofAbility(Abilities.ATTACK).forUnits(Tag.from(UNIT_TAG));
    }

    public static ActionSpatialUnitCommandBuilder spatialUnitCommand() {
        return ActionSpatialUnitCommand.unitCommand().useAbility(Abilities.ATTACK);
    }

    public static ActionSpatialCameraMoveBuilder spatialCameraMove() {
        return Actions.Spatial.cameraMove().to(PointI.of(10, 10));
    }

    public static ActionSpatialUnitSelectionPointBuilder spatialUnitSelectionPoint() {
        return click()
                .on(PointI.of(10, 10))
                .withMode(ActionSpatialUnitSelectionPoint.Type.SELECT);
    }

    public static ActionSpatialUnitSelectionRectBuilder spatialUnitSelectionRect() {
        return select().of(RectangleI.of(PointI.of(0, 0), PointI.of(1, 1)));
    }

    public static ActionUiControlGroupBuilder uiControlGroup() {
        return controlGroup().on(CONTROL_GROUP_INDEX).withMode(ActionUiControlGroup.Action.SET_AND_STEAL);
    }

    public static ActionUiSelectArmyBuilder uiSelectArmy() {
        return selectArmy();
    }

    public static ActionUiSelectWarpGatesBuilder uiSelectWarpGates() {
        return selectWarpGates();
    }

    public static ActionUiSelectLarva uiSelectLarva() {
        return selectLarva();
    }

    public static ActionUiSelectIdleWorkerBuilder uiSelectIdleWorker() {
        return selectIdleWorker().withMode(ActionUiSelectIdleWorker.Type.ADD_ALL);
    }

    public static ActionUiMultiPanelBuilder uiMultiPanel() {
        return multiPanel().select(UNIT_INDEX).withMode(ActionUiMultiPanel.Type.DESELECT_UNIT);
    }

    public static ActionUiCargoPanelUnloadBuilder uiCargoPanelUnload() {
        return cargoPanelUnload().of(UNIT_INDEX);
    }

    public static ActionUiProductionPanelRemoveFromQueueBuilder uiProductionPanelRemoveFromQueue() {
        return removeFromQueue().of(UNIT_INDEX);
    }

    public static ActionUiToggleAutocastBuilder uiToggleAutocast() {
        return ActionUiToggleAutocast.toggleAutocast().ofAbility(Abilities.EFFECT_STIM);
    }

    public static ActionChat message() {
        return ActionChat.message().of(CHAT_MESSAGE).toAll().build();
    }

    public static DebugDraw.Builder debugDraw() {
        return draw().texts(debugText());
    }

    public static DebugTextBuilder debugText() {
        return text().of(DEBUG_TEXT).withColor(SAMPLE_COLOR).withSize(DEBUG_TEXT_SIZE).on(Point.of(0.5f, 0.5f));
    }

    public static DebugLineBuilder debugLine() {
        return line().of(P0, P1).withColor(SAMPLE_COLOR);
    }

    public static DebugBoxBuilder debugBox() {
        return box().of(P0, P1).withColor(SAMPLE_COLOR);
    }

    public static DebugSphereBuilder debugSphere() {
        return sphere().on(P0).withRadius(RADIUS).withColor(SAMPLE_COLOR);
    }

    public static DebugCreateUnitBuilder debugCreateUnit() {
        return createUnit()
                .ofType(Units.PROTOSS_ARCHON)
                .forPlayer(PLAYER_ID)
                .on(POS)
                .withQuantity(QUANTITY);
    }

    public static DebugKillUnitBuilder debugKillUnit() {
        return killUnit().withTags(Tag.from(UNIT_TAG));
    }

    public static DebugTestProcessBuilder debugTestProcess() {
        return testProcess().with(DebugTestProcess.Test.CRASH).delayInMillis(DELAY);
    }

    public static DebugSetScoreBuilder debugSetScore() {
        return setScore().to(SCORE);
    }

    public static DebugEndGameBuilder debugEndGame() {
        return endGame().withResult(DebugEndGame.EndResult.DECLARE_VICTORY);
    }

    public static DebugSetUnitValueBuilder debugSetUnitValue() {
        return setUnitValue()
                .forUnit(Tag.from(UNIT_TAG))
                .set(DebugSetUnitValue.UnitValue.ENERGY)
                .to(VITAL_SCORE_ENERGY);
    }

    public static Sc2Api.InterfaceOptions sc2ApiInterfaceOptions() {
        return Sc2Api.InterfaceOptions.newBuilder()
                .setRaw(true)
                .setScore(true)
                .setFeatureLayer(sc2ApiSpatialCameraSetup())
                .setRender(sc2ApiSpatialCameraSetup())
                .build();
    }

    public static Sc2Api.SpatialCameraSetup sc2ApiSpatialCameraSetup() {
        return Sc2Api.SpatialCameraSetup.newBuilder()
                .setWidth(24.0f)
                .setResolution(Common.Size2DI.newBuilder().setX(64).setY(64).build())
                .setMinimapResolution(Common.Size2DI.newBuilder().setX(64).setY(64).build())
                .build();
    }

    public static QueryPathingBuilder queryPathing() {
        return path().from(START).to(END);
    }

    public static QueryAvailableAbilitiesBuilder queryAvailableAbilities() {
        return availableAbilities().of(Tag.from(UNIT_TAG));
    }

    public static QueryBuildingPlacementBuilder queryBuildingPlacement() {
        return placeBuilding()
                .withUnit(Tag.from(UNIT_TAG))
                .useAbility(Abilities.BUILD_ARMORY)
                .on(START);
    }

    public static BuilderSyntax<SpatialCameraSetup> aSpatialSetup() {
        return spatialSetup().width(24.0f).resolution(64, 64).minimap(64, 64);
    }

    public static ActionObserverPlayerPerspectiveBuilder observerPlayerPerspective() {
        return playerPerspective().ofAll();
    }

    public static ActionObserverCameraMoveBuilder observerCameraMove() {
        return Actions.Observer.cameraMove().to(Point2d.of(1, 2)).withDistance(CAMERA_DISTANCE);
    }

    public static ActionObserverCameraFollowPlayerBuilder observerCameraFollowPlayer() {
        return cameraFollowPlayer().withId(PLAYER_ID);
    }

    public static ActionObserverCameraFollowUnitsBuilder observerCameraFollowUnits() {
        return cameraFollowUnits().withTags(Tag.tag(UNIT_TAG));
    }
}
