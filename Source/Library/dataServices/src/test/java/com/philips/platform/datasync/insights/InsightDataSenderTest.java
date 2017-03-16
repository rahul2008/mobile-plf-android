package com.philips.platform.datasync.insights;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.BaseAppData;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.events.InsightBackendDeleteRequest;
import com.philips.platform.core.events.InsightBackendGetRequest;
import com.philips.platform.core.events.ListEvent;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
    Insight mInsight;


    @Captor
    private ArgumentCaptor<InsightBackendDeleteRequest> insightBackendDeleteRequestArgumentCaptor;

    @Mock
    private AppComponent appComponantMock;

    @Inject
    public InsightDataSenderTest(@NonNull UCoreAdapter uCoreAdapter, @NonNull GsonConverter gsonConverter, @NonNull InsightConverter insightConverter) {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        insightDataSender=new InsightDataSender(uCoreAdapter,gsonConverter,insightConverter);
        this.uCoreAdapterMock = uCoreAdapter;
        this.gsonConverterMock = gsonConverter;
        this.insightConverterMock = insightConverter;
    }

    @Test
    public void shouldNotFetchDataSince_WhenDataSenderIsBusy() throws Exception {

        insightDataSender.synchronizationState.set(DataSender.State.BUSY.getCode());
        insightDataSender.sendDataToBackend(anyList());
        verify(eventingMock, never()).post(insightBackendDeleteRequestArgumentCaptor.capture());
    }
}