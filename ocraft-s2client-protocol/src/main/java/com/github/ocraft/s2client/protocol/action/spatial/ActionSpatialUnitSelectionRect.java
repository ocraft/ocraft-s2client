package com.github.ocraft.s2client.protocol.action.spatial;

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

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.spatial.RectangleI;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionRectBuilder;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.ActionSpatialUnitSelectionRectSyntax;
import com.github.ocraft.s2client.protocol.syntax.action.spatial.AddSyntax;

import java.util.HashSet;
import java.util.Set;

import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;
import static com.github.ocraft.s2client.protocol.Preconditions.requireNotEmpty;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

public final class ActionSpatialUnitSelectionRect
        implements Sc2ApiSerializable<Spatial.ActionSpatialUnitSelectionRect> {

    private static final long serialVersionUID = -5680933609170654190L;

    private static final boolean DEFAULT_SELECTION_ADD = false;

    private final Set<RectangleI> selectionsInScreenCoord;
    private final boolean selectionAdd;

    public static final class Builder implements ActionSpatialUnitSelectionRectSyntax, AddSyntax {

        private Set<RectangleI> selectionsInScreenCoord = new HashSet<>();
        private boolean selectionAdd;

        @Override
        public AddSyntax of(RectangleI... selections) {
            selectionsInScreenCoord.addAll(asList(selections));
            return this;
        }

        @Override
        public ActionSpatialUnitSelectionRectBuilder add() {
            selectionAdd = true;
            return this;
        }

        @Override
        public ActionSpatialUnitSelectionRect build() {
            requireNotEmpty("selection list", selectionsInScreenCoord);
            return new ActionSpatialUnitSelectionRect(this);
        }
    }

    private ActionSpatialUnitSelectionRect(Builder builder) {
        selectionsInScreenCoord = builder.selectionsInScreenCoord;
        selectionAdd = builder.selectionAdd;
    }

    private ActionSpatialUnitSelectionRect(
            Spatial.ActionSpatialUnitSelectionRect sc2ApiActionSpatialUnitSelectionRect) {

        selectionsInScreenCoord = sc2ApiActionSpatialUnitSelectionRect.getSelectionScreenCoordList().stream()
                .map(RectangleI::from)
                .collect(toSet());

        requireNotEmpty("selection list", selectionsInScreenCoord);

        selectionAdd = tryGet(
                Spatial.ActionSpatialUnitSelectionRect::getSelectionAdd,
                Spatial.ActionSpatialUnitSelectionRect::hasSelectionAdd
        ).apply(sc2ApiActionSpatialUnitSelectionRect).orElse(DEFAULT_SELECTION_ADD);
    }

    public static ActionSpatialUnitSelectionRectSyntax select() {
        return new Builder();
    }

    public static ActionSpatialUnitSelectionRect from(
            Spatial.ActionSpatialUnitSelectionRect sc2ApiActionSpatialUnitSelectionRect) {
        require("sc2api action spatial unit selection rect", sc2ApiActionSpatialUnitSelectionRect);
        return new ActionSpatialUnitSelectionRect(sc2ApiActionSpatialUnitSelectionRect);
    }

    @Override
    public Spatial.ActionSpatialUnitSelectionRect toSc2Api() {
        return Spatial.ActionSpatialUnitSelectionRect.newBuilder()
                .setSelectionAdd(selectionAdd)
                .addAllSelectionScreenCoord(selectionsInScreenCoord.stream().map(RectangleI::toSc2Api).collect(toSet()))
                .build();
    }

    public Set<RectangleI> getSelectionsInScreenCoord() {
        return new HashSet<>(selectionsInScreenCoord);
    }

    public boolean isSelectionAdd() {
        return selectionAdd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionSpatialUnitSelectionRect that = (ActionSpatialUnitSelectionRect) o;

        return selectionAdd == that.selectionAdd && selectionsInScreenCoord.equals(that.selectionsInScreenCoord);
    }

    @Override
    public int hashCode() {
        int result = selectionsInScreenCoord.hashCode();
        result = 31 * result + (selectionAdd ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
