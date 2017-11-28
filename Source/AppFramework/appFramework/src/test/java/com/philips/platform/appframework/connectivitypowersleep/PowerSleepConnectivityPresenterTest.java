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
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Session;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPort;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionsOldestToNewest;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Summary;
import com.philips.platform.appframework.connectivitypowersleep.insights.InsightsFragmentState;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.UIView;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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
    private RefAppBleReferenceAppliance bleReferenceAppliance;

    @Mock
    ConnectivityHelper connectivityHelper;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private DataServicesManager dataServicesManager;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    SynchronizeSessionsUsecase synchronizeSessionsUsecase;

    @Captor
    private ArgumentCaptor<DBFetchRequestListner<Moment>> dbFetchRequestListnerArgumentCaptor;

    @Captor
    private ArgumentCaptor<DBRequestListener<Moment>> dbRequestListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<SynchronizeSessionsUsecase.Callback> synCallbackArgumentCaptor;


    @Before
    public void setUp() throws Exception {
        context = mock(TestActivity.class);
        powerSleepConnectivityPresenter = new PowerSleepConenctivityPresenterMock(context, view, uiView);
        powerSleepConnectivityPresenter.initDataServiceInterface(dataServicesManager);
    }

    @Test
    public void synchronizeSessionDataTest() throws Exception {
        powerSleepConnectivityPresenter.synchronizeSessionData(bleReferenceAppliance);
        verify(dataServicesManager).fetchLatestMomentByType(any(String.class),dbFetchRequestListnerArgumentCaptor.capture());
        DBFetchRequestListner<Moment> momentDBFetchRequestListner=dbFetchRequestListnerArgumentCaptor.getValue();
        ArrayList<Moment> moments=new ArrayList<>();
        moments.add(new OrmMoment("","", new OrmMomentType(0,""),new DateTime(System.currentTimeMillis())));
        momentDBFetchRequestListner.onFetchSuccess(moments);
        verify(synchronizeSessionsUsecase).execute(any(RefAppBleReferenceAppliance.class),any(SynchronizeSessionsUsecase.Callback.class),any(DateTime.class));
    }

    @Test
    public void synchronizeSessionDataWithEmptyListTest() throws Exception {
        powerSleepConnectivityPresenter.synchronizeSessionData(bleReferenceAppliance);
        verify(dataServicesManager).fetchLatestMomentByType(any(String.class),dbFetchRequestListnerArgumentCaptor.capture());
        DBFetchRequestListner<Moment> momentDBFetchRequestListner=dbFetchRequestListnerArgumentCaptor.getValue();
        ArrayList<Moment> moments=new ArrayList<>();
        momentDBFetchRequestListner.onFetchSuccess(moments);
        verify(synchronizeSessionsUsecase).execute(any(RefAppBleReferenceAppliance.class),any(SynchronizeSessionsUsecase.Callback.class),any(DateTime.class));
    }

    @Test
    public void synchronizeSessionDataFailureTest() throws Exception {
        powerSleepConnectivityPresenter.synchronizeSessionData(bleReferenceAppliance);
        verify(dataServicesManager).fetchLatestMomentByType(any(String.class),dbFetchRequestListnerArgumentCaptor.capture());
        DBFetchRequestListner<Moment> momentDBFetchRequestListner=dbFetchRequestListnerArgumentCaptor.getValue();
        momentDBFetchRequestListner.onFetchFailure(new Exception());
        verify(view).showToast("Exception");
    }

    @Test
    public void onSynchronizeSucceedWithNonEmptyListTest() throws Exception {
        powerSleepConnectivityPresenter.synchronizeSessionData(bleReferenceAppliance);
        verify(dataServicesManager).fetchLatestMomentByType(any(String.class),dbFetchRequestListnerArgumentCaptor.capture());
        DBFetchRequestListner<Moment> momentDBFetchRequestListner=dbFetchRequestListnerArgumentCaptor.getValue();
        ArrayList<Moment> moments=new ArrayList<>();
        moments.add(new OrmMoment("","", new OrmMomentType(0,""),new DateTime(System.currentTimeMillis())));
        momentDBFetchRequestListner.onFetchSuccess(moments);
        verify(synchronizeSessionsUsecase).execute(any(RefAppBleReferenceAppliance.class),synCallbackArgumentCaptor.capture(),any(DateTime.class));
        SynchronizeSessionsUsecase.Callback syncCallback = synCallbackArgumentCaptor.getValue();

        List<Session> sessions = new ArrayList<>();
        Date date = new Date();
        Summary summary = new Summary(date, 38927392, 329832);
        Session session = new Session(summary);
        sessions.add(session);
        SessionsOldestToNewest sessionsOldestToNewest = new SessionsOldestToNewest(sessions);

        syncCallback.onSynchronizeSucceed(sessionsOldestToNewest);

        verify(view).hideProgressDialog();
        verify(view).showToast("Moment list size::"+ sessions.size());

        verify(connectivityHelper).calculateDeepSleepScore(summary.getDeepSleepTime());
        verify(connectivityHelper).createMoment(eq(String.valueOf(connectivityHelper.calculateDeepSleepScore(summary.getDeepSleepTime()))),
                eq(String.valueOf(summary.getDeepSleepTime())), eq(String.valueOf(summary.getTotalSleepTime())),
                any(DateTime.class), eq(""));
    }

    @Test
    public void onSynchronizeSucceedWithEmptyListTest() throws Exception {
        powerSleepConnectivityPresenter.synchronizeSessionData(bleReferenceAppliance);
        verify(dataServicesManager).fetchLatestMomentByType(any(String.class),dbFetchRequestListnerArgumentCaptor.capture());
        DBFetchRequestListner<Moment> momentDBFetchRequestListner=dbFetchRequestListnerArgumentCaptor.getValue();
        ArrayList<Moment> moments=new ArrayList<>();
        moments.add(new OrmMoment("","", new OrmMomentType(0,""),new DateTime(System.currentTimeMillis())));
        momentDBFetchRequestListner.onFetchSuccess(moments);
        verify(synchronizeSessionsUsecase).execute(any(RefAppBleReferenceAppliance.class),synCallbackArgumentCaptor.capture(),any(DateTime.class));
        SynchronizeSessionsUsecase.Callback syncCallback = synCallbackArgumentCaptor.getValue();

        List<Session> sessions = new ArrayList<>();
        SessionsOldestToNewest sessionsOldestToNewest = new SessionsOldestToNewest(sessions);

        syncCallback.onSynchronizeSucceed(sessionsOldestToNewest);

        verify(view).hideProgressDialog();
        verify(view).showToast("Moment list size::"+ sessions.size());
        verify(view).showToast("No new sessions available");
    }

    @Test
    public void onNoNewSessionsAvailableTest() throws Exception {
        powerSleepConnectivityPresenter.synchronizeSessionData(bleReferenceAppliance);
        verify(dataServicesManager).fetchLatestMomentByType(any(String.class),dbFetchRequestListnerArgumentCaptor.capture());
        DBFetchRequestListner<Moment> momentDBFetchRequestListner=dbFetchRequestListnerArgumentCaptor.getValue();
        ArrayList<Moment> moments=new ArrayList<>();
        moments.add(new OrmMoment("","", new OrmMomentType(0,""),new DateTime(System.currentTimeMillis())));
        momentDBFetchRequestListner.onFetchSuccess(moments);
        verify(synchronizeSessionsUsecase).execute(any(RefAppBleReferenceAppliance.class),synCallbackArgumentCaptor.capture(),any(DateTime.class));
        SynchronizeSessionsUsecase.Callback syncCallback = synCallbackArgumentCaptor.getValue();

        syncCallback.onNoNewSessionsAvailable();
        verify(view).showToast("No new sessions available");
    }

    @Test
    public void onErrorTest() throws Exception {
        powerSleepConnectivityPresenter.synchronizeSessionData(bleReferenceAppliance);
        verify(dataServicesManager).fetchLatestMomentByType(any(String.class),dbFetchRequestListnerArgumentCaptor.capture());
        DBFetchRequestListner<Moment> momentDBFetchRequestListner=dbFetchRequestListnerArgumentCaptor.getValue();
        ArrayList<Moment> moments=new ArrayList<>();
        moments.add(new OrmMoment("","", new OrmMomentType(0,""),new DateTime(System.currentTimeMillis())));
        momentDBFetchRequestListner.onFetchSuccess(moments);
        verify(synchronizeSessionsUsecase).execute(any(RefAppBleReferenceAppliance.class),synCallbackArgumentCaptor.capture(),any(DateTime.class));
        SynchronizeSessionsUsecase.Callback syncCallback = synCallbackArgumentCaptor.getValue();
        Exception exception = new Exception("error msg");
        syncCallback.onError(exception);
        verify(view).hideProgressDialog();
        verify(view).showToast("Error while fetching data:Error:" + exception.getMessage());
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

    @Test
    public void savePowerSleepMomentsDataSuccessTest() {
        powerSleepConnectivityPresenter.savePowerSleepMomentsData(new ArrayList<Session>());
        verify(dataServicesManager).saveMoments(any(List.class), dbRequestListenerArgumentCaptor.capture());
        DBRequestListener<Moment> momentDBRequestListener = dbRequestListenerArgumentCaptor.getValue();
        Exception ex = new Exception("error msg");
        momentDBRequestListener.onSuccess(new ArrayList<Moment>());
        verify(dataServicesManager).synchronize();
        verify(view).showToast("Power sleep data pushed to DB" + 0);
    }

    @Test
    public void savePowerSleepMomentsDataFailureTest() {
        powerSleepConnectivityPresenter.savePowerSleepMomentsData(new ArrayList<Session>());
        verify(dataServicesManager).saveMoments(any(List.class), dbRequestListenerArgumentCaptor.capture());
        DBRequestListener<Moment> momentDBRequestListener = dbRequestListenerArgumentCaptor.getValue();
        Exception ex = new Exception("error msg");
        momentDBRequestListener.onFailure(ex);
        verify(view).showToast("Exception while saving moment in DB" + ex.getMessage());
    }

    @Test
    public void fetchLatestSessionInfoSucessWithNullTest(){
        powerSleepConnectivityPresenter.fetchLatestSessionInfo();
        verify(dataServicesManager).fetchLatestMomentByType(any(String.class), dbFetchRequestListnerArgumentCaptor.capture());
        DBFetchRequestListner<Moment> momentDBFetchRequestListner = dbFetchRequestListnerArgumentCaptor.getValue();
        momentDBFetchRequestListner.onFetchSuccess(null);
        verify(view).showToast("Data not available.");
    }

    @Test
    public void fetchLatestSessionInfoSucessTest(){
        powerSleepConnectivityPresenter.fetchLatestSessionInfo();
        verify(dataServicesManager).fetchLatestMomentByType(any(String.class), dbFetchRequestListnerArgumentCaptor.capture());
        DBFetchRequestListner<Moment> momentDBFetchRequestListner = dbFetchRequestListnerArgumentCaptor.getValue();
        ArrayList<Moment> moments=new ArrayList<>();
        moments.add(new OrmMoment("","", new OrmMomentType(0,""),new DateTime(System.currentTimeMillis())));
        momentDBFetchRequestListner.onFetchSuccess(moments);
        verify(connectivityHelper).getSummaryInfoFromMoment(any(Moment.class));
        verify(view).updateScreenWithLatestSessionInfo(any(Summary.class));
    }

    @Test
    public void fetchLatestSessionInfoFailureTest(){
        powerSleepConnectivityPresenter.fetchLatestSessionInfo();
        verify(dataServicesManager).fetchLatestMomentByType(any(String.class), dbFetchRequestListnerArgumentCaptor.capture());
        DBFetchRequestListner<Moment> momentDBFetchRequestListner = dbFetchRequestListnerArgumentCaptor.getValue();
        momentDBFetchRequestListner.onFetchFailure(new Exception("error msg"));
        verify(view).showToast("Error while fetching data from power sleep device. Error::error msg");
    }
    @After
    public void tearDown() {
        view = null;
        context = null;
        uiView = null;
        powerSleepConnectivityPresenter = null;
        portListener = null;
        dataServicesManager = null;
        dbFetchRequestListnerArgumentCaptor = null;
        dbRequestListenerArgumentCaptor = null;
    }

    class PowerSleepConenctivityPresenterMock extends PowerSleepConnectivityPresenter{

        public PowerSleepConenctivityPresenterMock(final Context context, final ConnectivityPowerSleepContract.View connectivityViewListener, final UIView uiView) {
            super(connectivityHelper,context, connectivityViewListener, uiView);
        }

        @NonNull
        @Override
        protected SynchronizeSessionsUsecase getSynchronizeSessionsUsecase() {
            return synchronizeSessionsUsecase;
        }
    }

}
