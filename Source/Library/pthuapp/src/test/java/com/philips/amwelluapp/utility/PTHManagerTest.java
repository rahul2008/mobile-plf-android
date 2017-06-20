package com.philips.amwelluapp.utility;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.AWSDKFactory;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.amwelluapp.login.PTHAuthentication;
import com.philips.amwelluapp.login.PTHLoginCallBack;
import com.philips.amwelluapp.registration.PTHRegistrationDetailCallback;
import com.philips.amwelluapp.registration.PTHState;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.welcome.PTHInitializeCallBack;

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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class PTHManagerTest {
    PTHManager pthManager;

    @Mock
    Context contextMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    AWSDKFactory awsdkFactoryMock;

    @Mock
    PTHLoginCallBack pthLoginCallBackMock;

    @Mock
    PTHInitializeCallBack pthInitializeCallBack;

    @Mock
    SDKCallback sdkCallbackMock;

    @Mock
    Authentication authentication;

    @Mock
    SDKError sdkError;

    @Mock
    Throwable throwable;

    @Captor
    private ArgumentCaptor<SDKCallback<Authentication, SDKError>> requestCaptor;

    @Captor
    private ArgumentCaptor<SDKCallback> initializationCallback;

    @Captor
    private ArgumentCaptor<SDKCallback> pthRegistrationDetailCallbackArgumentCaptor;

    @Mock
    PTHAuthentication pthAuthenticationMock;

    @Mock
    PTHRegistrationDetailCallback pthRegistrationDetailCallbackMock;

    @Mock
    PTHState pthStateMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pthManager = PTHManager.getInstance();
        pthManager.setAwsdk(awsdkMock);
    }

    @Test
    public void getInstance(){
        PTHManager pthManager = PTHManager.getInstance();
        //assertThat(pthManager).isNotNull();
        //assertThat(pthManager).isInstanceOf(PTHManager.class);
    }

    @Test
    public void getAwsdk() throws AWSDKInstantiationException {
        AWSDK awsdk = pthManager.getAwsdk(contextMock);
        //assertThat(awsdk).isNotNull();
        //assertThat(awsdk).isInstanceOf(AWSDK.class);
    }

    @Test
    public void authenticateOnResponse() throws AWSDKInstantiationException {
        pthManager.authenticate(contextMock, "username", "password", "variable", pthLoginCallBackMock);
        verify(awsdkMock).authenticate(eq("username"), eq("password"), eq("variable"), requestCaptor.capture());
        SDKCallback<Authentication, SDKError> value = requestCaptor.getValue();
        value.onResponse(authentication,sdkError);

        verify(pthLoginCallBackMock).onLoginResponse(any(PTHAuthentication.class),any(PTHSDKError.class));
    }


    @Test
    public void authenticateOnFailure() throws AWSDKInstantiationException {
        pthManager.authenticate(contextMock, "username", "password", "variable", pthLoginCallBackMock);
        verify(awsdkMock).authenticate(eq("username"), eq("password"), eq("variable"), requestCaptor.capture());
        SDKCallback<Authentication, SDKError> value = requestCaptor.getValue();
        value.onFailure(throwable);

        verify(pthLoginCallBackMock).onLoginFailure(throwable);
    }

    @Test
    public void initializeTeleHealthOnFailure() throws MalformedURLException, AWSDKInstantiationException, AWSDKInitializationException, URISyntaxException {
        pthManager.initializeTeleHealth(contextMock,pthInitializeCallBack);

        verify(awsdkMock).initialize(any(Map.class),initializationCallback.capture());
        SDKCallback value = initializationCallback.getValue();
        value.onFailure(throwable);

        verify(pthInitializeCallBack).onInitializationFailure(throwable);
    }

    @Test
    public void initializeTeleHealthOnResponse() throws MalformedURLException, AWSDKInstantiationException, AWSDKInitializationException, URISyntaxException {
        pthManager.initializeTeleHealth(contextMock,pthInitializeCallBack);

        verify(awsdkMock).initialize(any(Map.class),initializationCallback.capture());
        SDKCallback value = initializationCallback.getValue();
        value.onResponse(any(Object.class),any(SDKError.class));

        verify(pthInitializeCallBack).onInitializationResponse(any(Object.class),any(PTHSDKError.class));
    }

}