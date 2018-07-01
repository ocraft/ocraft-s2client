package com.github.ocraft.s2client.protocol.action;

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

import SC2APIProtocol.Error;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.action.ActionResult.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class ActionResultTest {

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "actionResultMappings")
    void mapsSc2ApiActionResult(Error.ActionResult sc2ApiActionResult, ActionResult expectedActionResult) {
        assertThat(ActionResult.from(sc2ApiActionResult)).isEqualTo(expectedActionResult);
    }

    private static Stream<Arguments> actionResultMappings() {
        return Stream.of(
                of(Error.ActionResult.Success, SUCCESS),
                of(Error.ActionResult.NotSupported, NOT_SUPPORTED),
                of(Error.ActionResult.Error, ERROR),
                of(Error.ActionResult.CantQueueThatOrder, CANT_QUEUE_THAT_ORDER),
                of(Error.ActionResult.Retry, RETRY),
                of(Error.ActionResult.Cooldown, COOLDOWN),
                of(Error.ActionResult.QueueIsFull, QUEUE_IS_FULL),
                of(Error.ActionResult.RallyQueueIsFull, RALLY_QUEUE_IS_FULL),
                of(Error.ActionResult.NotEnoughMinerals, NOT_ENOUGH_MINERALS),
                of(Error.ActionResult.NotEnoughVespene, NOT_ENOUGH_VESPENE),
                of(Error.ActionResult.NotEnoughTerrazine, NOT_ENOUGH_TERRAZINE),
                of(Error.ActionResult.NotEnoughCustom, NOT_ENOUGH_CUSTOM),
                of(Error.ActionResult.NotEnoughFood, NOT_ENOUGH_FOOD),
                of(Error.ActionResult.FoodUsageImpossible, FOOD_USAGE_IMPOSSIBLE),
                of(Error.ActionResult.NotEnoughLife, NOT_ENOUGH_LIFE),
                of(Error.ActionResult.NotEnoughShields, NOT_ENOUGH_SHIELDS),
                of(Error.ActionResult.NotEnoughEnergy, NOT_ENOUGH_ENERGY),
                of(Error.ActionResult.LifeSuppressed, LIFE_SUPPRESSED),
                of(Error.ActionResult.ShieldsSuppressed, SHIELDS_SUPPRESSED),
                of(Error.ActionResult.EnergySuppressed, ENERGY_SUPPRESSED),
                of(Error.ActionResult.NotEnoughCharges, NOT_ENOUGH_CHARGES),
                of(Error.ActionResult.CantAddMoreCharges, CANT_ADD_MORE_CHARGES),
                of(Error.ActionResult.TooMuchMinerals, TOO_MUCH_MINERALS),
                of(Error.ActionResult.TooMuchVespene, TOO_MUCH_VESPENE),
                of(Error.ActionResult.TooMuchTerrazine, TOO_MUCH_TERRAZINE),
                of(Error.ActionResult.TooMuchCustom, TOO_MUCH_CUSTOM),
                of(Error.ActionResult.TooMuchFood, TOO_MUCH_FOOD),
                of(Error.ActionResult.TooMuchLife, TOO_MUCH_LIFE),
                of(Error.ActionResult.TooMuchShields, TOO_MUCH_SHIELDS),
                of(Error.ActionResult.TooMuchEnergy, TOO_MUCH_ENERGY),
                of(Error.ActionResult.MustTargetUnitWithLife, MUST_TARGET_UNIT_WITH_LIFE),
                of(Error.ActionResult.MustTargetUnitWithShields, MUST_TARGET_UNIT_WITH_SHIELDS),
                of(Error.ActionResult.MustTargetUnitWithEnergy, MUST_TARGET_UNIT_WITH_ENERGY),
                of(Error.ActionResult.CantTrade, CANT_TRADE),
                of(Error.ActionResult.CantSpend, CANT_SPEND),
                of(Error.ActionResult.CantTargetThatUnit, CANT_TARGET_THAT_UNIT),
                of(Error.ActionResult.CouldntAllocateUnit, COULDNT_ALLOCATE_UNIT),
                of(Error.ActionResult.UnitCantMove, UNIT_CANT_MOVE),
                of(Error.ActionResult.TransportIsHoldingPosition, TRANSPORT_IS_HOLDING_POSITION),
                of(Error.ActionResult.BuildTechRequirementsNotMet, BUILD_TECH_REQUIREMENTS_NOT_MET),
                of(Error.ActionResult.CantFindPlacementLocation, CANT_FIND_PLACEMENT_LOCATION),
                of(Error.ActionResult.CantBuildOnThat, CANT_BUILD_ON_THAT),
                of(Error.ActionResult.CantBuildTooCloseToDropOff, CANT_BUILD_TOO_CLOSE_TO_DROP_OFF),
                of(Error.ActionResult.CantBuildLocationInvalid, CANT_BUILD_LOCATION_INVALID),
                of(Error.ActionResult.CantSeeBuildLocation, CANT_SEE_BUILD_LOCATION),
                of(Error.ActionResult.CantBuildTooCloseToCreepSource, CANT_BUILD_TOO_CLOSE_TO_CREEP_SOURCE),
                of(Error.ActionResult.CantBuildTooCloseToResources, CANT_BUILD_TOO_CLOSE_TO_RESOURCES),
                of(Error.ActionResult.CantBuildTooFarFromWater, CANT_BUILD_TOO_FAR_FROM_WATER),
                of(Error.ActionResult.CantBuildTooFarFromCreepSource, CANT_BUILD_TOO_FAR_FROM_CREEP_SOURCE),
                of(Error.ActionResult.CantBuildTooFarFromBuildPowerSource, CANT_BUILD_TOO_FAR_FROM_BUILD_POWER_SOURCE),
                of(Error.ActionResult.CantBuildOnDenseTerrain, CANT_BUILD_ON_DENSE_TERRAIN),
                of(Error.ActionResult.CantTrainTooFarFromTrainPowerSource, CANT_TRAIN_TOO_FAR_FROM_TRAIN_POWER_SOURCE),
                of(Error.ActionResult.CantLandLocationInvalid, CANT_LAND_LOCATION_INVALID),
                of(Error.ActionResult.CantSeeLandLocation, CANT_SEE_LAND_LOCATION),
                of(Error.ActionResult.CantLandTooCloseToCreepSource, CANT_LAND_TOO_CLOSE_TO_CREEP_SOURCE),
                of(Error.ActionResult.CantLandTooCloseToResources, CANT_LAND_TOO_CLOSE_TO_RESOURCES),
                of(Error.ActionResult.CantLandTooFarFromWater, CANT_LAND_TOO_FAR_FROM_WATER),
                of(Error.ActionResult.CantLandTooFarFromCreepSource, CANT_LAND_TOO_FAR_FROM_CREEP_SOURCE),
                of(Error.ActionResult.CantLandTooFarFromBuildPowerSource, CANT_LAND_TOO_FAR_FROM_BUILD_POWER_SOURCE),
                of(Error.ActionResult.CantLandTooFarFromTrainPowerSource, CANT_LAND_TOO_FAR_FROM_TRAIN_POWER_SOURCE),
                of(Error.ActionResult.CantLandOnDenseTerrain, CANT_LAND_ON_DENSE_TERRAIN),
                of(Error.ActionResult.AddOnTooFarFromBuilding, ADD_ON_TOO_FAR_FROM_BUILDING),
                of(Error.ActionResult.MustBuildRefineryFirst, MUST_BUILD_REFINERY_FIRST),
                of(Error.ActionResult.BuildingIsUnderConstruction, BUILDING_IS_UNDER_CONSTRUCTION),
                of(Error.ActionResult.CantFindDropOff, CANT_FIND_DROP_OFF),
                of(Error.ActionResult.CantLoadOtherPlayersUnits, CANT_LOAD_OTHER_PLAYERS_UNITS),
                of(Error.ActionResult.NotEnoughRoomToLoadUnit, NOT_ENOUGH_ROOM_TO_LOAD_UNIT),
                of(Error.ActionResult.CantUnloadUnitsThere, CANT_UNLOAD_UNITS_THERE),
                of(Error.ActionResult.CantWarpInUnitsThere, CANT_WARP_IN_UNITS_THERE),
                of(Error.ActionResult.CantLoadImmobileUnits, CANT_LOAD_IMMOBILE_UNITS),
                of(Error.ActionResult.CantRechargeImmobileUnits, CANT_RECHARGE_IMMOBILE_UNITS),
                of(Error.ActionResult.CantRechargeUnderConstructionUnits, CANT_RECHARGE_UNDER_CONSTRUCTION_UNITS),
                of(Error.ActionResult.CantLoadThatUnit, CANT_LOAD_THAT_UNIT),
                of(Error.ActionResult.NoCargoToUnload, NO_CARGO_TO_UNLOAD),
                of(Error.ActionResult.LoadAllNoTargetsFound, LOAD_ALL_NO_TARGETS_FOUND),
                of(Error.ActionResult.NotWhileOccupied, NOT_WHILE_OCCUPIED),
                of(Error.ActionResult.CantAttackWithoutAmmo, CANT_ATTACK_WITHOUT_AMMO),
                of(Error.ActionResult.CantHoldAnyMoreAmmo, CANT_HOLD_ANY_MORE_AMMO),
                of(Error.ActionResult.TechRequirementsNotMet, TECH_REQUIREMENTS_NOT_MET),
                of(Error.ActionResult.MustLockdownUnitFirst, MUST_LOCKDOWN_UNIT_FIRST),
                of(Error.ActionResult.MustTargetUnit, MUST_TARGET_UNIT),
                of(Error.ActionResult.MustTargetInventory, MUST_TARGET_INVENTORY),
                of(Error.ActionResult.MustTargetVisibleUnit, MUST_TARGET_VISIBLE_UNIT),
                of(Error.ActionResult.MustTargetVisibleLocation, MUST_TARGET_VISIBLE_LOCATION),
                of(Error.ActionResult.MustTargetWalkableLocation, MUST_TARGET_WALKABLE_LOCATION),
                of(Error.ActionResult.MustTargetPawnableUnit, MUST_TARGET_PAWNABLE_UNIT),
                of(Error.ActionResult.YouCantControlThatUnit, YOU_CANT_CONTROL_THAT_UNIT),
                of(Error.ActionResult.YouCantIssueCommandsToThatUnit, YOU_CANT_ISSUE_COMMANDS_TO_THAT_UNIT),
                of(Error.ActionResult.MustTargetResources, MUST_TARGET_RESOURCES),
                of(Error.ActionResult.RequiresHealTarget, REQUIRES_HEAL_TARGET),
                of(Error.ActionResult.RequiresRepairTarget, REQUIRES_REPAIR_TARGET),
                of(Error.ActionResult.NoItemsToDrop, NO_ITEMS_TO_DROP),
                of(Error.ActionResult.CantHoldAnyMoreItems, CANT_HOLD_ANY_MORE_ITEMS),
                of(Error.ActionResult.CantHoldThat, CANT_HOLD_THAT),
                of(Error.ActionResult.TargetHasNoInventory, TARGET_HAS_NO_INVENTORY),
                of(Error.ActionResult.CantDropThisItem, CANT_DROP_THIS_ITEM),
                of(Error.ActionResult.CantMoveThisItem, CANT_MOVE_THIS_ITEM),
                of(Error.ActionResult.CantPawnThisUnit, CANT_PAWN_THIS_UNIT),
                of(Error.ActionResult.MustTargetCaster, MUST_TARGET_CASTER),
                of(Error.ActionResult.CantTargetCaster, CANT_TARGET_CASTER),
                of(Error.ActionResult.MustTargetOuter, MUST_TARGET_OUTER),
                of(Error.ActionResult.CantTargetOuter, CANT_TARGET_OUTER),
                of(Error.ActionResult.MustTargetYourOwnUnits, MUST_TARGET_YOUR_OWN_UNITS),
                of(Error.ActionResult.CantTargetYourOwnUnits, CANT_TARGET_YOUR_OWN_UNITS),
                of(Error.ActionResult.MustTargetFriendlyUnits, MUST_TARGET_FRIENDLY_UNITS),
                of(Error.ActionResult.CantTargetFriendlyUnits, CANT_TARGET_FRIENDLY_UNITS),
                of(Error.ActionResult.MustTargetNeutralUnits, MUST_TARGET_NEUTRAL_UNITS),
                of(Error.ActionResult.CantTargetNeutralUnits, CANT_TARGET_NEUTRAL_UNITS),
                of(Error.ActionResult.MustTargetEnemyUnits, MUST_TARGET_ENEMY_UNITS),
                of(Error.ActionResult.CantTargetEnemyUnits, CANT_TARGET_ENEMY_UNITS),
                of(Error.ActionResult.MustTargetAirUnits, MUST_TARGET_AIR_UNITS),
                of(Error.ActionResult.CantTargetAirUnits, CANT_TARGET_AIR_UNITS),
                of(Error.ActionResult.MustTargetGroundUnits, MUST_TARGET_GROUND_UNITS),
                of(Error.ActionResult.CantTargetGroundUnits, CANT_TARGET_GROUND_UNITS),
                of(Error.ActionResult.MustTargetStructures, MUST_TARGET_STRUCTURES),
                of(Error.ActionResult.CantTargetStructures, CANT_TARGET_STRUCTURES),
                of(Error.ActionResult.MustTargetLightUnits, MUST_TARGET_LIGHT_UNITS),
                of(Error.ActionResult.CantTargetLightUnits, CANT_TARGET_LIGHT_UNITS),
                of(Error.ActionResult.MustTargetArmoredUnits, MUST_TARGET_ARMORED_UNITS),
                of(Error.ActionResult.CantTargetArmoredUnits, CANT_TARGET_ARMORED_UNITS),
                of(Error.ActionResult.MustTargetBiologicalUnits, MUST_TARGET_BIOLOGICAL_UNITS),
                of(Error.ActionResult.CantTargetBiologicalUnits, CANT_TARGET_BIOLOGICAL_UNITS),
                of(Error.ActionResult.MustTargetHeroicUnits, MUST_TARGET_HEROIC_UNITS),
                of(Error.ActionResult.CantTargetHeroicUnits, CANT_TARGET_HEROIC_UNITS),
                of(Error.ActionResult.MustTargetRoboticUnits, MUST_TARGET_ROBOTIC_UNITS),
                of(Error.ActionResult.CantTargetRoboticUnits, CANT_TARGET_ROBOTIC_UNITS),
                of(Error.ActionResult.MustTargetMechanicalUnits, MUST_TARGET_MECHANICAL_UNITS),
                of(Error.ActionResult.CantTargetMechanicalUnits, CANT_TARGET_MECHANICAL_UNITS),
                of(Error.ActionResult.MustTargetPsionicUnits, MUST_TARGET_PSIONIC_UNITS),
                of(Error.ActionResult.CantTargetPsionicUnits, CANT_TARGET_PSIONIC_UNITS),
                of(Error.ActionResult.MustTargetMassiveUnits, MUST_TARGET_MASSIVE_UNITS),
                of(Error.ActionResult.CantTargetMassiveUnits, CANT_TARGET_MASSIVE_UNITS),
                of(Error.ActionResult.MustTargetMissile, MUST_TARGET_MISSILE),
                of(Error.ActionResult.CantTargetMissile, CANT_TARGET_MISSILE),
                of(Error.ActionResult.MustTargetWorkerUnits, MUST_TARGET_WORKER_UNITS),
                of(Error.ActionResult.CantTargetWorkerUnits, CANT_TARGET_WORKER_UNITS),
                of(Error.ActionResult.MustTargetEnergyCapableUnits, MUST_TARGET_ENERGY_CAPABLE_UNITS),
                of(Error.ActionResult.CantTargetEnergyCapableUnits, CANT_TARGET_ENERGY_CAPABLE_UNITS),
                of(Error.ActionResult.MustTargetShieldCapableUnits, MUST_TARGET_SHIELD_CAPABLE_UNITS),
                of(Error.ActionResult.CantTargetShieldCapableUnits, CANT_TARGET_SHIELD_CAPABLE_UNITS),
                of(Error.ActionResult.MustTargetFlyers, MUST_TARGET_FLYERS),
                of(Error.ActionResult.CantTargetFlyers, CANT_TARGET_FLYERS),
                of(Error.ActionResult.MustTargetBuriedUnits, MUST_TARGET_BURIED_UNITS),
                of(Error.ActionResult.CantTargetBuriedUnits, CANT_TARGET_BURIED_UNITS),
                of(Error.ActionResult.MustTargetCloakedUnits, MUST_TARGET_CLOAKED_UNITS),
                of(Error.ActionResult.CantTargetCloakedUnits, CANT_TARGET_CLOAKED_UNITS),
                of(Error.ActionResult.MustTargetUnitsInAStasisField, MUST_TARGET_UNITS_IN_A_STASIS_FIELD),
                of(Error.ActionResult.CantTargetUnitsInAStasisField, CANT_TARGET_UNITS_IN_A_STASIS_FIELD),
                of(Error.ActionResult.MustTargetUnderConstructionUnits, MUST_TARGET_UNDER_CONSTRUCTION_UNITS),
                of(Error.ActionResult.CantTargetUnderConstructionUnits, CANT_TARGET_UNDER_CONSTRUCTION_UNITS),
                of(Error.ActionResult.MustTargetDeadUnits, MUST_TARGET_DEAD_UNITS),
                of(Error.ActionResult.CantTargetDeadUnits, CANT_TARGET_DEAD_UNITS),
                of(Error.ActionResult.MustTargetRevivableUnits, MUST_TARGET_REVIVABLE_UNITS),
                of(Error.ActionResult.CantTargetRevivableUnits, CANT_TARGET_REVIVABLE_UNITS),
                of(Error.ActionResult.MustTargetHiddenUnits, MUST_TARGET_HIDDEN_UNITS),
                of(Error.ActionResult.CantTargetHiddenUnits, CANT_TARGET_HIDDEN_UNITS),
                of(Error.ActionResult.CantRechargeOtherPlayersUnits, CANT_RECHARGE_OTHER_PLAYERS_UNITS),
                of(Error.ActionResult.MustTargetHallucinations, MUST_TARGET_HALLUCINATIONS),
                of(Error.ActionResult.CantTargetHallucinations, CANT_TARGET_HALLUCINATIONS),
                of(Error.ActionResult.MustTargetInvulnerableUnits, MUST_TARGET_INVULNERABLE_UNITS),
                of(Error.ActionResult.CantTargetInvulnerableUnits, CANT_TARGET_INVULNERABLE_UNITS),
                of(Error.ActionResult.MustTargetDetectedUnits, MUST_TARGET_DETECTED_UNITS),
                of(Error.ActionResult.CantTargetDetectedUnits, CANT_TARGET_DETECTED_UNITS),
                of(Error.ActionResult.CantTargetUnitWithEnergy, CANT_TARGET_UNIT_WITH_ENERGY),
                of(Error.ActionResult.CantTargetUnitWithShields, CANT_TARGET_UNIT_WITH_SHIELDS),
                of(Error.ActionResult.MustTargetUncommandableUnits, MUST_TARGET_UNCOMMANDABLE_UNITS),
                of(Error.ActionResult.CantTargetUncommandableUnits, CANT_TARGET_UNCOMMANDABLE_UNITS),
                of(Error.ActionResult.MustTargetPreventDefeatUnits, MUST_TARGET_PREVENT_DEFEAT_UNITS),
                of(Error.ActionResult.CantTargetPreventDefeatUnits, CANT_TARGET_PREVENT_DEFEAT_UNITS),
                of(Error.ActionResult.MustTargetPreventRevealUnits, MUST_TARGET_PREVENT_REVEAL_UNITS),
                of(Error.ActionResult.CantTargetPreventRevealUnits, CANT_TARGET_PREVENT_REVEAL_UNITS),
                of(Error.ActionResult.MustTargetPassiveUnits, MUST_TARGET_PASSIVE_UNITS),
                of(Error.ActionResult.CantTargetPassiveUnits, CANT_TARGET_PASSIVE_UNITS),
                of(Error.ActionResult.MustTargetStunnedUnits, MUST_TARGET_STUNNED_UNITS),
                of(Error.ActionResult.CantTargetStunnedUnits, CANT_TARGET_STUNNED_UNITS),
                of(Error.ActionResult.MustTargetSummonedUnits, MUST_TARGET_SUMMONED_UNITS),
                of(Error.ActionResult.CantTargetSummonedUnits, CANT_TARGET_SUMMONED_UNITS),
                of(Error.ActionResult.MustTargetUser1, MUST_TARGET_USER1),
                of(Error.ActionResult.CantTargetUser1, CANT_TARGET_USER1),
                of(Error.ActionResult.MustTargetUnstoppableUnits, MUST_TARGET_UNSTOPPABLE_UNITS),
                of(Error.ActionResult.CantTargetUnstoppableUnits, CANT_TARGET_UNSTOPPABLE_UNITS),
                of(Error.ActionResult.MustTargetResistantUnits, MUST_TARGET_RESISTANT_UNITS),
                of(Error.ActionResult.CantTargetResistantUnits, CANT_TARGET_RESISTANT_UNITS),
                of(Error.ActionResult.MustTargetDazedUnits, MUST_TARGET_DAZED_UNITS),
                of(Error.ActionResult.CantTargetDazedUnits, CANT_TARGET_DAZED_UNITS),
                of(Error.ActionResult.CantLockdown, CANT_LOCKDOWN),
                of(Error.ActionResult.CantMindControl, CANT_MIND_CONTROL),
                of(Error.ActionResult.MustTargetDestructibles, MUST_TARGET_DESTRUCTIBLES),
                of(Error.ActionResult.CantTargetDestructibles, CANT_TARGET_DESTRUCTIBLES),
                of(Error.ActionResult.MustTargetItems, MUST_TARGET_ITEMS),
                of(Error.ActionResult.CantTargetItems, CANT_TARGET_ITEMS),
                of(Error.ActionResult.NoCalldownAvailable, NO_CALLDOWN_AVAILABLE),
                of(Error.ActionResult.WaypointListFull, WAYPOINT_LIST_FULL),
                of(Error.ActionResult.MustTargetRace, MUST_TARGET_RACE),
                of(Error.ActionResult.CantTargetRace, CANT_TARGET_RACE),
                of(Error.ActionResult.MustTargetSimilarUnits, MUST_TARGET_SIMILAR_UNITS),
                of(Error.ActionResult.CantTargetSimilarUnits, CANT_TARGET_SIMILAR_UNITS),
                of(Error.ActionResult.CantFindEnoughTargets, CANT_FIND_ENOUGH_TARGETS),
                of(Error.ActionResult.AlreadySpawningLarva, ALREADY_SPAWNING_LARVA),
                of(Error.ActionResult.CantTargetExhaustedResources, CANT_TARGET_EXHAUSTED_RESOURCES),
                of(Error.ActionResult.CantUseMinimap, CANT_USE_MINIMAP),
                of(Error.ActionResult.CantUseInfoPanel, CANT_USE_INFO_PANEL),
                of(Error.ActionResult.OrderQueueIsFull, ORDER_QUEUE_IS_FULL),
                of(Error.ActionResult.CantHarvestThatResource, CANT_HARVEST_THAT_RESOURCE),
                of(Error.ActionResult.HarvestersNotRequired, HARVESTERS_NOT_REQUIRED),
                of(Error.ActionResult.AlreadyTargeted, ALREADY_TARGETED),
                of(Error.ActionResult.CantAttackWeaponsDisabled, CANT_ATTACK_WEAPONS_DISABLED),
                of(Error.ActionResult.CouldntReachTarget, COULDNT_REACH_TARGET),
                of(Error.ActionResult.TargetIsOutOfRange, TARGET_IS_OUT_OF_RANGE),
                of(Error.ActionResult.TargetIsTooClose, TARGET_IS_TOO_CLOSE),
                of(Error.ActionResult.TargetIsOutOfArc, TARGET_IS_OUT_OF_ARC),
                of(Error.ActionResult.CantFindTeleportLocation, CANT_FIND_TELEPORT_LOCATION),
                of(Error.ActionResult.InvalidItemClass, INVALID_ITEM_CLASS),
                of(Error.ActionResult.CantFindCancelOrder, CANT_FIND_CANCEL_ORDER));
    }

    @Test
    void throwsExceptionWhenSc2ApiActionResultIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionResult.from(nothing()))
                .withMessage("sc2api action result is required");
    }

}
