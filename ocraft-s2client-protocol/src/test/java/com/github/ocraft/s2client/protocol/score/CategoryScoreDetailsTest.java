package com.github.ocraft.s2client.protocol.score;

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

import SC2APIProtocol.ScoreOuterClass;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CategoryScoreDetailsTest {

    @Test
    void throwsExceptionWhenSc2ApiCategoryScoreDetailsIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CategoryScoreDetails.from(nothing()))
                .withMessage("sc2api category score details is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiCategoryScoreDetails() {
        assertThatAllFieldsAreConverted(CategoryScoreDetails.from(sc2ApiCategoryScoreDetails()));
    }

    private void assertThatAllFieldsAreConverted(CategoryScoreDetails categoryScoreDetails) {
        assertThat(categoryScoreDetails.getNone()).as("category score details: none")
                .isEqualTo(CATEGORY_SCORE_NONE);
        assertThat(categoryScoreDetails.getArmy()).as("category score details: army")
                .isEqualTo(CATEGORY_SCORE_ARMY);
        assertThat(categoryScoreDetails.getEconomy()).as("category score details: economy")
                .isEqualTo(CATEGORY_SCORE_ECONOMY);
        assertThat(categoryScoreDetails.getTechnology()).as("category score details: technology")
                .isEqualTo(CATEGORY_SCORE_TECHNOLOGY);
        assertThat(categoryScoreDetails.getUpgrade()).as("category score details: upgrade")
                .isEqualTo(CATEGORY_SCORE_UPGRADE);
    }

    @Test
    void throwsExceptionWhenNoneIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CategoryScoreDetails.from(without(
                        () -> sc2ApiCategoryScoreDetails().toBuilder(),
                        ScoreOuterClass.CategoryScoreDetails.Builder::clearNone).build()))
                .withMessage("none is required");
    }

    @Test
    void throwsExceptionWhenArmyIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CategoryScoreDetails.from(without(
                        () -> sc2ApiCategoryScoreDetails().toBuilder(),
                        ScoreOuterClass.CategoryScoreDetails.Builder::clearArmy).build()))
                .withMessage("army is required");
    }

    @Test
    void throwsExceptionWhenEconomyIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CategoryScoreDetails.from(without(
                        () -> sc2ApiCategoryScoreDetails().toBuilder(),
                        ScoreOuterClass.CategoryScoreDetails.Builder::clearEconomy).build()))
                .withMessage("economy is required");
    }

    @Test
    void throwsExceptionWhenTechnologyIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CategoryScoreDetails.from(without(
                        () -> sc2ApiCategoryScoreDetails().toBuilder(),
                        ScoreOuterClass.CategoryScoreDetails.Builder::clearTechnology).build()))
                .withMessage("technology is required");
    }

    @Test
    void throwsExceptionWhenUpgradeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CategoryScoreDetails.from(without(
                        () -> sc2ApiCategoryScoreDetails().toBuilder(),
                        ScoreOuterClass.CategoryScoreDetails.Builder::clearUpgrade).build()))
                .withMessage("upgrade is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(CategoryScoreDetails.class).verify();
    }

}
