package com.github.ocraft.s2client.protocol.syntax.query;

import com.github.ocraft.s2client.protocol.data.Ability;
import com.github.ocraft.s2client.protocol.query.TargetSyntax;

public interface UseAbilitySyntax {
    TargetSyntax useAbility(Ability ability);
}
