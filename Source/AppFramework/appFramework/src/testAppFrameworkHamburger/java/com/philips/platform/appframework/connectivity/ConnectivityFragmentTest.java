package com.philips.platform.appframework.connectivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;


/**
 * Created by philips on 29/07/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class ConnectivityFragmentTest {

    private TestActivity testActivity;

    private ConnectivityFragment connectivityFragment;

    private TextView connectionState;

    private EditText momentValueEditText;
    private EditText editText;
    private Button btnGetMoment;

//    @Mock
//    CommCentral commCentral;
//
//    @Mock
//    ApplianceManager applianceManager;
//
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() {
//        Shadows.shadowOf(RuntimeEnvironment.application).grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
//        Shadows.shadowOf(RuntimeEnvironment.application).grantPermissions(Manifest.permission.ACCESS_COARSE_LOCATION);
//        Shadows.shadowOf(RuntimeEnvironment.application).grantPermissions(Manifest.permission.BLUETOOTH);
//        Shadows.shadowOf(RuntimeEnvironment.application).grantPermissions(Manifest.permission.BLUETOOTH_ADMIN);
        MockitoAnnotations.initMocks(this);
        testActivity = Robolectric.buildActivity(TestActivity.class).create().start().get();
        connectivityFragment = new ConnectivityFragment();
//        when(connectivityFragment.getCommCentral(testActivity)).thenReturn(commCentral);
//        when(commCentral.getApplianceManager()).thenReturn(applianceManager);
        startFragment(connectivityFragment, TestActivity.class);
//        testActivity.getSupportFragmentManager().beginTransaction().add(connectivityFragment,null).commit();
        connectionState = (TextView) connectivityFragment.getView().findViewById(R.id.connectionState);
        momentValueEditText = (EditText) connectivityFragment.getView().findViewById(R.id.moment_value_editbox);
        editText = (EditText) connectivityFragment.getView().findViewById(R.id.measurement_value_editbox);
        btnGetMoment = (Button) connectivityFragment.getView().findViewById(R.id.get_momentumvalue_button);
    }
//
//    @Test
//    public void actionBarTitle(){
//        Assert.assertEquals(testActivity.getResources().getString(R.string.RA_ConnectivityScreen_Menu_Title),connectivityFragment.getActionbarTitle());
//    }

//    @Test
//    public void startDiscoverytest() {
//        connectivityFragment.startDiscovery();
//        Assert.assertEquals(testActivity.getResources().getString(R.string.RA_Connectivity_Connection_Status_Disconnected), connectionState.getText().toString());
//    }


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
        Assert.assertEquals("Error while reading measurement from reference board"+Error.NO_REQUEST_DATA.getErrorMessage(),ShadowToast.getTextOfLatestToast());
    }


//    @Test
//    public void onClickTest(){
//        connectivityFragment.onClick(btnGetMoment);
//        verify(connectivityFragment).check
//    }
}