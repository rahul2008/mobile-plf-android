/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.init;

import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uid.view.widget.ProgressBar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static com.philips.platform.ths.utility.THSConstants.THS_SDK_SERVICE_ID;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class THSInitPresenterTest {

    @Mock
    AWSDK awsdkMock;

    @Mock
    User userMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    ProgressBar progressBar;

    @Mock
    THSSDKError thssdkErrorMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    FragmentActivity activityMock;

    @Mock
    THSInitPresenter thsInitPresenterMock;

    THSInitPresenter mThsInitPresenter;

    @Mock
    THSInitFragment thsInitFragmentMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Throwable throwableMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        THSManager.getInstance().setAwsdk(awsdkMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);

        THSManager.getInstance().setAppInfra(appInfraInterface);


        THSManager.getInstance().TEST_FLAG = true;
        THSManager.getInstance().setUser(userMock);

        when(userMock.getHsdpUUID()).thenReturn("123");
        when(userMock.getHsdpAccessToken()).thenReturn("123");

        mThsInitPresenter = new THSInitPresenter(thsInitFragmentMock);
    }

    @Test
    public void onEvent() throws Exception {
        mThsInitPresenter.onEvent(-1);
    }

    @Test
    public void initializeAwsdk() throws Exception {
        when(userMock.isUserSignIn()).thenReturn(true);
        mThsInitPresenter.initializeAwsdk();
        verify(serviceDiscoveryMock).getServiceUrlWithCountryPreference(anyString(),any(ServiceDiscoveryInterface.OnGetServiceUrlListener.class));
    }

    @Test
    public void onInitializationResponse() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(null);
        mThsInitPresenter.onInitializationResponse(null,thssdkErrorMock);
        verify(consumerManagerMock).checkConsumerExists(anyString(), any(SDKCallback.class));
    }

    @Test
    public void onInitializationResponseOnSuccess() throws Exception {
        mThsInitPresenter.onInitializationResponse(null,thssdkErrorMock);
    }

    @Test
    public void onInitializationFailure() throws Exception {
        mThsInitPresenter.onInitializationFailure(throwableMock);
    }

    @Test
    public void onResponse() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        mThsInitPresenter.onResponse(true,thssdkErrorMock);
    }

    @Test
    public void onResponseFalse() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        mThsInitPresenter.onResponse(false,thssdkErrorMock);
    }

    @Test
    public void onResponsethsErrorNull() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(null);
        mThsInitPresenter.onResponse(true,thssdkErrorMock);
    }

    @Test
    public void onResponseFalsethsErrorNull() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(null);
        mThsInitPresenter.onResponse(false,thssdkErrorMock);
    }

    @Test
    public void onFailure() throws Exception {
        mThsInitPresenter.onFailure(throwableMock);
        verify(thsInitFragmentMock).hideProgressBar();
    }

}