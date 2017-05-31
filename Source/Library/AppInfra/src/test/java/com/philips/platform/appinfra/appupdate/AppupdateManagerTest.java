package com.philips.platform.appinfra.appupdate;


import android.os.Handler;
import android.support.annotation.NonNull;

import com.android.volley.Network;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.FileUtils;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import junit.framework.TestCase;

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

	public void testSaveResponse() {

	}

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


		/*final RequestManager requestManager = mock(RequestManager.class);
		final AppupdateInterface.OnRefreshListener responseListener = mock(AppupdateInterface.OnRefreshListener.class);
		final JsonObjectRequest jsonObjectRequest = mock(JsonObjectRequest.class);
//		final JSONObject responseJson = new JSONObject() {
//				return"fds";
//		};


		appupdateInterface.refresh(new AppupdateInterface.OnRefreshListener() {
			@Override
			public void onError(AIAppUpdateRefreshResult error, String message) {
				assertNotNull(error);
			}

			@Override
			public void onSuccess(AIAppUpdateRefreshResult result) {
				assertNotNull(result);
			}
		});*/
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
		assertEquals(appupdateInterface.getDeprecateMessage() ,
				"The current version of the Grooming app is outdated. Please update the app.");
		assertNotSame(appupdateInterface.getDeprecateMessage(),"null");
	}

	@Test
	public void getToBeDeprecatedMessage() throws Exception {
		assertNotNull(appupdateInterface.getToBeDeprecatedMessage());
		assertEquals(appupdateInterface.getToBeDeprecatedMessage() ,
				"The current version will be outdated by 2017-07-12. Please update the app soon.");
		assertNotSame(appupdateInterface.getToBeDeprecatedMessage(),"null");
	}

	@Test
	public void getToBeDeprecatedDate() throws Exception {
		assertNotNull(appupdateInterface.getToBeDeprecatedDate());
		assertEquals(appupdateInterface.getToBeDeprecatedDate() ,
				"2017-07-12");
		assertNotSame(appupdateInterface.getToBeDeprecatedDate(),"null");
	}

	@Test
	public void getUpdateMessage() throws Exception {
		assertNotNull(appupdateInterface.getUpdateMessage());
		assertEquals(appupdateInterface.getUpdateMessage() ,
				"A new version of the Grooming app is now available.");
		assertNotSame(appupdateInterface.getUpdateMessage(),"null");
	}

	@Test
	public void getMinimumVersion() throws Exception {
		assertNotNull(appupdateInterface.getMinimumVersion());
		assertEquals(appupdateInterface.getMinimumVersion() ,"1.0.0");
		assertNotSame(appupdateInterface.getMinimumVersion(),"1.2.3");
	}

	@Test
	public void getMinimumOSverion() throws Exception {
		assertNotNull(appupdateInterface.getMinimumOSverion());
		assertEquals(appupdateInterface.getMinimumOSverion() ,"15");
		assertNotSame(appupdateInterface.getMinimumOSverion(),"21");
	}

}