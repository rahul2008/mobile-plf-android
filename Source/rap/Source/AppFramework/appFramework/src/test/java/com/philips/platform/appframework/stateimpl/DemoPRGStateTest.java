/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appframework.stateimpl;

import androidx.annotation.NonNull;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.prdemoapp.PRDemoAppuAppInterface;
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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class DemoPRGStateTest {

    DemoPRGStateMock demoPRGStateMock;

    @Mock
    PRDemoAppuAppInterface uappDemoInterface;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        demoPRGStateMock = new DemoPRGStateMock();
    }

    @Test
    public void testDemoPRGNavigate() {
        demoPRGStateMock.updateDataModel();
        demoPRGStateMock.init(application);
        demoPRGStateMock.navigate(null);
        verify(uappDemoInterface).launch(any(ActivityLauncher.class), (UappLaunchInput)isNull());
    }

    @Test
    public void testGetUappInterface() {
        assertNotNull(new DemoPRGState().getPrDemoAppuAppInterface());
    }

    @After
    public void tearDown() {
        demoPRGStateMock = null;
        uappDemoInterface = null;
    }

    class DemoPRGStateMock extends DemoPRGState {
        @NonNull
        @Override
        protected PRDemoAppuAppInterface getPrDemoAppuAppInterface() {
            return uappDemoInterface;
        }
    }

}