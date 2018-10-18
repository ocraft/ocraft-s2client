package com.github.ocraft.s2client.protocol.observation.ui;

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

import SC2APIProtocol.Ui;
import com.github.ocraft.s2client.protocol.unit.UnitInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ProductionPanelTest {
    @Test
    void throwsExceptionWhenSc2ApiProductionPanelIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ProductionPanel.from(nothing()))
                .withMessage("sc2api production panel is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiProductionPanel() {
        assertThatAllFieldsAreConverted(ProductionPanel.from(sc2ApiProductionPanel()));
    }

    private void assertThatAllFieldsAreConverted(ProductionPanel productionPanel) {
        assertThat(productionPanel.getUnit()).as("production panel: unit").isNotNull();
        assertThat(productionPanel.getBuildQueue()).as("production panel: build queue").isNotEmpty();
        assertThat(productionPanel.getProductionQueue()).as("production panel: production queue").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenUnitIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ProductionPanel.from(without(
                        () -> sc2ApiProductionPanel().toBuilder(),
                        Ui.ProductionPanel.Builder::clearUnit).build()))
                .withMessage("unit is required");
    }

    @Test
    void hasEmptyListOfBuildQueueWhenNotProvided() {
        assertThat(ProductionPanel.from(
                without(() -> sc2ApiProductionPanel().toBuilder(), Ui.ProductionPanel.Builder::clearBuildQueue).build()
        ).getBuildQueue()).as("production panel: empty build queue list").isEmpty();
    }

    @Test
    void hasEmptyListOfProductionQueueWhenNotProvided() {
        assertThat(ProductionPanel.from(
                without(() -> sc2ApiProductionPanel().toBuilder(), Ui.ProductionPanel.Builder::clearProductionQueue)
                        .build()
        ).getProductionQueue()).as("production panel: empty production queue list").isEmpty();
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier
                .forClass(ProductionPanel.class)
                .withNonnullFields("unit", "buildQueue", "productionQueue")
                .withPrefabValues(UnitInfo.class, UnitInfo.from(sc2ApiUnitInfoAddOn()), UnitInfo.from(sc2ApiUnitInfo()))
                .verify();
    }


}
