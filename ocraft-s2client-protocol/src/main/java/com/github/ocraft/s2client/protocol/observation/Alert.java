package com.github.ocraft.s2client.protocol.observation;

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

import SC2APIProtocol.Sc2Api;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public enum Alert {
    ALERT_ERROR,
    ADD_ON_COMPLETE,
    BUILDING_COMPLETE,
    BUILDING_UNDER_ATTACK,
    LARVA_HATCHED,
    MERGE_COMPLETE,
    MINERALS_EXHAUSTED,
    MORPH_COMPLETE,
    MOTHERSHIP_COMPLETE,
    MULE_EXPIRED,
    NUKE_COMPLETE,
    RESEARCH_COMPLETE,
    TRAIN_ERROR,
    TRAIN_UNIT_COMPLETE,
    TRAIN_WORKER_COMPLETE,
    TRANSFORMATION_COMPLETE,
    UNIT_UNDER_ATTACK,
    UPGRADE_COMPLETE,
    VESPENE_EXHAUSTED,
    WARP_IN_COMPLETE,
    NUCLEAR_LAUNCH_DETECTED,
    NYDUS_WORM_DETECTED;

    public static Alert from(Sc2Api.Alert sc2ApiAlert) {
        require("sc2api alert", sc2ApiAlert);
        switch (sc2ApiAlert) {
            case AlertError:
                return ALERT_ERROR;
            case AddOnComplete:
                return ADD_ON_COMPLETE;
            case BuildingComplete:
                return BUILDING_COMPLETE;
            case BuildingUnderAttack:
                return BUILDING_UNDER_ATTACK;
            case LarvaHatched:
                return LARVA_HATCHED;
            case MergeComplete:
                return MERGE_COMPLETE;
            case MineralsExhausted:
                return MINERALS_EXHAUSTED;
            case MorphComplete:
                return MORPH_COMPLETE;
            case MothershipComplete:
                return MOTHERSHIP_COMPLETE;
            case MULEExpired:
                return MULE_EXPIRED;
            case NukeComplete:
                return NUKE_COMPLETE;
            case ResearchComplete:
                return RESEARCH_COMPLETE;
            case TrainError:
                return TRAIN_ERROR;
            case TrainUnitComplete:
                return TRAIN_UNIT_COMPLETE;
            case TrainWorkerComplete:
                return TRAIN_WORKER_COMPLETE;
            case TransformationComplete:
                return TRANSFORMATION_COMPLETE;
            case UnitUnderAttack:
                return UNIT_UNDER_ATTACK;
            case UpgradeComplete:
                return UPGRADE_COMPLETE;
            case VespeneExhausted:
                return VESPENE_EXHAUSTED;
            case WarpInComplete:
                return WARP_IN_COMPLETE;
            case NuclearLaunchDetected:
                return NUCLEAR_LAUNCH_DETECTED;
            case NydusWormDetected:
                return NYDUS_WORM_DETECTED;
            default:
                throw new AssertionError("unknown alert: " + sc2ApiAlert);
        }
    }
}
   