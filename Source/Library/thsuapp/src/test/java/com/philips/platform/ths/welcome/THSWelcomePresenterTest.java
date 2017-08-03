package com.philips.platform.ths.welcome;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.cdp.registration.User;
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
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pTHBaseViewMock.mInitButton = buttonMock;
        pthWelcomePresenter = new THSWelcomePresenter(pTHBaseViewMock);
        THSManager.getInstance().setAwsdk(awsdk);
        THSManager.getInstance().setUser(userMock);
        THSManager.getInstance().TEST_FLAG = true;
        when(userMock.getHsdpUUID()).thenReturn("abc");
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(activityMock);
    }

    @Test
    public void onEvent() throws Exception {
        pthWelcomePresenter.onEvent(R.id.init_amwell);
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
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        pthWelcomePresenter.onInitializationResponse(null, THSSDKError);
        verify(consumerManagerMock).checkConsumerExists(anyString(), any(SDKCallback.class));
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
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
        pthWelcomePresenter.onInitializationResponse(null, THSSDKError);
        verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }

    @Test
    public void onLoginResponse() throws Exception {
        when(THSAuthenticationMock.getAuthentication()).thenReturn(authenticationMock);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        pthWelcomePresenter.onLoginResponse(THSAuthenticationMock, THSSDKError);
        verify(ConsumerManagerMock, atLeast(1)).getConsumer(any(Authentication.class), any(SDKCallback.class));
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

}