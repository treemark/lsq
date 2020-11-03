package com.lsq.core.docker.components.mysql;

public class NoContainerFoundException extends RuntimeException {

	public NoContainerFoundException(String string) {
		super(string);
	}

}
