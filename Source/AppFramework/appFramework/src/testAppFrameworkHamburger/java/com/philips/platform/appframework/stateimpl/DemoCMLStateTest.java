/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by philips on 08/08/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class DemoCMLStateTest {

    private DemoCMLState demoCMLState;
    private ActivityController<TestActivity> activityController;
    private TestActivity testActivity;
    private FragmentLauncher fragmentLauncher;

    @Mock
    private CommlibUapp commlibUapp;

    @Mock
    Context context;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        demoCMLState=new DemoCMLStateMock();
        activityController= Robolectric.buildActivity(TestActivity.class);
        testActivity=activityController.create().start().get();
        fragmentLauncher = new FragmentLauncher(testActivity, R.id.frame_container, testActivity);
    }

    @After
    public void tearDown() throws Exception {
        activityController.pause().stop().destroy();
        testActivity=null;
        fragmentLauncher=null;
        demoCMLState=null;
    }

    @Test
    public void navigate() throws Exception {
        demoCMLState.init(context);
        demoCMLState.updateDataModel();
        demoCMLState.navigate(fragmentLauncher);
        verify(commlibUapp).launch(any(UiLauncher.class),any(UappLaunchInput.class));
    }

    private class DemoCMLStateMock extends DemoCMLState{
        @Override
        public CommlibUapp getCommLibUApp() {
            return commlibUapp;
        }
    }

}