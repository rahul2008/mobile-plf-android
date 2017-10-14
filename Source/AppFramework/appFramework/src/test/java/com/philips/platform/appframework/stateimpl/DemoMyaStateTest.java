package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.mya.demouapp.MyaDemouAppInterface;
import com.philips.platform.mya.demouapp.MyaDemouAppDependencies;
import com.philips.platform.mya.demouapp.MyaDemouAppLaunchInput;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class DemoMyaStateTest {

    private ActivityLauncher activityLauncher;

    @Mock
    DemoMyaStateMock demoMyaStateMock;

    @Mock
    MyaDemouAppDependencies myAccountDependencies;

    @Mock
    MyaDemouAppInterface myAccountDemoUAppInterface;

    @Mock
    AppInfra appInfra;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        demoMyaStateMock = new DemoMyaStateMock();
        demoMyaStateMock.init(application);
        demoMyaStateMock.updateDataModel();
        activityLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 0);
    }


    @Test
    public void testLaunchDemoMyaState() {
        demoMyaStateMock.navigate(activityLauncher);
        verify(myAccountDemoUAppInterface).launch(any(UiLauncher.class), any(MyaDemouAppLaunchInput.class));
    }

    @Test
    public void getUappDependenciesTest() {
        assertNotNull(new DemoMyaState().getUappDependencies(application));
    }

    @After
    public void tearDown() {
        activityLauncher = null;
        demoMyaStateMock = null;
        myAccountDependencies = null;
    }

    private class DemoMyaStateMock extends DemoMyaState {
        @NonNull
        @Override
        protected MyaDemouAppDependencies getUappDependencies(Context context) {
            return myAccountDependencies;
        }

        @NonNull
        @Override
        protected MyaDemouAppInterface getMyAccountDemoUAppInterface() {
            return myAccountDemoUAppInterface;
        }
    }
}
