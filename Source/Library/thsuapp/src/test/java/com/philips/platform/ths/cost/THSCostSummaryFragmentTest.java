/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.cost;

import android.view.View;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSCostSummaryFragmentTest {

    THSCostSummaryFragment mThsBaseFragment;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    THSCostSummaryPresenter thsCostSummaryPresenterMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);

        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);


        mThsBaseFragment = new THSCostSummaryFragmentTestMock();
        mThsBaseFragment.setActionBarListener(actionBarListenerMock);
        SupportFragmentTestUtil.startFragment(mThsBaseFragment);
    }

    @Test
    public void onClick() throws Exception {
        mThsBaseFragment.mPresenter = thsCostSummaryPresenterMock;
        final View viewById = mThsBaseFragment.getView().findViewById(R.id.ths_cost_summary_promotion_code_apply_button);
        viewById.performClick();
        verify(thsCostSummaryPresenterMock).onEvent(R.id.ths_cost_summary_promotion_code_apply_button);
    }

    @Test
    public void onDestroy(){
        mThsBaseFragment.onDestroy();
        assert mThsBaseFragment.thsVisit == null;
    }

}