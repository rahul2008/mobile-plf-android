/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.stateimpl;

import android.support.annotation.NonNull;

import com.iap.demouapp.IapDemoUAppInterface;
import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
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
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class DemoIAPStateTest {

    DemoIAPStateMock demoIAPStateMock;

    @Mock
    IapDemoUAppInterface uappDemoInterface;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        demoIAPStateMock = new DemoIAPStateMock();
    }

    @Test
    public void testDemoIAPNavigate() {
        demoIAPStateMock.updateDataModel();
        demoIAPStateMock.init(application);
        demoIAPStateMock.navigate(null);
        verify(uappDemoInterface).launch(any(ActivityLauncher.class), (UappLaunchInput)isNull());
    }

    @Test
    public void testGetUappInterface() {
        assertNotNull(new DemoIAPState().getIapDemoUAppInterface());
    }

    @After
    public void tearDown() {
        demoIAPStateMock = null;
        uappDemoInterface = null;
    }

    class DemoIAPStateMock extends DemoIAPState {
        @NonNull
        @Override
        protected IapDemoUAppInterface getIapDemoUAppInterface() {
            return uappDemoInterface;
        }
    }

}