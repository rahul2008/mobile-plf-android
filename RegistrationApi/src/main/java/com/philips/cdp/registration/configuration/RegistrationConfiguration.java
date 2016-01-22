
package com.philips.cdp.registration.configuration;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.support.v4.content.LocalBroadcastManager;

import com.janrain.android.Jump;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.listener.RegistrationConfigurationListener;
import com.philips.cdp.registration.settings.RegistrationEnvironmentConstants;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class RegistrationConfiguration {

	private JanRainConfiguration janRainConfiguration;

	private PILConfiguration pilConfiguration;

	private SigninProviders socialProviders;

	private Flow flow;

	private static RegistrationConfiguration registrationConfiguration;

	private CurrentHSDPConfiguration hsdpConfiguration;

	private RegistrationConfiguration() {
		janRainConfiguration = new JanRainConfiguration();
		pilConfiguration = new PILConfiguration();
		socialProviders = new SigninProviders();
		flow = new Flow();

	}

	public static RegistrationConfiguration getInstance() {
		if (registrationConfiguration == null) {
			registrationConfiguration = new RegistrationConfiguration();
		}
		return registrationConfiguration;
	}

	public static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	private void parseConfigurationJson(Context context, String path, RegistrationConfigurationListener registrationConfigurationListener) {
		AssetManager assetManager = context.getAssets();
		try {
			JSONObject configurationJson = new JSONObject(
					convertStreamToString(assetManager.open(path)));
			ConfigurationParser configurationParser = new ConfigurationParser();
			configurationParser.parse(configurationJson);
			registrationConfigurationListener.onSuccess();

		} catch (IOException e) {
			registrationConfigurationListener.onFailuer();
			e.printStackTrace();
		} catch (JSONException e) {
			registrationConfigurationListener.onFailuer();
			e.printStackTrace();
		}
	}

	public void initialize(final Context context, final RegistrationConfigurationListener registrationConfigurationListener){


		new Thread(new Runnable() {

			@Override
			public void run() {
					parseConfigurationJson(context, RegConstants.CONFIGURATION_JSON_PATH,registrationConfigurationListener );





			}
		}).start();

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

	public SigninProviders getSocialProviders() {
		return socialProviders;
	}

	public void setSocialProviders(SigninProviders socialProviders) {
		this.socialProviders = socialProviders;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	public CurrentHSDPConfiguration getHsdpConfiguration() {
		if(hsdpConfiguration == null) {
			hsdpConfiguration = new CurrentHSDPConfiguration();
		}

		return hsdpConfiguration;
	}



	public void setHsdpConfiguration(CurrentHSDPConfiguration hsdpConfiguration) {
		this.hsdpConfiguration = hsdpConfiguration;
	}

	public HSDPConfiguration getCurrentHSDPConfiguration(){
		return hsdpConfiguration;
	}
}
