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

	public KeyProvisioningHelper(KPSConfigurationInfo kpsConfigurationInfo){
		this.mKpsConfigurationInfo = kpsConfigurationInfo;

		// General configuration
		this.ICPClientPriority = kpsConfigurationInfo.getICPClientPriority();
		this.ICPClientStackSize = kpsConfigurationInfo.getICPClientStackSize();
		this.HTTPTimeout = kpsConfigurationInfo.getHttpTimeout();
		this.FilterString = kpsConfigurationInfo.getFilterString();
		this.MaxNrOfRetry = kpsConfigurationInfo.getMaxNumberOfRetries();

		// Specific configuration
		this.ICPClientBootStrapID = mKpsConfigurationInfo.getBootStrapId();
		this.ICPClientBootStrapKey = mKpsConfigurationInfo.getBootStrapKey();
		this.ICPClientBootStrapProductId = mKpsConfigurationInfo.getProductId();
		this.ICPClientproductVersion = mKpsConfigurationInfo.getProductVersion();
		this.ICPClientproductCountry = mKpsConfigurationInfo.getCountryCode();
		this.ICPClientproductLanguage = mKpsConfigurationInfo.getLanguageCode();

		this.ICPClientComponentCount = mKpsConfigurationInfo.getComponentCount();
		NVMComponentInfo appComponentInfo = new NVMComponentInfo();
		appComponentInfo.componentID = mKpsConfigurationInfo.getComponentId();
		appComponentInfo.componentVersion = mKpsConfigurationInfo.getAppVersion();
		this.ICPClientNVMComponents = new NVMComponentInfo[] {appComponentInfo};

		this.ICPClientdevicePortalURL1 = mKpsConfigurationInfo.getDevicePortUrl();

		ALog.i(ALog.ICPCLIENT, "Created new KeyProvisioningInfo with locale: " + mKpsConfigurationInfo.getLanguageCode());
	}
}
