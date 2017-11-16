package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionUiTest {

    private enum Action {
        CONTROL_GROUP,
        SELECT_ARMY,
        SELECT_WARP_GATES,
        SELECT_LARVA,
        SELECT_IDLE_WORKER,
        MULTI_PANEL,
        CARGO_PANEL_UNLOAD,
        PRODUCTION_PANEL_REMOVE_FROM_QUEUE,
        TOGGLE_AUTOCAST
    }

    @Test
    void throwsExceptionWhenSc2ApiActionUiIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUi.from(nothing()))
                .withMessage("sc2api action ui is required");
    }

    @Test
    void convertsSc2ApiActionUiControlGroup() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithControlGroup());
        assertThatCorrectActionIsConverted(Action.CONTROL_GROUP, actionUi);
    }

    private void assertThatCorrectActionIsConverted(Action action, ActionUi actionUi) {
        if (Action.CONTROL_GROUP.equals(action)) {
            assertThat(actionUi.getControlGroup()).as("action ui: control group").isNotEmpty();
        } else {
            assertThat(actionUi.getControlGroup()).as("action ui: control group").isEmpty();
        }
        if (Action.SELECT_ARMY.equals(action)) {
            assertThat(actionUi.getSelectArmy()).as("action ui: select army").isNotEmpty();
        } else {
            assertThat(actionUi.getSelectArmy()).as("action ui: select army").isEmpty();
        }
        if (Action.SELECT_WARP_GATES.equals(action)) {
            assertThat(actionUi.getSelectWarpGates()).as("action ui: select warp gates").isNotEmpty();
        } else {
            assertThat(actionUi.getSelectWarpGates()).as("action ui: select warp gates").isEmpty();
        }
        if (Action.SELECT_LARVA.equals(action)) {
            assertThat(actionUi.getSelectLarva()).as("action ui: select larva").isNotEmpty();
        } else {
            assertThat(actionUi.getSelectLarva()).as("action ui: select larva").isEmpty();
        }
        if (Action.SELECT_IDLE_WORKER.equals(action)) {
            assertThat(actionUi.getSelectIdleWorker()).as("action ui: select idle worker").isNotEmpty();
        } else {
            assertThat(actionUi.getSelectIdleWorker()).as("action ui: select idle worker").isEmpty();
        }
        if (Action.MULTI_PANEL.equals(action)) {
            assertThat(actionUi.getMultiPanel()).as("action ui: multi panel").isNotEmpty();
        } else {
            assertThat(actionUi.getMultiPanel()).as("action ui: multi panel").isEmpty();
        }
        if (Action.CARGO_PANEL_UNLOAD.equals(action)) {
            assertThat(actionUi.getCargoPanelUnload()).as("action ui: cargo panel unload").isNotEmpty();
        } else {
            assertThat(actionUi.getCargoPanelUnload()).as("action ui: cargo panel unload").isEmpty();
        }
        if (Action.PRODUCTION_PANEL_REMOVE_FROM_QUEUE.equals(action)) {
            assertThat(actionUi.getProductionPanelRemoveFromQueue()).as("action ui: production panel remove from queue")
                    .isNotEmpty();
        } else {
            assertThat(actionUi.getProductionPanelRemoveFromQueue()).as("action ui: production panel remove from queue")
                    .isEmpty();
        }
        if (Action.TOGGLE_AUTOCAST.equals(action)) {
            assertThat(actionUi.getToggleAutocast()).as("action ui: toggle autocast").isNotEmpty();
        } else {
            assertThat(actionUi.getToggleAutocast()).as("action ui: toggle autocast").isEmpty();
        }
    }

    @Test
    void convertsSc2ApiActionUiSelectArmy() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithSelectArmy());
        assertThatCorrectActionIsConverted(Action.SELECT_ARMY, actionUi);

    }

    @Test
    void convertsSc2ApiActionUiSelectWarpGates() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithSelectWarpGates());
        assertThatCorrectActionIsConverted(Action.SELECT_WARP_GATES, actionUi);

    }

    @Test
    void convertsSc2ApiActionUiSelectLarva() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithSelectLarva());
        assertThatCorrectActionIsConverted(Action.SELECT_LARVA, actionUi);

    }

    @Test
    void convertsSc2ApiActionUiSelectIdleWorker() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithSelectIdleWorker());
        assertThatCorrectActionIsConverted(Action.SELECT_IDLE_WORKER, actionUi);

    }

    @Test
    void convertsSc2ApiActionUiMultiPanel() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithMultiPanel());
        assertThatCorrectActionIsConverted(Action.MULTI_PANEL, actionUi);

    }

    @Test
    void convertsSc2ApiActionUiCargoPanelUnload() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithCargoPanelUnload());
        assertThatCorrectActionIsConverted(Action.CARGO_PANEL_UNLOAD, actionUi);

    }

    @Test
    void convertsSc2ApiActionUiProductionPanelRemoveFromQueue() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithProductionPanelRemoveFromQueue());
        assertThatCorrectActionIsConverted(Action.PRODUCTION_PANEL_REMOVE_FROM_QUEUE, actionUi);

    }

    @Test
    void convertsSc2ApiActionUiToggleAutocast() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithToggleAutocast());
        assertThatCorrectActionIsConverted(Action.TOGGLE_AUTOCAST, actionUi);
    }

    @Test
    void throwsExceptionWhenThereIsNoActionCase() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUi.from(aSc2ApiActionUi().build()))
                .withMessage("one of action case is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUi.of((ActionUiControlGroup) nothing()))
                .withMessage("control group is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUi.of((ActionUiSelectArmy) nothing()))
                .withMessage("select army is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUi.of((ActionUiSelectWarpGates) nothing()))
                .withMessage("select warp gates is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUi.of((ActionUiSelectLarva) nothing()))
                .withMessage("select larva is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUi.of((ActionUiSelectIdleWorker) nothing()))
                .withMessage("select idle worker is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUi.of((ActionUiMultiPanel) nothing()))
                .withMessage("multi panel is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUi.of((ActionUiCargoPanelUnload) nothing()))
                .withMessage("cargo panel unload is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUi.of((ActionUiProductionPanelRemoveFromQueue) nothing()))
                .withMessage("production panel remove from queue is required");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUi.of((ActionUiToggleAutocast) nothing()))
                .withMessage("toggle autocast is required");
    }

    @Test
    void serializesToSc2ApiActionUiSelectArmy() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithSelectArmy());
        assertThatCorrectActionIsSerialized(Action.SELECT_ARMY, actionUi.toSc2Api());
    }

    private void assertThatCorrectActionIsSerialized(Action action, Ui.ActionUI sc2ApiActionUi) {
        if (Action.CONTROL_GROUP.equals(action)) {
            assertThat(sc2ApiActionUi.hasControlGroup()).as("sc2api action ui: has control group").isTrue();
        } else {
            assertThat(sc2ApiActionUi.hasControlGroup()).as("sc2api action ui: has control group").isFalse();
        }
        if (Action.SELECT_ARMY.equals(action)) {
            assertThat(sc2ApiActionUi.hasSelectArmy()).as("sc2api action ui: has select army").isTrue();
        } else {
            assertThat(sc2ApiActionUi.hasSelectArmy()).as("sc2api action ui: has select army").isFalse();
        }
        if (Action.SELECT_WARP_GATES.equals(action)) {
            assertThat(sc2ApiActionUi.hasSelectWarpGates()).as("sc2api action ui: has select warp gates").isTrue();
        } else {
            assertThat(sc2ApiActionUi.hasSelectWarpGates()).as("sc2api action ui: has select warp gates").isFalse();
        }
        if (Action.SELECT_LARVA.equals(action)) {
            assertThat(sc2ApiActionUi.hasSelectLarva()).as("sc2api action ui: has select larva").isTrue();
        } else {
            assertThat(sc2ApiActionUi.hasSelectLarva()).as("sc2api action ui: has select larva").isFalse();
        }
        if (Action.SELECT_IDLE_WORKER.equals(action)) {
            assertThat(sc2ApiActionUi.hasSelectIdleWorker()).as("sc2api action ui: has select idle worker").isTrue();
        } else {
            assertThat(sc2ApiActionUi.hasSelectIdleWorker()).as("sc2api action ui: has select idle worker").isFalse();
        }
        if (Action.MULTI_PANEL.equals(action)) {
            assertThat(sc2ApiActionUi.hasMultiPanel()).as("sc2api action ui: has multi panel").isTrue();
        } else {
            assertThat(sc2ApiActionUi.hasMultiPanel()).as("sc2api action ui: has multi panel").isFalse();
        }
        if (Action.CARGO_PANEL_UNLOAD.equals(action)) {
            assertThat(sc2ApiActionUi.hasCargoPanel()).as("sc2api action ui: has cargo panel unload").isTrue();
        } else {
            assertThat(sc2ApiActionUi.hasCargoPanel()).as("sc2api action ui: has cargo panel unload").isFalse();
        }
        if (Action.PRODUCTION_PANEL_REMOVE_FROM_QUEUE.equals(action)) {
            assertThat(sc2ApiActionUi.hasProductionPanel())
                    .as("sc2api action ui: has production panel remove from queue").isTrue();
        } else {
            assertThat(sc2ApiActionUi.hasProductionPanel())
                    .as("sc2api action ui: has production panel remove from queue").isFalse();
        }
        if (Action.TOGGLE_AUTOCAST.equals(action)) {
            assertThat(sc2ApiActionUi.hasToggleAutocast()).as("sc2api action ui: has toggle autocast").isTrue();
        } else {
            assertThat(sc2ApiActionUi.hasToggleAutocast()).as("sc2api action ui: has toggle autocast").isFalse();
        }
    }

    @Test
    void serializesToSc2ApiActionUiSelectWarpGates() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithSelectWarpGates());
        assertThatCorrectActionIsSerialized(Action.SELECT_WARP_GATES, actionUi.toSc2Api());

    }

    @Test
    void serializesToSc2ApiActionUiSelectLarva() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithSelectLarva());
        assertThatCorrectActionIsSerialized(Action.SELECT_LARVA, actionUi.toSc2Api());

    }

    @Test
    void serializesToSc2ApiActionUiSelectIdleWorker() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithSelectIdleWorker());
        assertThatCorrectActionIsSerialized(Action.SELECT_IDLE_WORKER, actionUi.toSc2Api());

    }

    @Test
    void serializesToSc2ApiActionUiMultiPanel() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithMultiPanel());
        assertThatCorrectActionIsSerialized(Action.MULTI_PANEL, actionUi.toSc2Api());

    }

    @Test
    void serializesToSc2ApiActionUiCargoPanelUnload() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithCargoPanelUnload());
        assertThatCorrectActionIsSerialized(Action.CARGO_PANEL_UNLOAD, actionUi.toSc2Api());

    }

    @Test
    void serializesToSc2ApiActionUiProductionPanelRemoveFromQueue() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithProductionPanelRemoveFromQueue());
        assertThatCorrectActionIsSerialized(Action.PRODUCTION_PANEL_REMOVE_FROM_QUEUE, actionUi.toSc2Api());

    }

    @Test
    void serializesToSc2ApiActionUiToggleAutocast() {
        ActionUi actionUi = ActionUi.from(sc2ApiActionUiWithToggleAutocast());
        assertThatCorrectActionIsSerialized(Action.TOGGLE_AUTOCAST, actionUi.toSc2Api());
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUi.class).withIgnoredFields("selectLarva").verify();
    }
}