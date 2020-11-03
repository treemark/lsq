package com.lsq.core.docker.components.mysql;

public class ContainerExistsException extends RuntimeException {

	public ContainerExistsException(String string) {
		super(string);
	}

}
