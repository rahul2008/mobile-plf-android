package com.philips.platform.appinfra.appupdate;


import android.content.Context;
import android.os.Handler;

import com.android.volley.Network;
import com.philips.platform.appinfra.AppInfra;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.net.URL;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AppupdateManagerTest  extends TestCase  {

	private Context mContext;
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
		mContext = mock(Context.class);
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