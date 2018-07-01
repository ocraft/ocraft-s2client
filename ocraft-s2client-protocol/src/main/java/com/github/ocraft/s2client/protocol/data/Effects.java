package com.github.ocraft.s2client.protocol.data;

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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public enum Effects implements Effect {
    NULL(0),
    PSI_STORM_PERSISTENT(1),
    GUARDIAN_SHIELD_PERSISTENT(2),
    TEMPORAL_FIELD_GROWING_BUBBLE_CREATE_PERSISTENT(3),
    TEMPORAL_FIELD_AFTER_BUBBLE_CREATE_PERSISTENT(4),
    THERMAL_LANCES_FORWARD(5),
    SCANNER_SWEEP(6),
    NUKE_PERSISTENT(7),
    LIBERATOR_TARGET_MORPH_DELAY_PERSISTENT(8),
    LIBERATOR_TARGET_MORPH_PERSISTENT(9),
    BLINDING_CLOUD_CP(10),
    RAVAGER_CORROSIVE_BILE_CP(11),
    LURKER_MP(12);

    public static final class Other implements Effect {

        private static final long serialVersionUID = 6062989464678152879L;

        private static final Map<Integer, Other> INSTANCES = new ConcurrentHashMap<>();

        private final int effectId;

        private Other(int effectId) {
            this.effectId = effectId;
        }

        public static Other of(int effectId) {
            INSTANCES.computeIfAbsent(effectId, Other::new);
            return INSTANCES.get(effectId);
        }

        @Override
        public int getEffectId() {
            return effectId;
        }

        @Override
        public Integer toSc2Api() {
            return getEffectId();
        }

        @Override
        public String toString() {
            return "EFFECT_" + effectId;
        }
    }


    private final int effectId;

    private static Map<Integer, Effect> effectIdMap = new HashMap<>();

    static {
        EnumSet.allOf(Effects.class).forEach(effect -> effectIdMap.put(effect.getEffectId(), effect));
    }

    public static Effect from(int sc2ApiEffectId) {
        return Optional.ofNullable(Effects.effectIdMap.get(sc2ApiEffectId)).orElse(Other.of(sc2ApiEffectId));
    }

    Effects(int effectId) {
        this.effectId = effectId;
    }

    @Override
    public Integer toSc2Api() {
        return effectId;
    }

    @Override
    public int getEffectId() {
        return effectId;
    }
}
