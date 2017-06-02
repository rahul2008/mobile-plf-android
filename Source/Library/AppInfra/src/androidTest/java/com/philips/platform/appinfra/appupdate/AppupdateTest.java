package com.philips.platform.appinfra.appupdate;


import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.FileUtils;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Mockito.when;

public class AppupdateTest extends MockitoTestCase {

	private Context mContext;
	private AppInfra mAppInfra;
	private AppUpdateInterface mAppUpdateInterface;
	private AppConfigurationInterface mConfigInterface = null;
	private ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
	private AppUpdateManager mAppUpdateManager;

	private final String APPUPDATE_SERVICEID_KEY = "appUpdate.serviceId";
	private final String APPUPDATE_SERVICEID_VALUE = "appinfra.testing.version";
	private final String APPUPDATE_URL = "https://prod.appinfra.testing.service/en_IN/B2C/77000";
	private FileUtils mFileUtils;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mContext = getInstrumentation().getContext();
		assertNotNull(mContext);

		// overriding ConfigManager to get Test JSON data, as AppInfra library does not have uApp configuration file
		mConfigInterface = new AppConfigurationManager(mAppInfra) {
			@Override
			protected JSONObject getMasterConfigFromApp() {
				JSONObject result = null;
				try {
					String testJson = ConfigValues.testJson();
					result = new JSONObject(testJson);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}
		};
		assertNotNull(mConfigInterface);

		// override service discovery getServiceUrlWithCountryPreference to verify correct service id is being passed to SD
		mServiceDiscoveryInterface = new ServiceDiscoveryManager(mAppInfra) {
			@Override
			public void getServiceUrlWithCountryPreference(String serviceId, OnGetServiceUrlListener listener) {
				if (serviceId != null && serviceId.equals(APPUPDATE_SERVICEID_KEY)) {
					try {
						URL url = new URL(APPUPDATE_URL);
						listener.onSuccess(url);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				} else {
					listener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR, "Invalid ServiceID");
				}
			}
		};
		assertNotNull(mServiceDiscoveryInterface);

		mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).setServiceDiscovery(mServiceDiscoveryInterface).build(mContext);
		mAppUpdateInterface = mAppInfra.getAppUpdate();
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertNotNull(mAppUpdateInterface);

		mFileUtils = new FileUtils(mAppInfra.getAppInfraContext());
		assertNotNull(mFileUtils);
	}

	public void testAppUpdateServiceID() {
		String appUpdateServiceId = mAppUpdateManager.getServiceIdFromAppConfig();
		assertNotNull(appUpdateServiceId);
		assertEquals(APPUPDATE_SERVICEID_VALUE ,appUpdateServiceId);
		when(mAppUpdateManager.getServiceIdFromAppConfig()).thenReturn(null);
		assertNull(mAppUpdateManager.getServiceIdFromAppConfig());
	}

}
