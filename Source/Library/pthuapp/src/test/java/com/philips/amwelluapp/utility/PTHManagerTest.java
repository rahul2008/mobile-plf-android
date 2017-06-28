package com.philips.amwelluapp.utility;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.AWSDKFactory;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.ConsumerType;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.amwelluapp.login.PTHAuthentication;
import com.philips.amwelluapp.login.PTHLoginCallBack;
import com.philips.amwelluapp.practice.PTHPractice;
import com.philips.amwelluapp.practice.PTHPracticesListCallback;
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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PTHManagerTest {
    PTHManager pthManager;


    Consumer mConsumer;

    @Mock
    PracticeProvidersManager practiseprovidermanagerMock;

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


    @Mock
    PTHPracticesListCallback pTHPracticesListCallback;

    @Captor
    private ArgumentCaptor<SDKCallback<List<Practice>, SDKError>> requestPracticeCaptor;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pthManager = PTHManager.getInstance();
        pthManager.setAwsdk(awsdkMock);
        mConsumer = getConsumer();
    }

    @Test
    public void getInstance() {
        PTHManager pthManager = PTHManager.getInstance();
        assertThat(pthManager).isNotNull();
        assertThat(pthManager).isInstanceOf(PTHManager.class);
    }

    @Test
    public void getAwsdk() throws AWSDKInstantiationException {
        AWSDK awsdk = pthManager.getAwsdk(contextMock);
        assertThat(awsdk).isNotNull();
        assertThat(awsdk).isInstanceOf(AWSDK.class);
    }

    @Test
    public void authenticateOnResponse() throws AWSDKInstantiationException {
        pthManager.authenticate(contextMock, "username", "password", "variable", pthLoginCallBackMock);
        verify(awsdkMock).authenticate(eq("username"), eq("password"), eq("variable"), requestCaptor.capture());
        SDKCallback<Authentication, SDKError> value = requestCaptor.getValue();
        value.onResponse(authentication, sdkError);

        verify(pthLoginCallBackMock).onLoginResponse(any(PTHAuthentication.class), any(PTHSDKError.class));
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
        pthManager.initializeTeleHealth(contextMock, pthInitializeCallBack);

        verify(awsdkMock).initialize(any(Map.class), initializationCallback.capture());
        SDKCallback value = initializationCallback.getValue();
        value.onFailure(throwable);

        verify(pthInitializeCallBack).onInitializationFailure(throwable);
    }

    @Test
    public void initializeTeleHealthOnResponse() throws MalformedURLException, AWSDKInstantiationException, AWSDKInitializationException, URISyntaxException {
        pthManager.initializeTeleHealth(contextMock, pthInitializeCallBack);

        verify(awsdkMock).initialize(any(Map.class), initializationCallback.capture());
        SDKCallback value = initializationCallback.getValue();
        value.onResponse(any(Object.class), any(SDKError.class));

        verify(pthInitializeCallBack).onInitializationResponse(any(Object.class), any(PTHSDKError.class));
    }


    @Test
    public void getPracticesOnResponse() throws AWSDKInstantiationException {
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        pthManager.getPractices(contextMock, mConsumer, pTHPracticesListCallback);
        verify(practiseprovidermanagerMock).getPractices(any(Consumer.class), requestPracticeCaptor.capture());
        SDKCallback value = requestPracticeCaptor.getValue();
        value.onResponse(any(List.class), any(SDKError.class));

    }

    Consumer getConsumer() {
        Consumer consumer = new Consumer() {
            @Override
            public Gender getGender() {
                return Gender.MALE;
            }

            @Override
            public String getAge() {
                return "34";
            }

            @Override
            public String getFormularyRestriction() {
                return null;
            }

            @Override
            public boolean isEligibleForVisit() {
                return false;
            }

            @Override
            public boolean isEnrolled() {
                return true;
            }

            @Override
            public Subscription getSubscription() {
                return null;
            }

            @Override
            public String getPhone() {
                return "7899673388";
            }

            @Override
            public List<Consumer> getDependents() {
                return null;
            }

            @Override
            public boolean isDependent() {
                return false;
            }

            @Override
            public SDKLocalDate getDob() {
                return null;
            }

            @Override
            public Address getAddress() {
                return null;
            }

            @Override
            public ConsumerType getConsumerType() {
                return null;
            }

            @Override
            public String getEmail() {
                return null;
            }

            @Override
            public State getLegalResidence() {
                return null;
            }

            @NonNull
            @Override
            public String getFirstName() {
                return "Ravi";
            }

            @Nullable
            @Override
            public String getMiddleInitial() {
                return null;
            }

            @NonNull
            @Override
            public String getLastName() {
                return "Kumar";
            }

            @NonNull
            @Override
            public String getFullName() {
                return "Ravi Kumar";
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };
        return consumer;
    }

}