
package com.philips.cl.di.reg.configuration;

public class RegistrationConfiguration {

	private JanRainConfiguration janRainConfiguration;

	private PILConfiguration pilConfiguration;

	private SocialProviders socialProviders;

	private Flow flow;

	private static RegistrationConfiguration registrationConfiguration;

	private RegistrationConfiguration() {

	}

	public static RegistrationConfiguration getInstance() {
		if (registrationConfiguration == null) {
			registrationConfiguration = new RegistrationConfiguration();
		}
		return registrationConfiguration;
	}

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

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

}
