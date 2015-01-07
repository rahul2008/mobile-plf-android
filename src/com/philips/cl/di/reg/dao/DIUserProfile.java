package com.philips.cl.di.reg.dao;

public class DIUserProfile {

	private String mEmail;
	private String mGivenName;
	private String mPassword;
	private String mDisplayName;
	private boolean mIsOlderThanAgeLimit;
	private boolean mIsReceiveMarketingEmail;

	// For Traditional Registration
	public DIUserProfile() {

	}

	public DIUserProfile(String email, String givenName, String password,
			boolean isOlderThanAgeLimit, boolean isReceiveMarketingEmail) {

		mEmail = email;
		mGivenName = givenName;
		mPassword = password;
		mIsOlderThanAgeLimit = isOlderThanAgeLimit;
		mIsReceiveMarketingEmail = isReceiveMarketingEmail;

	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		mPassword = password;
	}

	public String getGivenName() {
		return mGivenName;
	}

	public void setGivenName(String givenName) {
		mGivenName = givenName;
	}

	public boolean getOlderThanAgeLimit() {
		return mIsOlderThanAgeLimit;
	}

	public void setOlderThanAgeLimit(boolean isOlderThanAgeLimit) {
		mIsOlderThanAgeLimit = isOlderThanAgeLimit;
	}

	public boolean getReceiveMarketingEmail() {
		return mIsReceiveMarketingEmail;
	}

	public void setReceiveMarketingEmail(boolean isReceiveMarketingEmail) {
		mIsReceiveMarketingEmail = isReceiveMarketingEmail;
	}

	public String getDisplayName() {
		return mDisplayName;
	}

	public void setDisplayName(String displayName) {
		mDisplayName = displayName;
	}

}
