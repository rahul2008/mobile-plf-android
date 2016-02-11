
package com.philips.cdp.registration.configuration;

public class JanRainConfiguration {

	private RegistrationClientId clientIds;

	public RegistrationClientId getClientIds() {
		return clientIds;
	}

	public JanRainConfiguration(){

	}
	public  JanRainConfiguration(RegistrationClientId clientIds){
		this.clientIds = clientIds;
	}

	public void setClientIds(RegistrationClientId clientIds) {
		this.clientIds = clientIds;
	}



}
