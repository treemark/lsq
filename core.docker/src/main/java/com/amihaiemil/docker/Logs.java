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
import java.io.Reader;

import org.apache.http.client.ResponseHandler;

/**
 * Logs of a Container.
 * 
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id: 92724e2148f277c6d1ee7010dfaecb3ca85314d9 $
 * @since 0.0.2
 */
public interface Logs {

	/**
	 * Fetch all the present logs.
	 * 
	 * @return String logs.
	 * @throws IOException
	 *             If something goes wrong.
	 * @throws UnexpectedResponseException
	 *             If the status response is not the expected one.
	 */
	String fetch() throws IOException, UnexpectedResponseException;

	/**
	 * Return the logs as a stream.
	 * 
	 * @return Reader logs' reader.
	 * @throws IOException
	 *             If something goes wrong.
	 * @throws UnexpectedResponseException
	 *             If the status response is not the expected one.
	 */
	Reader follow() throws IOException, UnexpectedResponseException;

	<T> T follow(ResponseHandler<T> handler) throws IOException, UnexpectedResponseException;

	/**
	 * Show stdout logs only.
	 * 
	 * @return New Logs instance.
	 * @throws IOException
	 *             If something goes wrong.
	 * @throws UnexpectedResponseException
	 *             If the status response is not the expected one.
	 */
	Logs stdout() throws IOException, UnexpectedResponseException;

	/**
	 * Show stderr logs only.
	 * 
	 * @return New Logs instance.
	 * @throws IOException
	 *             If something goes wrong.
	 * @throws UnexpectedResponseException
	 *             If the status response is not the expected one.
	 */
	Logs stderr() throws IOException, UnexpectedResponseException;

	/**
	 * The Container to which these Logs belong.
	 * 
	 * @return Container.
	 */
	Container container();

}
