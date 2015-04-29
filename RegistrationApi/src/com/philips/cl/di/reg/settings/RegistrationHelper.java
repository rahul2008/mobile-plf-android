package com.philips.cl.di.reg.settings;

import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.R;
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
				if ((Jump.JR_DOWNLOAD_FLOW_SUCCESS.equalsIgnoreCase(intent
						.getAction())) && (null != extras)) {
					mJanrainIntialized = true;
					System.out.println("success");

					EventHelper.getInstance().notifyEventOccurred(
							RegConstants.JANRAIN_INIT_SUCCESS);
				} else if (Jump.JR_FAILED_TO_DOWNLOAD_FLOW
						.equalsIgnoreCase(intent.getAction())
						&& (extras != null)) {

					System.out.println("failed in reciver");

					EventHelper.getInstance().notifyEventOccurred(
							RegConstants.JANRAIN_INIT_FAILURE);

					mJanrainIntialized = false;
				}
			}
		}
	};

	/*
	 * Initialize Janrain
	 * 
	 * @param isInitialized true for initialize and false for reinitialize
	 * Janrain
	 */
	public void intializeRegistrationSettings(Janrain isInitialized,
			Context context, Locale locale) {
		mJanrainIntialized = false;
		mContext = context.getApplicationContext();
		NetworkUtility.getInstance().checkIsOnline(mContext);

		IntentFilter flowFilter = new IntentFilter(
				Jump.JR_DOWNLOAD_FLOW_SUCCESS);
		flowFilter.addAction(Jump.JR_FAILED_TO_DOWNLOAD_FLOW);
		LocalBroadcastManager.getInstance(context).registerReceiver(
				janrainStatusReceiver, flowFilter);

		String mClientId = mContext.getResources()
				.getString(R.string.client_id);
		String mMicrositeId = mContext.getResources().getString(
				R.string.microsite_id);
		String mRegistrationType = mContext.getResources().getString(
				R.string.registration_type);
		boolean mIsInitialize = isInitialized.getValue();
		String mLocale = locale.toString();

		if (NetworkUtility.getInstance().isOnline()) {

			if (mRegistrationType.equalsIgnoreCase("EVAL"))
				initEvalSettings(mContext, mClientId, mMicrositeId,
						mRegistrationType, mIsInitialize, mLocale);

			else if (mRegistrationType.equalsIgnoreCase("PROD"))
				initProdSettings(mContext, mClientId, mMicrositeId,
						mRegistrationType, mIsInitialize, mLocale);

			else if (mRegistrationType.equalsIgnoreCase("DEV"))
				initDevSettings(mContext, mClientId, mMicrositeId,
						mRegistrationType, mIsInitialize, mLocale);
		}
	}

	private void initEvalSettings(Context context, String captureClientId,
			String microSiteId, String registrationType, boolean isintialize,
			String locale) {

		mEvalRegistrationSettings = new EvalRegistrationSettings();
		mRegistrationSettings = mEvalRegistrationSettings;
		mEvalRegistrationSettings.intializeRegistrationSettings(context,
				captureClientId, microSiteId, registrationType, isintialize,
				locale);

	}

	private void initDevSettings(Context context, String captureClientId,
			String microSiteId, String registrationType, boolean isintialize,
			String locale) {

		mDevRegistrationSettings = new DevRegistrationSettings();
		mRegistrationSettings = mDevRegistrationSettings;
		mDevRegistrationSettings.intializeRegistrationSettings(context,
				captureClientId, microSiteId, registrationType, isintialize,
				locale);
	}

	private void initProdSettings(Context context, String captureClientId,
			String microSiteId, String registrationType, boolean isintialize,
			String locale) {

		mProdRegistrationSettings = new ProdRegistrationSettings();
		mRegistrationSettings = mProdRegistrationSettings;
		mProdRegistrationSettings.intializeRegistrationSettings(context,
				captureClientId, microSiteId, registrationType, isintialize,
				locale);
	}

	public RegistrationSettings getRegistrationSettings() {

		return mRegistrationSettings;
	}

}
