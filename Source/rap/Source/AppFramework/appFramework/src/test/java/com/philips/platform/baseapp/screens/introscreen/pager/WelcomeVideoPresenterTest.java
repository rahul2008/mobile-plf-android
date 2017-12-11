package com.philips.platform.baseapp.screens.introscreen.pager;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by 310207283 on 7/31/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class WelcomeVideoPresenterTest {

    @Mock
    private Context context;

    @Mock
    private WelcomeVideoFragmentContract.View view;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    AppFrameworkApplication appFrameworkApplicationMock;

    WelcomeVideoPresenter welcomeVideoPresenter;

    @Captor
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlListener> captor;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryInterfaceMock;

    ServiceDiscoveryInterface.OnGetServiceUrlListener onGetServiceUrlListener;

    @Before
    public void setUp() {
        welcomeVideoPresenter = new WelcomeVideoPresenter(view, context);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        when(appFrameworkApplicationMock.getAppInfra()).thenReturn(appInfraInterfaceMock);
        when(context.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        welcomeVideoPresenter.fetchVideoDataSource();
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithLanguagePreference(eq(Constants.SERVICE_DISCOVERY_SPLASH_VIDEO), captor.capture());
        onGetServiceUrlListener = captor.getValue();


    }

    @Test
    public void fetchVideoUrlSuccess() throws Exception {
        String urlString = "https://images.philips.com/skins/PhilipsConsumer/CDP2_reference_app_vid_short";
        onGetServiceUrlListener.onSuccess(new URL(urlString));
        verify(view).setVideoDataSource(urlString+WelcomeVideoPresenter.COMPRESSED_VIDEO_EXTENSION);
    }

    @Test
    public void fetchVideoUrlError() throws Exception {
        onGetServiceUrlListener.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR,"");
        verify(view).onFetchError();
    }

    @After
    public void tearDown() {
        context = null;
        view = null;
        appInfraInterfaceMock = null;
        appFrameworkApplicationMock = null;
        welcomeVideoPresenter = null;
        captor = null;
        serviceDiscoveryInterfaceMock = null;
        onGetServiceUrlListener = null;
    }
}
