/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.init;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSWelcomeFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uid.view.widget.ProgressBar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static com.philips.platform.ths.utility.THSConstants.THS_SDK_SERVICE_ID;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSInitPresenterTest {

    @Mock
    AWSDK awsdkMock;

    @Mock
    User userMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    Context contextMock;

    @Mock
    ProgressBar progressBar;

    @Captor
    private ArgumentCaptor<SDKCallback> initializationCallback;

    @Captor
    private ArgumentCaptor<RefreshLoginSessionHandler> refreshLoginSessionHandlerArgumentCaptor;

    @Mock
    THSSDKError thssdkErrorMock;

    @Mock
    THSAuthentication THSAuthenticationMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    Authentication authenticationMock;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    FragmentActivity activityMock;

    THSInitPresenter mThsInitPresenter;

    @Mock
    THSInitFragment thsInitFragmentMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Throwable throwableMock;

    @Mock
    Consumer consumerMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        THSManager.getInstance().setAwsdk(awsdkMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(thsConsumerMock.getHsdpUUID()).thenReturn("123");

        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);

        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);
        when(thsInitFragmentMock.isFragmentAttached()).thenReturn(true);


        THSManager.getInstance().setUser(userMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        THSManager.getInstance().TEST_FLAG = true;
        when(userMock.getHsdpUUID()).thenReturn("abc");
        when(thsInitFragmentMock.getFragmentActivity()).thenReturn(activityMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);
        List list = new ArrayList();
        list.add(thsConsumerMock);
        when(thsConsumerMock.getDependents()).thenReturn(list);
        when(thsConsumerMock.getDependents()).thenReturn(list);


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
//        verifyNoMoreInteractions(consumerManagerMock);
    }

    @Test
    public void onInitializationResponseOnSuccess() throws Exception {
        mThsInitPresenter.onInitializationResponse(null,thssdkErrorMock);
    }

    @Test
    public void onInitializationFailure() throws Exception {
        when(thsInitFragmentMock.getContext()).thenReturn(contextMock);
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

    @Test
    public void onLoginResponse() throws Exception {
        when(THSAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        mThsInitPresenter.onLoginResponse(THSAuthenticationMock, thssdkErrorMock);
        verify(consumerManagerMock, atLeast(1)).getConsumer(any(Authentication.class), any(SDKCallback.class));
    }

    @Test
    public void onLoginResponseWhenSDKErrorIsNotNull() throws Exception {
        when(THSAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(401);
        when(thssdkErrorMock.getHttpResponseCode()).thenReturn(401);
        mThsInitPresenter.onLoginResponse(THSAuthenticationMock, thssdkErrorMock);
        verify(userMock, atLeast(1)).refreshLoginSession(any(RefreshLoginSessionHandler.class));
    }

    @Test
    public void onLoginResponseWhenNotEnrolledProperly() throws Exception {
        when(THSAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        when(THSAuthenticationMock.needsToCompleteEnrollment()).thenReturn(true);
        mThsInitPresenter.onLoginResponse(THSAuthenticationMock, thssdkErrorMock);
        verify(awsdkMock.getConsumerManager(), atLeast(1)).completeEnrollment(any(Authentication.class), any(State.class), anyString(), anyString(), any(SDKCallback.class));
    }

    @Test
    public void onLoginResponseisRefreshTokenRequestedBeforetrue() throws Exception {
        THSInitFragment mTHSInitFragmentTest = new THSInitFragmentTestMock();
        SupportFragmentTestUtil.startFragment(mTHSInitFragmentTest);
        mThsInitPresenter = new THSInitPresenter(mTHSInitFragmentTest);
        mThsInitPresenter.isRefreshTokenRequestedBefore = true;
        when(THSAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(401);
        when(thssdkErrorMock.getHttpResponseCode()).thenReturn(401);
        mThsInitPresenter.onLoginResponse(THSAuthenticationMock, thssdkErrorMock);
        verify(userMock).isUserSignIn();
    }

    @Test
    public void getConsumerThrowsAWSDKInstantiationException() throws Exception {
        when(THSAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).getConsumer(any(Authentication.class),any(SDKCallback.class));
        mThsInitPresenter.onLoginResponse(THSAuthenticationMock, thssdkErrorMock);
        verify(consumerManagerMock, atLeast(1)).getConsumer(any(Authentication.class),any(SDKCallback.class));
    }

    @Test
    public void onLoginFailure() throws Exception {
        mThsInitPresenter.onLoginFailure(throwableMock);
        verify(thsInitFragmentMock).hideProgressBar();
    }

    @Test
    public void onError() throws Exception {
        mThsInitPresenter.onError(throwableMock);
        verify(thsInitFragmentMock).hideProgressBar();
    }
    @Test
    public void onReceiveConsumerObject(){
        mThsInitPresenter.onReceiveConsumerObject(consumerMock, sdkErrorMock);
    }

    @Test
    public void onLoginResponserefresh() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(thssdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thsInitFragmentMock.getContext()).thenReturn(contextMock);
        mThsInitPresenter.onLoginResponse(null, thssdkErrorMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");

        Object object = new Object();
        when(appConfigurationInterfaceMock.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);

        verify(userMock).refreshLoginSession(refreshLoginSessionHandlerArgumentCaptor.capture());
        RefreshLoginSessionHandler value = refreshLoginSessionHandlerArgumentCaptor.getValue();
        value.onRefreshLoginSessionSuccess();
        verify(awsdkMock).authenticateMutual(anyString(), any(SDKCallback.class));
    }

    @Test
    public void onLoginResponserefreshReturnTypeMap() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(thssdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thsInitFragmentMock.getContext()).thenReturn(contextMock);
        mThsInitPresenter.onLoginResponse(null, thssdkErrorMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");

        Map object = new HashMap();
        object.put("default","default");
        when(appConfigurationInterfaceMock.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);

        verify(userMock).refreshLoginSession(refreshLoginSessionHandlerArgumentCaptor.capture());
        RefreshLoginSessionHandler value = refreshLoginSessionHandlerArgumentCaptor.getValue();
        value.onRefreshLoginSessionSuccess();
        verify(awsdkMock).authenticateMutual(anyString(), any(SDKCallback.class));
    }

    @Test
    public void onLoginResponserefreshReturnTypeMapWhenDefaultValueMissing() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(thssdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thsInitFragmentMock.getContext()).thenReturn(contextMock);
        mThsInitPresenter.onLoginResponse(null, thssdkErrorMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");

        Map object = new HashMap();
        //object.put("default","default");
        when(appConfigurationInterfaceMock.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);

        verify(userMock).refreshLoginSession(refreshLoginSessionHandlerArgumentCaptor.capture());
        RefreshLoginSessionHandler value = refreshLoginSessionHandlerArgumentCaptor.getValue();
        value.onRefreshLoginSessionSuccess();
        verify(awsdkMock).authenticateMutual(anyString(), any(SDKCallback.class));
    }

    @Test
    public void onLoginResponseOnRefreshLoginSessionFailedWithError() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(thssdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thsInitFragmentMock.getContext()).thenReturn(contextMock);
        mThsInitPresenter.onLoginResponse(null, thssdkErrorMock);

        verify(userMock).refreshLoginSession(refreshLoginSessionHandlerArgumentCaptor.capture());
        RefreshLoginSessionHandler value = refreshLoginSessionHandlerArgumentCaptor.getValue();
//        value.onRefreshLoginSessionFailedWithError(anyInt());
 //       verify(thsInitFragmentMock).hideProgressBar();
    }

    @Test
    public void onLoginResponseOnRefreshLoginSessionInProgress() throws Exception {
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(thssdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thsInitFragmentMock.getContext()).thenReturn(contextMock);
        mThsInitPresenter.onLoginResponse(null, thssdkErrorMock);

        verify(userMock).refreshLoginSession(refreshLoginSessionHandlerArgumentCaptor.capture());
        RefreshLoginSessionHandler value = refreshLoginSessionHandlerArgumentCaptor.getValue();
        value.onRefreshLoginSessionInProgress("123");
    }

    @Test
    public void onReceiveConsumerObjectTestAppointments(){
        mThsInitPresenter.onReceiveConsumerObject(consumerMock,sdkErrorMock);
        verify(thsInitFragmentMock).hideProgressBar();
    }

    @Test
    public void onReceiveConsumerObjectVisitHostory(){
        mThsInitPresenter.onReceiveConsumerObject(consumerMock,sdkErrorMock);
        verify(thsInitFragmentMock).hideProgressBar();
    }

    @Test
    public void onReceiveConsumerObjectHowItWorks(){
        mThsInitPresenter.onReceiveConsumerObject(consumerMock,sdkErrorMock);
        verify(thsInitFragmentMock).hideProgressBar();
    }

    @Test
    public void onReceiveConsumerObjectPractices(){
        mThsInitPresenter.onReceiveConsumerObject(consumerMock,sdkErrorMock);
        verify(thsInitFragmentMock).hideProgressBar();
    }

    @Test
    public void testAuthenticate(){
        mThsInitPresenter.launchWelcomeScreen();
        verify(thsInitFragmentMock).addFragment(any(THSBaseFragment.class), anyString(), any(Bundle.class), anyBoolean());
    }
}