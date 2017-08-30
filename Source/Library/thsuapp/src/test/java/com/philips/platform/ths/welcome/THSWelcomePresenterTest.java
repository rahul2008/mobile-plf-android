package com.philips.platform.ths.welcome;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKErrorReason;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.ths.R;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSWelcomePresenterTest {

    THSWelcomePresenter pthWelcomePresenter;

    @Mock
    THSWelcomeFragment pTHBaseViewMock;

    @Mock
    Context contextMock;

    @Captor
    private ArgumentCaptor<SDKCallback> initializationCallback;

    @Mock
    THSSDKError THSSDKError;

    @Mock
    THSAuthentication THSAuthenticationMock;

    @Mock
    FragmentActivity activityMock;

    @Mock
    Authentication authenticationMock;

    @Mock
    ConsumerManager ConsumerManagerMock;

    @Mock
    AWSDK awsdk;

    @Mock
    Throwable throwableMock;

    @Mock
    Consumer consumerMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    Button buttonMock;

    @Mock
    User userMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    AppConfigurationInterface appConfigurationInterface;

    @Captor
    private ArgumentCaptor<RefreshLoginSessionHandler> refreshLoginSessionHandlerArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pthWelcomePresenter = new THSWelcomePresenter(pTHBaseViewMock);
        THSManager.getInstance().setAwsdk(awsdk);
        THSManager.getInstance().setUser(userMock);
        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterface);
        THSManager.getInstance().TEST_FLAG = true;
        when(userMock.getHsdpUUID()).thenReturn("abc");
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(activityMock);
    }

    @Test
    public void onEventAppointments() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.appointments);
        verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }

    @Test
    public void onEventVisitHistory() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.visit_history);
        verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }

    @Test
    public void onEventHowItWorkd() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.how_it_works);
        verify(pTHBaseViewMock).showToast(anyString());
    }

    @Test
    public void onEventths_start() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.ths_start);
        verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }

    @Test
    public void initializeAwsdkThrowsMalformedURLException() throws AWSDKInitializationException {
        doThrow(MalformedURLException.class).when(awsdk).initialize(any(Map.class),any(SDKCallback.class));
        pthWelcomePresenter.initializeAwsdk();
        verify(awsdk).initialize(any(Map.class),any(SDKCallback.class));
    }

    @Test
    public void initializeAwsdkThrowsURISyntaxException() throws AWSDKInitializationException {
        doThrow(URISyntaxException.class).when(awsdk).initialize(any(Map.class),any(SDKCallback.class));
        pthWelcomePresenter.initializeAwsdk();
        verify(awsdk).initialize(any(Map.class),any(SDKCallback.class));
    }

    @Test
    public void initializeAwsdkThrowsAWSDKInstantiationException() throws AWSDKInitializationException {
        doThrow(AWSDKInstantiationException.class).when(awsdk).initialize(any(Map.class),any(SDKCallback.class));
        pthWelcomePresenter.initializeAwsdk();
        verify(awsdk).initialize(any(Map.class),any(SDKCallback.class));
    }

    @Test
    public void initializeAwsdkThrowsAWSDKInitializationException() throws AWSDKInitializationException {
        doThrow(AWSDKInitializationException.class).when(awsdk).initialize(any(Map.class),any(SDKCallback.class));
        pthWelcomePresenter.initializeAwsdk();
        verify(awsdk).initialize(any(Map.class),any(SDKCallback.class));
    }

    @Test
    public void onInitializationResponse() throws Exception {
        pthWelcomePresenter.onInitializationResponse(null, THSSDKError);
        verify(pTHBaseViewMock).updateView();
    }

    @Test
    public void onInitializationFailure()  {
        pthWelcomePresenter.onInitializationFailure(throwableMock);
        verify(pTHBaseViewMock).hideProgressBar();
    }

    @Test
    public void onInitializationFailureWhenContextIsNotNull()  {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        pthWelcomePresenter.onInitializationFailure(throwableMock);
        verify(pTHBaseViewMock).hideProgressBar();
    }


    @Test
    public void authenticateThrowsAWSDKInstantiationException() throws Exception {
        pthWelcomePresenter.onInitializationResponse(null, THSSDKError);
        verify(pTHBaseViewMock).updateView();
    }

    @Test
    public void onLoginResponse() throws Exception {
        when(THSAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        pthWelcomePresenter.onLoginResponse(THSAuthenticationMock, THSSDKError);
        verify(ConsumerManagerMock, atLeast(1)).getConsumer(any(Authentication.class), any(SDKCallback.class));
    }

    @Test
    public void onLoginResponseWhenSDKErrorIsNotNull() throws Exception {
        when(THSAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(THSSDKError.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(401);
        when(THSSDKError.getHttpResponseCode()).thenReturn(401);
        pthWelcomePresenter.onLoginResponse(THSAuthenticationMock, THSSDKError);
        verify(userMock, atLeast(1)).refreshLoginSession(any(RefreshLoginSessionHandler.class));
    }

    @Test
    public void onLoginResponseWhenNotEnrolledProperly() throws Exception {
        when(THSAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(THSSDKError.getSdkError()).thenReturn(sdkErrorMock);
        when(THSAuthenticationMock.needsToCompleteEnrollment()).thenReturn(true);
        pthWelcomePresenter.onLoginResponse(THSAuthenticationMock, THSSDKError);
        verify(awsdk.getConsumerManager(), atLeast(1)).completeEnrollment(any(Authentication.class), any(State.class), anyString(), anyString(), any(SDKCallback.class));
    }

     @Test
    public void getConsumerThrowsAWSDKInstantiationException() throws Exception {
        when(THSAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        doThrow(AWSDKInstantiationException.class).when(ConsumerManagerMock).getConsumer(any(Authentication.class),any(SDKCallback.class));
        pthWelcomePresenter.onLoginResponse(THSAuthenticationMock, THSSDKError);
         verify(ConsumerManagerMock, atLeast(1)).getConsumer(any(Authentication.class),any(SDKCallback.class));
    }

    @Test
    public void onLoginFailure() throws Exception {
        pthWelcomePresenter.onLoginFailure(throwableMock);
        verify(pTHBaseViewMock).hideProgressBar();
    }

    @Test
    public void onError() throws Exception {
        pthWelcomePresenter.onError(throwableMock);
        verify(pTHBaseViewMock).hideProgressBar();
    }
    @Test
    public void onReceiveConsumerObject(){
        pthWelcomePresenter.onReceiveConsumerObject(consumerMock, sdkErrorMock);
    }

    @Test
    public void onLoginResponserefresh() throws Exception {
        when(THSSDKError.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(THSSDKError.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        pthWelcomePresenter.onLoginResponse(null, THSSDKError);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");

        Object object = new Object();
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);

        verify(userMock).refreshLoginSession(refreshLoginSessionHandlerArgumentCaptor.capture());
        RefreshLoginSessionHandler value = refreshLoginSessionHandlerArgumentCaptor.getValue();
        value.onRefreshLoginSessionSuccess();
        verify(awsdk).authenticateMutual(anyString(), any(SDKCallback.class));
    }

    @Test
    public void onLoginResponserefreshReturnTypeMap() throws Exception {
        when(THSSDKError.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(THSSDKError.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        pthWelcomePresenter.onLoginResponse(null, THSSDKError);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");

        Map object = new HashMap();
        object.put("default","default");
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);

        verify(userMock).refreshLoginSession(refreshLoginSessionHandlerArgumentCaptor.capture());
        RefreshLoginSessionHandler value = refreshLoginSessionHandlerArgumentCaptor.getValue();
        value.onRefreshLoginSessionSuccess();
        verify(awsdk).authenticateMutual(anyString(), any(SDKCallback.class));
    }

    @Test
    public void onLoginResponserefreshReturnTypeMapWhenDefaultValueMissing() throws Exception {
        when(THSSDKError.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(THSSDKError.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        pthWelcomePresenter.onLoginResponse(null, THSSDKError);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");

        Map object = new HashMap();
        //object.put("default","default");
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);

        verify(userMock).refreshLoginSession(refreshLoginSessionHandlerArgumentCaptor.capture());
        RefreshLoginSessionHandler value = refreshLoginSessionHandlerArgumentCaptor.getValue();
        value.onRefreshLoginSessionSuccess();
        verify(awsdk).authenticateMutual(anyString(), any(SDKCallback.class));
    }

    @Test
    public void onLoginResponseOnRefreshLoginSessionFailedWithError() throws Exception {
        when(THSSDKError.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(THSSDKError.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        pthWelcomePresenter.onLoginResponse(null, THSSDKError);

        verify(userMock).refreshLoginSession(refreshLoginSessionHandlerArgumentCaptor.capture());
        RefreshLoginSessionHandler value = refreshLoginSessionHandlerArgumentCaptor.getValue();
        value.onRefreshLoginSessionFailedWithError(anyInt());
        verify(pTHBaseViewMock).hideProgressBar();
    }

    @Test
    public void onLoginResponseOnRefreshLoginSessionInProgress() throws Exception {
        when(THSSDKError.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(THSSDKError.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        pthWelcomePresenter.onLoginResponse(null, THSSDKError);

        verify(userMock).refreshLoginSession(refreshLoginSessionHandlerArgumentCaptor.capture());
        RefreshLoginSessionHandler value = refreshLoginSessionHandlerArgumentCaptor.getValue();
        value.onRefreshLoginSessionInProgress("123");
    }

    @Test
    public void onReceiveConsumerObjectTestAppointments(){
        pthWelcomePresenter.launchInput = 1;
        pthWelcomePresenter.onReceiveConsumerObject(consumerMock,sdkErrorMock);
        verify(pTHBaseViewMock).hideProgressBar();
    }

    @Test
    public void onReceiveConsumerObjectVisitHostory(){
        pthWelcomePresenter.launchInput = 2;
        pthWelcomePresenter.onReceiveConsumerObject(consumerMock,sdkErrorMock);
        verify(pTHBaseViewMock).hideProgressBar();
    }

    @Test
    public void onReceiveConsumerObjectHowItWorks(){
        pthWelcomePresenter.launchInput = 3;
        pthWelcomePresenter.onReceiveConsumerObject(consumerMock,sdkErrorMock);
        verify(pTHBaseViewMock).hideProgressBar();
    }

    @Test
    public void onReceiveConsumerObjectPractices(){
        pthWelcomePresenter.launchInput = 4;
        pthWelcomePresenter.onReceiveConsumerObject(consumerMock,sdkErrorMock);
        verify(pTHBaseViewMock).hideProgressBar();
    }

    @Test
    public void onResponseCheckIserExisits(){
        when(THSSDKError.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(THSSDKError.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
     //   pthWelcomePresenter.onLoginResponse(null, THSSDKError);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");
        when(sdkErrorMock.getSDKErrorReason()).thenReturn(SDKErrorReason.ATTACHMENT_NOT_FOUND);
       // when(sdkErrorReason.name()).thenReturn("sss");

        Map object = new HashMap();
        object.put("default","default");
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);
        pthWelcomePresenter.onResponse(null,THSSDKError);

        verify(pTHBaseViewMock).showToast(anyString());
    }

    @Test
    public void onResponseCheckIserExisitsNoErrorReason(){
        when(THSSDKError.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(THSSDKError.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        //   pthWelcomePresenter.onLoginResponse(null, THSSDKError);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");
       // when(sdkErrorMock.getSDKErrorReason()).thenReturn(SDKErrorReason.ATTACHMENT_NOT_FOUND);
        // when(sdkErrorReason.name()).thenReturn("sss");

        Map object = new HashMap();
        object.put("default","default");
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);
        pthWelcomePresenter.onResponse(null,THSSDKError);

        verify(pTHBaseViewMock).hideProgressBar();
    }

    @Test
    public void onResponseCheckIserExisitsSuccessTrue() {
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");
        Map object = new HashMap();
        object.put("default", "default");
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);
        pthWelcomePresenter.onResponse(true, THSSDKError);
        verify(awsdk).authenticateMutual(anyString(), any(SDKCallback.class));
    }

    @Test
    public void onResponseCheckIserExisitsSuccessTrueException() {
        doThrow(AWSDKInstantiationException.class).when(awsdk).authenticateMutual(anyString(),any(SDKCallback.class));
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");
        Map object = new HashMap();
        object.put("default", "default");
      //  doThrow(AWSDKInitializationException.class).when(awsdk).authenticateMutual(anyString(),any(SDKCallback.class));
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);
        pthWelcomePresenter.onResponse(true, THSSDKError);
        verify(awsdk).authenticateMutual(anyString(), any(SDKCallback.class));
    }

    @Test
    public void checkIserExisitsSuccessException() {

        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);

        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));

        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");
        Map object = new HashMap();
        object.put("default", "default");
        //  doThrow(AWSDKInitializationException.class).when(awsdk).authenticateMutual(anyString(),any(SDKCallback.class));
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);
        pthWelcomePresenter.launchInput = 1;
        pthWelcomePresenter.onEvent(R.id.appointments);
        verify(consumerManagerMock).checkConsumerExists(anyString(), any(SDKCallback.class));
    }

    @Test
    public void onResponseCheckIserExisitsSuccessFalse() {
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        pthWelcomePresenter.onResponse(false, THSSDKError);
        verify(pTHBaseViewMock).hideProgressBar();
    }

    @Test
    public void onFailure(){
        pthWelcomePresenter.onFailure(throwableMock);
    }
}
