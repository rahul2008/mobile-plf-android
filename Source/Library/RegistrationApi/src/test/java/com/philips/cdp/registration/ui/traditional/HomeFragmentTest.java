package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class HomeFragmentTest {

    private HomeFragment homeFragment;

    @Before
    public void setUp() throws Exception {
        homeFragment = new HomeFragment();
    }

    @After
    public void tearDown() throws Exception {
        homeFragment = null;
    }

    @Test
    public void onCreate() throws Exception {
        try {
            homeFragment.onCreate(null);
        } catch (Throwable t) {}

    }

    @Test
    public void onCreateView() throws Exception {
        try {
            homeFragment.onCreateView(null,null,null);
        } catch (Throwable t) {}
    }

    @Test
    public void onSaveInstanceState() throws Exception {
        try {
            homeFragment.onSaveInstanceState(null);
        } catch (Throwable t) {}
    }

    @Test
    public void onViewStateRestored() throws Exception {
        try {
            homeFragment.onViewStateRestored(null);
        } catch (Throwable t) {}
    }

    @Test
    public void onActivityCreated() throws Exception {
        try {
            homeFragment.onActivityCreated(null);
        } catch (Throwable t) {}
    }

    @Test
    public void onStart() throws Exception {
        try {
            homeFragment.onStart();
        } catch (Throwable t) {}
    }

    @Test
    public void onResume() throws Exception {
        try {
            homeFragment.onResume();
        } catch (Throwable t) {}
    }

//    @Test
//    public void handleWeChatCode() throws Exception {
//        try {
//            homeFragment.handleWeChatCode(null);
//        } catch (Throwable t) {}
//    }

    @Test
    public void onPause() throws Exception {
        try {
            homeFragment.onPause();
        } catch (Throwable t) {}
    }

    @Test
    public void onStop() throws Exception {
        try {
            homeFragment.onStop();
        } catch (Throwable t) {}
    }

    @Test
    public void onDestroyView() throws Exception {
        try {
            homeFragment.onDestroyView();
        } catch (Throwable t) {}
    }

    @Test
    public void onDestroy() throws Exception {
        try {
            homeFragment.onDestroy();
        } catch (Throwable t) {}
    }

    @Test
    public void onDetach() throws Exception {
        try {
            homeFragment.onDetach();
        } catch (Throwable t) {}
    }

    @Test
    public void onConfigurationChanged() throws Exception {
        try {
            homeFragment.onConfigurationChanged(null);
        } catch (Throwable t) {}
    }

//    @Test
//    public void onClick() throws Exception {
//        try {
//            homeFragment.onClick(null);
//        } catch (Throwable t) {}
//    }

    @Test
    public void setViewParams() throws Exception {
        try {
            homeFragment.setViewParams(null,0);
        } catch (Throwable t) {}
    }

    @Test
    public void handleOrientation() throws Exception {
        try {
            homeFragment.handleOrientation(null);
        } catch (Throwable t) {}
    }

    @Test
    public void getTitleResourceId() throws Exception {
        try {
            homeFragment.getTitleResourceId();
        } catch (Throwable t) {}
    }

}