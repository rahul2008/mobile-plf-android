package com.philips.platform.datasync.insights;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.BaseAppData;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.FetchInsightRequest;
import com.philips.platform.core.events.ListEvent;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class InsightDataFetcherTest {

    private String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";
    private String TEST_USER_ID = "TEST_USER_ID";


    private InsightDataFetcher mInsightDataFetcher;

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
    UCoreInsightList uCoreInsightListMock;

    @Mock
    Insight mInsight;

    InsightDataFetcher insightDataFetcher;

    @Mock
    DataFetcher dataFetcher;

    @Captor
    private ArgumentCaptor<FetchInsightRequest> insightBackendGetRequestArgumentCaptor;

    @Mock
    private AppComponent appComponantMock;
    private Response response;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        insightDataFetcher = new InsightDataFetcher(uCoreAdapterMock, gsonConverterMock, insightConverterMock);
        insightDataFetcher.uCoreAccessProvider = accessProviderMock;
        insightDataFetcher.eventing = eventingMock;
    }

    @Test
    public void shouldNotFetchDataSince_WhenDataSenderIsBusy() throws Exception {

        insightDataFetcher.synchronizationState.set(DataSender.State.BUSY.getCode());
        insightDataFetcher.fetchData();
        verify(eventingMock, never()).post(insightBackendGetRequestArgumentCaptor.capture());
    }

    @Test
    public void shouldFetchData_WhenDataSenderIsNotBusy() throws Exception {

        insightDataFetcher.synchronizationState.set(DataSender.State.IDLE.getCode());
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        when(accessProviderMock.getInsightLastSyncTimestamp()).thenReturn("2017-03-21T10:19:51.706Z");
        when(uCoreAdapterMock.getAppFrameworkClient(InsightClient.class, TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(mInsightClient);
        when(mInsightClient.fetchInsights(TEST_USER_ID, TEST_USER_ID, UCoreAdapter.API_VERSION, "2017-03-21T10:19:51.706Z")).thenReturn(uCoreInsightListMock);
        RetrofitError retrofitError = insightDataFetcher.fetchData();
        assertThat(retrofitError).isNull();
        insightDataFetcher.fetchData();
    }

    @Test
    public void shouldThrowError_WhenRetrofitErrorComes() throws Exception {

        final RetrofitError retrofitErrorMock = mock(RetrofitError.class);
        response = new Response("", 401, "Unauthorised", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitErrorMock.getResponse()).thenReturn(response);

        insightDataFetcher.synchronizationState.set(DataSender.State.IDLE.getCode());
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        when(accessProviderMock.getInsightLastSyncTimestamp()).thenReturn("2017-03-21T10:19:51.706Z");
        when(uCoreAdapterMock.getAppFrameworkClient(InsightClient.class, TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(mInsightClient);
        when(mInsightClient.fetchInsights(TEST_USER_ID, TEST_USER_ID, UCoreAdapter.API_VERSION, "2017-03-21T10:19:51.706Z")).thenThrow(retrofitErrorMock);
        insightDataFetcher.fetchData();
        verify(eventingMock).post(new BackendDataRequestFailed(any(RetrofitError.class)));
    }

    @Test
    public void shouldPostError_WhenUserisInvalid() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        insightDataFetcher.fetchData();
    }

    @Test
    public void returnFalseWhenIsUserInvalidIsCalled_IfAccessProviderIsNull() throws Exception {
        insightDataFetcher.uCoreAccessProvider = null;
        assertThat(insightDataFetcher.isUserInvalid()).isFalse();
    }

    @Test
    public void callSuperFetchAllData_whenFetchAllDataIsCalled() throws Exception {
        insightDataFetcher.fetchAllData();
    }
}