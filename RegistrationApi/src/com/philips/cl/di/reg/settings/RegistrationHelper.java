
package com.philips.cl.di.reg.settings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.configuration.ConfigurationParser;
import com.philips.cl.di.reg.configuration.RegistrationConfiguration;
import com.philips.cl.di.reg.configuration.SocialProviders;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class RegistrationHelper {

	private static Context mContext;

	public static boolean mJanrainIntialized = false;

	private static RegistrationHelper mRegistrationHelper = null;

	private EvalRegistrationSettings mEvalRegistrationSettings;

	private DevRegistrationSettings mDevRegistrationSettings;

	private ProdRegistrationSettings mProdRegistrationSettings;

	private RegistrationSettings mRegistrationSettings;

	private SocialProviders mSocialProivder;

	public enum Janrain {
		INITIALIZE(true), REINITIALIZE(false);

		private final boolean value;

		Janrain(boolean value) {
			this.value = value;
		}

		public boolean getValue() {
			return this.value;
		}
	}

	private interface RegistrationEnvironment {

		String EVAL = "EVAL";

		String DEV = "DEV";

		String PROD = "PROD";
	}

	public static boolean isJanrainIntialized() {
		return mJanrainIntialized;
	}

	public static void setJanrainIntialized(boolean janrainIntializationStatus) {
		mJanrainIntialized = janrainIntializationStatus;
	}

	private RegistrationHelper() {

	}

	public static RegistrationHelper getInstance() {
		if (mRegistrationHelper == null) {
			mRegistrationHelper = new RegistrationHelper();
		}
		return mRegistrationHelper;
	}

	private final BroadcastReceiver janrainStatusReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent != null) {
				Bundle extras = intent.getExtras();
				RLog.i(RLog.ACTIVITY_LIFECYCLE,
				        "janrainStatusReceiver, intent = " + intent.toString());
				if ((Jump.JR_DOWNLOAD_FLOW_SUCCESS.equalsIgnoreCase(intent.getAction()))
				        && (null != extras)) {
					mJanrainIntialized = true;

					EventHelper.getInstance()
					        .notifyEventOccurred(RegConstants.JANRAIN_INIT_SUCCESS);
				} else if (Jump.JR_FAILED_TO_DOWNLOAD_FLOW.equalsIgnoreCase(intent.getAction())
				        && (extras != null)) {

					EventHelper.getInstance()
					        .notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);

					mJanrainIntialized = false;
				}
			}
		}
	};

	/*
	 * Initialize Janrain
	 * @param isInitialized true for initialize and false for reinitialize
	 * Janrain
	 */
	public void intializeRegistrationSettings(final Janrain isInitialized, final Context context,
	        final Locale locale) {
		mJanrainIntialized = false;
		mContext = context.getApplicationContext();
		NetworkUtility.getInstance().checkIsOnline(mContext);
		mSocialProivder = null;
		new Thread(new Runnable() {

			@Override
			public void run() {

				RegistrationConfiguration registrationConfiguration = parseConfigurationJson(mContext);
				if (null != registrationConfiguration) {
					mSocialProivder = registrationConfiguration.getSocialProviders();
					IntentFilter flowFilter = new IntentFilter(Jump.JR_DOWNLOAD_FLOW_SUCCESS);
					flowFilter.addAction(Jump.JR_FAILED_TO_DOWNLOAD_FLOW);
					LocalBroadcastManager.getInstance(context).registerReceiver(
					        janrainStatusReceiver, flowFilter);

					String mMicrositeId = registrationConfiguration.getPilConfiguration()
					        .getMicrositeId();

					RLog.i(RLog.JANRAIN_INITIALIZE, "Mixrosite ID : " + mMicrositeId);

					String mRegistrationType = registrationConfiguration.getPilConfiguration()
					        .getRegistrationEnvironment();
					RLog.i(RLog.JANRAIN_INITIALIZE, "Registration Environment : "
					        + mRegistrationType);

					boolean mIsInitialize = isInitialized.getValue();
					String mLocale = locale.toString();

					if (NetworkUtility.getInstance().isOnline()) {

						if (RegistrationEnvironment.EVAL.equalsIgnoreCase(mRegistrationType)) {
							RLog.i(RLog.JANRAIN_INITIALIZE, "Client ID : "
							        + registrationConfiguration.getJanRainConfiguration()
							                .getClientIds().getEvaluationId());
							initEvalSettings(mContext, registrationConfiguration
							        .getJanRainConfiguration().getClientIds().getEvaluationId(),
							        mMicrositeId, mRegistrationType, mIsInitialize, mLocale);
						}
						if (RegistrationEnvironment.PROD.equalsIgnoreCase(mRegistrationType)) {
							RLog.i(RLog.JANRAIN_INITIALIZE, "Client ID : "
							        + registrationConfiguration.getJanRainConfiguration()
							                .getClientIds().getProductionId());
							initProdSettings(mContext, registrationConfiguration
							        .getJanRainConfiguration().getClientIds().getProductionId(),
							        mMicrositeId, mRegistrationType, mIsInitialize, mLocale);

						}
						if (RegistrationEnvironment.DEV.equalsIgnoreCase(mRegistrationType)) {
							RLog.i(RLog.JANRAIN_INITIALIZE, "Client ID : "
							        + registrationConfiguration.getJanRainConfiguration()
							                .getClientIds().getDevelopmentId());
							initDevSettings(mContext, registrationConfiguration
							        .getJanRainConfiguration().getClientIds().getDevelopmentId(),
							        mMicrositeId, mRegistrationType, mIsInitialize, mLocale);
						}

					}
				}
			}
		}).start();
	}

	public SocialProviders getSocialProviders() {
		return mSocialProivder;
	}

	private RegistrationConfiguration parseConfigurationJson(Context context) {
		RegistrationConfiguration registrationConfiguration = null;
		AssetManager assetManager = context.getAssets();
		try {
			JSONObject configurationJson = new JSONObject(
			        convertStreamToString(assetManager.open(RegConstants.CONFIGURATION_JSON_PATH)));
			ConfigurationParser configurationParser = new ConfigurationParser();
			registrationConfiguration = configurationParser.parse(configurationJson);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return registrationConfiguration;
	}

	public static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public void unregisterListener(Context context) {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(janrainStatusReceiver);
	}

	private void initEvalSettings(Context context, String captureClientId, String microSiteId,
	        String registrationType, boolean isintialize, String locale) {

		mEvalRegistrationSettings = new EvalRegistrationSettings();
		mRegistrationSettings = mEvalRegistrationSettings;
		mEvalRegistrationSettings.intializeRegistrationSettings(context, captureClientId,
		        microSiteId, registrationType, isintialize, locale);

	}

	private void initDevSettings(Context context, String captureClientId, String microSiteId,
	        String registrationType, boolean isintialize, String locale) {

		mDevRegistrationSettings = new DevRegistrationSettings();
		mRegistrationSettings = mDevRegistrationSettings;
		mDevRegistrationSettings.intializeRegistrationSettings(context, captureClientId,
		        microSiteId, registrationType, isintialize, locale);
	}

	private void initProdSettings(Context context, String captureClientId, String microSiteId,
	        String registrationType, boolean isintialize, String locale) {

		mProdRegistrationSettings = new ProdRegistrationSettings();
		mRegistrationSettings = mProdRegistrationSettings;
		mProdRegistrationSettings.intializeRegistrationSettings(context, captureClientId,
		        microSiteId, registrationType, isintialize, locale);
	}

	public RegistrationSettings getRegistrationSettings() {
		return mRegistrationSettings;
	}

}
