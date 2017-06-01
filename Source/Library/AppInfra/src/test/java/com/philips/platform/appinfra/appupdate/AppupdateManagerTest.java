package com.philips.platform.appinfra.appupdate;


import android.os.Handler;
import android.support.annotation.NonNull;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.FileUtils;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.net.URL;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppupdateManagerTest extends TestCase {

	private AppInfra mAppInfra;

	private AppUpdateInterface appupdateInterface;

	private Handler handlerMock;
	@Mock
	private Network mMockNetwork;

	private Runnable runnableMock;

	private FileUtils mFileUtils;

	AppConfigurationInterface appConfigurationInterface;

	ServiceDiscoveryInterface serviceDiscoveryInterface;

	Gson mGson;

	AppUpdateManager appUpdateManager;

	@Before
	public void setUp() throws Exception {

		mAppInfra = mock(AppInfra.class);
		MockitoAnnotations.initMocks(this);
		mFileUtils = mock(FileUtils.class);
		runnableMock = mock(Runnable.class);
		handlerMock = mock(Handler.class);


		when(handlerMock.post(runnableMock)).thenReturn(true);
		appConfigurationInterface = Mockito.mock(AppConfigurationInterface.class);

		appupdateInterface = mock(AppUpdateInterface.class);
		Mockito.when(mAppInfra.getConfigInterface()).thenReturn(appConfigurationInterface);
		Mockito.when(mAppInfra.getAppUpdate()).thenReturn(appupdateInterface);
		serviceDiscoveryInterface = Mockito.mock(ServiceDiscoveryInterface.class);
	}

	@After
	public void tearDown() throws Exception {

	}

//	@Test
//
//	public void testSaveResponse() {
//
//	}

	@Test
	public void refresh() throws Exception {

		AppUpdateInterface.OnRefreshListener onRefreshListener = Mockito.mock(AppUpdateInterface.OnRefreshListener.class);
		final ServiceDiscoveryInterface.OnGetServiceUrlListener onGetServiceUrlListener =
				Mockito.mock(ServiceDiscoveryInterface.OnGetServiceUrlListener.class);

		appUpdateManager = new AppUpdateManager(mAppInfra) {
			@NonNull
			@Override
			protected ServiceDiscoveryInterface.OnGetServiceUrlListener getServiceDiscoveryListener
					(OnRefreshListener refreshListener) {
				return onGetServiceUrlListener;
			}
		};
		appUpdateManager.refresh(onRefreshListener);
		Mockito.verify(onGetServiceUrlListener).onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR, "No Loacle error");
		Mockito.verify(onRefreshListener).onError(AppUpdateInterface.OnRefreshListener.AIAppUpdateRefreshResult.AppUpdate_REFRESH_FAILED, "Invalid ServiceID");

	}

	@Test
	public void refreshSuccesspath() throws Exception {
		URL url = new URL("https://prod.appinfra.testing.service/en_IN/B2C/77000");
		AppUpdateInterface.OnRefreshListener onRefreshListener = Mockito.mock(AppUpdateInterface.OnRefreshListener.class);
		final ServiceDiscoveryInterface.OnGetServiceUrlListener onGetServiceUrlListener =
				Mockito.mock(ServiceDiscoveryInterface.OnGetServiceUrlListener.class);

		appUpdateManager = new AppUpdateManager(mAppInfra) {
			@NonNull
			@Override
			protected ServiceDiscoveryInterface.OnGetServiceUrlListener getServiceDiscoveryListener
					(OnRefreshListener refreshListener) {
				return onGetServiceUrlListener;
			}
		};
		appUpdateManager.refresh(onRefreshListener);
		Mockito.verify(onGetServiceUrlListener).onSuccess(url);
		Mockito.verify(onRefreshListener).onSuccess(AppUpdateInterface.OnRefreshListener.AIAppUpdateRefreshResult.AppUpdate_REFRESH_SUCCESS);
	}

	@Test
	public void testDownloadAppUpdate() throws Exception {
		String json = "{  \n" +
				"   \"version\":{  \n" +
				"      \"minimumVersion\":\"1.5.0\",\n" +
				"      \"deprecatedVersion\":\"3.5.0\",\n" +
				"      \"deprecationDate\":\"2017-07-12\",\n" +
				"      \"currentVersion\":\"4.5.0\"\n" +
				"   },\n" +
				"   \"messages\":{  \n" +
				"      \"minimumVersionMessage\":\"The current version of the  app is outdated in India. Please update the app.\",\n" +
				"      \"deprecatedVersionMessage\":\"The current version will be outdated by 2017-07-12. Please update the app soon.\",\n" +
				"      \"currentVersionMessage\":\"A new version of the  App is now available in India.\"\n" +
				"   },\n" +
				"   \"requirements\":{  \n" +
				"      \"minimumOSVersion\":\"15\"\n" +
				"   }\n" +
				"}";
		//String url = "https://prod.appinfra.testing.service/en_IN/B2C/77000";
		AppUpdateInterface.OnRefreshListener onRefreshListener = Mockito.mock(AppUpdateInterface.OnRefreshListener.class);
		//JsonObjectRequest jsonObjectRequest = Mockito.mock(JsonObjectRequest.class);
		final Response.Listener responseListener = Mockito.mock(Response.Listener.class);
		final Response.ErrorListener errorListener = Mockito.mock(Response.ErrorListener.class);
		//RestInterface restInterface = Mockito.mock(RestInterface.class);

	//	jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener, null, null, null);
		JSONObject response = new JSONObject(json);
//		Mockito.verify(responseListener).onResponse(null);
		appUpdateManager = new AppUpdateManager(mAppInfra) {
			@NonNull
			@Override
			protected Response.Listener<JSONObject> getJsonResponseListener(OnRefreshListener refreshListener) {
				return responseListener;
			}

			@NonNull
			@Override
			protected Response.ErrorListener getJsonErrorListener(OnRefreshListener refreshListener) {
				return errorListener;
			}
		};
		Mockito.verify(responseListener).onResponse(response);


	}




	public void getAppupdateModel() {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource("Appflow.json");
		File file = new File(resource.getPath());
//		AppupdateModel appUpdateModel = mGson.fromJson(mFileUtils.readFile(file), AppupdateModel.class);
//		assertNotNull(appUpdateModel);
//		return appUpdateModel;
	}


	@Test
	public void isDeprecated() throws Exception {

	}

	@Test
	public void isToBeDeprecated() throws Exception {

	}

	@Test
	public void isUpdateAvailable() throws Exception {

	}

	@Test
	public void getDeprecateMessage() throws Exception {
		assertNotNull(appupdateInterface.getDeprecateMessage());
		assertEquals(appupdateInterface.getDeprecateMessage(),
				"The current version of the Grooming app is outdated. Please update the app.");
		assertNotSame(appupdateInterface.getDeprecateMessage(), "null");
	}

	@Test
	public void getToBeDeprecatedMessage() throws Exception {
		assertNotNull(appupdateInterface.getToBeDeprecatedMessage());
		assertEquals(appupdateInterface.getToBeDeprecatedMessage(),
				"The current version will be outdated by 2017-07-12. Please update the app soon.");
		assertNotSame(appupdateInterface.getToBeDeprecatedMessage(), "null");
	}

	@Test
	public void getToBeDeprecatedDate() throws Exception {
		assertNotNull(appupdateInterface.getToBeDeprecatedDate());
		assertEquals(appupdateInterface.getToBeDeprecatedDate(),
				"2017-07-12");
		assertNotSame(appupdateInterface.getToBeDeprecatedDate(), "null");
	}

	@Test
	public void getUpdateMessage() throws Exception {
		assertNotNull(appupdateInterface.getUpdateMessage());
		assertEquals(appupdateInterface.getUpdateMessage(),
				"A new version of the Grooming app is now available.");
		assertNotSame(appupdateInterface.getUpdateMessage(), "null");
	}

	@Test
	public void getMinimumVersion() throws Exception {
		assertNotNull(appupdateInterface.getMinimumVersion());
		assertEquals(appupdateInterface.getMinimumVersion(), "1.0.0");
		assertNotSame(appupdateInterface.getMinimumVersion(), "1.2.3");
	}

	@Test
	public void getMinimumOSverion() throws Exception {
		assertNotNull(appupdateInterface.getMinimumOSverion());
		assertEquals(appupdateInterface.getMinimumOSverion(), "15");
		assertNotSame(appupdateInterface.getMinimumOSverion(), "21");
	}

}