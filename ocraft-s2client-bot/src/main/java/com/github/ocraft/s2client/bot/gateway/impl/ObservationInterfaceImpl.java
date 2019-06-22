package com.github.ocraft.s2client.bot.gateway.impl;

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

import com.github.ocraft.s2client.bot.ClientError;
import com.github.ocraft.s2client.bot.gateway.ObservationInterface;
import com.github.ocraft.s2client.bot.gateway.UnitInPool;
import com.github.ocraft.s2client.protocol.GeneralizableAbility;
import com.github.ocraft.s2client.protocol.action.Action;
import com.github.ocraft.s2client.protocol.action.raw.ActionRaw;
import com.github.ocraft.s2client.protocol.action.spatial.ActionSpatial;
import com.github.ocraft.s2client.protocol.data.*;
import com.github.ocraft.s2client.protocol.game.raw.StartRaw;
import com.github.ocraft.s2client.protocol.observation.ChatReceived;
import com.github.ocraft.s2client.protocol.observation.Observation;
import com.github.ocraft.s2client.protocol.observation.PlayerCommon;
import com.github.ocraft.s2client.protocol.observation.PlayerResult;
import com.github.ocraft.s2client.protocol.observation.raw.*;
import com.github.ocraft.s2client.protocol.observation.spatial.ImageData;
import com.github.ocraft.s2client.protocol.request.RequestData;
import com.github.ocraft.s2client.protocol.request.Requests;
import com.github.ocraft.s2client.protocol.response.ResponseData;
import com.github.ocraft.s2client.protocol.response.ResponseGameInfo;
import com.github.ocraft.s2client.protocol.response.ResponseObservation;
import com.github.ocraft.s2client.protocol.score.Score;
import com.github.ocraft.s2client.protocol.spatial.Point;
import com.github.ocraft.s2client.protocol.spatial.Point2d;
import com.github.ocraft.s2client.protocol.unit.Alliance;
import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toMap;

// TODO p.picheta should make this thread safe/immutable?
class ObservationInterfaceImpl implements ObservationInterface {

    private final ControlInterfaceImpl controlInterface;

    private int playerId;
    private final UnitPool unitPool = new UnitPool();
    private Observation observation;
    private long currentGameLoop;
    private long previousGameLoop;
    private List<ActionRaw> rawActions = new ArrayList<>();
    private List<ActionSpatial> featureLayerActions = new ArrayList<>();
    private List<ActionSpatial> renderedActions = new ArrayList<>();
    private List<ChatReceived> chat = new ArrayList<>();
    private List<PowerSource> powerSources = new ArrayList<>();
    private List<EffectLocations> effects = new ArrayList<>();
    private List<Upgrade> upgrades = new ArrayList<>();
    private List<Upgrade> upgradesPrevious = new ArrayList<>();
    private List<PlayerResult> playerResults = new ArrayList<>();

    private boolean abilitiesCached;
    private boolean unitTypesCached;
    private boolean upgradesCached;
    private boolean buffsCached;
    private boolean effectsCached;
    private boolean gameInfoCached;

    private Map<Ability, AbilityData> abilities = new HashMap<>();
    private Map<UnitType, UnitTypeData> unitTypes = new HashMap<>();
    private Map<Upgrade, UpgradeData> upgradeIds = new HashMap<>();
    private Map<Buff, BuffData> buffs = new HashMap<>();
    private Map<Effect, EffectData> effectIds = new HashMap<>();
    private ResponseGameInfo gameInfo;
    private Point startLocation;

    ObservationInterfaceImpl(ControlInterfaceImpl controlInterface) {
        require("control interface", controlInterface);
        this.controlInterface = controlInterface;
    }

    private ControlInterfaceImpl control() {
        return controlInterface;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public long getGameLoop() {
        return currentGameLoop;
    }

    @Override
    public List<UnitInPool> getUnits() {
        return getUnits(unitInPool -> true);
    }

    @Override
    public List<UnitInPool> getUnits(Alliance alliance, Predicate<UnitInPool> filter) {
        return getUnits(unitInPool ->
                unitInPool.getUnit().filter(unit ->
                        unit.getAlliance().equals(alliance)).isPresent() &&
                        filter.test(unitInPool));
    }

    @Override
    public List<UnitInPool> getUnits(Alliance alliance) {
        return getUnits(alliance, unitInPool ->
                unitInPool.getUnit().filter(unit -> unit.getAlliance().equals(alliance)).isPresent());
    }

    @Override
    public List<UnitInPool> getUnits(Predicate<UnitInPool> filter) {
        List<UnitInPool> units = new ArrayList<>();
        unitPool().forEachExistingUnit(unitInPool -> {
            if (filter.test(unitInPool)) units.add(unitInPool);
        });
        return units;
    }

    @Override
    public UnitInPool getUnit(Tag tag) {
        return unitPool().getExistingUnit(tag).orElse(nothing());
    }

    @Override
    public List<ActionRaw> getRawActions() {
        return rawActions;
    }

    @Override
    public List<ActionSpatial> getFeatureLayerActions() {
        return featureLayerActions;
    }

    @Override
    public List<ActionSpatial> getRenderedActions() {
        return renderedActions;
    }

    @Override
    public List<ChatReceived> getChatMessages() {
        return chat;
    }

    @Override
    public List<PowerSource> getPowerSources() {
        return powerSources;
    }

    @Override
    public List<EffectLocations> getEffects() {
        return effects;
    }

    @Override
    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    @Override
    public Score getScore() {
        return Optional.ofNullable(observation).flatMap(Observation::getScore).orElse(nothing());
    }

    // TODO p.picheta add remaps_from_ability_id
    @Override
    public Map<Ability, AbilityData> getAbilityData(boolean forceRefresh) {
        if (forceRefresh) {
            abilitiesCached = false;
        }

        if (abilitiesCached) {
            return abilities;
        }

        Optional<ResponseData> responseData = control().waitForResponse(
                control().proto().sendRequest(Requests.data().of(RequestData.Type.ABILITIES))
        ).flatMap(response -> response.as(ResponseData.class));

        responseData.ifPresent(data -> {
            abilities = data.getAbilities().stream()
                    .peek(checkAbilityRemapValidity(data.getAbilities().size()))
                    .collect(toMap(AbilityData::getAbility, effectData -> effectData));
            abilitiesCached = true;
        });

        return abilities;
    }

    private Consumer<AbilityData> checkAbilityRemapValidity(int maxId) {
        return abilityData -> abilityData.getRemapsToAbility().ifPresent(ability ->
                control().errorIf(
                        ability.getAbilityId() >= maxId,
                        ClientError.INVALID_ABILITY_REMAP,
                        Collections.emptyList()));
    }

    @Override
    public Map<UnitType, UnitTypeData> getUnitTypeData(boolean forceRefresh) {
        if (forceRefresh) {
            unitTypesCached = false;
        }

        if (unitTypesCached) {
            return unitTypes;
        }

        Optional<ResponseData> responseData = control().waitForResponse(
                control().proto().sendRequest(Requests.data().of(RequestData.Type.UNITS))
        ).flatMap(response -> response.as(ResponseData.class));

        responseData.ifPresent(data -> {
            unitTypes = data.getUnitTypes().stream()
                    .collect(toMap(UnitTypeData::getUnitType, unitTypeData -> unitTypeData));
            unitTypesCached = true;
        });

        return unitTypes;
    }

    @Override
    public Map<Upgrade, UpgradeData> getUpgradeData(boolean forceRefresh) {
        if (forceRefresh) {
            upgradesCached = false;
        }

        if (upgradesCached) {
            return upgradeIds;
        }

        Optional<ResponseData> responseData = control().waitForResponse(
                control().proto().sendRequest(Requests.data().of(RequestData.Type.UPGRADES))
        ).flatMap(response -> response.as(ResponseData.class));

        responseData.ifPresent(data -> {
            upgradeIds = data.getUpgrades().stream()
                    .collect(toMap(UpgradeData::getUpgrade, upgradeData -> upgradeData));
            upgradesCached = true;
        });

        return upgradeIds;
    }

    @Override
    public Map<Buff, BuffData> getBuffData(boolean forceRefresh) {
        if (forceRefresh) {
            buffsCached = false;
        }

        if (buffsCached) {
            return buffs;
        }

        Optional<ResponseData> responseData = control().waitForResponse(
                control().proto().sendRequest(Requests.data().of(RequestData.Type.BUFFS))
        ).flatMap(response -> response.as(ResponseData.class));

        responseData.ifPresent(data -> {
            buffs = data.getBuffs().stream().collect(toMap(BuffData::getBuff, buffData -> buffData));
            buffsCached = true;
        });

        return buffs;
    }

    @Override
    public Map<Effect, EffectData> getEffectData(boolean forceRefresh) {
        if (forceRefresh) {
            effectsCached = false;
        }

        if (effectsCached) {
            return effectIds;
        }

        Optional<ResponseData> responseData = control().waitForResponse(
                control().proto().sendRequest(Requests.data().of(RequestData.Type.EFFECTS))
        ).flatMap(response -> response.as(ResponseData.class));

        responseData.ifPresent(data -> {
            effectIds = data.getEffects().stream().collect(toMap(EffectData::getEffect, effectData -> effectData));
            effectsCached = true;
        });

        return effectIds;
    }

    @Override
    public ResponseGameInfo getGameInfo() {
        if (gameInfoCached) {
            return gameInfo;
        }

        Optional<ResponseGameInfo> responseData = control().waitForResponse(
                control().proto().sendRequest(Requests.gameInfo())
        ).flatMap(response -> response.as(ResponseGameInfo.class));

        responseData.ifPresent(info -> {
            gameInfo = info;
            gameInfoCached = true;
        });

        return gameInfo;
    }

    @Override
    public int getMinerals() {
        return Optional.ofNullable(observation)
                .map(Observation::getPlayerCommon)
                .map(PlayerCommon::getMinerals)
                .orElse(0);
    }

    @Override
    public int getVespene() {
        return Optional.ofNullable(observation)
                .map(Observation::getPlayerCommon)
                .map(PlayerCommon::getVespene)
                .orElse(0);
    }

    @Override
    public int getFoodCap() {
        return Optional.ofNullable(observation)
                .map(Observation::getPlayerCommon)
                .map(PlayerCommon::getFoodCap)
                .orElse(0);
    }

    @Override
    public int getFoodUsed() {
        return Optional.ofNullable(observation)
                .map(Observation::getPlayerCommon)
                .map(PlayerCommon::getFoodUsed)
                .orElse(0);
    }

    @Override
    public int getFoodArmy() {
        return Optional.ofNullable(observation)
                .map(Observation::getPlayerCommon)
                .map(PlayerCommon::getFoodArmy)
                .orElse(0);
    }

    @Override
    public int getFoodWorkers() {
        return Optional.ofNullable(observation)
                .map(Observation::getPlayerCommon)
                .map(PlayerCommon::getFoodWorkers)
                .orElse(0);
    }

    @Override
    public int getIdleWorkerCount() {
        return Optional.ofNullable(observation)
                .map(Observation::getPlayerCommon)
                .map(PlayerCommon::getIdleWorkerCount)
                .orElse(0);
    }

    @Override
    public int getArmyCount() {
        return Optional.ofNullable(observation)
                .map(Observation::getPlayerCommon)
                .map(PlayerCommon::getArmyCount)
                .orElse(0);
    }

    @Override
    public int getWarpGateCount() {
        return Optional.ofNullable(observation)
                .map(Observation::getPlayerCommon)
                .flatMap(PlayerCommon::getWarpGateCount)
                .orElse(0);
    }

    @Override
    public Point getCameraPos() {
        return Optional.ofNullable(observation)
                .flatMap(Observation::getRaw)
                .map(ObservationRaw::getPlayer)
                .map(PlayerRaw::getCamera)
                .orElse(nothing());
    }

    @Override
    public Point getStartLocation() {
        return startLocation;
    }

    @Override
    public List<PlayerResult> getResults() {
        return playerResults;
    }

    @Override
    public boolean hasCreep(Point2d point) {
        return Optional.ofNullable(observation)
                .flatMap(Observation::getRaw)
                .map(ObservationRaw::getMapState)
                .map(MapState::getCreep)
                .map(imageData -> imageData.sample(point, imageOrigin()))
                .orElse(0) > 0;
    }

    private ImageData.Origin imageOrigin() {
        return !isVersionCompatible("4.8.5") ? ImageData.Origin.UPPER_LEFT : ImageData.Origin.BOTTOM_LEFT;
    }

    @Override
    public Visibility getVisibility(Point2d point) {
        return Optional.ofNullable(observation)
                .flatMap(Observation::getRaw)
                .map(ObservationRaw::getMapState)
                .map(MapState::getVisibility)
                .map(imageData -> imageData.sample(point, imageOrigin()))
                .map(Visibility::from)
                .orElse(Visibility.FULL_HIDDEN);
    }

    @Override
    public boolean isPathable(Point2d point) {
        Optional<StartRaw> startRaw = getGameInfo().getStartRaw();
        if (!startRaw.isPresent()) return false;

        ImageData pathingGrid = startRaw.get().getPathingGrid();
        ImageData.Origin origin = imageOrigin();
        return (pathingGrid.getBitsPerPixel() == 1 && pathingGrid.sample(point, origin) == 1) ||
                (pathingGrid.getBitsPerPixel() == 8 && pathingGrid.sample(point, origin) != 255);
    }

    @Override
    public boolean isPlacable(Point2d point) {
        Optional<StartRaw> startRaw = getGameInfo().getStartRaw();
        if (!startRaw.isPresent()) return false;
        ImageData placementGrid = startRaw.get().getPlacementGrid();
        ImageData.Origin origin = imageOrigin();
        return (placementGrid.getBitsPerPixel() == 1 && placementGrid.sample(point, origin) == 1) ||
                (placementGrid.getBitsPerPixel() == 8 && placementGrid.sample(point, origin) == 255);
    }

    @Override
    public float terrainHeight(Point2d point) {
        Optional<StartRaw> startRaw = getGameInfo().getStartRaw();
        int sample = 0;
        if (startRaw.isPresent()) {
            sample = startRaw.get().getTerrainHeight().sample(point, imageOrigin());
        }
        float scale;
        if (isVersionCompatible("4.8.5")) {
            scale = 16.0f;
        } else {
            scale = 100.0f;
        }
        return -scale + 2 * scale * (float) sample / 255.0f;

    }

    private boolean isVersionCompatible(String version) {
        if ("4.8.5".equals(version)) {
            return controlInterface.proto().getBaseBuild() > 73286;
        } else {
            return false;
        }
    }

    @Override
    public Observation getRawObservation() {
        return observation;
    }

    void clearFlags() {
        playerId = 0;
        abilitiesCached = false;
        unitTypesCached = false;
        upgradesCached = false;
        buffsCached = false;
        effectsCached = false;
        gameInfoCached = false;
    }

    void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    boolean updateObservation(ResponseObservation responseObservation) {

        observation = responseObservation.getObservation();
        previousGameLoop = currentGameLoop;
        currentGameLoop = responseObservation.getObservation().getGameLoop();
        playerId = observation.getPlayerCommon().getPlayerId();

        if (gameLoopChanged()) {
            updateActions(responseObservation);
        }

        unitPool().switchExistingToPrevious();
        observation.getRaw()
                .map(ObservationRaw::getUnits)
                .orElse(Collections.emptySet())
                .forEach(unit -> unitPool().createUnit(unit.getTag()).update(unit, currentGameLoop, true));

        if (control().isUseGeneralizedAbilityId()) {
            unitPool().forEachExistingUnit(unitInPool -> {
                Optional<Unit> unit = unitInPool.getUnit();
                unit.ifPresent(u -> unitInPool.update(u.generalizeAbility(this::getGeneralizedAbility)));
            });
        }

        chat.clear();
        chat.addAll(responseObservation.getChat());
        powerSources = new ArrayList<>(Optional.ofNullable(observation)
                .flatMap(Observation::getRaw)
                .map(ObservationRaw::getPlayer)
                .map(PlayerRaw::getPowerSources)
                .orElse(Collections.emptySet()));
        effects = new ArrayList<>(Optional.ofNullable(observation)
                .flatMap(Observation::getRaw)
                .map(ObservationRaw::getEffects)
                .orElse(Collections.emptySet()));

        upgradesPrevious = upgrades;
        upgrades = new ArrayList<>(Optional.ofNullable(observation)
                .flatMap(Observation::getRaw)
                .map(ObservationRaw::getPlayer)
                .map(PlayerRaw::getUpgrades)
                .orElse(Collections.emptySet()));
        playerResults = responseObservation.getPlayerResults();

        return true;
    }

    private void updateActions(ResponseObservation responseObservation) {
        rawActions = responseObservation.getActions().stream()
                .filter(action -> action.getRaw().isPresent())
                .map(Action::getRaw)
                .map(Optional::get)
                .map(generalizeAbility())
                .collect(Collectors.toList());
        featureLayerActions = responseObservation.getActions().stream()
                .filter(action -> action.getFeatureLayer().isPresent())
                .map(Action::getFeatureLayer)
                .map(Optional::get)
                .map(generalizeAbility())
                .collect(Collectors.toList());
        renderedActions = responseObservation.getActions().stream()
                .filter(action -> action.getFeatureLayer().isPresent())
                .map(Action::getFeatureLayer)
                .map(Optional::get)
                .map(generalizeAbility())
                .collect(Collectors.toList());
    }

    private <T extends GeneralizableAbility<T>> UnaryOperator<T> generalizeAbility() {
        return toGeneralize -> {
            if (control().isUseGeneralizedAbilityId()) {
                return toGeneralize.generalizeAbility(this::getGeneralizedAbility);
            } else {
                return toGeneralize;
            }
        };
    }

    Ability getGeneralizedAbility(Ability ability) {
        if (!isSet(ability)) return ability;
        if (getAbilityData(false).containsKey(ability)) {
            return getAbilityData(false).get(ability).getRemapsToAbility().filter(Ability::isKnown).orElse(ability);
        } else {
            return ability;
        }
    }

    boolean gameLoopChanged() {
        return previousGameLoop != currentGameLoop;
    }

    UnitPool unitPool() {
        return unitPool;
    }

    void setStartLocation(Point position) {
        this.startLocation = position;
    }

    Set<Upgrade> getUpgradesPrevious() {
        return new HashSet<>(upgradesPrevious);
    }
}
