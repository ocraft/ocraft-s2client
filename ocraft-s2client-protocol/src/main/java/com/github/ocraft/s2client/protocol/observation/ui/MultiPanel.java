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
import com.github.ocraft.s2client.protocol.unit.UnitInfo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public final class MultiPanel implements Serializable {

    private static final long serialVersionUID = -9050006820587053858L;

    private final List<UnitInfo> units;

    private MultiPanel(Ui.MultiPanel sc2ApiMultiPanel) {
        units = sc2ApiMultiPanel.getUnitsList().stream().map(UnitInfo::from)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public static MultiPanel from(Ui.MultiPanel sc2ApiMultiPanel) {
        require("sc2api multi panel", sc2ApiMultiPanel);
        return new MultiPanel(sc2ApiMultiPanel);
    }

    public List<UnitInfo> getUnits() {
        return units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiPanel that = (MultiPanel) o;

        return units.equals(that.units);
    }

    @Override
    public int hashCode() {
        return units.hashCode();
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
