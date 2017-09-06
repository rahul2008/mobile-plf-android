package com.philips.platform.baseapp.screens.userregistration;

import android.content.Intent;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.aboutscreen.AboutScreenState;
import com.philips.platform.baseapp.screens.termsandconditions.TermsAndConditionsState;
import com.philips.platform.baseapp.screens.termsandconditions.WebViewActivity;
import com.philips.platform.baseapp.screens.utility.AppStateConfiguration;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

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
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import java.util.Map;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SECRET;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;
import static com.philips.platform.baseapp.screens.userregistration.UserRegistrationState.CHINA_CODE;
import static com.philips.platform.baseapp.screens.userregistration.UserRegistrationState.DEFAULT;
import static com.philips.platform.baseapp.screens.userregistration.UserRegistrationState.DEVELOPMENT_SECRET_KEY_DEFAULT;
import static com.philips.platform.baseapp.screens.userregistration.UserRegistrationState.STAGE_SECRET_KEY_CHINA;
import static com.philips.platform.baseapp.screens.userregistration.UserRegistrationState.TEST_SECRET_KEY_DEFAULT;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by philips on 06/09/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class UserRegistrationStateTest {



    private ActivityController<TestActivity> activityController;

    private HamburgerActivity hamburgerActivity;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private FragmentLauncher fragmentLauncher;

    UserRegistrationStateMock userRegState;

    @Mock
    AppFrameworkApplication appFrameworkApplication;

    @Mock
    FlowManager flowManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userRegState = new UserRegistrationStateMock(AppStates.SETTINGS_REGISTRATION);
        activityController = Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity = activityController.create().start().get();
        fragmentLauncher = new FragmentLauncher(hamburgerActivity, R.id.frame_container, hamburgerActivity);
        userRegState.navigate(fragmentLauncher);
        when(appFrameworkApplication.getTargetFlowManager()).thenReturn(flowManager);
        when(flowManager.getCurrentState()).thenReturn(new AboutScreenState());
        when(flowManager.getNextState(any(BaseState.class),any(String.class))).thenReturn(new TermsAndConditionsState());
    }

    @Test
    public void testStageConfig(){
        userRegState.setConfiguration(AppStateConfiguration.STAGING);
        userRegState.init(application);
        AppInfraInterface appInfra = ((AppFrameworkApplication) application).getAppInfra();
        AppConfigurationInterface appConfigurationInterface = appInfra.getConfigInterface();
        Map<String,String> map= (Map<String, String>) appConfigurationInterface.getPropertyForKey(HSDP_CONFIGURATION_SECRET,UR,new AppConfigurationInterface.AppConfigurationError());
        assertEquals(STAGE_SECRET_KEY_CHINA,map.get(CHINA_CODE));
    }

    @Test
        public void testDevConfig(){
            userRegState.setConfiguration(AppStateConfiguration.DEVELOPMENT);
            userRegState.init(application);
            AppInfraInterface appInfra = ((AppFrameworkApplication) application).getAppInfra();
            AppConfigurationInterface appConfigurationInterface = appInfra.getConfigInterface();
            Map<String,String> map= (Map<String, String>) appConfigurationInterface.getPropertyForKey(HSDP_CONFIGURATION_SECRET,UR,new AppConfigurationInterface.AppConfigurationError());
            assertEquals(DEVELOPMENT_SECRET_KEY_DEFAULT,map.get(DEFAULT));
    }


    @Test
    public void testTestConfig(){
        userRegState.setConfiguration(AppStateConfiguration.TEST);
        userRegState.init(application);
        AppInfraInterface appInfra = ((AppFrameworkApplication) application).getAppInfra();
        AppConfigurationInterface appConfigurationInterface = appInfra.getConfigInterface();
        Map<String,String> map= (Map<String, String>) appConfigurationInterface.getPropertyForKey(HSDP_CONFIGURATION_SECRET,UR,new AppConfigurationInterface.AppConfigurationError());
        assertEquals(TEST_SECRET_KEY_DEFAULT,map.get(DEFAULT));
    }

    @Test
    public void getUserObject_NotNull(){
//        try {
//            userRegState.navigate(fragmentLauncher);
//        }
//        catch(Exception exception){
//        }
//        userRegState.onUserRegistrationComplete(hamburgerActivity);
        assertNotNull(userRegState.getUserObject(application));
    }

    @Test
    public void onPrivacyPolicyClickedTest(){
        userRegState.onPrivacyPolicyClick(hamburgerActivity);
        ShadowActivity shadowActivity=shadowOf(hamburgerActivity);
        Intent intent=shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent=shadowOf(intent);
        assertEquals(shadowIntent.getIntentClass().getSimpleName(), WebViewActivity.class.getSimpleName());
    }

    class UserRegistrationStateMock extends UserRegistrationState{

        private AppStateConfiguration configuration;

        /**
         * AppFlowState constructor
         *
         * @param stateID
         */
        public UserRegistrationStateMock(String stateID) {
            super(stateID);

        }

        @Override
        public AppStateConfiguration getConfiguration() {
            return configuration;
        }

        public void setConfiguration(AppStateConfiguration configuration){
            this.configuration=configuration;
        }

        @Override
        public void navigate(UiLauncher uiLauncher) {
            fragmentLauncher= (FragmentLauncher) uiLauncher;
        }

        @Override
        protected AppFrameworkApplication getApplicationContext() {
            return appFrameworkApplication;
        }
    }
}