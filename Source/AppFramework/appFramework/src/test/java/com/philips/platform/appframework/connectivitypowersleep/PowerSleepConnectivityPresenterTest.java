/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.platform.TestActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPort;
import com.philips.platform.appframework.connectivitypowersleep.insights.InsightsFragmentState;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.UIView;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmMomentType;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 8/30/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DataServicesManager.class)
public class PowerSleepConnectivityPresenterTest {

    @Mock
    private ConnectivityPowerSleepContract.View view;

    private Context context;

    @Mock
    private UIView uiView;

    PowerSleepConnectivityPresenter powerSleepConnectivityPresenter;

    DICommPortListener<SessionDataPort> portListener;

    @Mock
    BleReferenceAppliance bleReferenceAppliance;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    DataServicesManager dataServicesManager;

//    @Rule
//    public PowerMockRule rule = new PowerMockRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    SynchronizeSessionsUsecase synchronizeSessionsUsecase;

    @Captor
    private ArgumentCaptor<DBFetchRequestListner<Moment>> dbFetchRequestListnerArgumentCaptor;


    @Before
    public void setUp() throws Exception {
        context = mock(TestActivity.class);
        dataServicesManager = Whitebox.invokeConstructor(DataServicesManager.class);
        powerSleepConnectivityPresenter = new PowerSleepConenctivityPresenterMock(context, view, uiView);
    }

    @Test
    public void synchronizeSessionDataTest() throws Exception {
        powerSleepConnectivityPresenter.synchroniseSessionData(bleReferenceAppliance);
        verify(dataServicesManager).fetchLatestMomentByType(any(String.class),dbFetchRequestListnerArgumentCaptor.capture());
        DBFetchRequestListner<Moment> momentDBFetchRequestListner=dbFetchRequestListnerArgumentCaptor.getValue();
        ArrayList<Moment> moments=new ArrayList<>();
        moments.add(new OrmMoment("","", new OrmMomentType(0,""),new DateTime(System.currentTimeMillis())));
        momentDBFetchRequestListner.onFetchSuccess(moments);
        verify(synchronizeSessionsUsecase).execute(any(BleReferenceAppliance.class),any(SynchronizeSessionsUsecase.Callback.class),any(DateTime.class));

    }

    @Test
    public void onEventTest() {
        final InsightsFragmentState insightsFragmentState = mock(InsightsFragmentState.class);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        FlowManager uiFlowManagerMock = mock(FlowManager.class);
        when(context.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(uiFlowManagerMock);
        when(uiFlowManagerMock.getNextState(uiFlowManagerMock.getState(AppStates.POWER_SLEEP_CONNECTIVITY),AppStates.INSIGHTS)).thenReturn(insightsFragmentState);
        powerSleepConnectivityPresenter.onEvent(R.id.insights);
        verify(insightsFragmentState).navigate(any(FragmentLauncher.class));
    }

    @After
    public void tearDown() {
        view = null;
        context = null;
        uiView = null;
        powerSleepConnectivityPresenter = null;
        portListener = null;
    }

    class PowerSleepConenctivityPresenterMock extends PowerSleepConnectivityPresenter{

        public PowerSleepConenctivityPresenterMock(final Context context, final ConnectivityPowerSleepContract.View connectivityViewListener, final UIView uiView) {
            super(context, connectivityViewListener, uiView);
        }

        @Override
        public void initDataServiceInterface() {
            dataServicesManager=PowerSleepConnectivityPresenterTest.this.dataServicesManager;
        }

        @NonNull
        @Override
        protected SynchronizeSessionsUsecase getSynchronizeSessionsUsecase() {
            return synchronizeSessionsUsecase;
        }
    }

}
