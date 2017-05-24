package com.philips.platform.appinfra.appupdate;


import android.os.Handler;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.servicediscovery.RequestManager;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.net.URL;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class AppupdateManagerTest extends TestCase {

	private AppInfra mAppInfra;
	private AppupdateInterface appupdateInterface;
	private String appUpdateUrl = "https://hashim-rest.herokuapp.com/sd/dev/appupdate/appinfra/version.json";
	private String path;
	private Handler handlerMock;
	@Mock
	private Network mMockNetwork;

	private Runnable runnableMock;

	@Before
	public void setUp() throws Exception {
		mAppInfra = mock(AppInfra.class);
		appupdateInterface = mAppInfra.getAppupdate();
		assertNotNull(appupdateInterface);

		runnableMock = mock(Runnable.class);
		handlerMock = mock(Handler.class);
		when(handlerMock.post(runnableMock)).thenReturn(true);


		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource("Appflow.json");
		File file = new File(resource.getPath());
		path = file.getPath();
	}

	@After
	public void tearDown() throws Exception {

	}

	public void testSaveResponse() {

	}

	@Test
	public void refresh() throws Exception {

		final RequestManager requestManager = mock(RequestManager.class);
		final AppupdateInterface.OnRefreshListener responseListener = mock(AppupdateInterface.OnRefreshListener.class);
        final JsonObjectRequest jsonObjectRequest = mock(JsonObjectRequest.class);
		final JSONObject responseJson = new JSONObject() {
				return "fds";
		};



		appupdateInterface.refresh(new AppupdateInterface.OnRefreshListener() {
			@Override
			public void onError(AIAppUpdateRefreshResult error, String message) {
					assertNotNull(error);
			}

			@Override
			public void onSuccess(AIAppUpdateRefreshResult result) {
				assertNotNull(result);
			}
		});
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

	}

	@Test
	public void getToBeDeprecatedMessage() throws Exception {

	}

	@Test
	public void getToBeDeprecatedDate() throws Exception {

	}

	@Test
	public void getUpdateMessage() throws Exception {

	}

	@Test
	public void getMinimumVersion() throws Exception {

	}

	@Test
	public void getMinimumOSverion() throws Exception {

	}

}