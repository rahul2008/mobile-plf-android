/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.support.annotation.NonNull;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.uappdemo.UappDemoInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
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

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class DemoUFWStateTest {

    DemoUFWStateMock demoUFWStateMock;

    @Mock
    UappDemoInterface uappDemoInterface;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        demoUFWStateMock = new DemoUFWStateMock();
    }

    @Test
    public void testDemoUFWNavigate() {
        demoUFWStateMock.updateDataModel();
        demoUFWStateMock.init(application);
        demoUFWStateMock.navigate(null);
        verify(uappDemoInterface).launch(any(ActivityLauncher.class), any(UappLaunchInput.class));
    }

    @Test
    public void testGetUappInterface() {
        assertNotNull(new DemoUFWState().getUappDemoInterface());
    }

    @After
    public void tearDown() {
        demoUFWStateMock = null;
        uappDemoInterface = null;
    }

    class DemoUFWStateMock extends DemoUFWState {
        @NonNull
        @Override
        protected UappDemoInterface getUappDemoInterface() {
            return uappDemoInterface;
        }
    }

}