package com.github.ocraft.s2client.protocol.action.ui;

import SC2APIProtocol.Ui;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiActionUiSelectLarva;
import static com.github.ocraft.s2client.protocol.action.ui.ActionUiSelectLarva.selectLarva;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ActionUiSelectLarvaTest {
    @Test
    void throwsExceptionWhenSc2ApiActionUiSelectLarvaIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ActionUiSelectLarva.from(nothing()))
                .withMessage("sc2api action ui select larva is required");
    }

    @Test
    void convertsSc2ApiActionUiSelectLarvaToActionUiSelectLarva() {
        assertThat(ActionUiSelectLarva.from(sc2ApiActionUiSelectLarva())).as("action ui select larva").isNotNull();
    }

    @Test
    void serializesToSc2ApiActionUiSelectLarva() {
        assertThatAllFieldsInRequestAreSerialized(selectLarva().toSc2Api());
    }

    private void assertThatAllFieldsInRequestAreSerialized(Ui.ActionSelectLarva sc2ApiUiSelectLarva) {
        assertThat(sc2ApiUiSelectLarva).as("sc2api ui select larva").isNotNull();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(ActionUiSelectLarva.class).verify();
    }
}