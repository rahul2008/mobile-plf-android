/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appidentity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.R;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * The type App identity manager.
 */
public class AppIdentityManager implements AppIdentityInterface {

	private AppInfra mAppInfra;
	private Context context;

	private String mAppVersion;
	private String sector;
	private String mAppState;

	private final List<String> mSectorValuesList = Arrays.asList("b2b", "b2c", "b2b_Li", "b2b_HC");
	private final List<String> mServiceDiscoveryEnvList = Arrays.asList("STAGING", "PRODUCTION");
	private final List<String> mAppStateValuesList = Arrays.asList("DEVELOPMENT", "TEST", "STAGING", "ACCEPTANCE", "PRODUCTION");
	private AppConfigurationInterface.AppConfigurationError configError;


	public AppIdentityManager(AppInfra aAppInfra) {
		mAppInfra = aAppInfra;
		context = mAppInfra.getAppInfraContext();
		configError = new AppConfigurationInterface
				.AppConfigurationError();
		// Class shall not presume appInfra to be completely initialized at this point.
		// At any call after the constructor, appInfra can be presumed to be complete.
	}


	private void validateAppVersion() {
		PackageInfo pInfo;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			mAppVersion = String.valueOf(pInfo.versionName);
			//mAppVersion = BuildConfig.VERSION_NAME;
		} catch (PackageManager.NameNotFoundException e) {
			mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "AppIdentity", e.getMessage());
		}
		if (mAppVersion != null && !mAppVersion.isEmpty()) {
			if (!mAppVersion.matches("[0-9]+\\.[0-9]+\\.[0-9]+([_(-].*)?")) {
				throw new IllegalArgumentException("AppVersion should in this format " +
						"\" [0-9]+\\.[0-9]+\\.[0-9]+([_(-].*)?]\" ");
			}
		} else {
			throw new IllegalArgumentException("Appversion cannot be null");
		}
	}


	private void validateAppState() {

		final String defAppState = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
				("appidentity.appState", "appinfra", configError);
		if (defAppState != null) {
			if (defAppState.equalsIgnoreCase("production")) // allow manual override only if static appstate != production
				mAppState = defAppState;
			else {
				final Object dynAppState = mAppInfra.getConfigInterface().getPropertyForKey("appidentity.appState", "appinfra", configError);
				if (dynAppState != null)
					mAppState = dynAppState.toString();
				else
					mAppState = defAppState;
			}
		}

		final Set<String> set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		set.addAll(mAppStateValuesList);

		if (mAppState != null && !mAppState.isEmpty()) {
			if (!set.contains(mAppState)) {
				mAppState = defAppState;
			}
		} else {
			throw new IllegalArgumentException("AppState cannot be empty in AppConfig.json file");
		}

	}

	public void validateServiceDiscoveryEnv(String serviceDiscoveryEnvironment) {

		final Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		set.addAll(mServiceDiscoveryEnvList);
		if (serviceDiscoveryEnvironment != null && !serviceDiscoveryEnvironment.isEmpty()) {
			if (!set.contains(serviceDiscoveryEnvironment)) {
				mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "APPIDENTITY"
						, serviceDiscoveryEnvironment);
				throw new IllegalArgumentException("\"ServiceDiscovery Environment in AppConfig.json " +
						" file must match \" +\n" +
						"\"one of the following values \\n STAGING, \\n PRODUCTION\"");
			}
		} else {
			throw new IllegalArgumentException("ServiceDiscovery Environment cannot be empty" +
					" in AppConfig.json file");
		}
	}

	private void validateSector() {
		sector = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
				("appidentity.sector", "appinfra", configError);

		final Set<String> set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		set.addAll(mSectorValuesList);
		if (sector != null && !sector.isEmpty()) {
			if (!set.contains(sector)) {
				sector = null;
				throw new IllegalArgumentException("\"Sector in AppConfig.json  file" +
						" must match one of the following values\" +\n" +
						" \" \\\\n b2b,\\\\n b2c,\\\\n b2b_Li, \\\\n b2b_HC\"");

			}
		} else {
			throw new IllegalArgumentException("\"App Sector cannot be empty in" +
					" AppConfig.json file\"");
		}

	}

	public void validateMicrositeId(String micrositeId) {

		if (micrositeId != null && !micrositeId.isEmpty()) {
			if (!micrositeId.matches("[a-zA-Z0-9]+")) {
				throw new IllegalArgumentException("micrositeId must not contain special " +
						"charectors in AppConfig.json json file");
			}
		} else {
			throw new IllegalArgumentException("micrositeId cannot be empty in AppConfig.json" +
					"  file");
		}
	}

	@Override
	public String getAppName() {
		return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
	}


	@Override
	public String getAppVersion() {
		validateAppVersion();
		return mAppVersion;
	}

	@Override
	public AppState getAppState() {
		validateAppState();

		AppState mAppStateEnum = null;
		if (mAppState != null) {
			if (mAppState.equalsIgnoreCase("DEVELOPMENT")) {
				mAppStateEnum = AppState.DEVELOPMENT;
			} else if (mAppState.equalsIgnoreCase("TEST")) {
				mAppStateEnum = AppState.TEST;
			} else if (mAppState.equalsIgnoreCase("STAGING")) {
				mAppStateEnum = AppState.STAGING;
			} else if (mAppState.equalsIgnoreCase("ACCEPTANCE")) {
				mAppStateEnum = AppState.ACCEPTANCE;
			} else if (mAppState.equalsIgnoreCase("PRODUCTION")) {
				mAppStateEnum = AppState.PRODUCTION;
			} else {
				throw new IllegalArgumentException("\"App State in AppConfig.json  file must" +
						" match one of the following values \\\\n TEST,\\\\n DEVELOPMENT,\\\\n " +
						"STAGING, \\\\n ACCEPTANCE, \\\\n PRODUCTION\"");
			}
		}

//        if (mAppStateEnum != null) {
//            return mAppStateEnum;
//        }

		return mAppStateEnum;
	}

	@Override
	public String getServiceDiscoveryEnvironment() {
		String serviceDiscoveryEnvironment = null;

		final String defSevicediscoveryEnv = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
				("appidentity.serviceDiscoveryEnvironment", "appinfra", configError);
		final Object dynServiceDiscoveryEnvironment = mAppInfra.getConfigInterface()
				.getPropertyForKey("appidentity.serviceDiscoveryEnvironment", "appinfra",
						configError);
		if (defSevicediscoveryEnv != null) {
			if (defSevicediscoveryEnv.equalsIgnoreCase("production")) // allow manual override only if static appstate != production
				serviceDiscoveryEnvironment = defSevicediscoveryEnv;
			else {
				if (dynServiceDiscoveryEnvironment != null)
					serviceDiscoveryEnvironment = dynServiceDiscoveryEnvironment.toString();
				else
					serviceDiscoveryEnvironment = defSevicediscoveryEnv;
			}
		}

		validateServiceDiscoveryEnv(serviceDiscoveryEnvironment);
		if (serviceDiscoveryEnvironment != null) {
			if (serviceDiscoveryEnvironment.equalsIgnoreCase("STAGING")) {
				serviceDiscoveryEnvironment = "STAGING";
			} else if (serviceDiscoveryEnvironment.equalsIgnoreCase("PRODUCTION")) {
				serviceDiscoveryEnvironment = "PRODUCTION";
			} else {
				throw new IllegalArgumentException("\"ServiceDiscovery environment in AppConfig.json " +
						" file must match \" +\n" +
						"\"one of the following values \\n STAGING, \\n PRODUCTION\"");
			}
		}

		return serviceDiscoveryEnvironment;
	}


	@Override
	public String getLocalizedAppName() {
	    /* Vertical App should have this string defined for all supported language files
	     *  default <string name="localized_commercial_app_name">AppInfra DemoApp localized</string>
         * */
		String mLocalizedAppName = context.getResources().getString(R.string.localized_commercial_app_name);

		return mLocalizedAppName;
	}


	@Override
	public String getMicrositeId() {
		final String micrositeId = (String) mAppInfra.getConfigInterface().getDefaultPropertyForKey
				("appidentity.micrositeId", "appinfra", configError);
		validateMicrositeId(micrositeId);
		return micrositeId;
	}


	@Override
	public String getSector() {
		validateSector();
		return sector;
	}

}
