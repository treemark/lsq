/**
 * Copyright (c) 2018-2019, Mihai Emil Andronache
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1)Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 2)Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 3)Neither the name of docker-java-api nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.amihaiemil.docker;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import javax.json.Json;
import javax.json.JsonArray;
import java.io.IOException;

/**
 * Handler that reads a JsonArray from the response.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id: a845af0a179f8b7c4fef245b17f6aa53a032ebe4 $
 * @since 0.0.1
 */
final class ReadJsonArray implements ResponseHandler<JsonArray> {

    /**
     * Handlers to be executed before actually reading the array.
     */
    private final ResponseHandler<HttpResponse> other;

    /**
     * Ctor.
     * @param other Handlers to be executed before actually reading the array.
     */
    ReadJsonArray(final ResponseHandler<HttpResponse> other) {
        this.other = other;
    }

    @Override
    public JsonArray handleResponse(final HttpResponse httpResponse)
        throws IOException {
        final HttpResponse resp = this.other.handleResponse(httpResponse);
        return Json.createReader(
            resp.getEntity().getContent()
        ).readArray();
    }
}
