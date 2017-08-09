package com.philips.platform.appframework.connectivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;

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
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;

import static com.philips.platform.appframework.connectivity.ConnectivityFragment.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION;
import static com.philips.platform.appframework.connectivity.ConnectivityFragment.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.philips.platform.appframework.connectivity.ConnectivityFragment.REQUEST_ENABLE_BT;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;
/**
 * Created by philips on 29/07/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class ConnectivityFragmentTest {


    private ConnectivityFragment connectivityFragment;

    private TextView connectionState;

    private EditText momentValueEditText;
    private EditText editText;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private ActivityController<TestActivity> activityController;

    private TestActivity testActivity;

    @Mock
    private static ConnectivityPresenter connectivityPresenter;

    @Mock
    private static BluetoothAdapter bluetoothAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activityController= Robolectric.buildActivity(TestActivity.class);
        testActivity=activityController.create().start().get();
        connectivityFragment = new ConnectivityFragmentMock();
        testActivity.getSupportFragmentManager().beginTransaction().add(connectivityFragment,null).commit();
        connectionState = (TextView) connectivityFragment.getView().findViewById(R.id.connectionState);
        momentValueEditText = (EditText) connectivityFragment.getView().findViewById(R.id.moment_value_editbox);
        editText = (EditText) connectivityFragment.getView().findViewById(R.id.measurement_value_editbox);
    }

    @Test
    public void actionBarTitle() {
        Assert.assertEquals(connectivityFragment.getResources().getString(R.string.RA_ConnectivityScreen_Menu_Title), connectivityFragment.getActionbarTitle());
    }


    @Test
    public void onProcessMomentSuccessTest() {
        connectivityFragment.onProcessMomentSuccess("50");
        Assert.assertEquals("50", momentValueEditText.getText().toString());
    }

    @Test
    public void isFragmentLiveTest() {
        Assert.assertTrue(connectivityFragment.isFragmentLive());
    }


    @Test
    public void updateMeasurementValuetext() {
        connectivityFragment.updateDeviceMeasurementValue("50");
        Assert.assertEquals("50", editText.getText().toString());
    }

    @Test
    public void updateConnectionStateText() {
        connectivityFragment.updateConnectionStateText("Disconnected");
        Assert.assertEquals("Disconnected", connectionState.getText().toString());
    }

    @Test
    public void onDeviceMeasurementErrorTest() {
        connectivityFragment.onDeviceMeasurementError(Error.NO_REQUEST_DATA, "No request data");
        Assert.assertEquals("Error while reading measurement from reference board" + Error.NO_REQUEST_DATA.getErrorMessage(), ShadowToast.getTextOfLatestToast());
    }

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        connectivityFragment=null;
    }

    @Test
    public void testStartDiscoveryClick(){
        connectivityFragment.getView().findViewById(R.id.start_connectivity_button).performClick();
        Mockito.when(bluetoothAdapter.isEnabled()).thenReturn(false);
        assertNotNull(shadowOf(testActivity).getNextStartedActivity());
    }

    @Test
    public void testMomentValueClick(){
        connectivityFragment.getView().findViewById(R.id.get_momentumvalue_button).performClick();
        verify(connectivityPresenter).processMoment(any(String.class));
    }

//    @Test
//    public void testStartDiscovery(){
//        Mockito.when(bluetoothAdapter.isEnabled()).thenReturn(true);
//        ShadowApplication application= Shadows.shadowOf(testActivity.getApplication());
//        String[] perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
//        application.grantPermissions(perms);
//        connectivityFragment.getView().findViewById(R.id.start_connectivity_button).performClick();
//        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
//    }

    @Test
    public void onProcessMomentProgressTest(){
        connectivityFragment.onProcessMomentProgress("Progress dialog");
        assertNotNull(ShadowDialog.getLatestDialog());
        connectivityFragment.onProcessMomentProgress("Progress dialog");
        assertNotNull(ShadowDialog.getLatestDialog());
    }

    @Test
    public void onRequestPermissionsResultTest(){
        int[] permission={PackageManager.PERMISSION_DENIED};
        connectivityFragment.onRequestPermissionsResult(MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION,null,permission);
        assertEquals("Need permission",ShadowToast.getTextOfLatestToast());
        connectivityFragment.onRequestPermissionsResult(MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION,null,permission);
        assertEquals("Need permission",ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void onProcessMomentErrorTest(){
        connectivityFragment.onProcessMomentError("Error");
        assertEquals("Error",ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void onActivityResultTest(){
        connectivityFragment.onActivityResult(REQUEST_ENABLE_BT, Activity.RESULT_CANCELED,new Intent());
        assertEquals("Please enable bluetooth",ShadowToast.getTextOfLatestToast());
//        connectivityFragment.onActivityResult(REQUEST_ENABLE_BT, Activity.RESULT_OK,new Intent());
    }

     public static class ConnectivityFragmentMock extends ConnectivityFragment{

        @Override
        protected ConnectivityPresenter getConnectivityPresenter() {
            return connectivityPresenter;
        }

         @Override
         protected BluetoothAdapter getBluetoothAdapter() {
             return bluetoothAdapter;
         }
     }

}