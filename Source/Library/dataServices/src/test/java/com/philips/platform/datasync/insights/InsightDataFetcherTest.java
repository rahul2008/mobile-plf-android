package com.philips.platform.datasync.insights;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.BaseAppData;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.events.ListEvent;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import static org.mockito.MockitoAnnotations.initMocks;

public class InsightDataFetcherTest {

    private InsightDataFetcher mInsightDataFetcher;

    private UCoreInsightList uCoreInsightList = new UCoreInsightList();
    private List<UCoreInsight> uCoreInsights = new ArrayList<>();
    private List<Insight> appInsightList = new ArrayList<>();

    @Mock
    private Eventing eventingMock;

    @Mock
    private RetrofitError retrofitErrorMock;

    @Mock
    private InsightConverter mInsightConverter;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private UCoreAdapter coreAdapterMock;

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

    @Mock
    AppComponent mAppComponent;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }
}