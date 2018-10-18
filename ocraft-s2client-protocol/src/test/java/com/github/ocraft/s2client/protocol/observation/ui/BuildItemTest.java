package com.github.ocraft.s2client.protocol.observation.ui;

import SC2APIProtocol.Ui;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiBuildItem;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BuildItemTest {
    @Test
    void throwsExceptionWhenSc2ApiBuildItemIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BuildItem.from(nothing()))
                .withMessage("sc2api build item is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiBuildItem() {
        assertThatAllFieldsAreConverted(BuildItem.from(sc2ApiBuildItem()));
    }

    private void assertThatAllFieldsAreConverted(BuildItem buildItem) {
        assertThat(buildItem.getAbility()).as("build item: ability").isNotNull();
        assertThat(buildItem.getBuildProgress()).as("build item: build progress").isGreaterThan(0.0f);
    }

    @Test
    void throwsExceptionWhenAbilityIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BuildItem.from(
                        without(() -> sc2ApiBuildItem().toBuilder(), Ui.BuildItem.Builder::clearAbilityId).build()))
                .withMessage("ability is required");
    }

    @Test
    void throwsExceptionWhenBuildProgressIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BuildItem.from(
                        without(() -> sc2ApiBuildItem().toBuilder(), Ui.BuildItem.Builder::clearBuildProgress).build()))
                .withMessage("build progress is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(BuildItem.class).withNonnullFields("ability").verify();
    }
}