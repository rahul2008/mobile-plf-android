
package com.philips.cl.di.reg.configuration;

import java.util.ArrayList;

public class RegistrationConfiguration {

	private JanRainConfiguration janRainConfiguration;

	private PILConfiguration pilConfiguration;

	private SocialProviders socialProviders;

	public JanRainConfiguration getJanRainConfiguration() {
		return janRainConfiguration;
	}

	public void setJanRainConfiguration(JanRainConfiguration janRainConfiguration) {
		this.janRainConfiguration = janRainConfiguration;
	}

	public PILConfiguration getPilConfiguration() {
		return pilConfiguration;
	}

	public void setPilConfiguration(PILConfiguration pilConfiguration) {
		this.pilConfiguration = pilConfiguration;
	}

	public SocialProviders getSocialProviders() {
		return socialProviders;
	}

	public void setSocialProviders(SocialProviders socialProviders) {
		this.socialProviders = socialProviders;
	}

}
