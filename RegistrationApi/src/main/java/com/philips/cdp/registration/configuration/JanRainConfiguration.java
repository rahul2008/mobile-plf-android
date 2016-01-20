
package com.philips.cdp.registration.configuration;

public class JanRainConfiguration {

	private RegistrationClientId clientIds;

	public RegistrationClientId getClientIds() {
		return clientIds;
	}

	JanRainConfiguration(){

	}
	public  JanRainConfiguration(RegistrationClientId clientIds){
		this.clientIds = clientIds;
	}

	 void setClientIds(RegistrationClientId clientIds) {
		this.clientIds = clientIds;
	}

}
