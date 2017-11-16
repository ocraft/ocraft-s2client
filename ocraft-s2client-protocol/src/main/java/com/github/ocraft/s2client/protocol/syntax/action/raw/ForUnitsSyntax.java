package com.github.ocraft.s2client.protocol.syntax.action.raw;

import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

public interface ForUnitsSyntax {
    ActionRawToggleAutocastBuilder forUnits(Tag... units);

    ActionRawToggleAutocastBuilder forUnits(Unit... units);
}
