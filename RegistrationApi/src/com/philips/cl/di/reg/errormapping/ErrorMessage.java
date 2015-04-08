package com.philips.cl.di.reg.errormapping;

public class ErrorMessage {
	private String mErrorMessage;
	
	public ErrorMessage() {}
	
	public String getError(int error) {
		if (error == 1) {
			mErrorMessage = "Generic Error";
		} else if (error == 2) {
			mErrorMessage = "Ivvalid parameters";
		} else if (error == 3) {
			mErrorMessage = "Authentication cancelled by user";
		} else if (error == 4) {
			mErrorMessage = "Invalid email id";
		} else if (error == 5) {
			mErrorMessage = "Email Address already in use";
		} else if (error == 6) {
			mErrorMessage = "Internet connection lost";
		} else if (error == 7) {
			mErrorMessage = "Configuration failed";
		} else if (error == 8) {
			mErrorMessage = "Authentication Failed";
		} else if (error == 9) {
			mErrorMessage = "Invalid Password";
		} else if (error == 10) {
			mErrorMessage = "Invalid username or password";
		}else if (error == 11) {
			mErrorMessage = "Account doesnot exist";
		}else if (error == 12) {
			mErrorMessage = "Two step error";
		}else if (error == 13) {
			mErrorMessage = "Merge flow error";
		}
		else if (error == 14) {
			mErrorMessage = "Email address already in use";
		}
		else if (error == 15) {
			mErrorMessage = "Engage error";
		}
		return mErrorMessage;
		}
	}
