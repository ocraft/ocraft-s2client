package com.github.ocraft.s2client.protocol.unit;

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

import SC2APIProtocol.Common;
import com.github.ocraft.s2client.protocol.data.Abilities;
import com.github.ocraft.s2client.protocol.spatial.Point;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.TRAIN_PROGRESS;
import static com.github.ocraft.s2client.protocol.Fixtures.UNIT_TAG;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiRallyTarget;
import static com.github.ocraft.s2client.protocol.Fixtures.sc2ApiRallyTargetWithTag;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RallyTargetTest {
    @Test
    void throwsExceptionWhenSc2ApiPointIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> RallyTarget.from(nothing()))
                .withMessage("sc2api rally target is required");
    }

    @Test
    void convertsSc2ApiTagToTag() {
        assertThat(RallyTarget.from(sc2ApiRallyTargetWithTag()).getTag()).as("rally target: tag")
                .isEqualTo(Optional.of(Tag.from(UNIT_TAG)));
    }

    @Test
    void convertsSc2ApiPointToPoint() {
        assertThat(RallyTarget.from(sc2ApiRallyTarget()).getPoint()).as("rally target: point")
                .isEqualTo(Point.of(10.1f, 20.2f, 30.3f));
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(RallyTarget.class).withNonnullFields("point").verify();
    }

}
