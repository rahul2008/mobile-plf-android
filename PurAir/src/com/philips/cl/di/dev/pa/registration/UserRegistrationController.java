package com.philips.cl.di.dev.pa.registration;

import java.util.Locale;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.errormapping.Error;
import com.philips.cl.di.reg.settings.JanrainConfigurationSettings;

public class UserRegistrationController implements NetworkStateListener {

	private static UserRegistrationController smInstance;

	 private static final String CAPTURE_CLIENT_ID_EVAL = "yakfnsg2hph355fhga2mcac5ywn6fgzt";
//	private static final String CAPTURE_CLIENT_ID_PROD = "ypzud56u92tqa7xhfqry6np9f5zpr3bu";
//	private static final String CAPTURE_CLIENT_ID_DEV = "4gc24tp5rr84ftrfu3nn4hnrkwuyqxyp";

	private static final String MICRO_SITE_ID = "81374";

	 private static final String REGISTRATION_TYPE_EVAL = "REGISTRATION_USE_EVAL";
//	private static final String REGISTRATION_TYPE_PROD = "REGISTRATION_USE_PRODUCTION";
//	private static final String REGISTRATION_TYPE_DEV = "REGISTRATION_USE_DEVICE";

	private UserRegistrationController() {
		// ALog.i(ALog.USER_REGISTRATION,
		// "UserRegistrationController<init> DONE");
		NetworkReceiver.getInstance().addNetworkStateListener(this);

	}

	public static UserRegistrationController getInstance() {
		if (smInstance == null) {
			smInstance = new UserRegistrationController();
		}
		return smInstance;
	}

	public boolean isUserLoggedIn() {
		User user = new User(PurAirApplication.getAppContext());
		DIUserProfile profile = user.getUserInstance(PurAirApplication.getAppContext());

		return (null != profile);
	}

	public Error getErrorEnum(int errorCode) {
		if (errorCode == 2)
			return Error.INVALID_PARAM;
		if (errorCode == 3)
			return Error.AUTHENTICATION_CANCELED_BY_USER;
		if (errorCode == 4)
			return Error.INVALID_EMAILID;
		if (errorCode == 5)
			return Error.EMAIL_ADDRESS_IN_USE;
		if (errorCode == 6)
			return Error.NO_NETWORK_CONNECTION;
		if (errorCode == 7)
			return Error.CONFIGURATION_FAILED;
		if (errorCode == 8)
			return Error.AUTHENTICATION_FAILED;
		if (errorCode == 9)
			return Error.INVALID_PASSWORD;
		if (errorCode == 10)
			return Error.INVALID_USERNAME_OR_PASSWORD;
		if (errorCode == 11)
			return Error.ACCOUNT_DOESNOT_EXIST;
		if (errorCode == 12)
			return Error.TWO_STEP_ERROR;
		if (errorCode == 13)
			return Error.MERGE_FLOW_ERROR;
		if (errorCode == 14)
			return Error.EMAIL_ALREADY_EXIST;
		return Error.GENERIC_ERROR;
	}

	private boolean isJRInitilised = false;

	@Override
	public void onConnected(String ssid) {
		if (!isJRInitilised && !isUserLoggedIn()) {
			JanrainConfigurationSettings config = JanrainConfigurationSettings.getInstance();
			config.init(PurAirApplication.getAppContext(), CAPTURE_CLIENT_ID_EVAL, MICRO_SITE_ID, REGISTRATION_TYPE_EVAL, true, Locale.getDefault().toString());
			isJRInitilised = true;
		}
	}

	@Override
	public void onDisconnected() {

	}
}
