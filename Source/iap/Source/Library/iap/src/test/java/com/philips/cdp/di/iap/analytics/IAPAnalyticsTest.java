package com.philips.cdp.di.iap.analytics;

import android.app.Activity;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.BuildConfig;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class IAPAnalyticsTest {

    public AppTaggingInterface sAppTaggingInterface;

    private IAPDependencies iapDependenciesMock;

    @Before
    public void setUp() throws Exception {
        iapDependenciesMock = new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class));

        when(iapDependenciesMock.getAppInfra().getTagging()).thenReturn(mock(AppTaggingInterface.class));

        sAppTaggingInterface =
                iapDependenciesMock.getAppInfra().getTagging().
                        createInstanceForComponent(IAPAnalyticsConstant.COMPONENT_NAME, BuildConfig.VERSION_NAME);
    }

    @Test
    public void ShouldCalled_initAnalytics() throws Exception {
        IAPAnalytics.initIAPAnalytics(iapDependenciesMock);
        Assert.assertNotNull(iapDependenciesMock);
    }

    @Test
    public void ShouldCalled_TrackPage() throws Exception {
        IAPAnalytics.trackPage("first page");
    }

    @Test
    public void ShouldCalled_TrackAction() throws Exception {
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.PAYMENT_METHOD, "");
    }

    @Test
    public void ShouldCalled_TrackMultipleActions() throws Exception {
        IAPAnalytics.trackMultipleActions(IAPAnalyticsConstant.SEND_DATA, new HashMap<String, String>());
    }

    @Test
    public void ShouldCalled_CollectLifecycleData() throws Exception {
        IAPAnalytics.collectLifecycleData(mock(Activity.class));
    }

    @Test
    public void ShouldCalled_PauseCollectingLifecycleData() throws Exception {
        IAPAnalytics.pauseCollectingLifecycleData();
    }


    @Test
    public void ShouldCalled_ClearAppTaggingInterface() throws Exception {
        IAPAnalytics.clearAppTaggingInterface();
    }
}