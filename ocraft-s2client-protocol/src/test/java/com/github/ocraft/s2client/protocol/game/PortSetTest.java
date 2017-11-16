package com.github.ocraft.s2client.protocol.game;

import SC2APIProtocol.Sc2Api;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PortSetTest {

    @Test
    void throwsExceptionWhenPortIsLowerThanOne() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PortSet.of(0, 1))
                .withMessage("port must be greater than 0");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> PortSet.of(1, 0))
                .withMessage("port must be greater than 0");
    }

    @Test
    void serializesToSc2ApiPortSet() {
        Sc2Api.PortSet sc2ApiPortSet = PortSet.of(1, 2).toSc2Api();

        assertThat(sc2ApiPortSet.getGamePort()).as("game port is serialized").isEqualTo(1);
        assertThat(sc2ApiPortSet.getBasePort()).as("base port is serialized").isEqualTo(2);
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(PortSet.class).verify();
    }


}