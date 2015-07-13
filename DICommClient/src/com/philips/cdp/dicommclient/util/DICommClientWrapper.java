/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.util;

import android.content.Context;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceDatabase;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;

public class DICommClientWrapper {

	private static Context mContext;

	public static synchronized void initializeDICommLibrary(Context context) {
		mContext = context;
	}

	public static synchronized Context getContext() {
		return mContext;
	}
	
	public static <U extends DICommAppliance> DiscoveryManager<U> getDiscoveryManager(DICommApplianceFactory<U> applianceFactory, DICommApplianceDatabase<U> applianceDatabase, CppController cppController){	
		if( mContext == null){
			throw new RuntimeException("Please call initializeDICommLibrary() before you get discoverymanager");
		}
		if(applianceDatabase!=null){
			return DiscoveryManager.createSharedInstance(mContext, cppController, applianceFactory, applianceDatabase);
		}else{
			return DiscoveryManager.createSharedInstance(mContext, cppController, applianceFactory);
		}	
		
	}
}
