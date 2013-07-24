package com.philips.cl.di.dev.pa.exception;


public class AirException extends Exception {

	public AirException(Exception e) {

		super(e.getMessage());
	}

	public AirException(String exceptionMessage) {

		super(exceptionMessage);
	}
}
