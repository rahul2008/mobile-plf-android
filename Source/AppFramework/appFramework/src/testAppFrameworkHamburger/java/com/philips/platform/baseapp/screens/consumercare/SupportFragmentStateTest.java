/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.consumercare;

import android.support.annotation.NonNull;

import com.philips.cdp.digitalcare.CcInterface;
import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.productregistration.ProductRegistrationState;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
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
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class SupportFragmentStateTest {

    private ActivityController<TestActivity> activityController;

    private HamburgerActivity hamburgerActivity;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private FragmentLauncher fragmentLauncher;

    SupportFragmentStateMock supportFragmentStateMock;

    @Mock
    AppFrameworkApplication appFrameworkApplication;

    @Mock
    FlowManager flowManager;

    @Mock
    CcInterface ccInterface;

    @Mock
    ProductRegistrationState productRegistrationState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        supportFragmentStateMock = new SupportFragmentStateMock();
        supportFragmentStateMock.init(null);
        activityController = Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity = activityController.create().start().get();
        fragmentLauncher = new FragmentLauncher(hamburgerActivity, R.id.frame_container, hamburgerActivity);
        UIStateData supportFragmentStateData = new UIStateData();
        supportFragmentStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
        supportFragmentStateMock.setUiStateData(supportFragmentStateData);
        supportFragmentStateMock.onProductMenuItemClicked(null);
        supportFragmentStateMock.onSocialProviderItemClicked(null);
    }


    @Test
    public void testLaunchCC(){
        supportFragmentStateMock.navigate(fragmentLauncher);
        verify(ccInterface).launch(any(UiLauncher.class),any(UappLaunchInput.class));
    }

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        hamburgerActivity=null;
        flowManager=null;
        ccInterface=null;
        productRegistrationState=null;
        appFrameworkApplication=null;
        fragmentLauncher=null;
    }

    @Test
    public void testMainMenuItemClicked(){
        supportFragmentStateMock.navigate(fragmentLauncher);
        when(appFrameworkApplication.getTargetFlowManager()).thenReturn(flowManager);
        when(flowManager.getNextState(any(BaseState.class),any(String.class))).thenReturn(productRegistrationState);
        supportFragmentStateMock.onMainMenuItemClicked("RA_Product_Registration_Text");
        verify(productRegistrationState).navigate(any(UiLauncher.class));

    }

    class SupportFragmentStateMock extends SupportFragmentState{
        @NonNull
        @Override
        protected CcInterface getCcInterface() {
            return ccInterface;
        }

        @Override
        public AppFrameworkApplication getApplicationContext() {
            return appFrameworkApplication;
        }
        @Override
        public void updateDataModel() {
            RALog.d(TAG," updateDataModel called ");
            String[] ctnList = new String[]{"HX6322/04"};
            setCtnList(ctnList);
        }

    }
}