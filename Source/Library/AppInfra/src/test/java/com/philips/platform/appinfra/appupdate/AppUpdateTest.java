package com.philips.platform.appinfra.appupdate;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;

import com.android.volley.Network;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.FileUtils;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.languagepack.LanguagePackInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static com.philips.platform.appinfra.appupdate.AppUpdateManager.APPUPDATE_DATE_FORMAT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gkavya on 6/5/17.
 */

public class AppUpdateTest extends TestCase {

	private AppInfra mAppInfra;

	private AppUpdateInterface mAppupdateInterface;

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

		mAppupdateInterface = mock(AppUpdateInterface.class);
		Mockito.when(mAppInfra.getConfigInterface()).thenReturn(appConfigurationInterface);
		Mockito.when(mAppInfra.getAppUpdate()).thenReturn(mAppupdateInterface);
		serviceDiscoveryInterface = Mockito.mock(ServiceDiscoveryInterface.class);
		mAppUpdateManager = mock(AppUpdateManager.class);
	}

	@Test
	public void testServiceIdKeywithValueNull() {
		when(mAppUpdateManager.getServiceIdFromAppConfig()).thenReturn(null);
		assertNull(mAppUpdateManager.getServiceIdFromAppConfig());
	}

	@Test
	public void testServiceIdKeywithValue() {
		when(mAppUpdateManager.getServiceIdFromAppConfig()).thenReturn("appinfra.testing.version");
//		verify(mAppUpdateManager).getServiceIdFromAppConfig();
		Assert.assertEquals(mAppUpdateManager.getServiceIdFromAppConfig(), "appinfra.testing.version");
	}

	@Test
	public void testgetServiceDiscoveryUrl() {
		try {
			final URL url = new URL("http://hashim-rest.herokuapp.com/sd/dev/appupdate/appinfra/version.json");
			final AppUpdateInterface.OnRefreshListener onRefreshListener = Mockito.mock
					(AppUpdateInterface.OnRefreshListener.class);
			final ServiceDiscoveryInterface.OnGetServiceUrlListener onGetServiceUrlListener =
					Mockito.mock(ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
			when(mAppUpdateManager.getServiceDiscoveryListener(onRefreshListener))
					.thenReturn(onGetServiceUrlListener);

			doAnswer(new Answer() {
				@Override
				public Object answer(InvocationOnMock invocation) throws Throwable {
					((AppUpdateInterface.OnRefreshListener) invocation.getArguments()[0])
							.onSuccess(AppUpdateInterface.OnRefreshListener.AIAppUpdateRefreshResult.AppUpdate_REFRESH_SUCCESS);
					return null;
				}
			}).when(mAppUpdateManager).getServiceDiscoveryListener(any(AppUpdateInterface.OnRefreshListener.class));

			mAppUpdateManager.getServiceDiscoveryListener(onRefreshListener);
			// Verify state and interaction
			verify(mAppUpdateManager, times(1)).getServiceDiscoveryListener(
					any(AppUpdateInterface.OnRefreshListener.class));

			verify(mAppUpdateManager).getServiceDiscoveryListener(onRefreshListener);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void testJsonResponseListener() {
		final AppUpdateInterface.OnRefreshListener onRefreshListener = Mockito.mock
				(AppUpdateInterface.OnRefreshListener.class);

		Response.ErrorListener errorListener = mock(Response.ErrorListener.class);

		when(mAppUpdateManager.getJsonErrorListener(onRefreshListener)).thenReturn(errorListener);

		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				((AppUpdateInterface.OnRefreshListener) invocation.getArguments()[0])
						.onError(AppUpdateInterface.OnRefreshListener.AIAppUpdateRefreshResult.AppUpdate_REFRESH_FAILED ,"Invalid input");
				return null;
			}
		}).when(mAppUpdateManager).getJsonErrorListener(any(AppUpdateInterface.OnRefreshListener.class));

		mAppUpdateManager.getJsonErrorListener(onRefreshListener);
		// Verify state and interaction
		verify(mAppUpdateManager, times(1)).getJsonErrorListener(
				any(AppUpdateInterface.OnRefreshListener.class));

		verify(mAppUpdateManager).getJsonErrorListener(onRefreshListener);
	}

	@Test
	public void testAppUpdateRefresh() {
		final AppUpdateInterface.OnRefreshListener onRefreshListener = Mockito.mock
				(AppUpdateInterface.OnRefreshListener.class);
		final ServiceDiscoveryInterface.OnGetServiceUrlListener onGetServiceUrlListener =
				Mockito.mock(ServiceDiscoveryInterface.OnGetServiceUrlListener.class);
		when(mAppUpdateManager.getServiceDiscoveryListener(onRefreshListener))
				.thenReturn(onGetServiceUrlListener);
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				((AppUpdateInterface.OnRefreshListener) invocation.getArguments()[0])
						.onSuccess(AppUpdateInterface.OnRefreshListener.AIAppUpdateRefreshResult.AppUpdate_REFRESH_SUCCESS);
				return null;
			}
		}).when(mAppUpdateManager).refresh(any(AppUpdateInterface.OnRefreshListener.class));

		mAppUpdateManager.refresh(onRefreshListener);
		// Verify state and interaction
		verify(mAppUpdateManager, times(1)).refresh(
				any(AppUpdateInterface.OnRefreshListener.class));
	}

	@Test
	public void testgetDeprecateMessage() throws Exception {
		when(mAppupdateInterface.getDeprecateMessage()).
				thenReturn("The current version of the Grooming app is outdated. Please update the app.");
		mAppupdateInterface.getDeprecateMessage();
		verify(mAppupdateInterface).getDeprecateMessage();
		assertNotNull(mAppupdateInterface.getDeprecateMessage());
		assertEquals(mAppupdateInterface.getDeprecateMessage(),
				"The current version of the Grooming app is outdated. Please update the app.");
		assertNotSame(mAppupdateInterface.getDeprecateMessage(), "null");
	}

	@Test
	public void testgetToBeDeprecatedMessage() throws Exception {

		when(mAppupdateInterface.getToBeDeprecatedMessage()).
				thenReturn("The current version will be outdated by 2017-07-12. Please update the app soon.");
		mAppupdateInterface.getToBeDeprecatedMessage();
		verify(mAppupdateInterface).getToBeDeprecatedMessage();
		assertNotNull(mAppupdateInterface.getToBeDeprecatedMessage());
		assertEquals(mAppupdateInterface.getToBeDeprecatedMessage(),
				"The current version will be outdated by 2017-07-12. Please update the app soon.");
		assertNotSame(mAppupdateInterface.getToBeDeprecatedMessage(), "null");
	}

	@Test
	public void testgetToBeDeprecatedDate() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(APPUPDATE_DATE_FORMAT
				, Locale.ENGLISH);
		formatter.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));

		when(mAppupdateInterface.getToBeDeprecatedDate()).thenReturn(formatter.parse("2017-07-12"));
		mAppupdateInterface.getToBeDeprecatedDate();
		verify(mAppupdateInterface).getToBeDeprecatedDate();
		assertNotNull(mAppupdateInterface.getToBeDeprecatedDate());

		assertEquals(formatter.format(mAppupdateInterface.getToBeDeprecatedDate()),
				"2017-07-12");
		assertNotSame(mAppupdateInterface.getToBeDeprecatedDate(), "null");
	}

	@Test
	public void testgetUpdateMessage() throws Exception {

		when(mAppupdateInterface.getUpdateMessage()).thenReturn("A new version of the Grooming app is now available.");
		mAppupdateInterface.getUpdateMessage();
		verify(mAppupdateInterface).getUpdateMessage();

		assertNotNull(mAppupdateInterface.getUpdateMessage());
		assertEquals(mAppupdateInterface.getUpdateMessage(),
				"A new version of the Grooming app is now available.");
		assertNotSame(mAppupdateInterface.getUpdateMessage(), "null");
	}

	@Test
	public void testgetMinimumVersion() throws Exception {

		when(mAppupdateInterface.getMinimumVersion()).thenReturn("1.0.0");
		mAppupdateInterface.getMinimumVersion();
		verify(mAppupdateInterface).getMinimumVersion();

		assertNotNull(mAppupdateInterface.getMinimumVersion());
		assertEquals(mAppupdateInterface.getMinimumVersion(), "1.0.0");
		assertNotSame(mAppupdateInterface.getMinimumVersion(), "1.2.3");
	}

	@Test
	public void testgetMinimumOSverion() throws Exception {

		when(mAppupdateInterface.getMinimumOSverion()).thenReturn("15");
		mAppupdateInterface.getMinimumOSverion();
		verify(mAppupdateInterface).getMinimumOSverion();
		assertNotNull(mAppupdateInterface.getMinimumOSverion());
		assertEquals(mAppupdateInterface.getMinimumOSverion(), "15");
		assertNotSame(mAppupdateInterface.getMinimumOSverion(), "21");
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
