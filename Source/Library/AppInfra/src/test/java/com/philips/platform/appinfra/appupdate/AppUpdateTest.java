package com.philips.platform.appinfra.appupdate;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;

import com.android.volley.Network;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.FileUtils;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gkavya on 6/5/17.
 */

public class AppUpdateTest extends TestCase {

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

	AppUpdateManager mAppUpdateManager;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mAppInfra = mock(AppInfra.class);

		mFileUtils = mock(FileUtils.class);
		runnableMock = mock(Runnable.class);
		handlerMock = mock(Handler.class);


		when(handlerMock.post(runnableMock)).thenReturn(true);
		appConfigurationInterface = Mockito.mock(AppConfigurationInterface.class);

		appupdateInterface = mock(AppUpdateInterface.class);
		Mockito.when(mAppInfra.getConfigInterface()).thenReturn(appConfigurationInterface);
		Mockito.when(mAppInfra.getAppUpdate()).thenReturn(appupdateInterface);
		serviceDiscoveryInterface = Mockito.mock(ServiceDiscoveryInterface.class);
		mAppUpdateManager = mock(AppUpdateManager.class);
	}

	@Test
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
