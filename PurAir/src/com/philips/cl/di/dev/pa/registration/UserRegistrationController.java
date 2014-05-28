package com.philips.cl.di.dev.pa.registration;


import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.errormapping.Error;
import com.philips.cl.di.reg.settings.JanrainConfigurationSettings;

public class UserRegistrationController {

	private static UserRegistrationController smInstance;
	
	//TODO : Replace with PurAir values (Currently using Figaro values).
	private static final String CAPTURE_CLIENT_ID = "yakfnsg2hph355fhga2mcac5ywn6fgzt";
	private static final String MICRO_SITE_ID = "81374";
	private static final String REGISTRATION_TYPE = "REGISTRATION_USE_EVAL";

	private UserRegistrationController() {
		JanrainConfigurationSettings config = new JanrainConfigurationSettings();
		config.init(PurAirApplication.getAppContext(), CAPTURE_CLIENT_ID, MICRO_SITE_ID, REGISTRATION_TYPE);
	}
	
	public static UserRegistrationController getInstance() {
		if(smInstance == null) {
			smInstance = new UserRegistrationController();
		}
		return smInstance;
	}
	
	public boolean isUserLoggedIn() {
		User user = new User(PurAirApplication.getAppContext());
		DIUserProfile profile = user.getUserInstance(PurAirApplication.getAppContext());
		
		return (null != profile);
	}
	
	public Error getErrorEnum(int errorCode) {
		if (errorCode == 2) return Error.INVALID_PARAM;
	    if (errorCode == 3) return Error.AUTHENTICATION_CANCELED_BY_USER;
		if (errorCode == 4) return Error.INVALID_EMAILID;
		if (errorCode == 5) return Error.EMAIL_ADDRESS_IN_USE;
		if (errorCode == 6) return Error.NO_NETWORK_CONNECTION;
		if (errorCode == 7) return Error.CONFIGURATION_FAILED;
		if (errorCode == 8) return Error.AUTHENTICATION_FAILED;
		if (errorCode == 9) return Error.INCORRECT_PASSWORD;
		if (errorCode == 10) return Error.INVALID_USERNAME_OR_PASSWORD;
		if (errorCode == 11) return Error.ACCOUNT_DOESNOT_EXIST;
		if (errorCode == 12) return Error.TWO_STEP_ERROR;
		if (errorCode == 13) return Error.MERGE_FLOW_ERROR;
		if (errorCode == 14) return Error.EMAIL_ALREADY_EXIST;
		return Error.GENERIC_ERROR ;
	}
	
}
