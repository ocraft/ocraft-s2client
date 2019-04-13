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
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.data.Buff;
import com.github.ocraft.s2client.protocol.data.Buffs;
import com.github.ocraft.s2client.protocol.unit.UnitInfo;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public final class SinglePanel implements Serializable {

    private static final long serialVersionUID = -4279366565953511871L;

    private final UnitInfo unit;

    // since 4.84
    private final Integer attackUpgradeLevel;
    private final Integer armorUpgradeLevel;
    private final Integer shieldUpgradeLevel;
    private final Set<Buff> buffs;

    private SinglePanel(Ui.SinglePanel sc2ApiSinglePanel) {
        unit = tryGet(
                Ui.SinglePanel::getUnit, Ui.SinglePanel::hasUnit
        ).apply(sc2ApiSinglePanel).map(UnitInfo::from).orElseThrow(required("unit"));

        attackUpgradeLevel = tryGet(Ui.SinglePanel::getAttackUpgradeLevel, Ui.SinglePanel::hasAttackUpgradeLevel)
                .apply(sc2ApiSinglePanel).orElse(nothing());

        armorUpgradeLevel = tryGet(Ui.SinglePanel::getArmorUpgradeLevel, Ui.SinglePanel::hasArmorUpgradeLevel)
                .apply(sc2ApiSinglePanel).orElse(nothing());

        shieldUpgradeLevel = tryGet(Ui.SinglePanel::getShieldUpgradeLevel, Ui.SinglePanel::hasShieldUpgradeLevel)
                .apply(sc2ApiSinglePanel).orElse(nothing());

        buffs = sc2ApiSinglePanel.getBuffsList().stream().map(Buffs::from)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    public static SinglePanel from(Ui.SinglePanel sc2ApiSinglePanel) {
        require("sc2api single panel", sc2ApiSinglePanel);
        return new SinglePanel(sc2ApiSinglePanel);
    }

    public UnitInfo getUnit() {
        return unit;
    }

    public Optional<Integer> getAttackUpgradeLevel() {
        return Optional.ofNullable(attackUpgradeLevel);
    }

    public Optional<Integer> getArmorUpgradeLevel() {
        return Optional.ofNullable(armorUpgradeLevel);
    }

    public Optional<Integer> getShieldUpgradeLevel() {
        return Optional.ofNullable(shieldUpgradeLevel);
    }

    public Set<Buff> getBuffs() {
        return buffs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SinglePanel that = (SinglePanel) o;

        if (!unit.equals(that.unit)) return false;
        if (!Objects.equals(attackUpgradeLevel, that.attackUpgradeLevel))
            return false;
        if (!Objects.equals(armorUpgradeLevel, that.armorUpgradeLevel))
            return false;
        if (!Objects.equals(shieldUpgradeLevel, that.shieldUpgradeLevel))
            return false;
        return buffs.equals(that.buffs);

    }

    @Override
    public int hashCode() {
        int result = unit.hashCode();
        result = 31 * result + (attackUpgradeLevel != null ? attackUpgradeLevel.hashCode() : 0);
        result = 31 * result + (armorUpgradeLevel != null ? armorUpgradeLevel.hashCode() : 0);
        result = 31 * result + (shieldUpgradeLevel != null ? shieldUpgradeLevel.hashCode() : 0);
        result = 31 * result + buffs.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
