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
import javax.json.JsonObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

/**
 * Runtime {@link Image}.
 * @author George Aristy (george.aristy@gmail.com)
 * @version $Id: 9e6a20fdbba0e8eaf734cbadb751d831cc81e69e $
 * @since 0.0.1
 */
final class RtImage extends JsonResource implements Image {
    /**
     * Apache HttpClient which sends the requests.
     */
    private final HttpClient client;

    /**
     * Base URI.
     */
    private final URI baseUri;

    /**
     * Docker API.
     */
    private final Docker docker;

    /**
     * Ctor.
     * @param rep JsonObject representation of this Image.
     * @param client The http client.
     * @param uri The URI for this image.
     * @param dkr The docker entry point.
     * @checkstyle ParameterNumber (5 lines)
     */
    RtImage(
        final JsonObject rep, final HttpClient client,
        final URI uri, final Docker dkr
    ) {
        super(rep);
        this.client = client;
        this.baseUri = uri;
        this.docker = dkr;
    }

    @Override
    public JsonObject inspect()
        throws IOException, UnexpectedResponseException {
        return new Inspection(this.client, this.baseUri.toString() + "/json");
    }

    @Override
    public Iterable<Image> history() {
        return () -> new ResourcesIterator<>(
            this.client,
            new HttpGet(this.baseUri.toString().concat("/history")),
            json -> new RtImage(
                json,
                this.client,
                this.baseUri,
                this.docker
            )
        );
    }

    @Override
    public void delete() throws IOException, UnexpectedResponseException {
        final HttpDelete delete = new HttpDelete(this.baseUri);
        try {
            this.client.execute(
                delete,
                new MatchStatus(delete.getURI(), HttpStatus.SC_OK)
            );
        } finally {
            delete.releaseConnection();
        }
    }

    @Override
    public void tag(
        final String repo, final String name
    ) throws IOException, UnexpectedResponseException {
        final HttpPost tag = new HttpPost(
            new UncheckedUriBuilder(
                this.baseUri.toString() + "/tag"
            ).addParameter("repo", repo)
            .addParameter("tag", name)
            .build()
        );
        try {
            this.client.execute(
                tag,
                new MatchStatus(tag.getURI(), HttpStatus.SC_CREATED)
            );
        } finally {
            tag.releaseConnection();
        }
    }

    @Override
    public Container run() throws IOException, UnexpectedResponseException {
        final Container container = this.docker.containers().create(
            this.baseUri.getPath().substring(
                this.baseUri.getPath().lastIndexOf('/') + 1
            )
        );
        container.start();
        return container;
    }
    
    @Override
    public Docker docker() {
        return this.docker;
    }
}
