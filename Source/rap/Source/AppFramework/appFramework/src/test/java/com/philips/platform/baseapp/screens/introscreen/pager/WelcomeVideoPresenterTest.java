package com.philips.platform.baseapp.screens.introscreen.pager;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
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
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlMapListener> captor;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryInterfaceMock;

    ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlListener;

    @Before
    public void setUp() {
        welcomeVideoPresenter = new WelcomeVideoPresenter(view, context);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        when(appFrameworkApplicationMock.getAppInfra()).thenReturn(appInfraInterfaceMock);
        when(context.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        welcomeVideoPresenter.fetchVideoDataSource();
        ArrayList<String> serviceIDList = new ArrayList<>();
        serviceIDList.add(Constants.SERVICE_DISCOVERY_SPLASH_VIDEO);
        ArgumentCaptor<ArrayList<String>> captorList = ArgumentCaptor.forClass(ArrayList.class);
        verify(serviceDiscoveryInterfaceMock).getServicesWithLanguagePreference(captorList.capture(), captor.capture(),eq(null));
        onGetServiceUrlListener = captor.getValue();
    }

    @Test
    public void fetchVideoUrlSuccess() throws Exception {
        String urlString = "https://images.philips.com/skins/PhilipsConsumer/CDP2_reference_app_vid_short";
        Map<String, ServiceDiscoveryService> mapUrl = new HashMap<>();
        ServiceDiscoveryService serviceDiscoveryService = new ServiceDiscoveryService();
        serviceDiscoveryService.setConfigUrl(urlString);
        mapUrl.put(Constants.SERVICE_DISCOVERY_SPLASH_VIDEO,serviceDiscoveryService);
        onGetServiceUrlListener.onSuccess(mapUrl);
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
