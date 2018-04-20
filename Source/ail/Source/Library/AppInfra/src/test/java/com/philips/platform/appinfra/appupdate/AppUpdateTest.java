package com.philips.platform.appinfra.appupdate;

import android.os.Handler;

import com.android.volley.Network;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * AppUpdate Test class.
 */

public class AppUpdateTest extends TestCase {

	@Mock
	private Network mMockNetwork;

	AppConfigurationInterface appConfigurationInterface;

	ServiceDiscoveryInterface serviceDiscoveryInterface;

	Gson mGson;

	AppUpdateManager mAppUpdateManager;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
        AppInfra mAppInfra = mock(AppInfra.class);
		VolleyLog.DEBUG = false;
		Runnable runnableMock = mock(Runnable.class);
		Handler handlerMock = mock(Handler.class);


		when(handlerMock.post(runnableMock)).thenReturn(true);
		appConfigurationInterface = Mockito.mock(AppConfigurationInterface.class);

		AppUpdateInterface appupdateInterface = mock(AppUpdateInterface.class);
		Mockito.when(mAppInfra.getConfigInterface()).thenReturn(appConfigurationInterface);
		Mockito.when(mAppInfra.getAppUpdate()).thenReturn(appupdateInterface);
		serviceDiscoveryInterface = Mockito.mock(ServiceDiscoveryInterface.class);
		mAppUpdateManager = mock(AppUpdateManager.class);
	}

	public void testServiceIdKey() {
		when(mAppUpdateManager.getServiceIdFromAppConfig()).thenReturn(null);
		assertNull(mAppUpdateManager.getServiceIdFromAppConfig());
	}

//	@Test
//	public void testRefresh()  {
//
//		AppUpdateInterface.OnRefreshListener onRefreshListener = Mockito.mock(AppUpdateInterface.OnRefreshListener.class);
//		final ServiceDiscoveryInterface.OnGetServiceUrlListener onGetServiceUrlListener =
//				Mockito.mock(ServiceDiscoveryInterface.OnGetServiceUrlListener.class);

//		appUpdateManager = new AppUpdateManager(mAppInfra) {
//			@NonNull
//			@Override
//			protected ServiceDiscoveryInterface.OnGetServiceUrlListener getServiceDiscoveryListener
//					(OnRefreshListener refreshListener) {
//				return onGetServiceUrlListener;
//			}
//		};
//		appUpdateManager.refresh(onRefreshListener);
//		Mockito.verify(onGetServiceUrlListener).onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_SERVICE_LOCALE_ERROR, "No Loacle error");
//		Mockito.verify(onRefreshListener).onError(AppUpdateInterface.OnRefreshListener.AIAppUpdateRefreshResult.AppUpdate_REFRESH_FAILED, "Invalid ServiceID");

	//}
}
