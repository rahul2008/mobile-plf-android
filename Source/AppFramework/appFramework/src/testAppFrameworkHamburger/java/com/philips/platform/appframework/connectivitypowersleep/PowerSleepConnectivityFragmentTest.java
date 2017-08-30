/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivitypowersleep;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.ConnectivityBaseFragment;
import com.philips.platform.appframework.ConnectivityDeviceType;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.ConnectivityFragment;
import com.philips.platform.appframework.connectivity.ConnectivityPresenter;

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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Test for ConnectivityFragment
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class PowerSleepConnectivityFragmentTest {


    private PowerSleepConnectivityFragment connectivityFragment;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private ActivityController<TestActivity> activityController;

    private TestActivity testActivity;

    @Mock
    private static PowerSleepConnectivityPresenter connectivityPresenter;

    @Mock
    private static BluetoothAdapter bluetoothAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activityController = Robolectric.buildActivity(TestActivity.class);
        testActivity = activityController.create().start().get();
        connectivityFragment = new ConnectivityFragmentMock();
        testActivity.getSupportFragmentManager().beginTransaction().add(connectivityFragment, null).commit();
    }

    @Test
    public void actionBarTitle() {
        Assert.assertEquals(connectivityFragment.getResources().getString(R.string.RA_ConnectivityScreen_Menu_Title), connectivityFragment.getActionbarTitle());
    }

    @Test
    public void isFragmentLiveTest() {
        Assert.assertTrue(connectivityFragment.isFragmentLive());
    }

    @Test
    public void testStartDiscoveryClick() {
        connectivityFragment.getView().findViewById(R.id.powersleep_sync).performClick();
        Mockito.when(bluetoothAdapter.isEnabled()).thenReturn(false);
        assertNotNull(shadowOf(testActivity).getNextStartedActivity());
    }

    @Test
    public void onRequestPermissionsResultTest() {
        int[] permission = {PackageManager.PERMISSION_DENIED};
        connectivityFragment.onRequestPermissionsResult(ConnectivityBaseFragment.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, null, permission);
        assertEquals("Need permission", ShadowToast.getTextOfLatestToast());
        connectivityFragment.onRequestPermissionsResult(ConnectivityBaseFragment.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION, null, permission);
        assertEquals("Need permission", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void updateSessionDataTest() {
        connectivityFragment.showProgressBar();
        connectivityFragment.updateSessionData(19200000, 3, 5400000);
        assertEquals("320 mins", ((TextView) connectivityFragment.getView().findViewById(R.id.sleep_time_value)).getText().toString());
        assertEquals("90 mins", ((TextView) connectivityFragment.getView().findViewById(R.id.deep_sleep_time_value)).getText().toString());
    }

    @Test
    public void onActivityResultTest() {
        connectivityFragment.onActivityResult(ConnectivityBaseFragment.REQUEST_ENABLE_BT, Activity.RESULT_CANCELED, new Intent());
        assertEquals("Please enable bluetooth", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void showErrorTest() {
        connectivityFragment.showProgressBar();
        connectivityFragment.showError(Error.CANNOT_CONNECT, "");
        assertEquals(testActivity.getString(R.string.RA_DLS_data_fetch_error), ShadowToast.getTextOfLatestToast());
    }

    public static class ConnectivityFragmentMock extends PowerSleepConnectivityFragment {

        @Override
        protected PowerSleepConnectivityPresenter getConnectivityPresenter() {
            return connectivityPresenter;
        }

        @Override
        protected BluetoothAdapter getBluetoothAdapter() {
            return bluetoothAdapter;
        }

        @Override
        protected void removeApplianceListener() {

        }
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        connectivityFragment = null;
    }

}