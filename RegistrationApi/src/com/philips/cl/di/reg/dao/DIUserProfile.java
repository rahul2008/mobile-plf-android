package com.philips.cl.di.reg.dao;

public class DIUserProfile {

	private String email;
	private String givenName;
	private String password;
	private String displayName;
    private boolean olderThanAgeLimit;
    private boolean receiveMarketingEmail;
   
    // For Traditional Registration
	 public DIUserProfile() {

	}

	public DIUserProfile(String email,String givenName,String password,
			boolean olderThanAgeLimit,boolean receiveMarketingEmail) {

		this.email = email;
		this.givenName = givenName;
		this.password = password;
		this.olderThanAgeLimit = olderThanAgeLimit;
		this.receiveMarketingEmail = receiveMarketingEmail;
		
		}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	
	public boolean getOlderThanAgeLimit() {
		return olderThanAgeLimit;
	}

	public void setOlderThanAgeLimit(boolean olderThanAgeLimit) {
		this.olderThanAgeLimit = olderThanAgeLimit;
	}

	public boolean getReceiveMarketingEmail() {
		return receiveMarketingEmail;
	}

	public void setReceiveMarketingEmail(boolean receiveMarketingEmail) {
		this.receiveMarketingEmail = receiveMarketingEmail;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
