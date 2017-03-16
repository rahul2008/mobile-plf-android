package com.philips.platform.datasync.insights;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.BaseAppData;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.events.InsightBackendGetRequest;
import com.philips.platform.core.events.ListEvent;
import com.philips.platform.core.events.SettingsBackendGetRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.settings.SettingsClient;
import com.philips.platform.datasync.settings.SettingsDataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
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
    UCoreInsightList mUCoreInsightList;

    @Mock
    Insight mInsight;

    InsightDataFetcher insightDataFetcher;

    @Captor
    private ArgumentCaptor<InsightBackendGetRequest> insightBackendGetRequestArgumentCaptor;

    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        insightDataFetcher = new InsightDataFetcher(uCoreAdapterMock, gsonConverterMock, insightConverterMock);
        insightDataFetcher.uCoreAccessProvider = accessProviderMock;
        insightDataFetcher.eventing = eventingMock;
    }

    @Test
    public void shouldNotFetchDataSince_WhenDataSenderIsBusy() throws Exception {

        insightDataFetcher.synchronizationState.set(DataSender.State.BUSY.getCode());
        insightDataFetcher.fetchDataSince(new DateTime());
        verify(eventingMock, never()).post(insightBackendGetRequestArgumentCaptor.capture());
    }

    @Test
    public void shouldFetchData_WhenDataSenderIsNotBusy() throws Exception {

        insightDataFetcher.synchronizationState.set(DataSender.State.IDLE.getCode());
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        when(uCoreAdapterMock.getAppFrameworkClient(InsightClient.class, TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(mInsightClient);
        RetrofitError retrofitError = insightDataFetcher.fetchDataSince(null);
        assertThat(retrofitError).isNull();
        insightDataFetcher.fetchDataSince(new DateTime());
    }

    @Test
    public void shouldPostError_WhenUserisInvalid() throws Exception{
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        insightDataFetcher.fetchDataSince(new DateTime());
    }

    @Test
    public void returnFalseWhenIsUserInvalidIsCalled_IfAccessProviderIsNull() throws Exception {
        insightDataFetcher.uCoreAccessProvider=null;
        assertThat(insightDataFetcher.isUserInvalid()).isFalse();
    }
}