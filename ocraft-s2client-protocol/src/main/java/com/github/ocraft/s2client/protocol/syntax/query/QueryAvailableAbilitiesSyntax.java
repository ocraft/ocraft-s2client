package com.github.ocraft.s2client.protocol.syntax.query;

import com.github.ocraft.s2client.protocol.unit.Tag;
import com.github.ocraft.s2client.protocol.unit.Unit;

public interface QueryAvailableAbilitiesSyntax {
    QueryAvailableAbilitiesBuilder of(Tag unitTag);

    QueryAvailableAbilitiesBuilder of(Unit unit);
}
