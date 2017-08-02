package com.philips.platform.baseapp.screens.introscreen.pager;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;

import static org.mockito.Matchers.any;
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

    ServiceDiscoveryInterface.OnGetServiceUrlListener value;

    @Before
    public void setUp() {
        welcomeVideoPresenter = new WelcomeVideoPresenter(view, context);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        when(appFrameworkApplicationMock.getAppInfra()).thenReturn(appInfraInterfaceMock);
        when(context.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        welcomeVideoPresenter.fetchVideoDataSource();
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithLanguagePreference(eq(Constants.SERVICE_DISCOVERY_SPLASH_VIDEO), captor.capture());
        value= captor.getValue();


    }

    @Test
    public void fetchVideoUrlSuccess() throws Exception {
        value.onSuccess(new URL("https://www.philips.com/wrx/b2c/c/us/en/apps/77000/TermsOfUse.html"));
        verify(view).setVideoDataSource(any(String.class));
    }

    @Test
    public void fetchVideoUrlError() throws Exception {
        value.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR,"");
        verify(view).onFetchError();
    }
}
