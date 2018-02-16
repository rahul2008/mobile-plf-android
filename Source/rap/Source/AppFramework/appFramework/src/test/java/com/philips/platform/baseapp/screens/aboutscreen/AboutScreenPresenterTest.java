/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.aboutscreen;

import android.content.Context;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.screens.webview.WebViewState;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.uappframework.launcher.UiLauncher;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class AboutScreenPresenterTest {

    AboutScreenPresenterMock aboutScreenPresenterMock;


    @Mock
    AboutScreenContract.View view;

    @Mock
    FlowManager flowManager;

    @Mock
    WebViewState webViewState;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private ActivityController<TestActivity> activityController;

    private HamburgerActivity hamburgerActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activityController = Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity = activityController.create().start().get();
        aboutScreenPresenterMock = new AboutScreenPresenterMock(hamburgerActivity, view);
        when(flowManager.getNextState(any(BaseState.class),any(String.class))).thenReturn(webViewState);

    }

    @Test
    @Ignore
    public void loadTermsAndPrivacy() throws Exception {
        aboutScreenPresenterMock.loadTermsAndPrivacy(Constants.PRIVACY);
        verify(webViewState).navigate((UiLauncher)isNull());
    }

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        aboutScreenPresenterMock=null;
        view=null;
        flowManager=null;
        webViewState =null;
        hamburgerActivity=null;
    }

    class AboutScreenPresenterMock extends AboutScreenPresenter {

        public AboutScreenPresenterMock(Context context, AboutScreenContract.View view) {
            super(context, view);
        }

        @Override
        protected BaseFlowManager getTargetFlowManager() {
            return flowManager;
        }
    }
}