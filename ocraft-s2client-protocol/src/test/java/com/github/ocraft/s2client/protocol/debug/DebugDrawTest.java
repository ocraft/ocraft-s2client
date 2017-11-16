package com.github.ocraft.s2client.protocol.debug;

import SC2APIProtocol.Debug;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static com.github.ocraft.s2client.protocol.debug.DebugDraw.draw;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DebugDrawTest {

    @Test
    void serializesToSc2ApiDebugDraw() {
        assertThatAllFieldsAreSerialized(draw()
                .texts(debugText().build())
                .lines(debugLine().build())
                .spheres(debugSphere().build())
                .boxes(debugBox().build()).build().toSc2Api());
    }

    private void assertThatAllFieldsAreSerialized(Debug.DebugDraw sc2ApiDebugDraw) {
        assertThat(sc2ApiDebugDraw.getTextList()).as("sc2api debug draw: texts").isNotEmpty();
        assertThat(sc2ApiDebugDraw.getLinesList()).as("sc2api debug draw: lines").isNotEmpty();
        assertThat(sc2ApiDebugDraw.getBoxesList()).as("sc2api debug draw: boxes").isNotEmpty();
        assertThat(sc2ApiDebugDraw.getSpheresList()).as("sc2api debug draw: spheres").isNotEmpty();
    }

    @Test
    void serializesToSc2ApiDebugDrawWithBuilder() {
        assertThatAllFieldsAreSerialized(draw()
                .texts(debugText())
                .lines(debugLine())
                .spheres(debugSphere())
                .boxes(debugBox()).build().toSc2Api());
    }

    @Test
    void throwsExceptionWhenOneOfDrawElementsIsNotSet() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ((DebugDraw.Builder) draw()).build())
                .withMessage("one of draw elements is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(DebugDraw.class).withNonnullFields("texts", "lines", "boxes", "spheres").verify();
    }
}