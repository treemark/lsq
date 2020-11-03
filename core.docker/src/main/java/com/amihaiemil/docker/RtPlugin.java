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

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

/**
 * Runtime {@link Plugin}.
 * @author Boris Kuzmic (boris.kuzmic@gmail.com)
 * @since 0.0.8
 */
final class RtPlugin extends JsonResource implements Plugin {

    /**
     * Apache HttpClient which sends the requests.
     */
    private final HttpClient client;

    /**
     * Base URI.
     */
    private final URI uri;

    /**
     * Docker API.
     */
    private final Docker docker;

    /**
     * Ctor.
     * @param rep JsonObject representation of this Volume.
     * @param client The http client.
     * @param uri The URI for this image.
     * @param dkr The docker entry point.
     * @checkstyle ParameterNumber (5 lines)
     */
    RtPlugin(
        final JsonObject rep, final HttpClient client,
        final URI uri, final Docker dkr
    ) {
        super(rep);
        this.client = client;
        this.uri = uri;
        this.docker = dkr;
    }

    @Override
    public JsonObject inspect()
        throws IOException, UnexpectedResponseException {
        return new Inspection(this.client,
            String.format("%s/%s", this.uri.toString(), "json"));
    }

    @Override
    public void enable() throws IOException, UnexpectedResponseException {
        final HttpPost enable =
            new HttpPost(
                String.format("%s/%s", this.uri.toString(), "enable")
            );
        try {
            this.client.execute(
                enable,
                new MatchStatus(
                    enable.getURI(),
                    HttpStatus.SC_OK
                )
            );
        } finally {
            enable.releaseConnection();
        }
    }

    @Override
    public void disable() throws IOException, UnexpectedResponseException {
        final HttpPost disable =
            new HttpPost(
                String.format("%s/%s", this.uri.toString(), "disable")
            );
        try {
            this.client.execute(
                disable,
                new MatchStatus(
                    disable.getURI(),
                    HttpStatus.SC_OK
                )
            );
        } finally {
            disable.releaseConnection();
        }
    }

    @Override
    public void upgrade(final String remote, final JsonArray properties)
        throws IOException, UnexpectedResponseException {
        final HttpPost upgrade =
            new HttpPost(
                new UncheckedUriBuilder(this.uri.toString().concat("/upgrade"))
                    .addParameter("remote", remote)
                    .build()
            );
        try {
            upgrade.setEntity(
                new StringEntity(
                    properties.toString(), ContentType.APPLICATION_JSON
                )
            );
            this.client.execute(
                upgrade,
                new MatchStatus(
                    upgrade.getURI(),
                    HttpStatus.SC_NO_CONTENT
                )
            );
        } finally {
            upgrade.releaseConnection();
        }
    }

    @Override
    public void push() throws IOException, UnexpectedResponseException {
        final HttpPost push =
            new HttpPost(
                String.format("%s/%s", this.uri.toString(), "push")
            );
        try {
            this.client.execute(
                push,
                new MatchStatus(
                    push.getURI(),
                    HttpStatus.SC_OK
                )
            );
        } finally {
            push.releaseConnection();
        }
    }

    @Override
    public void configure(final Map<String, String> options)
        throws IOException, UnexpectedResponseException {
        final JsonArrayBuilder json = Json.createArrayBuilder();
        if (options != null) {
            options.forEach(
                (key, value) -> json.add(String.format("%s=%s", key, value))
            );
        }
        final HttpPost upgrade =
            new HttpPost(
                String.format("%s/%s", this.uri.toString(), "set")
            );
        try {
            upgrade.setEntity(
                new StringEntity(
                    json.build().toString(), ContentType.APPLICATION_JSON
                )
            );
            this.client.execute(
                upgrade,
                new MatchStatus(
                    upgrade.getURI(),
                    HttpStatus.SC_NO_CONTENT
                )
            );
        } finally {
            upgrade.releaseConnection();
        }
    }
}
