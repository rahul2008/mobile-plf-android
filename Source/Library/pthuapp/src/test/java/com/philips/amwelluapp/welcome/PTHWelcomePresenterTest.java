package com.philips.amwelluapp.welcome;

import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.amwelluapp.login.PTHAuthentication;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.utility.PTHManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PTHWelcomePresenterTest {

    PTHWelcomePresenter pthWelcomePresenter;

    @Mock
    PTHWelcomeFragment pTHBaseViewMock;

    @Captor
    private ArgumentCaptor<SDKCallback> initializationCallback;

    @Mock
    PTHSDKError pthsdkError;

    @Mock
    PTHAuthentication pthAuthenticationMock;

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


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pthWelcomePresenter = new PTHWelcomePresenter(pTHBaseViewMock);
        PTHManager.getInstance().setAwsdk(awsdk);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(activityMock);
    }

    @Test
    public void onEvent() throws Exception {
        //TODO: Since its empty I am not writing any assertions for it
        pthWelcomePresenter.onEvent(-1);
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
       pthWelcomePresenter.onInitializationResponse(null, pthsdkError);
        verify(awsdk).authenticate(anyString(),anyString(),anyString(),any(SDKCallback.class));
    }

    @Test
    public void onInitializationFailure()  {
        pthWelcomePresenter.onInitializationFailure(throwableMock);
        verify(pTHBaseViewMock).hideProgressBar();
    }


    @Test
    public void authenticateThrowsAWSDKInstantiationException() throws Exception {
        doThrow(AWSDKInstantiationException.class).when(awsdk).authenticate(anyString(),anyString(),anyString(),any(SDKCallback.class));
        pthWelcomePresenter.onInitializationResponse(null, pthsdkError);
        verify(awsdk).authenticate(anyString(),anyString(),anyString(),any(SDKCallback.class));
    }

   @Test
    public void onLoginResponse() throws Exception {
        when(pthAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        pthWelcomePresenter.onLoginResponse(pthAuthenticationMock, pthsdkError);
        verify(ConsumerManagerMock, atLeast(1)).getConsumer(any(Authentication.class),any(SDKCallback.class));
    }

     @Test
    public void getConsumerThrowsAWSDKInstantiationException() throws Exception {
        when(pthAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        doThrow(AWSDKInstantiationException.class).when(ConsumerManagerMock).getConsumer(any(Authentication.class),any(SDKCallback.class));
        pthWelcomePresenter.onLoginResponse(pthAuthenticationMock, pthsdkError);
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

}