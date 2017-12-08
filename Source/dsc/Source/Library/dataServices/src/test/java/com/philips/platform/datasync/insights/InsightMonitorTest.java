package com.philips.platform.datasync.insights;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.events.DeleteInsightRequest;
import com.philips.platform.core.events.FetchInsightRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import retrofit.converter.GsonConverter;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class InsightMonitorTest {

    private InsightMonitor mInsightMonitor;
    @Mock
    private InsightDataSender mInsightDataSender;
    @Mock
    private GsonConverter mGsonConverter;
    @Mock
    private UCoreAdapter mUCoreAdapter;
    @Mock
    private InsightDataFetcher mInsightDataFetcher;
    @Mock
    private InsightConverter mInsightConverter;
    @Mock
    private Insight mInsight;
    @Mock
    private DeleteInsightRequest mInsightBackendDeleteRequest;
    @Mock
    private FetchInsightRequest mInsightBackendGetRequest;
    @Mock
    private AppComponent mAppComponent;
    @Mock
    private UCoreAccessProvider mUCoreAccessProvider;

    @Captor
    private ArgumentCaptor<List<? extends Insight>> captor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(mAppComponent);
        mInsightMonitor = new InsightMonitor(mInsightDataSender, mInsightDataFetcher);
        mInsightMonitor.uCoreAccessProvider = mUCoreAccessProvider;
    }

    @Test
    public void ShouldCallDeleteInsight() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mInsightMonitor.onEventAsync(mInsightBackendDeleteRequest);
    }

    @Test
    public void ShouldCallFetchInsight() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mInsightMonitor.onEventAsync(mInsightBackendGetRequest);
    }
}