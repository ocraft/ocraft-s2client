package com.github.ocraft.s2client.bot;

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

import SC2APIProtocol.*;
import SC2APIProtocol.Error;
import com.google.protobuf.ByteString;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

// TODO p.picheta merge somehow with protocol fixtures?
public class GameServerResponses {

    public static final String PING_ERROR = "ping error";
    public static final int PLAYER_ID = 1;

    private static final String GAME_VERSION = "3.17.1.57218";
    private static final String DATA_VERSION = "3F2FCED08798D83B873B5543BEFA6C4B";
    private static final int DATA_BUILD = 57218;
    private static final int BASE_BUILD = 56787;

    private GameServerResponses() {
        throw new AssertionError("private constructor");
    }

    public static Sc2Api.Response ping() {
        return Sc2Api.Response.newBuilder().setPing(
                Sc2Api.ResponsePing.newBuilder()
                        .setBaseBuild(BASE_BUILD)
                        .setDataBuild(DATA_BUILD)
                        .setDataVersion(DATA_VERSION)
                        .setGameVersion(GAME_VERSION)
                        .build())
                .build();
    }

    public static Supplier<Sc2Api.Response> limitedCorrectPingResponses(int limit) {
        final AtomicInteger pingCounter = new AtomicInteger(0);
        return () -> {
            int counter = pingCounter.incrementAndGet();
            if (counter > limit) {
                return Sc2Api.Response.newBuilder().addError(PING_ERROR).build();
            } else {
                return ping();
            }
        };
    }

    public static Sc2Api.Response createGame() {
        return Sc2Api.Response.newBuilder().setCreateGame(Sc2Api.ResponseCreateGame.newBuilder().build()).build();
    }

    public static Sc2Api.Response createGameWithError() {
        return Sc2Api.Response.newBuilder().setCreateGame(
                Sc2Api.ResponseCreateGame.newBuilder()
                        .setError(Sc2Api.ResponseCreateGame.Error.MissingMap)
                        .setErrorDetails("Unable to find map with that name.")
                        .build()
        ).build();
    }

    public static Sc2Api.Response joinGame() {
        return Sc2Api.Response.newBuilder().setJoinGame(
                Sc2Api.ResponseJoinGame.newBuilder()
                        .setPlayerId(PLAYER_ID)
                        .build()
        ).build();
    }

    public static Sc2Api.Response joinGameWithError() {
        return Sc2Api.Response.newBuilder().setJoinGame(
                Sc2Api.ResponseJoinGame.newBuilder()
                        .setError(Sc2Api.ResponseJoinGame.Error.MapDoesNotExist)
                        .setErrorDetails("Missing participation")
                        .build()
        ).build();
    }

    public static Sc2Api.Response saveMap() {
        return Sc2Api.Response.newBuilder().setSaveMap(Sc2Api.ResponseSaveMap.newBuilder().build()).build();
    }

    public static Sc2Api.Response saveMapWithError() {
        return Sc2Api.Response.newBuilder().setSaveMap(
                Sc2Api.ResponseSaveMap.newBuilder()
                        .setError(Sc2Api.ResponseSaveMap.Error.InvalidMapData)
                        .build()
        ).build();
    }

    public static Sc2Api.Response leaveGame() {
        return Sc2Api.Response.newBuilder().setLeaveGame(Sc2Api.ResponseLeaveGame.newBuilder().build()).build();
    }

    public static Sc2Api.Response step() {
        return Sc2Api.Response.newBuilder().setStep(Sc2Api.ResponseStep.newBuilder().build()).build();
    }

    public static Sc2Api.Response error() {
        return Sc2Api.Response.newBuilder().addError("game error").build();
    }

    public static Sc2Api.Response saveReplay() {
        return Sc2Api.Response.newBuilder().setSaveReplay(Sc2Api.ResponseSaveReplay.newBuilder()
                .setData(ByteString.copyFromUtf8("test"))
                .build()
        ).build();
    }

    public static Sc2Api.Response saveReplayWithEmptyData() {
        return Sc2Api.Response.newBuilder().setSaveReplay(Sc2Api.ResponseSaveReplay.newBuilder()
                .setData(ByteString.EMPTY)
                .build()
        ).build();
    }

    public static Sc2Api.Response observation() {
        return Sc2Api.Response.newBuilder()
                .setStatus(Sc2Api.Status.in_game)
                .setObservation(Sc2Api.ResponseObservation.newBuilder()
                        .setObservation(Sc2Api.Observation.newBuilder()
                                .setGameLoop(1)
                                .setPlayerCommon(playerCommon())
                                .addAllAlerts(
                                        asList(Sc2Api.Alert.NydusWormDetected, Sc2Api.Alert.NuclearLaunchDetected))
                                .setRawData(observationRaw())
                                .build())
                        .build()
                ).build();
    }

    private static Sc2Api.PlayerCommon playerCommon() {
        return Sc2Api.PlayerCommon.newBuilder()
                .setPlayerId(PLAYER_ID)
                .setMinerals(1)
                .setVespene(1)
                .setFoodCap(1)
                .setFoodUsed(1)
                .setFoodArmy(1)
                .setFoodWorkers(1)
                .setIdleWorkerCount(1)
                .setArmyCount(1)
                .setWarpGateCount(1)
                .setLarvaCount(1)
                .build();
    }

    private static Raw.ObservationRaw observationRaw() {
        return Raw.ObservationRaw.newBuilder()
                .setPlayer(Raw.PlayerRaw.newBuilder()
                        .setCamera(Common.Point.newBuilder()
                                .setX(1.0f).setY(1.0f).setZ(1.0f)
                                .build()).build())
                .setMapState(Raw.MapState.newBuilder()
                        .setVisibility(imageData())
                        .setCreep(imageData())
                        .build())
                .build();
    }

    private static Common.ImageData imageData() {
        int size = 64;
        byte[] data = new byte[size * size];
        Arrays.fill(data, (byte) 255);
        data[3402] = 2;
        return Common.ImageData.newBuilder()
                .setBitsPerPixel(8)
                .setSize(Common.Size2DI.newBuilder().setX(size).setY(size).build())
                .setData(ByteString.copyFrom(data))
                .build();
    }

    public static Sc2Api.Response abilityData() {
        return Sc2Api.Response.newBuilder().setData(Sc2Api.ResponseData.newBuilder()
                .addAbilities(Data.AbilityData.newBuilder()
                        .setAbilityId(1)
                        .setLinkIndex(2)
                        .setLinkName("ability test")
                        .build())
                .build()
        ).build();
    }

    public static Sc2Api.Response abilityDataWithUnknownRemap() {
        return Sc2Api.Response.newBuilder().setData(Sc2Api.ResponseData.newBuilder()
                .addAbilities(Data.AbilityData.newBuilder()
                        .setAbilityId(1)
                        .setRemapsToAbilityId(100000)
                        .setLinkIndex(2)
                        .setLinkName("ability with invalid remap test")
                        .build())
                .build()
        ).build();
    }

    public static Sc2Api.Response unitTypeData() {
        return Sc2Api.Response.newBuilder().setData(Sc2Api.ResponseData.newBuilder()
                .addUnits(Data.UnitTypeData.newBuilder()
                        .setUnitId(1)
                        .setName("unit type test")
                        .build())
                .build()
        ).build();
    }

    public static Sc2Api.Response upgradeData() {
        return Sc2Api.Response.newBuilder().setData(Sc2Api.ResponseData.newBuilder()
                .addUpgrades(Data.UpgradeData.newBuilder()
                        .setUpgradeId(1)
                        .setName("upgrade test")
                        .build())
                .build()
        ).build();
    }

    public static Sc2Api.Response buffData() {
        return Sc2Api.Response.newBuilder().setData(Sc2Api.ResponseData.newBuilder()
                .addBuffs(Data.BuffData.newBuilder()
                        .setBuffId(1)
                        .setName("buff test")
                        .build())
                .build()
        ).build();
    }

    public static Sc2Api.Response effectData() {
        return Sc2Api.Response.newBuilder().setData(Sc2Api.ResponseData.newBuilder()
                .addEffects(Data.EffectData.newBuilder()
                        .setEffectId(1)
                        .setName("effect test")
                        .setFriendlyName("effect friendly name test")
                        .setRadius(2)
                        .build())
                .build()
        ).build();
    }

    public static Sc2Api.Response gameInfo() {
        return Sc2Api.Response.newBuilder().setGameInfo(Sc2Api.ResponseGameInfo.newBuilder()
                .setMapName("Lava Flow")
                .addPlayerInfo(Sc2Api.PlayerInfo.newBuilder()
                        .setPlayerId(1)
                        .setRaceRequested(Common.Race.Protoss).build())
                .setStartRaw(Raw.StartRaw.newBuilder()
                        .setMapSize(Common.Size2DI.newBuilder().setX(64).setY(64).build())
                        .setPlayableArea(Common.RectangleI.newBuilder()
                                .setP0(Common.PointI.newBuilder().setX(0).setY(0).build())
                                .setP1(Common.PointI.newBuilder().setX(64).setY(64).build())
                                .build())
                        .setPathingGrid(imageData())
                        .setPlacementGrid(imageData())
                        .setTerrainHeight(imageData())
                        .addStartLocations(Common.Point2D.newBuilder().setX(10).setY(10).build())
                        .build())
                .setOptions(Sc2Api.InterfaceOptions.newBuilder().setRaw(true).build())
                .build()
        ).build();
    }

    public static Raw.Unit sc2ApiUnit(Long tag, Raw.Alliance alliance, boolean withOrders, float buildProgress) {
        Raw.Unit.Builder unitBuilder = Raw.Unit.newBuilder()
                .setDisplayType(Raw.DisplayType.Visible)
                .setAlliance(alliance)
                .setTag(tag)
                .setUnitType(2)
                .setOwner(PLAYER_ID)
                .setPos(Common.Point.newBuilder().setX(1.0f).setY(1.0f).setZ(1.0f).build())
                .setFacing(1.0f)
                .setRadius(1.0f)
                .setBuildProgress(buildProgress)
                .setCloak(Raw.CloakState.NotCloaked)
                .setDetectRange(1.0f)
                .setRadarRange(1.0f)
                .setIsSelected(false)
                .setIsOnScreen(true)
                .setIsBlip(true)
                .setIsPowered(false)
                .setHealth(1.0f)
                .setHealthMax(1.0f)
                .setShield(1.0f)
                .setShieldMax(1.0f)
                .setEnergy(1.0f)
                .setEnergyMax(1.0f)
                .setMineralContents(1100)
                .setVespeneContents(500)
                .setIsFlying(false)
                .setIsBurrowed(false)
                .setAddOnTag(2L)
                .addPassengers(sc2ApiPassengerUnit())
                .setCargoSpaceTaken(2)
                .setCargoSpaceMax(3)
                .addAllBuffIds(asList(16, 17))
                .setAssignedHarvesters(2)
                .setIdealHarvesters(2)
                .setWeaponCooldown(2)
                .setEngagedTargetTag(2);

        if (withOrders) unitBuilder.addOrders(sc2ApiUnitOrder());
        return unitBuilder.build();
    }

    public static Raw.UnitOrder sc2ApiUnitOrder() {
        return Raw.UnitOrder.newBuilder()
                .setAbilityId(2)
                .setTargetUnitTag(2)
                .setProgress(2)
                .build();
    }

    public static Raw.PassengerUnit sc2ApiPassengerUnit() {
        return Raw.PassengerUnit.newBuilder()
                .setTag(8)
                .setHealth(3)
                .setHealthMax(3)
                .setShield(3)
                .setShieldMax(3)
                .setEnergy(3)
                .setEnergyMax(3)
                .setUnitType(3)
                .build();
    }

    public static Sc2Api.Response quickSave() {
        return Sc2Api.Response.newBuilder()
                .setQuickSave(Sc2Api.ResponseQuickSave.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response quickLoad() {
        return Sc2Api.Response.newBuilder()
                .setQuickLoad(Sc2Api.ResponseQuickLoad.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response action() {
        return Sc2Api.Response.newBuilder()
                .setAction(Sc2Api.ResponseAction.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response restartGame() {
        return Sc2Api.Response.newBuilder()
                .setStatus(Sc2Api.Status.in_game)
                .setRestartGame(Sc2Api.ResponseRestartGame.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response restartGameWithError() {
        return Sc2Api.Response.newBuilder()
                .setStatus(Sc2Api.Status.in_game)
                .setRestartGame(Sc2Api.ResponseRestartGame.newBuilder()
                        .setError(Sc2Api.ResponseRestartGame.Error.LaunchError)
                        .setErrorDetails("restart game error")
                        .build())
                .build();
    }

    public static Sc2Api.Response query() {
        return Sc2Api.Response.newBuilder()
                .setQuery(Query.ResponseQuery.newBuilder()
                        .addAbilities(Query.ResponseQueryAvailableAbilities.newBuilder()
                                .setUnitTag(2)
                                .setUnitTypeId(2)
                                .addAbilities(Common.AvailableAbility.newBuilder()
                                        .setAbilityId(2)
                                        .setRequiresPoint(true)
                                        .build())
                                .build())
                        .addPathing(Query.ResponseQueryPathing.newBuilder()
                                .setDistance(3.0f)
                                .build())
                        .addPlacements(Query.ResponseQueryBuildingPlacement.newBuilder()
                                .setResult(Error.ActionResult.Success)
                                .build())
                        .build())
                .build();
    }

    public static Sc2Api.Response debug() {
        return Sc2Api.Response.newBuilder()
                .setDebug(Sc2Api.ResponseDebug.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response obsAction() {
        return Sc2Api.Response.newBuilder()
                .setObsAction(Sc2Api.ResponseObserverAction.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response replayInfo() {
        return Sc2Api.Response.newBuilder()
                .setReplayInfo(sc2ApiResponseReplayInfo())
                .build();
    }

    public static Sc2Api.ResponseReplayInfo sc2ApiResponseReplayInfo() {
        return Sc2Api.ResponseReplayInfo.newBuilder()
                .setMapName("Lava Flow")
                .setLocalMapPath("/tst")
                .addPlayerInfo(sc2ApiPlayerInfoExtra())
                .addPlayerInfo(sc2ApiPlayerInfoExtra())
                .setGameDurationLoops(1)
                .setGameDurationSeconds(1)
                .setGameVersion(GAME_VERSION)
                .setDataVersion(DATA_VERSION)
                .setBaseBuild(BASE_BUILD)
                .setDataBuild(DATA_BUILD)
                .build();
    }

    public static Sc2Api.PlayerInfoExtra sc2ApiPlayerInfoExtra() {
        return Sc2Api.PlayerInfoExtra.newBuilder()
                .setPlayerInfo(sc2ApiPlayerInfo())
                .setPlayerResult(Sc2Api.PlayerResult.newBuilder()
                        .setPlayerId(PLAYER_ID)
                        .setResult(Sc2Api.Result.Victory)
                        .build())
                .setPlayerMmr(1)
                .setPlayerApm(1)
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

    public static Sc2Api.Response startReplay() {
        return Sc2Api.Response.newBuilder()
                .setStatus(Sc2Api.Status.in_game)
                .setStartReplay(Sc2Api.ResponseStartReplay.newBuilder().build())
                .build();
    }

    public static Sc2Api.Response replayInfoWithError() {
        return Sc2Api.Response.newBuilder()
                .setReplayInfo(Sc2Api.ResponseReplayInfo.newBuilder()
                        .setError(Sc2Api.ResponseReplayInfo.Error.DownloadError)
                        .setErrorDetails("replay info error")
                        .build())
                .build();
    }

    public static Sc2Api.Response startReplayWithError() {
        return Sc2Api.Response.newBuilder()
                .setStatus(Sc2Api.Status.in_game)
                .setStartReplay(Sc2Api.ResponseStartReplay.newBuilder()
                        .setError(Sc2Api.ResponseStartReplay.Error.InvalidMapData)
                        .setErrorDetails("start replay error")
                        .build())
                .build();
    }

    public static Sc2Api.Response startReplayWithInvalidState() {
        return Sc2Api.Response.newBuilder()
                .setStatus(Sc2Api.Status.launched)
                .setStartReplay(Sc2Api.ResponseStartReplay.newBuilder().build())
                .build();
    }
}
