/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import java.util.Random;

import android.content.Context;

import com.philips.cdp.dicommclient.BuildConfig;
import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceDatabase;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;

public class DICommClientWrapper {

	private static Context mContext;
	private static String mAppId;

	public static synchronized <U extends DICommAppliance> void initializeDICommLibrary(Context context,DICommApplianceFactory<U> applianceFactory, DICommApplianceDatabase<U> applianceDatabase, CppController cppController) {
		mContext = context;
		mAppId = generateTemporaryAppId();
		if( mContext == null){
			throw new RuntimeException("Please call initializeDICommLibrary() before you get discoverymanager");
		}
		if(applianceDatabase!=null){
			DiscoveryManager.createSharedInstance(mContext, cppController, applianceFactory, applianceDatabase);
		}else{
			DiscoveryManager.createSharedInstance(mContext, cppController, applianceFactory);
		}	
	}

	public static synchronized Context getContext() {
		return mContext;
	}
	
	private static String generateTemporaryAppId() {
		return String.format("deadbeef%08x",new Random().nextInt());
	}
	
	// TODO: DIComm Refactor to find the ideal place for keeping app id
	public static String getAppId(){
		return mAppId;
	}

    public static String getDICommClientLibVersion(){
        return BuildConfig.VERSION_NAME;
    }
	
}
