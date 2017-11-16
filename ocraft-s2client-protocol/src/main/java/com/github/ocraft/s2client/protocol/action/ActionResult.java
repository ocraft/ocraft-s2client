/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ocraft Project
 *
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
 */
package com.github.ocraft.s2client.protocol.action;

import SC2APIProtocol.Error;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum ActionResult {
    SUCCESS,
    NOT_SUPPORTED,
    ERROR,
    CANT_QUEUE_THAT_ORDER,
    RETRY,
    COOLDOWN,
    QUEUE_IS_FULL,
    RALLY_QUEUE_IS_FULL,
    NOT_ENOUGH_MINERALS,
    NOT_ENOUGH_VESPENE,
    NOT_ENOUGH_TERRAZINE,
    NOT_ENOUGH_CUSTOM,
    NOT_ENOUGH_FOOD,
    FOOD_USAGE_IMPOSSIBLE,
    NOT_ENOUGH_LIFE,
    NOT_ENOUGH_SHIELDS,
    NOT_ENOUGH_ENERGY,
    LIFE_SUPPRESSED,
    SHIELDS_SUPPRESSED,
    ENERGY_SUPPRESSED,
    NOT_ENOUGH_CHARGES,
    CANT_ADD_MORE_CHARGES,
    TOO_MUCH_MINERALS,
    TOO_MUCH_VESPENE,
    TOO_MUCH_TERRAZINE,
    TOO_MUCH_CUSTOM,
    TOO_MUCH_FOOD,
    TOO_MUCH_LIFE,
    TOO_MUCH_SHIELDS,
    TOO_MUCH_ENERGY,
    MUST_TARGET_UNIT_WITH_LIFE,
    MUST_TARGET_UNIT_WITH_SHIELDS,
    MUST_TARGET_UNIT_WITH_ENERGY,
    CANT_TRADE,
    CANT_SPEND,
    CANT_TARGET_THAT_UNIT,
    COULDNT_ALLOCATE_UNIT,
    UNIT_CANT_MOVE,
    TRANSPORT_IS_HOLDING_POSITION,
    BUILD_TECH_REQUIREMENTS_NOT_MET,
    CANT_FIND_PLACEMENT_LOCATION,
    CANT_BUILD_ON_THAT,
    CANT_BUILD_TOO_CLOSE_TO_DROP_OFF,
    CANT_BUILD_LOCATION_INVALID,
    CANT_SEE_BUILD_LOCATION,
    CANT_BUILD_TOO_CLOSE_TO_CREEP_SOURCE,
    CANT_BUILD_TOO_CLOSE_TO_RESOURCES,
    CANT_BUILD_TOO_FAR_FROM_WATER,
    CANT_BUILD_TOO_FAR_FROM_CREEP_SOURCE,
    CANT_BUILD_TOO_FAR_FROM_BUILD_POWER_SOURCE,
    CANT_BUILD_ON_DENSE_TERRAIN,
    CANT_TRAIN_TOO_FAR_FROM_TRAIN_POWER_SOURCE,
    CANT_LAND_LOCATION_INVALID,
    CANT_SEE_LAND_LOCATION,
    CANT_LAND_TOO_CLOSE_TO_CREEP_SOURCE,
    CANT_LAND_TOO_CLOSE_TO_RESOURCES,
    CANT_LAND_TOO_FAR_FROM_WATER,
    CANT_LAND_TOO_FAR_FROM_CREEP_SOURCE,
    CANT_LAND_TOO_FAR_FROM_BUILD_POWER_SOURCE,
    CANT_LAND_TOO_FAR_FROM_TRAIN_POWER_SOURCE,
    CANT_LAND_ON_DENSE_TERRAIN,
    ADD_ON_TOO_FAR_FROM_BUILDING,
    MUST_BUILD_REFINERY_FIRST,
    BUILDING_IS_UNDER_CONSTRUCTION,
    CANT_FIND_DROP_OFF,
    CANT_LOAD_OTHER_PLAYERS_UNITS,
    NOT_ENOUGH_ROOM_TO_LOAD_UNIT,
    CANT_UNLOAD_UNITS_THERE,
    CANT_WARP_IN_UNITS_THERE,
    CANT_LOAD_IMMOBILE_UNITS,
    CANT_RECHARGE_IMMOBILE_UNITS,
    CANT_RECHARGE_UNDER_CONSTRUCTION_UNITS,
    CANT_LOAD_THAT_UNIT,
    NO_CARGO_TO_UNLOAD,
    LOAD_ALL_NO_TARGETS_FOUND,
    NOT_WHILE_OCCUPIED,
    CANT_ATTACK_WITHOUT_AMMO,
    CANT_HOLD_ANY_MORE_AMMO,
    TECH_REQUIREMENTS_NOT_MET,
    MUST_LOCKDOWN_UNIT_FIRST,
    MUST_TARGET_UNIT,
    MUST_TARGET_INVENTORY,
    MUST_TARGET_VISIBLE_UNIT,
    MUST_TARGET_VISIBLE_LOCATION,
    MUST_TARGET_WALKABLE_LOCATION,
    MUST_TARGET_PAWNABLE_UNIT,
    YOU_CANT_CONTROL_THAT_UNIT,
    YOU_CANT_ISSUE_COMMANDS_TO_THAT_UNIT,
    MUST_TARGET_RESOURCES,
    REQUIRES_HEAL_TARGET,
    REQUIRES_REPAIR_TARGET,
    NO_ITEMS_TO_DROP,
    CANT_HOLD_ANY_MORE_ITEMS,
    CANT_HOLD_THAT,
    TARGET_HAS_NO_INVENTORY,
    CANT_DROP_THIS_ITEM,
    CANT_MOVE_THIS_ITEM,
    CANT_PAWN_THIS_UNIT,
    MUST_TARGET_CASTER,
    CANT_TARGET_CASTER,
    MUST_TARGET_OUTER,
    CANT_TARGET_OUTER,
    MUST_TARGET_YOUR_OWN_UNITS,
    CANT_TARGET_YOUR_OWN_UNITS,
    MUST_TARGET_FRIENDLY_UNITS,
    CANT_TARGET_FRIENDLY_UNITS,
    MUST_TARGET_NEUTRAL_UNITS,
    CANT_TARGET_NEUTRAL_UNITS,
    MUST_TARGET_ENEMY_UNITS,
    CANT_TARGET_ENEMY_UNITS,
    MUST_TARGET_AIR_UNITS,
    CANT_TARGET_AIR_UNITS,
    MUST_TARGET_GROUND_UNITS,
    CANT_TARGET_GROUND_UNITS,
    MUST_TARGET_STRUCTURES,
    CANT_TARGET_STRUCTURES,
    MUST_TARGET_LIGHT_UNITS,
    CANT_TARGET_LIGHT_UNITS,
    MUST_TARGET_ARMORED_UNITS,
    CANT_TARGET_ARMORED_UNITS,
    MUST_TARGET_BIOLOGICAL_UNITS,
    CANT_TARGET_BIOLOGICAL_UNITS,
    MUST_TARGET_HEROIC_UNITS,
    CANT_TARGET_HEROIC_UNITS,
    MUST_TARGET_ROBOTIC_UNITS,
    CANT_TARGET_ROBOTIC_UNITS,
    MUST_TARGET_MECHANICAL_UNITS,
    CANT_TARGET_MECHANICAL_UNITS,
    MUST_TARGET_PSIONIC_UNITS,
    CANT_TARGET_PSIONIC_UNITS,
    MUST_TARGET_MASSIVE_UNITS,
    CANT_TARGET_MASSIVE_UNITS,
    MUST_TARGET_MISSILE,
    CANT_TARGET_MISSILE,
    MUST_TARGET_WORKER_UNITS,
    CANT_TARGET_WORKER_UNITS,
    MUST_TARGET_ENERGY_CAPABLE_UNITS,
    CANT_TARGET_ENERGY_CAPABLE_UNITS,
    MUST_TARGET_SHIELD_CAPABLE_UNITS,
    CANT_TARGET_SHIELD_CAPABLE_UNITS,
    MUST_TARGET_FLYERS,
    CANT_TARGET_FLYERS,
    MUST_TARGET_BURIED_UNITS,
    CANT_TARGET_BURIED_UNITS,
    MUST_TARGET_CLOAKED_UNITS,
    CANT_TARGET_CLOAKED_UNITS,
    MUST_TARGET_UNITS_IN_A_STASIS_FIELD,
    CANT_TARGET_UNITS_IN_A_STASIS_FIELD,
    MUST_TARGET_UNDER_CONSTRUCTION_UNITS,
    CANT_TARGET_UNDER_CONSTRUCTION_UNITS,
    MUST_TARGET_DEAD_UNITS,
    CANT_TARGET_DEAD_UNITS,
    MUST_TARGET_REVIVABLE_UNITS,
    CANT_TARGET_REVIVABLE_UNITS,
    MUST_TARGET_HIDDEN_UNITS,
    CANT_TARGET_HIDDEN_UNITS,
    CANT_RECHARGE_OTHER_PLAYERS_UNITS,
    MUST_TARGET_HALLUCINATIONS,
    CANT_TARGET_HALLUCINATIONS,
    MUST_TARGET_INVULNERABLE_UNITS,
    CANT_TARGET_INVULNERABLE_UNITS,
    MUST_TARGET_DETECTED_UNITS,
    CANT_TARGET_DETECTED_UNITS,
    CANT_TARGET_UNIT_WITH_ENERGY,
    CANT_TARGET_UNIT_WITH_SHIELDS,
    MUST_TARGET_UNCOMMANDABLE_UNITS,
    CANT_TARGET_UNCOMMANDABLE_UNITS,
    MUST_TARGET_PREVENT_DEFEAT_UNITS,
    CANT_TARGET_PREVENT_DEFEAT_UNITS,
    MUST_TARGET_PREVENT_REVEAL_UNITS,
    CANT_TARGET_PREVENT_REVEAL_UNITS,
    MUST_TARGET_PASSIVE_UNITS,
    CANT_TARGET_PASSIVE_UNITS,
    MUST_TARGET_STUNNED_UNITS,
    CANT_TARGET_STUNNED_UNITS,
    MUST_TARGET_SUMMONED_UNITS,
    CANT_TARGET_SUMMONED_UNITS,
    MUST_TARGET_USER1,
    CANT_TARGET_USER1,
    MUST_TARGET_UNSTOPPABLE_UNITS,
    CANT_TARGET_UNSTOPPABLE_UNITS,
    MUST_TARGET_RESISTANT_UNITS,
    CANT_TARGET_RESISTANT_UNITS,
    MUST_TARGET_DAZED_UNITS,
    CANT_TARGET_DAZED_UNITS,
    CANT_LOCKDOWN,
    CANT_MIND_CONTROL,
    MUST_TARGET_DESTRUCTIBLES,
    CANT_TARGET_DESTRUCTIBLES,
    MUST_TARGET_ITEMS,
    CANT_TARGET_ITEMS,
    NO_CALLDOWN_AVAILABLE,
    WAYPOINT_LIST_FULL,
    MUST_TARGET_RACE,
    CANT_TARGET_RACE,
    MUST_TARGET_SIMILAR_UNITS,
    CANT_TARGET_SIMILAR_UNITS,
    CANT_FIND_ENOUGH_TARGETS,
    ALREADY_SPAWNING_LARVA,
    CANT_TARGET_EXHAUSTED_RESOURCES,
    CANT_USE_MINIMAP,
    CANT_USE_INFO_PANEL,
    ORDER_QUEUE_IS_FULL,
    CANT_HARVEST_THAT_RESOURCE,
    HARVESTERS_NOT_REQUIRED,
    ALREADY_TARGETED,
    CANT_ATTACK_WEAPONS_DISABLED,
    COULDNT_REACH_TARGET,
    TARGET_IS_OUT_OF_RANGE,
    TARGET_IS_TOO_CLOSE,
    TARGET_IS_OUT_OF_ARC,
    CANT_FIND_TELEPORT_LOCATION,
    INVALID_ITEM_CLASS,
    CANT_FIND_CANCEL_ORDER;

    public static ActionResult from(Error.ActionResult sc2ApiActionResult) {
        require("sc2api action result", sc2ApiActionResult);
        switch (sc2ApiActionResult) {
            case Success:
                return SUCCESS;
            case NotSupported:
                return NOT_SUPPORTED;
            case Error:
                return ERROR;
            case CantQueueThatOrder:
                return CANT_QUEUE_THAT_ORDER;
            case Retry:
                return RETRY;
            case Cooldown:
                return COOLDOWN;
            case QueueIsFull:
                return QUEUE_IS_FULL;
            case RallyQueueIsFull:
                return RALLY_QUEUE_IS_FULL;
            case NotEnoughMinerals:
                return NOT_ENOUGH_MINERALS;
            case NotEnoughVespene:
                return NOT_ENOUGH_VESPENE;
            case NotEnoughTerrazine:
                return NOT_ENOUGH_TERRAZINE;
            case NotEnoughCustom:
                return NOT_ENOUGH_CUSTOM;
            case NotEnoughFood:
                return NOT_ENOUGH_FOOD;
            case FoodUsageImpossible:
                return FOOD_USAGE_IMPOSSIBLE;
            case NotEnoughLife:
                return NOT_ENOUGH_LIFE;
            case NotEnoughShields:
                return NOT_ENOUGH_SHIELDS;
            case NotEnoughEnergy:
                return NOT_ENOUGH_ENERGY;
            case LifeSuppressed:
                return LIFE_SUPPRESSED;
            case ShieldsSuppressed:
                return SHIELDS_SUPPRESSED;
            case EnergySuppressed:
                return ENERGY_SUPPRESSED;
            case NotEnoughCharges:
                return NOT_ENOUGH_CHARGES;
            case CantAddMoreCharges:
                return CANT_ADD_MORE_CHARGES;
            case TooMuchMinerals:
                return TOO_MUCH_MINERALS;
            case TooMuchVespene:
                return TOO_MUCH_VESPENE;
            case TooMuchTerrazine:
                return TOO_MUCH_TERRAZINE;
            case TooMuchCustom:
                return TOO_MUCH_CUSTOM;
            case TooMuchFood:
                return TOO_MUCH_FOOD;
            case TooMuchLife:
                return TOO_MUCH_LIFE;
            case TooMuchShields:
                return TOO_MUCH_SHIELDS;
            case TooMuchEnergy:
                return TOO_MUCH_ENERGY;
            case MustTargetUnitWithLife:
                return MUST_TARGET_UNIT_WITH_LIFE;
            case MustTargetUnitWithShields:
                return MUST_TARGET_UNIT_WITH_SHIELDS;
            case MustTargetUnitWithEnergy:
                return MUST_TARGET_UNIT_WITH_ENERGY;
            case CantTrade:
                return CANT_TRADE;
            case CantSpend:
                return CANT_SPEND;
            case CantTargetThatUnit:
                return CANT_TARGET_THAT_UNIT;
            case CouldntAllocateUnit:
                return COULDNT_ALLOCATE_UNIT;
            case UnitCantMove:
                return UNIT_CANT_MOVE;
            case TransportIsHoldingPosition:
                return TRANSPORT_IS_HOLDING_POSITION;
            case BuildTechRequirementsNotMet:
                return BUILD_TECH_REQUIREMENTS_NOT_MET;
            case CantFindPlacementLocation:
                return CANT_FIND_PLACEMENT_LOCATION;
            case CantBuildOnThat:
                return CANT_BUILD_ON_THAT;
            case CantBuildTooCloseToDropOff:
                return CANT_BUILD_TOO_CLOSE_TO_DROP_OFF;
            case CantBuildLocationInvalid:
                return CANT_BUILD_LOCATION_INVALID;
            case CantSeeBuildLocation:
                return CANT_SEE_BUILD_LOCATION;
            case CantBuildTooCloseToCreepSource:
                return CANT_BUILD_TOO_CLOSE_TO_CREEP_SOURCE;
            case CantBuildTooCloseToResources:
                return CANT_BUILD_TOO_CLOSE_TO_RESOURCES;
            case CantBuildTooFarFromWater:
                return CANT_BUILD_TOO_FAR_FROM_WATER;
            case CantBuildTooFarFromCreepSource:
                return CANT_BUILD_TOO_FAR_FROM_CREEP_SOURCE;
            case CantBuildTooFarFromBuildPowerSource:
                return CANT_BUILD_TOO_FAR_FROM_BUILD_POWER_SOURCE;
            case CantBuildOnDenseTerrain:
                return CANT_BUILD_ON_DENSE_TERRAIN;
            case CantTrainTooFarFromTrainPowerSource:
                return CANT_TRAIN_TOO_FAR_FROM_TRAIN_POWER_SOURCE;
            case CantLandLocationInvalid:
                return CANT_LAND_LOCATION_INVALID;
            case CantSeeLandLocation:
                return CANT_SEE_LAND_LOCATION;
            case CantLandTooCloseToCreepSource:
                return CANT_LAND_TOO_CLOSE_TO_CREEP_SOURCE;
            case CantLandTooCloseToResources:
                return CANT_LAND_TOO_CLOSE_TO_RESOURCES;
            case CantLandTooFarFromWater:
                return CANT_LAND_TOO_FAR_FROM_WATER;
            case CantLandTooFarFromCreepSource:
                return CANT_LAND_TOO_FAR_FROM_CREEP_SOURCE;
            case CantLandTooFarFromBuildPowerSource:
                return CANT_LAND_TOO_FAR_FROM_BUILD_POWER_SOURCE;
            case CantLandTooFarFromTrainPowerSource:
                return CANT_LAND_TOO_FAR_FROM_TRAIN_POWER_SOURCE;
            case CantLandOnDenseTerrain:
                return CANT_LAND_ON_DENSE_TERRAIN;
            case AddOnTooFarFromBuilding:
                return ADD_ON_TOO_FAR_FROM_BUILDING;
            case MustBuildRefineryFirst:
                return MUST_BUILD_REFINERY_FIRST;
            case BuildingIsUnderConstruction:
                return BUILDING_IS_UNDER_CONSTRUCTION;
            case CantFindDropOff:
                return CANT_FIND_DROP_OFF;
            case CantLoadOtherPlayersUnits:
                return CANT_LOAD_OTHER_PLAYERS_UNITS;
            case NotEnoughRoomToLoadUnit:
                return NOT_ENOUGH_ROOM_TO_LOAD_UNIT;
            case CantUnloadUnitsThere:
                return CANT_UNLOAD_UNITS_THERE;
            case CantWarpInUnitsThere:
                return CANT_WARP_IN_UNITS_THERE;
            case CantLoadImmobileUnits:
                return CANT_LOAD_IMMOBILE_UNITS;
            case CantRechargeImmobileUnits:
                return CANT_RECHARGE_IMMOBILE_UNITS;
            case CantRechargeUnderConstructionUnits:
                return CANT_RECHARGE_UNDER_CONSTRUCTION_UNITS;
            case CantLoadThatUnit:
                return CANT_LOAD_THAT_UNIT;
            case NoCargoToUnload:
                return NO_CARGO_TO_UNLOAD;
            case LoadAllNoTargetsFound:
                return LOAD_ALL_NO_TARGETS_FOUND;
            case NotWhileOccupied:
                return NOT_WHILE_OCCUPIED;
            case CantAttackWithoutAmmo:
                return CANT_ATTACK_WITHOUT_AMMO;
            case CantHoldAnyMoreAmmo:
                return CANT_HOLD_ANY_MORE_AMMO;
            case TechRequirementsNotMet:
                return TECH_REQUIREMENTS_NOT_MET;
            case MustLockdownUnitFirst:
                return MUST_LOCKDOWN_UNIT_FIRST;
            case MustTargetUnit:
                return MUST_TARGET_UNIT;
            case MustTargetInventory:
                return MUST_TARGET_INVENTORY;
            case MustTargetVisibleUnit:
                return MUST_TARGET_VISIBLE_UNIT;
            case MustTargetVisibleLocation:
                return MUST_TARGET_VISIBLE_LOCATION;
            case MustTargetWalkableLocation:
                return MUST_TARGET_WALKABLE_LOCATION;
            case MustTargetPawnableUnit:
                return MUST_TARGET_PAWNABLE_UNIT;
            case YouCantControlThatUnit:
                return YOU_CANT_CONTROL_THAT_UNIT;
            case YouCantIssueCommandsToThatUnit:
                return YOU_CANT_ISSUE_COMMANDS_TO_THAT_UNIT;
            case MustTargetResources:
                return MUST_TARGET_RESOURCES;
            case RequiresHealTarget:
                return REQUIRES_HEAL_TARGET;
            case RequiresRepairTarget:
                return REQUIRES_REPAIR_TARGET;
            case NoItemsToDrop:
                return NO_ITEMS_TO_DROP;
            case CantHoldAnyMoreItems:
                return CANT_HOLD_ANY_MORE_ITEMS;
            case CantHoldThat:
                return CANT_HOLD_THAT;
            case TargetHasNoInventory:
                return TARGET_HAS_NO_INVENTORY;
            case CantDropThisItem:
                return CANT_DROP_THIS_ITEM;
            case CantMoveThisItem:
                return CANT_MOVE_THIS_ITEM;
            case CantPawnThisUnit:
                return CANT_PAWN_THIS_UNIT;
            case MustTargetCaster:
                return MUST_TARGET_CASTER;
            case CantTargetCaster:
                return CANT_TARGET_CASTER;
            case MustTargetOuter:
                return MUST_TARGET_OUTER;
            case CantTargetOuter:
                return CANT_TARGET_OUTER;
            case MustTargetYourOwnUnits:
                return MUST_TARGET_YOUR_OWN_UNITS;
            case CantTargetYourOwnUnits:
                return CANT_TARGET_YOUR_OWN_UNITS;
            case MustTargetFriendlyUnits:
                return MUST_TARGET_FRIENDLY_UNITS;
            case CantTargetFriendlyUnits:
                return CANT_TARGET_FRIENDLY_UNITS;
            case MustTargetNeutralUnits:
                return MUST_TARGET_NEUTRAL_UNITS;
            case CantTargetNeutralUnits:
                return CANT_TARGET_NEUTRAL_UNITS;
            case MustTargetEnemyUnits:
                return MUST_TARGET_ENEMY_UNITS;
            case CantTargetEnemyUnits:
                return CANT_TARGET_ENEMY_UNITS;
            case MustTargetAirUnits:
                return MUST_TARGET_AIR_UNITS;
            case CantTargetAirUnits:
                return CANT_TARGET_AIR_UNITS;
            case MustTargetGroundUnits:
                return MUST_TARGET_GROUND_UNITS;
            case CantTargetGroundUnits:
                return CANT_TARGET_GROUND_UNITS;
            case MustTargetStructures:
                return MUST_TARGET_STRUCTURES;
            case CantTargetStructures:
                return CANT_TARGET_STRUCTURES;
            case MustTargetLightUnits:
                return MUST_TARGET_LIGHT_UNITS;
            case CantTargetLightUnits:
                return CANT_TARGET_LIGHT_UNITS;
            case MustTargetArmoredUnits:
                return MUST_TARGET_ARMORED_UNITS;
            case CantTargetArmoredUnits:
                return CANT_TARGET_ARMORED_UNITS;
            case MustTargetBiologicalUnits:
                return MUST_TARGET_BIOLOGICAL_UNITS;
            case CantTargetBiologicalUnits:
                return CANT_TARGET_BIOLOGICAL_UNITS;
            case MustTargetHeroicUnits:
                return MUST_TARGET_HEROIC_UNITS;
            case CantTargetHeroicUnits:
                return CANT_TARGET_HEROIC_UNITS;
            case MustTargetRoboticUnits:
                return MUST_TARGET_ROBOTIC_UNITS;
            case CantTargetRoboticUnits:
                return CANT_TARGET_ROBOTIC_UNITS;
            case MustTargetMechanicalUnits:
                return MUST_TARGET_MECHANICAL_UNITS;
            case CantTargetMechanicalUnits:
                return CANT_TARGET_MECHANICAL_UNITS;
            case MustTargetPsionicUnits:
                return MUST_TARGET_PSIONIC_UNITS;
            case CantTargetPsionicUnits:
                return CANT_TARGET_PSIONIC_UNITS;
            case MustTargetMassiveUnits:
                return MUST_TARGET_MASSIVE_UNITS;
            case CantTargetMassiveUnits:
                return CANT_TARGET_MASSIVE_UNITS;
            case MustTargetMissile:
                return MUST_TARGET_MISSILE;
            case CantTargetMissile:
                return CANT_TARGET_MISSILE;
            case MustTargetWorkerUnits:
                return MUST_TARGET_WORKER_UNITS;
            case CantTargetWorkerUnits:
                return CANT_TARGET_WORKER_UNITS;
            case MustTargetEnergyCapableUnits:
                return MUST_TARGET_ENERGY_CAPABLE_UNITS;
            case CantTargetEnergyCapableUnits:
                return CANT_TARGET_ENERGY_CAPABLE_UNITS;
            case MustTargetShieldCapableUnits:
                return MUST_TARGET_SHIELD_CAPABLE_UNITS;
            case CantTargetShieldCapableUnits:
                return CANT_TARGET_SHIELD_CAPABLE_UNITS;
            case MustTargetFlyers:
                return MUST_TARGET_FLYERS;
            case CantTargetFlyers:
                return CANT_TARGET_FLYERS;
            case MustTargetBuriedUnits:
                return MUST_TARGET_BURIED_UNITS;
            case CantTargetBuriedUnits:
                return CANT_TARGET_BURIED_UNITS;
            case MustTargetCloakedUnits:
                return MUST_TARGET_CLOAKED_UNITS;
            case CantTargetCloakedUnits:
                return CANT_TARGET_CLOAKED_UNITS;
            case MustTargetUnitsInAStasisField:
                return MUST_TARGET_UNITS_IN_A_STASIS_FIELD;
            case CantTargetUnitsInAStasisField:
                return CANT_TARGET_UNITS_IN_A_STASIS_FIELD;
            case MustTargetUnderConstructionUnits:
                return MUST_TARGET_UNDER_CONSTRUCTION_UNITS;
            case CantTargetUnderConstructionUnits:
                return CANT_TARGET_UNDER_CONSTRUCTION_UNITS;
            case MustTargetDeadUnits:
                return MUST_TARGET_DEAD_UNITS;
            case CantTargetDeadUnits:
                return CANT_TARGET_DEAD_UNITS;
            case MustTargetRevivableUnits:
                return MUST_TARGET_REVIVABLE_UNITS;
            case CantTargetRevivableUnits:
                return CANT_TARGET_REVIVABLE_UNITS;
            case MustTargetHiddenUnits:
                return MUST_TARGET_HIDDEN_UNITS;
            case CantTargetHiddenUnits:
                return CANT_TARGET_HIDDEN_UNITS;
            case CantRechargeOtherPlayersUnits:
                return CANT_RECHARGE_OTHER_PLAYERS_UNITS;
            case MustTargetHallucinations:
                return MUST_TARGET_HALLUCINATIONS;
            case CantTargetHallucinations:
                return CANT_TARGET_HALLUCINATIONS;
            case MustTargetInvulnerableUnits:
                return MUST_TARGET_INVULNERABLE_UNITS;
            case CantTargetInvulnerableUnits:
                return CANT_TARGET_INVULNERABLE_UNITS;
            case MustTargetDetectedUnits:
                return MUST_TARGET_DETECTED_UNITS;
            case CantTargetDetectedUnits:
                return CANT_TARGET_DETECTED_UNITS;
            case CantTargetUnitWithEnergy:
                return CANT_TARGET_UNIT_WITH_ENERGY;
            case CantTargetUnitWithShields:
                return CANT_TARGET_UNIT_WITH_SHIELDS;
            case MustTargetUncommandableUnits:
                return MUST_TARGET_UNCOMMANDABLE_UNITS;
            case CantTargetUncommandableUnits:
                return CANT_TARGET_UNCOMMANDABLE_UNITS;
            case MustTargetPreventDefeatUnits:
                return MUST_TARGET_PREVENT_DEFEAT_UNITS;
            case CantTargetPreventDefeatUnits:
                return CANT_TARGET_PREVENT_DEFEAT_UNITS;
            case MustTargetPreventRevealUnits:
                return MUST_TARGET_PREVENT_REVEAL_UNITS;
            case CantTargetPreventRevealUnits:
                return CANT_TARGET_PREVENT_REVEAL_UNITS;
            case MustTargetPassiveUnits:
                return MUST_TARGET_PASSIVE_UNITS;
            case CantTargetPassiveUnits:
                return CANT_TARGET_PASSIVE_UNITS;
            case MustTargetStunnedUnits:
                return MUST_TARGET_STUNNED_UNITS;
            case CantTargetStunnedUnits:
                return CANT_TARGET_STUNNED_UNITS;
            case MustTargetSummonedUnits:
                return MUST_TARGET_SUMMONED_UNITS;
            case CantTargetSummonedUnits:
                return CANT_TARGET_SUMMONED_UNITS;
            case MustTargetUser1:
                return MUST_TARGET_USER1;
            case CantTargetUser1:
                return CANT_TARGET_USER1;
            case MustTargetUnstoppableUnits:
                return MUST_TARGET_UNSTOPPABLE_UNITS;
            case CantTargetUnstoppableUnits:
                return CANT_TARGET_UNSTOPPABLE_UNITS;
            case MustTargetResistantUnits:
                return MUST_TARGET_RESISTANT_UNITS;
            case CantTargetResistantUnits:
                return CANT_TARGET_RESISTANT_UNITS;
            case MustTargetDazedUnits:
                return MUST_TARGET_DAZED_UNITS;
            case CantTargetDazedUnits:
                return CANT_TARGET_DAZED_UNITS;
            case CantLockdown:
                return CANT_LOCKDOWN;
            case CantMindControl:
                return CANT_MIND_CONTROL;
            case MustTargetDestructibles:
                return MUST_TARGET_DESTRUCTIBLES;
            case CantTargetDestructibles:
                return CANT_TARGET_DESTRUCTIBLES;
            case MustTargetItems:
                return MUST_TARGET_ITEMS;
            case CantTargetItems:
                return CANT_TARGET_ITEMS;
            case NoCalldownAvailable:
                return NO_CALLDOWN_AVAILABLE;
            case WaypointListFull:
                return WAYPOINT_LIST_FULL;
            case MustTargetRace:
                return MUST_TARGET_RACE;
            case CantTargetRace:
                return CANT_TARGET_RACE;
            case MustTargetSimilarUnits:
                return MUST_TARGET_SIMILAR_UNITS;
            case CantTargetSimilarUnits:
                return CANT_TARGET_SIMILAR_UNITS;
            case CantFindEnoughTargets:
                return CANT_FIND_ENOUGH_TARGETS;
            case AlreadySpawningLarva:
                return ALREADY_SPAWNING_LARVA;
            case CantTargetExhaustedResources:
                return CANT_TARGET_EXHAUSTED_RESOURCES;
            case CantUseMinimap:
                return CANT_USE_MINIMAP;
            case CantUseInfoPanel:
                return CANT_USE_INFO_PANEL;
            case OrderQueueIsFull:
                return ORDER_QUEUE_IS_FULL;
            case CantHarvestThatResource:
                return CANT_HARVEST_THAT_RESOURCE;
            case HarvestersNotRequired:
                return HARVESTERS_NOT_REQUIRED;
            case AlreadyTargeted:
                return ALREADY_TARGETED;
            case CantAttackWeaponsDisabled:
                return CANT_ATTACK_WEAPONS_DISABLED;
            case CouldntReachTarget:
                return COULDNT_REACH_TARGET;
            case TargetIsOutOfRange:
                return TARGET_IS_OUT_OF_RANGE;
            case TargetIsTooClose:
                return TARGET_IS_TOO_CLOSE;
            case TargetIsOutOfArc:
                return TARGET_IS_OUT_OF_ARC;
            case CantFindTeleportLocation:
                return CANT_FIND_TELEPORT_LOCATION;
            case InvalidItemClass:
                return INVALID_ITEM_CLASS;
            case CantFindCancelOrder:
                return CANT_FIND_CANCEL_ORDER;
            default:
                throw new AssertionError("unknown action result error: " + sc2ApiActionResult);
        }
    }
}