
package com.philips.cl.di.reg.configuration;

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

	public void setRegistrationEnvironment(String registrationEnvironment) {
		this.registrationEnvironment = registrationEnvironment;
	}

	public String getCampaignID() {
		return campaignID;
	}

	public void setCampaignID(String campaignID) {
		this.campaignID = campaignID;
	}

}
