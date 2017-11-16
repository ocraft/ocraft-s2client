package com.github.ocraft.s2client.protocol.query;

import SC2APIProtocol.Query;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.unit.Tag;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.query.QueryBuildingPlacement.placeBuilding;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class QueryBuildingPlacementTest {

    @Test
    void serializesToSc2ApiQueryBuildingPlacement() {
        assertThatAllFieldsAreSerialized(queryBuildingPlacement().build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Query.RequestQueryBuildingPlacement sc2ApiText) {
        assertThat(sc2ApiText.hasAbilityId()).as("sc2api building placement: has ability").isTrue();
        assertThat(sc2ApiText.hasTargetPos()).as("sc2api building placement: has target pos").isTrue();
        assertThat(sc2ApiText.hasPlacingUnitTag()).as("sc2api building placement: has placing unit tag").isTrue();
    }

    @Test
    void throwsExceptionWhenUnitIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(placeBuilding().useAbility(Abilities.BUILD_ARMORY)).build())
                .withMessage("unit is required");
    }

    private QueryBuildingPlacement.Builder fullAccessTo(Object obj) {
        return (QueryBuildingPlacement.Builder) obj;
    }

    @Test
    void throwsExceptionWhenAbilityIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(placeBuilding().withUnit(Tag.from(UNIT_TAG))).on(START).build())
                .withMessage("ability is required");
    }

    @Test
    void throwsExceptionWhenTargetIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fullAccessTo(placeBuilding().withUnit(Tag.from(UNIT_TAG))
                        .useAbility(Abilities.BUILD_ARMORY)).build())
                .withMessage("target is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(QueryBuildingPlacement.class).withNonnullFields("ability", "target").verify();
    }

}