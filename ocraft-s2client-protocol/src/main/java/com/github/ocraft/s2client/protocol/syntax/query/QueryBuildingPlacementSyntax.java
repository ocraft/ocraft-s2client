package com.github.ocraft.s2client.protocol.syntax.query;

import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

public interface QueryBuildingPlacementSyntax {
    UseAbilitySyntax withUnit(Tag unitTag);

    UseAbilitySyntax withUnit(Unit unit);
}
