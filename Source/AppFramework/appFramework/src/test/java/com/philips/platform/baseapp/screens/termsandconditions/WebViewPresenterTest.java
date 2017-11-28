/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.termsandconditions;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.contentloader.ContentLoader;
import com.philips.platform.appinfra.contentloader.ContentLoaderInterface;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 27/07/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class WebViewPresenterTest {

    @Mock
    private Context context;

    @Mock
    private WebViewContract.View view;

    WebViewPresenter webViewPresenter;

    @Captor
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlListener> captor;

    ServiceDiscoveryInterface serviceDiscoveryInterfaceMock;

    ServiceDiscoveryInterface.OnGetServiceUrlListener value;

    @Mock
    private static ContentLoader contentLoader;

    @Captor
    private ArgumentCaptor<ContentLoaderInterface.OnRefreshListener> refreshListenerCaptor;

    @Captor
    private ArgumentCaptor<ContentLoaderInterface.OnResultListener> onResultListenerArgumentCaptor;

    @After
    public void tearDown() {
        context = null;
        view = null;
        webViewPresenter = null;
        captor = null;
        serviceDiscoveryInterfaceMock = null;
        value = null;
    }

    @Before
    public void setUp() {
        webViewPresenter = new WebViewPresenterMock(view, context);
        AppInfraInterface appInfraInterfaceMock = mock(AppInfraInterface.class);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        when(appFrameworkApplicationMock.getAppInfra()).thenReturn(appInfraInterfaceMock);
        when(context.getApplicationContext()).thenReturn(appFrameworkApplicationMock);

    }

    @Test
    public void loadUrlTest_onUrlLoadSuccess() throws Exception {
        webViewPresenter.loadUrl(Constants.TERMS_AND_CONDITIONS);
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(eq(Constants.TERMS_AND_CONDITIONS), captor.capture());
        value= captor.getValue();
        value.onSuccess(new URL("https://www.usa.philips.com/content/B2C/en_US/apps/77000/deep-sleep/sleep-score/articles/sleep-score/high-sleepscore.html"));
        verify(view).onUrlLoadSuccess(any(String.class));
    }

    @Test
    public void loadUrlTest_onUrlLoadError() throws Exception {
        webViewPresenter.loadUrl(Constants.TERMS_AND_CONDITIONS);
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(eq(Constants.TERMS_AND_CONDITIONS), captor.capture());
        value= captor.getValue();
        value.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_NETWORK,"");
        verify(view).onUrlLoadError(any(String.class));
    }
//    @Test
//    public void loadTermsAndConditionsUrlEmpty() throws Exception {
//        value.onSuccess(null);
//        verify(view).onUrlLoadError(any(String.class));
//    }



    static class WebViewPresenterMock extends WebViewPresenter {

        public WebViewPresenterMock(WebViewContract.View viewListener, Context context) {
            super(viewListener, context);
        }

    }
}