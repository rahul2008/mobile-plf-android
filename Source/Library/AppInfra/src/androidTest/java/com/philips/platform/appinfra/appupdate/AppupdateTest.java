package com.philips.platform.appinfra.appupdate;


import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.FileUtils;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import org.json.JSONObject;
import org.junit.Before;

import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppupdateTest extends AppInfraInstrumentation {

    private AppInfra mAppInfra;
	private AppUpdateInterface mAppUpdateInterface;
	private AppUpdateManager mAppUpdateManager;

	private final String APPUPDATE_SERVICEID_KEY = "appUpdate.serviceId";
	private final String APPUPDATE_SERVICEID_VALUE = "appinfra.testing.version";
	private final String APPUPDATE_URL = "https://prod.appinfra.testing.service/en_IN/B2C/77000";
	private FileUtils mFileUtils;


	@Before
	public void setUp() throws Exception {
		super.setUp();
        Context mContext = getInstrumentation().getContext();
		assertNotNull(mContext);
		mAppInfra = new AppInfra.Builder().build(mContext);

		// overriding ConfigManager to get Test JSON data, as AppInfra library does not have uApp configuration file
		AppConfigurationInterface mConfigInterface = new AppConfigurationManager(mAppInfra) {
			@Override
			protected JSONObject getMasterConfigFromApp() {
				JSONObject result = null;
				try {
					String testJson = ConfigValues.testJson();
					result = new JSONObject(testJson);
				} catch (Exception e) {
					mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_APP_UPDATE,
							"Error in App update setup");
				}
				return result;
			}
		};
		assertNotNull(mConfigInterface);

		// override service discovery getServiceUrlWithCountryPreference to verify correct service id is being passed to SD
		ServiceDiscoveryInterface mServiceDiscoveryInterface = new ServiceDiscoveryManager(mAppInfra) {
			@Override
			public void getServiceUrlWithCountryPreference(String serviceId, OnGetServiceUrlListener listener) {
				if (serviceId != null && serviceId.equals(APPUPDATE_SERVICEID_KEY)) {
					try {
						URL url = new URL(APPUPDATE_URL);
						listener.onSuccess(url);
					} catch (MalformedURLException e) {
						mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_APP_UPDATE,
								"Error in App version");
					}
				} else {
					listener.onError(OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR, "Invalid ServiceID");
				}
			}
		};
		assertNotNull(mServiceDiscoveryInterface);
		mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).setServiceDiscovery(mServiceDiscoveryInterface).build(mContext);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		mAppUpdateInterface = (AppUpdateInterface) mAppUpdateManager;

		assertNotNull(mAppUpdateInterface);


		mFileUtils = new FileUtils(mAppInfra.getAppInfraContext());
		assertNotNull(mFileUtils);
		deleteFile();
	}

	public void testErrorListener() {
		final AppUpdateInterface.OnRefreshListener refreshListenerMock = mock(AppUpdateInterface.OnRefreshListener.class);
		Response.ErrorListener jsonErrorListener = mAppUpdateManager.getJsonErrorListener(refreshListenerMock);
		NetworkResponse networkResponse = new NetworkResponse(200, "some_response".getBytes(), null, false, 5000);
		VolleyError volleyErrorMock = new VolleyError(networkResponse);
		jsonErrorListener.onErrorResponse(volleyErrorMock);
		String errMsg = " Error Code:" + 200 ;
		verify(refreshListenerMock).onError(AppUpdateInterface.OnRefreshListener.AIAppUpdateRefreshResult.AppUpdate_REFRESH_FAILED, errMsg);
	}

	public void testAppUpdateServiceID() {
		String appUpdateServiceId = mAppUpdateManager.getServiceIdFromAppConfig();
		assertNotNull(appUpdateServiceId);
		assertEquals(APPUPDATE_SERVICEID_VALUE, appUpdateServiceId);
		AppUpdateManager appUpdateManager = mock(AppUpdateManager.class);
		when(appUpdateManager.getServiceIdFromAppConfig()).thenReturn(null);
		assertNull(appUpdateManager.getServiceIdFromAppConfig());
	}

	/**
	 * when autorefresh set to false
	 */
	public void testAutoRefresh() {
		AppConfigurationManager appConfigurationManager = new AppConfigurationManager(mAppInfra);
		AppConfigurationInterface.AppConfigurationError configurationError = new AppConfigurationInterface.AppConfigurationError();
		Object isappUpdate = appConfigurationManager.getPropertyForKey("appUpdate.autoRefresh", "appinfra", configurationError);

		if (isappUpdate != null && !(boolean) isappUpdate) {
			assertNull(mAppUpdateInterface.getMinimumVersion());
			assertNull(mAppUpdateInterface.getMinimumOSverion());
			assertNull(mAppUpdateInterface.getToBeDeprecatedMessage());
			assertNull(mAppUpdateInterface.getUpdateMessage());
			assertNull(mAppUpdateInterface.getDeprecateMessage());
			assertFalse(mAppUpdateInterface.isDeprecated());
			assertFalse(mAppUpdateInterface.isToBeDeprecated());
			assertFalse(mAppUpdateInterface.isUpdateAvailable());
		}
	}


	public void testDeprecated() {
		deleteFile();
		String testMinimumVersionMessage = "test minimum version message";
		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"1001.0.0\",\n" +
				"      \"deprecatedVersion\":\"1002.0.2\",\n" +
				"      \"deprecationDate\":\"2019-07-12\",\n" +
				"      \"currentVersion\":\"1003.1.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertNotNull(mAppUpdateManager.getAppVersion());
		assertTrue(mAppUpdateManager.isDeprecated());
		assertEquals(testMinimumVersionMessage, mAppUpdateInterface.getDeprecateMessage());
	}

	public void testRefreshAppUpdateWithNullMinimumVersion() {
		deleteFile();

		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":null,\n" +
				"      \"deprecatedVersion\":\"3.0.0-SNAPSHOT.1\",\n" +
				"      \"deprecationDate\":\"2019-07-12\",\n" +
				"      \"currentVersion\":\"300.1.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertNull(mAppUpdateManager.getMinimumVersion());
	}

	public void testAppUpdateWithrareMinimumVersion() {
		deleteFile();

		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":0.5.0-SNAPSHOT.1,\n" +
				"      \"deprecatedVersion\":\"3.0.0-SNAPSHOT.1\",\n" +
				"      \"deprecationDate\":\"2019-07-12\",\n" +
				"      \"currentVersion\":\"300.1.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertNotNull(mAppUpdateManager.getMinimumVersion());
	}

	public void testDeprecatedForFalse() {
		deleteFile();
		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"1.6.0\",\n" +
				"      \"deprecatedVersion\":\"1002.0.2\",\n" +
				"      \"deprecationDate\":\"2019-07-12\",\n" +
				"      \"currentVersion\":\"1003.1.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertFalse(mAppUpdateManager.isDeprecated());
	}

	//test deprecated since tobedeprecatedDate is over (deprecated date set to past date ).
	public void testDeprecatedAfterTobeDeprecateddate() {
		deleteFile();
		String testMinimumVersionMessage = "test minimum version message";

		final String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"0.0.0\",\n" +
				"      \"deprecatedVersion\":\"1.6.0\",\n" +
				"      \"deprecationDate\":\"2010-05-12\",\n" +
				"      \"currentVersion\":\"1003.1.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertTrue(mAppUpdateManager.isDeprecated());
		assertEquals(testMinimumVersionMessage, mAppUpdateInterface.getDeprecateMessage());
	}


	public void testDeprecatedAfterTobeDeprecateddateWithInvalidDate() {
		deleteFile();
		String testMinimumVersionMessage = "test minimum version message";


		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"1.6.0\",\n" +
				"      \"deprecatedVersion\":\"1002.0.2\",\n" +
				"      \"deprecationDate\":\"testdata\",\n" +
				"      \"currentVersion\":\"1003.1.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertFalse(mAppUpdateManager.isDeprecated());
		assertEquals(testMinimumVersionMessage, mAppUpdateInterface.getDeprecateMessage());
	}

	//test deprecated since tobedeprecatedDate is not over (deprecated date set to future date)
	public void testDeprecatedBeforeTobeDeprecateddate() {
		deleteFile();
		String testMinimumVersionMessage = "test minimum version message";

		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"1.6.0\",\n" +
				"      \"deprecatedVersion\":\"1002.0.2\",\n" +
				"      \"deprecationDate\":\"2020-07-12\",\n" +
				"      \"currentVersion\":\"1003.1.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertFalse(mAppUpdateManager.isDeprecated());
		assertEquals(testMinimumVersionMessage, mAppUpdateInterface.getDeprecateMessage());
	}

	public void testTobeDeprecatedTrue() {
		deleteFile();
		String testDeprecatedMessage = "test deprecated version message";


		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"0.0.0\",\n" +
				"      \"deprecatedVersion\":\"2002.0.2\",\n" +
				"      \"deprecationDate\":\"2020-07-12\",\n" +
				"      \"currentVersion\":\"1003.1.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertFalse(mAppUpdateManager.isDeprecated());
		assertTrue(mAppUpdateManager.isToBeDeprecated());
		assertEquals(testDeprecatedMessage, mAppUpdateInterface.getToBeDeprecatedMessage());
	}

	public void testTobeDeprecatedTrueForSameVersion() {
		deleteFile();
		String testDeprecatedMessage = "test deprecated version message";

		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"0.0.0\",\n" +
				"      \"deprecatedVersion\":\"1.6.0\",\n" +
				"      \"deprecationDate\":\"2020-07-12\",\n" +
				"      \"currentVersion\":\"1003.1.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"test deprecated version message\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertFalse(mAppUpdateManager.isDeprecated());
		assertTrue(mAppUpdateManager.isToBeDeprecated());
		assertEquals(testDeprecatedMessage, mAppUpdateManager.getToBeDeprecatedMessage());
	}

	public void testTobeDeprecatedFalse() {
		deleteFile();
		String testDeprecatedMessage = "test deprecated version message";


		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"0.0.0\",\n" +
				"      \"deprecatedVersion\":\"0.1.6\",\n" +
				"      \"deprecationDate\":\"2020-07-12\",\n" +
				"      \"currentVersion\":\"1003.1.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"test deprecated version message\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertFalse(mAppUpdateManager.isDeprecated());
		assertFalse(mAppUpdateManager.isToBeDeprecated());
		assertEquals(testDeprecatedMessage, mAppUpdateManager.getToBeDeprecatedMessage());
	}

	public void testIsUpdateAvailable() {
		deleteFile();
		String testUpdateAvailablemsg = "test update available message";
		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"0.0.0\",\n" +
				"      \"deprecatedVersion\":\"0.0.0\",\n" +
				"      \"deprecationDate\":\"2017-08-12\",\n" +
				"      \"currentVersion\":\"1003.1.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"test deprecated version message\",\n" +
				"      \"currentVersionMessage\":\"test update available message\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertTrue(mAppUpdateManager.isUpdateAvailable());
		assertEquals(testUpdateAvailablemsg, mAppUpdateManager.getUpdateMessage());
	}

	public void testISUpdateAvailableForSameVersion() {
		deleteFile();
		String testUpdateAvailablemsg = "test update available message";
		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"0.0.0\",\n" +
				"      \"deprecatedVersion\":\"0.0.0\",\n" +
				"      \"deprecationDate\":\"2017-08-12\",\n" +
				"      \"currentVersion\":\"1.6.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"test deprecated version message\",\n" +
				"      \"currentVersionMessage\":\"test update available message\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@Override
			protected String getAppVersion() {
				return "1.6.0";
			}
		};
		assertFalse(mAppUpdateManager.isUpdateAvailable());
		assertEquals(testUpdateAvailablemsg, mAppUpdateManager.getUpdateMessage());
	}


	public void testgetOSVersion() {
		deleteFile();

		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"0.0.0\",\n" +
				"      \"deprecatedVersion\":\"0.0.0\",\n" +
				"      \"deprecationDate\":\"2017-08-12\",\n" +
				"      \"currentVersion\":\"1.6.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"test deprecated version message\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertEquals("15", mAppUpdateInterface.getMinimumOSverion());
	}

	public void testgetMinimumVersion() {
		deleteFile();

		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"1.2.5\",\n" +
				"      \"deprecatedVersion\":\"0.0.0\",\n" +
				"      \"deprecationDate\":\"2017-08-12\",\n" +
				"      \"currentVersion\":\"1.6.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"test deprecated version message\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertEquals("1.2.5", mAppUpdateInterface.getMinimumVersion());
	}

	public void testgetDeprecatedDate() {
		deleteFile();
		String testResponse = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"1.2.5\",\n" +
				"      \"deprecatedVersion\":\"0.0.0\",\n" +
				"      \"deprecationDate\":\"2017-08-12\",\n" +
				"      \"currentVersion\":\"1.6.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"test minimum version message\",\n" +
				"      \"deprecatedVersionMessage\":\"test deprecated version message\",\n" +
				"      \"currentVersionMessage\":\"A new version of the App is now available.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
	}

	public void saveResponse(String response) {
		mFileUtils.saveFile(response, "downloadedappupdate.json", "/AppInfra/AIAppupdate");
	}

	public void deleteFile() {
		mFileUtils.deleteFile("downloadedappupdate.json", "/AppInfra/AIAppupdate");
	}

}
