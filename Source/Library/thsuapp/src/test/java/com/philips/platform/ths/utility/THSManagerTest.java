package com.philips.platform.ths.utility;

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
import com.americanwell.sdk.entity.SDKPasswordError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.ConsumerType;
import com.americanwell.sdk.entity.consumer.ConsumerUpdate;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.health.Medication;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.entity.practice.OnDemandSpecialty;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.entity.visit.Vitals;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.SDKValidatedCallback;
import com.americanwell.sdk.manager.VisitManager;
import com.philips.platform.ths.intake.THSConditionsCallBack;
import com.philips.platform.ths.intake.THSMedication;
import com.philips.platform.ths.intake.THSMedicationCallback;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.intake.THSUpdateConsumerCallback;
import com.philips.platform.ths.intake.THSVisitContext;
import com.philips.platform.ths.intake.THSVisitContextCallBack;
import com.philips.platform.ths.intake.THSVitalSDKCallback;
import com.philips.platform.ths.intake.THSVitals;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.login.THSLoginCallBack;
import com.philips.platform.ths.practice.THSPracticesListCallback;
import com.philips.platform.ths.providerdetails.THSProviderDetailsCallback;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.providerslist.THSProvidersListCallback;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.registration.THSRegistrationDetailCallback;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.welcome.THSInitializeCallBack;

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

public class THSManagerTest {
    THSManager THSManager;

    THSConsumer mTHSConsumer;

    Consumer mConsumer;

    @Mock
    THSMedication mTHSMedication;

    @Mock
    Consumer mConsumerMock;

    Practice practice;

    THSProviderInfo providerInfo;

    @Mock
    THSProviderInfo THSProviderInfo;

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
    THSConsumer THSConsumerMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    AWSDKFactory awsdkFactoryMock;

    @Mock
    THSLoginCallBack THSLoginCallBackMock;

    @Mock
    THSInitializeCallBack THSInitializeCallBack;

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
    THSAuthentication THSAuthenticationMock;

    @Mock
    THSRegistrationDetailCallback THSRegistrationDetailCallbackMock;

    @Mock
    THSVisitContextCallBack THSVisitContextCallBack;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    THSVisitContext THSVisitContextMock;
    ConsumerUpdate consumerUpdateMock;


    @Mock
    THSPracticesListCallback pTHPracticesListCallback;

    @Captor
    private ArgumentCaptor<SDKCallback<List<Practice>, SDKError>> requestPracticeCaptor;

    @Mock
    THSProvidersListCallback THSProvidersListCallback;

    @Captor
    private ArgumentCaptor<SDKCallback<List<ProviderInfo>, SDKError>> providerListCaptor;

    @Mock
    THSProviderDetailsCallback THSProviderDetailsCallback;

    @Captor
    private ArgumentCaptor<SDKCallback<Provider, SDKError>> providerDetailsCaptor;

    @Mock
    THSMedicationCallback.PTHGetMedicationCallback pTHGetMedicationCallbackMock;
    @Captor
    private ArgumentCaptor<SDKCallback<List<Medication>, SDKError>> getMedicationCaptor;


    @Mock
    THSSDKValidatedCallback pTHSDKValidatedCallback;
    @Captor
    private ArgumentCaptor<SDKValidatedCallback<List<Medication>, SDKError>> getSearchedMedicationCaptor;

    @Mock
    THSMedicationCallback.PTHUpdateMedicationCallback pTHUpdateMedicationCallback;
    @Captor
    private ArgumentCaptor<SDKCallback<Void, SDKError>> getUpdateMedicationCaptor;

    @Mock
    THSUpdateConsumerCallback pTHUpdateConsumerCallback;
    @Captor
    private ArgumentCaptor<SDKValidatedCallback<Consumer, SDKPasswordError>> getUpdateConsumerCaptor;



    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        THSManager = com.philips.platform.ths.utility.THSManager.getInstance();
        THSManager.setAwsdk(awsdkMock);
        mConsumer = getConsumer();
        practice = getPractice();
        providerInfo = THSProviderInfo;
        mTHSConsumer = new THSConsumer();
        mTHSConsumer.setConsumer(getConsumer());
        THSManager.setPTHConsumer(mTHSConsumer);
    }

    @Test
    public void getInstance() {
        THSManager THSManager = com.philips.platform.ths.utility.THSManager.getInstance();
        assertThat(THSManager).isNotNull();
        assertThat(THSManager).isInstanceOf(THSManager.class);
    }

    @Test
    public void getAwsdk() throws AWSDKInstantiationException {
        AWSDK awsdk = THSManager.getAwsdk(contextMock);
        assertThat(awsdk).isNotNull();
        assertThat(awsdk).isInstanceOf(AWSDK.class);
    }

    @Test
    public void authenticateOnResponse() throws AWSDKInstantiationException {
        THSManager.authenticate(contextMock, "username", "password", "variable", THSLoginCallBackMock);
        verify(awsdkMock).authenticate(eq("username"), eq("password"), eq("variable"), requestCaptor.capture());
        SDKCallback<Authentication, SDKError> value = requestCaptor.getValue();
        value.onResponse(authentication, sdkError);

        verify(THSLoginCallBackMock).onLoginResponse(any(THSAuthentication.class), any(THSSDKError.class));
    }


    @Test
    public void authenticateOnFailure() throws AWSDKInstantiationException {
        THSManager.authenticate(contextMock, "username", "password", "variable", THSLoginCallBackMock);
        verify(awsdkMock).authenticate(eq("username"), eq("password"), eq("variable"), requestCaptor.capture());
        SDKCallback<Authentication, SDKError> value = requestCaptor.getValue();
        value.onFailure(throwable);

        verify(THSLoginCallBackMock).onLoginFailure(throwable);
    }

    @Test
    public void initializeTeleHealthOnFailure() throws MalformedURLException, AWSDKInstantiationException, AWSDKInitializationException, URISyntaxException {
        THSManager.initializeTeleHealth(contextMock, THSInitializeCallBack);

        verify(awsdkMock).initialize(any(Map.class), initializationCallback.capture());
        SDKCallback value = initializationCallback.getValue();
        value.onFailure(throwable);

        verify(THSInitializeCallBack).onInitializationFailure(throwable);
    }

    @Test
    public void initializeTeleHealthOnResponse() throws MalformedURLException, AWSDKInstantiationException, AWSDKInitializationException, URISyntaxException {
        THSManager.initializeTeleHealth(contextMock, THSInitializeCallBack);

        verify(awsdkMock).initialize(any(Map.class), initializationCallback.capture());
        SDKCallback value = initializationCallback.getValue();
        value.onResponse(any(Object.class), any(SDKError.class));

        verify(THSInitializeCallBack).onInitializationResponse(any(Object.class), any(THSSDKError.class));
    }

    @Test
    public void getVisitContext() throws MalformedURLException, AWSDKInstantiationException, AWSDKInitializationException, URISyntaxException {
        when(THSConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(THSProviderInfo.getProviderInfo()).thenReturn(providerInfoMock);
        when(awsdkMock.getVisitManager()).thenReturn(visitManagerMock);
        THSManager.getVisitContext(contextMock, THSProviderInfo, THSVisitContextCallBack);

        verify(visitManagerMock).getVisitContext(any(Consumer.class), any(ProviderInfo.class),visitContextCaptor.capture());
        SDKCallback value = visitContextCaptor.getValue();
        value.onResponse(any(VisitContext.class), any(SDKError.class));

        verify(THSVisitContextCallBack).onResponse(any(THSVisitContext.class), any(THSSDKError.class));
    }

    @Test
    public void getVisitContextOnFailure() throws MalformedURLException, AWSDKInstantiationException, AWSDKInitializationException, URISyntaxException {
        when(THSConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(THSProviderInfo.getProviderInfo()).thenReturn(providerInfoMock);
        when(awsdkMock.getVisitManager()).thenReturn(visitManagerMock);
        THSManager.getVisitContext(contextMock, THSProviderInfo, THSVisitContextCallBack);

        verify(visitManagerMock).getVisitContext(any(Consumer.class), any(ProviderInfo.class),visitContextCaptor.capture());
        SDKCallback value = visitContextCaptor.getValue();
        value.onFailure(any(Throwable.class));

        verify(THSVisitContextCallBack).onFailure(any(Throwable.class));
    }

    @Test
    public void getVitals() throws AWSDKInstantiationException {
        THSManager.setPTHConsumer(THSConsumerMock);
        when(THSVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(THSConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.getVitals(contextMock, thsVitalCallBackMock);

        verify(consumerManagerMock).getVitals(any(Consumer.class),any(VisitContext.class), vitalsCaptor.capture());
        SDKCallback value = vitalsCaptor.getValue();
        value.onResponse(any(Vitals.class), any(SDKError.class));

        verify(thsVitalCallBackMock).onResponse(any(THSVitals.class), any(THSSDKError.class));
    }

    @Test
    public void getVitalsOnFailure() throws AWSDKInstantiationException {
        THSManager.setPTHConsumer(THSConsumerMock);
        when(THSVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(THSConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.getVitals(contextMock, thsVitalCallBackMock);

        verify(consumerManagerMock).getVitals(any(Consumer.class),any(VisitContext.class), vitalsCaptor.capture());
        SDKCallback value = vitalsCaptor.getValue();
        value.onFailure(any(Throwable.class));

        verify(thsVitalCallBackMock).onFailure(any(Throwable.class));
    }

    @Test
    public void getConditions() throws AWSDKInstantiationException {
        THSManager.setPTHConsumer(THSConsumerMock);

        when(THSConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);


        THSManager.getConditions(contextMock,thsConditionsCallback);

        verify(consumerManagerMock).getConditions(any(Consumer.class), conditionsCaptor.capture());
        SDKCallback value = conditionsCaptor.getValue();
        value.onResponse(any(List.class), any(SDKError.class));

        verify(thsConditionsCallback).onResponse(any(List.class), any(THSSDKError.class));
    }

    @Test
    public void getOnFailure() throws AWSDKInstantiationException {
        THSManager.setPTHConsumer(THSConsumerMock);

        when(THSConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);


        THSManager.getConditions(contextMock,thsConditionsCallback);

        verify(consumerManagerMock).getConditions(any(Consumer.class), conditionsCaptor.capture());
        SDKCallback value = conditionsCaptor.getValue();
        value.onFailure(any(Throwable.class));

        verify(thsConditionsCallback).onFailure(any(Throwable.class));
    }


    @Test
    public void getPracticesOnResponse() throws AWSDKInstantiationException {
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        THSManager.getPractices(contextMock, pTHPracticesListCallback);
        verify(practiseprovidermanagerMock).getPractices(any(Consumer.class), requestPracticeCaptor.capture());
        SDKCallback value = requestPracticeCaptor.getValue();
        value.onResponse(any(List.class), any(SDKError.class));
        value.onFailure(any(Throwable.class));
    }

    @Test
    public void getProviderListTest() throws AWSDKInstantiationException {
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        THSManager.getProviderList(contextMock, mConsumer, practice, THSProvidersListCallback);
        verify(practiseprovidermanagerMock).findProviders(any(Consumer.class), any(Practice.class), any(OnDemandSpecialty.class), any(String.class), any(Set.class), any(Set.class), any(State.class), any(Language.class), any(Integer.class), providerListCaptor.capture());
        SDKCallback value = providerListCaptor.getValue();
//        value.onResponse(any(List.class), any(SDKError.class));
        value.onFailure(any(Throwable.class));
    }

    @Test
    public void getProviderDetailsTest() throws AWSDKInstantiationException {
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        THSManager.getProviderDetails(contextMock, mConsumer, providerInfo, THSProviderDetailsCallback);
        verify(practiseprovidermanagerMock).getProvider(any(ProviderInfo.class), any(Consumer.class), providerDetailsCaptor.capture());
        SDKCallback value = providerDetailsCaptor.getValue();
        value.onResponse(any(Provider.class), any(SDKError.class));
        value.onFailure(any(Throwable.class));
    }

    @Test
    public void getConsumerTest(){
        THSManager.setPTHConsumer(mTHSConsumer);
        THSConsumer consumer = THSManager.getPTHConsumer();
        assertThat(consumer).isNotNull();
        assertThat(consumer).isInstanceOf(THSConsumer.class);
    }

    public void getMedicationTest() throws AWSDKInstantiationException {
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.getMedication(contextMock, pTHGetMedicationCallbackMock);
        verify(consumerManagerMock).getMedications(any(Consumer.class), getMedicationCaptor.capture());
        SDKCallback value = getMedicationCaptor.getValue();
        value.onResponse(any(List.class), any(SDKError.class));
        value.onFailure(any(Throwable.class));
    }

    @Test
    public void searchMedicationTest() throws AWSDKInstantiationException {
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.searchMedication(contextMock, "dol" ,pTHSDKValidatedCallback);
        verify(consumerManagerMock).searchMedications(any(Consumer.class), any(String.class),getSearchedMedicationCaptor.capture());
        SDKCallback value = getSearchedMedicationCaptor.getValue();
//        value.onResponse(any(List.class), any(SDKError.class));
        //value.onFailure(any(Throwable.class));  //todo
    }

    @Test
    public void updateMedicationTest()throws AWSDKInstantiationException {
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.updateMedication(contextMock, mTHSMedication,pTHUpdateMedicationCallback);
        verify(consumerManagerMock).updateMedications(any(Consumer.class), any(List.class),getUpdateMedicationCaptor.capture());
        SDKCallback value = getUpdateMedicationCaptor.getValue();
        value.onResponse(any(List.class), any(SDKPasswordError.class));
//        value.onFailure(any(Throwable.class));
    }


    // follow up test
  /*  @Test
    public void updateConsumerTest()throws AWSDKInstantiationException {
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(consumerManagerMock.getNewConsumerUpdate(any(Consumer.class))).thenReturn(consumerUpdateMock);
        THSManager.updateConsumer(contextMock, "67767262" ,pTHUpdateConsumerCallback);
        verify(consumerManagerMock).updateConsumer(any(ConsumerUpdate.class),getUpdateConsumerCaptor.capture());
        SDKCallback value = getUpdateConsumerCaptor.getValue();
        value.onResponse(any(Consumer.class), any(SDKError.class));
        value.onFailure(any(Throwable.class));
    }*/


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
}