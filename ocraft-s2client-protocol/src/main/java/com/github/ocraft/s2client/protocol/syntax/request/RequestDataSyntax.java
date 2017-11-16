package com.github.ocraft.s2client.protocol.syntax.request;

import com.github.ocraft.s2client.protocol.BuilderSyntax;
import com.github.ocraft.s2client.protocol.request.RequestData;

public interface RequestDataSyntax {
    BuilderSyntax<RequestData> of(RequestData.Type... types);
}
