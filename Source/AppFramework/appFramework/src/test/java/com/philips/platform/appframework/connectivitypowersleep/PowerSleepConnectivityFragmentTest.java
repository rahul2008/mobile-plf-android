/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivitypowersleep;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.AbstractConnectivityBaseFragment;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Summary;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.trackers.DataServicesManager;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowProgressDialog;
import org.robolectric.shadows.ShadowToast;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowToast.getTextOfLatestToast;

/**
 * Test for ConnectivityFragment
 */
@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class, sdk=25)
public class PowerSleepConnectivityFragmentTest {


    private PowerSleepConnectivityFragment connectivityFragment;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private ActivityController<TestActivity> activityController;

    private TestActivity testActivity;

    @Mock
    private static PowerSleepConnectivityPresenter connectivityPresenter;

    @Mock
    private static DataServicesManager dataServicesManager;

    @Mock
    private static BluetoothAdapter bluetoothAdapter;



    private TextView sleepScoretextView;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activityController = Robolectric.buildActivity(TestActivity.class);
        testActivity = activityController.create().start().get();
        connectivityFragment = new ConnectivityFragmentMock();
        testActivity.getSupportFragmentManager().beginTransaction().add(connectivityFragment, null).commit();
        sleepScoretextView=connectivityFragment.getView().findViewById(R.id.sleepoverview_score);
    }

    @Test
    public void actionBarTitle() {
        Assert.assertEquals(connectivityFragment.getResources().getString(R.string.RA_DLS_power_sleep_connectivity), connectivityFragment.getActionbarTitle());
    }

    @Test
    public void isFragmentLiveTest() {
        Assert.assertTrue(connectivityFragment.isFragmentLive());
    }

    @Test
    public void testBluetoothActivityLaunched() {
        connectivityFragment.getView().findViewById(R.id.powersleep_sync).performClick();
        Mockito.when(bluetoothAdapter.isEnabled()).thenReturn(false);
        assertNotNull(shadowOf(testActivity).getNextStartedActivity());
    }

    @Test
    public void updateScreenWithLatestSessionInfoTest(){
        connectivityFragment.updateScreenWithLatestSessionInfo(new Summary(new Date(System.currentTimeMillis()),24289123,6950712));
        assertEquals(sleepScoretextView.getText().toString(),"95");
    }
    @Test
    public void onRequestPermissionsResultTest() {
        int[] permission = {PackageManager.PERMISSION_DENIED};
        connectivityFragment.onRequestPermissionsResult(AbstractConnectivityBaseFragment.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, null, permission);
        assertEquals("Need permission", getTextOfLatestToast());
        connectivityFragment.onRequestPermissionsResult(AbstractConnectivityBaseFragment.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION, null, permission);
        assertEquals("Need permission", getTextOfLatestToast());
    }

//    @Test
//    public void updateSessionDataTest() {
//        connectivityFragment.updateSessionData(19200000, 3, 5400000,2343243223L);
//        assertEquals("320 mins", ((TextView) connectivityFragment.getView().findViewById(R.id.sleep_time_value)).getText().toString());
//        assertEquals("90 mins", ((TextView) connectivityFragment.getView().findViewById(R.id.deep_sleep_time_value)).getText().toString());
//    }

    @Test
    public void onActivityResultTest() {
        connectivityFragment.onActivityResult(AbstractConnectivityBaseFragment.REQUEST_ENABLE_BT, Activity.RESULT_CANCELED, new Intent());
        assertEquals("Please enable bluetooth", getTextOfLatestToast());
    }

    @Test
    public void showErrorTest() {
        connectivityFragment.showError(Error.CANNOT_CONNECT, "");
        assertEquals(testActivity.getString(R.string.RA_DLS_data_fetch_error), getTextOfLatestToast());
    }

    @Test
    public void insightsButtonClickTest() {
        connectivityFragment.getView().findViewById(R.id.insights).performClick();
        verify(connectivityPresenter).onEvent(R.id.insights);
    }


    @Test
    public void getConnectivityPresenterTest() {
        ConnectivityFragmentMock connectivityFragment = new ConnectivityFragmentMock();
        connectivityFragment.selectSuperPresenter(true);
        testActivity.getSupportFragmentManager().beginTransaction().add(connectivityFragment, null).commit();
        assertNotNull(connectivityFragment.getConnectivityPresenter());
    }

    @Test
    public void onSyncCompleteTest() throws Exception {
        connectivityFragment.onSyncComplete();
        verify(connectivityPresenter,times(2)).fetchLatestSessionInfo();
    }

    @Test
    public void showProgressDialogTest() throws Exception {
        connectivityFragment.showProgressDialog();
        Dialog dialog=ShadowProgressDialog.getLatestDialog();
        assertTrue(dialog.isShowing());
        connectivityFragment.hideProgressDialog();
        assertFalse(dialog.isShowing());
    }

//    @Test
//    public void hideProgressDialogTest() throws Exception {
//        connectivityFragment.hideProgressDialog();
//        Dialog dialog=ShadowProgressDialog.get;
//        assertFalse(dialog.isShowing());
//    }
    @Test
    public void onSyncFailedTest() throws Exception {
        connectivityFragment.onSyncFailed(new Exception());
        String message=ShadowToast.getTextOfLatestToast();
        assertEquals(message,"Sync failed");
    }

    @Test
    public void onDBChangeSuccessTest() throws Exception {
        connectivityFragment.dBChangeSuccess(SyncType.MOMENT);
        verify(dataServicesManager).synchronize();
    }

    @Test
    public void onDBChangeFailedTest() throws Exception {
        connectivityFragment.dBChangeFailed(new Exception());
        String message=ShadowToast.getTextOfLatestToast();
        assertEquals(message,"DB Change failed");
    }

    @Test
    public void getFragmentActivityTest() {
        assertNotNull(connectivityFragment.getFragmentActivity());
    }

    public static class ConnectivityFragmentMock extends PowerSleepConnectivityFragment {

        private boolean isSuperPresenterChosen;

        public void selectSuperPresenter(boolean isPresenter) {
            this.isSuperPresenterChosen = isPresenter;
        }
        @Override
        protected PowerSleepConnectivityPresenter getConnectivityPresenter() {
            return isSuperPresenterChosen ? super.getConnectivityPresenter() : connectivityPresenter;
        }

        @Override
        protected DataServicesManager getDataServicesManager() {
            return dataServicesManager;
        }

        @Override
        protected BluetoothAdapter getBluetoothAdapter() {
            return bluetoothAdapter;
        }

    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        connectivityPresenter = null;
        connectivityFragment = null;
        testActivity = null;
        activityController = null;
        dataServicesManager = null;
    }

}