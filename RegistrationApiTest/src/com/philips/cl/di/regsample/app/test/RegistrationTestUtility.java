package com.philips.cl.di.regsample.app.test;

import com.philips.cl.di.reg.dao.DIUserProfile;

public class RegistrationTestUtility {

	public static DIUserProfile setValuesForTraditionalLogin(String mGivenName, String mUserEmail, String password, boolean olderThanAgeLimit, 
			boolean isReceiveMarketingEmail){
		
		DIUserProfile profile = new DIUserProfile();
		profile.setGivenName(mGivenName);
		profile.setEmail(mUserEmail);
		profile.setPassword(password);
		profile.setOlderThanAgeLimit(olderThanAgeLimit);
		profile.setReceiveMarketingEmail(isReceiveMarketingEmail);
		
		return profile;
		
	}
}
