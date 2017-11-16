package com.github.ocraft.s2client.protocol.data;

import SC2APIProtocol.Data;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.toSet;

public final class Weapon implements Serializable {

    private static final long serialVersionUID = 8179090982908219422L;

    public enum TargetType {
        GROUND,
        AIR,
        ANY;

        public static TargetType from(Data.Weapon.TargetType sc2ApiTargetType) {
            require("sc2api target type", sc2ApiTargetType);
            switch (sc2ApiTargetType) {
                case Air:
                    return AIR;
                case Any:
                    return ANY;
                case Ground:
                    return GROUND;
                default:
                    throw new AssertionError("unknown sc2api target type: " + sc2ApiTargetType);
            }
        }
    }

    private final TargetType targetType;
    private final float damage;
    private final Set<DamageBonus> damageBonuses;
    private final int attacks;  // Number of hits per attack. (eg. Colossus has 2 beams)
    private final float range;
    private final float speed;  // Time between attacks.

    private Weapon(Data.Weapon sc2ApiWeapon) {
        targetType = tryGet(Data.Weapon::getType, Data.Weapon::hasType)
                .apply(sc2ApiWeapon).map(TargetType::from).orElseThrow(required("target type"));

        damage = tryGet(Data.Weapon::getDamage, Data.Weapon::hasDamage)
                .apply(sc2ApiWeapon).orElseThrow(required("damage"));

        damageBonuses = sc2ApiWeapon.getDamageBonusList().stream().map(DamageBonus::from).collect(toSet());

        attacks = tryGet(Data.Weapon::getAttacks, Data.Weapon::hasAttacks)
                .apply(sc2ApiWeapon).orElseThrow(required("attacks"));

        range = tryGet(Data.Weapon::getRange, Data.Weapon::hasRange)
                .apply(sc2ApiWeapon).orElseThrow(required("range"));

        speed = tryGet(Data.Weapon::getSpeed, Data.Weapon::hasSpeed)
                .apply(sc2ApiWeapon).orElseThrow(required("speed"));
    }

    public static Weapon from(Data.Weapon sc2ApiWeapon) {
        require("sc2api weapon", sc2ApiWeapon);
        return new Weapon(sc2ApiWeapon);
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public float getDamage() {
        return damage;
    }

    public Set<DamageBonus> getDamageBonuses() {
        return new HashSet<>(damageBonuses);
    }

    public int getAttacks() {
        return attacks;
    }

    public float getRange() {
        return range;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weapon weapon = (Weapon) o;

        return Float.compare(weapon.damage, damage) == 0 &&
                attacks == weapon.attacks &&
                Float.compare(weapon.range, range) == 0 &&
                Float.compare(weapon.speed, speed) == 0 &&
                targetType == weapon.targetType &&
                damageBonuses.equals(weapon.damageBonuses);
    }

    @Override
    public int hashCode() {
        int result = targetType.hashCode();
        result = 31 * result + (damage != +0.0f ? Float.floatToIntBits(damage) : 0);
        result = 31 * result + damageBonuses.hashCode();
        result = 31 * result + attacks;
        result = 31 * result + (range != +0.0f ? Float.floatToIntBits(range) : 0);
        result = 31 * result + (speed != +0.0f ? Float.floatToIntBits(speed) : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
