package com.philips.amwelluapp.utility;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.AWSDKFactory;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.ConsumerType;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.entity.practice.OnDemandSpecialty;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderType;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.entity.visit.Vitals;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.VisitManager;
import com.philips.amwelluapp.intake.PTHVisitContext;
import com.philips.amwelluapp.intake.PTHVisitContextCallBack;
import com.philips.amwelluapp.intake.THSConditionsCallBack;
import com.philips.amwelluapp.intake.THSVitalSDKCallback;
import com.philips.amwelluapp.intake.THSVitals;
import com.philips.amwelluapp.login.PTHAuthentication;
import com.philips.amwelluapp.login.PTHLoginCallBack;
import com.philips.amwelluapp.practice.PTHPracticesListCallback;
import com.philips.amwelluapp.providerdetails.PTHProviderDetailsCallback;
import com.philips.amwelluapp.providerslist.PTHProviderInfo;
import com.philips.amwelluapp.providerslist.PTHProvidersListCallback;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.registration.PTHRegistrationDetailCallback;
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
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PTHManagerTest {
    PTHManager pthManager;

    Consumer mConsumer;

    Practice practice;

    ProviderInfo providerInfo;

    @Mock
    PTHProviderInfo pthProviderInfo;

    @Mock
    VisitContext visitContextMock;

    @Mock
    THSConditionsCallBack thsConditionsCallback;

    @Mock
    THSVitalSDKCallback thsVitalCallBackMock;

    @Mock
    VisitManager visitManagerMock;

    @Mock
    PracticeProvidersManager practiseprovidermanagerMock;

    @Mock
    Context contextMock;

    @Mock
    Consumer consumerMock;

    @Mock
    ProviderInfo providerInfoMock;

    @Mock
    PTHConsumer pthConsumerMock;

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
    private ArgumentCaptor<SDKCallback> visitContextCaptor;

    @Captor
    private ArgumentCaptor<SDKCallback> vitalsCaptor;

    @Captor
    private ArgumentCaptor<SDKCallback> conditionsCaptor;

    @Captor
    private ArgumentCaptor<SDKCallback> pthRegistrationDetailCallbackArgumentCaptor;

    @Mock
    PTHAuthentication pthAuthenticationMock;

    @Mock
    PTHRegistrationDetailCallback pthRegistrationDetailCallbackMock;

    @Mock
    PTHVisitContextCallBack pthVisitContextCallBack;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    PTHVisitContext pthVisitContextMock;


    @Mock
    PTHPracticesListCallback pTHPracticesListCallback;

    @Captor
    private ArgumentCaptor<SDKCallback<List<Practice>, SDKError>> requestPracticeCaptor;

    @Mock
    PTHProvidersListCallback pthProvidersListCallback;

    @Captor
    private ArgumentCaptor<SDKCallback<List<ProviderInfo>,SDKError>> providerListCaptor;

    @Mock
    PTHProviderDetailsCallback pthProviderDetailsCallback;

    @Captor
    private ArgumentCaptor<SDKCallback<Provider,SDKError>> providerDetailsCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pthManager = PTHManager.getInstance();
        pthManager.setAwsdk(awsdkMock);
        mConsumer = getConsumer();
        practice = getPractice();
        providerInfo = getProviderInfo();
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
    public void getVisitContext() throws MalformedURLException, AWSDKInstantiationException, AWSDKInitializationException, URISyntaxException {
        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(pthProviderInfo.getProviderInfo()).thenReturn(providerInfoMock);
        when(awsdkMock.getVisitManager()).thenReturn(visitManagerMock);
        pthManager.getVisitContext(contextMock, pthConsumerMock,pthProviderInfo,pthVisitContextCallBack);

        verify(visitManagerMock).getVisitContext(any(Consumer.class), any(ProviderInfo.class),visitContextCaptor.capture());
        SDKCallback value = visitContextCaptor.getValue();
        value.onResponse(any(VisitContext.class), any(SDKError.class));

        verify(pthVisitContextCallBack).onResponse(any(PTHVisitContext.class), any(PTHSDKError.class));
    }

    @Test
    public void getVisitContextOnFailure() throws MalformedURLException, AWSDKInstantiationException, AWSDKInitializationException, URISyntaxException {
        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(pthProviderInfo.getProviderInfo()).thenReturn(providerInfoMock);
        when(awsdkMock.getVisitManager()).thenReturn(visitManagerMock);
        pthManager.getVisitContext(contextMock, pthConsumerMock,pthProviderInfo,pthVisitContextCallBack);

        verify(visitManagerMock).getVisitContext(any(Consumer.class), any(ProviderInfo.class),visitContextCaptor.capture());
        SDKCallback value = visitContextCaptor.getValue();
        value.onFailure(any(Throwable.class));

        verify(pthVisitContextCallBack).onFailure(any(Throwable.class));
    }

    @Test
    public void getVitals() throws AWSDKInstantiationException {
        pthManager.setPTHConsumer(pthConsumerMock);
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        pthManager.getVitals(contextMock, pthVisitContextMock,thsVitalCallBackMock);

        verify(consumerManagerMock).getVitals(any(Consumer.class),any(VisitContext.class), vitalsCaptor.capture());
        SDKCallback value = vitalsCaptor.getValue();
        value.onResponse(any(Vitals.class), any(SDKError.class));

        verify(thsVitalCallBackMock).onResponse(any(THSVitals.class), any(PTHSDKError.class));
    }

    @Test
    public void getVitalsOnFailure() throws AWSDKInstantiationException {
        pthManager.setPTHConsumer(pthConsumerMock);
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        pthManager.getVitals(contextMock, pthVisitContextMock,thsVitalCallBackMock);

        verify(consumerManagerMock).getVitals(any(Consumer.class),any(VisitContext.class), vitalsCaptor.capture());
        SDKCallback value = vitalsCaptor.getValue();
        value.onFailure(any(Throwable.class));

        verify(thsVitalCallBackMock).onFailure(any(Throwable.class));
    }

    @Test
    public void getConditions() throws AWSDKInstantiationException {
        pthManager.setPTHConsumer(pthConsumerMock);

        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);


        pthManager.getConditions(contextMock,thsConditionsCallback);

        verify(consumerManagerMock).getConditions(any(Consumer.class), conditionsCaptor.capture());
        SDKCallback value = conditionsCaptor.getValue();
        value.onResponse(any(List.class), any(SDKError.class));

        verify(thsConditionsCallback).onResponse(any(List.class), any(PTHSDKError.class));
    }

    @Test
    public void getOnFailure() throws AWSDKInstantiationException {
        pthManager.setPTHConsumer(pthConsumerMock);

        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);


        pthManager.getConditions(contextMock,thsConditionsCallback);

        verify(consumerManagerMock).getConditions(any(Consumer.class), conditionsCaptor.capture());
        SDKCallback value = conditionsCaptor.getValue();
        value.onFailure(any(Throwable.class));

        verify(thsConditionsCallback).onFailure(any(Throwable.class));
    }


    @Test
    public void getPracticesOnResponse() throws AWSDKInstantiationException {
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        pthManager.getPractices(contextMock, mConsumer, pTHPracticesListCallback);
        verify(practiseprovidermanagerMock).getPractices(any(Consumer.class), requestPracticeCaptor.capture());
        SDKCallback value = requestPracticeCaptor.getValue();
        value.onResponse(any(List.class), any(SDKError.class));

    }

    @Test
    public void getProviderListTest() throws AWSDKInstantiationException {
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        pthManager.getProviderList(contextMock,mConsumer,practice,pthProvidersListCallback);
        verify(practiseprovidermanagerMock).findProviders(any(Consumer.class), any(Practice.class), any(OnDemandSpecialty.class), any(String.class), any(Set.class), any(Set.class), any(State.class), any(Language.class), any(Integer.class), providerListCaptor.capture());
        SDKCallback value =  providerListCaptor.getValue();
        value.onResponse(any(List.class),any(SDKError.class));
        value.onFailure(any(Throwable.class));
    }

    @Test
    public void getProviderDetailsTest() throws AWSDKInstantiationException{
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        pthManager.getProviderDetails(contextMock,mConsumer,providerInfo,pthProviderDetailsCallback);
        verify(practiseprovidermanagerMock).getProvider(any(ProviderInfo.class),any(Consumer.class),providerDetailsCaptor.capture());
        SDKCallback value = providerDetailsCaptor.getValue();
        value.onResponse(any(Provider.class), any(SDKError.class));
        value.onFailure(any(Throwable.class));
    }

    @Test
    public void getConsumerTest(){
        pthManager.setPTHConsumer(pthConsumerMock);
        PTHConsumer consumer = pthManager.getPTHConsumer();
        assertThat(consumer).isNotNull();
        assertThat(consumer).isInstanceOf(PTHConsumer.class);
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

    public Practice getPractice() {

        Practice practiceObj = new Practice() {
            @Override
            public Address getAddress() {
                return null;
            }

            @Override
            public String getPhone() {
                return null;
            }

            @Override
            public String getFax() {
                return null;
            }

            @Override
            public String getHours() {
                return null;
            }

            @Override
            public String getWelcomeMessage() {
                return null;
            }

            @Override
            public boolean isShowScheduling() {
                return false;
            }

            @Override
            public boolean isShowAvailableNow() {
                return false;
            }

            @Override
            public String getPracticeType() {
                return "Practice Test";
            }

            @Override
            public boolean hasLogo() {
                return true;
            }

            @Override
            public boolean hasSmallLogo() {
                return true;
            }

            @Override
            public String getName() {
                return "TS Patient Test";
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };

        return practiceObj;
    }

    public ProviderInfo getProviderInfo() {

        ProviderInfo providerInfoObj = new ProviderInfo() {
            @Override
            public ProviderVisibility getVisibility() {
                return ProviderVisibility.WEB_AVAILABLE;
            }

            @Nullable
            @Override
            public PracticeInfo getPracticeInfo() {
                return null;
            }

            @Override
            public ProviderType getSpecialty() {
                return null;
            }

            @Override
            public int getRating() {
                return 4;
            }

            @Override
            public Integer getWaitingRoomCount() {
                return null;
            }

            @Override
            public Gender getGender() {
                return Gender.MALE;
            }

            @Override
            public String getSourceId() {
                return null;
            }

            @Override
            public boolean hasImage() {
                return true;
            }

            @NonNull
            @Override
            public String getFirstName() {
                return "Dr. Welsh";
            }

            @Nullable
            @Override
            public String getMiddleInitial() {
                return null;
            }

            @NonNull
            @Override
            public String getLastName() {
                return "Something";
            }

            @NonNull
            @Override
            public String getFullName() {
                return "Dr. Welsh Something";
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };
        return providerInfoObj;
    }
}