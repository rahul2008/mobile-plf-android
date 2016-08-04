package com.philips.platform.appframework;

import android.content.Context;

import com.philips.platform.appframework.introscreen.WelcomeActivity;
import com.philips.platform.appframework.introscreen.WelcomePresenter;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.DebugTestFragmentState;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
//@RunWith(RobolectricTestRunner.class)
public class WelcomeScreenUnitTestcase {
    Context context;
    WelcomePresenter presenter;
    UIFlowManager flowManager;

    public WelcomeScreenUnitTestcase() {
    }

    @Before
    public void setUp() throws Exception {
        context = mock(WelcomeActivity.class);
        AppFrameworkApplication appFrameworkApplication = mock(AppFrameworkApplication.class);
        when(context.getApplicationContext()).thenReturn(appFrameworkApplication);
     //   when(context).thenReturn(WelcomeActivity);
        flowManager = new UIFlowManager();
        when(appFrameworkApplication.getFlowManager()).thenReturn(flowManager);
        presenter = new WelcomePresenter();
    }

    @Test
    public void testSkipClickTest() {

        presenter.onClick(R.id.appframework_skip_button,context);
        Assert.assertEquals(UIState.UI_USER_REGISTRATION_STATE,flowManager.getCurrentState().getStateID());
    }

    @Test
    public void testLoadTest() {
        DebugTestFragmentState debugTestFragment=new DebugTestFragmentState(UIState.UI_DEBUG_FRAGMENT_STATE);
        flowManager.setCurrentState(debugTestFragment);
        Assert.assertFalse(flowManager.getCurrentState().getStateID()==UIState.UI_USER_REGISTRATION_STATE);
    }

    @Test
    public void testDoneClickTest() {

        presenter.onClick(R.id.start_registration_button,context);
        Assert.assertEquals(UIState.UI_USER_REGISTRATION_STATE,flowManager.getCurrentState().getStateID());
    }
}

