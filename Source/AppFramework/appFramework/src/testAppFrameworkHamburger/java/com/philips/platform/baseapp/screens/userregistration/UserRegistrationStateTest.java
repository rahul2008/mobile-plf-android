package com.philips.platform.baseapp.screens.userregistration;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.AppStateConfiguration;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

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
import static org.robolectric.RuntimeEnvironment.application;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userRegState = new UserRegistrationStateMock(AppStates.SETTINGS_REGISTRATION);
        activityController = Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity = activityController.create().start().get();
        fragmentLauncher = new FragmentLauncher(hamburgerActivity, R.id.frame_container, hamburgerActivity);
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

    class UserRegistrationStateMock extends UserRegistrationState{

        private AppStateConfiguration configuration;

        private FragmentLauncher fragmentLauncher;

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

    }
}