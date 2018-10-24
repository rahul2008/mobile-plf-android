package com.philips.platform.datasync.moments;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.events.MomentDataSenderCreatedRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.philips.testing.verticals.TestEntityCreator;
import com.philips.testing.verticals.table.TestMoment;
import com.philips.testing.verticals.table.TestMomentType;
import com.philips.testing.verticals.table.TestSynchronisationData;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MomentsDataSenderTest {

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String USER_ID = "TEST_GUID";
    private static final String BABY_ID = "BABY_ID";
    private static final DateTime DATE_TIME = DateTime.now();
    private static final String CREATOR_ID = "1234";
    private static final String SUBJECT_ID = "5678";
    private static final int MOMENT_TYPE_ID = 1;
    private static final String MOMENT_TYPE = "Temperature";
    public static final String GUID = "someGuidGoesHere";
    private final String TEST_MOMENT_ID = "TEST_MOMENT_ID";
    private final String TEST_MOMENT_URL = "http://xyx";

    private MomentsDataSender momentsDataSender;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private ServiceDiscoveryInterface serviceDiscoveryInterface;

    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private MomentsConverter momentsConverterMock;

    @Mock
    private Eventing eventingMock;

    @Mock
    private MomentsClient clientMock;

    @Mock
    private TypedByteArray typedByteArrayMock;

    @Captor
    private ArgumentCaptor<BackendResponse> errorEventCaptor;

    @Mock
    private Moment momentMock;

    @Mock
    private UserRegistrationInterface userRegistrationInterfaceMock;

    @Mock
    private SynchronisationData synchronisationDataMock;

    @Mock
    private SynchronisationManager synchronisationManagerMock;

    @Mock
    private MomentGsonConverter momentGsonConverterMock;

    @Mock
    private UCoreMoment uCoreMomentMock;

    @Mock
    private UCoreMoment uCoreMomentResponseMock;

    @Mock
    private BaseAppDataCreator uGrowDataCreatorMock;

    @Mock
    private RetrofitError retrofitErrorMock;

    private TestEntityCreator verticalDataCreater;

    @Mock
    private AppComponent appComponantMock;

    @Mock
    ServiceDiscoveryInterface.OnGetServiceUrlListener listenerMock;

    private String TEST_DATE_TIME_STRING = "2020-10-01T14:44:00.000Z";

    @Before
    public void setUp() {
        initMocks(this);
        verticalDataCreater = new TestEntityCreator(new UuidGenerator());
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        DataServicesManager.getInstance().setServiceDiscoveryInterface(serviceDiscoveryInterface);
        DataServicesManager.getInstance().mDataServicesBaseUrl = TEST_MOMENT_URL;
        when(accessProviderMock.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(USER_ID);
        when(accessProviderMock.getSubjectId()).thenReturn(BABY_ID);
        when(uCoreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, momentGsonConverterMock)).thenReturn(clientMock);
        when(uCoreAdapterMock.getClient(MomentsClient.class, TEST_MOMENT_URL, ACCESS_TOKEN, momentGsonConverterMock)).thenReturn(clientMock);
        when(clientMock.saveMoment(USER_ID, USER_ID, uCoreMomentMock)).thenReturn(uCoreMomentMock);

        momentsDataSender = new MomentsDataSender(momentsConverterMock, momentGsonConverterMock);
        momentsDataSender.accessProvider = accessProviderMock;
        momentsDataSender.uCoreAdapter = uCoreAdapterMock;
        momentsDataSender.baseAppDataCreater = verticalDataCreater;
        momentsDataSender.eventing = eventingMock;
        momentsDataSender.userRegistrationImpl = userRegistrationInterfaceMock;
        momentsDataSender.synchronisationManager = synchronisationManagerMock;

        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(momentMock.getCreatorId()).thenReturn(USER_ID);
        when(momentMock.getSubjectId()).thenReturn(BABY_ID);
    }

    @Test
    public void ShouldNotCallSendMoments_WhenUserIsNotLoggedIn() {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        boolean sendDataToBackend = momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verifyZeroInteractions(clientMock);
        assertThat(sendDataToBackend).isFalse();
    }

    @Test
    public void ShouldNotCallSendMoments_When_Moments_isNull() {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);

        boolean sendDataToBackend = momentsDataSender.sendDataToBackend(null);

        verifyZeroInteractions(clientMock);
        assertThat(sendDataToBackend).isFalse();
    }

    @Test
    public void ShouldNotCallSendMoments_WhenUserAccessTokenIsNull() {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);

        when(uCoreAdapterMock.getAppFrameworkClient(MomentsClient.class, null, momentGsonConverterMock)).thenReturn(clientMock);
        when(uCoreAdapterMock.getClient(MomentsClient.class, TEST_MOMENT_URL, null, momentGsonConverterMock)).thenReturn(clientMock);

        boolean sendDataToBackend = momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        // verifyZeroInteractions(clientMock);
        assertThat(sendDataToBackend).isFalse();
    }

    @Test
    public void ShouldNotCallSendMoments_WhenUserAccessTokenIsEmpty() {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");
        when(uCoreAdapterMock.getAppFrameworkClient(MomentsClient.class, "", momentGsonConverterMock)).thenReturn(clientMock);
        when(uCoreAdapterMock.getClient(MomentsClient.class, TEST_MOMENT_URL, "", momentGsonConverterMock)).thenReturn(clientMock);
        boolean sendDataToBackend = momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));
        assertThat(sendDataToBackend).isFalse();
    }

    @Test
    public void ShouldNotSendToBE_WhenMomentSyncDataIsNotNull() {
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn("1");
        when(momentMock.getSubjectId()).thenReturn("2");
        when(momentMock.getCreatorId()).thenReturn("1");

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock, never()).saveMoment((String) any(), (String) any(), any(UCoreMoment.class));
    }

    @Test
    public void ShouldNotSendUpdateToBE_WhenMomentSyncDataIsNotNull() {
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn("1");
        when(momentMock.getSubjectId()).thenReturn("2");
        when(momentMock.getCreatorId()).thenReturn("1");

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock).updateMoment(anyString(), anyString(), anyString(), (UCoreMoment) any());
    }

    @Test
    public void ShouldSendSaveToBE_WhenMomentSyncDataIsNull() {
        when(momentMock.getSynchronisationData()).thenReturn(null);
        when(momentMock.getSubjectId()).thenReturn("2");
        when(momentMock.getCreatorId()).thenReturn("1");

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock).saveMoment(anyString(), anyString(), (UCoreMoment) any());
    }

    @Test
    public void ShouldNotSendUpdateToBE_WhenMomentSyncDataIsNull() {
        when(momentMock.getSynchronisationData()).thenReturn(null);
        when(momentMock.getSubjectId()).thenReturn("2");
        when(momentMock.getCreatorId()).thenReturn("1");

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock, never()).updateMoment(anyString(), anyString(), anyString(), (UCoreMoment) any());
    }

    @Test
    public void ShouldNotSendToBE_WhenMomentCreatorIdIsNull() {
        when(momentMock.getSubjectId()).thenReturn("2");
        when(momentMock.getCreatorId()).thenReturn(null);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock, never()).saveMoment(anyString(), anyString(), any(UCoreMoment.class));
    }

    @Test
    public void ShouldNotSendToBE_WhenMomentSubjectIdIsEmpty() {
        when(momentMock.getSubjectId()).thenReturn("");
        when(momentMock.getCreatorId()).thenReturn("1");

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock, never()).saveMoment(anyString(), anyString(), any(UCoreMoment.class));
    }

    @Test
    public void ShouldNotSendToBE_WhenMomentCreatorIdIsEmpty() {
        when(momentMock.getSubjectId()).thenReturn("2");
        when(momentMock.getCreatorId()).thenReturn("");

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock, never()).saveMoment(anyString(), anyString(), any(UCoreMoment.class));
    }

    @Test
    public void ShouldSendSaveMoment_WhenMomentSyncDataIsOK() {
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when(clientMock.saveMoment(BABY_ID, USER_ID, uCoreMomentMock)).thenReturn(uCoreMomentResponseMock);
        when(uCoreMomentResponseMock.getGuid()).thenReturn(TEST_MOMENT_ID);
        when(uGrowDataCreatorMock.createSynchronisationData(TEST_MOMENT_ID, false, momentMock.getDateTime(), 1)).thenReturn(synchronisationDataMock);
        when(uCoreMomentResponseMock.getExpirationDate()).thenReturn(TEST_DATE_TIME_STRING);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));
        verify(eventingMock).post(isA(MomentDataSenderCreatedRequest.class));

        ArgumentCaptor<DateTime> argumentCaptor = ArgumentCaptor.forClass(DateTime.class);
        verify(momentMock).setExpirationDate(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().toString()).isEqualTo(TEST_DATE_TIME_STRING);
    }

    @Test
    public void ShouldSendSaveMoment_WhenMomentSyncDataIsNotNullButGuidIsMinusOne() {
        UCoreMomentSaveResponse uCoreMomentSaveResponse = new UCoreMomentSaveResponse();
        uCoreMomentSaveResponse.setMomentId(TEST_MOMENT_ID);
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when(clientMock.saveMoment(BABY_ID, USER_ID, uCoreMomentMock)).thenReturn(uCoreMomentResponseMock);
        //    when(uCoreMomentSaveResponse.getMomentId()).thenReturn(TEST_MOMENT_ID);
        when(uGrowDataCreatorMock.createSynchronisationData(TEST_MOMENT_ID, false, momentMock.getDateTime(), 1)).thenReturn(synchronisationDataMock);
        when(momentMock.getSynchronisationData().isInactive()).thenReturn(true);
        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(isA(MomentBackendDeleteResponse.class));
    }

    @Test
    public void ShouldSetMomentSyncDataE_WhenBEResponseIsNotNull() {
        when(momentMock.getCreatorId()).thenReturn(USER_ID);
        when(momentMock.getSubjectId()).thenReturn(BABY_ID);
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when(clientMock.saveMoment(BABY_ID, USER_ID, uCoreMomentMock)).thenReturn(uCoreMomentResponseMock);
        when(uCoreMomentResponseMock.getGuid()).thenReturn(TEST_MOMENT_ID);
        when(uGrowDataCreatorMock.createSynchronisationData(TEST_MOMENT_ID, false, momentMock.getDateTime(), 1)).thenReturn(synchronisationDataMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(isA(MomentDataSenderCreatedRequest.class));
    }

    @Test
    public void ShouldPostNetworkErrorEvent_WhenRetrofitHttpErrorHappens() {
        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.HTTP);
        when(clientMock.saveMoment((String) any(), (String) any(), (UCoreMoment) any())).
                thenThrow(retrofitErrorMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(errorEventCaptor.capture());
        final BackendResponse errorEvent = errorEventCaptor.getValue();
        assertThat(errorEvent.getCallException()).isEqualTo(retrofitErrorMock);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenRetrofitUnexpectedErrorHappens() {
        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.UNEXPECTED);
        when(clientMock.saveMoment((String) any(), (String) any(), (UCoreMoment) any())).
                thenThrow(retrofitErrorMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(errorEventCaptor.capture());
        final BackendResponse errorEvent = errorEventCaptor.getValue();
        assertThat(errorEvent.getCallException()).isEqualTo(retrofitErrorMock);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenRetrofitConversionErrorHappens() {
        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(clientMock.saveMoment((String) any(), (String) any(), (UCoreMoment) any())).
                thenThrow(retrofitErrorMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(errorEventCaptor.capture());
        final BackendResponse errorEvent = errorEventCaptor.getValue();
        assertThat(errorEvent.getCallException()).isEqualTo(retrofitErrorMock);
    }

    @Test
    public void ShouldCallUpdate_WhenMomentHasSyncDataAndResponseCodeHttpOk() {
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_ID);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when(clientMock.updateMoment(BABY_ID, TEST_MOMENT_ID, USER_ID, uCoreMomentMock)).thenReturn(uCoreMomentResponseMock);
        when(uCoreMomentResponseMock.getExpirationDate()).thenReturn(TEST_DATE_TIME_STRING);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(isA(MomentDataSenderCreatedRequest.class));
        ArgumentCaptor<DateTime> argumentCaptor = ArgumentCaptor.forClass(DateTime.class);
        verify(momentMock).setExpirationDate(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().toString()).isEqualTo(TEST_DATE_TIME_STRING);
    }

    @Test
    public void ShouldCallUpdate_WhenMomentHasSyncData_throws_exception() {
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_ID);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);

        String string = "not able to connect";
        ArrayList<Header> headers = new ArrayList<>();
        headers.add(new Header("test", "test"));

        when((typedByteArrayMock).getBytes()).thenReturn(string.getBytes());
        Response response = new Response("http://localhost", 403, string, headers, typedByteArrayMock);
        when(retrofitErrorMock.getResponse()).thenReturn(response);

        when(clientMock.updateMoment(BABY_ID, TEST_MOMENT_ID, USER_ID, uCoreMomentMock)).thenThrow(retrofitErrorMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));
        verify(eventingMock).post(isA(BackendResponse.class));
    }

    @Test
    public void ShouldCallUpdate_WhenMomentHasSyncData_throws_exception_Error_Conflict() {
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_ID);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when((typedByteArrayMock).getBytes()).thenReturn("not able to connect".getBytes());
        Response response = new Response("", 409, "CONFLICT", new ArrayList<Header>(), null);
        when(retrofitErrorMock.getResponse()).thenReturn(response);
        when(clientMock.updateMoment(BABY_ID, TEST_MOMENT_ID, USER_ID, uCoreMomentMock)).thenThrow(retrofitErrorMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));
        
        verifyNoMoreInteractions(eventingMock);
    }

    @Test
    public void ShouldCallUpdate_WhenMomentHasSyncDataAndResponseCodeHttpAccepted() {
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_ID);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when(clientMock.updateMoment(BABY_ID, TEST_MOMENT_ID, USER_ID, uCoreMomentMock)).thenReturn(uCoreMomentResponseMock);
        when(uCoreMomentResponseMock.getExpirationDate()).thenReturn(TEST_DATE_TIME_STRING);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(isA(MomentDataSenderCreatedRequest.class));
        ArgumentCaptor<DateTime> argumentCaptor = ArgumentCaptor.forClass(DateTime.class);
        verify(momentMock).setExpirationDate(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().toString()).isEqualTo(TEST_DATE_TIME_STRING);
    }

    @Test
    public void ShouldPostExceptionEventDuringUpdate_WhenRetrofitErrorHappens() {
        Response response = new Response("", 409, "CONFLICT", new ArrayList<Header>(), null);

        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_ID);
        when(retrofitErrorMock.getResponse()).thenReturn(response);

        when(clientMock.updateMoment((String) any(), (String) any(), (String) any(), (UCoreMoment) any())).
                thenThrow(retrofitErrorMock);

        boolean conflict = momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        assertThat(conflict).isTrue();
    }

    @Test
    public void ShouldCallDeleteMoment_WhenMomentSynchronizationDataHasInactiveSetToTrue() {
        Response response = new Response("", 204, "NO_CONTENT", new ArrayList<Header>(), null);

        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.isInactive()).thenReturn(true);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_ID);
        when(clientMock.deleteMoment(BABY_ID, TEST_MOMENT_ID, USER_ID)).thenReturn(response);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(any(MomentBackendDeleteResponse.class));
    }

    @Test
    public void ShouldPostExceptionEventDuringDelete_WhenRetrofitErrorHappens() {
        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.isInactive()).thenReturn(true);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_ID);

        when(clientMock.deleteMoment((String) any(), (String) any(), (String) any())).
                thenThrow(retrofitErrorMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(errorEventCaptor.capture());
        final BackendResponse errorEvent = errorEventCaptor.getValue();
        assertThat(errorEvent.getCallException()).isEqualTo(retrofitErrorMock);
    }

    @Test
    public void testgetClassForSyncData() {
        Class<? extends Moment> classType = momentsDataSender.getClassForSyncData();
        assertThat(classType).isNotNull();
    }

    @Test
    public void test_givenMockedMomentsToUpdate_whenRetrofitReturnsNullOnUpdate_thenShouldReturnTrue() {
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_ID);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when(clientMock.updateMoment(anyString(), anyString(), anyString(), (UCoreMoment) any())).thenReturn(null);

        boolean outcome = momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        assertThat(outcome).isTrue();
    }

    @Test
    public void test_givenRealMomentsToUpdate_whenRetrofitReturnsNullOnUpdate_thenShouldReturnTrue() {
        when(clientMock.updateMoment(anyString(), anyString(), anyString(), (UCoreMoment) any())).thenReturn(null);

        Moment moment = new TestMoment(CREATOR_ID, SUBJECT_ID, new TestMomentType(MOMENT_TYPE_ID, MOMENT_TYPE), new DateTime());
        TestSynchronisationData syncData = new TestSynchronisationData(GUID, false, new DateTime(), 1);
        moment.setSynchronisationData(syncData);
        boolean outcome = momentsDataSender.sendDataToBackend(Collections.singletonList(moment));

        assertThat(outcome).isTrue();
    }
}