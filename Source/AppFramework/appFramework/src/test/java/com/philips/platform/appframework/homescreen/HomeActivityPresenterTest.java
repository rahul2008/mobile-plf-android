package com.philips.platform.appframework.homescreen;

import android.content.Context;
import android.test.ApplicationTestCase;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.DebugTestFragmentState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.InAppPurchaseFragmentState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.SettingsFragmentState;
import com.philips.platform.modularui.stateimpl.SupportFragmentState;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class HomeActivityPresenterTest extends ApplicationTestCase<AppFrameworkApplication> {
    Context context;
    HomeActivityPresenter mHomeActivityPresenter;
    AppFrameworkApplication mAppFrameworkApplication;
    UIFlowManager mFlowManager;
    final int UI_HOME_FRAGMENT_STATE_CHANGE = 0;
    final int UI_SUPPORT_FRAGMENT_STATE_CHANGE = 1;
    final int UI_IAP_SHOPPING_FRAGMENT_STATE_CHANGE = 2;
    final int UI_DEBUG_FRAGMENT_STATE_CHANGE = 3;

    public HomeActivityPresenterTest(Class<AppFrameworkApplication> applicationClass) {
        super(AppFrameworkApplication.class);
    }

    public HomeActivityPresenterTest() {
        super(AppFrameworkApplication.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mHomeActivityPresenter = new HomeActivityPresenter();

        createApplication();
        context = getApplication();
        setContext(context);

        context = mock(Context.class);
        mAppFrameworkApplication = mock(AppFrameworkApplication.class);
        when(context.getApplicationContext()).thenReturn(mAppFrameworkApplication);

        mFlowManager = mock(UIFlowManager.class);
        when(mAppFrameworkApplication.getFlowManager()).thenReturn(mFlowManager);

    }

    @Test
    public void testClickOnHomeFragment() {
        HomeFragmentState homeFragmentState = new HomeFragmentState(UIState.UI_HOME_FRAGMENT_STATE);
        mHomeActivityPresenter.onClick(UI_SUPPORT_FRAGMENT_STATE_CHANGE, context);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .setCurrentState(homeFragmentState);
        mFlowManager.setCurrentState(homeFragmentState);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .getCurrentState();

        int stateID = mFlowManager.getCurrentState().getStateID();
        Assert.assertEquals(UIState.UI_HOME_FRAGMENT_STATE, stateID);
    }

    @Test
    public void testClickSupport() {
        SupportFragmentState supportFragmentState = new SupportFragmentState(UIState.UI_SUPPORT_FRAGMENT_STATE);
        mHomeActivityPresenter.onClick(UI_SUPPORT_FRAGMENT_STATE_CHANGE, context);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .setCurrentState(supportFragmentState);
        mFlowManager.setCurrentState(supportFragmentState);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .getCurrentState();

        int stateID = mFlowManager.getCurrentState().getStateID();
        Assert.assertEquals(UIState.UI_SUPPORT_FRAGMENT_STATE, stateID);
    }

    @Test
    public void testClickIAP() {
        InAppPurchaseFragmentState inAppPurchaseFragmentState = new InAppPurchaseFragmentState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE);
        mHomeActivityPresenter.onClick(UI_IAP_SHOPPING_FRAGMENT_STATE_CHANGE, context);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .setCurrentState(inAppPurchaseFragmentState);
        mFlowManager.setCurrentState(inAppPurchaseFragmentState);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .getCurrentState();

        int stateID = mFlowManager.getCurrentState().getStateID();
        Assert.assertEquals(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE, stateID);
    }

    @Test
    public void testClickSettings() {
        SettingsFragmentState mSettingsFragmentState = new SettingsFragmentState(UIState.UI_SETTINGS_FRAGMENT_STATE);
        mHomeActivityPresenter.onClick(UI_SUPPORT_FRAGMENT_STATE_CHANGE, context);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .setCurrentState(mSettingsFragmentState);
        mFlowManager.setCurrentState(mSettingsFragmentState);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .getCurrentState();

        int stateID = mFlowManager.getCurrentState().getStateID();
        Assert.assertEquals(UIState.UI_SETTINGS_FRAGMENT_STATE, stateID);
    }

    @Test
    public void testClickDebug() {
        DebugTestFragmentState mDebugTestFragmentState = new DebugTestFragmentState(UIState.UI_DEBUG_FRAGMENT_STATE);
        mHomeActivityPresenter.onClick(UI_DEBUG_FRAGMENT_STATE_CHANGE, context);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .setCurrentState(mDebugTestFragmentState);
        mFlowManager.setCurrentState(mDebugTestFragmentState);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .getCurrentState();

        int stateID = mFlowManager.getCurrentState().getStateID();
        Assert.assertEquals(UIState.UI_DEBUG_FRAGMENT_STATE, stateID);
    }

    @Test
    public void setNextStateTest() {
        ProductRegistrationState productRegistrationState = new ProductRegistrationState(UIState.UI_PROD_REGISTRATION_STATE);
        mHomeActivityPresenter.setNextState(context);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .setCurrentState(productRegistrationState);
        mFlowManager.setCurrentState(productRegistrationState);

        doAnswer(new CallsRealMethods()).when(mFlowManager)
                .getCurrentState();

        int stateID = mFlowManager.getCurrentState().getStateID();
        Assert.assertEquals(UIState.UI_PROD_REGISTRATION_STATE, stateID);
    }

}