package com.cloudcomputing.aura.utils;

/**
 * @author Ramanathan and Suraj
 *
 * Enum Class for the type of request that are available
 * 
 */
public enum RequestType {

	MAPS("Google Maps Request"), PLACES("Google Places Request");

	private String detail;

	RequestType(String detail) {
		this.detail = detail;
	}

	public String detail() {
		return detail;
	}

}
