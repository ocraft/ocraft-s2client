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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class AlertTest {
    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "alertMappings")
    void mapsSc2ApiAlert(Sc2Api.Alert sc2ApiAlert, Alert expectedAlert) {
        assertThat(Alert.from(sc2ApiAlert)).isEqualTo(expectedAlert);
    }

    private static Stream<Arguments> alertMappings() {
        return Stream.of(
                of(Sc2Api.Alert.AlertError, Alert.ALERT_ERROR),
                of(Sc2Api.Alert.AddOnComplete, Alert.ADD_ON_COMPLETE),
                of(Sc2Api.Alert.BuildingComplete, Alert.BUILDING_COMPLETE),
                of(Sc2Api.Alert.BuildingUnderAttack, Alert.BUILDING_UNDER_ATTACK),
                of(Sc2Api.Alert.LarvaHatched, Alert.LARVA_HATCHED),
                of(Sc2Api.Alert.MergeComplete, Alert.MERGE_COMPLETE),
                of(Sc2Api.Alert.MineralsExhausted, Alert.MINERALS_EXHAUSTED),
                of(Sc2Api.Alert.MorphComplete, Alert.MORPH_COMPLETE),
                of(Sc2Api.Alert.MothershipComplete, Alert.MOTHERSHIP_COMPLETE),
                of(Sc2Api.Alert.MULEExpired, Alert.MULE_EXPIRED),
                of(Sc2Api.Alert.NukeComplete, Alert.NUKE_COMPLETE),
                of(Sc2Api.Alert.ResearchComplete, Alert.RESEARCH_COMPLETE),
                of(Sc2Api.Alert.TrainError, Alert.TRAIN_ERROR),
                of(Sc2Api.Alert.TrainUnitComplete, Alert.TRAIN_UNIT_COMPLETE),
                of(Sc2Api.Alert.TrainWorkerComplete, Alert.TRAIN_WORKER_COMPLETE),
                of(Sc2Api.Alert.TransformationComplete, Alert.TRANSFORMATION_COMPLETE),
                of(Sc2Api.Alert.UnitUnderAttack, Alert.UNIT_UNDER_ATTACK),
                of(Sc2Api.Alert.UpgradeComplete, Alert.UPGRADE_COMPLETE),
                of(Sc2Api.Alert.VespeneExhausted, Alert.VESPENE_EXHAUSTED),
                of(Sc2Api.Alert.WarpInComplete, Alert.WARP_IN_COMPLETE),
                of(Sc2Api.Alert.NuclearLaunchDetected, Alert.NUCLEAR_LAUNCH_DETECTED),
                of(Sc2Api.Alert.NydusWormDetected, Alert.NYDUS_WORM_DETECTED));
    }

    @Test
    void throwsExceptionWhenAlertIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Alert.from(nothing()))
                .withMessage("sc2api alert is required");
    }
}
