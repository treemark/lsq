package com.lsq.core.docker.components.mysql;

public class DoneTailingLogException extends RuntimeException {
	public DoneTailingLogException(Boolean found) {
		this.found = found;
	}

	boolean found;
}
