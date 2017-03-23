package com.philips.platform.baseapp.screens.connectivity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.GradleRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.ConnectivityFragment;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static junit.framework.Assert.assertNotNull;

@RunWith(GradleRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 24)
public class ConnectivityFragmentTest {
    private HamburgerActivity hamburgerActivity = null;
    private ConnectivityFragmentMock connectivityFragmentMock;

    @Before
    public void setUp(){
        hamburgerActivity = Robolectric.buildActivity(HamburgerActivity.class).create().start().get();
        connectivityFragmentMock = new ConnectivityFragmentMock();
        SupportFragmentTestUtil.startFragment(connectivityFragmentMock);
    }

    @Test
    public void testConnectivityFragmentLaunch(){
        assertNotNull(connectivityFragmentMock);
    }

    public static class ConnectivityFragmentMock extends ConnectivityFragment{
        View view;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.af_connectivity_fragment,container,false);
            return view;
        }
    }
}
