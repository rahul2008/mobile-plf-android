package com.philips.platform.appframework.connectivitypowersleep.insights;

import android.content.Context;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.trackers.DataServicesManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InsightsPresenterTest {

    private InsightsPresenter insightsPresenter;

    @Captor
    private ArgumentCaptor<DBFetchRequestListner> captor;

    @Mock
    DataServicesManager dataServicesManager;

    DBFetchRequestListner dbFetchRequestListner;

    @Mock
    InsightsContract.View view;

    @Mock
    Context context;
    
    @Before
    public void setUp() {
        insightsPresenter = new InsightsPresenter(view, context);
    }

    @Test
    public void showArticle() {

    }

    @Test
    public void loadInsightsTest() {
        insightsPresenter.loadInsights(dataServicesManager);
        verify(view).showProgressDialog();
        verify(dataServicesManager).fetchInsights(insightsPresenter);
    }

    @Test
    public void onFetchSuccess() {
        insightsPresenter.loadInsights(dataServicesManager);
        verify(dataServicesManager).fetchInsights(captor.capture());
        dbFetchRequestListner = captor.getValue();
        List<Insight> insightList = new ArrayList<Insight>();
        dbFetchRequestListner.onFetchSuccess(insightList);
        verify(view, times(1)).hideProgressDialog();
        verify(view, times(1)).onInsightLoadSuccess(insightList);
    }

    @Test
    public void onFetchFailure() {
        insightsPresenter.loadInsights(dataServicesManager);
        verify(dataServicesManager).fetchInsights(captor.capture());
        dbFetchRequestListner = captor.getValue();
        dbFetchRequestListner.onFetchFailure(new Exception("error"));
        verify(view, times(1)).hideProgressDialog();
        verify(view, times(1)).onInsightLoadError("error");
    }

    @After
    public void tearDown() {
        insightsPresenter = null;
        captor = null;
        dataServicesManager = null;
        dbFetchRequestListner = null;
        view = null;
        context = null;
    }

}