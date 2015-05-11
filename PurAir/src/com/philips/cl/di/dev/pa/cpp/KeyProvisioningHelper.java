package com.philips.cl.di.dev.pa.cpp;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.cpp.KPSConfigurationInfo;
import com.philips.icpinterface.configuration.KeyProvisioningConfiguration;
import com.philips.icpinterface.data.NVMComponentInfo;

/**
 *This class provide interface to set ICP Client configuration.
 *configuration parameters set by the application.
 *Set the necessary parameters, as per the request.
 */
public class KeyProvisioningHelper extends KeyProvisioningConfiguration{
	private final KPSConfigurationInfo mKpsConfigurationInfo;
	
	public KeyProvisioningHelper(KPSConfigurationInfo kpsConfigurationInfo)
	{
		// TODO:DICOMM REFACTOR, check if any of the below needs to be configurable from app
		ICPClientPriority = 20;
		ICPClientStackSize = 131072;
		HTTPTimeout = 30;
		FilterString = "TEST";
		MaxNrOfRetry = 2;

		mKpsConfigurationInfo = kpsConfigurationInfo;
		
		setNVMConfigParams();
	}

	public void setNVMConfigParams() {		
		//TODO - Obscure the below constants.
		this.ICPClientBootStrapID = mKpsConfigurationInfo.getBootStrapId();
		this.ICPClientBootStrapKey = mKpsConfigurationInfo.getBootStrapKey();
		this.ICPClientBootStrapProductId = mKpsConfigurationInfo.getProductId();
		this.ICPClientproductVersion = mKpsConfigurationInfo.getProductVersion();
		this.ICPClientproductCountry = mKpsConfigurationInfo.getCountryCode();
		this.ICPClientproductLanguage = mKpsConfigurationInfo.getLanguageCode();
		ALog.i(ALog.ICPCLIENT, "setNVMConfigParams, getCountryLocale: "+mKpsConfigurationInfo.getLanguageCode());

		this.ICPClientComponentCount = mKpsConfigurationInfo.getComponentCount();
		NVMComponentInfo appComponentInfo = new NVMComponentInfo();
		appComponentInfo.componentID = mKpsConfigurationInfo.getComponentId();
		appComponentInfo.componentVersion = mKpsConfigurationInfo.getAppVersion();
		this.ICPClientNVMComponents = new NVMComponentInfo[] {appComponentInfo};

		this.ICPClientdevicePortalURL1 = mKpsConfigurationInfo.getDevicePortUrl();
	}

	public KPSConfigurationInfo getKpsConfigurationInfo() {
		return mKpsConfigurationInfo;
	}

}
