/*
package com.philips.cl.di.reg.settings;

import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class JanrainSettings {

	private static Context mContext;

	public static boolean mJanrainIntialized = false;
	
	private static JanrainSettings mJanrainSettings = null;
	private EvalJanrainSettings mEvalJanrainSettings;
	private DevJanrainSettings mDevJanrainSettings;
	private ProdJanrainSettings mProdJanrainSettings;
	private RegistrationHelper mRegistrationHelper;

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

	*//**
     * 
     *//*
	public JanrainSettings(Context context) {

		mContext = context.getApplicationContext();
		*//** commented to restrict registration process *//*
		IntentFilter flowFilter = new IntentFilter(Jump.JR_DOWNLOAD_FLOW_SUCCESS);
		flowFilter.addAction(Jump.JR_FAILED_TO_DOWNLOAD_FLOW);
		LocalBroadcastManager.getInstance(context).registerReceiver(janrainStatusReceiver,
		        flowFilter);
	}
	
	public static JanrainSettings getInstance() {
		if (mJanrainSettings == null) {
			mJanrainSettings = new JanrainSettings(mContext);
		}
		return mJanrainSettings;
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
					System.out.println("success");

					EventHelper.getInstance()
					        .notifyEventOccurred(RegConstants.JANRAIN_INIT_SUCCESS);
				} else if (Jump.JR_FAILED_TO_DOWNLOAD_FLOW.equalsIgnoreCase(intent.getAction())
				        && (extras != null)) {

					System.out.println("failed in reciver");

					EventHelper.getInstance()
					        .notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);

					mJanrainIntialized = false;
				}
			}
		}
	};

	*//**
	 * Initialize Janrain
	 * 
	 * @param isInitialized
	 *            true for initialize and false for reinitialize Janrain
	 *//*
	public void intializeJanrain(Janrain isInitialized) {
		mJanrainIntialized = false;
		NetworkUtility.getInstance().checkIsOnline(mContext);
		if (NetworkUtility.getInstance().isOnline()) {
			
			//JanrainSettings mJanrainHelper = JanrainSettings.getInstance();
			
			initEvalSettings(mContext, RegConstants.EVAL_CLIENT_Id, RegConstants.MICROSITE_ID,
			        RegConstants.REGISTRATION_USE_EVAL, isInitialized.getValue(), Locale.getDefault()
	                .toString());

			JanrainConfigurationSettings user = JanrainConfigurationSettings.getInstance();
			user.init(mContext, RegConstants.EVAL_CLIENT_Id, RegConstants.MICROSITE_ID,
			        RegConstants.REGISTRATION_USE_EVAL, isInitialized.getValue(), Locale.getDefault()
			                .toString());

			// user.init(getApplicationContext(), AppConstants.PROD_CLIENT_ID,
			// AppConstants.MICROSITE_ID, AppConstants.REGISTRATION_USE_PROD,
			// isInitialized,
			// LocaleUtil.getAppLocale(getApplicationContext()));
		}
	}
	
	public void initEvalSettings(Context context, String captureClientId,
			String microSiteId, String registrationType, boolean isintialize,
			String locale){
		
		mEvalJanrainSettings = new EvalJanrainSettings();
		mEvalJanrainSettings.intializeJanrainSettings(context, captureClientId, microSiteId, registrationType, isintialize, locale);
		mRegistrationHelper = mEvalJanrainSettings;
		
	}
	
	public void initDevSettings(Context context, String captureClientId,
			String microSiteId, String registrationType, boolean isintialize,
			String locale){
		
		mDevJanrainSettings = new DevJanrainSettings();
		mRegistrationHelper = mDevJanrainSettings;
		mDevJanrainSettings.intializeJanrainSettings(context, captureClientId, microSiteId, registrationType, isintialize, locale);
	}
	
	public void initProdSettings(Context context, String captureClientId,
			String microSiteId, String registrationType, boolean isintialize,
			String locale){
		
		mProdJanrainSettings = new ProdJanrainSettings();
		mRegistrationHelper = mProdJanrainSettings;
		mProdJanrainSettings.intializeJanrainSettings(context, captureClientId, microSiteId, registrationType, isintialize, locale);
	}
	
	public RegistrationHelper getRegistrationHelper() {
		
		return mRegistrationHelper;
	}

}
*/