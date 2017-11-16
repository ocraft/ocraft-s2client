package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiActionUiSelectIdleWorker;
import static com.github.ocraft.s2client.protocol.Fixtures.without;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectIdleWorker.Type.*;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectIdleWorker.selectIdleWorker;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.of;

class ActionUiSelectIdleWorkerTest {
    @Test
    void throwsExceptionWhenSc2ApiActionUiSelectIdleWorkerIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiSelectIdleWorker.from(nothing()))
                .withMessage("sc2api action ui select idle worker is required");
    }

    @Test
    void convertsSc2ApiActionUiSelectIdleWorkerToActionUiSelectIdleWorker() {
        assertThatAllFieldsAreConverted(ActionUiSelectIdleWorker.from(sc2ApiActionUiSelectIdleWorker()));
    }

    private void assertThatAllFieldsAreConverted(ActionUiSelectIdleWorker selectIdleWorker) {
        assertThat(selectIdleWorker.getType()).as("action ui select idle worker: type").isEqualTo(ADD_ALL);
    }

    @Test
    void throwsExceptionWhenTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiSelectIdleWorker.from(without(
                        () -> sc2ApiActionUiSelectIdleWorker().toBuilder(),
                        Ui.ActionSelectIdleWorker.Builder::clearType).build()))
                .withMessage("type is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((ActionUiSelectIdleWorker.Builder) selectIdleWorker()).build())
                .withMessage("type is required");
    }

    @ParameterizedTest(name = "\"{0}\" is mapped to {1}")
    @MethodSource(value = "selectIdleWorkerTypeMappings")
    void mapsSc2ApiSelectIdleWorkerType(
            Ui.ActionSelectIdleWorker.Type sc2ApiSelectIdleWorkerType,
            ActionUiSelectIdleWorker.Type expectedSelectIdleWorkerType) {
        assertThat(ActionUiSelectIdleWorker.Type.from(sc2ApiSelectIdleWorkerType))
                .isEqualTo(expectedSelectIdleWorkerType);
    }

    private static Stream<Arguments> selectIdleWorkerTypeMappings() {
        return Stream.of(
                of(Ui.ActionSelectIdleWorker.Type.Set, SET),
                of(Ui.ActionSelectIdleWorker.Type.Add, ADD),
                of(Ui.ActionSelectIdleWorker.Type.All, ALL),
                of(Ui.ActionSelectIdleWorker.Type.AddAll, ADD_ALL));
    }

    @ParameterizedTest(name = "\"{0}\" is serialization of {1}")
    @MethodSource(value = "selectIdleWorkerTypeMappings")
    void serializesToSc2ApiSelectIdleWorkerType(
            Ui.ActionSelectIdleWorker.Type expectedSc2ApiSelectIdleWorkerType,
            ActionUiSelectIdleWorker.Type selectIdleWorkerType) {
        assertThat(selectIdleWorkerType.toSc2Api()).isEqualTo(expectedSc2ApiSelectIdleWorkerType);
    }

    @Test
    void throwsExceptionWhenSelectIdleWorkerTypeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiSelectIdleWorker.Type.from(nothing()))
                .withMessage("sc2api select idle worker type is required");
    }

    @Test
    void serializesToSc2ApiActionUiSelectIdleWorker() {
        assertThatAllFieldsInRequestAreSerialized(selectIdleWorker().withMode(ADD).build().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Ui.ActionSelectIdleWorker sc2ApiUiSelectIdleWorker) {
        assertThat(sc2ApiUiSelectIdleWorker.getType()).as("sc2api ui select idle worker: type")
                .isEqualTo(Ui.ActionSelectIdleWorker.Type.Add);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUiSelectIdleWorker.class).withNonnullFields("type").verify();
    }

}