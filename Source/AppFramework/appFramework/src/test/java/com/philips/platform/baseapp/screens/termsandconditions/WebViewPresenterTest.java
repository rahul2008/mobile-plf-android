/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.termsandconditions;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;

import static com.philips.platform.baseapp.screens.termsandconditions.WebViewPresenter.PRIVACY;
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

    @Before
    public void setUp() {
        webViewPresenter = new WebViewPresenter(view, context);
        AppInfraInterface appInfraInterfaceMock = mock(AppInfraInterface.class);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        when(appFrameworkApplicationMock.getAppInfra()).thenReturn(appInfraInterfaceMock);
        when(context.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        webViewPresenter.loadUrl(WebViewEnum.PRIVACY_CLICKED);
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(eq(PRIVACY), captor.capture());
        value= captor.getValue();


    }

    @Test
    public void loadTermsAndConditionsUrlSuccess() throws Exception {
        value.onSuccess(new URL("https://www.philips.com/wrx/b2c/c/us/en/apps/77000/TermsOfUse.html"));
        verify(view).updateUiOnUrlLoaded(any(String.class));
    }
    @Test
    public void loadTermsAndConditionsUrlError() throws Exception {
        value.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR,"");
        verify(view).onUrlLoadError(any(String.class));
    }

    @Test
    public void loadTermsAndConditionsUrlEmpty() throws Exception {
        value.onSuccess(null);
        verify(view).onUrlLoadError(any(String.class));
    }
}