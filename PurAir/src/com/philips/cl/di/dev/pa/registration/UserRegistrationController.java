package com.philips.cl.di.dev.pa.registration;


import android.support.v4.app.Fragment;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.settings.JanrainConfigurationSettings;

public class UserRegistrationController {

	private static UserRegistrationController smInstance;

	private UserRegistrationController() {
		JanrainConfigurationSettings config = new JanrainConfigurationSettings();
		config.init(PurAirApplication.getAppContext(), "4r36zdbeycca933nufcknn2hnpsz6gxu", "81338", "REGISTRATION_USE_EVAL");
	}
	
	public static UserRegistrationController getInstance() {
		if(smInstance == null) {
			smInstance = new UserRegistrationController();
		}
		return smInstance;
	}
	
//	public Fragment getFragment() {
//		Fragment fragment = null ;
//		if(isUserLoggedIn()) {
//			fragment = new SignedInFragment();
//		} else {
//			fragment = new CreateAccountFragment();
//		}
//		return fragment ;
//	}

	public boolean isUserLoggedIn() {
		User user = new User(PurAirApplication.getAppContext());
		DIUserProfile profile = user.getUserInstance(PurAirApplication.getAppContext());
		
		if(profile != null) {
			return true;
		} else {
			return false;
		}
	}
}
