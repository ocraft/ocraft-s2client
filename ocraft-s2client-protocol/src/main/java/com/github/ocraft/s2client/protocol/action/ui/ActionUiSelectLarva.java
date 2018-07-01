package com.github.ocraft.s2client.protocol.action.ui;

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
import com.github.ocraft.s2client.protocol.Sc2ApiSerializable;
import com.github.ocraft.s2client.protocol.Strings;

import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ActionUiSelectLarva implements Sc2ApiSerializable<Ui.ActionSelectLarva> {

    private static final long serialVersionUID = -5834863791532766043L;

    private ActionUiSelectLarva() {

    }

    public static ActionUiSelectLarva from(Ui.ActionSelectLarva sc2ApiActionSelectLarva) {
        require("sc2api action ui select larva", sc2ApiActionSelectLarva);
        return new ActionUiSelectLarva();
    }

    public static ActionUiSelectLarva selectLarva() {
        return new ActionUiSelectLarva();
    }

    @Override
    public Ui.ActionSelectLarva toSc2Api() {
        return Ui.ActionSelectLarva.newBuilder().build();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof ActionUiSelectLarva;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }

}
