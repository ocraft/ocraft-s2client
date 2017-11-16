package com.github.ocraft.s2client.protocol.syntax.debug;

import com.github.ocraft.s2client.protocol.debug.DebugSetUnitValue;

public interface ValueTypeSyntax {
    ValueSyntax set(DebugSetUnitValue.UnitValue unitValue);
}
