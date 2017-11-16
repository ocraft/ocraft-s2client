package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.request.RequestCreateGame;

public interface WithRandomSeedSyntax extends BuilderSyntax<RequestCreateGame> {
    BuilderSyntax<RequestCreateGame> withRandomSeed(int randomSeed);
}
