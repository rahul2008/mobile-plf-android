package com.philips.cl.di.reg.errormapping;

public enum Error {

	GENERIC_ERROR(1),
	INVALID_PARAM(2),
	AUTHENTICATION_CANCELED_BY_USER(3),
	INVALID_EMAILID(4),
	EMAIL_ADDRESS_IN_USE(5),
	NO_NETWORK_CONNECTION(6),
	CONFIGURATION_FAILED(7),
	AUTHENTICATION_FAILED(8),
	INVALID_PASSWORD(9),
	INVALID_USERNAME_OR_PASSWORD(10),
	ACCOUNT_DOESNOT_EXIST(11),
	TWO_STEP_ERROR(12),
	MERGE_FLOW_ERROR(13),
	EMAIL_ALREADY_EXIST(14),
	ENGAGE_ERROR(15);
	
	private int mErrorCode;
    
	private Error(int code) {
		mErrorCode = code;
	}
	public int geterrorList() {
		return mErrorCode;
	}

}
