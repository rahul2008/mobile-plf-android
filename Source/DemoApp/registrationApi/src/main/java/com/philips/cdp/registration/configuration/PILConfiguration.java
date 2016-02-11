
package com.philips.cdp.registration.configuration;

public class PILConfiguration {

	private String micrositeId;

	private String registrationEnvironment;

	private String campaignID;

	public String getMicrositeId() {
		return micrositeId;
	}

	public void setMicrositeId(String micrositeId) {
		this.micrositeId = micrositeId;
	}

	public String getRegistrationEnvironment() {
		return registrationEnvironment;
	}

	 void setRegistrationEnvironment(final String registrationEnvironment) {
		this.registrationEnvironment = registrationEnvironment;
	}

	public void setRegistrationEnvironment(final Configuration registrationEnvironment) {
		this.registrationEnvironment = registrationEnvironment.getValue();
	}


	public String getCampaignID() {
		return campaignID;
	}

	public void setCampaignID(String campaignID) {
		this.campaignID = campaignID;
	}

}
