package com.philips.platform.appframework.stateimpl;

import android.support.annotation.NonNull;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.urdemo.URDemouAppInterface;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by philips on 06/09/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class DemoUSRStateTest {

    DemoUSRStateMock demoUSRStateMock;

    @Mock
    URDemouAppInterface uappDemoInterface;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        demoUSRStateMock = new DemoUSRStateMock();
    }

    @Test
    public void testDemoUSRNavigate(){
        demoUSRStateMock.updateDataModel();
        demoUSRStateMock.init(application);
        demoUSRStateMock.navigate(null);
        verify(uappDemoInterface).launch(any(ActivityLauncher.class),any(UappLaunchInput.class));
    }

    class DemoUSRStateMock extends DemoUSRState{
        @NonNull
        @Override
        protected URDemouAppInterface getUrDemouAppInterface() {
            return uappDemoInterface;
        }
    }

}