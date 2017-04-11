package com.philips.platform.datasync.moments;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.philips.testing.verticals.ErrorHandlerImplTest;
import com.philips.testing.verticals.OrmCreatorTest;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by indrajitkumar on 12/12/16.
 */
public class MomentsDataSenderTest {

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String USER_ID = "TEST_GUID";
    public static final String BABY_ID = "BABY_ID";
    public static final DateTime DATE_TIME = DateTime.now();
    private final String TEST_MOMENT_UD = "TEST_MOMENT_ID";

    private MomentsDataSender momentsDataSender;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private MomentsConverter momentsConverterMock;

    @Mock
    private Eventing eventingMock;

    @Mock
    private MomentsClient clientMock;

    @Captor
    private ArgumentCaptor<BackendResponse> errorEventCaptor;

    @Mock
    private BackendResponse networkCommunicationErrorEventMock;

    @Mock
    private Moment momentMock;

    @Mock
    private UserRegistrationInterface userRegistrationInterfaceMock;

    @Mock
    private SynchronisationData synchronisationDataMock;

    @Mock
    private MomentGsonConverter momentGsonConverterMock;

    @Mock
    private UCoreMomentSaveResponse uCoreMomentSaveResponseMock;

    @Mock
    private UCoreMoment uCoreMomentMock;

    @Mock
    private BaseAppDataCreator uGrowDataCreatorMock;

    @Mock
    private RetrofitError retrofitErrorMock;
    ErrorHandlerImplTest errorHandler;
    //Context context;
    DataServicesManager dataServicesManager;
    private OrmCreatorTest verticalDataCreater;

    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() {
        initMocks(this);
        dataServicesManager = DataServicesManager.getInstance();
        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        errorHandler = new ErrorHandlerImplTest();
        DataServicesManager.getInstance().setAppComponant(appComponantMock);

        when(accessProviderMock.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(USER_ID);
        when(accessProviderMock.getSubjectId()).thenReturn(BABY_ID);
        when(uCoreAdapterMock.getAppFrameworkClient(MomentsClient.class, ACCESS_TOKEN, momentGsonConverterMock)).thenReturn(clientMock);

        momentsDataSender = new MomentsDataSender(momentsConverterMock, momentGsonConverterMock);
        momentsDataSender.accessProvider = accessProviderMock;
        momentsDataSender.uCoreAdapter = uCoreAdapterMock;
        momentsDataSender.baseAppDataCreater = verticalDataCreater;
        momentsDataSender.eventing = eventingMock;
        momentsDataSender.userRegistrationImpl = userRegistrationInterfaceMock;

        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(momentMock.getCreatorId()).thenReturn(USER_ID);
        when(momentMock.getSubjectId()).thenReturn(BABY_ID);
    }

    @Test
    public void ShouldNotCallSendMoments_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        boolean sendDataToBackend = momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verifyZeroInteractions(clientMock);
        assertThat(sendDataToBackend).isFalse();
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotCallSendMoments_WhenUserAccessTokenIsNull() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);

        boolean sendDataToBackend = momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verifyZeroInteractions(clientMock);
        assertThat(sendDataToBackend).isFalse();
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotCallSendMoments_WhenUserAccessTokenIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");

        boolean sendDataToBackend = momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verifyZeroInteractions(clientMock);
        assertThat(sendDataToBackend).isFalse();
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotSendToBE_WhenMomentSyncDataIsNotNull() {
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn("1");
        when(momentMock.getSubjectId()).thenReturn("2");
        when(momentMock.getCreatorId()).thenReturn("1");

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock, never()).saveMoment(anyString(), anyString(), any(UCoreMoment.class));
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotSendToBE_WhenMomentSubjectIdIsNull() {
        when(momentMock.getSubjectId()).thenReturn(null);
        when(momentMock.getCreatorId()).thenReturn("1");

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock, never()).saveMoment(anyString(), anyString(), any(UCoreMoment.class));
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotSendToBE_WhenMomentCreatorIdIsNull() {
        when(momentMock.getSubjectId()).thenReturn("2");
        when(momentMock.getCreatorId()).thenReturn(null);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock, never()).saveMoment(anyString(), anyString(), any(UCoreMoment.class));
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotSendToBE_WhenMomentSubjectIdIsEmpty() {
        when(momentMock.getSubjectId()).thenReturn("");
        when(momentMock.getCreatorId()).thenReturn("1");

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock, never()).saveMoment(anyString(), anyString(), any(UCoreMoment.class));
    }

    @Test(expected = NullPointerException.class)
    public void ShouldNotSendToBE_WhenMomentCreatorIdIsEmpty() {
        when(momentMock.getSubjectId()).thenReturn("2");
        when(momentMock.getCreatorId()).thenReturn("");

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(clientMock, never()).saveMoment(anyString(), anyString(), any(UCoreMoment.class));
    }

    @Test(expected = NullPointerException.class)
    public void ShouldSendSaveMoment_WhenMomentSyncDataIsNull() {
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when(clientMock.saveMoment(BABY_ID, USER_ID, uCoreMomentMock)).thenReturn(uCoreMomentSaveResponseMock);
        when(uCoreMomentSaveResponseMock.getMomentId()).thenReturn(TEST_MOMENT_UD);
        when(uGrowDataCreatorMock.createSynchronisationData(TEST_MOMENT_UD, false, momentMock.getDateTime(), 1)).thenReturn(synchronisationDataMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(isA(MomentBackendDeleteResponse.class));
    }

    @Test(expected = NullPointerException.class)
    public void ShouldSendSaveMoment_WhenMomentSyncDataIsNotNullButGuidIsMinusOne() {
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when(clientMock.saveMoment(BABY_ID, USER_ID, uCoreMomentMock)).thenReturn(uCoreMomentSaveResponseMock);
        when(uCoreMomentSaveResponseMock.getMomentId()).thenReturn(TEST_MOMENT_UD);
        when(uGrowDataCreatorMock.createSynchronisationData(TEST_MOMENT_UD, false, momentMock.getDateTime(), 1)).thenReturn(synchronisationDataMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(isA(MomentBackendDeleteResponse.class));
    }

    @Test(expected = NullPointerException.class)
    public void ShouldSetMomentSyncDataE_WhenBEResponseIsNotNull() {
        when(momentMock.getCreatorId()).thenReturn(USER_ID);
        when(momentMock.getSubjectId()).thenReturn(BABY_ID);
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when(clientMock.saveMoment(BABY_ID, USER_ID, uCoreMomentMock)).thenReturn(uCoreMomentSaveResponseMock);
        when(uCoreMomentSaveResponseMock.getMomentId()).thenReturn(TEST_MOMENT_UD);
        when(uGrowDataCreatorMock.createSynchronisationData(TEST_MOMENT_UD, false, momentMock.getDateTime(), 1)).thenReturn(synchronisationDataMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(momentMock).setSynchronisationData(synchronisationDataMock);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldPostNetworkErrorEvent_WhenRetrofitHttpErrorHappens() throws Exception {
        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.HTTP);
        when(clientMock.saveMoment(anyString(), anyString(), any(UCoreMoment.class))).
                thenThrow(retrofitErrorMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(errorEventCaptor.capture());
        final BackendResponse errorEvent = errorEventCaptor.getValue();
        assertThat(errorEvent.getCallException()).isEqualTo(retrofitErrorMock);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldPostExceptionEvent_WhenRetrofitUnexpectedErrorHappens() throws Exception {
        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.UNEXPECTED);
        when(clientMock.saveMoment(anyString(), anyString(), any(UCoreMoment.class))).
                thenThrow(retrofitErrorMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(errorEventCaptor.capture());
        final BackendResponse errorEvent = errorEventCaptor.getValue();
        assertThat(errorEvent.getCallException()).isEqualTo(retrofitErrorMock);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldPostExceptionEvent_WhenRetrofitConversionErrorHappens() throws Exception {
        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(clientMock.saveMoment(anyString(), anyString(), any(UCoreMoment.class))).
                thenThrow(retrofitErrorMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(errorEventCaptor.capture());
        final BackendResponse errorEvent = errorEventCaptor.getValue();
        assertThat(errorEvent.getCallException()).isEqualTo(retrofitErrorMock);
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCallUpdate_WhenMomentHasSyncDataAndResponseCodeHttpOk() {
        Response response = new Response("", 200, "OK", new ArrayList<Header>(), null);

        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_UD);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when(clientMock.updateMoment(BABY_ID, TEST_MOMENT_UD, USER_ID, uCoreMomentMock)).thenReturn(response);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(isA(MomentBackendDeleteResponse.class));
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCallUpdate_WhenMomentHasSyncDataAndResponseCodeHttpAccepted() {
        Response response = new Response("", 201, "CREATED", new ArrayList<Header>(), null);

        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_UD);
        when(momentsConverterMock.convertToUCoreMoment(momentMock)).thenReturn(uCoreMomentMock);
        when(clientMock.updateMoment(BABY_ID, TEST_MOMENT_UD, USER_ID, uCoreMomentMock)).thenReturn(response);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(isA(MomentBackendDeleteResponse.class));
    }

    @Test(expected = NullPointerException.class)
    public void ShouldPostExceptionEventDuringUpdate_WhenRetrofitErrorHappens() throws Exception {
        Response response = new Response("", 409, "CONFLICT", new ArrayList<Header>(), null);


        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_UD);
        when(retrofitErrorMock.getResponse()).thenReturn(response);

        when(clientMock.updateMoment(anyString(), anyString(), anyString(), any(UCoreMoment.class))).
                thenThrow(retrofitErrorMock);

        boolean conflict = momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(errorEventCaptor.capture());
        final BackendResponse errorEvent = errorEventCaptor.getValue();
        assertThat(errorEvent.getCallException()).isEqualTo(retrofitErrorMock);
        assertThat(conflict).isTrue();
    }

    @Test(expected = NullPointerException.class)
    public void ShouldCallDeleteMoment_WhenMomentSynchronizationDataHasInactiveSetToTrue() {
        Response response = new Response("", 204, "NO_CONTENT", new ArrayList<Header>(), null);

        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.isInactive()).thenReturn(true);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_UD);
        when(clientMock.deleteMoment(BABY_ID, TEST_MOMENT_UD, USER_ID)).thenReturn(response);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(any(MomentBackendDeleteResponse.class));
    }

    @Test(expected = NullPointerException.class)
    public void ShouldPostExceptionEventDuringDelete_WhenRetrofitErrorHappens() throws Exception {
        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(momentMock.getDateTime()).thenReturn(DATE_TIME);
        when(momentMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(synchronisationDataMock.isInactive()).thenReturn(true);
        when(synchronisationDataMock.getGuid()).thenReturn(TEST_MOMENT_UD);

        when(clientMock.deleteMoment(anyString(), anyString(), anyString())).
                thenThrow(retrofitErrorMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(errorEventCaptor.capture());
        final BackendResponse errorEvent = errorEventCaptor.getValue();
        assertThat(errorEvent.getCallException()).isEqualTo(retrofitErrorMock);
    }

    /*@Test
    public void ShouldPostNetworkErrorEvent_WhenRetrofitHttpErrorHappens() throws Exception {

        String string = "not able to connect";
        ArrayList<Header> headers = new ArrayList<>();
        headers.add(new Header("test", "test"));

        when((typedByteArrayMock).getBytes()).thenReturn(string.getBytes());
        Response response = new Response("http://localhost", 403, string, headers, typedByteArrayMock);
        when(retrofitErrorMock.getResponse()).thenReturn(response);

        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.HTTP);
        when(clientMock.saveMoment(anyString(), anyString(), any(UCoreMoment.class))).
                thenThrow(retrofitErrorMock);

        momentsDataSender.sendDataToBackend(Collections.singletonList(momentMock));

        verify(eventingMock).post(errorEventCaptor.capture());
        final BackendResponse errorEvent = errorEventCaptor.getValue();
        assertThat(errorEvent.getCallException()).isEqualTo(retrofitErrorMock);
        verify(analyticsErrorHandlingMock).TrackError(string);
    }*/
}