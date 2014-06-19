package com.philips.cl.di.reg.errormapping;

public class ErrorMessage {
	private String errorMessage;
	
	public ErrorMessage() {}
	
	public String getError(int error) {
		if (error == 1) {
			errorMessage = "GENERIC ERROR";
		} else if (error == 2) {
			errorMessage = "INVALID PARAMETERS";
		} else if (error == 3) {
			errorMessage = "AUTHENTICATION CANCELED BY USER";
		} else if (error == 4) {
			errorMessage = "INVALID EMAILID";
		} else if (error == 5) {
			errorMessage = "EMAIL ADDRESS IN USE";
		} else if (error == 6) {
			errorMessage = "INTERNET CONNECTION LOST";
		} else if (error == 7) {
			errorMessage = "CONFIGURATION FAILED";
		} else if (error == 8) {
			errorMessage = "AUTHENTICATION FAILED";
		} else if (error == 9) {
			errorMessage = "INVALID PASSWORD";
		} else if (error == 10) {
			errorMessage = "INVALID USERNAME OR PASSWORD";
		}else if (error == 11) {
			errorMessage = "ACCOUNT DOES NOT EXIST";
		}else if (error == 12) {
			errorMessage = "TWO STEP ERROR";
		}else if (error == 13) {
			errorMessage = "MERGE_FLOW_ERROR";
		}
		else if (error == 14) {
			errorMessage = "EMAIL_ALREADY_EXIST";
		}
		else if (error == 15) {
			errorMessage = "ENGAGE_ERROR";
		}
		return errorMessage;
		}
	}
