package com.philips.platform.appframework.connectivity;

import android.widget.EditText;
import android.widget.TextView;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
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


    private ConnectivityFragment connectivityFragment;

    private TextView connectionState;

    private EditText momentValueEditText;
    private EditText editText;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        connectivityFragment = new ConnectivityFragment();
        startFragment(connectivityFragment);
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
        connectivityFragment=null;
    }

}