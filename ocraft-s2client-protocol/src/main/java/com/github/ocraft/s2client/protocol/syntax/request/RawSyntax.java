package com.github.ocraft.s2client.protocol.syntax.request;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2019 Ocraft Project
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

public interface RawSyntax extends ScoreSyntax {

    /**
     * By default raw actions select, act and revert the selection. This is useful
     * if you're playing simultaneously with the agent so it doesn't steal your
     * selection. This inflates APM (due to deselect) and makes the actions hard
     * to follow in a replay. Setting this to true will cause raw actions to do
     * select, act, but not revert the selection.
     */
    RawSyntax rawAffectsSelection(Boolean value);

    /**
     * Changes the coordinates in raw.proto to be relative to the playable area.
     * The map_size and playable_area will be the diagonal of the real playable area.
     */
    RawSyntax rawCropToPlayableArea(Boolean value);
}
