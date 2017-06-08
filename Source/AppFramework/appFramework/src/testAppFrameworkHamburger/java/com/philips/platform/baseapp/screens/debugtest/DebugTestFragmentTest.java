package com.philips.platform.baseapp.screens.debugtest;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.AppStateConfiguration;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class DebugTestFragmentTest extends TestCase {
    private HamburgerActivity hamburgerActivity;
    private DebugTestFragment debugFragment;
    private FragmentActivity fragmentActivityMock;
//    private AppFrameworkApplication appFrameworkApplication;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        super.setUp();
        hamburgerActivity = Robolectric.buildActivity(HamburgerActivity.class).create().start().visible().get();
        debugFragment = new DebugTestFragment();
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(debugFragment,"DebugFragmentTest").commit();
        fragmentActivityMock = mock(FragmentActivity.class);
//        appFrameworkApplication = mock(AppFrameworkApplication.class);
    }

    @Test
    public void testDebugFragment() throws Exception{
        FragmentManager fragmentManager = hamburgerActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        assertEquals(1,fragmentCount);
    }

//    @Test
//    public void testView() throws Exception {
//        Spinner spinner = (Spinner) debugFragment.getView().findViewById(R.id.spinner);
//        assertEquals(3, spinner.getAdapter().getCount());
//    }
//
//    @Test
//    public void testSettingState()  throws Exception {
//        String configurationType[] =
//                {
//                        AppStateConfiguration.STAGING.getValue(),
//                        AppStateConfiguration.TEST.getValue(),
//                        AppStateConfiguration.DEVELOPMENT.getValue()
//                };
//
//        Spinner spinner = new Spinner(hamburgerActivity);
//        ArrayAdapter<String> configType = debugFragment.getArrayAdapter(configurationType);
//        when(debugFragment.getSpinner()).thenReturn(spinner);
//        debugFragment.setSpinnerAdaptor(configurationType);
//        verify(spinner).setAdapter(configType);
//    }

//    @Test
//    public void getList()  throws Exception {
//        String configurationType[] =
//                {
//                        AppStateConfiguration.STAGING.getValue(),
//                        AppStateConfiguration.TEST.getValue(),
//                        AppStateConfiguration.DEVELOPMENT.getValue()
//                };
//        List<String> list = debugFragment.getList(configurationType);
//        assertTrue(list.contains(AppStateConfiguration.STAGING.getValue()));
//        assertTrue(list.contains(AppStateConfiguration.DEVELOPMENT.getValue()));
//        assertTrue(list.contains(AppStateConfiguration.TEST.getValue()));
//        assertFalse(list.contains("Production"));
//    }
}