package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.data.Units;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ControlGroupTest {

    @Test
    void throwsExceptionWhenSc2ApiControlGroupIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ControlGroup.from(nothing()))
                .withMessage("sc2api control group is required");
    }

    @Test
    void convertsSc2ApiControlGroupToControlGroup() {
        assertThatAllFieldsAreConverted(ControlGroup.from(sc2ApiControlGroup()));
    }

    private void assertThatAllFieldsAreConverted(ControlGroup controlGroup) {
        assertThat(controlGroup.getIndex()).as("control group: index").isEqualTo(CONTROL_GROUP_INDEX);
        assertThat(controlGroup.getLeaderUnitType()).as("control group: leader unit type")
                .isEqualTo(Units.PROTOSS_COLOSSUS);
        assertThat(controlGroup.getCount()).as("control group: count").isEqualTo(UNIT_COUNT);
    }

    @Test
    void throwsExceptionWhenIndexIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ControlGroup.from(without(
                        () -> sc2ApiControlGroup().toBuilder(),
                        Ui.ControlGroup.Builder::clearControlGroupIndex).build()))
                .withMessage("index is required");
    }

    @Test
    void throwsExceptionWhenLeaderUnitTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ControlGroup.from(without(
                        () -> sc2ApiControlGroup().toBuilder(),
                        Ui.ControlGroup.Builder::clearLeaderUnitType).build()))
                .withMessage("leader unit type is required");
    }

    @Test
    void throwsExceptionWhenCountIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ControlGroup.from(without(
                        () -> sc2ApiControlGroup().toBuilder(),
                        Ui.ControlGroup.Builder::clearCount).build()))
                .withMessage("count is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ControlGroup.class).withNonnullFields("leaderUnitType").verify();
    }
}