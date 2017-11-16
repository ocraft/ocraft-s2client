package com.github.ocraft.s2client.protocol.game;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class PlayerSetupTest {

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(PlayerSetup.class)
                .withRedefinedSubclass(ComputerPlayerSetup.class)
                .withNonnullFields("playerType")
                .verify();
    }


}