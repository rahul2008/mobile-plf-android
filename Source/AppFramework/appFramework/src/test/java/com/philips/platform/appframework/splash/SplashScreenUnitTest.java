package com.philips.platform.appframework.splash;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.cocointerface.UICoCoUserRegImpl;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.DebugTestFragmentState;
import com.philips.platform.modularui.stateimpl.WelcomeRegistrationState;
import com.philips.platform.modularui.util.UIConstants;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SplashScreenUnitTest {
    Context context;
    SplashPresenter presenter;
    UIFlowManager flowManager;
    UICoCoUserRegImpl uiCoCoUserReg;

    SharedPreferenceUtility sharedPreferenceUtility;
    public SplashScreenUnitTest() {
    }

    @Before
    public void setUp() throws Exception {
        context = mock(Context.class);

        AppFrameworkApplication appFrameworkApplication = mock(AppFrameworkApplication.class);
        when(context.getApplicationContext()).thenReturn(appFrameworkApplication);
        flowManager = new UIFlowManager();
        when(appFrameworkApplication.getFlowManager()).thenReturn(flowManager);
        presenter = spy(new SplashPresenter());
        //presenter.sharedPreferenceUtility = mock(SharedPreferenceUtility.class);
        sharedPreferenceUtility = mock(SharedPreferenceUtility.class);
        //when(presenter.getSharedPreferenceUtility(any(Context.class))).thenReturn(presenter.sharedPreferenceUtility);


    }


    @Test
    public void loadTest() {
        DebugTestFragmentState debugTestFragment = new DebugTestFragmentState(UIState.UI_DEBUG_FRAGMENT_STATE);
        flowManager.setCurrentState(debugTestFragment);
        Assert.assertFalse(flowManager.getCurrentState().getStateID() == UIState.UI_USER_REGISTRATION_STATE);
    }

    @Test
    public void launchRegistrationTest() {
        WelcomeRegistrationState welcomeState = new WelcomeRegistrationState(UIState.UI_WELCOME_REGISTRATION_STATE);
        flowManager.setCurrentState(welcomeState);
        User user= UICoCoUserRegImpl.getInstance().getUserObject(RuntimeEnvironment.application.getApplicationContext());
        presenter.onLoad(context);
        Assert.assertEquals(flowManager.getCurrentState().getStateID(), UIState.UI_WELCOME_REGISTRATION_STATE);
    }


    @Test
    public void launchWelcomeTest() {
//        when(sharedPreferenceUtility.getPreferenceBoolean(UIConstants.DONE_PRESSED)).thenReturn(false);
        uiCoCoUserReg = (UICoCoUserRegImpl) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION);
        //when((UICoCoUserRegImpl) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION)).thenReturn(uiCoCoUserReg);
        sharedPreferenceUtility.writePreferenceBoolean(UIConstants.DONE_PRESSED,false);
        presenter.onLoad(context);
        Assert.assertEquals(flowManager.getCurrentState().getStateID(), UIState.UI_WELCOME_STATE);
    }
}

