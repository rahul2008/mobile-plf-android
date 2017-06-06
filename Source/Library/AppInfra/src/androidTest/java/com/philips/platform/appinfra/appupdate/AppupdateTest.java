package com.philips.platform.appinfra.appupdate;


import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Response;
import com.google.gson.Gson;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.FileUtils;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;

import com.philips.platform.appinfra.appupdate.model.AppUpdateModel;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
		assertEquals(APPUPDATE_SERVICEID_VALUE, appUpdateServiceId);
		when(mAppUpdateManager.getServiceIdFromAppConfig()).thenReturn(null);
		assertNull(mAppUpdateManager.getServiceIdFromAppConfig());
	}

	/**
	 * when autorefresh set to false
	 */
	public void testAutoRefresh() {
		AppConfigurationManager appConfigurationManager = new AppConfigurationManager(mAppInfra);
		Object isappUpdateRq = AppInfra.getAutoRefreshValue(appConfigurationManager);
		when(isappUpdateRq).thenReturn(false);
		//AppUpdateInterface.OnRefreshListener refreshListener = mock(AppUpdateInterface.OnRefreshListener.class);
		//verify(refreshListener).onError(AppUpdateInterface.OnRefreshListener.AIAppUpdateRefreshResult.AppUpdate_REFRESH_FAILED ,"");
		assertNull(mAppUpdateInterface.getMinimumVersion());
		assertNull(mAppUpdateInterface.getMinimumOSverion());
		assertNull(mAppUpdateInterface.getToBeDeprecatedMessage());
		assertNull(mAppUpdateInterface.getUpdateMessage());
		assertNull(mAppUpdateInterface.getDeprecateMessage());
		assertFalse(mAppUpdateInterface.isDeprecated());
		assertFalse(mAppUpdateInterface.isToBeDeprecated());
		assertFalse(mAppUpdateInterface.isUpdateAvailable());
	}

	public void testAutoRefreshwithTrueVal() {
		AppConfigurationManager appConfigurationManager = new AppConfigurationManager(mAppInfra);
		Object isappUpdateRq = AppInfra.getAutoRefreshValue(appConfigurationManager);
		when(isappUpdateRq).thenReturn(true);
		assertNotNull(mAppUpdateInterface.getMinimumVersion());
		assertNull(mAppUpdateInterface.getMinimumOSverion());
		assertNull(mAppUpdateInterface.getToBeDeprecatedMessage());
		assertNull(mAppUpdateInterface.getUpdateMessage());
		assertNull(mAppUpdateInterface.getDeprecateMessage());
	}

	public void testDeprecated() {
		deleteFile();
		String testMinimumVersionMessage = "test minimum version message";
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"1001.0.0\",\n" +
				"deprecatedVersion: \"1002.0.2\",\n" +
				"deprecationDate: \"2019-07-12\",\n" +
				"currentVersion: \"1003.1.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"A new version of the App is now available.\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertTrue(mAppUpdateInterface.isDeprecated());
		assertEquals(testMinimumVersionMessage, mAppUpdateInterface.getDeprecateMessage());
	}

	public void testRefreshAppUpdateWithNullMinimumVersion() {
		deleteFile();
		final String testResponse = "android: {\n" +
				"version: {\n" +
				"minimumVersion: null,\n" +
				"deprecatedVersion: \"3.0.0-SNAPSHOT.1\",\n" +
				"deprecationDate: \"2019-07-12\",\n" +
				"currentVersion: \"300.1.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"The current version of the app is outdated. Please update the app.\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"A new version of the App is now available.\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}\n" +
				"}";
//		final Response.Listener<JsonObject> res = new Response.Listener<JsonObject>() {
//			@Override
//			public void onResponse(JsonObject response) {
//
//			}
//		};
//
//		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
//			@NonNull
//			@Override
//			protected Response.Listener<JsonObject> getJsonResponseListener(OnRefreshListener refreshListener) {
//				return res;
//			}
//		};
//		try {
//			when(res).thenReturn(new Response.Listener<JsonObject>() {
//				@Override
//				public void onResponse(JsonObject response) {
//
//				}
//			});
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

		AppUpdateInterface.OnRefreshListener onRefreshListener = Mockito.mock(AppUpdateInterface.OnRefreshListener.class);
		final Response.Listener responseListener = Mockito.mock(Response.Listener.class);
		final Response.ErrorListener errorListener = Mockito.mock(Response.ErrorListener.class);

//		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
//			@NonNull
//			@Override
//			protected Response.Listener<JsonObject> getJsonResponseListener(OnRefreshListener refreshListener) {
//				return responseListener;
//			}
//
//			@NonNull
//			@Override
//			protected Response.ErrorListener getJsonErrorListener(OnRefreshListener refreshListener) {
//				return errorListener;
//			}
//		};
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertNull(mAppUpdateInterface.getMinimumVersion());
	}

	public void testRefreshAppUpdateWithrareMinimumVersion() {
		deleteFile();
		final String testResponse = "android: {\n" +
				"version: {\n" +
				"minimumVersion:\"0.5.0-SNAPSHOT.1\",\n" +
				"deprecatedVersion: \"3.0.0\",\n" +
				"deprecationDate: \"2019-07-12\",\n" +
				"currentVersion: \"300.1.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"The current version of the app is outdated. Please update the app.\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"A new version of the App is now available.\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}\n" +
				"}";

	}

	/* refresh with null service url */
	public void testRefreshWithoutServiceUrl() {
		final AppUpdateInterface.OnRefreshListener onRefreshListener = Mockito.mock(AppUpdateInterface.OnRefreshListener.class);
		final ServiceDiscoveryInterface.OnGetServiceUrlListener onGetServiceUrlListener =
				Mockito.mock(ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@NonNull
			@Override
			protected ServiceDiscoveryInterface.OnGetServiceUrlListener getServiceDiscoveryListener
					(OnRefreshListener refreshListener) {
				return onGetServiceUrlListener;
			}
		};
		mAppUpdateManager.refresh(onRefreshListener);

		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				((ServiceDiscoveryInterface.OnGetServiceUrlListener) invocation.getArguments()[0]).onSuccess(null);
				return null;
			}
		}).when(mAppUpdateManager.getServiceDiscoveryListener(onRefreshListener));

		verify(onRefreshListener).onError(AppUpdateInterface.OnRefreshListener.AIAppUpdateRefreshResult.AppUpdate_REFRESH_FAILED,
				"NO SERVICE LOCALE");
	}


	public void testRefreshWithoutAppUpdateServiceIDKey() {
		final AppUpdateInterface.OnRefreshListener onRefreshListener = Mockito.mock(AppUpdateInterface.OnRefreshListener.class);
		final ServiceDiscoveryInterface.OnGetServiceUrlListener onGetServiceUrlListener =
				Mockito.mock(ServiceDiscoveryInterface.OnGetServiceUrlListener.class);

		mAppUpdateManager = new AppUpdateManager(mAppInfra) {
			@NonNull
			@Override
			protected ServiceDiscoveryInterface.OnGetServiceUrlListener getServiceDiscoveryListener
					(OnRefreshListener refreshListener) {
				return onGetServiceUrlListener;
			}
		};
		mAppUpdateManager.refresh(onRefreshListener);
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				((ServiceDiscoveryInterface.OnGetServiceUrlListener) invocation.getArguments()[0]).onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR,
						"No SERVICE ID ERROR");
				return null;
			}
		}).when(mAppUpdateManager.getServiceDiscoveryListener(onRefreshListener));
		when(mAppUpdateManager.getServiceIdFromAppConfig()).thenReturn(null);
		Mockito.verify(onGetServiceUrlListener).onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR, "No Loacle error");
		Mockito.verify(onRefreshListener).onError(AppUpdateInterface.OnRefreshListener.AIAppUpdateRefreshResult.AppUpdate_REFRESH_FAILED, "Invalid ServiceID");
	}



	public void testDeprecatedForFalse() {
		deleteFile();
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"1.6.0\",\n" +
				"deprecatedVersion: \"1002.0.2\",\n" +
				"deprecationDate: \"2019-07-12\",\n" +
				"currentVersion: \"1003.1.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"A new version of the App is now available.\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";

		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertFalse(mAppUpdateInterface.isDeprecated());
	}

	//test deprecated since tobedeprecatedDate is over (deprecated date set to past date ).
	public void testDeprecatedAfterTobeDeprecateddate() {
		deleteFile();
		String testMinimumVersionMessage = "test minimum version message";
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"1.6.0\",\n" +
				"deprecatedVersion: \"1002.0.2\",\n" +
				"deprecationDate: \"2016-07-12\",\n" +
				"currentVersion: \"1003.1.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"A new version of the App is now available.\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertTrue(mAppUpdateInterface.isDeprecated());
		assertEquals(testMinimumVersionMessage, mAppUpdateInterface.getDeprecateMessage());
	}


	public void testDeprecatedAfterTobeDeprecateddateWithInvalidDate() {
		deleteFile();
		String testMinimumVersionMessage = "test minimum version message";
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"1.6.0\",\n" +
				"deprecatedVersion: \"1002.0.2\",\n" +
				"deprecationDate: \"testdata\",\n" +
				"currentVersion: \"1003.1.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"A new version of the App is now available.\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";

		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertFalse(mAppUpdateInterface.isDeprecated());
		assertEquals(testMinimumVersionMessage, mAppUpdateInterface.getDeprecateMessage());
	}

	//test deprecated since tobedeprecatedDate is not over (deprecated date set to future date)
	public void testDeprecatedBeforeTobeDeprecateddate() {
		deleteFile();
		String testMinimumVersionMessage = "test minimum version message";
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"1.6.0\",\n" +
				"deprecatedVersion: \"1002.0.2\",\n" +
				"deprecationDate: \"2020-07-12\",\n" +
				"currentVersion: \"1003.1.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"A new version of the App is now available.\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertFalse(mAppUpdateInterface.isDeprecated());
		assertEquals(testMinimumVersionMessage, mAppUpdateInterface.getDeprecateMessage());
	}

	public  void testTobeDeprecatedTrue() {
		deleteFile();
		String testDeprecatedMessage = "test deprecated version message";
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"0.0.0\",\n" +
				"deprecatedVersion: \"2002.0.2\",\n" +
				"deprecationDate: \"2020-07-12\",\n" +
				"currentVersion: \"1003.1.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"test deprecated version message\",\n" +
				"currentVersionMessage: \"A new version of the App is now available.\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertFalse(mAppUpdateInterface.isDeprecated());
		assertTrue(mAppUpdateInterface.isToBeDeprecated());
		assertEquals(testDeprecatedMessage, mAppUpdateInterface.getToBeDeprecatedMessage());
	}

	public void testTobeDeprecatedTrueForSameVersion() {
		deleteFile();
		String testDeprecatedMessage = "test deprecated version message";
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"0.0.0\",\n" +
				"deprecatedVersion: \"1.6.0\",\n" +
				"deprecationDate: \"2020-07-12\",\n" +
				"currentVersion: \"1003.1.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"test deprecated version message\",\n" +
				"currentVersionMessage: \"A new version of the App is now available.\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertFalse(mAppUpdateInterface.isDeprecated());
		assertTrue(mAppUpdateInterface.isToBeDeprecated());
		assertEquals(testDeprecatedMessage, mAppUpdateInterface.getToBeDeprecatedMessage());
	}

	public void testTobeDeprecatedFalse() {
		deleteFile();
		String testDeprecatedMessage = "test deprecated version message";
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"0.0.0\",\n" +
				"deprecatedVersion: \"0.1.6\",\n" +
				"deprecationDate: \"2020-07-12\",\n" +
				"currentVersion: \"1003.1.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"test deprecated version message\",\n" +
				"currentVersionMessage: \"A new version of the App is now available.\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertFalse(mAppUpdateInterface.isDeprecated());
		assertFalse(mAppUpdateInterface.isToBeDeprecated());
		assertEquals(testDeprecatedMessage, mAppUpdateInterface.getToBeDeprecatedMessage());
	}

	public void testIsUpdateAvailable() {
		deleteFile();
		String testUpdateAvailablemsg = "test update available message";
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"0.0.0\",\n" +
				"deprecatedVersion: \"0.0.0\",\n" +
				"deprecationDate: \"2017-08-12\",\n" +
				"currentVersion: \"1003.1.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"test update available message\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertTrue(mAppUpdateInterface.isUpdateAvailable());
		assertEquals(testUpdateAvailablemsg, mAppUpdateInterface.getUpdateMessage());
	}

	public void testISUpdateAvailableForSameVersion() {
		deleteFile();
		String testUpdateAvailablemsg = "test update available message";
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"0.0.0\",\n" +
				"deprecatedVersion: \"0.0.0\",\n" +
				"deprecationDate: \"2017-08-12\",\n" +
				"currentVersion: \"1.6.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"test update available message\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertFalse(mAppUpdateInterface.isUpdateAvailable());
		assertEquals(testUpdateAvailablemsg, mAppUpdateInterface.getUpdateMessage());
	}


	public void testgetOSVersion() {
		deleteFile();
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"0.0.0\",\n" +
				"deprecatedVersion: \"0.0.0\",\n" +
				"deprecationDate: \"2017-08-12\",\n" +
				"currentVersion: \"1.6.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"test update available message\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertEquals("15", mAppUpdateInterface.getMinimumOSverion());
	}

	public void testgetMinimumVersion() {
		deleteFile();
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"1.2.5\",\n" +
				"deprecatedVersion: \"0.0.0\",\n" +
				"deprecationDate: \"2017-08-12\",\n" +
				"currentVersion: \"1.6.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"test update available message\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
		assertEquals("1.2.3", mAppUpdateInterface.getMinimumVersion());
	}

	public void testgetDeprecatedDate() {
		deleteFile();
		String testResponse = "{\n" +
				"version: {\n" +
				"minimumVersion: \"1.2.5\",\n" +
				"deprecatedVersion: \"0.0.0\",\n" +
				"deprecationDate: \"2017-08-12\",\n" +
				"currentVersion: \"1.6.0\"\n" +
				"},\n" +
				"messages: {\n" +
				"minimumVersionMessage: \"test minimum version message\",\n" +
				"deprecatedVersionMessage: \"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"currentVersionMessage: \"test update available message\"\n" +
				"},\n" +
				"requirements: {\n" +
				"minimumOSVersion: \"15\"\n" +
				"}";
		saveResponse(testResponse);
		mAppUpdateManager = new AppUpdateManager(mAppInfra);
	}



	public void saveResponse(String response) {
		mFileUtils.saveFile(response, "test.json", "/AppInfra/AIAppupdate");
	}


	public AppUpdateModel getAppUpdateModel(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, AppUpdateModel.class);

	}

	public void deleteFile() {
		mFileUtils.deleteFile("test.json", "/AppInfra/AIAppupdate");
	}
}
