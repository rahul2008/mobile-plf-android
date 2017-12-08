package com.philips.platform.datasync.insights;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.BaseAppData;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.DeleteInsightRequest;
import com.philips.platform.core.events.ListEvent;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;
import com.philips.platform.verticals.VerticalCreater;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedString;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class InsightDataSenderTest {

    private String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";
    private String TEST_USER_ID = "TEST_USER_ID";


    private InsightDataSender insightDataSender;

    private UCoreInsightList uCoreInsightList = new UCoreInsightList();
    private List<UCoreInsight> uCoreInsights = new ArrayList<>();
    private List<Insight> appInsightList = new ArrayList<>();

    @Mock
    private Eventing eventingMock;

    @Mock
    private RetrofitError retrofitErrorMock;

    @Mock
    private InsightConverter insightConverterMock;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private InsightClient mInsightClient;

    @Mock
    private GsonConverter gsonConverterMock;

    @Captor
    private ArgumentCaptor<ListEvent<? extends BaseAppData>> saveRequestCaptor;

    @Mock
    UCoreInsightList mUCoreInsightList;

    @Mock
    Insight mInsightMock;

    @Mock
    VerticalCreater dataCreatorMock;


    @Captor
    private ArgumentCaptor<DeleteInsightRequest> insightBackendDeleteRequestArgumentCaptor;

    @Mock
    private AppComponent appComponantMock;

    @Mock
    private SynchronisationManager synchronisationManagerMock;

    @Mock
    SynchronisationData synchronisationDataMock;

    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        insightDataSender=new InsightDataSender(uCoreAdapterMock,gsonConverterMock,insightConverterMock);
        insightDataSender.eventing = eventingMock;
        insightDataSender.uCoreAccessProvider = accessProviderMock;
        insightDataSender.synchronisationManager=synchronisationManagerMock;
    }

    @Test
    public void shouldNotSendData_WhenDataSenderIsBusy() throws Exception {

        insightDataSender.synchronizationState.set(DataSender.State.BUSY.getCode());
        insightDataSender.sendDataToBackend(null);
        verify(eventingMock, never()).post(insightBackendDeleteRequestArgumentCaptor.capture());
    }

    @Test
    public void shouldNotSendDataToBackEnd_WhenUserIsNotLoggedIN() throws Exception {

        insightDataSender.synchronizationState.set(DataSender.State.IDLE.getCode());
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        insightDataSender.sendDataToBackend(null);
        verify(eventingMock, never()).post(insightBackendDeleteRequestArgumentCaptor.capture());

    }

    @Test
    public void shouldSendDataToBackEnd_WhenUserIsLoggedInAndDataIsNotEmpty() throws Exception {

        insightDataSender.synchronizationState.set(DataSender.State.IDLE.getCode());
        when(accessProviderMock.isLoggedIn()).thenReturn(true);

        List<Insight> insights=new ArrayList<>();

        insights.add(mInsightMock);

        when(mInsightMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(mInsightMock.getSynchronisationData().getGuid()).thenReturn("aefe5623-a7ac-4b4a-b789-bdeaf23add9f");
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        when(accessProviderMock.getInsightLastSyncTimestamp()).thenReturn("2017-03-21T10:19:51.706Z");
        when(uCoreAdapterMock.getAppFrameworkClient(InsightClient.class, TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(mInsightClient);

        Response response = new Response("", 201, "OK", new ArrayList<Header>(), null);
        when(mInsightClient.deleteInsight(TEST_USER_ID,"aefe5623-a7ac-4b4a-b789-bdeaf23add9f",TEST_USER_ID)).thenReturn(response);

        insightDataSender.sendDataToBackend(insights);
    }

    /* final RetrofitError retrofitErrorMock = mock(RetrofitError.class);
        response = new Response("", 401, "Unauthorised", new ArrayList<Header>(), new TypedString("ERROR"));*/

    @Test
    public void shouldThrowError_WhenDeleteInsightfails() throws Exception {

        insightDataSender.synchronizationState.set(DataSender.State.IDLE.getCode());
        when(accessProviderMock.isLoggedIn()).thenReturn(true);

        List<Insight> insights=new ArrayList<>();

        insights.add(mInsightMock);

        when(mInsightMock.getSynchronisationData()).thenReturn(synchronisationDataMock);
        when(mInsightMock.getSynchronisationData().getGuid()).thenReturn("aefe5623-a7ac-4b4a-b789-bdeaf23add9f");
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        when(accessProviderMock.getInsightLastSyncTimestamp()).thenReturn("2017-03-21T10:19:51.706Z");
        when(uCoreAdapterMock.getAppFrameworkClient(InsightClient.class, TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(mInsightClient);

        final RetrofitError retrofitErrorMock = mock(RetrofitError.class);
        Response response = new Response("", 401, "Unauthorised", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitErrorMock.getResponse()).thenReturn(response);
        when(mInsightClient.deleteInsight(TEST_USER_ID,"aefe5623-a7ac-4b4a-b789-bdeaf23add9f",TEST_USER_ID)).thenThrow(retrofitErrorMock);

        insightDataSender.sendDataToBackend(insights);

        verify(eventingMock).post(isA(BackendResponse.class));
    }

    @Test
    public void shouldReturnInsightClass_WhenGetClassForSyncDataIsCalled() throws Exception {
        insightDataSender.getClassForSyncData();
    }
}