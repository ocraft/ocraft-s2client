package com.github.ocraft.s2client.bot;

/*-
 * #%L
 * ocraft-s2client-bot
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

public enum ClientError {
    ERROR_SC2, // TODO p.picheta needed?

    /**
     * An ability was improperly mapped to an ability id that doesn't exist.
     */
    INVALID_ABILITY_REMAP,

    /**
     * The response does not contain a field that was expected.
     */
    INVALID_RESPONSE,

    /**
     * The unit does not have any abilities.
     */
    NO_ABILITIES_FOR_TAG,

    /**
     * A request was made without consuming the response from the previous request of given type.
     */
    RESPONSE_NOT_CONSUMED,

    /**
     * The response received from SC2 does not match the request.
     */
    RESPONSE_MISMATCH, // TODO p.picheta needed?

    /**
     * The websocket connection has prematurely closed, this could mean starcraft crashed or a websocket timeout has
     * occurred.
     */
    CONNECTION_CLOSED,

    SC2_UNKNOWN_STATUS,

    /**
     * SC2 has either crashed or been forcibly terminated by this library because it was not responding to requests.
     */
    SC2_APP_FAILURE,

    /**
     * The response from SC2 contains errors, most likely meaning the API was not used in a correct way.
     */
    SC2_PROTOCOL_ERROR,

    /**
     * A request was made and a response was not received in the amount of time given by the timeout.
     */
    SC2_PROTOCOL_TIMEOUT,

    /**
     * A replay was attempted to be loaded in the wrong game version.
     */
    WRONG_GAME_VERSION,
}
