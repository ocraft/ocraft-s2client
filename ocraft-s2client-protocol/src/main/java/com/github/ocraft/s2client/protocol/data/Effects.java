package com.github.ocraft.s2client.protocol.data;

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
