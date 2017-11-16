package com.github.ocraft.s2client.protocol.syntax.action.raw;

import com.github.ocraft.s2client.protocol.data.Ability;

public interface UseAbilitySyntax {
    TargetSyntax useAbility(Ability ability);
}
